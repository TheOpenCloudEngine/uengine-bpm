package org.uengine.modeling.modeler.condition;

import org.metaworks.FieldDescriptor;
import org.metaworks.WebFieldDescriptor;
import org.metaworks.WebObjectType;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.component.SelectBox;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.contexts.ComplexType;
import org.uengine.kernel.ProcessVariable;

import java.util.ArrayList;

public class VariableSelectBox extends SelectBox {
	
	VariableSelectBox childSelectBox;
		public VariableSelectBox getChildSelectBox() {
			return childSelectBox;
		}
		public void setChildSelectBox(VariableSelectBox childSelectBox) {
			this.childSelectBox = childSelectBox;
		}
	String selectedDepthString;
		public String getSelectedDepthString() {
			return selectedDepthString;
		}
		public void setSelectedDepthString(String selectedDepthString) {
			this.selectedDepthString = selectedDepthString;
		}
	public ArrayList<ProcessVariable> variableList;
		public ArrayList<ProcessVariable> getVariableList() {
			return variableList;
		}
		public void setVariableList(ArrayList<ProcessVariable> variableList) {
			this.variableList = variableList;
		}
	public VariableSelectBox(){
		super();
	}
	
	@AutowiredFromClient
	public ConditionNode conditionNode;
	
	@ServiceMethod( callByContent=true )
	public Object[] makeValiableChoice() throws Exception{
		this.setSelectedDepthString(this.getSelected());
		VariableSelectBox childSelectBox = null;
		
		if( variableList != null){
			for(int i = 0; i < variableList.size(); i++){
				ProcessVariable processVariable = variableList.get(i);
				String nameAttr = processVariable.getName();
				if( nameAttr.equals(this.getSelected()) ){
					Object typeAttr = processVariable.getDefaultValue();
					if( typeAttr instanceof ComplexType){
						ComplexType complexType = (ComplexType)typeAttr;
						String typeIdAttr = complexType.getTypeId();
						childSelectBox = new VariableSelectBox();
						childSelectBox.setId(this.getId() +"."+ this.getSelected());
						childSelectBox.setMetaworksContext(this.getMetaworksContext());
						
						if( typeIdAttr != null && !"".equals(typeIdAttr) ){
							String formName = typeIdAttr.substring(1, typeIdAttr.length() -1); 
							WebObjectType wot = MetaworksRemoteService.getInstance().getMetaworksType( formName ); 
							WebFieldDescriptor wfields[] = wot.getFieldDescriptors();
							FieldDescriptor fields[] = wot.metaworks2Type().getFieldDescriptors();
							for(int j=0; j<fields.length; j++){
								WebFieldDescriptor wfd = wfields[j];
								String classType = wfd.getClassName().substring(wfd.getClassName().lastIndexOf(".")+1);
								String displayName = "".equals(wfd.getDisplayName()) ? wfd.getName() : wfd.getDisplayName();
								if( j == 0){
									childSelectBox.setSelectedDepthString(this.getSelectedDepthString()+"."+wfd.getName());
								}
								childSelectBox.add("["+classType+"]"+displayName, "["+wfd.getClassName()+"]"+wfd.getName());
							}
						}
					}
					break;
				}
			}
		}
		this.setMetaworksContext(conditionNode.getMetaworksContext());
		this.setChildSelectBox(childSelectBox);
		
		// TODO hardcode
		if( this.getId().equals(conditionNode.getValiableChoice().getId())){
			conditionNode.setValiableChoice(this);
		}else{
			conditionNode.getConditionInput().setValiableChoice(this);
		}
		
		return new Object[]{ conditionNode.saveCondition(), conditionNode };
	}
	
	@ServiceMethod( callByContent=true,eventBinding = "change", bindingFor = "childSelectBox")
	public Object[] makeValiableChoiceChild() throws Exception{
		String selectVal = childSelectBox.getSelected();
		childSelectBox.setChildSelectBox(null);
		int beginIndex = selectVal.indexOf("[");
		int endIndex = selectVal.indexOf("]");
		VariableSelectBox child = null;
		String selectClass = selectVal.substring(beginIndex+1, endIndex);
		String childId = selectVal.substring(endIndex+1);
		childSelectBox.setSelectedDepthString(this.getSelectedDepthString()+"."+childId);
		if( selectClass != null && selectClass.startsWith("org.uengine.codi.mw3")){
			WebObjectType wot2 = MetaworksRemoteService.getInstance().getMetaworksType(selectClass); 
			WebFieldDescriptor wfields2[] = wot2.getFieldDescriptors();
			FieldDescriptor fields2[] = wot2.metaworks2Type().getFieldDescriptors();
			child = new VariableSelectBox();
			child.setId(childSelectBox.getId()+"."+childId);
			for(int k=0; k<fields2.length; k++){
				WebFieldDescriptor wfd2 = wfields2[k];
				String classType = wfd2.getClassName().substring(wfd2.getClassName().lastIndexOf(".")+1);
				String displayName = "".equals(wfd2.getDisplayName()) ? wfd2.getName() : wfd2.getDisplayName();
				if( k == 0){
					child.setSelectedDepthString(childSelectBox.getSelectedDepthString()+"."+wfd2.getName());
				}
				child.add("["+classType+"]"+displayName, "["+wfd2.getClassName()+"]"+wfd2.getName());
			}
		}
		childSelectBox.setChildSelectBox(child);
		childSelectBox.setMetaworksContext(this.getMetaworksContext());
		
		// TODO hardcode 
		VariableSelectBox rootSelectBox = null;
		if( this.getId().startsWith((conditionNode.getValiableChoice().getId()))){
			rootSelectBox = conditionNode.getValiableChoice();
		}else{
			rootSelectBox = conditionNode.getConditionInput().getValiableChoice();
		}
		this.fillChildSelectBox(rootSelectBox);
		
		return new Object[]{conditionNode.saveCondition() , conditionNode};
	}
	
	public void loadRoot() throws Exception{
		this.setSelectedDepthString(this.getSelected());
		VariableSelectBox childSelectBox = null;
		
		if( variableList != null){
			for(int i = 0; i < variableList.size(); i++){
				ProcessVariable processVariable = variableList.get(i);
				String nameAttr = processVariable.getName();
				if( nameAttr.equals(this.getSelected()) ){
					Object typeAttr = processVariable.getDefaultValue();
					if( typeAttr instanceof ComplexType){
						ComplexType complexType = (ComplexType)typeAttr;
						String typeIdAttr = complexType.getTypeId();
						childSelectBox = new VariableSelectBox();
						childSelectBox.setId(this.getId() +"."+ this.getSelected());
						childSelectBox.setMetaworksContext(this.getMetaworksContext());
						
						if( typeIdAttr != null && !"".equals(typeIdAttr) ){
							String formName = typeIdAttr.substring(1, typeIdAttr.length() -1); 
							WebObjectType wot = MetaworksRemoteService.getInstance().getMetaworksType( formName ); 
							WebFieldDescriptor wfields[] = wot.getFieldDescriptors();
							FieldDescriptor fields[] = wot.metaworks2Type().getFieldDescriptors();
							for(int j=0; j<fields.length; j++){
								WebFieldDescriptor wfd = wfields[j];
								String classType = wfd.getClassName().substring(wfd.getClassName().lastIndexOf(".")+1);
								String displayName = "".equals(wfd.getDisplayName()) ? wfd.getName() : wfd.getDisplayName();
								if( j == 0){
									childSelectBox.setSelectedDepthString(this.getSelectedDepthString()+"."+wfd.getName());
								}
								childSelectBox.add("["+classType+"]"+displayName, "["+wfd.getClassName()+"]"+wfd.getName());
							}
						}
					}
					break;
				}
			}
		}
		this.setChildSelectBox(childSelectBox);
	}
	public void loadChild(int index, String selectedVal) throws Exception{
		// index 만큼 child를 찾아오는 로직
		VariableSelectBox childSelectBox = this;
		for( int i=0; i < index; i++){
			childSelectBox = childSelectBox.getChildSelectBox();
		}
		
		ArrayList<String> valueList = childSelectBox.getOptionValues();
		for(String str : valueList){
			int endIndex = str.indexOf("]");
			String childId = str.substring(endIndex+1);
			if( childId.equals(selectedVal)){
				childSelectBox.setSelected(str);
			}
		}
		
		String selectVal = childSelectBox.getSelected();
		childSelectBox.setChildSelectBox(null);
		int beginIndex = selectVal.indexOf("[");
		int endIndex = selectVal.indexOf("]");
		VariableSelectBox child = null;
		String selectClass = selectVal.substring(beginIndex+1, endIndex);
		String childId = selectVal.substring(endIndex+1);
		childSelectBox.setSelectedDepthString(this.getSelectedDepthString()+"."+childId);
		if( selectClass != null && selectClass.startsWith("org.uengine.codi.mw3")){
			WebObjectType wot2 = MetaworksRemoteService.getInstance().getMetaworksType(selectClass); 
			WebFieldDescriptor wfields2[] = wot2.getFieldDescriptors();
			FieldDescriptor fields2[] = wot2.metaworks2Type().getFieldDescriptors();
			child = new VariableSelectBox();
			child.setId(childSelectBox.getId()+"."+childId);
			for(int k=0; k<fields2.length; k++){
				WebFieldDescriptor wfd2 = wfields2[k];
				String classType = wfd2.getClassName().substring(wfd2.getClassName().lastIndexOf(".")+1);
				String displayName = "".equals(wfd2.getDisplayName()) ? wfd2.getName() : wfd2.getDisplayName();
				if( k == 0){
					child.setSelectedDepthString(childSelectBox.getSelectedDepthString()+"."+wfd2.getName());
				}
				child.add("["+classType+"]"+displayName, "["+wfd2.getClassName()+"]"+wfd2.getName());
			}
		}
		childSelectBox.setChildSelectBox(child);
		childSelectBox.setMetaworksContext(this.getMetaworksContext());
	}
	
	public void fillChildSelectBox(VariableSelectBox selectBox){
		if( selectBox != null ){
			if( this.getId().equals(selectBox.getId())){
				selectBox.setChildSelectBox(childSelectBox);
			}else{
				if( selectBox.getChildSelectBox() != null ){
					fillChildSelectBox(selectBox.getChildSelectBox());
				}
			}
		}
	}
	
	public String findChildDepthString(VariableSelectBox selectBox) throws Exception{
		if( selectBox != null ){
			if( selectBox.getChildSelectBox() != null ){
				return findChildDepthString(selectBox.getChildSelectBox());
			}else{
				return selectBox.getSelectedDepthString();
			}
		}
		
		return null;
	}
}
