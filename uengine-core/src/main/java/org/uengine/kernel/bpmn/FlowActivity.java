package org.uengine.kernel.bpmn;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.metaworks.annotation.Hidden;
import org.uengine.five.framework.ProcessTransactionListener;
import org.uengine.kernel.Activity;
import org.uengine.kernel.ActivityEventListener;
import org.uengine.kernel.ActivityReference;
import org.uengine.kernel.ComplexActivity;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.MessageListener;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.TransactionListener;
import org.uengine.kernel.UEngineException;
import org.uengine.processmanager.ProcessTransactionContext;
import org.uengine.processmanager.TransactionContext;
import org.uengine.util.TreeVisitor;

public class FlowActivity extends ComplexActivity {

	public static final String UN_COMPLETED_BRANCHES = "unCompletedBranches";

	@Override
	public void beforeSerialization() {
		super.beforeSerialization();

		if(sequenceFlows !=null)
		for(SequenceFlow sequenceFlow : getSequenceFlows()){
			sequenceFlow.beforeSerialization();
		}
	}



	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	public ArrayList<SequenceFlow> sequenceFlows;
	@Hidden
		public ArrayList<SequenceFlow> getSequenceFlows() {
			if (this.sequenceFlows == null) {
				this.setSequenceFlows(new ArrayList<SequenceFlow>());
			}
			return sequenceFlows;
		}
		public void setSequenceFlows(ArrayList<SequenceFlow> sequenceFlows) {
			this.sequenceFlows = sequenceFlows;
		}
		public void addSequenceFlow(SequenceFlow trasition) {
			this.getSequenceFlows().add(trasition);
		}

	@Override
	public void afterDeserialization() {
		super.afterDeserialization();

		//clear the incoming / outgoing sequence flows firstly.
		for(Activity activity : getChildActivities()){
			activity.setIncomingSequenceFlows(new ArrayList<SequenceFlow>());
			activity.setOutgoingSequenceFlows(new ArrayList<SequenceFlow>());
		}

		SequenceFlow sequenceFlow = null;
		if(sequenceFlows !=null){
			for (Iterator<SequenceFlow> it = sequenceFlows.iterator(); it.hasNext();) {
				sequenceFlow = it.next();
	
				// source
                String source = sequenceFlow.getSourceRef();

				if(source!=null) {
					Activity sourceActivity = getProcessDefinition().getActivity(source);

					if (sourceActivity != null) {
						if(!sourceActivity.getOutgoingSequenceFlows().contains(sequenceFlow)){
							sourceActivity.addOutgoingTransition(sequenceFlow);
							sequenceFlow.setSourceActivity(sourceActivity);
						}else{
							System.out.println("duplicated SequenceFlow in FlowActivity " + getName());
						}
					}
				}
    
                // target
                String target = sequenceFlow.getTargetRef();

				if(target!=null){
					Activity targetActivity = getProcessDefinition().getActivity(target);

					if(targetActivity!=null) {
						if(!targetActivity.getIncomingSequenceFlows().contains(sequenceFlow)){
							targetActivity.addIncomingTransition(sequenceFlow);
							sequenceFlow.setTargetActivity(targetActivity);
						}else{
							System.out.println("duplicated SequenceFlow in FlowActivity " + getName());
						}
					}
				}

				sequenceFlow.afterDeserialization();
			}
		}

		// for each events:   getProcessDefinition().addMessageListener(instance, eventActivity);
		for(Activity childActivity: getChildActivities()){
			if(childActivity instanceof Event && "Catching".equals(((Event)childActivity).getEventType())
					&& (childActivity.getIncomingSequenceFlows() == null || childActivity.getIncomingSequenceFlows().size() == 0)){

				final Event event = (Event)childActivity;

				if(event.getAttachedToRef() != null){
					getProcessDefinition().getActivity(event.getAttachedToRef())
							.addEventListener(
									new ActivityEventListener() {
										@Override
										public void afterExecute(Activity activity, ProcessInstance instance) throws Exception {
											getProcessDefinition().addMessageListener(instance, event);
											queueActivity(event, instance);
										}

										@Override
										public void afterComplete(Activity activity, ProcessInstance instance) throws Exception {
											getProcessDefinition().removeMessageListener(instance, event);
										}
									}
							);
				}

			}
		}



	}

	private List<Activity> getStartActivities() throws UEngineException {

		List<Activity> startActivities = new ArrayList<Activity>();

		//if the model is old version, returns the first child in the order.  [changed] the danggling activties would be all the startable activities each other without start event.
//		if(getSequenceFlows()==null || getSequenceFlows().size()==0 && getChildActivities().size() > 0){
//			startActivities.add(getChildActivities().get(0));
//
//			return startActivities;
//		}

		Activity child = null;
		// TODO 프로세스를 퍼블리싱하여 제공할때는 이벤트로 프로세스가 시작이 될수가 있다 이때를 위한 부분을 처리해야함
		for (Iterator it = getChildActivities().iterator(); it.hasNext();) {
			child = (Activity) it.next();
			if (child.getIncomingSequenceFlows().size() == 0) {

				if(child instanceof Event && !(child instanceof StartEvent) && ((Event) child).getAttachedToRef()!=null){ //attachted to 가 있다는 이야기는 조건 이벤트가 왔을때만 실행하는 것이기 때문에 Start 로 인식하면 안된다.
					continue;
				}

				if(this instanceof ProcessDefinition && !(getProcessDefinition().isRunAllStartableActivities()) && !(child instanceof StartEvent))
					continue;

				startActivities.add(child);
			}
		}

		if(startActivities.size()>0){
			return startActivities;
		}

		// inconsistent xml or miss starting point
		// it should be never triggered

//		if(getSequenceFlows()!=null && getSequenceFlows().size()>0) {
//			UEngineException exception = new UEngineException("inconsistent process model - missing starting activity");
//			exception.setErrorLevel(UEngineException.FATAL);
//			throw exception;
//		}

		return null;
	}

	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
		//TODO: find out events and register them as event listeners as follows:
		// for each events:   getProcessDefinition().addMessageListener(instance, eventActivity);
		for(Activity childActivity: getChildActivities()){
			if(childActivity instanceof Event && childActivity instanceof MessageListener
					&& (childActivity.getIncomingSequenceFlows() == null || childActivity.getIncomingSequenceFlows().size() == 0)){

				getProcessDefinition().addMessageListener(instance, (MessageListener)childActivity);
			}
		}


//		if (getSequenceFlows().size() == 0) {
////			System.out.println("This is conventional uengine process - 2");
//			super.executeActivity(instance);
//		}else{
			List<Activity> startActivities = getStartActivities();
			if (startActivities!=null && startActivities.size() > 0) {

				//Execute Event activity first than the regular activities since the first activity may occur any events.
				for(Activity startActivity : startActivities) {
					if(startActivity instanceof Event)
						queueActivity(startActivity, instance);
				}

				for(Activity startActivity : startActivities) {
					if(!(startActivity instanceof Event))
						queueActivity(startActivity, instance);
				}
			} else {
				fireComplete(instance);  //throw new UEngineException("Can't find start activity");
			}
//		}
		

	}

	@Override
	protected void onEvent(String command, final ProcessInstance instance, Object payload) throws Exception {

		if (command.equals(CHILD_DONE)) {

			//if adhoc instance, don't continue to execute process
			Boolean adhoc = (Boolean) instance.getProperty("","__adhoc");
			if (adhoc!=null && adhoc) {
			    return;
			}

			// when we finish??
			// boolean stillRunning = false;
			final Activity currentActivity = (Activity) payload;
			List<Activity> possibleNextActivities = currentActivity.getPossibleNextActivities(instance, "");
			
            if (possibleNextActivities.size() == 0) {
                
                /*
                 * 1 -> G -> 2
                 *      |
                 *      ---> 3 -> 4  
                 * 2의 TracTag가 3보다 작을 경우 2가 먼저 실행됨
                 * 2가 시스템 Task(또는 Event)일 경우 2 수행 이후 ProcessDefinition의 fireComplete가 호출되면서 main으로 복귀함
                 * G가 Parallel Gateway일 경우 2수행 이후 3이 수행되기 때문에
                 * main으로 복귀할 경우 main의 다음 Task와 Task 3이 동시에 발생함
                 * 본 문제를 해결하기 위해 ProcessDefinition의 fireComplete는 ProcessTransactionContext의 beforeCommit시 처리하도록 수정함으로서
                 * 3까지 수행된 이후에 최종 수행중인 Task가 있는지 확인하여 처리함
                 * 2018.01.02 Leehc
                 */
                final FlowActivity finalThis = this;
                ProcessTransactionListener tl = new ProcessTransactionListener() {
                    
                    @Override
                    public void beforeRollback(org.uengine.five.framework.ProcessTransactionContext tx) throws Exception {
                    }
                    
                    @Override
                    public void beforeCommit(org.uengine.five.framework.ProcessTransactionContext tx) throws Exception {
                        // 프로세스의 경우 실행중인 Activity가 없을 경우에만 종료
                        boolean completeAvail = true;
                        if (finalThis instanceof ProcessDefinition) {
                            List<Activity> list = instance.getCurrentRunningActivities();
                            if (list.size() != 0) {
                                completeAvail = false;
                            }                    
                        }
                        
                        if (completeAvail) {                    
                            if (!currentActivity.checkStartsWithBoundaryEventActivity()) {
                                setStatus(instance, STATUS_COMPLETED);
                                fireComplete(instance);
                            }
                        }
                    }
                    
                    @Override
                    public void afterRollback(org.uengine.five.framework.ProcessTransactionContext tx) throws Exception {
                    }
                    
                    @Override
                    public void afterCommit(org.uengine.five.framework.ProcessTransactionContext tx) throws Exception {
                    }
                };
                
                org.uengine.five.framework.ProcessTransactionContext.getThreadLocalInstance().addTransactionListener(tl);
            }
                
			// register token before queueActivity()
			for (int i = 0; i < possibleNextActivities.size(); i++) {
				Activity child = possibleNextActivities.get(i);
				
				child.reset(instance);
				child.setStartedTime(instance, (Calendar)null);
				
				int tokenCount = child.getTokenCount(instance);
				child.setTokenCount(instance, ++tokenCount);
			}

			for (int i = 0; i < possibleNextActivities.size(); i++) {
				Activity activity = possibleNextActivities.get(i);

				// if there are no gateway but if it look like a join, apply inclusive gateway.
				if(activity.getIncomingSequenceFlows().size() == 1 || !Gateway.hasTokenInPreviousActivities(instance, activity))
					queueActivity(activity, instance);
			}

		}else if(command.equals(ACTIVITY_DONE)){
			
			setStatus(instance, STATUS_COMPLETED);
			//review: Ensure subclasses are not overrided this method.
			afterComplete(instance);
			
			if(!isFaultTolerant() && getParentActivity()!=null)
				notifyCompletionToParent(instance);
		}else if(command.equals(CHILD_STOPPED)){
			System.out.println(command);
		}else if(command.equals(CHILD_RESUMED)){
			super.onEvent(command, instance, payload);
		}else if(command.equals(CHILD_FAULT) || command.equals(ACTIVITY_FAULT)){
			super.onEvent(command, instance, payload);
		}
	}
		
	@Override
	public ActivityReference getInitiatorHumanActivityReference(
			ProcessTransactionContext ptc) {
		// TODO 처음이 휴먼 액티비티가 아닐수 있기때문에, 제일 처음 나오는 휴먼엑티비티를 찾아야함
		
		// transition 이 없으면 super 로직을 태움
		ActivityReference startActivityRef = new ActivityReference();
		try {
			List<Activity> act = getStartActivities();
			if( act == null || act.size()==0){
				startActivityRef = super.getInitiatorHumanActivityReference(ptc);
			}else{

				//TODO implement this
				Activity firstStartActivity = act.get(0);

				if( firstStartActivity instanceof HumanActivity){
					startActivityRef.setActivity(firstStartActivity);
					startActivityRef.setAbsoluteTracingTag(firstStartActivity.getTracingTag());
				}else{
					Activity nextActivity = this.findNextHumanActivity(firstStartActivity);
					if( nextActivity != null ){
						startActivityRef.setActivity(nextActivity);
						startActivityRef.setAbsoluteTracingTag(nextActivity.getTracingTag());
					}
				}
			}
		} catch (UEngineException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
		return startActivityRef;
	}
	
	public Activity findNextHumanActivity(Activity act) {
		List<SequenceFlow> sequenceFlowList = act.getOutgoingSequenceFlows();
		Activity returnActiviy = null;
		if( sequenceFlowList != null ){
			for(SequenceFlow ts : sequenceFlowList){
				if( ts.getTargetActivity() instanceof HumanActivity){
					return ts.getTargetActivity();
				}else{
					act.setChecked(true);
					if( !ts.getTargetActivity().isChecked() ){
						returnActiviy = this.findNextHumanActivity(ts.getTargetActivity());
					}
				}
			}
		}
		return returnActiviy;
	}


	@Override
	protected void gatherPropagatedActivitiesOf(final ProcessInstance instance, Activity child, List list) throws Exception{

		final List<Activity> propagatedActivities = new ArrayList<Activity>();

		new TreeVisitor<Activity>(){

			@Override
			public List<Activity> getChild(Activity parent) {
				List<SequenceFlow> outgoings = parent.getOutgoingSequenceFlows();

				List<Activity> outgoingActivities = new ArrayList<Activity>();

				for(SequenceFlow sequenceFlow : outgoings){
					outgoingActivities.add(sequenceFlow.getTargetActivity());
				}

				return outgoingActivities;
			}

			@Override
			public void logic(Activity elem) {
				try {
					if(!Activity.STATUS_READY.equals(elem.getStatus(instance))) {
                        propagatedActivities.add(elem);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.run(child);

		list.addAll(propagatedActivities);


	}

}
