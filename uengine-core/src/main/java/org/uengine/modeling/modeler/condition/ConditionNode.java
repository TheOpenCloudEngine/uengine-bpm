package org.uengine.modeling.modeler.condition;

import org.metaworks.*;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.component.SelectBox;
import org.metaworks.component.TreeNode;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.Role;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConditionNode implements Cloneable, ContextAware{
	
	MetaworksContext metaworksContext;
        public MetaworksContext getMetaworksContext() {
            return metaworksContext;
        }
        public void setMetaworksContext(MetaworksContext metaworksContext) {
		this.metaworksContext = metaworksContext;
	}

	ConditionVariableSelectBox valiableChoice;
		public ConditionVariableSelectBox getValiableChoice() {
			return valiableChoice;
		}
		public void setValiableChoice(ConditionVariableSelectBox valiableChoice) {
			this.valiableChoice = valiableChoice;
		}

    SelectBox signChoice;
		public SelectBox getSignChoice() {
			return signChoice;
		}
		public void setSignChoice(SelectBox signChoice) {
			this.signChoice = signChoice;
		}

    SelectBox expressionChoice;
		public SelectBox getExpressionChoice() {
			return expressionChoice;
		}
		public void setExpressionChoice(SelectBox expressionChoice) {
			this.expressionChoice = expressionChoice;
		}

    ConditionInput conditionInput;
		public ConditionInput getConditionInput() {
			return conditionInput;
		}
		public void setConditionInput(ConditionInput conditionInput) {
			this.conditionInput = conditionInput;
		}

	String conditionType;
		public String getConditionType() {
			return conditionType;
		}
		public void setConditionType(String conditionType) {
			this.conditionType = conditionType;
		}

	ConditionTreeNode parentTreeNode;
		public ConditionTreeNode getParentTreeNode() {
			return parentTreeNode;
		}
		public void setParentTreeNode(ConditionTreeNode parentTreeNode) {
			this.parentTreeNode = parentTreeNode;
		}

	protected List<Role> roleList;
        public List<Role> getRoleList() {
            return roleList;
        }
        public void setRoleList(List<Role> roleList) {
            this.roleList = roleList;
        }

    protected List<ProcessVariable> processVariableList;
        public List<ProcessVariable> getProcessVariableList() {
            return processVariableList;
        }
        public void setProcessVariableList(List<ProcessVariable> processVariableList) {
            this.processVariableList = processVariableList;
        }

    public ConditionNode(){
		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhen("edit");
	}
	public void makeValiableChoice() throws Exception{
		ConditionVariableSelectBox choice = new ConditionVariableSelectBox();
		choice.setId("makeKey");
		choice.add("선택해 주세요", "null");
		if( this.getRoleList() != null){
			for(int i = 0; i < roleList.size(); i++){
				Role role = roleList.get(i);
				String displayName = role.getDisplayName() == null ? role.getName() : role.getDisplayName().getText();
				choice.add("[ROLE]"+displayName, role.getName());
			}

		}

		if( this.getProcessVariableList() != null){
			choice.setProcessVariableList(this.getProcessVariableList());
			for(int i = 0; i < this.getProcessVariableList().size(); i++){
				ProcessVariable processVariable = this.getProcessVariableList().get(i);
				String nameAttr = processVariable.getName();
				String displayName = processVariable.getDisplayName() == null ? nameAttr : processVariable.getDisplayName().getText();
				choice.add("[VARIABLE]"+displayName, nameAttr);
			}
		}

		setValiableChoice(choice);
	}

	@ServiceMethod(callByContent=true, eventBinding="change", bindingFor={"valiableChoice"})
	public Object[] makeValiableChoiceChild() throws Exception{
		ConditionVariableSelectBox parentSelectBox = this.getValiableChoice();
		parentSelectBox.conditionNode = this;

		return parentSelectBox.makeValiableChoice();
	}

	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND, eventBinding="change", bindingFor={"signChoice"})
	public Object[] toEventReceive() throws Exception{
		return new Object[]{new ToEvent(ServiceMethodContext.TARGET_SELF, "saveCondition") };
	}

	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND, eventBinding="change", bindingFor={"expressionChoice"})
	public Object[] toEventReceiveExpression() throws Exception{
		this.getConditionInput().setChangeType(this.getExpressionChoice().getSelected());
		this.getConditionInput().changeInput();

		return new Object[]{new Refresh(this.getConditionInput()), new ToEvent(ServiceMethodContext.TARGET_SELF, "saveCondition") };
	}

	public void makeSignChoice() throws Exception{
		SelectBox choice = new SelectBox();
		choice.setId("sign");
		choice.add("==", "==");
		choice.add("!=", "!=");
		choice.add(">=", ">=");
		choice.add(">", ">");
		choice.add("<", "<");
		choice.add("<=", "<=");
		choice.add("contains", "contains");
		choice.add("not contains", "not contains");
		setSignChoice(choice);
	}

	public void makeExpressionChoice() throws Exception{
		SelectBox choice = new SelectBox();
		choice.setId("expression");
		choice.add("NULL", "null");
		choice.add("Text", "text");
		choice.add("Number", "number");
		choice.add("Date", "date");
		choice.add("Yes or No", "Yes or No");
		choice.add("Process Variable" ,"variable");
		setExpressionChoice(choice);
	}

	@ServiceMethod( callByContent=true ,target=ServiceMethodContext.TARGET_AUTO, eventBinding="saveCondition")
	public Object saveCondition() throws Exception{
		String nodeName = "";
		if( conditionType != null && ( conditionType.equals(ConditionTreeNode.CONDITION_AND) || conditionType.equals(ConditionTreeNode.CONDITION_OR) )){
			String val1 = this.getValiableChoice().findChildDepthString(this.getValiableChoice());
			if( val1 == null || "".equals(val1) || "null".equals(val1)){
				val1 = this.getValiableChoice().getSelectedText()	;	// default
			}
			String val2 = this.getSignChoice().getSelected();
			String val3 = this.getExpressionChoice().getSelected();

			ConditionInput expVal = this.getConditionInput();
			if( val3 != null && val3.equalsIgnoreCase("text") ){
				val3 = expVal.getExpressionText();
				val3 = "\'" + val3 + "\'";
			}else if( val3 != null && val3.equalsIgnoreCase("number") ){
				val3 = expVal.getExpressionText();
				if( "".equals(val3) ){
					val3 = "0";			// default
				}
			}else if( val3 != null && val3.equalsIgnoreCase("Yes or No") ){
				val3 = expVal.getYesNo();
			}else if( val3 != null && val3.equalsIgnoreCase("date") ){
				if( expVal.getExpressionDate() != null ){
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					val3 = df.format(expVal.getExpressionDate());
				}else{
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					val3 = df.format(new Date());	// default
				}
			}else if( val3 != null && val3.equalsIgnoreCase("variable") ){
				if( expVal.getValiableChoice().getSelected() == null || "null".equals(expVal.getValiableChoice().getSelected())){
					val3 = "선택해 주세요";	// default
				}else{
					val3 = expVal.getValiableChoice().findChildDepthString(expVal.getValiableChoice());
				}
			}
			nodeName =  val1 + " " +val2 + " " + val3;

		}else if( conditionType != null && conditionType.equals(ConditionTreeNode.CONDITION_OTHERWISE) ){
			nodeName = "otherwise";

		}else{
			nodeName = "오류";
		}

		// 왼쪽 트리 변경
		ConditionTreeNode node = getParentTreeNode();
		node.setName(nodeName);
		node.setConditionNode(this);
		node.setSelected(true);

		return node;
	}

	public void init() throws Exception{
		makeValiableChoice();
		makeSignChoice();
		makeExpressionChoice();

		conditionInput = new ConditionInput();
		conditionInput.init();

		// 두개를 동시에 쓰는 경우가 생기기 때문에 객체를 분리하여 주었다
		ConditionVariableSelectBox selectBox = new ConditionVariableSelectBox();
		selectBox.setId("makeSecondKey");
		selectBox.setOptionNames(valiableChoice.getOptionNames());
		selectBox.setOptionValues(valiableChoice.getOptionValues());
		selectBox.setProcessVariableList(this.getProcessVariableList());
		conditionInput.setValiableChoice(selectBox);
	}

	public ConditionTreeNode makeConditionTreeNode() throws Exception{
		ConditionTreeNode parentNode = this.getParentTreeNode();

		if( parentNode == null ){
			parentNode = new ConditionTreeNode();
			parentNode.setId("rootNode");
			parentNode.setName("Condition");
			parentNode.setRoleList(this.getRoleList());
			parentNode.setProcessVariableList(this.getProcessVariableList());
			parentNode.setType(TreeNode.TYPE_FOLDER);
			parentNode.setConditionType(ConditionTreeNode.CONDITION_OR);

			this.setParentTreeNode(parentNode);
		}

		ConditionTreeNode node = new ConditionTreeNode();
		Long idByTime = new Date().getTime();
		node.setId(idByTime.toString());
		node.setParentNode(this.getParentTreeNode());
		node.setParentId(this.getParentTreeNode().getId());
        node.setRoleList(this.getRoleList());
        node.setProcessVariableList(this.getProcessVariableList());
		node.setLoaded(true);

		return node;
	}

	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object newAND() throws Exception{
		ConditionTreeNode node = this.makeConditionTreeNode();
		node.setName(ConditionTreeNode.CONDITION_AND);
		node.setConditionType(ConditionTreeNode.CONDITION_AND);
		node.setFolder(true);
		node.setExpanded(true);
		node.setType(TreeNode.TYPE_FOLDER);
		node.getMetaworksContext().setHow("folder");

		return new ToAppend(this.getParentTreeNode() , node);
	}

	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object newOR() throws Exception{

		ConditionTreeNode node = this.makeConditionTreeNode();
		node.setName(ConditionTreeNode.CONDITION_OR);
		node.setConditionType(ConditionTreeNode.CONDITION_OR);
		node.setFolder(true);
		node.setExpanded(true);
		node.setType(TreeNode.TYPE_FOLDER);
		node.getMetaworksContext().setHow("folder");

		return new ToAppend(this.getParentTreeNode() , node);

	}

	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object newOtherwise() throws Exception{

		ConditionTreeNode node = this.makeConditionTreeNode();
		node.setName(ConditionTreeNode.CONDITION_OTHERWISE);
		node.setConditionType(ConditionTreeNode.CONDITION_OTHERWISE);
		node.setType("page_white_text");

		return new ToAppend(this.getParentTreeNode() , node);
	}

	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
    public Object newExpression() throws Exception{
		if( this.getConditionType() == null ){
			this.setConditionType(ConditionTreeNode.CONDITION_AND);
		}

		ConditionTreeNode node = this.makeConditionTreeNode();
		node.setName(ConditionTreeNode.CONDITION_DEFAULT_EXPRESSION);
		node.setConditionType(ConditionTreeNode.CONDITION_EXPRESSION);
		node.conditionInit();
		node.getConditionNode().setConditionType(this.getConditionType());
		node.getConditionNode().setParentTreeNode(node);

		return new ToAppend(this.getParentTreeNode() , node);
	}
}
