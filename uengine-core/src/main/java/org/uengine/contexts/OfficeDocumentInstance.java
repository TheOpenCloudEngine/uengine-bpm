package org.uengine.contexts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessDefinitionFactory;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.ProcessVariablePartResolver;
import org.uengine.kernel.TransactionListener;
import org.uengine.kernel.UEngineException;
import org.uengine.processmanager.TransactionContext;
import org.uengine.util.UEngineUtil;

/**
 * 
 * 
 * @author Lee Hee-byung
 */
public class OfficeDocumentInstance implements Serializable, ProcessVariablePartResolver{
	
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	public final static String FILE_SYSTEM_DIR = GlobalContext.getPropertyString("filesystem.path", ProcessDefinitionFactory.DEFINITION_ROOT);

	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd;
		
		type.removeFieldDescriptor("FilePath");
		
		fd = type.getFieldDescriptor("DocumentDefId");
	}

	public void setPart(ProcessInstance instance, String[] partPath, Object value) throws Exception {
		String fieldName = partPath[1];
		
		if(valueMap==null) valueMap = new HashMap();
		if(value==null) valueMap.remove(fieldName);
		valueMap.put(fieldName, value);
		
		if(!instance.getProcessTransactionContext().getTransactionListeners().contains(this))
			instance.getProcessTransactionContext().addTransactionListener(new TransactionListener(){

				public void beforeCommit(TransactionContext tx) throws Exception {
					saveValueMap();
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
				
			});
	}
	public InputStream openValueXMLStream() throws IOException{
		return new FileInputStream(FILE_SYSTEM_DIR + filePath.replace(".xls", "") + ".xml");
	}
	public Object getPart(ProcessInstance instance, String[] partPath, Object value) throws Exception {
		if(partPath==null || partPath.length == 0) return this;
		String fieldName = partPath[0].toLowerCase();
		
		if(valueMap==null){
			loadValueMap();
		} 

		Object rtnValue = valueMap.get(fieldName);

		
		return rtnValue;
	}

	String documentDefId;
		public String getDocumentDefId() {
			return documentDefId;
		}
		public void setDocumentDefId(String documentDefId) {
			this.documentDefId = documentDefId;
		}
	
	String filePath;
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		
	transient Map valueMap;
		public Map getValueMap() {
			if(valueMap == null) {
				try {
					loadValueMap();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			return valueMap;
		}
		public void saveValueMap() throws Exception{
			FileOutputStream fos = null;
			if(getFilePath()==null) throw new UEngineException("OfficeDocument has not been initialized.");
			//File f = new File(FILE_SYSTEM_DIR + getFilePath().replace(".xls", "") + ".xml");
			//System.out.println("Absolute Path : " + f.getAbsolutePath());
			String calFilePath = UEngineUtil.getCalendarDir();
			File dirToCreate = new File(FILE_SYSTEM_DIR + calFilePath);
			dirToCreate.mkdirs();			
			System.out.println("Absolute Path : " + FILE_SYSTEM_DIR + filePath + File.separator);
			fos = new FileOutputStream(FILE_SYSTEM_DIR + filePath.replace(".xls", "") + ".xml");
			GlobalContext.serialize(valueMap, fos, HashMap.class);
			fos.close();
		}
		public void loadValueMap() throws Exception{
			InputStream fis = openValueXMLStream();
			
			if(getFilePath()==null) throw new UEngineException("OfficeDocument has not been initialized.");
			
			setValueMap((HashMap)GlobalContext.deserialize(fis, HashMap.class));
			
			//load up the values from office document file
		}
		public void setValueMap(Map valueMap) {
			this.valueMap = valueMap;
		}


}
