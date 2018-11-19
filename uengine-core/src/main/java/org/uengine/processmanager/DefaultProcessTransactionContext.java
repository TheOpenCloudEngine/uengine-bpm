package org.uengine.processmanager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.uengine.kernel.DefaultProcessInstance;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessDefinitionFactory;

/**
 * @author  <a href="mailto:ghbpark@hanwha.co.kr">Sungsoo Park</a>
 * @version    $Id: ProcessTransactionContext.java,v 1.1 2012/02/13 05:29:33 sleepphoenix4 Exp $
 */
public class DefaultProcessTransactionContext extends DefaultTransactionContext implements ProcessTransactionContext {
		
	static PrintStream originalSystemOut = System.out;
	static PrintStream originalSystemErr = System.err;

	
	ProcessManagerBean processManagerBean;
	
	@Override
	public void addDebugInfo(Object message){
		if (GlobalContext.logLevelIsDebug) {
			StringBuilder richDebugInfo = (StringBuilder) getSharedContext("_richDebugInfo");
			
			if(richDebugInfo==null){
				richDebugInfo = new StringBuilder();
				richDebugInfo.append("\n=================================================================================");
				richDebugInfo.append("\n==[ uEngine Application Execution Log ]==========================================");
				richDebugInfo.append("\n=================================================================================\n");
			}
			
			richDebugInfo.append(message);
	
			setSharedContext("_richDebugInfo", richDebugInfo);
		}
	}
	
	@Override
	public StringBuilder getDebugInfo(){
		StringBuilder richDebugInfo = (StringBuilder) getSharedContext("_richDebugInfo");
		
		return (richDebugInfo);
	}

	protected DefaultProcessTransactionContext(ProcessManagerBean processManagerBean){
		super(processManagerBean!=null ? processManagerBean.getConnectionFactory(): null);

		if(GlobalContext.captureSystemOut){
			
			System.setOut(new PrintStream(new ByteArrayOutputStream()){
				public void println(String str){
					originalSystemOut.println(str);
					addDebugInfo(new StringBuilder().append(str).append("\n").toString());
				}

				public void println(Object x) {
					originalSystemOut.println(x);
					addDebugInfo(x.toString());
				}
				
			});	

			System.setErr(new PrintStream(new ByteArrayOutputStream()){
				public void println(String str){
					originalSystemErr.println(str);
					addDebugInfo(new StringBuilder().append(str).append("\n").toString());
				}
				public void print(String str){
					originalSystemErr.println(str);
					addDebugInfo(str);
				}
				public void println(Object x) {
					originalSystemOut.println(x);
					addDebugInfo(x.toString());
				}
			});

		}
		
		if(processManagerBean==null) return;

		this.processManagerBean = processManagerBean;
		
		setAutoCloseConnection(processManagerBean.isAutoCloseConnection());
		setManagedTransaction(processManagerBean.isManagedTransaction()); //who removed this? this causes the transaction mismatch
	}
	
	@Override
	public ServletRequest getServletRequest() {
		if(getProcessManager().getGenericContext()!=null && getProcessManager().getGenericContext().containsKey("request")){
			return (ServletRequest)getProcessManager().getGenericContext().get("request");
		}
		
		return null;
	} 
	
	@Override
	public ServletResponse getServletResponse() {
		if(getProcessManager().getGenericContext()!=null && getProcessManager().getGenericContext().containsKey("response")){
			return (ServletResponse)getProcessManager().getGenericContext().get("response");
		}
		
		return null;
	}
	
	@Override
	public ProcessManagerBean getProcessManager(){
		return processManagerBean;
	}

	@Override
	public boolean isManagedTransaction() {
		return getProcessManager().isManagedTransaction();
	}	
	
	@Override
	public ProcessDefinition getProcessDefinition(String pdvid) throws Exception{
		return (ProcessDefinition) ProcessDefinitionFactory.getInstance(this).getActivity(pdvid, true);
	}

	@Override
	public void registerProcessInstance(DefaultProcessInstance defaultProcessInstance) {

	}

}
