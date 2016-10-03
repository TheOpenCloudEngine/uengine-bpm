package org.uengine.kernel.bpmn;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.metaworks.annotation.Hidden;
import org.uengine.kernel.*;
import org.uengine.processmanager.ProcessTransactionContext;

public class FlowActivity extends ComplexActivity {

	public static final String UN_COMPLETED_BRANCHES = "unCompletedBranches";
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

		SequenceFlow sequenceFlow = null;
		if(sequenceFlows !=null){
			for (Iterator<SequenceFlow> it = sequenceFlows.iterator(); it.hasNext();) {
				sequenceFlow = it.next();
	
				// source
                String source = sequenceFlow.getSourceRef();

				if(source!=null) {
					Activity sourceActivity = getProcessDefinition().getActivity(source);

					if (sourceActivity != null) {
						sourceActivity.addOutgoingTransition(sequenceFlow);
						sequenceFlow.setSourceActivity(sourceActivity);
					}
				}
    
                // target
                String target = sequenceFlow.getTargetRef();

				if(target!=null){
					Activity targetActivity = getProcessDefinition().getActivity(target);

					if(targetActivity!=null) {
						targetActivity.addIncomingTransition(sequenceFlow);
						sequenceFlow.setTargetActivity(targetActivity);
					}
				}
			}
		}

		// for each events:   getProcessDefinition().addMessageListener(instance, eventActivity);
		for(Activity childActivity: getChildActivities()){
			if(childActivity instanceof Event && "Catching".equals(((Event)childActivity).getEventType())
					&& (childActivity.getIncomingSequenceFlows() == null || childActivity.getIncomingSequenceFlows().size() == 0)){

				final Event event = (Event)childActivity;
				if(event.getAttachedToRef() != null){
					getProcessDefinition().getActivity(event.getAttachedToRef()).addEventListener(
							new ActivityEventListener(){
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

				////// TODO: why does following implementation try to ignore event activities as a start activity?
//				if( child instanceof Event && child instanceof MessageListener ){
//					continue;
//				}
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
				for(Activity startActivity : startActivities) {
					queueActivity(startActivity, instance);
				}
			} else {
				fireComplete(instance);  //throw new UEngineException("Can't find start activity");
			}
//		}
		

	}

	@Override
	protected void onEvent(String command, ProcessInstance instance, Object payload) throws Exception {

		if (command.equals(CHILD_DONE)) {

			// when we finish??
			// boolean stillRunning = false;
			Activity currentActivity = (Activity) payload;
			List<Activity> possibleNextActivities = currentActivity.getPossibleNextActivities(instance, "");
			
			if (possibleNextActivities.size() == 0) {
				// fireComplete(instance);
				 if( !currentActivity.checkStartsWithBoundaryEventActivity() ){
					 setStatus(instance, STATUS_COMPLETED);

					 fireComplete(instance);

				 }
				 // change the status to be completed 
				 //after the completion of all the activities
				 if (instance != null && instance.isSubProcess()) {
					instance.getProcessDefinition().returnToMainProcess(instance);
				 }
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
				queueActivity(possibleNextActivities.get(i), instance);
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
		}else {
			//onEvent(command, instance, payload);
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
			if( act == null){
				startActivityRef = super.getInitiatorHumanActivityReference(ptc);
			}else{

				//TODO implement this
//
//				if( act instanceof HumanActivity){
//					startActivityRef.setActivity(act);
//					startActivityRef.setAbsoluteTracingTag(act.getTracingTag());
//				}else{
//					Activity nextActivity = this.findNextHumanActivity(act);
//					if( nextActivity != null ){
//						startActivityRef.setActivity(nextActivity);
//						startActivityRef.setAbsoluteTracingTag(nextActivity.getTracingTag());
//					}
//				}
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
}
