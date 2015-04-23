package org.uengine.kernel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.metaworks.annotation.Hidden;
import org.uengine.kernel.graph.Transition;
import org.uengine.processmanager.ProcessTransactionContext;

public class FlowActivity extends ComplexActivity {

	public static final String UN_COMPLETED_BRANCHES = "unCompletedBranches";
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	public ArrayList<Transition> transitions;
	@Hidden
		public ArrayList<Transition> getTransitions() {
			if (this.transitions == null) {
				this.setTransitions(new ArrayList<Transition>());
			}
			return transitions;
		}
		public void setTransitions(ArrayList<Transition> transitions) {
			this.transitions = transitions;
		}
		public void addTransition(Transition trasition) {
			this.getTransitions().add(trasition);
		}

	@Override
	public void afterDeserialization() {
		super.afterDeserialization();

		Transition transition = null;
		if(transitions!=null){
			for (Iterator<Transition> it = transitions.iterator(); it.hasNext();) {
				transition = it.next();
	
				// source
                String source = transition.getSource();
                Activity sourceActivity = getProcessDefinition().getActivity(source);
                sourceActivity.addOutgoingTransition(transition);
                transition.setSourceActivity(sourceActivity);
    
                // target
                String target = transition.getTarget();
                Activity targetActivity = getProcessDefinition().getActivity(target);
                targetActivity.addIncomingTransition(transition);
                transition.setTargetActivity(targetActivity);
			}
		}
	}

	private Activity getStartActivity() throws UEngineException {
		Activity child = null;
		// TODO 프로세스를 퍼블리싱하여 제공할때는 이벤트로 프로세스가 시작이 될수가 있다 이때를 위한 부분을 처리해야함
		for (Iterator it = getChildActivities().iterator(); it.hasNext();) {
			child = (Activity) it.next();
			if( child.getOutgoingTransitions().size() == 0 && child.getIncomingTransitions().size() == 0){
				// null 로 리턴될 경우 super 로직을 태움
				continue;
//				return null;
			}else	if (child.getIncomingTransitions().size() == 0) {
				if( child instanceof EventActivity && child instanceof MessageListener ){
					continue;
				}
				return child;
			}
		}

		// inconsistent xml or miss starting point
		// it should be never triggered
		UEngineException exception = new UEngineException("inconsistent xml or miss starting point");
		exception.setErrorLevel(UEngineException.FATAL);
		throw exception;
	}

	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
		//TODO: find out events and register them as event listeners as follows:
		// for each events:   getProcessDefinition().addMessageListener(instance, eventActivity);
		for(Activity childActivity: getChildActivities()){
			if(childActivity instanceof EventActivity && childActivity instanceof MessageListener 
					&& (childActivity.getIncomingTransitions() == null || childActivity.getIncomingTransitions().size() == 0)){
				getProcessDefinition().addMessageListener(instance, (MessageListener)childActivity);
			}
		}
		if (getTransitions().size() == 0) {
//			System.out.println("This is conventional uengine process - 2");
			super.executeActivity(instance);
		}else{
			Activity startActivity = getStartActivity();
			if (startActivity != null) {
				queueActivity(startActivity, instance);
			} else {
				throw new UEngineException("Can't find start activity");
			}
		}
		

	}

	@Override
	protected void onEvent(String command, ProcessInstance instance, Object payload) throws Exception {
		
		if (getTransitions().size() == 0) {
//			System.out.println("This is conventional uengine process - 1");
			super.onEvent(command, instance, payload);
			
			return;
		}
		
		
		if (command.equals(CHILD_DONE)) {

			// when we finish??
			// boolean stillRunning = false;
			Activity currentActivity = (Activity) payload;
			List<Activity> possibleNextActivities = currentActivity.getPossibleNextActivities(instance, "");
			
			if (possibleNextActivities.size() == 0) {
				// fireComplete(instance);
				 if( !currentActivity.checkStartsWithEventActivity() ){
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

//			if(instance.getProcessTransactionContext().getSharedContext(UN_COMPLETED_BRANCHES)==null){
//				instance.getProcessTransactionContext().setSharedContext(UN_COMPLETED_BRANCHES, new Integer(0));
//			}

//			Integer unCompletedBranches = (Integer) instance.getProcessTransactionContext().getSharedContext(UN_COMPLETED_BRANCHES);
//			unCompletedBranches++;
//			instance.getProcessTransactionContext().setSharedContext(UN_COMPLETED_BRANCHES, unCompletedBranches);
//
//
			for (int i = 0; i < possibleNextActivities.size(); i++) {
				queueActivity(possibleNextActivities.get(i), instance);
			}
//
//			unCompletedBranches = (Integer) instance.getProcessTransactionContext().getSharedContext(UN_COMPLETED_BRANCHES);
//			unCompletedBranches--;
//			instance.getProcessTransactionContext().setSharedContext(UN_COMPLETED_BRANCHES, unCompletedBranches);

		}else if(command.equals(ACTIVITY_DONE)){
			
			setStatus(instance, STATUS_COMPLETED);
			//review: Ensure subclasses are not overrided this method.
			afterComplete(instance);
			
			if(!isFaultTolerant() && getParentActivity()!=null)
				getParentActivity().onEvent(CHILD_DONE, instance, this);
		}else if(command.equals(CHILD_STOPPED)){
			System.out.println(command);
		}else{
//			onEvent(command, instance, payload);
		}
	}
		
	@Override
	public ActivityReference getInitiatorHumanActivityReference(
			ProcessTransactionContext ptc) {
		// TODO 처음이 휴먼 액티비티가 아닐수 있기때문에, 제일 처음 나오는 휴먼엑티비티를 찾아야함
		
		// transition 이 없으면 super 로직을 태움
		ActivityReference startActivityRef = new ActivityReference();
		try {
			Activity act = getStartActivity();
			if( act == null){
				startActivityRef = super.getInitiatorHumanActivityReference(ptc);
			}else{
				if( act instanceof HumanActivity){
					startActivityRef.setActivity(act);
					startActivityRef.setAbsoluteTracingTag(act.getTracingTag());
				}else{
					Activity nextActivity = this.findNextHumanActivity(act);
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
		List<Transition> transitionList = act.getOutgoingTransitions();
		Activity returnActiviy = null;
		if( transitionList != null ){
			for(Transition ts : transitionList){
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
