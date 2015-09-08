package org.uengine.uml.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Range;
import org.uengine.uml.model.face.AttributeTypeSelector;

import java.io.Serializable;
import java.util.Calendar;

public class Attribute implements Serializable{

    String name;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

    String type;
    @Face(faceClass=AttributeTypeSelector.class)
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }

    public AttributeInstance createInstance() {
        Class<?> typeClass = null;
        try {
            typeClass = Thread.currentThread().getContextClassLoader().loadClass(getType());
        } catch (ClassNotFoundException e) {
            new RuntimeException(e);
        }

        Object value = null;

        //typeClass.newInstance();

        if(typeClass == Calendar.class){
            value = Calendar.getInstance();
        }else if(typeClass == String.class){
            value = "";
        }else if(Number.class.isAssignableFrom(typeClass)){
            value = 0l;
        }


        AttributeInstance attributeInstance = new AttributeInstance();
        attributeInstance.setName(getName());
        attributeInstance.setType(getType());
        attributeInstance.setValueObject(value);

        return attributeInstance;

    }



}
