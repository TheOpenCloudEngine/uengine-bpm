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

        return (Serializable) new InParentExecutionScope(){

            @Override
            public Object logic(ProcessInstance instance) throws Exception {
                return instance.getAt("", getKey(), getIndex());
            }

        }.run(instance);

        //return instance.getAt("", getKey(), getIndex());
    }

    public void setValue(ProcessInstance instance, final Serializable value) throws Exception {

        new InParentExecutionScope(){

            @Override
            public Object logic(ProcessInstance instance) throws Exception {

                instance.setAt("", getKey(), getIndex(), value);

                return null;

            }
        }.run(instance);

    }

}
