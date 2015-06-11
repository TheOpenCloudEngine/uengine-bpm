package org.uengine.kernel;

import java.sql.*;
import java.util.*;
import java.awt.Component;
import java.io.*;

import javax.swing.JLabel;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.AbstractComponentInputter;
import org.metaworks.inputter.SelectInput;
import org.uengine.contexts.MappingContext;
import org.uengine.processmanager.ProcessTransactionContext;
import org.uengine.util.SQLGeneratorForDBMappingActivity;
import org.uengine.util.UEngineUtil;
import org.uengine.util.dao.ConnectionFactory;
import org.uengine.util.dao.ConnectiveDAO;
import org.uengine.util.dao.DataSourceConnectionFactory;
import org.uengine.util.dao.IDAO;
import org.uengine.util.dao.JDBCConnectionFactory;

/**
 * @author Jinyoung Jang
 */

public class DatabaseMappingActivity extends MappingActivity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	public final static int QUERYMODE_SELECT = 1;
	public final static int QUERYMODE_INSERT = 2;
	public final static int QUERYMODE_INSERT_IF_NOT_EXIST_AND_UPDATE_IF_EXIST = 5;
	public final static int QUERYMODE_UPDATE = 3;
	public final static int QUERYMODE_DELETE = 4;

	public DatabaseMappingActivity(){
		setName("Database Mapping");
		setQueryMode(this.QUERYMODE_INSERT);
	}
	
	public void executeActivity(ProcessInstance instance) throws Exception{
		
		Connection con = null; 

		IDAO dao = ConnectiveDAO.createDAOImpl(instance.getProcessTransactionContext(), null, IDAO.class);

		MappingContext mappingContext = getMappingContext();

		//Using DAO
/*		for(int i=0; i<mappingContext.getMappingElements().length; i++){
			ParameterContext paramContext = mappingContext.getMappingElements()[i];
			String tableAndFieldName = paramContext.getArgument().getText();
			String varName = paramContext.getVariable().getName();

			tableAndFieldName = tableAndFieldName.replace('.','@');
			String[] tableAndFieldNames = tableAndFieldName.split("@");
			String tableName = tableAndFieldNames[0];
			String fieldName = tableAndFieldNames[1];
			
			insertingTableName = tableName;
			intoClause.append(sep + fieldName);
			valuesClause.append(sep + "?" + fieldName);
			sep = ", ";
			
			dao.set(fieldName, instance.get("", varName));
			
		}
		
		dao.getImplementationObject().setTableName(insertingTableName);
		dao.getImplementationObject().createInsertSql();//setSqlStmt("insert into " + insertingTableName + "(" + intoClause + ")values(" + valuesClause + ")");
		dao.insert();
*/		
		
		//Using SQLActivity
		SQLActivity sqlActivity = new SQLActivity(){			
			
			@Override
			public ProcessDefinition getProcessDefinition() {
				// TODO Auto-generated method stub
				return DatabaseMappingActivity.this.getProcessDefinition();
			}

			public void fireComplete(ProcessInstance instance) throws Exception {
				// Disarm firing next step
			}
			
		};

/*		ParameterContext[] parametersForSQLActivity = new ParameterContext[mappingContext.getMappingElements().length];
		String generatedSQL = null;
				
		for(int i=0; i<mappingContext.getMappingElements().length; i++){
			ParameterContext paramContext = mappingContext.getMappingElements()[i];
			String tableAndFieldName = paramContext.getArgument().getText();
			String varName = paramContext.getVariable()!=null ? paramContext.getVariable().getName() : null;
	
			tableAndFieldName = tableAndFieldName.replace('.','@');
			String[] tableAndFieldNames = tableAndFieldName.split("@");
			String tableName = tableAndFieldNames[0];
			String fieldName = tableAndFieldNames[1];
			
			insertingTableName = tableName;
			intoClause.append(sep + fieldName);
			valuesClause.append(sep + "?");
			sep = ", ";
			
			parametersForSQLActivity[i] = new ParameterContext();
			
			if(varName!=null)
				parametersForSQLActivity[i].setVariable(ProcessVariable.forName(varName));
			
			parametersForSQLActivity[i].setType(paramContext.getType());
			parametersForSQLActivity[i].setTransformer(paramContext.getTransformer());
		}
		
		generatedSQL = "insert into " + insertingTableName + "(" + intoClause + ")values(" + valuesClause + ")";*/
		
		SQLGeneratorForDBMappingActivity generatedSQL = new SQLGeneratorForDBMappingActivity(mappingContext);
		
//		sqlActivity.setParentActivity(getProcessDefinition());
		
		sqlActivity.setSqlStmt(generatedSQL.getGeneratedSql(getQueryMode()));
		sqlActivity.setParameters(generatedSQL.getParametersForSQLActivity());
		sqlActivity.setConnectionFactory(getConnectionFactory());
		sqlActivity.setApplySingleValueOnly(isApplySingleValueOnly());
		if(getQueryMode() == DatabaseMappingActivity.QUERYMODE_SELECT){
			sqlActivity.setQuery(true);
			sqlActivity.setSelectMappings(generatedSQL.getSelectMapping());
		} else {
			sqlActivity.setQuery(false);
		}
		sqlActivity.executeActivity(instance);
		
		fireComplete(instance);
	}
	
	String tableName;
		public String getTableName() {
			return tableName;
		}
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}		
	
	ConnectionFactory connectionFactory;
		public ConnectionFactory getConnectionFactory() {
			return connectionFactory;
		}
		public void setConnectionFactory(ConnectionFactory factory) {
			connectionFactory = factory;
		}

	boolean applySingleValueOnly;
		public boolean isApplySingleValueOnly() {
			return applySingleValueOnly;
		}
		public void setApplySingleValueOnly(boolean applySingleValueOnly) {
			this.applySingleValueOnly = applySingleValueOnly;
		}
		
	int queryMode;
		public int getQueryMode() {
			return queryMode;
		}
		public void setQueryMode(int queryMode) {
			this.queryMode = queryMode;
		}

}

