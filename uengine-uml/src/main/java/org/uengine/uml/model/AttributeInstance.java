package org.uengine.uml.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Hidden;

import java.io.Serializable;
import java.util.Calendar;

public class AttributeInstance extends Attribute implements ContextAware{


    private Object value;
    @Hidden
        public Object getValue() {
            return value;
        }
//        private void setValue(Object value) {  //must be private to prevent DWR to set this value.
//            this.value = value;
//        }
        public void setValueObject(Object value){ //use this instead.
            this.value = (value);
        }


    @Available(condition = "type=='java.lang.String' || type._realValue=='java.lang.String'")
    public String getValueString() {
        if (value instanceof String)
            return (String) value;
        else
            return null;
    }

    public void setValueString(String valueString) {
        if (valueString instanceof String)
            setValueObject(valueString);
    }


    @Available(condition = "type=='java.lang.Long' || type._realValue=='java.lang.Long'")
    public Long getValueLong() {
        if (value instanceof Long)
            return (Long) value;
        else
            return null;
    }

    public void setValueLong(Long valueLong) {
        if (value instanceof Long)
            setValueObject(valueLong);
    }

    @Available(condition = "type=='java.util.Calendar' || type._realValue=='java.util.Calendar'")
    public Calendar getValueDate() {
        if (value instanceof Calendar){
            return (Calendar) value;
        }else
            return null;
    }

    public void setValueDate(Calendar valueDate) {
        if(valueDate != null)
            setValueObject(valueDate);
    }

    @Available(condition = "type=='org.uengine.uml.model.ObjectInstance' || type._realValue=='org.uengine.uml.model.ObjectInstance'")
    public ObjectInstance getValueObjectInstance() {
        if (value instanceof ObjectInstance){
            return (ObjectInstance) value;
        }else
            return null;
    }

    public void setValueObjectInstance(ObjectInstance valueObjectInstance) {
        if(valueObjectInstance != null)
            setValueObject(valueObjectInstance);
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


}
