package org.uengine.kernel.face;

import org.metaworks.Face;
import org.metaworks.FieldFace;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.kernel.ProcessVariable;
import org.uengine.util.UEngineUtil;

/**
 * Created by jangjinyoung on 15. 7. 18..
 */
public class ProcessVariableDefaultValueFace implements Face, FieldFace<ProcessVariable> {

    transient ProcessVariable processVariable;

    @ServiceMethod
    public ProcessVariableDefaultValueFace setValue(){
        String classNameForDefaultValueInput = getClass().getPackage().getName() + "." + processVariable.getType() + UEngineUtil.getClassNameOnly(getClass().getName());
        try {
            Class classForDefaultValueInput = Thread.currentThread().getContextClassLoader().loadClass(classNameForDefaultValueInput);

            ProcessVariableDefaultValueFace face = (ProcessVariableDefaultValueFace) classForDefaultValueInput.newInstance();
            //face.setValueToFace();

            return face;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public void setValueToFace(Object defaultValue) {

    }

    @Override
    public Object createValueFromFace() {
        return null;
    }

    @Override
    public void visitHolderObjectOfField(ProcessVariable holderObject) {

        processVariable = holderObject;

    }
}
