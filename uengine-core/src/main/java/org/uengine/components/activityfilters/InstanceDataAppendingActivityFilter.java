/*
 * Created on 2004. 12. 19.
 */
package org.uengine.components.activityfilters;

import java.io.Serializable;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ActivityFilter;
import org.uengine.kernel.EJBProcessInstance;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.RoleMapping;



/**
 * @author Jinyoung Jang
 */
public class InstanceDataAppendingActivityFilter implements ActivityFilter, Serializable{

	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public void afterExecute(Activity activity, final ProcessInstance instance)
		throws Exception {
		

		if(activity instanceof HumanActivity){
			try{
				RoleMapping rm = ((HumanActivity)activity).getRole().getMapping(instance);
				rm.fill(instance);
				if(rm == null) return;
				if(
						instance.isNew() 
						&& instance.getProcessDefinition().getInitiatorHumanActivityReference(instance.getProcessTransactionContext()).getActivity().equals(activity)
				){	
					((EJBProcessInstance) instance).getProcessInstanceDAO().set("initEp", rm.getEndpoint());
					((EJBProcessInstance) instance).getProcessInstanceDAO().set("initRSNM", rm.getResourceName());
					((EJBProcessInstance) instance).getProcessInstanceDAO().set("INITCOMCD", rm.getCompanyId());
					
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("prevCurrEp", rm.getEndpoint());
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("prevCurrRSNM", rm.getResourceName());
					
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("currEp", "");
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("currRSNM", "");
					//((EJBProcessInstance)instance).getProcessInstanceDAO().set("lastCmnt", "");
					
					
				} else {
					StringBuffer endpoint = new StringBuffer();
					StringBuffer resourceName = new StringBuffer();
					do {
						if (endpoint.length() > 0) endpoint.append(";");
						endpoint.append(rm.getEndpoint());
						
						if (resourceName.length() > 0) resourceName.append(";");
						resourceName.append(rm.getResourceName());
					} while (rm.next());
					
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("prevCurrEp", ((EJBProcessInstance)instance.getRootProcessInstance()).getProcessInstanceDAO().get("currEp"));
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("prevCurrRSNM", ((EJBProcessInstance)instance.getRootProcessInstance()).getProcessInstanceDAO().get("currRSNM"));
					
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("currEp", endpoint.toString());
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("currRSNM", resourceName.toString());
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("lastCmnt", activity.getName());
					
					//for root instance replication: 
					((EJBProcessInstance)instance.getRootProcessInstance()).getProcessInstanceDAO().set("currEp", endpoint.toString());
					((EJBProcessInstance)instance.getRootProcessInstance()).getProcessInstanceDAO().set("currRSNM", resourceName.toString());
					((EJBProcessInstance)instance.getRootProcessInstance()).getProcessInstanceDAO().set("lastCmnt", activity.getName());
					
					
					//((EJBProcessInstance)instance).getProcessInstanceDAO().set("currACT", activity.getName().getText());
				}

				
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		/*
		if ( instance.isNew() && instance.isSubProcess() && !instance.getInstanceId().equals(instance.getRootProcessInstanceId())) {
			String initEp = (String) ((EJBProcessInstance)instance.getRootProcessInstance()).getProcessInstanceDAO().get("initEp");
			String initRSNM = (String) ((EJBProcessInstance)instance.getRootProcessInstance()).getProcessInstanceDAO().get("initRSNM");
			String initComcode = (String) ((EJBProcessInstance)instance.getRootProcessInstance()).getProcessInstanceDAO().get("INITCOMCD");
			((EJBProcessInstance) instance).getProcessInstanceDAO().set("initEp", initEp);
			((EJBProcessInstance) instance).getProcessInstanceDAO().set("initRSNM", initRSNM);
			((EJBProcessInstance) instance).getProcessInstanceDAO().set("INITCOMCD", initComcode);
		}
		*/
	}

	public void afterComplete(Activity activity, ProcessInstance instance) throws Exception {
		try{
			String abstraction = null;
			
			String alias = activity.getProcessDefinition().getAlias();
			
//			if(activity instanceof FormActivity){
//				FormActivity formActivity = (FormActivity)activity;
//				
//				ProcessVariable formVariable = formActivity.getVariableForHtmlFormContext();
//				HtmlFormContext formContext = (HtmlFormContext) formVariable.get(instance, "");
//				
//				String htmlPath = formContext.getHtmlPath();
//				File file = new File(htmlPath);
//				
//				BufferedReader f_in = new BufferedReader(new InputStreamReader(new FileInputStream(file), GlobalContext.ENCODING));
//				StringBuffer buf = new StringBuffer();
//				
//				while(true) {
//					String line = f_in.readLine();
//					if(line == null) break;
//					String changeStr = line.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "").trim();
//					if(changeStr.length() == 0) continue;					
//					buf.append(changeStr + "/...");					
//				}
//				f_in.close();
//				
//				abstraction = buf.toString().trim();
//				abstraction = abstraction.substring(abstraction.length() - 980 > 0 ? abstraction.length() - 980 : 0);
//				
//				//TODO:
//				abstraction = "";
//				//String abstraction = formContext.getValueMap().toString();	
//				//abstraction = abstraction.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");         
//				
//			} else if(("pingProcess".equals(alias) || "replyProcess".equals(alias)) && activity instanceof HumanActivity){
//				abstraction = (String) instance.get("message");
//			}
//			
//			if(abstraction!=null){
//				WorkListDAO wlDAO = (WorkListDAO) instance.getProcessTransactionContext().findSynchronizedDAO("BPM_WORKLIST", "TASKID", ((HumanActivity)activity).getTaskIds(instance)[0], WorkListDAO.class);
//				wlDAO.set("abstract", abstraction);
//				((EJBProcessInstance)instance.getRootProcessInstance()).getProcessInstanceDAO().set("abstractinst", abstraction);
//			}
		}catch(Exception e){
		   	throw e;
		}
	}
	
	public void beforeExecute(Activity activity, ProcessInstance instance)
		throws Exception {
	}

	public void onDeploy(ProcessDefinition definition) throws Exception {
	}

	public void onPropertyChange(Activity activity, ProcessInstance instance, String propertyName, Object changedValue) throws Exception {
	
		if(activity instanceof HumanActivity && "saveEndpoint".equals(propertyName)){
			try{
				RoleMapping rm = ((HumanActivity)activity).getRole().getMapping(instance);
				rm.fill(instance);
				if(rm == null) return;
				if(
						instance.isNew() 
						&& instance.getProcessDefinition().getInitiatorHumanActivityReference(instance.getProcessTransactionContext()).getActivity().equals(activity)
				){	
					((EJBProcessInstance) instance).getProcessInstanceDAO().set("initEp", rm.getEndpoint());
					((EJBProcessInstance) instance).getProcessInstanceDAO().set("initRSNM", rm.getResourceName());
					((EJBProcessInstance) instance).getProcessInstanceDAO().set("INITCOMCD", rm.getCompanyId());
					
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("currEp", rm.getEndpoint());
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("currRSNM", rm.getResourceName());
				} else {
					StringBuffer endpoint = new StringBuffer();
					StringBuffer resourceName = new StringBuffer();
					do {
						if (endpoint.length() > 0) endpoint.append(";");
						endpoint.append(rm.getEndpoint());
						
						if (resourceName.length() > 0) resourceName.append(";");
						resourceName.append(rm.getResourceName());
					} while (rm.next());
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("currEp", endpoint.toString());
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("currRSNM", resourceName.toString());
					//((EJBProcessInstance)instance).getProcessInstanceDAO().set("currACT", activity.getName().getText());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if ( instance.isNew() && instance.isSubProcess() && !instance.getInstanceId().equals(instance.getRootProcessInstanceId())) {
				String initEp = (String) ((EJBProcessInstance)instance.getRootProcessInstance()).getProcessInstanceDAO().get("initEp");
				String initRSNM = (String) ((EJBProcessInstance)instance.getRootProcessInstance()).getProcessInstanceDAO().get("initRSNM");
				String initComcode = (String) ((EJBProcessInstance)instance.getRootProcessInstance()).getProcessInstanceDAO().get("INITCOMCD");
				((EJBProcessInstance) instance).getProcessInstanceDAO().set("initEp", initEp);
				((EJBProcessInstance) instance).getProcessInstanceDAO().set("initRSNM", initRSNM);
				((EJBProcessInstance) instance).getProcessInstanceDAO().set("INITCOMCD", initComcode);
				
				((EJBProcessInstance)instance).getProcessInstanceDAO().set("currEp",initEp);
				((EJBProcessInstance)instance).getProcessInstanceDAO().set("currRSNM", initRSNM);
			}
		}
	}
}
