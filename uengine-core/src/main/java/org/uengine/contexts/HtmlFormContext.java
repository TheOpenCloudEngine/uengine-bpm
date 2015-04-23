package org.uengine.contexts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.metaworks.FieldDescriptor;
import org.metaworks.Instance;
import org.metaworks.Type;
import org.metaworks.validator.Validator;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessDefinitionFactory;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.ProcessVariablePartResolver;
import org.uengine.kernel.ProcessVariableValue;
import org.uengine.kernel.TransactionListener;
import org.uengine.kernel.UEngineException;
import org.uengine.processmanager.TransactionContext;
import org.uengine.util.UEngineUtil;

/**
 * 
 * 
 * @author <a href="mailto:bigmahler@users.sourceforge.net">Jong-Uk Jeong</a>
 * @version $Id: HtmlFormContext.java,v 1.1 2012/02/13 05:29:41 sleepphoenix4 Exp $
 */
public class HtmlFormContext implements Serializable, ProcessVariablePartResolver, TransactionListener , IFileContent {
	
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	private static final String XML_VALUE_TAG = "__which_is_xml_value";
	private static final String HANDLER_CLASS_NAME = "_value_handler_class";
	private static final String HANDLER_STRING_NAME = "_value_handler_string";
	public final static String FILE_SYSTEM_DIR = GlobalContext.getPropertyString("filesystem.path", ProcessDefinitionFactory.DEFINITION_ROOT);

	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd;
		
		type.removeFieldDescriptor("ValueMap");
		type.removeFieldDescriptor("FilePath");
		
		fd = type.getFieldDescriptor("FormDefId");
		
		fd.setValidators(new Validator[]{
			new Validator(){
			
				public String validate(Object data, Instance instance){
					if(org.uengine.util.UEngineUtil.isNotEmpty((String)data)) return null;
					return ("Should be set");
				}
				
			}
		});

	}
	
	transient ProcessInstance instance;
	transient ProcessVariable variable;
	
    private Method getMethod(Class src, String name) {
        Method meths[] = src.getMethods();
        for (int i = 0; i < meths.length; i++) {
            if (meths[i].getName().equals(name))
                return meths[i];
        }
        return null;
    }
	public Object getFindFieldValue(String fieldName,String addTag) throws Exception{
		Object value = null;
		//if there is XML value for this entry, use it first.
		if(valueMap.containsKey(fieldName+addTag+HANDLER_CLASS_NAME)) {
			String strValue="";
			strValue = (String)valueMap.get(fieldName+HANDLER_CLASS_NAME);
			Class theClass = getClass().getClassLoader().loadClass(strValue);
			
			Method theSetter = getMethod(theClass,"parseValue");
			
			value =theSetter.invoke(theClass.newInstance(), new Object[] { valueMap,fieldName });
		}else if(valueMap.containsKey(fieldName+addTag+HANDLER_STRING_NAME)) {
			String strValue="";
			strValue = (String)valueMap.get(fieldName+HANDLER_STRING_NAME);
			Class theClass = getClass().getClassLoader().loadClass(strValue);
			
			Method theSetter = getMethod(theClass,"parseToString");
			
			value =theSetter.invoke(theClass.newInstance(), new Object[] { valueMap,fieldName });
		}
		
		if(valueMap.containsKey(fieldName+XML_VALUE_TAG+addTag)) {
			String strValue="";
			strValue = (String)valueMap.get(fieldName+XML_VALUE_TAG+addTag);
			
			if(org.uengine.util.UEngineUtil.isNotEmpty(strValue))				
				value = GlobalContext.deserialize((String)strValue,Object.class);
		}
		

		if(value==null && valueMap.containsKey(fieldName+addTag+XML_VALUE_TAG)) {
			String strValue="";
			strValue = (String)valueMap.get(fieldName+addTag+XML_VALUE_TAG);
			
			if(org.uengine.util.UEngineUtil.isNotEmpty(strValue) )				
				value = GlobalContext.deserialize((String)strValue,Object.class);
		}

		if(value==null){
			if(!valueMap.containsKey(fieldName+addTag)){
				value = null;
			}else{
				if(fieldName.indexOf(XML_VALUE_TAG)>-1){
					String strValue = (String)valueMap.get(fieldName+addTag);
					if(org.uengine.util.UEngineUtil.isNotEmpty(strValue) )				
						value = GlobalContext.deserialize((String)strValue,Object.class);
				}else{
					value = valueMap.get(fieldName+addTag);					
				}
			}
		}
		return value;
	}
	public Object getFieldValue(String fieldName) throws Exception{
		fieldName = fieldName.toLowerCase();
		Object value = null;		

		if(valueMap==null){
			loadValueMap();
		}
		//if there is XML value for this entry, use it first.		
		ProcessVariableValue pvv = new ProcessVariableValue();
		int arrayLv = 0; 
		for(int i=0 ; i < valueMap.size();i++){
			String arrTag = (i==0 ? "":"["+(i-1)+"]");
			if(fieldName.indexOf('[')>-1) {
				fieldName = fieldName.substring(0,fieldName.indexOf('['));
			}
			if(	i > 0 && 
				( 
						valueMap.containsKey(fieldName+XML_VALUE_TAG+"[]") 
					|| valueMap.containsKey(fieldName+"[]"+XML_VALUE_TAG)
					|| valueMap.containsKey(fieldName+"[]")
				)
			)	arrayLv=1;
			if(	i > 0 && 
					( 
							valueMap.containsKey(fieldName+XML_VALUE_TAG+"[][]") 
						|| valueMap.containsKey(fieldName+"[][]"+XML_VALUE_TAG)
						|| valueMap.containsKey(fieldName+"[][]")
					)
			)	arrayLv=2;
			
			//if(arrayLv == 0) continue;
			
			if(arrayLv > 1){
				ProcessVariableValue innerPvv = new ProcessVariableValue();
				
				for(int j=0 ; j < valueMap.size() ; j++){
				    String addArrTag = "";
					if(j > 0){
						addArrTag = arrTag +"["+(j-1)+"]";
					}else{
						addArrTag = arrTag;
					}

					value = getFindFieldValue(fieldName , addArrTag);
					
					if(value!=null){
						innerPvv.setValue(value);
						innerPvv.moveToAdd();
						value=null;
					}
				}
				innerPvv.beforeFirst();
				if(innerPvv!=null && innerPvv.getValue() !=null){
					pvv.setValue(innerPvv);
					pvv.moveToAdd();
					innerPvv=null;
				}
			}else {
				value = getFindFieldValue(fieldName , arrTag);
				
				if(value!=null){
					pvv.setValue(value);
					pvv.moveToAdd();
					value=null;
				}
			}
		}
		
		pvv.beforeFirst();
		
		if(pvv.size()==0)
			return null;
		
		if(pvv.size()==1)
			return pvv.getValue();
		else
			return pvv;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setPart(ProcessInstance instance, String[] partPath, Object value) throws Exception {
		//boolean isSimulation = instance instanceof SimulatorProcessInstance;
		
		String fieldName = partPath[1];
		String variableName = partPath[0];
		
		if(valueMap==null) valueMap = new HashMap();
		if(value==null) valueMap.remove(fieldName);
		valueMap.put(fieldName, value);
		
		if(!instance.getProcessTransactionContext().getTransactionListeners().contains(this)){
			this.instance = instance;
			this.variable = instance.getProcessDefinition().getProcessVariable(variableName);
			instance.getProcessTransactionContext().addTransactionListener(this);
		}
		
	}

	public Object getPart(ProcessInstance instance, String[] partPath, Object value) throws Exception {
		if(partPath==null || partPath.length == 0) return this;
		String fieldName = partPath[0].toLowerCase();
		
		if (value instanceof HtmlFormContext) {
			if (((HtmlFormContext) value).getFilePath() == null) {
				return null;
			}
		}
		
		Serializable rtnValue = (Serializable)getFieldValue(fieldName);
		
		return rtnValue;
	}

	String formDefId;
		public String getFormDefId() {
			return formDefId;
		}
		public void setFormDefId(String defId) {
			this.formDefId = defId;
		}
	
	String filePath;
		public String getFilePath() {
			return filePath;
		}
		
	String htmlPath;
		public String getHtmlPath() {
			return htmlPath;
		}
		public void setHtmlPath(String htmlPath) {
			this.htmlPath = htmlPath;
		}

	public InputStream openValueXMLStream() throws IOException{
		return new FileInputStream(FILE_SYSTEM_DIR + getFilePath());
	}

	public InputStream openValueHTMLStream() throws IOException{
		return new FileInputStream(FILE_SYSTEM_DIR + getHtmlPath());
	}

	transient Map valueMap;
		public Map getValueMap() {
			return valueMap;
		}
		
		public void loadValueMap(){
			try{
				InputStream fis = openValueXMLStream();
				setValueMap((HashMap)GlobalContext.deserialize(fis, HashMap.class));
				
				if(getFilePath()==null) throw new UEngineException("Form has not been initialized.");
			}catch(Exception e){
				new UEngineException("Form has not been initialized.");
			}
		}

		public void setValueMap(Map valueMap) {
			this.valueMap = valueMap;
			
			//indexing array depth for each variables
			Set clonedSet = new HashSet();
			clonedSet.addAll(valueMap.keySet());
			Iterator keyIter = clonedSet.iterator();
			while(keyIter.hasNext()) {
				String key = (String)keyIter.next();
				
				int indexOfArraySign = key.indexOf('[');
				if(indexOfArraySign>-1){
					String arrayIndexingKey = key.substring(0, indexOfArraySign); 
					for(int j=0; j<key.length(); j++){
						char charWhereI = key.charAt(j);
						if(charWhereI == '['){
							arrayIndexingKey = arrayIndexingKey + "[]";
						}
					}
					valueMap.put(arrayIndexingKey, "");
				}
			}
		}
		
				
		public void beforeCommit(TransactionContext tx) throws Exception {

			if(getFilePath()==null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS", Locale.KOREA);
				
				String filePath = UEngineUtil.getCalendarDir();
				File dirToCreate = new File(FILE_SYSTEM_DIR + filePath);
				dirToCreate.mkdirs();
			
				String datePrefix = sdf.format(new Date());
				String fileName = instance.getInstanceId() +"_"+datePrefix + ".xml";
				File newFile = new File(FILE_SYSTEM_DIR + filePath+"/"+fileName);
				FileOutputStream fos = new FileOutputStream(newFile);
				GlobalContext.serialize(valueMap, fos, HashMap.class);
				fos.close();
				
				HtmlFormContext formDefInfo = (HtmlFormContext)variable.getDefaultValue();
				String[] formDefID = formDefInfo.getFormDefId().split("@");
				String formDefinitionVersionId = (String) valueMap.get("formdefinitionversionid");
				if(!UEngineUtil.isNotEmpty(formDefinitionVersionId))formDefinitionVersionId= formDefID[1];
			
				
				HtmlFormContext newFormCtx = new HtmlFormContext();
				newFormCtx.setFilePath(filePath+"/"+fileName);
				newFormCtx.setFormDefId(formDefID[0] + "@" + formDefID[1]);
				newFormCtx.setValueMap(valueMap);
				
				variable.set(instance, "", newFormCtx);
			}else{
			
				FileOutputStream fos = null;
				fos = new FileOutputStream(FILE_SYSTEM_DIR + getFilePath());			
				GlobalContext.serialize(valueMap, fos, HashMap.class);
				fos.close();
			}
		}
		
		public void beforeRollback(TransactionContext tx) throws Exception {
			// TODO Auto-generated method stub
			
		}

		public void afterCommit(TransactionContext tx) throws Exception {
			// TODO Auto-generated method stub
			
		}

		public void afterRollback(TransactionContext tx) throws Exception {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public String toString() {
			StringBuilder readable = new StringBuilder();
			
			if(getFilePath()!=null)
				readable.append("<a href='showFormInstance.jsp?filePath=").append(this.getFilePath()).append("'>").append(this.getFilePath()).append("</a>");
			
			return readable.toString();
		
		}
		
		public String[] getPaths() {
		    
		    ArrayList<String> filePaths = new ArrayList<String>();
		    String htmlFilePath = getHtmlPath();
			if (htmlFilePath != null) {
				if (htmlFilePath.startsWith(".")) {
					htmlFilePath = htmlFilePath.substring(2);
				}
				filePaths.add("htmlFilePath:" + htmlFilePath);
	
				// add attached filePaths to return value
				if (this.getValueMap() != null) {
					Set keys = this.getValueMap().keySet();
					for (Object key : keys) {
						Object value = this.getValueMap().get(key);
						if (value instanceof String) {
							if (value.toString().contains("org.uengine.contexts.FileContext") && value.toString().contains("<path>") && value.toString().contains("</path>")
									&& value.toString().indexOf("<path>") < value.toString().indexOf("</path>")) {
								String attachedFilePath = value.toString().substring(value.toString().indexOf("<path>") + 6, value.toString().indexOf("</path>"));
								filePaths.add(attachedFilePath);
							}
						}
					}
				}
	
			}
			
//		    for (Object key : keys) {
//			Object value = this.getValueMap().get(key);
//			if(value instanceof org.uengine.contexts.FileContext) {
//			    org.uengine.contexts.FileContext fileContext = (org.uengine.contexts.FileContext)value;
//			    filePaths.add(fileContext.getPath());
//			}
//		    }
		    
		    return filePaths.toArray(new String[filePaths.size()]);
		}
		
		

}
