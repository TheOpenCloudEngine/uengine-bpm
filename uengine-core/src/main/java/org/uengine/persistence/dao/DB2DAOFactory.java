/*
 * Created on 2004. 12. 14.
 */
package org.uengine.persistence.dao;

import org.uengine.util.dao.*;

import java.util.*;
import java.lang.reflect.*;


/**
 * @author Jinyoung Jang
 */
public class DB2DAOFactory extends OracleDAOFactory{
	
	static Hashtable currKeys = new Hashtable();
	
	public WorkListDAO createWorkListDAOForInsertCall(Map options) throws Exception{
		return (WorkListDAO)Proxy.newProxyInstance(
			WorkListDAO.class.getClassLoader(),
			new Class[]{WorkListDAO.class},
			new ConnectiveDAO(
				getConnectionFactory(), 
				true,
				"insert into bpm_worklist(taskId, title, description, endpoint, resName, status, priority, startdate, enddate, duedate, instid, rootinstid, defid, defname, trctag, tool, dispatchOption, parameter, roleName, refRoleName, dispatchParam1)	values(?taskId, ?title, ?description, ?endpoint, ?resName, ?status, ?priority, ?startdate, ?enddate, ?duedate, ?instId, ?rootinstId, ?defId, ?DefName, ?trcTag, ?tool, ?dispatchOption, ?parameter, ?roleName, ?refRoleName, ?dispatchParam1)",
				WorkListDAO.class
			){
				public int call() throws Exception{
					
					Number var_taskId = (Number)get("TASKID");
					
					if(var_taskId!=null){
						WorkListDAO existingWorklist = (WorkListDAO)ConnectiveDAO.createDAOImpl(
							getConnectionFactory(),
							"delete from BPM_WORKLIST where TASKID=?taskId",
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
		return new KeyGeneratorDAO(){

			public Number getKeyNumber() {
				try {
					
					String forTableName  = new String(forWhat);
					String forColumnName = new String(forWhat+"id");
					forColumnName = forColumnName.replaceFirst("Proc","");
					
					if(forWhat.equals("WORKLIST")) forColumnName = "taskid";

					Long key  = null;
					IDAO gdao = ConnectiveDAO.createDAOImpl(
							getConnectionFactory(), 
							"select value(max(seq),0) + 1 as lastKey from bpm_seq where tbname = '" + forTableName + "'",
							IDAO.class
						);
				    gdao.select();
				    if(gdao.next()){
				    	Number currKey = (Number)gdao.get("lastKey");
						key = new Long(currKey.longValue());
				    } else {
				    	key = new Long(1);
				    }
				    
					IDAO udao = ConnectiveDAO.createDAOImpl(
							getConnectionFactory(), 
							//"update bpm_seq set seq = ?seq , moddate = current timestamp where  tbname = '" + forTableName + "'",
							"update bpm_seq set seq = ?seq , moddate = current timestamp where  tbname = ?tbname",
							IDAO.class
						);
					
					udao.set("seq", key);
					udao.set("tbname", forTableName);
					
					int modcount = udao.update();
					if(modcount == 0){
						IDAO idao = ConnectiveDAO.createDAOImpl(
								getConnectionFactory(), 
								//"insert into bpm_seq (tbname, seq, description, moddate) values('"+forTableName+"',"+key.longValue()+",'"+forTableName+"',current timestamp)",
								"insert into bpm_seq (tbname, seq, description, moddate) values(?tbname, ?seq, ?description, current timestamp)",
								IDAO.class
							);
						idao.set("tbname", forTableName);
						idao.set("seq", key);
						idao.set("description", forTableName);
						idao.insert();					
					}
					
					return key;
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					
					throw new RuntimeException(e);
				}				

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
	/*
	public KeyGeneratorDAO createKeyGenerator(final String forWhat, final Map options) throws Exception {
		return new KeyGeneratorDAO(){

			public Number getKeyNumber() {
				try {
					
					String forTableName  = new String(forWhat);
					String forColumnName = new String(forWhat+"id");
					forColumnName = forColumnName.replaceFirst("Proc","");
					
					if(forWhat.equals("WORKLIST")) forColumnName = "taskid";

					
					if(currKeys.containsKey(forWhat)){
						Long key = (Long)currKeys.get(forWhat);
						Long currKeyValue = new Long(key.intValue() + 1);
						currKeys.put(forWhat, currKeyValue);
						
						return currKeyValue;
						
						
					}else{
						IDAO dao = GenericDAO.createDAOImpl(
							DefaultConnectionFactory.create(), 
							"select max("+forColumnName+") as lastKey from bpm_" + forTableName,// + " order by lastKey desc",
							IDAO.class
						);
						dao.select();
						
						if(dao.next()){
							Number currKey = (Number)dao.get("lastKey");
							if(currKey==null){
								Long key = new Long(1);
								currKeys.put(forWhat, key);
								
								return key;
							}
							Long key = new Long(currKey.longValue() + 1);
							currKeys.put(forWhat, key);
							return key;
						}else{
							Long key = new Long(1);
							currKeys.put(forWhat, key);
							
							return key;
						}
						//throw new RuntimeException("Empty resultset from key generation");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					
					throw new RuntimeException(e);
				}				

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
		};
	}
    */
	public String getDBMSProductName() throws Exception {
		return "DB2";
	}
	
	public Calendar getNow() throws Exception {
		IDAO nowQuery = (IDAO)create(IDAO.class, "select current timestamp as NOW from SYSIBM.SYSDUMMY1"); //SELECT DATE_FORMAT(now(), '%Y-%m-%d')
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