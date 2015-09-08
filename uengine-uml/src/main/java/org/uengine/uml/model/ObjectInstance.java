package org.uengine.uml.model;

import org.uengine.kernel.BeanPropertyResolver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ObjectInstance implements Serializable, BeanPropertyResolver {

    public List<AttributeInstance> attributeInstanceList = new ArrayList<AttributeInstance>();
        public List<AttributeInstance> getAttributeInstanceList() {
            return attributeInstanceList;
        }

        public void setAttributeInstanceList(List<AttributeInstance> attributeInstanceList) {
            this.attributeInstanceList = attributeInstanceList;
        }


    @Override
    public void setBeanProperty(String key, Object value) {
        for(AttributeInstance attributeInstance : getAttributeInstanceList()){
            if(attributeInstance.getName().equals(key)){
                attributeInstance.setValue(value);
            }
        }
    }

    @Override
    public Object getBeanProperty(String key) {
        for(AttributeInstance attributeInstance : getAttributeInstanceList()){
            if(attributeInstance.getName().equals(key)){
                return attributeInstance.getValue();
            }
        }

        return null;
    }
}
