package org.uengine.processpublisher;


import org.uengine.kernel.ProcessDefinition;
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
                String packageStr = clazz.getPackage().getName();
                packageStr = packageStr.substring(0, packageStr.lastIndexOf("."));

                if(activityTypeName.toLowerCase().contains("microsoft")) {
                    adapter = (Adapter) Thread.currentThread().getContextClassLoader().loadClass(packageStr + "." + (isImporter ? "importer" : "exporter") + "." + activityTypeName + "Adapter").newInstance();
                } else {
                    adapter = (Adapter) Thread.currentThread().getContextClassLoader().loadClass(packageStr + "." + (isImporter ? "importer" : "exporter") + "." + activityTypeName + "Adapter").newInstance();
                }
                if(adapter!=null) {
                    adapters.put(clazz.getName(), adapter);
                }

            } catch(Exception e){
                clazz = clazz.getSuperclass();
            }

        } while(adapter == null && clazz != Object.class);

        if(adapter==null) {
            System.out.println("ProcessDefinitionAdapter::getAdapter : can't find adapter for " + clazz);

        } else {
            System.out.println("matching adapter for " + clazz + " is " + adapter);
            adapters.put(clazz.getName(), adapter);
        }

        return adapter;
    }

    public static Object adapt(Object value, Hashtable context) throws Exception {
        return getAdapter(value.getClass(), true).convert(value, context);
    }

    public static Object export(Object value, Hashtable context) throws Exception {
        return getAdapter(value.getClass(), false).convert(value, context);
    }

    public static Object adapt(Object value) throws Exception {
        return getAdapter(value.getClass(), true).convert(value, null);
    }

    public static Object export(Object value) throws Exception {
        return getAdapter(value.getClass(), false).convert(value, null);
    }


    public static ProcessDefinition adapt(File file) throws Exception{
        ProcessDefinition processDefinition = null;
        String productName = checkProductName(file);

        if(VP.equals(productName)) {
            JAXBContext jc = JAXBContext.newInstance("org.omg.spec.bpmn._20100524.model:org.omg.spec.bpmn._20100524.di:org.activiti.bpmn");
            Unmarshaller um = jc.createUnmarshaller();
            JAXBElement element = (JAXBElement) um.unmarshal(file);

            processDefinition = (ProcessDefinition) adapt(element.getValue());
            processDefinition.afterDeserialization();

        } else if(MSProject.equals(productName)) {
            processDefinition = (ProcessDefinition) adapt(new MSProjectFileAdapter());
            processDefinition.afterDeserialization();

        } else {
            ;
        }

        return processDefinition;
    }

    public static ProcessDefinition adapt(InputStream is) throws Exception{
        ProcessDefinition processDefinition = null;
        String productName = checkProductName(is);

        if(VP.equals(productName)) {
            JAXBContext jc = JAXBContext.newInstance("org.omg.spec.bpmn._20100524.model:org.omg.spec.bpmn._20100524.di:org.activiti.bpmn");
            Unmarshaller um = jc.createUnmarshaller();
            JAXBElement element = (JAXBElement) um.unmarshal(is);

            processDefinition = (ProcessDefinition) adapt(element.getValue());
            processDefinition.afterDeserialization();

        } else if(MSProject.equals(productName)) {
            processDefinition = (ProcessDefinition) adapt(new MSProjectFileAdapter());
            processDefinition.afterDeserialization();

        } else {
            ;
        }

        return processDefinition;
    }

    public static String checkProductName(Object object) {
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        String productName = null;
        String temp = null;
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
