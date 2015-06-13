/*
 * Created on 2004. 12. 14.
 */
package org.uengine.persistence.dao;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.uengine.util.dao.AbstractGenericDAO;
import org.uengine.util.dao.ConnectiveDAO;
import org.uengine.util.dao.IDAO;


/**
 * @author Jinyoung Jang
 */
public class CubridDAOFactory extends OracleDAOFactory{
		
	public WorkListDAO createWorkListDAOForInsertCall(Map options) throws Exception{
		return (WorkListDAO)Proxy.newProxyInstance(
			WorkListDAO.class.getClassLoader(),
			new Class[]{WorkListDAO.class},
			new ConnectiveDAO(
				getConnectionFactory(), 
				true,
				null, 
				//"insert into bpm_worklist(taskId, title, description, endpoint, resName, roleName, refRoleName, status, priority, startdate, enddate, duedate, instId, rootInstId, defId, DefName, trcTag, tool, dispatchOption, dispatchParam1, parameter)	values(?taskId, ?title, ?description, ?endpoint, ?resName, ?roleName, ?refRoleName, ?status, ?priority, ?startdate, ?enddate, ?duedate, ?instId, ?rootInstId, ?defId, ?DefName, ?trcTag, ?tool, ?dispatchOption, ?dispatchParam1, ?parameter)",
				WorkListDAO.class
			) {
				
				public int call() throws Exception {
						Number var_taskId = (Number) get("TASKID");

						if (var_taskId == null) {
							IDAO valueDAO = ConnectiveDAO.createDAOImpl(
										getConnectionFactory(),
										"select FOR SEQ_BPM_WORKITEM.NEXT_VALUE as TASKID from DUAL",
										IDAO.class
							);
							valueDAO.select();
							valueDAO.next();
							var_taskId = (Number) valueDAO.get("TASKID");
							set("TASKID", var_taskId);
						} else {
							WorkListDAO existingWorklist = (WorkListDAO) ConnectiveDAO.createDAOImpl (
										getConnectionFactory(),
										"delete from BPM_WORKLIST where TASKID=?taskId",
										WorkListDAO.class
								);
							existingWorklist.setTaskId(var_taskId);
							existingWorklist.update();
						}

						setTableName("bpm_worklist");
						createInsertSql();

						return insert();
					}
				}
		);
	}
	
//	select bpm_func_reserveWorkItem(100, 'title','de', 'e','s',1,null,null,null,'','','','') from DUAL;
/*	public static Long bpm_func_reserveWorkItem(
		Long var_taskId,
		String var_title,
		String var_description,
		String var_endpoint,
		String var_status,
		int var_priority,
		Date var_startdate,
		Date var_enddate,
		Date var_duedate,
		String var_processInstance,
		String var_processDefinition,
		String var_tracingTag,
		String var_tool,
		String var_parameter
	) throws Exception{
		
		if(var_taskId==null){
			IDAO valueDAO = GenericDAO.createDAOImpl(
				DAOFactory.getConnectionFactory(),
				"select NEXT VALUE FOR SEQ_BPM_WORKITEM as TASKID from DUAL",
				IDAO.class
			);
			valueDAO.select();
			valueDAO.next();
			var_taskId = (Long)valueDAO.get("TASKID");
		}
				
		WorkListDAO worklist = (WorkListDAO)GenericDAO.createDAOImpl(
			DAOFactory.getConnectionFactory(),
			"insert into bpm_worklist(taskId, title, description, endpoint, status, priority, startdate, enddate, duedate, processInstance, processDefinition, tracingTag, tool, parameter)	values(?taskId, ?title, ?description, ?endpoint, ?status, ?priority, ?startdate, ?enddate, ?duedate, ?processInstance, ?processDefinition, ?tracingTag, ?tool, ?parameter)",
			WorkListDAO.class
		);

		worklist.setTaskId(var_taskId);
		worklist.setTitle(var_title);
		worklist.setDescription(var_description);
		worklist.setEndpoint(var_endpoint);
		worklist.setStatus(var_status);
		worklist.setPriority(new Long(var_priority));
		worklist.setStartDate(var_startdate);
		worklist.setEndDate(var_enddate);
		worklist.setDueDate(var_duedate);
		worklist.setProcessInstance(new Long(var_processInstance));
		worklist.setProcessDefinition(new Long(var_processDefinition));
		worklist.setTracingTag(var_tracingTag);
		worklist.setTool(var_tool);
		worklist.setParameter(var_parameter);

		worklist.insert();

		return var_taskId;
	}
*/
	public KeyGeneratorDAO createKeyGenerator(final String forWhat, final Map options) throws Exception {
		boolean option_useTableNameHeader = true;
		if(options!=null && options.containsKey("useTableNameHeader")){
			option_useTableNameHeader = !"false".equals(options.get("useTableNameHeader"));
		}
		
		final boolean useTableNameHeader = option_useTableNameHeader;

		return new KeyGeneratorDAO(){

			public Number getKeyNumber() {
				
				String forTableName = new String(forWhat);
				String forColumnName = new String(((useTableNameHeader) ? forWhat : "") + "id");
				forColumnName = forColumnName.replaceFirst("Proc", "");
//				forTableName = forTableName.toLowerCase();
				forTableName = forTableName.toUpperCase();
				if (forTableName.equals("WORKITEM")) {
					forColumnName = "TASKID";
				}
				
				Connection conn = null;
				Statement stmt_select_seq = null;
				ResultSet rs_select_seq = null;
				
				Statement stmt_select_table_max_key = null;
				ResultSet rs_select_table_max_key = null;
				
				PreparedStatement pstmt_update_seq = null;
				PreparedStatement pstmt_insert_seq = null;
				
				try {
					//conn = DefaultConnectionFactory.create().getConnection(); //may cause connection leak and undesired sequence number increment.
					conn = getConnectionFactory().getConnection();
					conn.setAutoCommit(false);
					
					Long seq_key  = null;
						stmt_select_seq = conn.createStatement();
						rs_select_seq = stmt_select_seq.executeQuery("select ifnull(max(SEQ),0) + 1 as LASTKEY from BPM_SEQ where TBNAME = '" + forTableName + "'");
						if (rs_select_seq.next()) {
							seq_key = rs_select_seq.getLong("LASTKEY");
						} else {
							seq_key = new Long(1);
						}
					
					Long id_key  = null;
						stmt_select_table_max_key = conn.createStatement();
						rs_select_table_max_key = stmt_select_table_max_key.executeQuery("select ifnull(max("+forColumnName+"),0) as LASTKEY from " +(forTableName.equals("WORKITEM")? "BPM_WORKLIST" : ((useTableNameHeader)?"BPM_":"") + forTableName));
						if (rs_select_table_max_key.next()) {
							id_key = rs_select_table_max_key.getLong("LASTKEY");
						}
					
					Long key = null;
						if (seq_key.longValue() > id_key.longValue()) {
							key = seq_key;
						} else {
							key = new Long(id_key.longValue() + 1);
						}
					
					pstmt_update_seq = conn.prepareStatement("update BPM_SEQ set SEQ = ? , MODDATE = now() where  TBNAME = ?");
					pstmt_update_seq.setLong(1, key);
					pstmt_update_seq.setString(2, forTableName);
					int modcount = pstmt_update_seq.executeUpdate();
					
					if(modcount == 0) {
						pstmt_insert_seq = conn.prepareStatement("insert into BPM_SEQ (TBNAME, SEQ, DESCRIPTION, MODDATE) values(?, ?, ?, now())");
						pstmt_insert_seq.setString(1, forTableName);
						pstmt_insert_seq.setLong(2, key);
						pstmt_insert_seq.setString(3, forTableName);
						pstmt_insert_seq.executeUpdate();
					}
					
					conn.commit();
					
					return key;
					
				} catch (Exception e1) {
					try {
						conn.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					throw new RuntimeException(e1);
				} finally {
					if (stmt_select_seq != null) try { stmt_select_seq.close(); } catch (SQLException e1) {}
					if (rs_select_seq != null) try { rs_select_seq.close(); } catch (SQLException e1) {}
					
					if (stmt_select_table_max_key != null) try { stmt_select_table_max_key.close(); } catch (SQLException e1) {}
					if (rs_select_table_max_key != null) try { rs_select_table_max_key.close(); } catch (SQLException e1) {}
					
					if (pstmt_update_seq != null) try { pstmt_update_seq.close(); } catch (SQLException e1) {}
					if (pstmt_insert_seq != null) try { pstmt_insert_seq.close(); } catch (SQLException e1) {}
					
					if (conn != null) try { conn.setAutoCommit(true); } catch (SQLException e1) {}
					if (conn != null) try { conn.close(); } catch (SQLException e) {}
				}
			}

			@Override
			public void select() throws Exception {
				// TODO Auto-generated method stub
				
			}

			@Override
			public int insert() throws Exception {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int update() throws Exception {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int update(String stmt) throws Exception {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int call() throws Exception {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void addBatch() throws Exception {
				// TODO Auto-generated method stub
				
			}

			@Override
			public int[] updateBatch() throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void beforeFirst() throws Exception {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean previous() throws Exception {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean next() throws Exception {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean first() throws Exception {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void afterLast() throws Exception {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean last() throws Exception {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean absolute(int pos) throws Exception {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public int size() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object get(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object set(String key, Object value) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getString(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Integer getInt(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Long getLong(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Boolean getBoolean(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Date getDate(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public AbstractGenericDAO getImplementationObject() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void releaseResource() throws Exception {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setKeyNumber(Number id) {
				// TODO Auto-generated method stub
				
			}
		};
	}

	public Calendar getNow() throws Exception {
		String sql = "select CURRENT_TIMESTAMP as NOW";

		IDAO nowQuery = (IDAO) create(IDAO.class, sql);
		nowQuery.select();

		if (nowQuery.next()) {
			Calendar now = Calendar.getInstance();
			now.setTime((Date) nowQuery.get("NOW"));
			// now.setTimeZone(TimeZone.getDefault());

			return now;
		} else {
			throw new Exception("Can't get current system date from DB.");
		}
	}

	public String getDBMSProductName() throws Exception {
		return "Cubrid";
	}
}
