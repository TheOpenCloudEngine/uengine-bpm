package org.uengine.uml.model;

import org.metaworks.ContextAware;
import org.metaworks.AllChildFacesAreIgnored;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Payload;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.kernel.BeanPropertyResolver;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.ResourceManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

//@Face(ejsPath="genericfaces/CleanObjectFace.ejs")
public class ObjectInstance implements Serializable, ContextAware, BeanPropertyResolver {
//
//    public List<AttributeInstance> attributeInstanceList = new ArrayList<AttributeInstance>();
//        public List<AttributeInstance> getAttributeInstanceList() {
//            return attributeInstanceList;
//        }
//
//        public void setAttributeInstanceList(List<AttributeInstance> attributeInstanceList) {
//            this.attributeInstanceList = attributeInstanceList;
//        }

    ClassDefinition classDefinition;
    @Face(faceClass = AllChildFacesAreIgnored.class)
        public ClassDefinition getClassDefinition() {
            return classDefinition;
        }
        public void setClassDefinition(ClassDefinition classDefinition) {
            this.classDefinition = classDefinition;
        }


    String className;
        public String getClassName() {
            return className;
        }
        public void setClassName(String className) {
            this.className = className;
        }


    Map valueMap = new HashMap();
        public Map getValueMap() {
            return valueMap;
        }
        public void setValueMap(Map valueMap) {
            this.valueMap = valueMap;
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


        if(valueMap==null) valueMap = new HashMap();


        getValueMap().put(key, value);
//        boolean set = false;
//
//
//        for(AttributeInstance attributeInstance : getAttributeInstanceList()){
//            if(key.equals(attributeInstance.getName())){
//                attributeInstance.setValueObject(value);
//
//                set = true;
//
//            }
//        }
//
//        if(!set) {
//            AttributeInstance newAttrInst = new AttributeInstance();
//            newAttrInst.setName(key);
//            newAttrInst.setValueObject(value);
//
//            getAttributeInstanceList().add(newAttrInst);
//        }
    }

    @Override
    public Object getBeanProperty(String key) {
//        for(AttributeInstance attributeInstance : getAttributeInstanceList()){
//            if(key.equals(attributeInstance.getName())){
//                return attributeInstance.getValue();
//            }
//        }
//
//        return null;

        return getValueMap().get(key);

    }


    @ServiceMethod(callByContent = true, target = ServiceMethod.TARGET_NONE)
    public ObjectInstance fillClassDefinition(@Payload("className") String className) throws Exception {

        ResourceManager resourceManager = MetaworksRemoteService.getComponent(ResourceManager.class);

        DefaultResource classDefinitionResource = new DefaultResource( className);
        ClassDefinition definition = (ClassDefinition) resourceManager.getStorage().getObject(classDefinitionResource);

        setClassDefinition(definition);

        return this;
    }
}
