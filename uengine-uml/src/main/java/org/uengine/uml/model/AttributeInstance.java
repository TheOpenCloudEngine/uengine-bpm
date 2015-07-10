package org.uengine.uml.model;

import org.metaworks.annotation.Available;

import java.io.Serializable;
import java.util.Calendar;

public class AttributeInstance extends Attribute{


    private Object value;

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
        if (valueString != null)
            this.value = valueString;
    }


    @Available(condition = "type=='java.lang.Long'")
    public long getValueLong() {
        if (value instanceof Long)
            return (Long) value;
        else
            return 0;
    }

    public void setValueLong(long valueLong) {
        this.value = valueLong;
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
            this.value = valueDate;
    }

}
