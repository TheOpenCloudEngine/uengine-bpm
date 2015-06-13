package org.uengine.persistence.processvariable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.uengine.contexts.IFileContent;
import org.uengine.kernel.DefaultProcessInstance;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.ProcessVariableValue;
import org.uengine.persistence.AbstractDAOType;
import org.uengine.processmanager.TransactionContext;
import org.uengine.util.UEngineUtil;
import org.uengine.util.dao.ConnectiveDAO;

public class ProcessVariableDAOType extends AbstractDAOType {
	
	public static ProcessVariableDAOType getInstance(TransactionContext tc) {
		return (ProcessVariableDAOType)getInstance(ProcessVariableDAOType.class, tc);
	}	
	
	protected ProcessVariableDAO createDAOImpl(String sql) throws Exception {
		ProcessVariableDAO processVariable = (ProcessVariableDAO)ConnectiveDAO.createDAOImpl(getTransactionContext(), sql, ProcessVariableDAO.class);
		return processVariable;
	}	
	
	public final static int TYPE_NULL 			= -2;
	public final static int TYPE_ANY 			= -1;
	public final static int TYPE_STRING 		= 1;
	public final static int TYPE_INTEGER 		= 2;
	public final static int TYPE_LONG 			= 3;
	public final static int TYPE_BOOLEAN 		= 4;
	public final static int TYPE_DATE 			= 5;
	public final static int TYPE_CALENDAR		= 6;	
	public final static int TYPE_FILE			= 7;	
	public final static int TYPE_LONGSTRING 	= 8;
	public final static int TYPE_LONGANY 	= 11;
	
	public final static int TYPE_FILECONTENT 	= 9;
	
	public final static int TYPE_AUTODETECT		= 0;	
	public final static int TYPE_XML			= 10;
	

	public String updateValue_SQL = 
		"UPDATE BPM_PROCVAR " + 
		"SET DataType = ?DataType, ValueString = ?ValueString, ValueLong = ?ValueLong, ValueBoolean = ?ValueBoolean, ValueDate = ?ValueDate, FileContent = ?FileContent, ?HtmlFilePath" +
		"WHERE InstId = ?InstId " +
		"AND KeyString = ?KeyString";   
   
	public String insertValue_SQL = GlobalContext.getSQL("processvariable.insertvalue");

	public String deleteValue_SQL = 
		"DELETE FROM BPM_PROCVAR " + 
		"WHERE instId = ?instId " +
		"AND KeyString = ?KeyString";   
	
	public String deleteValueBatch_SQL = 
		"DELETE FROM BPM_PROCVAR " + 
		"WHERE instId = ?instId " +
		"AND KeyString in (?KeyString)";

	public String getValue_SQL = 
		"SELECT KeyString, DataType, ValueString, ValueLong, ValueBoolean, ValueDate " +
		"FROM BPM_PROCVAR " +
		"WHERE instId = ?instId " +
		"AND KeyString = ?KeyString " +
		"ORDER BY varIndex";

	public String getAllValue_SQL = 
		"SELECT trctag, KeyName, IsProperty, KeyString, DataType, ValueString, ValueLong, ValueBoolean, ValueDate, VarIndex " +
		"FROM BPM_PROCVAR " +
		"WHERE instId = ?instId order by VARID";		
   
	public void updateValue (
			String instanceId,

			String tracingTag,
			String key,
			boolean isProperty,

			String keyString,
			Object value,
			int dataType
		) throws Exception {
		//TODO [�ӽ�] ��� �̷��� ����		
		ProcessVariableDAO processVariableToDelete = createDAOImpl(deleteValue_SQL);
		processVariableToDelete.setKeyString(keyString);
		processVariableToDelete.setInstId(new Long(instanceId));
		processVariableToDelete.update();		
		//

		//ProcessVariableDAO processVariable = createDAOImpl("updateValue_SQL");
		//updateValue(processVariable, instanceId, keyString, value, dataType);
		
		insertValue(
				instanceId,
				
				tracingTag,
				key,
				isProperty,
				
				keyString,
				value,
				dataType
		);
	}   

	public void deleteValue (String instanceId, Iterator keyStrings) throws Exception {
		ProcessVariableDAO processVariableToDelete = createDAOImpl(deleteValueBatch_SQL);
		
		//String keyStringsStr = "";
		//String sep = "'";
		if(keyStrings!=null){
			boolean addOnce = keyStrings.hasNext();
			while(keyStrings.hasNext()){
	
				processVariableToDelete.setKeyString((String)keyStrings.next());
				processVariableToDelete.setInstId(new Long(instanceId));
				processVariableToDelete.addBatch();
	//			keyStringsStr += sep + keyStrings.next();
	//			sep="', '";
			}
			
			if(addOnce)
				processVariableToDelete.updateBatch();				
		}
//		keyStringsStr = keyStringsStr + "'";
		
	}

	public void insertValue (
			String instanceId,
			String tracingTag,
			String key,
			boolean isProperty,
			String keyString, 
			Object value, 
			int dataType
			
			) throws Exception {
		
		insertValue(
				instanceId,
				
				tracingTag,
				key,
				isProperty,

				keyString,
				value,
				dataType,
				0); 
	}

	public void insertValue (
			String instanceId,

			String tracingTag,
			String key,
			boolean isProperty,

			String keyString,
			Object value,
			int dataType,
			int index
		) throws Exception {		
		ProcessVariableDAO processVariable = createDAOImpl(insertValue_SQL);
		updateValue(
				processVariable,
				instanceId,
				
				tracingTag,
				key,
				isProperty,

				keyString,
				value,
				dataType,
				index,
				false
			);
	}
	
	public ProcessVariableDAO createProcessVariableDAOForBatchInsert() throws Exception{
		ProcessVariableDAO processVariable = createDAOImpl(insertValue_SQL);
		return processVariable;
	}
	
	public void insertValueBatch(ProcessVariableDAO processVariable,
		String instanceId,

		String tracingTag,
		String key,
		boolean isProperty,
		
		String keyString,
		Object value,
		int dataType,
		int index
		) throws Exception {
		updateValue(
				processVariable,
				instanceId,
				
				tracingTag,
				key,
				isProperty,

				keyString,
				value,
				dataType,
				index,
				true);
	}
	
	private void updateValue(
		ProcessVariableDAO processVariable,
		String instanceId,

		String tracingTag,
		String key,
		boolean isProperty,
		
		String keyString,
		Object value,
		int dataType,
		int index,
		boolean isBatch
		) throws Exception {
	
/**
 * change to insert NULL value explicitly so that the value history can persists
 */
		//batch insert�� ���� �ϰ���8�� ������ ������ �� ����; �¿� �ʿ䰡 ��=.
/*		if(dataType == TYPE_NULL){
			
			if(!isBatch){
				ProcessVariableDAO processVariableToDelete = createDAOImpl(deleteValue_SQL);
				processVariableToDelete.setKeyString(keyString);
				processVariableToDelete.setInstId(new Long(instanceId));
				processVariableToDelete.update();
			}
			return;
		}
*/		
		processVariable.setInstId(new Long(instanceId));
		processVariable.setKeyString(keyString);
		processVariable.setDataType(dataType);
		processVariable.setValueString("");
		processVariable.setValueLong(0);
		processVariable.setValueBoolean(false);
		processVariable.setValueDate(null);
		
		processVariable.setTrcTag(tracingTag);
		processVariable.setKeyName(key);
		processVariable.setIsProperty(new Boolean(isProperty));
		
		processVariable.setVarIndex(new Long(index));
		
		processVariable.setFileContent(null);
		processVariable.setHtmlFilePath(null);
		
		if (dataType == TYPE_STRING || dataType == TYPE_ANY)
			if (((String) value).getBytes().length > 4000) {
				if(dataType == TYPE_STRING)
					dataType = TYPE_LONGSTRING;
				else
					dataType = TYPE_LONGANY;
				
				processVariable.setDataType(dataType);
			}
		
		switch(dataType){
			case TYPE_ANY: //not breaking here means TYPE_ANY will be treated as a String
			case 1: processVariable.setValueString((String)value);
				break;
			case 2: processVariable.setValueLong(((Number)value).longValue());
				break;
			case 3: processVariable.setValueLong(((Number)value).longValue());
				break;
			case 4: processVariable.setValueBoolean( ((Boolean)value).booleanValue() );
				break;
			case 5: processVariable.setValueDate((Date)value);
				break;
			case 6: 
					processVariable.setValueDate(new Timestamp(((Calendar)value).getTimeInMillis()));
				break;
			case TYPE_LONGSTRING: case TYPE_LONGANY:
				String filePath = saveLongString(keyString, (String) value);
				processVariable.setValueString(filePath);
				break;
			case 9: if(value instanceof IFileContent){
			    		IFileContent fc = (IFileContent)value;
			    		StringBuffer filePaths = new StringBuffer();
			    		for (int i = 0; i < fc.getPaths().length; i++) {
			    		    if(fc.getPaths()[i].startsWith("htmlFilePath:")) {
			    			processVariable.setHtmlFilePath(fc.getPaths()[i].replaceFirst("htmlFilePath:", ""));
			    		    } else {
        			    		    filePaths.append(fc.getPaths()[i]);
        			    		    filePaths.append(";");
			    		    }
					}
			    		if(!"".equals(filePaths.toString())) {
			    			processVariable.setFileContent(filePaths.toString());
			    		}
			    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
			    		GlobalContext.serialize(value, bos, String.class);
									//encoding
			    		String xml = bos.toString(GlobalContext.DATABASE_ENCODING);
				    	processVariable.setValueString(xml);
				}
			    	break;
		}	
		
		if(isBatch)
			processVariable.addBatch();
		else
			processVariable.update();
	}

	
	public Serializable get(String instanceId, String scopeByTracingTag, String key) throws Exception {
		return get(instanceId, scopeByTracingTag, key, TYPE_AUTODETECT);
	}
	
	public Serializable get(String instanceId, String scopeByTracingTag, String key, int type) throws Exception {		
		ProcessVariableDAO processVariable = createDAOImpl(getValue_SQL); 
		processVariable.setInstId(new Long(instanceId));
		processVariable.setKeyString(scopeByTracingTag + ":" + key);
		processVariable.select();
		if ( processVariable.size() > 0 ) processVariable.next();
			
		return getDeserializedValue(processVariable, key, type);
	}

	public ProcessVariableValue getAsProcessVariableValue(String instanceId, String scopeByTracingTag, String key) throws Exception {		
		ProcessVariableDAO processVariable = createDAOImpl(getValue_SQL); 
		processVariable.setInstId(new Long(instanceId));
		processVariable.setKeyString(scopeByTracingTag + ":" + key);
		processVariable.select();
				
		ProcessVariableValue pvv = new ProcessVariableValue();
		pvv.setName(key);
		
		while(processVariable.next()){
			Serializable value = getDeserializedValue(processVariable, key, TYPE_AUTODETECT);
			pvv.setValue(value);
			pvv.moveToAdd();
		}

		pvv.beforeFirst();
		
		return pvv;
	}
	
	
	public Map getAll(String instanceId) throws Exception{
		ProcessVariableDAO processVariable = createDAOImpl(getAllValue_SQL);
		processVariable.setInstId(new Long(instanceId));
		processVariable.select();
		
		Hashtable variableHT = new Hashtable();
		
		while (processVariable.next()) {
			Object value = getDeserializedValue(processVariable);
			
			if ( value != null ){
				variableHT.put(processVariable.getKeyString(), value);
			}
		}		
		
		return variableHT;
	}	
	
	public DefaultProcessInstance getAllVariablesAsDefaultProcessInstance(String instanceId) throws Exception{
		ProcessVariableDAO processVariable = createDAOImpl(getAllValue_SQL);
		processVariable.setInstId(new Long(instanceId));
		processVariable.select();
		
		DefaultProcessInstance defaultProcessInstance = new DefaultProcessInstance();
		
		while (processVariable.next()) {
			Serializable value = getDeserializedValue(processVariable);
			String scope = processVariable.getTrcTag();

			if(scope==null) scope = "";
			
			String key = processVariable.getKeyName();
			boolean isProperty = processVariable.getIsProperty().booleanValue();
			
			//if ( value != null ){
				if(isProperty){
					defaultProcessInstance.setProperty(scope, key, value);
				}else if(processVariable.getVarIndex().intValue() > 0)
					defaultProcessInstance.add(scope, key, value, processVariable.getVarIndex().intValue());
				else
					defaultProcessInstance.set(scope, key, value);
			//}
		}
		
		return defaultProcessInstance;
	}	
	

	private Serializable getDeserializedValue(ProcessVariableDAO processVariable) throws Exception {
		String keyString = processVariable.getKeyString();
		return getDeserializedValue(processVariable, keyString.substring(keyString.indexOf(':')));
	}
	
	
	private Serializable getDeserializedValue(ProcessVariableDAO processVariable, String key) throws Exception {
		return getDeserializedValue(processVariable, key, TYPE_AUTODETECT);
	}
	
	private Serializable getDeserializedValue(ProcessVariableDAO processVariable, String key, int dataType) throws Exception {
		
		if ( processVariable.size() < 1 ) return null;		
		
		Object value = null;
			
		Vector parts = getParts(key);
		if(parts.size()>1) key=(String)parts.elementAt(0);

		int type = TYPE_ANY;
		type = processVariable.getDataType();
		
		if ( dataType == TYPE_XML) {
			return processVariable.getValueString();
		}
		
		if( type > TYPE_ANY && type != TYPE_FILECONTENT && type != TYPE_LONGANY ) {
			switch(type){
				case 1: 
					value = processVariable.getValueString();
					break;
				case 2: 
					value = new Integer((int)processVariable.getValueLong());
					break;
				case 3: 
					value = new Long(processVariable.getValueLong());
					break;
				case 4: 
					value = new Boolean(processVariable.getValueBoolean());
					break;
				case 5:	
					value = processVariable.getValueDate();
					break;
				case 6: 
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(processVariable.getValueDate().getTime()); 
					value = calendar;
					break; 
				case TYPE_LONGSTRING:
					value = loadLongString(processVariable.getValueString());
					break;
			}				
		} else if(type == TYPE_NULL) {
			;
		
//		} else if(type == TYPE_XML) {
		}  else {
			
			if(type == TYPE_LONGANY){
				value = loadLongString(processVariable.getValueString());
			}else
				value = processVariable.getValueString();
		
			if(value==null || value.toString().trim().length()==0) return null;
		
			ByteArrayInputStream bis = new ByteArrayInputStream(value.toString().getBytes(GlobalContext.DATABASE_ENCODING));
			try{
				value = GlobalContext.deserialize(bis, String.class);
			}catch(Exception e){
				System.out.println("===========================HARD-TO-FIND-BUG==> " + e);
				throw e;
			}			
		}
		
		if(parts.size()==1) {
			value = (Serializable)value;
		} else {
			parts.remove(0); //remove the key part
			value = (Serializable)getObjectAtPart(value, parts);
		}
		
		return (Serializable)value;	
	}

	
	private Object getObjectAtPart(Object src, Vector parts){
		Object temp = src;						
		
		Enumeration enu = parts.elements();
		while(enu.hasMoreElements()){
			try{
				String elementName = (String)enu.nextElement();
				temp = temp.getClass().getMethod("get"+elementName, new Class[]{}).invoke(temp, new Object[]{});
			}catch(Exception e){
				e.printStackTrace();
				return src;
			}
		}
		
		return temp;
	}
	
	private Vector getParts(String key){		
		String fullKey = key;
		Vector parts = new Vector();

		if((key.indexOf('.')) > -1){
			StringTokenizer tokenizer = new StringTokenizer(key, ".");

			while(tokenizer.hasMoreElements()){				
				parts.add(""+tokenizer.nextToken());
			}
		}else parts.add(key);		

		return parts;
	}
	
	private String saveLongString(String keyString, String value) throws Exception {
		String fileSystem = GlobalContext.getPropertyString(
				"filesystem.path",
				"." + File.separatorChar + "uengine" + File.separatorChar + "fileSystem" + File.separatorChar
		);
		
		keyString = keyString.substring(1, keyString.length()).replace(":", ".");
		
		String dirPath = UEngineUtil.getCalendarDir();
		File dirToCreate = new File(fileSystem + dirPath);
		if (!dirToCreate.exists())
			dirToCreate.mkdirs();

		String filePath = dirPath + File.separatorChar + keyString + value.hashCode() + System.currentTimeMillis() + ".txt";
		
		BufferedWriter bw = null;
		try {
			//bw = new BufferedWriter(new FileWriter(fileSystem + filePath));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileSystem + filePath), GlobalContext.ENCODING));
			bw.write(value);
			bw.flush();
		} catch (IOException e) {
			//e.printStackTrace();
			throw e;
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return filePath;
	}
	
	private String loadLongString(String filePath) throws Exception {
		String fileSystem = GlobalContext.getPropertyString(
				"filesystem.path",
				"." + File.separatorChar + "uengine" + File.separatorChar + "fileSystem" + File.separatorChar
		);
		
		File file = new File(fileSystem + filePath);

		StringBuffer totalStr = new StringBuffer();
		if (file.exists()) {
			char[] buff = new char[1024];
	        int len = -1;
	        BufferedReader br = null;
	        
			try {
				//br = new BufferedReader(new FileReader(file));
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file), GlobalContext.ENCODING));
				
				while ((len = br.read(buff)) != -1) {
					totalStr.append(new String(buff, 0, len));
				}
			} catch (FileNotFoundException e) {
//				e.printStackTrace();
				throw e;
			} catch (IOException e) {
//				e.printStackTrace();
				throw e;
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			throw new FileNotFoundException(fileSystem + filePath + " 를 찾을 수 없습니다.");
		}

		return totalStr.toString();
	}
}
