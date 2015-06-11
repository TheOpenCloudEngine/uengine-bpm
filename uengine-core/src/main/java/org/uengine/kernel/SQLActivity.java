package org.uengine.kernel;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.io.*;

import org.uengine.contexts.HtmlFormContext;
import org.uengine.processmanager.ProcessTransactionContext;
import org.uengine.util.UEngineUtil;
import org.uengine.util.dao.ConnectionFactory;
import org.uengine.util.dao.DataSourceConnectionFactory;
import org.uengine.util.dao.JDBCConnectionFactory;

/**
 * @author Jinyoung Jang
 */

public class SQLActivity extends DefaultActivity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	String sqlStmt;
		public String getSqlStmt() {
			return sqlStmt;
		}
		public void setSqlStmt(String value) {
			sqlStmt = value;
		}

	ConnectionFactory connectionFactory;
		public ConnectionFactory getConnectionFactory() {
			return connectionFactory;
		}
		public void setConnectionFactory(ConnectionFactory factory) {
			connectionFactory = factory;
		}

//	String connectionString;
//		public String getConnectionString() {
//			return connectionString;
//		}
//		public void setConnectionString(String value) {
//			connectionString = value;
//		}
//
//	String userId;
//		public String getUserId() {
//			return userId;
//		}
//		public void setUserId(String value) {
//			userId = value;
//		}
//
//	String password;
//		public String getPassword() {
//			return password;
//		}
//		public void setPassword(String value) {
//			password = value;
//		}
//
//	String driverClass;
//		public String getDriverClass() {
//			return driverClass;
//		}
//		public void setDriverClass(String value) {
//			driverClass = value;
//		}

	ParameterContext[] parameters;
		public ParameterContext[] getParameters() {
			return parameters;
		}
		public void setParameters(ParameterContext[] contexts) {
			parameters = contexts;
		}
		
	ParameterContext[] selectMappings;
		public ParameterContext[] getSelectMappings() {
			return selectMappings;
		}
		public void setSelectMappings(ParameterContext[] selectMappings) {
			this.selectMappings = selectMappings;
		}
		
	boolean query;
		public boolean isQuery() {
			return query;
		}
		public void setQuery(boolean b) {
			query = b;
		}

	boolean applySingleValueOnly;
		public boolean isApplySingleValueOnly() {
			return applySingleValueOnly;
		}
		public void setApplySingleValueOnly(boolean applySingleValueOnly) {
			this.applySingleValueOnly = applySingleValueOnly;
		}
		
	boolean replaceWithBlankStringIfNull;
		public boolean isReplaceWithBlankStringIfNull() {
			return replaceWithBlankStringIfNull;
		}
		public void setReplaceWithBlankStringIfNull(boolean replaceWithBlankStringIfNull) {
			this.replaceWithBlankStringIfNull = replaceWithBlankStringIfNull;
		}

	public SQLActivity(){
		super("SQL");
		setQuery(false);	
		setReplaceWithBlankStringIfNull(true);
	}

/*	public SQLActivity(String sqlStmt, String[] variantNames){
		super("SQL activity");
		this.sqlStmt = sqlStmt;
		this.variantNames = variantNames;
	}*/
	
	public void executeActivity(ProcessInstance instance) throws Exception{
		
		try{
				Connection con = null; 
		
				boolean uEngineShouldManageTheTransaction = 
					((!instance.getProcessTransactionContext().getProcessManager().isManagedTransaction()) &&
					getConnectionFactory() instanceof DataSourceConnectionFactory);
					
		
				if(getConnectionFactory() == null)
					con = instance.getProcessTransactionContext().getConnection();
				else{
					
					if(uEngineShouldManageTheTransaction){
						con = instance.getProcessTransactionContext().createManagedExternalConnection(
							(DataSourceConnectionFactory)getConnectionFactory()
						);
					}else{
						try{
							con = getConnectionFactory().getConnection();
						}catch(Exception e){
							throw new UEngineException("[SQLActivity]Error when to get connection from connection factory: " + e.getMessage(), e);
						}
					}
				}
				
				String actualSql = evaluateContent(instance, getSqlStmt()).toString().trim();
				
				instance.addDebugInfo("Actual SQL", actualSql);
				
				ParameterContext[] parameters = getParameters();
		//		if(parameters!=null){
		
					ProcessVariableValue[] parameterValues = new ProcessVariableValue[parameters==null ? 0 : parameters.length];
					int maxOfProcessValueCount = 1;
					for(int i=0; i<parameters.length; i++){
						
						if(parameters[i] == null) continue;
						
						if(parameters[i].getTransformerMapping() !=null && parameters[i].getTransformerMapping().getTransformer() != null){
							if(parameters[i].getTransformerMapping().getTransformer() != null){
								Object valueOut = parameters[i].getTransformerMapping().getTransformer().letTransform(instance, parameters[i].getTransformerMapping().getLinkedArgumentName());
								
								if(!(valueOut instanceof ProcessVariableValue)){
									parameterValues[i] = new ProcessVariableValue();
									parameterValues[i].setValue((Serializable)valueOut);
								}else{
									parameterValues[i] = (ProcessVariableValue)valueOut;
								}
							}
							
		
						}else{				
							//if the variable name is a bean expression
							if(parameters[i].getVariable().getName().startsWith("[")){
								parameterValues[i] = new ProcessVariableValue();
		
								String beanPropertyKey = parameters[i].getVariable().getName();
								Serializable value = (Serializable) instance.getBeanProperty(beanPropertyKey);
								parameterValues[i].setValue(value);
							}else{ //case of normal process variable expression
								parameterValues[i] = parameters[i].getVariable().getMultiple(instance, "");
							}
						}
						
						
						if(parameterValues[i].size() > maxOfProcessValueCount ) maxOfProcessValueCount = parameterValues[i].size();
					}
					
					if(isApplySingleValueOnly() && maxOfProcessValueCount > 1) maxOfProcessValueCount = 1;
		
					PreparedStatement prepStmt = con.prepareStatement(actualSql);

					for(int valueCnt = 0; valueCnt < maxOfProcessValueCount; valueCnt++){
//						PreparedStatement prepStmt = con.prepareStatement(actualSql);
						
						for(int i=0; i<parameters.length; i++){
							Object value = null;
							try{
								ProcessVariableValue pvv = parameterValues[i];
								int valuePos = valueCnt;
								if(valuePos >= pvv.size()) valuePos = pvv.size()-1;
								
								pvv.setCursor(valuePos);
								value = pvv.getValue();
								pvv.beforeFirst();

								instance.addDebugInfo((i+1)+"th Parameter Argument(DB column: " + parameters[i].getArgument() +") set from variable [" + (parameters[i].getVariable()!=null ? parameters[i].getVariable().getName() : "NONE") + "]", value);

								if(value!=null && value instanceof Calendar){
								//	value = ((Calendar)value).getTimeInMillis();
									prepStmt.setTimestamp(i+1, new Timestamp(((Calendar)value).getTimeInMillis()));
									continue;
								} else if(value != null && value instanceof Date){
									prepStmt.setTimestamp(i+1, new Timestamp(((Date)value).getTime()));
									continue;
								} else if(value != null && value instanceof String){
		                            value = value.toString().trim();
		                        } else if(value == null && isReplaceWithBlankStringIfNull()){
		                        	value="";
		                        } else if(value != null && value instanceof RoleMapping){
		                        	RoleMapping roleMapping = (RoleMapping) value;
		                        	value = roleMapping.getEndpoint();
		                        }
								
//								if(Number.class.equals(parameters[i].getType()) && parameters[i].getVariable() != null){
								if(parameters[i].getVariable() != null && parameters[i].getVariable().getType() == Number.class){
									if(("" + value).indexOf(",") > -1 ){
										value = ("" + value).replace(",", "");
									}
									try {
										value = new Long(""+value);
									} catch (Exception e) {
										value = new Long(0);
									}
								}
								
		
								prepStmt.setObject(i+1, value);
							}catch(Exception e){
								throw new UEngineException("An error occurred when to set the value: [" + value + "] where [" + (i+1) + "]th parameter: \n" + e.getClass() + ": " + e.getMessage() + "\n (SQL:" + sqlStmt +")", e);
							}
						}
		
						ResultSet rs = null;
						if(isQuery()){
							rs = prepStmt.executeQuery();
							
							instance.addDebugInfo("Result Set", rs);

							if(rs.next()){
								instance.addDebugInfo("Result Set", "has value");
								
								ByteArrayOutputStream resultLog = null;
								
								ProcessVariable [] variables = getProcessDefinition().getProcessVariables();
								
								
								Vector vValue = new Vector(); //lists variable values with HashMap that contains its variable names as keys and the values as its entries.
								Vector vKey = new Vector(); //lists variables names which have been changed with new value
								do{
									ResultSetMetaData rmeta=rs.getMetaData();
									for(int i=1; i<=rmeta.getColumnCount(); i++){
										String fieldName = rmeta.getColumnLabel(i);
										
										//find same fieldname with variable name to bind value
										//							for(int j=0; j<variables.length; j++){
										//								
										//								if(fieldName.toUpperCase().equals(variables[j].getName().toUpperCase())){
										//									HashMap hmResult = setResult(instance, variables[j].getName(), rs, i);
										//									vValue.add(hmResult);
										//									if(!vKey.contains(variables[j].getName()))
										//										vKey.add(variables[j].getName());
										//									break;
										//								}
										//							}
										
										//find same fieldname with argument name to bind by the selectMappings
										for(int j=0; j<selectMappings.length; j++){
											ParameterContext selectMapping = selectMappings[j];
											
											if(fieldName.toUpperCase().equals(selectMapping.getArgument().getText().toUpperCase())){
												HashMap hmResult = setResult(instance, selectMapping.getVariable().getName(), rs, i);
												vValue.add(hmResult);
												
												instance.addDebugInfo("  Bind Value", hmResult.get(selectMapping.getVariable().getName()) + " to "+fieldName);
												
												if(!vKey.contains( selectMapping.getVariable().getName()))
													vKey.add( selectMapping.getVariable().getName());
												break;
											}								
										}	
									}
								}while(!isApplySingleValueOnly() && rs.next());
								
								Vector vPvv = new Vector();
								for(int i=0 ;i < vKey.size() ; i++){
									ProcessVariableValue pvv = new  ProcessVariableValue();
									String key = (String)vKey.get(i);
									for(int j=0 ;j < vValue.size() ; j++){
										HashMap hmTemp = (HashMap)vValue.get(j);
										if(hmTemp.containsKey(key)){
											pvv.setValue(hmTemp.get(key));
											pvv.moveToAdd();
										}
									}
									vPvv.add(pvv);
								}
								
								for (int i = 0; i < vKey.size(); i++) {
									ProcessVariableValue pvv = (ProcessVariableValue) vPvv.get(i);
									pvv.beforeFirst();
									if(pvv.size()==1)
										instance.set("", (String) vKey.get(i), pvv.getValue());
									else
										instance.set("", (String) vKey.get(i), pvv);
								}
								
								
								rs.close();
							}else{
								instance.addDebugInfo("Result Set is ","Empty!");
							}
						}else{
							//TODO: batch update mechanism required for performance enhancement.
							prepStmt.executeUpdate();
						}
						
//						prepStmt.close();
					}
					prepStmt.close();
		//		}
		
				if(getConnectionFactory()!=null && !uEngineShouldManageTheTransaction)
					con.close();

		
				fireComplete(instance);

		}catch(Exception e){

			throw e;
		}
	}
	
	protected HashMap setResult(ProcessInstance instance, String varName, ResultSet rs, int colNum) throws Exception{
		Class type = getProcessDefinition().getProcessVariable(varName).getType();
		
		HashMap hm = new HashMap();
		//review: all types should be supported			
		if(type == String.class){
			String value = rs.getString(colNum);
			if (value == null && isReplaceWithBlankStringIfNull()) {
				value = "";
			}
			hm.put(varName, value);
		}else if(type == Integer.class){
			hm.put(varName, Integer.valueOf(rs.getInt(colNum)));
		}else if(type == Long.class){
			hm.put(varName, Long.valueOf(rs.getLong(colNum)));
		}else if(type == Number.class){
			Object num = rs.getObject(colNum);
			hm.put(varName, num);
//			hm.put(varName, new Integer(rs.getInt(colNum)));
		}else if(type == Boolean.class){
			hm.put(varName, Boolean.valueOf(rs.getBoolean(colNum)));
		}else if(type == java.util.Date.class){
			hm.put(varName,  rs.getTimestamp(colNum));
		}else if(type == Calendar.class){
			java.util.Date date = rs.getTimestamp(colNum);
			Calendar cal = Calendar.getInstance();
			if(date != null){
				cal.setTime(date);
			}			
			hm.put(varName, cal);
		}else{
			//if we don't have type information of the target variable, try use the value from recordset.
			hm.put(varName, rs.getObject(colNum));
			//throw new UEngineException("There's no proper sql type for variable '"+varName+"'. couldn't convert.");
		}
		
		return hm;
	}
	
	@Override
	public Map getActivityDetails(ProcessInstance inst, String locale)
			throws Exception {
		Map details = super.getActivityDetails(inst, locale);
		details.put("query string", evaluateContent(inst, getSqlStmt()).toString());
		
		return details;
	}
	
	public ValidationContext validate(Map options) {
		ValidationContext vc = super.validate(options);
		
		String sql = getSqlStmt();
		
		int paramCnt = 0;
		if(sql!=null)
		for(int i=0; i<sql.length(); i++){
			if(sql.charAt(i) == '?') paramCnt++;
		}
		
		if(getParameters()!=null && paramCnt != getParameters().length){
			vc.add(getActivityLabel() + "Number of parameter does not match with parameter placeholders - '?'.");
		}		
		return vc;
	}
}

