/*
 * Created on 2007. 1. 21.
 */
package org.uengine.persistence.dao;

import java.lang.reflect.Proxy;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.uengine.util.dao.AbstractGenericDAO;
import org.uengine.util.dao.ConnectiveDAO;
import org.uengine.util.dao.IDAO;


/**
 * 
 * 
 * @author <a href="mailto:bigmahler@users.sourceforge.net">Jong-Uk Jeong</a>
 * @version MSsqlDAOFactory.java v1.0 2007. 1. 29. 오후 3:20:45
 */
public class MSsqlDAOFactory extends OracleDAOFactory{
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
						KeyGeneratorDAO kg = DAOFactory.getInstance(getConnectionFactory()).createKeyGenerator("WorkList", null);
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
					
					if(forWhat.equals("WorkList")) forColumnName = "taskid";

					Long key  = null;
					IDAO gdao = ConnectiveDAO.createDAOImpl(
							getConnectionFactory(), 
							"select isNull(max(seq),0) + 1 as lastKey from bpm_seq where tbname = '" + forTableName + "'",
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
							"update bpm_seq set seq = ?seq , moddate = getdate() where  tbname = ?tbname",
							IDAO.class
						);
					
					udao.set("seq", key);
					udao.set("tbname", forTableName);
					
					int modcount = udao.update();
					if(modcount == 0){
						IDAO idao = ConnectiveDAO.createDAOImpl(
								getConnectionFactory(), 
								"insert into bpm_seq (tbname, seq, description, moddate) values(?tbname, ?seq, ?description, getdate())",
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
	
	public String getDBMSProductName() throws Exception {
		return "MSsql";
	}
	
	public Calendar getNow() throws Exception {
		//IDAO nowQuery = (IDAO)create(IDAO.class, "select convert(varchar(10), getdate(), 120) as now");
		IDAO nowQuery = (IDAO)create(IDAO.class, "select getdate() as now");
		nowQuery.select();
		
		if(nowQuery.next()){
			Calendar now = Calendar.getInstance();
			now.setTime((Date)nowQuery.get("now"));
			
			return now;
		}else{
			throw new Exception("Can't get current system date from DB.");
		}
	}
	
	
	public String getSequenceSql(String seqName) throws Exception {
		// TODO Auto-generated method stub
		return "";
	} 
}