/*
 * Created on 2004. 12. 19.
 */
package org.uengine.components.activityfilters;

import org.uengine.kernel.*;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Jinyoung Jang
 */
public class WorkNotificationMailSendingActivityFilter implements ActivityFilter, Serializable{

	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	final static String messageBody = GlobalContext.getPropertyString("alarmactivityfilter.message", "You've received a work to do");
	final static String host = GlobalContext.getPropertyString("alarmactivityfilter.wih.host","localhost");
	final static String port = GlobalContext.getPropertyString("alarmactivityfilter.wih.port","8080");
	
	public void afterExecute(Activity activity, final ProcessInstance instance)
		throws Exception {
		
		
		String fromAddr = GlobalContext.getPropertyString("work_notification.from_mailaddress", "admin@uengine.org");

		if(activity instanceof HumanActivity){
			if(!((HumanActivity)activity).isSendEmailWorkitem())
				return;
			
			HttpServletRequest request = (HttpServletRequest) instance.getProcessTransactionContext().getServletRequest();
			if (request != null && "yes".equals(request.getParameter("initiate"))) {
				Activity firstExecuted = null;
				try {
					firstExecuted = ((ActivityInstanceContext) instance.getProcessTransactionContext().getExecutedActivityInstanceContextsInTransaction().get(0)).getActivity();
				} catch (Exception e) { }
				
				if(firstExecuted == null)
					return;
				
				if(firstExecuted == activity)
					return;
			}
			
			try {
//				if(instance.isNew() && instance.getProcessDefinition().getInitiatorHumanActivityReference(instance.getProcessTransactionContext()).getActivity().equals(activity)) return;
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			try {
				HumanActivity humanActivity = (HumanActivity)activity;				
				
				//TODO: hard-coded
				final LocalEMailActivity mailer = new LocalEMailActivity(){

					public void fireComplete(ProcessInstance instance) throws Exception {
					//disabled
					}
					
				};
			
				// find root instance initiator
				ActivityReference ar = instance.getRootProcessInstance().getProcessDefinition().getInitiatorHumanActivityReference(instance.getProcessTransactionContext());
				String initiator = "";
				if (ar.getActivity() instanceof HumanActivity) {
					RoleMapping initiatorRoleMapping = ((HumanActivity)ar.getActivity()).getRole().getMapping(instance.getRootProcessInstance());
					initiator = initiatorRoleMapping.getResourceName() + "/" + initiatorRoleMapping.getEndpoint();
				}
				
				// Previous Completed HumanActivity In Transaction
				Activity previousHumanAct = humanActivity.getCompletedHumanActivityInTransaction(instance);
				String previousEndpoint = "";
				Role previousRole = null;
				if (previousHumanAct instanceof HumanActivity) {
					previousRole = ((HumanActivity) previousHumanAct).getRole();
					RoleMapping previousRoleMapping = previousRole.getMapping(instance);
					previousEndpoint = previousRoleMapping.getResourceName() + "/" + previousRoleMapping.getEndpoint(); 
				}
				
				RoleMapping roleMapping = (RoleMapping)(humanActivity.getRole()).getMapping(instance, activity.getTracingTag());
				String[] taskIds = humanActivity.getTaskIds(instance);
				final String taskId = taskIds[0];

				String server = null;
				String serverPort = null;
				if (request != null) {
					server = GlobalContext.getPropertyString("web.server.ip", request.getServerName());
					serverPort = request.getServerPort() != 80 ? (":"+ String.valueOf(request.getServerPort())) : "";
				} else {
					server = GlobalContext.getPropertyString("web.server.ip", "localhost");
					serverPort = ":" + GlobalContext.getPropertyString("web.server.port", "8080");
				}
				
				String workURL = "http://"
						+ (host != null ? host : server)
						+ (port != null ? (":" + port) : serverPort)
						+ GlobalContext.WEB_CONTEXT_ROOT + "/wih/defaultHandler/forwardWork.jsp?taskId=" 
						+ taskId + "&userId=" + roleMapping.getEndpoint();
				
				// from org.uengine.components.activityfilters.WorkNotificationMailSendingActivityFilter
				/*
				com.defaultcompany.aes.RoboEncryptor re = new com.defaultcompany.aes.RoboEncryptor();
				
				
				String workURL = "http://"
						+ (host != null ? host : server)
						+ (port != null ? (":" + port) : serverPort)
						+ GlobalContext.WEB_CONTEXT_ROOT + "/wih/defaultHandler/frmEmailAtLogin.jsp?taskId=" 
//						+ re.encrypt(taskId) + "&userId=" + re.encrypt(roleMapping.getEndpoint()) + "&passWd=" + re.encrypt(roleMapping.getEndpoint());
						+ URLEncoder.encode(re.encrypt(taskId), "UTF-8") + "&userId=" + URLEncoder.encode(re.encrypt(roleMapping.getEndpoint()), "UTF-8");
				*/
				
				StringBuilder content = new StringBuilder();
				content.append(messageBody).append("<br />").append("\r\n");
				content.append("<table width='780' border='1' cellpadding='5' cellspacing='0'>").append("\r\n");
				content.append("	<col style='width: 150px;'>").append("\r\n");
				content.append("	<col>").append("\r\n");
				content.append("	<col style='width: 150px;'>").append("\r\n");
				content.append("	<col>").append("\r\n");
				
				content.append("	<tr>").append("\r\n");
				content.append("		<td>").append(GlobalContext.getMessageForWeb("Initiator", GlobalContext.DEFAULT_LOCALE)).append("</td>").append("\r\n");
				content.append("		<td>").append(initiator).append("</td>").append("\r\n");
				content.append("		<td>").append(GlobalContext.getMessageForWeb("Previous Participant", GlobalContext.DEFAULT_LOCALE)).append("</td>").append("\r\n");
				content.append("		<td>").append(previousEndpoint).append("</td>").append("\r\n");
				content.append("	</tr>").append("\r\n");
				
				content.append("	<tr>").append("\r\n");
				content.append("		<td>").append(GlobalContext.getMessageForWeb("Process Name", GlobalContext.DEFAULT_LOCALE)).append("</td>").append("\r\n");
				content.append("		<td>").append(instance.getProcessDefinition().getName()).append("</td>").append("\r\n");
				content.append("		<td>").append(GlobalContext.getMessageForWeb("Instance Name", GlobalContext.DEFAULT_LOCALE)).append("</td>").append("\r\n");
				content.append("		<td>").append(instance.getName()).append("</td>").append("\r\n");
				content.append("	</tr>").append("\r\n");
				
				content.append("	<tr>").append("\r\n");
				content.append("		<td>").append(GlobalContext.getMessageForWeb("Task Name", GlobalContext.DEFAULT_LOCALE)).append("</td>").append("\r\n");
				content.append("		<td colspan='3'>").append(activity.getName()).append("</td>").append("\r\n");
				content.append("	</tr>").append("\r\n");
				
				content.append("	<tr>").append("\r\n");
				content.append("		<td>").append(GlobalContext.getMessageForWeb("Work URL", GlobalContext.DEFAULT_LOCALE)).append("</td>").append("\r\n");
				content.append("		<td colspan='3'><a href='" + workURL + "'>").append(workURL).append("</a></td>").append("\r\n");
				content.append("	</tr>").append("\r\n");
				content.append("</table>").append("\r\n");
				
				mailer.setContents(content.toString());
				mailer.setToRole(humanActivity.getRole());
				mailer.setFrom(fromAddr);
				mailer.setFromRole(previousRole);
				mailer.setTitle("[uEngine] " + instance.getName() + " > " + activity.getName());
				
				Thread sender = new Thread(){

					public void run() {
						try {
							mailer.executeActivity(instance);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				};
				
				sender.start();
				
				
																
			}catch(Exception e){
				System.out.println("[AlarmActivityFilter] failed to send alarm: " + e.getMessage());
			}
		}
	}

	public void afterComplete(Activity activity, ProcessInstance instance) throws Exception {
	}
	
	public void beforeExecute(Activity activity, ProcessInstance instance)
		throws Exception {
	}

	public void onDeploy(ProcessDefinition definition) throws Exception {
	}

	public void onPropertyChange(Activity activity, ProcessInstance instance, String propertyName, Object changedValue) throws Exception {
	}

}
