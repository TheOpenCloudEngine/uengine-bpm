package org.uengine.kernel.bpmn.view;

import org.metaworks.EventContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.contexts.TextContext;
import org.uengine.kernel.*;
import org.uengine.modeling.IRelation;
import org.uengine.modeling.RelationPropertiesView;
import org.uengine.modeling.RelationView;
import org.uengine.modeling.modeler.condition.ConditionInput;
import org.uengine.modeling.modeler.condition.ConditionNode;
import org.uengine.modeling.modeler.condition.ConditionPanel;
import org.uengine.modeling.modeler.condition.ConditionTreeNode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SequenceFlowView extends RelationView {
	public final static String SHAPE_ID = "OG.shape.bpmn.C_Sequence";

    ConditionPanel conditionPanel;
        public ConditionPanel getConditionPanel() {
            return conditionPanel;
        }

        public void setConditionPanel(ConditionPanel conditionPanel) {
            this.conditionPanel = conditionPanel;
        }

    public SequenceFlowView(){
		super();
	}
	
	public SequenceFlowView(IRelation relation){
		super(relation);
	}
//
//	@ServiceMethod(callByContent=true, eventBinding=EventContext.EVENT_DBLCLICK)
//	public Object properties() throws Exception {
//		SequenceFlow sequenceFlow;
//		if(this.getRelation() == null)
//			sequenceFlow = new SequenceFlow();
//		else
//			sequenceFlow = (SequenceFlow)this.getRelation();
//
//		//transition.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
//
//		return new RelationPropertiesView(sequenceFlow);
//	}
//

	@ServiceMethod(callByContent = true, eventBinding = EventContext.EVENT_DBLCLICK, target = ServiceMethodContext.TARGET_POPUP)
	public Object showProperty() throws Exception {
        RelationPropertiesView relationPropertiesView = new RelationPropertiesView(this);
        relationPropertiesView.setWidth(800);
        relationPropertiesView.setHeight(1000);

        return relationPropertiesView;

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

