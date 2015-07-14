package org.uengine.uml.model;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.uengine.uml.model.face.AttributeListFace;

import java.util.ArrayList;
import java.util.List;

import static org.metaworks.dwr.MetaworksRemoteService.wrapReturn;

public class ClassDefinition {

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
        ObjectInstance objectInstance = new ObjectInstance();

        for(Attribute attribute : getAttributeList()){
            objectInstance.getAttributeInstanceList().add(attribute.createInstance());

        }

        wrapReturn(new ModalWindow(objectInstance));

        return objectInstance;
    }


}
