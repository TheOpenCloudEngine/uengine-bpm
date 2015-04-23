/*
 * Created on 2004. 12. 14.
 */
package org.uengine.persistence.dao;

import org.uengine.processmanager.SimpleTransactionContext;
import org.uengine.processmanager.TransactionContext;
import org.uengine.util.dao.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.lang.reflect.*;

import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;


/**
 * @author Jinyoung Jang
 */
public class MySQLDAOFactory extends OracleDAOFactory{
	
	static Hashtable currKeys = new Hashtable();
	
	public WorkListDAO createWorkListDAOForInsertCall(Map options) throws Exception{
		return (WorkListDAO)Proxy.newProxyInstance(
			WorkListDAO.class.getClassLoader(),
			new Class[]{WorkListDAO.class},
			new GenericDAO(
				getConnectionFactory(), 
				"insert into BPM_WORKLIST(TASKID, TITLE, DESCRIPTION, ENDPOINT, RESNAME, STATUS, PRIORITY, STARTDATE, ENDDATE, DUEDATE, INSTID, ROOTINSTID, DEFID, DEFNAME, TRCTAG, TOOL, DISPATCHOPTION, PARAMETER, ROLENAME, REFROLENAME, DISPATCHPARAM1)	values(?TASKID, ?TITLE, ?DESCRIPTION, ?ENDPOINT, ?RESNAME, ?STATUS, ?PRIORITY, ?STARTDATE, ?ENDDATE, ?DUEDATE, ?INSTID, ?ROOTINSTID, ?DEFID, ?DEFNAME, ?TRCTAG, ?TOOL, ?DISPATCHOPTION, ?PARAMETER, ?ROLENAME, ?REFROLENAME, ?DISPATCHPARAM1)",
				WorkListDAO.class
			){
				public int call() throws Exception{
					
					Number var_taskId = (Number)get("TASKID");
					
					if(var_taskId!=null){
						WorkListDAO existingWorklist = (WorkListDAO)GenericDAO.createDAOImpl(
							getConnectionFactory(),
							"delete from BPM_WORKLIST where TASKID=?TASKID",
							WorkListDAO.class
						);
						existingWorklist.setTaskId(var_taskId);
						existingWorklist.update();
					}else{
						KeyGeneratorDAO kg = DAOFactory.getInstance(getConnectionFactory()).createKeyGenerator("WORKLIST", null);
						kg.select();
						kg.next();
								
						Number taskId = kg.getKeyNumber();
						
						set("TASKID", taskId);
					}
				
					int cnt = super.insert();
					
					
					return cnt;
				}

				
			}
		);
	}
	
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
				
//				TransactionContext tc = new SimpleTransactionContext();
//				try {
//					
//					String forTableName  = new String(forWhat);
//					String forColumnName = new String(((useTableNameHeader)?forWhat:"") + "id");
//					forColumnName = forColumnName.replaceFirst("Proc","");
//					forTableName = forTableName.toLowerCase();
//					if(forTableName.equals("workitem")) 	forColumnName = "taskid";
//                    
//					//bpm_seq sequence getting///////////////////////////////////////////////////////////////////
//					Long seq_key  = null;
//					IDAO gdao = ConnectiveDAO.createDAOImpl(
//							tc, 
//							"select ifnull(max(seq),0) + 1 as lastKey from bpm_seq where tbname = '" + forTableName + "'",
//							IDAO.class
//						);
//				    gdao.select();
//				    if(gdao.next()){
//				    	Number currKey = (Number)gdao.get("lastKey");
//				    	seq_key = new Long(currKey.longValue());
//				    } else {
//				    	seq_key = new Long(1);
//				    }
//				    
//                    //table sequence getting/////////////////////////////////////////////////////////////////////
//				    Long id_key  = null;
//					IDAO tdao = ConnectiveDAO.createDAOImpl(
//							tc, 
//							"select ifnull(max("+forColumnName+"),0) as lastKey from " +(forTableName.equals("workitem")? "bpm_worklist" : ((useTableNameHeader)?"bpm_":"") + forTableName) ,
//							IDAO.class
//						);
//					tdao.select();
//					if(tdao.next()){
//					 	Number currKey = (Number)tdao.get("lastKey");
//					 	id_key = new Long(currKey.longValue());
//					} 
//					
//					//update key/////////////////////////////////////////////////////////////////////////////////
//					Long key  = null;
//					if(seq_key.longValue() > id_key.longValue()) key = seq_key;
//					else key = new Long(id_key.longValue()+1);
//					
//					IDAO udao = ConnectiveDAO.createDAOImpl(
//							tc, 
//							"update bpm_seq set seq = ?seq , moddate = now() where  tbname = ?tbname",
//							IDAO.class
//						);
//					
//					udao.set("seq", key);
//					udao.set("tbname", forTableName);
//					//udao.set("preSeq", new Long(key.longValue()-1));
//					
//					//if seq dont't exist,seq insert ////////////////////////////////////////////////////////////
//					int modcount = udao.update();
//					if(modcount == 0){
//						IDAO idao = ConnectiveDAO.createDAOImpl(
//								tc, 
//								"insert into bpm_seq (tbname, seq, description, moddate) values(?tbname, ?seq, ?description, now())",
//								IDAO.class
//							);
//						idao.set("tbname", forTableName);
//						idao.set("seq", key);
//						idao.set("description", forTableName);
//						idao.insert();					
//					}
//					
//					return key;
//					
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					//e.printStackTrace();
//					
//					throw new RuntimeException(e);
//				} finally {
//					try {
//						tc.releaseResources();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}

			}

			public void setKeyNumber(Number id) {
				// TODO Auto-generated method stub
				
			}

			public void select() throws Exception {
				// TODO Auto-generated method stub
				
			}

			public int insert() throws Exception {
				// TODO Auto-generated method stub
				return 0;
			}

			public int update() throws Exception {
				// TODO Auto-generated method stub
				return 0;
			}

			public int call() throws Exception {
				// TODO Auto-generated method stub
				return 0;
			}

			public void beforeFirst() throws Exception {
				// TODO Auto-generated method stub
				
			}

			public boolean previous() throws Exception {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean next() throws Exception {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean first() throws Exception {
				// TODO Auto-generated method stub
				return false;
			}
			
			public void afterLast() throws Exception {
			}
			
			public boolean last() throws Exception {
				return false;
			}

			public int size() {
				// TODO Auto-generated method stub
				return 0;
			}

			public Object get(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			public Object set(String key, Object value) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			public int update(String stmt) throws Exception {
				// TODO Auto-generated method stub
				return 0;
			}

			public void addBatch() throws Exception {
				// TODO Auto-generated method stub
				
			}

			public int[] updateBatch() throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			public String getString(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			public Integer getInt(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			public Long getLong(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			public Boolean getBoolean(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			public Date getDate(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean absolute(int pos) throws Exception {
				// TODO Auto-generated method stub
				return false;
			}

			public AbstractGenericDAO getImplementationObject() {
				// TODO Auto-generated method stub
				return null;
			}

			public void releaseResource() throws Exception {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	public String getSequenceSql(String seqName) throws Exception {
		// TODO Auto-generated method stub
		return "";
	}

	public String getDBMSProductName() throws Exception {
		return "MySQL";
	}
	
	public Calendar getNow() throws Exception {
		IDAO nowQuery = (IDAO)create(IDAO.class, "select now() as NOW"); //SELECT DATE_FORMAT(now(), '%Y-%m-%d')
		nowQuery.select();
		
		if(nowQuery.next()){
			Calendar now = Calendar.getInstance();
			now.setTime((Date)nowQuery.get("NOW"));
			
			return now;
		}else{
			throw new Exception("Can't get current system date from DB.");
		}
	}
}
