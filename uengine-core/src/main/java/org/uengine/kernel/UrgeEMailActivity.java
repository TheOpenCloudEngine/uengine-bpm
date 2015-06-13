package org.uengine.kernel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.uengine.persistence.dao.DAOFactory;
import org.uengine.util.UEngineUtil;
import org.uengine.util.dao.DefaultConnectionFactory;
import org.uengine.webservices.emailserver.impl.EMailServerSoapBindingImpl;

public class UrgeEMailActivity extends DefaultActivity {

	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public UrgeEMailActivity() {
		setName("UrgeEMailActivity");
	}
	
	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
		
		String host = GlobalContext.getPropertyString("web.server.ip", "localhost");
		String port = GlobalContext.getPropertyString("web.server.port", "8080");
		String adminMailAddress = GlobalContext.getPropertyString("work_notification.from_mailaddress", "admin@uengine.org");
		
		ArrayList<WorkList> delayedWorks = this.getDelayedWorks();
		HashMap<String, ArrayList<WorkList>> delayedWorksByEndpoint = new HashMap<String, ArrayList<WorkList>>();

		for (WorkList delayedWork : delayedWorks) {
			String endpoint = delayedWork.getEndpoint();
			ArrayList<WorkList> workList = null;
			if (delayedWorksByEndpoint.containsKey(endpoint)) {
				workList = delayedWorksByEndpoint.get(endpoint);
			} else {
				workList = new ArrayList<WorkList>();
			}
			workList.add(delayedWork);
			delayedWorksByEndpoint.put(endpoint, workList);
		}
		
		for (Entry<String, ArrayList<WorkList>> entry : delayedWorksByEndpoint.entrySet()) {
			String endpoint = entry.getKey();
			
			RoleMapping rm = new RoleMapping().create();
			rm.setEndpoint(endpoint);
			rm.fill(instance);
			
			String emailAddress = rm.getEmailAddress();
			StringBuffer emailContent = new StringBuffer("");
			
			if (UEngineUtil.isNotEmpty(emailAddress)) {
				for (WorkList workList : entry.getValue()) {
					
					StringBuffer workUrl = new StringBuffer(); 
					workUrl.append("http://").append(host).append(":").append(port);
					workUrl.append(GlobalContext.WEB_CONTEXT_ROOT).append("/wih/defaultHandler/forwardWork.jsp");
					workUrl.append("?taskId=").append(workList.getTaskId()).append("&userId=").append(endpoint);
					
					StringBuffer content = new StringBuffer();
					
					content.append("<table width=\"780\" border=\"1\" cellpadding=\"5\" cellspacing=\"0\">").append("\r\n");
					content.append("	<col style=\"width: 150px\">").append("\r\n");
					content.append("	<col style=\"\">").append("\r\n");
					content.append("	<col style=\"width: 150px\">").append("\r\n");
					content.append("	<col style=\"\">").append("\r\n");
					
					content.append("	<tr>").append("\r\n");
					content.append("		<td>").append("Initiator").append("</td>").append("\r\n");
					content.append("		<td>").append(workList.getInitiator()).append("</td>").append("\r\n");
					content.append("	</tr>").append("\r\n");
					
					content.append("	<tr>").append("\r\n");
					content.append("		<td>").append("Instance Name").append("</td>").append("\r\n");
					content.append("		<td>").append(workList.getDefName()).append("</td>").append("\r\n");
					content.append("	</tr>").append("\r\n");
					
					content.append("	<tr>").append("\r\n");
					content.append("		<td>").append("Task Name").append("</td>").append("\r\n");
					content.append("		<td>").append(workList.getTitle()).append("</td>").append("\r\n");
					content.append("	</tr>").append("\r\n");
					
					content.append("	<tr>").append("\r\n");
					content.append("		<td>").append("Work URL").append("</td>").append("\r\n");
					content.append("		<td>").append(workUrl.toString()).append("</td>").append("\r\n");
					content.append("	</tr>").append("\r\n");
					content.append("</table>").append("\r\n");
					
					content.append("<br /><br />");
					
					emailContent.append(content);
				}
				
				if (UEngineUtil.isNotEmpty(emailContent.toString())) {
					(new EMailServerSoapBindingImpl()).sendMail(
							adminMailAddress, emailAddress, "[Urge Notice]", emailContent.toString());
					
					Thread.sleep(100);
				}
			}
		}
		
		fireComplete(instance);
	}
	
	private ArrayList<String> getEndpoints() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT EMPCODE FROM EMPTABLE WHERE ISDELETED='0' ");
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		ArrayList<String> endpoints = null;

		try {
			conn = DefaultConnectionFactory.create().getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql.toString());
			
			endpoints = new ArrayList<String>();
			
			while (rs.next()) {
				endpoints.add(rs.getString("EMPCODE"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close(); } catch (SQLException e) { }
			if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
			if (conn != null) try { conn.close(); } catch (SQLException e) { }
		}
		
		return endpoints;
	}
	
	private ArrayList<WorkList> getDelayedWorks() {
		ArrayList<WorkList> delayedWorks = new ArrayList<WorkList>();
		ArrayList<String> endpoints = getEndpoints();
		
		for (String endpoint : endpoints) {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" select DISTINCT INST.NAME as PROCINSTNM, INST.INITRSNM, INST.INFO, WL.* FROM BPM_PROCINST INST, BPM_WORKLIST WL ");
			sql.append(" INNER JOIN BPM_ROLEMAPPING ROLE ON WL.ROLENAME=ROLE.ROLENAME OR WL.ENDPOINT='").append(endpoint).append("' where ");
			sql.append(" (WL.STATUS = 'NEW' or WL.STATUS = 'CONFIRMED' or WL.STATUS = 'DRAFT')  ");
			
			String typeOfDBMS = null;
			try {
				typeOfDBMS = DAOFactory.getInstance().getDBMSProductName().toUpperCase();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if ("ORACLE".equals(typeOfDBMS))
				sql.append(" and (TO_CHAR(WL.DUEDATE,'yyyy-MM-dd HH24:mm:ss') <= TO_CHAR(SYSDATE,'yyyy-MM-dd HH24:mm:ss'))");
			else if ("HSQL".equals(typeOfDBMS))
				sql.append(" and (TO_CHAR(WL.DUEDATE,'yyyy-MM-dd HH24:mm:ss') <= TO_CHAR(CURRENT_TIMESTAMP,'yyyy-MM-dd HH24:mm:ss'))");
			else if ("MYSQL".equals(typeOfDBMS))
				sql.append(" and WL.DUEDATE <= now()");
			
			sql.append(" AND WL.INSTID=INST.INSTID AND WL.INSTID=ROLE.INSTID AND INST.ISDELETED='0' ");
			sql.append(" AND ROLE.ENDPOINT='").append(endpoint).append("' order by WL.STARTDATE desc "); 
			
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
	
			try {
				conn = DefaultConnectionFactory.create().getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql.toString());
				
				while (rs.next()) {
					WorkList wl = new WorkList();
					wl.setDefName(rs.getString("NAME"));
					wl.setEndpoint(endpoint);
					wl.setTitle(rs.getString("TITLE"));
					wl.setTaskId(String.valueOf(rs.getInt("TASKID")));
					wl.setInitiator(rs.getString("INITRSNM")+"/"+rs.getString("INITEP"));
					
					delayedWorks.add(wl);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (rs != null) try { rs.close(); } catch (SQLException e) { }
				if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
				if (conn != null) try { conn.close(); } catch (SQLException e) { }
			}
		}
		
		return delayedWorks;
	}
	
	class WorkList implements Serializable {

		private String initiator;
		private String endpoint;
		private String instanceId;
		private String rootInstanceId;
		private String taskId;
		private String tracingTag;
		private String title;
		private String defName;
		private String info;
		private String startDate;
		private String duplicateTaskCount;
		private String roleName;

		public String getInitiator() {
			return initiator;
		}

		public void setInitiator(String initiator) {
			this.initiator = initiator;
		}

		public String getEndpoint() {
			return endpoint;
		}

		public void setEndpoint(String endpoint) {
			this.endpoint = endpoint;
		}

		public String getInstanceId() {
			return instanceId;
		}

		public void setInstanceId(String instanceId) {
			this.instanceId = instanceId;
		}

		public String getRootInstanceId() {
			return rootInstanceId;
		}

		public void setRootInstanceId(String rootInstanceId) {
			this.rootInstanceId = rootInstanceId;
		}

		public String getTaskId() {
			return taskId;
		}

		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}

		public String getTracingTag() {
			return tracingTag;
		}

		public void setTracingTag(String tracingTag) {
			this.tracingTag = tracingTag;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDefName() {
			return defName;
		}

		public void setDefName(String defName) {
			this.defName = defName;
		}

		public String getInfo() {
			return info;
		}

		public void setInfo(String info) {
			this.info = info;
		}

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public String getDuplicateTaskCount() {
			return duplicateTaskCount;
		}

		public void setDuplicateTaskCount(String duplicateTaskCount) {
			this.duplicateTaskCount = duplicateTaskCount;
		}

		public String getRoleName() {
			return roleName;
		}

		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}

	}


}
