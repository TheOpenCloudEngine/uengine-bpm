package org.uengine.modeling.modeler.condition;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.component.TreeNode;
import org.uengine.kernel.*;
import org.uengine.util.UEngineUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConditionPanel implements ContextAware {
	
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	
	String conditionLabel;
		public String getConditionLabel() {
			return conditionLabel;
		}
		public void setConditionLabel(String conditionLabel) {
			this.conditionLabel = conditionLabel;
		}
		
	String conditionId;
		public String getConditionId() {
			return conditionId;
		}
		public void setConditionId(String conditionId) {
			this.conditionId = conditionId;
		}
	/*public ArrayList<Role>	 roleList;
		public ArrayList<Role> getRoleList() {
			return roleList;
		}
		public void setRoleList(ArrayList<Role> roleList) {
			this.roleList = roleList;
		}
	public ArrayList<ProcessVariable> variableList;
		public ArrayList<ProcessVariable> getVariableList() {
			return variableList;
		}
		public void setVariableList(ArrayList<ProcessVariable> variableList) {
			this.variableList = variableList;
		}*/

	ConditionTree conditionTree;
		public ConditionTree getConditionTree() {
			return conditionTree;
		}
		public void setConditionTree(ConditionTree conditionTree) {
			this.conditionTree = conditionTree;
		}
	ConditionExPressionPanel conditionExPressionPanel;
		public ConditionExPressionPanel getConditionExPressionPanel() {
			return conditionExPressionPanel;
		}
		public void setConditionExPressionPanel(
				ConditionExPressionPanel conditionExPressionPanel) {
			this.conditionExPressionPanel = conditionExPressionPanel;
		}
	/*Transition transition;
		public Transition getTransition() {
			return transition;
		}
		public void setTransition(Transition transition) {
			this.transition = transition;
		}*/

	public ConditionPanel() throws Exception{
		this("");
	}
	public ConditionPanel(String conditionLabel) throws Exception{
		setConditionLabel(conditionLabel);
	}
	public void load(Condition condition)  throws Exception{
		conditionTree = new ConditionTree();
		conditionTree.setId("tree");
		
		ConditionTreeNode treeNode = new ConditionTreeNode();
		treeNode.getMetaworksContext().setHow("tree");
		treeNode.setLoaded(true);
		treeNode.setFolder(true);
		treeNode.setRoot(true);
		treeNode.setExpanded(true);
		treeNode.setSelected(true);
		treeNode.setId("rootNode");
		treeNode.setName("조건선택");  //TODO: locale
		//treeNode.setRoleList(roleList);
		//treeNode.setProcessVariableList(variableList);
		treeNode.setType(TreeNode.TYPE_FOLDER);
		treeNode.setConditionType(ConditionTreeNode.CONDITION_AND);
		treeNode.getMetaworksContext().setHow("root");
		
		if( condition != null ){
			makeChildTreeNode(treeNode , condition);
		}
		conditionTree.setNode(treeNode);
			
		ConditionExPressionPanel conditionExPressionPanel = new ConditionExPressionPanel();
		// TODO 아이디를 주니... 엉뚱한게 바뀐다
		ConditionTreeNodeView conditionTreeNode = conditionExPressionPanel.getConditionTreeNode();
		conditionTreeNode.setParentId(treeNode.getId());
		conditionTreeNode.setParentNode(treeNode);
		conditionTreeNode.getConditionNode().setParentTreeNode(treeNode);
		//conditionTreeNode.getConditionNode().setRoleList(getRoleList());
		conditionTreeNode.getConditionNode().getMetaworksContext().setHow("folder");
		setConditionExPressionPanel(conditionExPressionPanel);

    }

    public void makeChildTreeNode( ConditionTreeNode rootNode , Condition condition ) throws Exception{
		Condition[] condis = ((And)condition).getConditions();
		if( condis != null){
			for( int i=0; i< condis.length; i++){
				Condition condi = condis[i];
				if( condi instanceof Otherwise){
					ConditionTreeNode otherwiseTreeNode = new ConditionTreeNode();
					Long otherwiseIdByTime = Math.round(Math.random() * 100000);
					otherwiseTreeNode.setId(otherwiseIdByTime.toString());
					otherwiseTreeNode.setParentNode(rootNode);
					otherwiseTreeNode.setParentId( rootNode.getId() );
					otherwiseTreeNode.setConditionType(ConditionTreeNode.CONDITION_OTHERWISE);
					otherwiseTreeNode.setName(ConditionTreeNode.CONDITION_OTHERWISE);
					otherwiseTreeNode.setType("page_white_text");
					rootNode.add(otherwiseTreeNode);
					continue;
				}else{
					String nodeName = condi.getDescription() != null ? condi.getDescription().getText() : "";
					ConditionTreeNode treeNode = new ConditionTreeNode();
					Long idByTime = Math.round(Math.random() * 100000);
					treeNode.setId(idByTime.toString());
					treeNode.setParentNode(rootNode);
					treeNode.setParentId( rootNode.getId() );
					/*treeNode.setRoleList(roleList);
					treeNode.setVariableList(variableList);*/
					
					if( nodeName.equals(ConditionTreeNode.CONDITION_AND) || nodeName.equals(ConditionTreeNode.CONDITION_OR)){
						treeNode.setName(nodeName);
						treeNode.setConditionType(nodeName);
						treeNode.setFolder(true);
						treeNode.setExpanded(true);
						treeNode.setType(TreeNode.TYPE_FOLDER);
						treeNode.getMetaworksContext().setHow("folder");
						makeChildTreeNode(treeNode, condi);
					}else{
						treeNode.conditionInit();
						ConditionNode conditionNode = makeConditionNode(treeNode , condi);
						
						String nodeType = "";
						if( condi instanceof Or){
							nodeType = ConditionTreeNode.CONDITION_OR;
						}else if( condi instanceof And ){
							nodeType = ConditionTreeNode.CONDITION_AND;
						}else if( condi instanceof Otherwise ){
							nodeType = ConditionTreeNode.CONDITION_OTHERWISE;
						}
						conditionNode.setConditionType(nodeType);
						treeNode.setType("page_white_text");
						treeNode.setName(nodeName);
						conditionNode.setParentTreeNode(treeNode);
						treeNode.setConditionNode(conditionNode);
					}
					rootNode.add(treeNode);
				}
			}
		}
	}
	
	public ConditionNode makeConditionNode(ConditionTreeNode treeNode , Condition condition ) throws Exception{
		ConditionNode conditionNode = treeNode.getConditionNode();
		// and 와 or 의 공통 로직 처리
		if( condition instanceof Or || condition instanceof And){
			Condition childCondition[] = ((And)condition).getConditions();
			if( childCondition != null && childCondition.length > 0){
				for(int j = 0; j < childCondition.length; j++){
					Condition cd = childCondition[j];
					if( cd instanceof Evaluate){
						Evaluate eval = (Evaluate)cd;
						String type = eval.getType();
						
						if( eval.getKey() != null ){
							String valKey = eval.getKey();
							if( valKey.startsWith("[instance]")  ){
								conditionNode.getValiableChoice().setSelected(valKey);
							}else{
								String[] valKeys = valKey.replaceAll("[.]", "@").split("@");
								if( valKeys.length > 0){
									for(int k=0; k < valKeys.length; k++){
										if( k == 0 ){
											conditionNode.getValiableChoice().setSelected(valKeys[k]);
											conditionNode.getValiableChoice().loadRoot();
										}else{
											conditionNode.getValiableChoice().loadChild(k, valKeys[k]);
										}
									}
								}
							}
						}
						
						
						conditionNode.getSignChoice().setSelected(eval.getCondition());
						ConditionInput conditionInput = conditionNode.getConditionInput();
						Object value = eval.getValue();
						if( value != null && value instanceof String /*&& UEngineUtil.isNumeric((String) value)*/ || "Number".equalsIgnoreCase(type)){
							conditionNode.getExpressionChoice().setSelected("number");
							conditionInput.getMetaworksContext().setHow("number");
							conditionInput.setExpressionText(value.toString());
						}else if( type != null && "Date".equalsIgnoreCase(type)){
							conditionNode.getExpressionChoice().setSelected("date");
							conditionInput.getMetaworksContext().setHow("date");
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							Date date = df.parse((String)value);
							conditionInput.setExpressionDate(date);
						}else if( value instanceof String){
							String expString = (String)value;
							if( ("yes".equalsIgnoreCase(expString) || "no".equalsIgnoreCase(expString)) || "Yes or No".equalsIgnoreCase(type) ){
								conditionNode.getExpressionChoice().setSelected("Yes or No");
								conditionInput.getMetaworksContext().setHow("Yes or No");
								conditionInput.setYesNo(expString);
							}else if( conditionInput.getValiableChoice().getOptionValues().contains(expString) || "variable".equalsIgnoreCase(type) ){
								conditionNode.getExpressionChoice().setSelected("variable");
								conditionInput.getMetaworksContext().setHow("variable");
								if( expString.startsWith("[instance]")  ){
									conditionInput.getValiableChoice().setSelected(expString);
								}else{
									String[] valKeys = expString.replaceAll("[.]", "@").split("@");
									if( valKeys.length > 0){
										for(int k=0; k < valKeys.length; k++){
											if( k == 0 ){
												conditionInput.getValiableChoice().setSelected(valKeys[k]);
												conditionInput.getValiableChoice().loadRoot();
											}else{
												conditionInput.getValiableChoice().loadChild(k, valKeys[k]);
											}
										}
									}
								}
							}else if( "null".equalsIgnoreCase(type) ){
								conditionNode.getExpressionChoice().setSelected("null");
								conditionInput.getMetaworksContext().setHow("null");
							}else{
								conditionNode.getExpressionChoice().setSelected("text");
								conditionInput.getMetaworksContext().setHow("text");
								conditionInput.setExpressionText(expString);
							}
						}else{
							conditionNode.getExpressionChoice().setSelected("null");
							conditionInput.getMetaworksContext().setHow("null");
						}	
					}else if( cd instanceof Or){
						makeChildTreeNode(treeNode , cd);
					}else if( cd instanceof And){
						conditionNode = makeConditionNode(treeNode , cd);
					}
				}
			}
		}
		
		return conditionNode;
	}
	
}
