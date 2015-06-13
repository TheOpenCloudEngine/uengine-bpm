package org.uengine.kernel;

import java.util.*;


/**
 * @author Jinyoung Jang
 */

public class EMailActivity extends WebServiceActivity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	protected static final String MAIL_SERVICE = "mailServer";

	String contents;
		public String getContents() {
			return contents;
		}
		public void setContents(String value) {
			contents = value;
		}

	String title;
		public String getTitle() {
			return title;
		}
		public void setTitle(String value) {
			title = value;
		}

	Role toRole;
		public Role getToRole() {
			return toRole;
		}
		public void setToRole(Role value) {
			toRole = value;
		}
	
	public EMailActivity(){
		setName("Email Activity");
		setPortType("EMailService");
		setOperationName("sendMail");
		
		//recode
		setRole(ProcessDefinition.EMAIL_SERVICE_ROLE);
	}

	public void executeActivity(ProcessInstance instance) throws Exception{
		StringBuffer generating = evaluateContent(instance, getContents());
		
System.out.println("email activity: rolname:"+role.getName());
		try{		
			System.out.println(role.getMapping(instance, getTracingTag()).getEndpoint());
		}catch(Exception e){
			throw new UEngineException("No role mapping found: " + role.getName(), "Please check role mapping for mailserver");
		}

		setParameters(new Object[]{
			"uengine",
			getToRole().getMapping(instance, getTracingTag()).getEndpoint(),
			evaluateContent(instance, getTitle()).toString(),
			generating.toString()
		});

		invokeWebService(instance);
	}

	protected void invokeWebService(ProcessInstance instance) throws Exception{
		super.executeActivity(instance);
	}

	public Map getActivityDetails(ProcessInstance inst, String locale) throws Exception{
		Hashtable details = (Hashtable)super.getActivityDetails(inst, locale);
		details.put("content", evaluateContent(inst, getContents()).toString());
		
		return details;
	}
	
	
	
/*	protected String[] getFieldsToEvaluate(){
		return new String[]{"Contents", "Title"};
	}
*/
}

