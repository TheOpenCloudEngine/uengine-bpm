package org.uengine.processpublisher.bpmn.importer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.omg.spec.bpmn._20100524.model.TLane;
import org.uengine.kernel.Activity;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.bpmn.Event;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.kernel.bpmn.SubProcess;
import org.uengine.modeling.RelationView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by MisakaMikoto on 2016. 7. 11..
 */
public class OpenGraphAdapter {
    List<Activity> allActivityList = new ArrayList<>();
    public List<Activity> getAllActivityList() {
        return allActivityList;
    }
    public void setAllActivityList(List<Activity> allActivityList) {
        this.allActivityList = allActivityList;
    }

    List<SequenceFlow> allSequenceFlowList = new ArrayList<>();
    public List<SequenceFlow> getAllSequenceFlowList() {
        return allSequenceFlowList;
    }
    public void setAllSequenceFlowList(List<SequenceFlow> allSequenceFlowList) {
        this.allSequenceFlowList = allSequenceFlowList;
    }

    public void createAllActivityListAndSequenceFlowList(ProcessDefinition processDefinition) {
        findSubProcessActivityInformation(processDefinition.getChildActivities(), processDefinition.getSequenceFlows());
    }

    public void findSubProcessActivityInformation(List<Activity> activityList, List<SequenceFlow> sequenceFlowList) {
        for(SequenceFlow sequenceFlow : sequenceFlowList) {
            getAllSequenceFlowList().add(sequenceFlow);
        }

        for(Activity activity : activityList) {
            if (activity instanceof Activity) {
                getAllActivityList().add(activity);

            } else if(activity instanceof SubProcess) {
                getAllActivityList().add(activity);
                findSubProcessActivityInformation(((SubProcess) activity).getChildActivities(), ((SubProcess) activity).getSequenceFlows());
            }

            else {
                ;
            }
        }

    }

    public void createOpenGraphInformation() {
        for(SequenceFlow sequenceFlow : getAllSequenceFlowList()) {
            StringBuffer values = new StringBuffer();

            for(Activity activity : getAllActivityList()) {
                if(activity.getTracingTag().equals(sequenceFlow.getSourceRef()) || activity.getTracingTag().equals(sequenceFlow.getTargetRef())) {
                    values.append(createValue(activity) + "@");
                }
            }
            sequenceFlow.getRelationView().setValue(values.toString().substring(0, values.length() - 1).replace("@", ","));
            sequenceFlow.getRelationView().setGeom(createGEOM(values.toString().substring(0, values.length() - 1)));
            sequenceFlow.getRelationView().setStyle(createStyle());
        }
    }

    private String createValue(Activity activity) {
        String[] values = new String[2];
        values[0] = String.valueOf((int) Math.round(activity.getElementView().getX()));
        values[1] = String.valueOf((int) Math.round(activity.getElementView().getY()));

        return Arrays.toString(values).replace(" ", "");
    }

    private String createGEOM(String values) {
        JSONObject geomJSON = new JSONObject();
        geomJSON.put("type", "PolyLine");

        JSONArray vertices = new JSONArray();
        String[] parseValues = values.split("@");
        for(String value : parseValues) {
            vertices.add(value);
        }
        geomJSON.put("vertices", vertices);

        String geom = geomJSON.toString();
        try {
            geom = URLEncoder.encode(geom, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return geom;
    }

    private String createStyle() {
        String style = "{\"map\":{\"stroke\":\"black\",\"fill-r\":\".5\",\"fill-cx\":\".5\",\"fill-cy\":\".5\",\"fill\":\"none\",\"fill-opacity\":0,\"label-position\":\"center\",\"stroke-width\":1.5,\"stroke-opacity\":1,\"edge-type\":\"plain\",\"edge-direction\":\"c c\",\"arrow-start\":\"none\",\"arrow-end\":\"block\",\"stroke-dasharray\":\"\",\"stroke-linejoin\":\"round\",\"cursor\":\"pointer\"}}";
        try {
            style = URLEncoder.encode(style, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return style;
    }
}
