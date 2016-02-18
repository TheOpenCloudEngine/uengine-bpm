package org.uengine.kernel;

import java.io.Serializable;
import java.util.Map;

import org.metaworks.Type;
import org.uengine.contexts.TextContext;
import org.uengine.persistence.processinstance.ProcessInstanceDAO;
import org.uengine.util.ExpressionFormatter;



public class ListField implements Serializable{
	
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public ListField(){
	}
	
	public ListField (String displayNameParam, ListFieldType listFieldType){
		if( displayName != null ){
			displayName.setText(displayNameParam);
		}
		this.listFieldType = listFieldType;
	}
	
	public static void metaworksCallback_changeMetadata(Type type){
		/*
		FieldDescriptor fd;

		fd = type.getFieldDescriptor("ListFieldType");
		
		//ArrayList fieldNames = new ArrayList();
		ArrayList fieldValues = new ArrayList();
		try {
			ProcessInstanceDAO piDAO;
			WorkListDAO wlDAO;
	
			wlDAO = (org.uengine.persistence.dao.WorkListDAO)GenericDAO.createDAOImpl(org.uengine.persistence.dao.WorkListDAO.class);
			
			for(Iterator i = wlDAO.getImplementationObject().getPropertyTypes().keySet().iterator(); i.hasNext();){
				String fieldName = (String)i.next();
				//fieldNames.add(fieldName);
				WorkListTableListFieldType listFieldType = new WorkListTableListFieldType();
				listFieldType.setFieldName(fieldName);
				fieldValues.add(listFieldType);
			}
			
			piDAO = (ProcessInstanceDAO)GenericDAO.createDAOImpl(ProcessInstanceDAO.class);
			
			for(Iterator i = piDAO.getImplementationObject().getPropertyTypes().keySet().iterator(); i.hasNext();){
				String fieldName = (String)i.next();
				//fieldNames.add(fieldName);
				InstanceTableListFieldType listFieldType = new InstanceTableListFieldType();
				listFieldType.setFieldName(fieldName);
				fieldValues.add(listFieldType);
			}
			
			ProcessDesigner pdr = ProcessDesigner.getInstance();
			ProcessDefinition pd = (ProcessDefinition)pdr.getProcessDefinitionDesigner().getActivity();
			ProcessVariable[] variables = pd.getProcessVariables();
			if(variables != null){
				for(int i=0; i<variables.length; i++){
					if(variables[i].isDatabaseSynchronized()){
						VariablePointingListFieldType listFieldType = new VariablePointingListFieldType();
						listFieldType.setVariable(variables[i]);
						fieldValues.add(listFieldType);
					}
				}
			}
			Object [] listFieldTypesInArray = new Object[fieldValues.size()];
			fieldValues.toArray(listFieldTypesInArray);
			fd.setInputter(new SelectInput(listFieldTypesInArray));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
	}

	TextContext displayName = org.uengine.contexts.TextContext.createInstance();
		public TextContext getDisplayName() {
			if(displayName.getText()==null){
				TextContext tc = TextContext.createInstance();
				tc.setText(getListFieldType().getFieldName());
				return tc;
			}
			return displayName;
		}
		public void setDisplayName(TextContext displayName) {
			this.displayName = displayName;
		}

    ListFieldType listFieldType;
		public ListFieldType getListFieldType() {
			return listFieldType;
		}
		public void setListFieldType(ListFieldType listFieldType) {
			this.listFieldType = listFieldType;
		}
		
	TextContext formatString = org.uengine.contexts.TextContext.createInstance();
		public TextContext getFormatString() {
			return formatString;
		}
		public void setFormatString(TextContext formatString) {
			this.formatString = formatString;
		}
		
	public Object getFieldValue(final ProcessInstanceDAO instanceDAO, Map genericContext) throws Exception{
		TextContext formatString = getFormatString();
		String fStr = formatString.getText();
		
		if(fStr!=null){
			ExpressionFormatter ef = new ExpressionFormatter(){
				public Object formatExpression(String expression) {
					try {
						return instanceDAO.get(expression);
					} catch (Exception e) {
						return e.getMessage();
					}
				}
			};
			
			return ef.format(fStr);
		}else
			return getListFieldType().getFieldValue(instanceDAO, genericContext);
	}

}
