package org.uengine.processpublisher;


import org.uengine.kernel.ProcessDefinition;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Hashtable;

public class AdapterUtil {

    static Hashtable adapters = new Hashtable();

    public static Adapter getAdapter(Class activityType){

        Class originalType = activityType;

        if(adapters.containsKey(activityType.getName()))
            return (Adapter)adapters.get(activityType.getName());

        Adapter adapter = null;
        do{
            try{
                String activityTypeName = org.uengine.util.UEngineUtil.getClassNameOnly(activityType);

                adapter = (Adapter)Thread.currentThread().getContextClassLoader().loadClass("org.uengine.processpublisher.bpmn.importer." + activityTypeName + "Adapter").newInstance();

                if(adapter!=null)
                    adapters.put(activityType.getName(), adapter);

            }catch(Exception e){
                activityType = activityType.getSuperclass();
            }
        }while(adapter==null && activityType!=Object.class);

        if(adapter==null)
            System.out.println("ProcessDefinitionAdapter::getAdapter : can't find adapter for " + originalType);
        else {
            System.out.println("matching adapter for " + originalType + " is " + adapter);
            adapters.put(originalType.getName(), adapter);
        }

        return adapter;
    }

    public static Object adapt(Object value, Hashtable context) throws Exception {
        return getAdapter(value.getClass()).convert(value, context);
    }

    public static Object adapt(Object value) throws Exception {
        return getAdapter(value.getClass()).convert(value, null);
    }


    public static ProcessDefinition adapt(File file) throws Exception{
        JAXBContext jc = JAXBContext.newInstance("org.omg.spec.bpmn._20100524.model:org.omg.spec.bpmn._20100524.di:org.activiti.bpmn");
        Unmarshaller um = jc.createUnmarshaller();

        JAXBElement element = (JAXBElement) um.unmarshal(file);


        return (ProcessDefinition) adapt(element.getValue());
    }

}
