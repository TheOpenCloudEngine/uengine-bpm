package org.uengine.kernel.face;

import org.metaworks.Face;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Range;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.uml.model.Attribute;
import org.uengine.uml.model.AttributeInstance;

import java.util.Calendar;

/**
 * Created by jinyoung jang on 2015. 8. 2..
 */
public class GenericValueFace extends AttributeInstance implements Face{

    @ServiceMethod(eventBinding = "change", bindingFor = "type", callByContent = true)
    public void changeType(){
        setMetaworksContext(new MetaworksContext());
        getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);

        //create default value for the type
        if(getValue()==null){
            AttributeInstance instance = createInstance();
            setValueObject(instance.getValue());
        }else{
            //some conversions between value
        }

        //just refresh
    }

    @Override
    public void setValueToFace(Object value) {
        if(value!=null) {
            setType(value.getClass().getName());
            setValueObject(value);
        }else{
            setType(String.class.getName());
            setValueObject("");
        }
    }

    @Override
    public Object createValueFromFace() {
        return getValue();
    }
}
