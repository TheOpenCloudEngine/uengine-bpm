package org.uengine.modeling.modeler.condition;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Range;
import org.metaworks.annotation.ServiceMethod;

import java.util.Date;

public class ConditionInput implements ContextAware {
	
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
		
	String expressionText;
		public String getExpressionText() {
			return expressionText;
		}
		public void setExpressionText(String expressionText) {
			this.expressionText = expressionText;
		}
		
	Date expressionDate;
		public Date getExpressionDate() {
			return expressionDate;
		}
		public void setExpressionDate(Date expressionDate) {
			this.expressionDate = expressionDate;
		}	
		
	String yesNo;
	    @Range(options={"Yes","No"},values={"Yes","No"})
		public String getYesNo() {
			return yesNo;
		}
		public void setYesNo(String yesNo) {
			this.yesNo = yesNo;
		}
		
	String changeType;
		public String getChangeType() {
			return changeType;
		}
		public void setChangeType(String changeType) {
			this.changeType = changeType;
		}
		
	VariableSelectBox valiableChoice;
		public VariableSelectBox getValiableChoice() {
			return valiableChoice;
		}
		public void setValiableChoice(VariableSelectBox valiableChoice) {
			this.valiableChoice = valiableChoice;
		}

	public ConditionInput(){
		this.setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhen("edit");
	}
	public void init(){
		expressionDate = new Date();
		valiableChoice = new VariableSelectBox();
	}
	public void load() throws Exception{
		
	}
		
	@ServiceMethod( payload={"changeType" , "valiableChoice"},eventBinding="change", bindingFor={"changeType"} )
	public void changeInput() throws Exception{
		this.getMetaworksContext().setHow(changeType);
	}
	
	@ServiceMethod( callByContent=true ,eventBinding="change", bindingFor={"yesNo","expressionDate","expressionText"})
	public Object[] changeValue() throws Exception{
		conditionNode.getConditionInput().setChangeType(this.getChangeType());
		conditionNode.getConditionInput().setExpressionDate(getExpressionDate());
		conditionNode.getConditionInput().setExpressionText(getExpressionText());
		conditionNode.getConditionInput().setYesNo(getYesNo());
		return new Object[]{ conditionNode.saveCondition(), conditionNode };
	}
	
	@AutowiredFromClient
	public ConditionNode conditionNode;
}
