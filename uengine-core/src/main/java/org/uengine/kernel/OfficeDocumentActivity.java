package org.uengine.kernel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.uengine.contexts.MappingContext;
import org.uengine.contexts.OfficeDocumentInstance;

/**
 * 
 * 
 * @author Lee Hee-byung
 */

public class OfficeDocumentActivity extends HumanActivity {
	
	public OfficeDocumentActivity(){
		super();
		setName("Office Document");
	}
	
	ProcessVariable variableForOfficeDocumentInstance;

		public ProcessVariable getVariableForOfficeDocumentInstance() {
			return variableForOfficeDocumentInstance;
		}	
		public void setVariableForOfficeDocumentInstance(
				ProcessVariable variableForOfficeDocumentInstance) {
			this.variableForOfficeDocumentInstance = variableForOfficeDocumentInstance;
		}
	
	MappingContext mappingContext;

		public MappingContext getMappingContext() {
			return mappingContext;
		}
		public void setMappingContext(MappingContext mappingContext) {
			this.mappingContext = mappingContext;
		}
		
	public String getTool() {
		return "officeDocumentHandler";
	}

	public Map getMappedResult(ProcessInstance instance) throws Exception{
		
		
		
		Map mappedResult = new HashMap();
		OfficeDocumentInstance officeDocInst = instance==null ? (OfficeDocumentInstance)(getVariableForOfficeDocumentInstance().getDefaultValue()) : (OfficeDocumentInstance)(getVariableForOfficeDocumentInstance().get(instance, ""));

/*		InputStream officeDocDefXMLInputStream = ProcessDefinitionFactory.getInstance(instance.getProcessTransactionContext()).getResourceStream(officeDocInst.getDocumentDefId());
		OfficeDocumentDefinition officeDocDef = (OfficeDocumentDefinition) GlobalContext.deserialize(officeDocDefXMLInputStream, OfficeDocumentDefinition.class);
		List fieldList = officeDocDef.getFieldList();
		final Map fieldListHT = new HashMap();
		new ForLoop(){

			public void logic(Object target) {
				FieldDescriptor fd = (FieldDescriptor)target;
				fieldListHT.put(fd.getName(), fd);
			}
			
		}.run(fieldList);*/
		
		
		Map valueMap = new HashMap();
		if(officeDocInst==null) return valueMap;
		
		valueMap = officeDocInst.getValueMap();
		if(valueMap==null && officeDocInst.getFilePath()!=null){
			officeDocInst.loadValueMap();
			mappedResult = officeDocInst.getValueMap();
		}
		
		MappingContext mappingContext = getMappingContext();
		
		ParameterContext[] params = mappingContext.getMappingElements();//getVariableBindings();
		if(params!=null && instance!=null){
			//String script = "";
			String objName = null;
			Serializable objValue = null;
			for (int i = 0; i < params.length; i++) {
				ParameterContext param = params[i];

				String sourceProcessVariable = param.getVariable().getName();
				String targetFormField = param.getArgument().getText();
				System.out.println("sourceProcessVariable = " +sourceProcessVariable);
				System.out.println("targetFormField = " +targetFormField);
				
				String [] targetFormFieldName = targetFormField.split("[.]");
				
				if(getVariableForOfficeDocumentInstance().getName().equals(targetFormFieldName[0])){
					objName = targetFormFieldName[1];
					
					if(sourceProcessVariable.startsWith("["))
						objValue = (Serializable) instance.getBeanProperty(sourceProcessVariable);
					else{
						ProcessVariableValue pvv = instance.getMultiple("", sourceProcessVariable);
						pvv.beforeFirst();
						if(pvv.size()>1){
							String values[] = new String[pvv.size()];
							int j=0;
							do{
								values[j++] = ""+pvv.getValue();
							}while(pvv.next());
							
							objValue = values;
						}else{
							objValue = pvv.getValue();
						}
					}
					
					mappedResult.put(objName.toLowerCase(), objValue);
				}
			}
		}
		
		return mappedResult;
	}
	
	protected void afterComplete(ProcessInstance instance) throws Exception {

		// load up the OfficeDocumentInstance
		OfficeDocumentInstance officeDocumentInstance = (OfficeDocumentInstance) getVariableForOfficeDocumentInstance().get(instance, "");
		if(officeDocumentInstance.getFilePath()==null) throw new UEngineException("You didn't write any document.");
		
		ParameterContext[] params = getMappingContext().getMappingElements();
		for (int i = 0; i < params.length; i++) {
			ParameterContext param = params[i];

			String srcVariableName = param.getVariable().getName();
			String targetFieldName = param.getArgument().getText();
			
			Object value = null;
			
			value = instance.getBeanProperty(srcVariableName);
			
			ProcessVariable pv = getProcessDefinition().getProcessVariable(srcVariableName);
			if(pv == getVariableForOfficeDocumentInstance()){
				
				instance.setBeanProperty(targetFieldName, (Serializable)value);
				
			}
		}
		
		super.afterComplete(instance);
	}
}
