package org.uengine.uml.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.WebFieldDescriptor;
import org.metaworks.annotation.*;
import org.uengine.uml.model.face.AttributeTypeSelector;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

public class Attribute extends WebFieldDescriptor implements Serializable{

//    String name;
//        public String getName() {
//            return name;
//        }
//        public void setName(String name) {
//            this.name = name;
//        }

    //    String type;
    @Face(faceClass=AttributeTypeSelector.class)
    @Order(3)
    @Override
    public String getClassName() {
        return super.getClassName();
    }


//
//    public String getType() {
//            return type;
//        }
//        public void setType(String type) {
//            this.type = type;
//        }

//    public AttributeInstance createInstance() {
//        Class<?> typeClass = null;
//        try {
//            typeClass = Thread.currentThread().getContextClassLoader().loadClass(getClassName());
//        } catch (ClassNotFoundException e) {
//            new RuntimeException(e);
//        }
//
//        Object value = null;
//
//        //typeClass.newInstance();
//
//        if(typeClass == Calendar.class){
//            value = Calendar.getInstance();
//        }else if(typeClass == String.class){
//            value = "";
//        }else if(Number.class.isAssignableFrom(typeClass)){
//            value = 0l;
//        }
//
//
//        AttributeInstance attributeInstance = new AttributeInstance();
//        attributeInstance.setName(getName());
//        attributeInstance.setType(getType());
//        attributeInstance.setValueObject(value);
//
//        return attributeInstance;
//
//    }


    //// --- ordering ----

    @Override
    @Order(1)
    @Validator(name = ValidatorContext.VALIDATE_REGULAREXPRESSION, options = {"/^\\S*$/"}, message = "$SpaceAndSpecialIsNotAllowed")
    public String getName() {
        return super.getName();
    }

    @Override
    @Order(2)
    public String getDisplayName() {
        return super.getDisplayName();
    }


    //// --- hidden properties ----

    @Override
    @Hidden
    public String getViewFace() {
        return super.getViewFace();
    }

    @Override
    @Hidden
    public String getInputFace() {
        return super.getInputFace();
    }

    @Override
    @Hidden
    public Object[] getOptions() {
        return super.getOptions();
    }

    @Override
    @Hidden
    public Object[] getValues() {
        return super.getValues();
    }

    @Override
    @Hidden
    public Map<String, Object> getAttributes() {
        return super.getAttributes();
    }
}
