package org.uengine.processpublisher;


import com.thoughtworks.xstream.XStream;
import org.uengine.kernel.Activity;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.SwitchActivity;
import org.uengine.kernel.bpmn.Gateway;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.RelationView;
import org.uengine.processpublisher.uengine3.importer.ProcessDefinitionAdapter;

import java.io.File;
import java.util.Hashtable;
import java.util.List;

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
                String packageStr = adapterBaseClass.getPackage().getName();

                //System.out.println(packageStr + "." + activityTypeName + "Adapter");
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
            //System.out.println("matching adapter for " + clazz + " is " + adapter);
            adapters.put(clazz.getName(), adapter);
        }

        return adapter;
    }

    public static ProcessDefinition createSequenceFlow(ProcessDefinition processDefinition, Hashtable keyedContext) {
        SequenceFlow sequenceFlow = new SequenceFlow();
        int index = (int) keyedContext.get("index");
        if(index > 0) {

            String targetActivityId_prefix = "";
            String sourceActivityId_prefix = "";
            Activity targetRefActivity = processDefinition.getChildActivities().get(index);
            Activity sourceRefActivity = processDefinition.getChildActivities().get(index - 1);

            if(targetRefActivity instanceof HumanActivity) {
                targetActivityId_prefix = "HumanActivity_";
            } else {
                //TODO
            }

            if(sourceRefActivity instanceof HumanActivity) {
                sourceActivityId_prefix = "HumanActivity_";
            } else {
                //TODO
            }

            // toEdge, fromEdge 설정
            targetRefActivity.getElementView().setFromEdge(sourceActivityId_prefix + index);
            sourceRefActivity.getElementView().setToEdge(targetActivityId_prefix+index);

            sequenceFlow.setSourceRef(sourceRefActivity.getTracingTag());
            sequenceFlow.setTargetRef(targetRefActivity.getTracingTag());
            RelationView relationView = sequenceFlow.createView();
            relationView.setShapeId("OG.shape.EdgeShape");
            relationView.setFrom(sourceRefActivity.getElementView().getId()+"_TERMINAL_C_INOUT_0");
            relationView.setTo(targetRefActivity.getElementView().getId()+"_TERMINAL_C_INOUT_0");

            Double relationViewWidth = targetRefActivity.getElementView().getX() + (targetRefActivity.getElementView().getX() - sourceRefActivity.getElementView().getX())/2;

            relationView.setWidth(relationViewWidth);
            relationView.setHeight(0);
            sequenceFlow.setRelationView(relationView);
            processDefinition.addSequenceFlow(sequenceFlow);
        }


        return processDefinition;
    }

    public static ProcessDefinition importAdapt(File file, Class adapterBaseClass) throws Exception{

        XStream xstream = new XStream();
        xstream.ignoreUnknownElements();
        // file로부터 xstream을 통해서 객체로 만든다.
        Object definitionObject = xstream.fromXML(file);
        Hashtable keyedContext = new Hashtable();
        ProcessDefinition processDefinition = (ProcessDefinition)getAdapter(((ProcessDefinition)definitionObject).getClass(), adapterBaseClass)
                .convert(((ProcessDefinition) definitionObject), keyedContext);




        //List<Activity> list = ((ProcessDefinition)definitionObject).getChildActivities();

        //int idx = 0;
/*
        for(Activity activity : list) {
            if(activity instanceof HumanActivity) {
                Hashtable keyedContext = new Hashtable();
                keyedContext.put("index", idx);
                keyedContext.put("root", processDefinition);
                processDefinition = (ProcessDefinition) getAdapter(activity.getClass(), adapterBaseClass).convert(activity, keyedContext);
            } else if(activity instanceof SwitchActivity) {
                Gateway gateWay = new Gateway();
                ElementView elementView = gateWay.createView();
                elementView.setX(150);
                elementView.setY(150);
                elementView.setWidth(50);
                elementView.setHeight(50);
                gateWay.setElementView(elementView);
                processDefinition.addChildActivity(gateWay);
            }

            idx++;
        }
*/
        return processDefinition;
    }
}
