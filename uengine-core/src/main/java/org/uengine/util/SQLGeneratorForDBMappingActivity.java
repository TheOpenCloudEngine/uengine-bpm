package org.uengine.util;

import java.io.Serializable;
import java.util.Vector;

import org.uengine.contexts.MappingContext;
import org.uengine.contexts.TextContext;
import org.uengine.kernel.DatabaseMappingActivity;
import org.uengine.kernel.MappingElement;
import org.uengine.kernel.ParameterContext;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.ProcessVariableValue;

public class SQLGeneratorForDBMappingActivity {
	
	ParameterContext[] parametersForSQLActivity;
	ParameterContext[] selectMapping;
	MappingContext mappingContext;
	
	public SQLGeneratorForDBMappingActivity(MappingContext mappingContext){
		this.mappingContext = mappingContext;
	}
	
	public String getGeneratedSql(int queryMode){
		
		String generatedSQL = null;
		String insertingTableName = null;
		StringBuffer intoClause = new StringBuffer();
		StringBuffer valuesClause = new StringBuffer();
		StringBuffer whereClause = new StringBuffer();
		Vector whereClausePCList = new Vector();
		Vector selectFieldAndVariables = new Vector();
		String sep = "";
		String whereSep = "";
		boolean existKey = false;
		int parametersForSQLActivityCnt = 0;
		
		if(queryMode != DatabaseMappingActivity.QUERYMODE_SELECT){
			parametersForSQLActivity = new ParameterContext[mappingContext.getMappingElements().length];
		}
		
		for(int i=0; i<mappingContext.getMappingElements().length; i++){
			ParameterContext paramContext = mappingContext.getMappingElements()[i];
			String tableAndFieldName = paramContext.getArgument().getText();
			ProcessVariable variable = paramContext.getVariable();
			boolean isKey = ((MappingElement)paramContext).isKey();
	
			tableAndFieldName = tableAndFieldName.replace('.','@');
			String[] tableAndFieldNames = tableAndFieldName.split("@");
			String tableName = tableAndFieldNames[0];
			String fieldName = tableAndFieldNames[1];			
			insertingTableName = tableName;
			
			if(isKey && !(queryMode == DatabaseMappingActivity.QUERYMODE_INSERT)){
				whereClause.append(whereSep + fieldName + "=? ");
				whereSep = "and ";
				
				whereClausePCList.add(paramContext);
				existKey = true;
			}else{
				
				boolean ommitInParameters = (queryMode == DatabaseMappingActivity.QUERYMODE_UPDATE) && (queryMode == DatabaseMappingActivity.QUERYMODE_UPDATE && isKey);
				
				if(queryMode == DatabaseMappingActivity.QUERYMODE_INSERT){
					intoClause.append(sep + fieldName);
					valuesClause.append(sep + "?");
					sep = ", ";
				}else if(queryMode == DatabaseMappingActivity.QUERYMODE_SELECT){
					intoClause.append(sep + fieldName);
//					intoClause.append(sep + fieldName + " as " + varName);
					sep = ", ";	
					Vector fieldAndVariable = new Vector();
					fieldAndVariable.add(fieldName);
					fieldAndVariable.add(variable);
					selectFieldAndVariables.add(fieldAndVariable);
				}else if(queryMode == DatabaseMappingActivity.QUERYMODE_UPDATE && !isKey /* if the mapped field is key, it should be ommited in the set clause */ ){
					intoClause.append(sep + fieldName + "=?");
					sep = ", ";				
				}else if(queryMode == DatabaseMappingActivity.QUERYMODE_INSERT_IF_NOT_EXIST_AND_UPDATE_IF_EXIST){

				}
				
				if(!ommitInParameters && parametersForSQLActivity != null){
					parametersForSQLActivity[parametersForSQLActivityCnt] = paramContext;
					parametersForSQLActivityCnt++;
				}
			}
		}
		
		/*if(queryMode == DatabaseMappingActivity.QUERYMODE_SELECT){
			
			this.parametersForSQLActivity = new ParameterContext[whereClauseVarList.size()];
			parametersForSQLActivityCnt = 0;
						
		}*/
		if(queryMode == DatabaseMappingActivity.QUERYMODE_SELECT){
			selectMapping = new ParameterContext[selectFieldAndVariables.size()];
			parametersForSQLActivity = new ParameterContext[whereClausePCList.size()];
			for(int i=0; i< selectFieldAndVariables.size();i++){
				selectMapping[i] = new ParameterContext();
				Vector fieldAndVariable = (Vector) selectFieldAndVariables.get(i);
				TextContext tc = new TextContext();
				tc.setText((String)fieldAndVariable.get(0));
				selectMapping[i].setArgument(tc);
				selectMapping[i].setVariable((ProcessVariable)fieldAndVariable.get(1));
			}		
		}
		
		//appends parameterContext for where clauses into existing value place holders those appended above
		for(int i = 0; i < whereClausePCList.size() ; i++){
			ParameterContext pc = (ParameterContext) whereClausePCList.get(i);
			parametersForSQLActivity[parametersForSQLActivityCnt] = pc;
			parametersForSQLActivityCnt++;
		}
		
		if(queryMode == DatabaseMappingActivity.QUERYMODE_INSERT){
			generatedSQL = "insert into " + insertingTableName + "(" + intoClause + ") values(" + valuesClause + ")";
		}else if(queryMode == DatabaseMappingActivity.QUERYMODE_SELECT){
			generatedSQL = "select " + intoClause + " from " + insertingTableName;
		}else if(queryMode == DatabaseMappingActivity.QUERYMODE_UPDATE){
			generatedSQL = "update " + insertingTableName + " set " + intoClause;
		}else if(queryMode == DatabaseMappingActivity.QUERYMODE_INSERT_IF_NOT_EXIST_AND_UPDATE_IF_EXIST){
			generatedSQL = "";
		}else if(queryMode == DatabaseMappingActivity.QUERYMODE_DELETE){
			generatedSQL = "delete from " + insertingTableName;
		}
		
		if(existKey){
			generatedSQL = generatedSQL + " where " + whereClause;
		}		
		System.out.println("[This sql was generated from DatabaseMappingActivity] SQL=" + generatedSQL);
		return generatedSQL;
	}
	
	public ParameterContext[] getParametersForSQLActivity(){
		return this.parametersForSQLActivity;
	}
	
	public ParameterContext[] getSelectMapping(){
		return this.selectMapping;
	}

}
