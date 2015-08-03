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
        public void setValue(Object value) {
            this.value = value;
        }


    @Available(condition = "type=='java.lang.String'")
    public String getValueString() {
        if (value instanceof String)
            return (String) value;
        else
            return null;
    }

    public void setValueString(String valueString) {
        if (valueString instanceof String)
            setValue(valueString);
    }


    @Available(condition = "type=='java.lang.Long'")
    public Long getValueLong() {
        if (value instanceof Long)
            return (Long) value;
        else
            return null;
    }

    public void setValueLong(Long valueLong) {
        setValue(valueLong);
    }

    @Available(condition = "type=='java.util.Calendar'")
    public Calendar getValueDate() {
        if (value instanceof Calendar){
            return (Calendar) value;
        }else
            return null;
    }

    public void setValueDate(Calendar valueDate) {
        if(valueDate != null)
            setValue(valueDate);
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
