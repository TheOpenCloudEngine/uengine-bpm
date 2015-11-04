package org.uengine.kernel;

import java.io.*;


/**
 * @author Jinyoung Jang
 */

public class UEngineException extends LeveledException{

	public UEngineException(String errorMsg, String resolution, Throwable cause){
		this(errorMsg, resolution, cause, null, null);

	}
		
	public UEngineException(String errorMsg, String resolution, Throwable cause, ProcessInstance instance, Activity activity){
		super(LeveledException.NPEifNull(errorMsg), makeExceptionRicher(cause, instance));
		setCauseException(cause);
		setResolution(resolution);
		setInstance(instance);
		setActivity(activity);
		
		if(cause instanceof UEngineException){
			setErrorLevel(((UEngineException) cause).getErrorLevel());
		}
	}	


	private static Throwable makeExceptionRicher(Throwable e, ProcessInstance instance){
		if(instance==null) return e;
		
		StringBuilder richDebugInfo = (StringBuilder) instance.getProcessTransactionContext().getDebugInfo();
		
		if(GlobalContext.logLevelIsDebug){
			DebuggingContext dc;
			try {
				dc = new DebuggingContext(instance.getRootProcessInstance().getProcessTransactionContext(), instance.getRootProcessInstanceId(), true);
				
				richDebugInfo.append(dc.getExecutionPaths());
			} catch (Exception e1) {
			}
		}
		
		if(richDebugInfo!=null){
			return (new UEngineException(richDebugInfo.toString(), e));
		}
		
		return e;
	}

	public UEngineException(String errorMsg, Throwable cause){
		this(errorMsg, null, cause);
	}

	public UEngineException(String errorMsg, String resolution){
		this(errorMsg, resolution, null);
	}

	public UEngineException(String errorMsg){
		this(errorMsg, null, null);
	}

	public UEngineException(){
		this(null, null, null);
	}


	public UEngineExceptionContext createContext(){
		UEngineExceptionContext ctx = new UEngineExceptionContext();
		ctx.setMessage(getMessage());

		ctx.setDetails(getDetails());
		ctx.setResolution(getResolution());
		
		return ctx;
	}
	
	ProcessInstance instance;
		public ProcessInstance getInstance() {
			return instance;
		}
		public void setInstance(ProcessInstance instance) {
			this.instance = instance;
		}
		
	Activity activity;
		public Activity getActivity() {
			return activity;
		}
		public void setActivity(Activity activity) {
			this.activity = activity;
		}
		
	public void printStackTrace(PrintWriter pw) {
		String instanceInfo = getInstanceInfo();
		if(instanceInfo!=null){			
			pw.print(instanceInfo);
			//pw.flush();
		}
	
		super.printStackTrace(pw);
	}
	
	public void printStackTrace(PrintStream arg0) {
		
		String instanceInfo = getInstanceInfo();
		if(instanceInfo!=null){
			PrintWriter pw = new PrintWriter(arg0);
			pw.print(instanceInfo);
//			pw.flush();
			
			printStackTrace(pw);
		}else
			super.printStackTrace(arg0);
		
	}

	public String getInstanceInfo(){
		if(getInstance()!=null){
			StringBuffer errorMsg = new StringBuffer();
			errorMsg
				.append("<BPM Application Exception>\n")
				//.append((instance.getRequestContext()!=null && instance.getRequestContext().getUser()!=null) ? "  : " + instance.getRequestContext().getUser().getLoginName()  + "\n": "" )
				.append(" Instance: " + instance + "\n")
				.append(getActivity()!=null ? " Activity: "+ getActivity() + "\n" : "")
				.append(getActivity()!=null ? " Definition: "+ getActivity().getProcessDefinition()+"("+getActivity().getProcessDefinition().getAlias()+")" + "\n" : "")
				.append(" Detailed Info: ")
			;
			
			return  errorMsg.toString();
		}
		
		return null;
	}

}

