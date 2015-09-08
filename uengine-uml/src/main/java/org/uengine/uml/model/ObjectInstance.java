package org.uengine.uml.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.uengine.kernel.BeanPropertyResolver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Face(ejsPath="genericfaces/CleanObjectFace.ejs")
public class ObjectInstance implements Serializable, ContextAware, BeanPropertyResolver {

    public List<AttributeInstance> attributeInstanceList = new ArrayList<AttributeInstance>();
        public List<AttributeInstance> getAttributeInstanceList() {
            return attributeInstanceList;
        }

        public void setAttributeInstanceList(List<AttributeInstance> attributeInstanceList) {
            this.attributeInstanceList = attributeInstanceList;
        }

    MetaworksContext metaworksContext;
        @Override
        public MetaworksContext getMetaworksContext() {
            return metaworksContext;
        }
        @Override
        public void setMetaworksContext(MetaworksContext metaworksContext) {
            this.metaworksContext = metaworksContext;
        }


    @Override
    public void setBeanProperty(String key, Object value) {

        boolean set = false;


        for(AttributeInstance attributeInstance : getAttributeInstanceList()){
            if(key.equals(attributeInstance.getName())){
                attributeInstance.setValueObject(value);

                set = true;

            }
        }

        if(!set) {
            AttributeInstance newAttrInst = new AttributeInstance();
            newAttrInst.setName(key);
            newAttrInst.setValueObject(value);

            getAttributeInstanceList().add(newAttrInst);
        }
    }

    @Override
    public Object getBeanProperty(String key) {
        for(AttributeInstance attributeInstance : getAttributeInstanceList()){
            if(key.equals(attributeInstance.getName())){
                return attributeInstance.getValue();
            }
        }

        return null;
    }
}
