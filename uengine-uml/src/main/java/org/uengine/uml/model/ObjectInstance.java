package org.uengine.uml.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectInstance{

    public List<AttributeInstance> attributeInstanceList = new ArrayList<AttributeInstance>();
        public List<AttributeInstance> getAttributeInstanceList() {
            return attributeInstanceList;
        }

        public void setAttributeInstanceList(List<AttributeInstance> attributeInstanceList) {
            this.attributeInstanceList = attributeInstanceList;
        }


}
