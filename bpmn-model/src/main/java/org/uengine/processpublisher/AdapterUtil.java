package org.uengine.processpublisher;


import org.uengine.kernel.ProcessDefinition;
import org.uengine.processpublisher.microsoft.importer.MSProjectFileAdapter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.Hashtable;

public class AdapterUtil {
    static Hashtable adapters = new Hashtable();

    public static Adapter getAdapter(Class clazz, Class adapterBaseClass){
        if(adapters.containsKey(clazz.getName())) {
            return (Adapter) adapters.get(clazz.getName());
        }

        Adapter adapter = null;

        do {
            try {
                String activityTypeName = org.uengine.util.UEngineUtil.getClassNameOnly(clazz);
                String packageStr = adapterBaseClass.getClass().getPackage().getName();

                adapter = (Adapter) Thread.currentThread().getContextClassLoader().loadClass(packageStr + "." + activityTypeName + "Adapter").newInstance();

                if(adapter != null) {
                    adapters.put(clazz.getName(), adapter);
                }

            } catch(Exception e){
                clazz = clazz.getSuperclass();
            }

        } while(adapter == null && clazz != Object.class);

        if(adapter == null) {
            System.out.println("ProcessDefinitionAdapter::getAdapter : can't find adapter for " + clazz);

        } else {
            System.out.println("matching adapter for " + clazz + " is " + adapter);
            adapters.put(clazz.getName(), adapter);
        }

        return adapter;
    }

}
