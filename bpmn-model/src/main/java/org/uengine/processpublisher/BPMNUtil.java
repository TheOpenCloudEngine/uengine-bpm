package org.uengine.processpublisher;


import org.uengine.kernel.ProcessDefinition;
import org.uengine.processpublisher.bpmn.importer.TBoundaryEventAdapter;
import org.uengine.processpublisher.microsoft.importer.MSProjectFileAdapter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.Hashtable;

public class BPMNUtil {
    public static final String MSProject = "project";
    public static final String VP = "definitions";

    static Hashtable adapters = new Hashtable();

    public static Adapter getAdapter(Class clazz, boolean isImporter){
        if(adapters.containsKey(clazz.getName())) {
            return (Adapter) adapters.get(clazz.getName());
        }

        Adapter adapter = null;

        do {
            try {
                String activityTypeName = org.uengine.util.UEngineUtil.getClassNameOnly(clazz);
                String packageStr = BPMNUtil.class.getPackage().getName() + ".bpmn";

                if(activityTypeName.toLowerCase().contains("microsoft")) {
                    adapter = (Adapter) Thread.currentThread().getContextClassLoader().loadClass(packageStr + "." + (isImporter ? "importer" : "exporter") + "." + activityTypeName + "Adapter").newInstance();
                } else {
                    adapter = (Adapter) Thread.currentThread().getContextClassLoader().loadClass(packageStr + "." + (isImporter ? "importer" : "exporter") + "." + activityTypeName + "Adapter").newInstance();
                }
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

    public static Object importAdapt(Object value, Hashtable context) throws Exception {
        return getAdapter(value.getClass(), true).convert(value, context);
    }

    public static Object exportAdapt(Object value, Hashtable context) throws Exception {
        return getAdapter(value.getClass(), false).convert(value, context);
    }

    public static Object importAdapt(Object value) throws Exception {
        return getAdapter(value.getClass(), true).convert(value, null);
    }

    public static Object exportAdapt(Object value) throws Exception {
        return getAdapter(value.getClass(), false).convert(value, null);
    }


    public static ProcessDefinition importAdapt(File file) throws Exception{
        ProcessDefinition processDefinition = null;
        String productName = checkProductName(file);

        if(VP.equals(productName)) {
            JAXBContext jc = JAXBContext.newInstance("org.omg.spec.bpmn._20100524.model:org.omg.spec.bpmn._20100524.di:org.activiti.bpmn");
            Unmarshaller um = jc.createUnmarshaller();
            JAXBElement element = (JAXBElement) um.unmarshal(file);

            processDefinition = (ProcessDefinition) importAdapt(element.getValue());
            processDefinition.afterDeserialization();

        } else if(MSProject.equals(productName)) {
            processDefinition = (ProcessDefinition) importAdapt(new MSProjectFileAdapter());
            processDefinition.afterDeserialization();

        } else {
            ;
        }

        return processDefinition;
    }

    public static ProcessDefinition importAdapt(InputStream is) throws Exception{
        ProcessDefinition processDefinition = null;
        String productName = checkProductName(is);

        if(VP.equals(productName)) {
            JAXBContext jc = JAXBContext.newInstance("org.omg.spec.bpmn._20100524.model:org.omg.spec.bpmn._20100524.di:org.activiti.bpmn");
            Unmarshaller um = jc.createUnmarshaller();
            JAXBElement element = (JAXBElement) um.unmarshal(is);

            processDefinition = (ProcessDefinition) importAdapt(element.getValue());
            processDefinition.afterDeserialization();

        } else if(MSProject.equals(productName)) {
            processDefinition = (ProcessDefinition) importAdapt(new MSProjectFileAdapter());
            processDefinition.afterDeserialization();

        } else {
            ;
        }

        return processDefinition;
    }

    public static String checkProductName(Object object) {
        FileInputStream fileInputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader = null;

        String productName ;
        String temp;
        String content = null;

        int lineCount = 0;

        try {
            if(object instanceof File) {
                fileInputStream = new FileInputStream((File) object);
                inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                bufferedReader = new BufferedReader(inputStreamReader);

            } else if(object instanceof InputStream){
                inputStreamReader = new InputStreamReader((InputStream) object, "UTF-8");
                bufferedReader = new BufferedReader(inputStreamReader);

            } else {
                ;
            }

            while( (temp = bufferedReader.readLine()) != null) {
                ++lineCount;

                // lineCount 2 is first XML's element
                if(lineCount == 2) {
                    content = temp;

                    break;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(content.toLowerCase().contains(MSProject)) {
            productName = MSProject;

        } else {
            productName = VP;
        }

        return productName;
    }

}
