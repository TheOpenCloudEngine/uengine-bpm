package org.uengine.kernel;


import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFManager;
import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.util.UEngineUtil;


public class ExpressionEvaluateCondition extends Condition {

    String conditionExpression;
        public String getConditionExpression() {
            return conditionExpression;
        }
        public void setConditionExpression(String conditionExpression) {
            this.conditionExpression = conditionExpression;
        }

    public ExpressionEvaluateCondition(){
        super();
    }

    public ExpressionEvaluateCondition(String conditionExpression) {
        super();
        setConditionExpression(conditionExpression);
    }

    @Override
    public boolean isMet(ProcessInstance instance, String scope) throws Exception {

//        ExpressionEvaluator ee = new ExpressionEvaluator(
//                "try{" + getConditionExpression() + ";}catch(Exception e){throw new RuntimeException(e);}",                     // expression
//                boolean.class,                           // expressionType
//                new String[] { "instance" },           // parameterNames
//                new Class[] { ProcessInstance.class } // parameterTypes
//        );
//
//        Boolean res = (Boolean) ee.evaluate(
//                new Object[] {          // parameterValues
//                        instance
//                }
//        );

        if(!UEngineUtil.isNotEmpty(getConditionExpression())) return true;

        try {
            BSFManager manager = new BSFManager();
            manager.setClassLoader(this.getClass().getClassLoader());


            if (instance != null)
                manager.declareBean("instance", instance, ProcessInstance.class);

            manager.declareBean("activity", this, Activity.class);
            manager.declareBean("definition", instance.getProcessDefinition(), ProcessDefinition.class);
            manager.declareBean("util", new ScriptUtil(), ScriptUtil.class);

            BSFEngine engine = manager.loadScriptingEngine("javascript");

            Object result = engine.eval("my_class.my_generated_method", 0, 0, "function getVal(){\nimportPackage(java.lang); \n return " + getConditionExpression() + "}\n getVal();");

            if (result instanceof Boolean) {
                return (Boolean) result;
            } else {
                throw new RuntimeException("Not a boolean return value by the condition expression: " + getConditionExpression() + ". Returned is " + result);
            }
        }catch(Exception e){
            throw new RuntimeException("Exception during evaluate condition expression: " + getConditionExpression(), e);

        }


    }

    @Override
    public String toString() {
        if(UEngineUtil.isNotEmpty(getConditionExpression())) {
            return getConditionExpression();
        }else{
            return "";
        }
    }

    @ServiceMethod(target = ServiceMethodContext.TARGET_POPUP, callByContent = true, inContextMenu = true)
    public ModalWindow edit(@AutowiredFromClient ProcessVariablePanel processVariablePanel){
        ModalWindow modalWindow = new ModalWindow(this);
        modalWindow.setMetaworksContext(new MetaworksContext());
        modalWindow.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
        modalWindow.getMetaworksContext().setHow("full-fledged");

        return modalWindow;
    }

}
