package org.uengine.kernel;


import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.uengine.processmanager.SimulatorTransactionContext;
import org.uengine.webservice.ServiceProvider;


/**
 * @author Jinyoung Jang
 */

public class WebServiceActivity extends DefaultActivity{

	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	ServiceDefinition service;
		public ServiceDefinition getService() {
			if (service == null && getRole() != null)
				return getRole().getServiceType();

			return service;
		}
		public void setService(ServiceDefinition service) {
			this.service = service;
		}

	String portType;
		public String getPortType() {
			return portType;
		}
		public void setPortType(String value) {
			portType = value;
		}

	String operationName;
		public String getOperationName() {
			return operationName;
		}
		public void setOperationName(String value) {
			operationName = value;
			System.out.println(
				"WebServiceActivity::setOperationName.parameterin = " + value);
		}

	Object[] parameters;
		public Object[] getParameters() {
			return parameters;
		}
		public void setParameters(Object[] value) {
			parameters = value;
		}

	Role role;
		public Role getRole() {
			return role;
		}
		public void setRole(Role value) {
			System.out.println(
				"WebServiceActivity::setRole.parameterin = " + value);
			role = value;
		}

	ProcessVariable output;
		public ProcessVariable getOutput() {
			return output;
		}
		public void setOutput(ProcessVariable value) {
			System.out.println(
				"WebServiceActivity::setOut.parameterin = " + value);
			output = value;
		}
	
	boolean stublessInvocation;
		public boolean getStublessInvocation() {
			return stublessInvocation;
		}
		public void setStublessInvocation(boolean stublessInvocation) {
			this.stublessInvocation = stublessInvocation;
		}
		
	public WebServiceActivity(){
		setName("");
		setStublessInvocation(true);
	}

/*	public WebServiceActivity(String portType, String operationName, Object[] parameters){
		this();
		this.portType = portType;
		this.operationName = operationName;
		this.parameters = parameters;
	}*/
	
	public WebServiceActivity(Role role, String portType, String operationName, Object[] parameters, ProcessVariable out,boolean stublessInvocation){
		this.role = role;
		this.portType = portType;
		this.operationName = operationName;
		this.parameters = parameters;
		this.stublessInvocation = stublessInvocation;
		setOutput(out);
	}
	
	public void executeActivity(ProcessInstance instance) throws Exception{
		
		try{
			Thread.currentThread().setContextClassLoader(GlobalContext.getComponentClassLoader());
						
			if (getStublessInvocation()) {
///TODO
			}
			
			fireComplete(instance);
			
		}catch(Exception e){
			throw new UEngineException("WebServiceActivity:: fail to replace the classloader", e);
		}			
	}
	
	@Override
	public Map getActivityDetails(ProcessInstance instance, String locale) throws Exception{
		Map details = super.getActivityDetails(instance, locale);

		try{
			Object[] actualParameters = getActualParameters(instance);
			
			if(actualParameters!=null)
				for(int i=0; i<actualParameters.length; i++){
					details.put("Parameters["+i+"]", actualParameters[i]);
				}
		
			if(getOutput()!=null)
				details.put("Output", getOutput().get(instance, ""));
		}catch(Exception e){e.printStackTrace();}
		
		return details;
	}
	
	private Object[] getActualParameters(ProcessInstance instance) throws Exception{
		if(getParameters()==null) return null;
				
		Object[] actualParameters = new Object[getParameters().length];		
		if(getParameters()!=null){
			
			for(int i=0; i<getParameters().length; i++){
				//for backward compatibility
				Object actualParameter = getParameters()[i];
				
				if(actualParameter instanceof ParameterContext){
					actualParameter = ((ParameterContext)actualParameter).getVariable(); 
				}
				
				if(actualParameter instanceof ProcessVariable){
					actualParameter = ((ProcessVariable)actualParameter).get(instance, "");
				}
				//
				actualParameters[i] = actualParameter;
			}
		}
		
		return actualParameters;
	}


	public ValidationContext validate(Map options){
		ValidationContext validationContext  = super.validate(options);
		
		if(getPortType()== null)
			validationContext.add(getActivityLabel()+" 'PortType' must be specified.");
			
		if(getOperationName()== null)
			validationContext.add(getActivityLabel()+" 'OperationName' must be specified.");
			
		if(getParameters()!=null){
			Object[] parameters = getParameters();
			for(int i=0; i<parameters.length; i++){
				if(parameters[i] instanceof ParameterContext && ((ParameterContext)parameters[i]).getVariable()==null){
					validationContext.addWarning(getActivityLabel()+" All of the parameters must be bound with variable.");
					break;
				}
			}
		}
		
		if(getRole()== null)
			validationContext.add(getActivityLabel()+" Role must be specified.");

		return validationContext;
	}
	

	
	 public static void main(String[] args)  throws Exception{
		
		ProcessDefinition process = new ProcessDefinition();	
		
		Role role = new Role();
		role.setName("unpayedFactService");
		role.setDefaultEndpoint("http://localhost:8080/axis/UnpayedUserQueryService.jws");
		process.setRoles(new Role[]{
			role	
		});
		
		ProcessVariable var1 = new ProcessVariable();
		var1.setName("unpayedFactReturn");
		var1.setType(String.class);
		process.setProcessVariables(new ProcessVariable[]{
			var1	
		});
		
		ServiceDefinition sd = new ServiceDefinition();
		sd.setName("unpayedFactService");
		sd.setWsdlLocation("http://localhost:8080/axis/UnpayedUserQueryService.jws?wsdl");
		
		WebServiceActivity wsAct = new WebServiceActivity();
		
		wsAct.setOperationName("getUnpayedFact");
		wsAct.setRole(role);
		wsAct.setService(sd);
		wsAct.setParameters(new Object[]{"7709211907315"});
		wsAct.setOutput(var1);
		wsAct.setPortType("UnpayedUserQueryService");
	
		process.addChildActivity(wsAct);
		
		process.afterDeserialization();
		
		//(ProcessDefinition) GlobalContext.deserialize(new FileInputStream(""), ProcessDefinition.class);
		
		ProcessInstance instance = new DefaultProcessInstance();
		instance.setProcessDefinition(process);
		instance.setProcessTransactionContext(new SimulatorTransactionContext());
		instance.execute();
	}

}
