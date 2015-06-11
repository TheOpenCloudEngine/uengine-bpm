package org.uengine.contexts;

import java.io.Serializable;

import org.uengine.kernel.EJBProcessInstance;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.ProcessVariableValue;
import org.uengine.kernel.SpecializedVariableValue;
import org.uengine.kernel.TransactionListener;
import org.uengine.kernel.UEngineException;
import org.uengine.persistence.dao.DAOFactory;
import org.uengine.processmanager.TransactionContext;
import org.uengine.util.dao.ConnectiveDAO;
import org.uengine.util.dao.IDAO;

public class DatabaseSynchronizationOption implements SpecializedVariableValue{
	
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

    // todo db lock isolation level
    public static int ISOLATION_READ_UNCOMMITTED = java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;
    public static int ISOLATION_READ_COMMITTED   = java.sql.Connection.TRANSACTION_READ_COMMITTED;
    public static int ISOLATION_REPEATABLE_READ  = java.sql.Connection.TRANSACTION_REPEATABLE_READ;
    public static int ISOLATION_SERIALIZABLE     = java.sql.Connection.TRANSACTION_SERIALIZABLE;

    String tableName;
	String correlationFieldName;
	String correlatedFieldName;
	String fieldName;
	
	public String getCorrelatedFieldName() {
		return correlatedFieldName;
	}
	public void setCorrelatedFieldName(String correlatedFieldName) {
		this.correlatedFieldName = correlatedFieldName;
	}
	public String getCorrelationFieldName() {
		return correlationFieldName;
	}
	public void setCorrelationFieldName(String correlationFieldName) {
		this.correlationFieldName = correlationFieldName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	
	public Serializable get(ProcessInstance instance, String scope) throws Exception {
		
		IDAO dao;
		
/*		if(getFieldName().equals("BTTM_DCD")){
			System.out.println();
		}
*/		
		if(instance==null) throw new UEngineException("Instance is null");
		
		if("bpm_procinst".equalsIgnoreCase(getTableName()) && instance instanceof EJBProcessInstance){
			dao = ((EJBProcessInstance)instance).getProcessInstanceDAO();
		}else{
			Object keyValue = ((EJBProcessInstance)instance).getProcessInstanceDAO().get(getCorrelatedFieldName());
			String sharedContextKey = "DAO." + getTableName() + "@" + keyValue;
			
			if(keyValue==null) throw new UEngineException("Database Synchronization Process Variable Exception: No value for correlation key [" + getCorrelatedFieldName() + "] of instance [" + instance.getInstanceId() + "]");
			
			dao = (IDAO)instance.getProcessTransactionContext().getSharedContext(sharedContextKey);

			if(dao==null){
				String sql = "select * from " + getTableName() + " where " + getCorrelationFieldName() + "=?" + getCorrelationFieldName();
				
				//if the using database product is DB2, an uncommitted read (phantom read) should be allowed by explicit expression.
				if("DB2".equals(DAOFactory.getInstance(null).getDBMSProductName()))
					sql = sql + " with ur";
				
				dao = ConnectiveDAO.createDAOImpl(instance.getProcessTransactionContext(), sql, IDAO.class);
				
				dao.set(getCorrelationFieldName(), keyValue);
				dao.select();

				if(dao.next()){
					instance.getProcessTransactionContext().setSharedContext(sharedContextKey, dao);
				}else
					return null;
			}else{
                try{
                    dao.beforeFirst();
				    dao.next();
                }catch(Exception e){}
            }
		}

		try{
			if(dao.size() == 1)
				return (Serializable)dao.get(getFieldName());
		}catch(Exception e){
			return (Serializable)dao.get(getFieldName());
		}

		ProcessVariableValue pvv = new ProcessVariableValue();
		do{
			pvv.setValue((Serializable)dao.get(getFieldName()));
			pvv.moveToAdd();
		}while(dao.next());
        pvv.beforeFirst();

        return pvv;
	}
	
	public boolean set(ProcessInstance instance, String scope, Serializable value) throws Exception {
		if("bpm_procinst".equalsIgnoreCase(getTableName()) && instance instanceof EJBProcessInstance){
			IDAO dao = ((EJBProcessInstance)instance).getProcessInstanceDAO();
			dao.set(getFieldName(), value);
			return true;
		}

		Object keyValue = ((EJBProcessInstance)instance).getProcessInstanceDAO().get(getCorrelatedFieldName());

		IDAO dao = (IDAO)instance.getProcessTransactionContext().getSharedContext("DAO." + getTableName() + (keyValue!=null ? "@" + keyValue : ""));
		if(dao==null){
			get(instance, scope);
			dao = (IDAO)instance.getProcessTransactionContext().getSharedContext("DAO." + getTableName() + (keyValue!=null ? "@" + keyValue : ""));
		}
		
		final boolean isExistingTuple;;
		
		if(dao==null){
			dao = ConnectiveDAO.createDAOImpl(instance.getProcessTransactionContext(), null, IDAO.class);
			instance.getProcessTransactionContext().setSharedContext("DAO." + getTableName() + (keyValue!=null ? "@" + keyValue : ""), dao);
			
			isExistingTuple = false;
						
		}else{
			isExistingTuple = true;
		}
		
		if(!dao.getImplementationObject().isAutoSQLGeneration()){ //'isAutoSQLGeneration()' was simply used for flagging whether a transaction listener has been attached.
			final IDAO finalDAO = dao;
			dao.set(getCorrelationFieldName(), ((EJBProcessInstance)instance).getProcessInstanceDAO().get(getCorrelatedFieldName()));
			
			instance.getProcessTransactionContext().addTransactionListener(new TransactionListener(){

				public void beforeCommit(TransactionContext tx) throws Exception {
					finalDAO.getImplementationObject().setTableName(getTableName());
					finalDAO.getImplementationObject().setSqlStmt(null);
					finalDAO.getImplementationObject().setAutoSQLGeneration(true);
					finalDAO.getImplementationObject().setKeyField(getCorrelationFieldName());
					
					if(isExistingTuple)
						finalDAO.getImplementationObject().update();
					else{
						//don't need to determine the existing tuple here since line 127 determines already.
/*						finalDAO.getImplementationObject().select();
						
						finalDAO.getImplementationObject().setSqlStmt(null);
						
						if(finalDAO.next()){
							finalDAO.getImplementationObject().update();
						}else{
*/	
						finalDAO.getImplementationObject().insert();
//						}
					}
				}

				public void beforeRollback(TransactionContext tx) throws Exception {
				}

				public void afterCommit(TransactionContext tx) throws Exception {
					// TODO Auto-generated method stub
					
				}

				public void afterRollback(TransactionContext tx) throws Exception {
					// TODO Auto-generated method stub
					
				}
				
			});

		}
		
		dao.set(getFieldName(), value);
		
		return true;
	}

}
