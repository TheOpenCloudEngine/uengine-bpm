package org.uengine.kernel;

import java.util.Hashtable;
import java.util.Map;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.RadioInput;
import org.uengine.kernel.bpmn.StartActivity;
import org.uengine.persistence.processinstance.ProcessInstanceDAO;
import org.uengine.persistence.processinstance.ProcessInstanceDAOType;

/**
 * @author Jinyoung Jang
 */

public class EndActivity extends StartActivity {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public final static int STATUS_STOPPED		= 1;
	public final static int STATUS_CANCELLED	= 2;
	public final static int STATUS_FAULT		= 3;
	public final static int STATUS_COMPLETED	= 4;

	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd = null;
		
		fd = type.getFieldDescriptor("Status");
		fd.setDisplayName("Terminate Status");
		fd.setInputter(
			new RadioInput(
				new String[] {
						"Stopped",
						"Cancelled",
						"Failed",
						"Completed"
				},
				new Object[] {
						EndActivity.STATUS_STOPPED,
						EndActivity.STATUS_CANCELLED,
						EndActivity.STATUS_FAULT,
						EndActivity.STATUS_COMPLETED
				}
			)
		);
		
		type.removeFieldDescriptor("TerminateStatus");
	}
	
	boolean escalate = false;
		public boolean isEscalate() {
			return escalate;
		}
		public void setEscalate(boolean escalate) {
			this.escalate = escalate;
		}
		
	int escalationLevel;
		public int getEscalationLevel() {
			return escalationLevel;
		}
		public void setEscalationLevel(int escalationLevel) {
			this.escalationLevel = escalationLevel;
		}	
	
	boolean terminateRunAndForgetSubProcess = false;
		public boolean isTerminateRunAndForgetSubProcess() {
			return terminateRunAndForgetSubProcess;
		}
		public void setTerminateRunAndForgetSubProcess(boolean terminateRunAndForgetSubProcess) {
			this.terminateRunAndForgetSubProcess = terminateRunAndForgetSubProcess;
		}
		
	/**
	 * @deprecated rather use status
	 */
	@Deprecated
	String terminateStatus;
		@Deprecated
		public String getTerminateStatus() {
			return terminateStatus;
		}
		@Deprecated
		public void setTerminateStatus(String terminateStatus) {
			this.terminateStatus = terminateStatus;
		}
	
	int status;
		public int getStatus() {
			if (this.status == 0 ) {
				return EndActivity.STATUS_COMPLETED;
			}
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		
	public EndActivity() {
		// TODO: 확인해야함
		//super("terminate");
	}

	public void executeActivity(ProcessInstance instance) throws Exception{
		
		String terminateStatus = null;
		if (this.getStatus() == EndActivity.STATUS_STOPPED) {
			terminateStatus = Activity.STATUS_STOPPED;
		} else if (this.getStatus() == EndActivity.STATUS_CANCELLED) {
			terminateStatus = Activity.STATUS_CANCELLED;
		} else if (this.getStatus() == EndActivity.STATUS_FAULT) {
			terminateStatus = Activity.STATUS_FAULT;
		} else if (this.getStatus() == EndActivity.STATUS_COMPLETED) {
			terminateStatus = Activity.STATUS_COMPLETED;
		}
		
		if ((isEscalate() || getEscalationLevel() > 0) && instance.isSubProcess()) {
			Long targetProcessInstanceId;
			ProcessInstance targetPI = null;
			
			if (!isEscalate()) {
				targetPI = instance;
				for (int escalatedCnt = getEscalationLevel(); escalatedCnt > -1; escalatedCnt--) {
					targetPI = instance.getMainProcessInstance();
					if (targetPI == null) {
						throw new UEngineException("Can't find the parent process located in the " + getEscalationLevel() + "th higher level. Check the Process Definition.");
					}
				}
				targetProcessInstanceId = new Long(targetPI.getInstanceId());
			} else {
				targetProcessInstanceId = new Long(instance.getRootProcessInstanceId());
			}
			
			ProcessInstanceDAOType piDAOF = ProcessInstanceDAOType.getInstance(instance.getProcessTransactionContext());
			ProcessInstanceDAO piDAO = piDAOF.getSiblingProcessInstances(targetProcessInstanceId, this.isTerminateRunAndForgetSubProcess());

			Hashtable options = new Hashtable();
			options.put("ptc", instance.getProcessTransactionContext());

			while (piDAO.next()) {
				ProcessInstance theInstance = ProcessInstance.create().getInstance(piDAO.getInstId().toString(), options);
				theInstance.stop(terminateStatus);
				if (targetPI != null && targetPI.isSubProcess()) {
					targetPI.getProcessDefinition().returnToMainProcess(targetPI);
				}
			}
			
		} else {
			instance.stop(terminateStatus);
			if (instance != null && instance.isSubProcess()) {
				instance.getProcessDefinition().returnToMainProcess(instance);
			}
		}
	}
	
	@Override
	public ValidationContext validate(Map options) {
		ValidationContext vc = super.validate(options);

		if (this.getStatus() == 0) {
			vc.add("Select a terminate Status");
		}

		return vc;
	}
	

	
}