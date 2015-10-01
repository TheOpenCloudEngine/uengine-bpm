package org.uengine.kernel.face;

import org.metaworks.Face;
import org.metaworks.MetaworksContext;
import org.metaworks.WebFieldDescriptor;
import org.metaworks.annotation.Range;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.uml.model.Attribute;
import org.uengine.uml.model.ClassDefinition;
import org.uengine.uml.model.ObjectInstance;


import java.util.Calendar;

/**
 * Created by jinyoung jang on 2015. 8. 2..
 */
public class GenericValueFace implements Face{

    ObjectInstance value;
        public ObjectInstance getValue() {
            return value;
        }
        public void setValue(ObjectInstance value) {
            this.value = value;
        }


    @ServiceMethod(eventBinding = "change", bindingFor = "type", callByContent = true)
    public void changeType(){

        setValue(new ObjectInstance());

        getValue().setMetaworksContext(new MetaworksContext());
        getValue().getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);

        //create default value for the type
        if(getValue()==null){
            //TODO: implement this in other ways
           // AttributeInstance instance = createInstance();
           // setValueObject(instance.getValue());
        }else{
            //some conversions between value
        }

        //just refresh
    }

    @Override
    public void setValueToFace(Object value) {
        if(value==null) {
            value = "";
        }

        ClassDefinition classDefinition = new ClassDefinition();
        classDefinition.setFieldDescriptors(new Attribute[]{new Attribute()});
        classDefinition.getFieldDescriptors()[0].setName("value");
        classDefinition.getFieldDescriptors()[0].setClassName(value.getClass().getName());

        ObjectInstance instance = new ObjectInstance();
        instance.setClassDefinition(classDefinition);
        instance.setBeanProperty("value", value);

        setValue(instance);
    }

    @Override
    public Object createValueFromFace() {
        return getValue();
    }
}
