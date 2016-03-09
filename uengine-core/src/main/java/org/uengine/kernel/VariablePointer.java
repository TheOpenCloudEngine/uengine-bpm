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



    public Serializable getValue(ProcessInstance instance) throws Exception {

        ProcessVariableValue pvv = instance.getMultiple("", getKey());

        if(pvv == null)
            return null;

        if(pvv.size() <= getIndex()){
            return null;
        }

        pvv.setCursor(getIndex());

        return pvv.getValue();
    }

    public void setValue(ProcessInstance instance, final Serializable value) throws Exception {

        new InParentExecutionScope(){

            @Override
            public Object logic(ProcessInstance instance) throws Exception {
                ProcessVariableValue pvv = instance.getMultiple("", getKey());
                pvv.setCursor(getIndex());

                pvv.setValue(value);

                instance.set("", getKey(), pvv);

                return null;
            }
        }.run(instance);

    }

}
