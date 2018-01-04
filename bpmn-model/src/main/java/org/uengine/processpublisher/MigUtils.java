package org.uengine.processpublisher;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessDefinition;

import java.util.Enumeration;

public class MigUtils {

    private static int MAX_TRACING_TAG = 0;

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

    public static String getRelationViewValue(Activity sourceActivity, Activity targetActivity, boolean isOtherwise){
        StringBuffer rv = new StringBuffer();
        String cl_ = "[";
        String cr_ = "]";
        String cs_ = ",";

        double sourceX = sourceActivity.getElementView().getX();
        double sourceY = sourceActivity.getElementView().getY();
        double sourceW = sourceActivity.getElementView().getWidth();
        double sourceH = sourceActivity.getElementView().getHeight();

        double targetX = targetActivity.getElementView().getX();
        double targetY = targetActivity.getElementView().getY();
        double targetW = targetActivity.getElementView().getWidth();
        double targetH = targetActivity.getElementView().getHeight();

        rv.append(cl_);
        //포인트가 3개이상 있는경우만 생성(그외는 자동 설정됨)
        //Otherwise의경우 SourceY = targetY 가 같은경우만 4point 계산(강제생성된 otherwise)
        if( (isOtherwise  && (sourceY == targetY)) || (sourceY < targetY) ){
            //down
            rv.append( cl_ + (sourceX)+ cs_ +(sourceY + (sourceH / 2)) + cr_ );
            rv.append( cs_ );

            if( (isOtherwise  && (sourceY == targetY))  ){
                rv.append( cl_ + (sourceX)+ cs_ +(sourceY + sourceH) + cr_ );
                rv.append( cs_ );
                rv.append( cl_ + (targetX)+ cs_ +(sourceY + sourceH) + cr_ );
                rv.append( cs_ );
                rv.append( cl_ + (targetX)+ cs_ +(targetY + (targetH / 2)) + cr_ );
            }else{
                rv.append( cl_ + (sourceX)+ cs_ +(targetY) + cr_ );
                rv.append( cs_ );
                rv.append( cl_ + (targetX - (targetW / 2))+ cs_ +(targetY) + cr_ );
            }
        }else if( sourceY > targetY ){
            //to right
            rv.append( cl_ + (sourceX + (sourceW / 2))+ cs_ +(sourceY) + cr_ );
            rv.append( cs_ );
            rv.append( cl_ + (targetX)+ cs_ +(sourceY) + cr_ );
            rv.append( cs_ );
            rv.append( cl_ + (targetX)+ cs_ +(targetY + (targetH / 2)) + cr_ );
        }else{
            return null;
        }
        rv.append( cr_ );

        return rv.toString();
    }

}

