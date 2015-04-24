package org.uengine.kernel;

import java.io.Serializable;
import java.util.HashMap;

public class IndexedProcessVariableMap extends HashMap {

    int maxIndex = 0;
    public int getMaxIndex() {
        return maxIndex;
    }
    public void setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
    }

    public void putProcessVariable(int index, Object arg1) {
        if(getMaxIndex() < index){
            setMaxIndex(index);
        }

        put(Integer.valueOf(index), arg1);
    }

    public Serializable getProcessVariableAt(int index){
        return (Serializable)get(Integer.valueOf(index));
    }
}

