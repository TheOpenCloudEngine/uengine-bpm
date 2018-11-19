package org.uengine.kernel;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.metaworks.Type;
import org.metaworks.annotation.Hidden;
import org.uengine.kernel.bpmn.FlowActivity;
import org.uengine.util.UEngineUtil;


public class ScopeActivity extends FlowActivity implements MessageListener{

	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public static void metaworksCallback_changeMetadata(Type type){
		//type.setName((String)ProcessDesigner.getInstance().getActivityTypeNameMap().get(ScopeActivity.class));
	}

	ProcessVariable[] processVariableDescriptors;

	transient Hashtable processVariableDescriptorsHT;
		@Hidden
		public ProcessVariable[] getProcessVariables(){
			return processVariableDescriptors;
		}		
		@Hidden
		public ProcessVariable getProcessVariable(String pvName) {

			if(pvName==null)
				return null;

			if(pvName.startsWith("[roles].")){
				ProcessVariable variable = ProcessVariable.forName(pvName);
				variable.setTypeClassName(RoleMapping.class.getName());

				return variable;
			}

			String pvNameLower = pvName.toLowerCase();
			if(pvName.indexOf('.') > -1){
				String[] parts = pvNameLower.replace('.','@').split("@");
				pvName = parts[0];
			}
			
			if(processVariableDescriptorsHT==null && getProcessVariables()!=null){
				setProcessVariables(getProcessVariables()); //cache process variables
			}
			
			if(processVariableDescriptorsHT!=null && processVariableDescriptorsHT.containsKey(pvNameLower)){
				return (ProcessVariable)processVariableDescriptorsHT.get(pvNameLower);
			}

			if(processVariableDescriptorsHT!=null && processVariableDescriptorsHT.containsKey(pvName)){
				return (ProcessVariable)processVariableDescriptorsHT.get(pvName);
			}

			return null;
		}
		public void setProcessVariables(ProcessVariable[] pvds){
			this.processVariableDescriptors = pvds;
			alignProcessVariables();
		}

	private void alignProcessVariables() {
		processVariableDescriptorsHT = new Hashtable();
		if( processVariableDescriptors != null ){
            for(int i=0; i<processVariableDescriptors.length; i++)
                processVariableDescriptorsHT.put(
                    processVariableDescriptors[i].getName().toLowerCase(),
                    processVariableDescriptors[i]
                );
            firePropertyChangeEvent(new PropertyChangeEvent(this, "processVariables", processVariableDescriptors, processVariableDescriptors));
        }
	}

	EventHandlerPanel eventHandlerPanel;

	EventHandler[] eventHandlers;
		@Hidden
		public EventHandler[] getEventHandlers() {
			return eventHandlers;
		}
		public void setEventHandlers(EventHandler[] eventHandlers) {
			
			this.eventHandlers = eventHandlers;
			if( eventHandlers != null ){
				for(int i=0; i<eventHandlers.length; i++){
					Activity eventHandlingActivity = eventHandlers[i].getHandlerActivity(); 
					
					eventHandlingActivity.setParentActivity(this);
					autoTag(eventHandlingActivity);
					
					if(getProcessDefinition()!=null)
						getProcessDefinition().registerActivity(eventHandlingActivity);
				}			
			}
		}	
	
	boolean leaveEventListenersEvenIfOutOfScope;
		public boolean isLeaveEventListenersEvenIfOutOfScope() {
			return leaveEventListenersEvenIfOutOfScope;
		}
		public void setLeaveEventListenersEvenIfOutOfScope(
				boolean leaveEventListenersEvenIfOutOfScope) {
			this.leaveEventListenersEvenIfOutOfScope = leaveEventListenersEvenIfOutOfScope;
		}

	boolean isCollapsed;
	@Hidden
		public boolean isCollapsed() {
			return isCollapsed;
		}
		public void setCollapsed(boolean isCollapsed) {
			this.isCollapsed = isCollapsed;
		}
		
	public ScopeActivity(){
		setName("");
	}
	
	public void afterDeserialization() {
		ProcessVariable [] processVariables = getProcessVariables();
		
		if(processVariables!=null)
		for(int i=0; i<processVariables.length; i++){
			processVariables[i].afterDeserialization();			
		}
		
		EventHandler[] eventHandlers = getEventHandlers();
		
		if(eventHandlers!=null)
		for(int i=0; i<getEventHandlers().length; i++ ){			
			Activity child = (Activity)eventHandlers[i].getHandlerActivity();
			child.setParentActivity(this);
			
			if(child instanceof NeedArrangementToSerialize)
				((NeedArrangementToSerialize)child).afterDeserialization();		
		}

		super.afterDeserialization();
	}

	@Override
	public void beforeSerialization() {

		alignProcessVariables();

		ProcessVariable [] processVariables = getProcessVariables();

		if(processVariables!=null)
			for(int i=0; i<processVariables.length; i++){
				processVariables[i].beforeSerialization();
			}

		super.beforeSerialization();
	}

	protected void beforeExecute(ProcessInstance instance) throws Exception {


		super.beforeExecute(instance);
		if(getEventHandlers()!=null && getEventHandlers().length > 0) 
			getProcessDefinition().addMessageListener(instance, this);
		
		
		EventHandler[] eventHandlers = getEventHandlers();

		if(eventHandlers!=null){
			boolean faultHandlerExists = false;
			for(int i=0; i<eventHandlers.length; i++){
				if(eventHandlers[i].getTriggeringMethod() == EventHandler.TRIGGERING_BY_FAULT){
					faultHandlerExists = true;
					
					break;
				}
			}

			if(faultHandlerExists)
				instance.getProcessTransactionContext().setSharedContext("faultTolerant", true);
		}

	}
	
	protected void afterComplete(ProcessInstance instance) throws Exception {
		super.afterComplete(instance);
		if(!isLeaveEventListenersEvenIfOutOfScope())
		getProcessDefinition().removeMessageListener(instance, this);
	}
	
	public boolean fireEventHandlers(ProcessInstance instance, int triggerType, EventMessagePayload emp) throws Exception{
		boolean firedOnce = false;
		
		EventHandler[] eventHandlers = getEventHandlers();

		if(eventHandlers!=null){
			for(int i=0; i<eventHandlers.length; i++){
				if(eventHandlers[i].getTriggeringMethod() == triggerType){
					if(emp.getEventName()==null) emp.setEventName(eventHandlers[i].getName());

					//detect whether recursive call
					if(emp.getTriggerTracingTag()!=null){
						
						Activity originatorActivity = getProcessDefinition().getActivity(emp.getTriggerTracingTag());
						
						if(eventHandlers[i].getHandlerActivity() == originatorActivity 
								|| eventHandlers[i].getHandlerActivity().isAncestorOf(originatorActivity)){
							continue;
						}
					}
					
					onMessage(instance, emp);
					firedOnce = true;
				}
			}
		}
		return firedOnce;
	}
	
	public boolean onMessage(ProcessInstance instance, Object payload) throws Exception {
		if(payload instanceof EventMessagePayload){
			EventMessagePayload eventMessagePayload = (EventMessagePayload)payload;
			String eventName = eventMessagePayload.getEventName();
			
			EventHandler eh = getEventHandler(eventName);
					
			if(eh!=null){
				
				Activity handlerActivity = eh.getHandlerActivity();
				Activity triggerActivity = null;
				
				if(UEngineUtil.isNotEmpty(eventMessagePayload.getTriggerTracingTag())){
					triggerActivity = getProcessDefinition().getActivity(eventMessagePayload.getTriggerTracingTag());
				}else{
					List runningActivities = instance.getCurrentRunningActivities();
					if(runningActivities!=null && runningActivities.size() > 0)
						triggerActivity = (Activity)runningActivities.get(0);
				}
				
				ExecutionScopeContext esc = instance.issueNewExecutionScope(handlerActivity, triggerActivity, eh.getDisplayName().getText(GlobalContext.DEFAULT_LOCALE));
				instance.setExecutionScopeContext(esc);
				
//				if(instance.isRunning(eh.getHandlerActivity().getTracingTag())){
//					//new UEngineException("Event [" + eventName + "] is already running. Retry when the event handling activity is finished.");
//					UEngineException eventIsAlreadyRunning = new UEngineException("Event [" + eventName + "] is currently running. User may wait until the previous request completes.");
//					eventIsAlreadyRunning.setErrorLevel(UEngineException.FATAL);
//				
//					throw eventIsAlreadyRunning;
//				}
				
				queueActivity(eh.getHandlerActivity(), instance);
				
				return true;
			}else{
				//throw new UEngineException("Event [" + eventName + "] is not declared.");
				return false;
			}
		}
		
		return false;
	}

	protected void onEvent(String command, ProcessInstance instance, Object payload) throws Exception {

		if(instance.fireActivityEventInterceptor(this, command, instance, payload))
			return;

		
		EventHandler[] eventHandlers = getEventHandlers();

		if(command.equals(Activity.CHILD_DONE) || command.equals(Activity.CHILD_COMPENSATED)){
			Activity childActivityHasBeenDone = (Activity)payload;
			
			if(eventHandlers!=null)
			for(int i=0; i<eventHandlers.length; i++){
				if(eventHandlers[i].getHandlerActivity()==childActivityHasBeenDone){
					return; //ignore CHILD_DONE event from the event handlers
				}
			}
		}else{
			if(command.equals(Activity.CHILD_FAULT)){
				if(eventHandlers!=null){
					boolean faultHandlerExists = false;
					for(int i=0; i<eventHandlers.length; i++){
						if(eventHandlers[i].getTriggeringMethod() == EventHandler.TRIGGERING_BY_FAULT){
							EventMessagePayload emp = new EventMessagePayload();
							emp.setEventName(eventHandlers[i].getName());
							if(payload instanceof FaultContext){
								emp.setTriggerTracingTag(((FaultContext)payload).getCauseActivity().getTracingTag());
							}
							
							onMessage(instance, emp);
							faultHandlerExists = true;
						}
					}
					
					if(faultHandlerExists && !instance.getStatus(getTracingTag()).equals(Activity.STATUS_COMPLETED)){
						fireComplete(instance);
						return;
					}
				}

				forwardErrorEvent(instance, payload);
			}
		}
	
		super.onEvent(command, instance, payload);
	}

	private void forwardErrorEvent(ProcessInstance instance, Object payload) {


	}

	public EventHandler getEventHandler(String eventName){
		
		EventHandler[] eventHandlers = getEventHandlers();

		if(eventHandlers!=null)
		for(int i=0; i<eventHandlers.length; i++){
			if(eventHandlers[i]!=null && eventHandlers[i].getName()!=null && eventHandlers[i].getName().equals(eventName))
				return eventHandlers[i];
		}
		
		return null;
	}
	
	public String getMessage() {
		return "event";
	}



	public boolean registerToProcessDefinition(boolean autoTagging, boolean checkCollision) {
		
		EventHandler[] eventHandlers = getEventHandlers();

		boolean ok = super.registerToProcessDefinition(autoTagging, checkCollision);
		
		if(checkCollision && !ok) return false;
		
		if(eventHandlers!=null)
		for(int i=0; i<eventHandlers.length; i++){
			Activity eventHandlingActivity = eventHandlers[i].getHandlerActivity(); 
			
			//if(eventHandlingActivity.getProcessDefinition() == null || eventHandlingActivity.getParentActivity() == null){
			eventHandlingActivity.setParentActivity(this);
			//}
			
			ok = eventHandlingActivity.registerToProcessDefinition(autoTagging, checkCollision);
			
			if(checkCollision && !ok) return false;
		}
		
		return true;
	}
	
	public void compensate(ProcessInstance instance) throws Exception {

		EventHandler[] eventHandlers = getEventHandlers();

		super.compensate(instance);

		if(eventHandlers!=null){
			for(int i=0; i<eventHandlers.length; i++){
				//Activity eventHandlingActivity = eventHandlers[i].getHandlerActivity(); 
				
				if(eventHandlers[i].getTriggeringMethod() == EventHandler.TRIGGERING_BY_COMPENSATION){
					EventMessagePayload eventMessage = new EventMessagePayload();
					eventMessage.setEventName(eventHandlers[i].getName());

					onMessage(instance, eventMessage);
				}
				
				/**
				 * We don't need to compensate the event executions
				 */
				/*else				
				if(isCompensatable(eventHandlingActivity.getStatus(instance))){
					eventHandlingActivity.compensate(instance);
				}*/
				
			}
		}
		
		if(!isLeaveEventListenersEvenIfOutOfScope())
			getProcessDefinition().removeMessageListener(instance, this);

	}
	
	public ValidationContext validate(Map options) {
		
//		EventHandler[] eventHandlers = getEventHandlers();

		ValidationContext vc = super.validate(options);
		
//		HashMap eventNames = new HashMap();
//		if(eventHandlers!=null){
//			for(int i=0; i<eventHandlers.length; i++){
//				EventHandler eventHandler = eventHandlers[i];
//				Activity eventHandlingActivity = eventHandlers[i].getHandlerActivity();
//
//				if (!(eventHandlingActivity instanceof SubProcessActivity) &&
//						!(eventHandlingActivity instanceof HumanActivity) &&
//						!(eventHandlingActivity instanceof ComplexActivity)) {
//					vc.add("[" + eventHandlingActivity.getName() + "] is not available as an event handler.");
//				}
//
//				if(eventNames.containsKey(eventHandler.getName())){
//					vc.add(getActivityLabel() + "The event handler name ["+eventHandler.getName()+"] is duplicated.");
//				}
//
//				eventNames.put(eventHandler.getName(),"");
//			}
//		}
//		if( this.getIncomingSequenceFlows() == null || (this.getIncomingSequenceFlows() != null && this.getIncomingSequenceFlows().size() < 1) ){
//			vc.add(getActivityLabel() + " is not execute. no incomming transition. ");
//		}
		
		return vc;
	}


////// implementations for MessageLister Framework //////
	@Override
	public void beforeRegistered(ProcessInstance instance) throws Exception {

	}

	@Override
	public void afterRegistered(ProcessInstance instance) throws Exception {

	}

	@Override
	public void afterUnregistered(ProcessInstance instance) throws Exception {

	}

////// End of implementations for MessageLister Framework //////


}
