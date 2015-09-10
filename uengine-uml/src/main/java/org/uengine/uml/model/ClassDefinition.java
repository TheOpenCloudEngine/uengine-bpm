package org.uengine.uml.model;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.uengine.uml.model.face.AttributeListFace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.metaworks.dwr.MetaworksRemoteService.metaworksCall;
import static org.metaworks.dwr.MetaworksRemoteService.wrapReturn;

public class ClassDefinition implements Serializable{

    List<Attribute> attributeList = new ArrayList<Attribute>();
    @Face(faceClass = AttributeListFace.class)
        public List<Attribute> getAttributeList() {
            return attributeList;
        }
        public void setAttributeList(List<Attribute> attributeList) {
            this.attributeList = attributeList;
        }

    @ServiceMethod(callByContent = true, target= ServiceMethodContext.TARGET_POPUP)
    public ObjectInstance createObjectInstance() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        ObjectInstance objectInstance = newObjectInstance();

        for(Attribute attribute : getAttributeList()){
            objectInstance.getAttributeInstanceList().add(attribute.createInstance());

        }

        if(metaworksCall()) wrapReturn(new ModalWindow(objectInstance));

        return objectInstance;
    }

    protected ObjectInstance newObjectInstance() {
        return new ObjectInstance();
    }


}
