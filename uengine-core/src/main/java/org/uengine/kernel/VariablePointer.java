package org.uengine.kernel;

import java.io.Serializable;

/**
 * Created by jjy on 2015. 12. 21..
 */
public class VariablePointer implements Serializable{

    String key;
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }


    int index;
        public int getIndex() {
            return index;
        }
        public void setIndex(int index) {
            this.index = index;
        }


    String executionScope;
        public String getExecutionScope() {
            return executionScope;
        }
        public void setExecutionScope(String executionScope) {
            this.executionScope = executionScope;
        }



    public Serializable getValue(ProcessInstance instance) throws Exception {

  //      if(getExecutionScope()!=null){

            return (Serializable) new InExecutionScope(getExecutionScope()){

                @Override
                public Object logic(ProcessInstance instance) throws Exception {
                    Object value = instance.getAt("", getKey(), getIndex());

                    if(value instanceof VariablePointer){ //if value is variablepointer too, find the real value recursively.
                        return ((VariablePointer) value).getValue(instance);
                    }else{
                        return value;
                    }
                }

            }.run(instance);

//        }else{
//            return (Serializable) new InParentExecutionScope(){
//
//                @Override
//                public Object logic(ProcessInstance instance) throws Exception {
//                    return instance.getAt("", getKey(), getIndex());
//                }
//
//            }.run(instance);
//        }

        //return instance.getAt("", getKey(), getIndex());
    }

    public void setValue(ProcessInstance instance, final Serializable value) throws Exception {
   //     if(getExecutionScope()!=null) {
            new InExecutionScope(getExecutionScope()){

                @Override
                public Object logic(ProcessInstance instance) throws Exception {

                    instance.setAt("", getKey(), getIndex(), value);

                    return null;

                }
            }.run(instance);
//        }else{
//
//            new InParentExecutionScope(){
//
//                @Override
//                public Object logic(ProcessInstance instance) throws Exception {
//
//                    instance.setAt("", getKey(), getIndex(), value);
//
//                    return null;
//
//                }
//            }.run(instance);
//        }

    }

}
