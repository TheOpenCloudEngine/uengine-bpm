package org.uengine.modeling.modeler.condition;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.component.TreeNode;
import org.uengine.contexts.TextContext;
import org.uengine.kernel.*;
import org.uengine.util.UEngineUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConditionEditor implements ContextAware {
	
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

	ConditionTree conditionTree;
		public ConditionTree getConditionTree() {
			return conditionTree;
		}
		public void setConditionTree(ConditionTree conditionTree) {
			this.conditionTree = conditionTree;
		}

	ConditionEditorExtension conditionEditorExtension;
		public ConditionEditorExtension getConditionEditorExtension() {
			return conditionEditorExtension;
		}
		public void setConditionEditorExtension(ConditionEditorExtension conditionEditorExtension) {
			this.conditionEditorExtension = conditionEditorExtension;
		}

	public ConditionEditor() throws Exception{
		this("");
	}
	public ConditionEditor(String conditionLabel) throws Exception{
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
		treeNode.setType(TreeNode.TYPE_FOLDER);
		treeNode.setConditionType(ConditionTreeNode.CONDITION_AND);
		treeNode.getMetaworksContext().setHow("root");
		
		if( condition != null ) {
			makeChildTreeNode(treeNode , condition);
		}
		conditionTree.setNode(treeNode);
			
		ConditionEditorExtension conditionEditorExtension = new ConditionEditorExtension();
		// TODO 아이디를 주니... 엉뚱한게 바뀐다
		ConditionTreeNodeView conditionTreeNodeView = conditionEditorExtension.getConditionTreeNode();
		conditionTreeNodeView.setParentId(treeNode.getId());
		conditionTreeNodeView.setParentNode(treeNode);
		conditionTreeNodeView.getConditionNode().setParentTreeNode(treeNode);
		conditionTreeNodeView.getConditionNode().getMetaworksContext().setHow("folder");
		setConditionEditorExtension(conditionEditorExtension);

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

						if( value != null && value instanceof String && UEngineUtil.isNumeric((String) value) || "Number".equalsIgnoreCase(type)){
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


	public Condition makeCondition(ConditionTreeNode rootNode) throws Exception{
		if( rootNode != null){
			ArrayList<ConditionTreeNode> child = rootNode.getChild();
			if( "rootNode".equals(rootNode.getId()) && child.size() == 0 ){
				return null;
			}
			Condition condition;
			if( rootNode.getConditionType() == null || "".equals(rootNode.getConditionType())){	// 최상위 root Node
				condition = new And();
				TextContext condiName = new TextContext();
				condiName.setText(ConditionTreeNode.CONDITION_AND);
				condition.setDescription(condiName);
			}else{
				String rootConditionType = rootNode.getConditionType();
				if( rootConditionType.equals(ConditionTreeNode.CONDITION_AND) ){
					condition = new And();
					TextContext condiName = new TextContext();
					condiName.setText(ConditionTreeNode.CONDITION_AND);
					condition.setDescription(condiName);
				}else{
					condition = new Or();
					TextContext condiName = new TextContext();
					condiName.setText(ConditionTreeNode.CONDITION_OR);
					condition.setDescription(condiName);
				}
			}
			for( int i=0; i < child.size(); i++){
				ConditionTreeNode childNode = child.get(i);
				String conditionType = childNode.getConditionType();
				TextContext condiName = new TextContext();
				condiName.setText(childNode.getName());
				if( childNode.isFolder() ){
					Condition childcond = makeCondition(childNode);
					((And)condition).addCondition(childcond);
				}else{
					ConditionNode conditionNode = childNode.getConditionNode();
					if( conditionType != null && conditionType.equals(ConditionTreeNode.CONDITION_OTHERWISE) ){
						condition = new Or();
						TextContext aa = new TextContext();
						condiName.setText(ConditionTreeNode.CONDITION_OR);
						condition.setDescription(aa);

						Otherwise otherwise = new Otherwise();
						otherwise.setDescription(condiName);
						((Or)condition).addCondition(otherwise);
					}else if( conditionNode != null && conditionNode.getConditionType().equals(ConditionTreeNode.CONDITION_AND) || conditionNode.getConditionType().equals(ConditionTreeNode.CONDITION_OR) ){

						String expressionChoice 	= conditionNode.getExpressionChoice().getSelected();	// Text, Number
						String sign = conditionNode.getSignChoice().getSelected();								// == , =>
						String valiable = conditionNode.getValiableChoice().findChildDepthString(conditionNode.getValiableChoice());					// processValiable
						ConditionInput expressionInput 	= conditionNode.getConditionInput();			// Text , Number , Date ..

						Object exppObject = new Object();
						if( expressionChoice.equalsIgnoreCase("Text") ){
							exppObject = expressionInput.getExpressionText();
						}else if( expressionChoice.equalsIgnoreCase("Number")){
							exppObject = expressionInput.getExpressionText();
						}else if( expressionChoice.equalsIgnoreCase("Yes or No")){
							exppObject = expressionInput.getYesNo();
						}else if( expressionChoice.equalsIgnoreCase("Date")){
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							exppObject = df.format(expressionInput.getExpressionDate());
						}else if( expressionChoice.equals("variable")){
							exppObject = expressionInput.getValiableChoice().findChildDepthString(expressionInput.getValiableChoice());
						}else{
							exppObject = "";
						}

						if( conditionNode.getConditionType().equals(ConditionTreeNode.CONDITION_OR)){
							Or orCondition = new Or();
							Evaluate eval = new Evaluate(valiable, sign, exppObject);
							eval.setType(expressionChoice);
							And andCondition = new And(new Condition[]{eval});
							// 자식이 또 있는 경우 재귀호출
							if( childNode.getChild() != null &&  childNode.getChild().size() > 0){
								Condition childcond = makeCondition(childNode);
								if( childcond != null ){
									andCondition.addCondition(childcond);
								}
							}
							orCondition.setDescription(condiName);
							orCondition.addCondition(andCondition);
							((Or)condition).addCondition(orCondition);
						}else if( conditionNode.getConditionType().equals(ConditionTreeNode.CONDITION_AND)){
							Evaluate eval = new Evaluate(valiable, sign, exppObject);
							eval.setType(expressionChoice);
							And andCondition = new And(new Condition[]{eval});
							// 자식이 또 있는 경우 재귀호출
							if( childNode.getChild() != null &&  childNode.getChild().size() > 0){
								Condition childcond = makeCondition(childNode);
								if( childcond != null ){
									andCondition.addCondition(childcond);
								}
							}
							andCondition.setDescription(condiName);
							((And)condition).addCondition(andCondition);
						}
					}
				}
			}
			return condition;
		}
		return null;
	}

}
