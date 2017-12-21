package org.uengine.processpublisher;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.Role;
import org.uengine.kernel.bpmn.SequenceFlow;

import java.util.Enumeration;
import java.util.Vector;

public class MigUtils {

    private static int MAX_TRACING_TAG = 0;

    private static String PARENT_ROLE_NAME = "ROLE";

    private static double maxY = 0;
    
    private static boolean isParentSequenceActivity = false;
    
    private static boolean isDrawingSwitchActicity = false;

    public static double getYPosition(ProcessDefinition src, Activity curActivity){
        Activity tempActivity = curActivity;
        for(int i = 0 ; i < src.getWholeChildActivities().size() ; i++){
            if( i == 0 ){
                tempActivity.getElementView().setY(tempActivity.getElementView().getY() + MigDrawPositoin.getStartEventYPosition());
            }
            if(isDupYPosition(src, tempActivity)){
                tempActivity.getElementView().setY(tempActivity.getElementView().getY() + MigDrawPositoin.getActivityIntervalY() );
                maxY = tempActivity.getElementView().getY();
                System.out.println(tempActivity.getTracingTag() + " YYYYYYYYYYYYYYYYY " + tempActivity.getElementView().getY());
            }
        }

        return tempActivity.getElementView().getY();
    }

    //아래함수로는 위치조정후에는 겹치는게 없음.
/*    public static boolean isParallelStructure(ProcessDefinition src, Activity curActivity) {
        Activity tempActivity = curActivity;
        for(int i = 0 ; i < src.getWholeChildActivities().size() ; i++){
            if(isDupYPosition(src, tempActivity)){
               return true;
            }
        }
        return false;
    }*/

    private static boolean isDupYPosition(ProcessDefinition src, Activity curActivity) {
        Enumeration<Activity> enumeration = src.getWholeChildActivities().elements();
        while(enumeration.hasMoreElements()){
            Activity activity = (Activity)enumeration.nextElement();
            if(curActivity.getElementView().getX() == activity.getElementView().getX()
                    && curActivity.getElementView().getY() == activity.getElementView().getY()
                    && curActivity.getTracingTag().equals(activity.getTracingTag()) == false){

                System.out.println(curActivity.getTracingTag() + " vs " + activity.getTracingTag());
                return true;
            }
        }
        return false;
    }
    public static void setMaxTracingTag(ProcessDefinition src) {
        Enumeration enumeration = src.getWholeChildActivities().keys();

        int maxTrcTag = 0;
        while(enumeration.hasMoreElements()){
            int currTrcTag = cnvInt(enumeration.nextElement().toString());
            if( maxTrcTag < currTrcTag){
                maxTrcTag = currTrcTag;
            }
        }
        MAX_TRACING_TAG = maxTrcTag;
    }

    public static String getNewTracingTag(){
        return ++MAX_TRACING_TAG + "";
    }

    public static int cnvInt(String str){
        if(str == null || str.equals("")) return 0;
        return Integer.parseInt(str);
    }

    public static double getFirstRoleYPosition(ProcessDefinition processDefinition){
        double rv = MigDrawPositoin.getRoleXPosition();
        for(Role role : processDefinition.getRoles()){
            if(role.getElementView().getParent() != null) {
                if (rv > role.getElementView().getY()) {
                    rv = role.getElementView().getY();
                }
            }
        }
        return rv;
    }

    public static double getRoleTotalHeight(ProcessDefinition processDefinition){
        double rv = 0;
        for(Role role : processDefinition.getRoles()){
            if(role.getElementView().getParent() != null) {
                rv += role.getElementView().getHeight();
            }
        }
        return rv;
    }

    public static String getParentRoleName() {
        return PARENT_ROLE_NAME;
    }

    public static boolean isNotSourceTransition(ProcessDefinition processDefinition, String trcTag) {
        for (SequenceFlow sFlow : processDefinition.getSequenceFlows()) {
            if (sFlow.getSourceRef().equals(trcTag)) {
                return false;
            }
        }
        return true;
    }
    
    
    public static boolean isNotTargetTransition(ProcessDefinition processDefinition, String trcTag) {
        for (SequenceFlow sFlow : processDefinition.getSequenceFlows()) {
            if (sFlow.getTargetRef().equals(trcTag)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isParentSequenceActivity() {
        return isParentSequenceActivity;
    }

    public static void setParentSequenceActivity(boolean isParentSequenceActivity) {
        MigUtils.isParentSequenceActivity = isParentSequenceActivity;
    }

    public static boolean isDrawingSwitchActicity() {
        return isDrawingSwitchActicity;
    }

    public static void setDrawingSwitchActicity(boolean isDrawingSwitchActicity) {
        MigUtils.isDrawingSwitchActicity = isDrawingSwitchActicity;
    }
    
    
    public static boolean dupTransition(ProcessDefinition processDefinition, String soucrceTrcTag, String targetTrcTag){
        for(SequenceFlow sequenceFlow : processDefinition.getSequenceFlows()){
            if(sequenceFlow.getSourceRef().equals(soucrceTrcTag) && sequenceFlow.getTargetRef().equals(targetTrcTag) ){
                return true;
            }
        }
        return false;
    }
    
    public static String getOtherwiseSequenceElementValue(Activity sourceActivity, Activity targetActivity){
        StringBuffer rv = new StringBuffer();
        double sourceX = sourceActivity.getElementView().getX();
        double sourceY = sourceActivity.getElementView().getY();
        double targetX = targetActivity.getElementView().getX();
        double targetY = targetActivity.getElementView().getY();
        
        rv.append("[");
        rv.append("[");
        rv.append( (sourceX+1)+","+(sourceY+24) );
        rv.append("]");
        rv.append(",[");
        rv.append( (sourceX+1)+","+(sourceY+124) );
        rv.append("]");
        rv.append(",[");
        rv.append( (targetX+1)+","+(targetY+124) );
        rv.append("]");
        rv.append(",[");
        rv.append( (targetX+1)+","+(targetY+24) );
        rv.append("]");
        rv.append("]");
        
        return rv.toString();
    }
}

