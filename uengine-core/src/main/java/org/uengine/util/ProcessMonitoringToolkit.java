package org.uengine.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.uengine.kernel.Activity;
import org.uengine.kernel.DefaultProcessInstance;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.LoopActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;

public class ProcessMonitoringToolkit {
	
	public static String createFlowChart(InputStream inputStream,MonitoringOption options) throws Exception{

		
		final ProcessDefinition definition = (ProcessDefinition) GlobalContext.deserialize(
				inputStream, ProcessDefinition.class);
		
		final HashMap statusCode = options.getStatusCode();
		
		ProcessInstance instance = new DefaultProcessInstance(){

			public String getStatus(String scope) throws Exception {
				Activity act =  definition.getActivity(scope);
				
				if(act instanceof ProcessDefinition) return Activity.STATUS_RUNNING;
				
				return statusCode.containsKey(act.getStatusCode()) ? Activity.STATUS_RUNNING : Activity.STATUS_READY; 
			}
			
		};
		
		ProcessMonitoringToolkit processMonitoringToolkit = new ProcessMonitoringToolkit();
		//businessStatus
		processMonitoringToolkit.setBusinessStatus(definition,instance,options);
		
		processMonitoringToolkit.setActivityDetail(options);
		
		processMonitoringToolkit.setOnClickAction(options);
		
		processMonitoringToolkit.setPortraitURL(options);
		
		processMonitoringToolkit.setRepeatCount(definition, instance, options);
		
		return null;
		
	}

	public static String createFlowChart(String xpdFilePath,MonitoringOption options) throws Exception{
		return createFlowChart(new FileInputStream(xpdFilePath), options);
	}
	
	private void setBusinessStatus(ProcessDefinition pd,ProcessInstance pi,MonitoringOption options) throws Exception{
		HashMap businessStatus = options.getBusinessStatus();
		Set clonedSet = new HashSet();
		clonedSet.addAll(businessStatus.keySet());
		Iterator keyIter = clonedSet.iterator();
		while(keyIter.hasNext()) {
			String tracingTag = (String)keyIter.next();
			String statusString = (String)businessStatus.get(tracingTag);
			
			pd.getActivity(tracingTag).setBusinessStatus(pi,statusString);
		}
	}
	
	private void setOnClickAction(MonitoringOption monitoringOption) throws Exception{
		HashMap onClickAction = monitoringOption.getOnClickAction();
		Map flowchartOption = monitoringOption.getFlowchartOptions();
		Set clonedSet = new HashSet();
		clonedSet.addAll(onClickAction.keySet());
		Iterator keyIter = clonedSet.iterator();
		while(keyIter.hasNext()) {
			String tracingTag = (String)keyIter.next();
			String OnClickActionString = (String)onClickAction.get(tracingTag);
			
			flowchartOption.put("onClickFor"+tracingTag, OnClickActionString);
		}
	}

	private void setRepeatCount(ProcessDefinition pd,ProcessInstance pi,MonitoringOption options) throws Exception{
		HashMap repeatCount = options.getRepeatCount();
		Set clonedSet = new HashSet();
		clonedSet.addAll(repeatCount.keySet());
		Iterator keyIter = clonedSet.iterator();
		while(keyIter.hasNext()) {
			String tracingTag = (String)keyIter.next();
			String repeatCountString = (String)repeatCount.get(tracingTag);
			
			((LoopActivity)pd.getActivity(tracingTag)).setLoopingCount(pi, Integer.parseInt(repeatCountString));
		}
	}

	private void setPortraitURL(MonitoringOption monitoringOption) throws Exception{
		HashMap portraitURL = monitoringOption.getPortraitURL();
		Map flowchartOption = monitoringOption.getFlowchartOptions();
		if(flowchartOption !=null)
			flowchartOption.put(MonitoringOption.ACTIVITY_PORTRAIT, portraitURL);
	}
	
	private void setActivityDetail(MonitoringOption monitoringOption) throws Exception{
		HashMap activityDetail = monitoringOption.getActivityDetail();
		Map flowchartOption = monitoringOption.getFlowchartOptions();
		if(flowchartOption !=null)
			flowchartOption.put(MonitoringOption.ACTIVITY_DETAIL, activityDetail);
		
//		Set clonedSet = new HashSet();
//		clonedSet.addAll(activityDetail.keySet());
//		Iterator keyIter = clonedSet.iterator();
//		while(keyIter.hasNext()) {
//			String tracingTag = (String)keyIter.next();
//			HashMap activityDetailList = (HashMap)activityDetail.get(tracingTag);
//			Vector activityDetailIndexKeys = (Vector)activityDetailList.get(MonitoringOption.ACTIVITY_DETAIL_INDEX_KEYS);
//			for(int i=0 ; i < activityDetailIndexKeys.size();i++){
//				String key = (String)activityDetailIndexKeys.get(i);
//				String value = (String)activityDetailList.get(key);
//				
//			}
//		}
	}
	
	public static void main(String[] args) throws Exception{
		String path ="C:\\DATA\\WORK\\uEngine\\LEP44\\uengine3.0_0610\\webapps\\monitoringToolkit\\ex3.XPD";
		Hashtable options = new Hashtable();
		//options.put("businessStatus", arg1);
		//System.out.println(ProcessMonitoringToolkit.createFlowChart(new FileInputStream(path), "act1",options));
	}

}
