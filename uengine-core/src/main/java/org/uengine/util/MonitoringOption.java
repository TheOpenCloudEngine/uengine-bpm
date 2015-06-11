package org.uengine.util;

import java.util.*;

public class MonitoringOption {
	private HashMap businessStatus;
	private HashMap onClickAction;
	private HashMap repeatCountMessage;	
	private HashMap portraitURL;	
	private HashMap acticityDetail;	
	private Map flowchartOptions;
	private HashMap statusCode;
	
	
	final public static String ACTIVITY_DETAIL_INDEX_KEYS = "activityDetailIndexKeys";
	final public static String ACTIVITY_PORTRAIT = "activityPortrait";
	final public static String ACTIVITY_DETAIL = "activityDetail";

	final public static String USER_NAME = "userName";
	final public static String USER_PORTRAIT = "userPortrait";
	
	public MonitoringOption(){
		businessStatus = new HashMap();
		onClickAction = new HashMap();
		repeatCountMessage = new HashMap();
		portraitURL = new HashMap();
		acticityDetail = new HashMap();
		statusCode = new HashMap();
	}
	
	public HashMap getStatusCode() {
		return statusCode;
	}
	
	public void setStatusCode(String statusCode) {
		if(!getStatusCode().containsKey(statusCode))
			getStatusCode().put(statusCode, statusCode);
	}
	
	public void setBusinessStatus(String tracingTag, String status){
		businessStatus.put(tracingTag, status);
	}
	
	public HashMap getBusinessStatus(){
		return businessStatus;
	}
	
	public void setOnClickAction(String tracingTag, String javascript){
		onClickAction.put(tracingTag, javascript);
	}

	public HashMap getOnClickAction(){
		return onClickAction;
	}
	
	public void addActivityDetail(String tracingTag, String key, String value){
		HashMap acticityDetailList = (HashMap)acticityDetail.get(tracingTag);
		if(acticityDetailList !=null){
			HashMap activityDetailIndexKeys = (HashMap)acticityDetailList.get(ACTIVITY_DETAIL_INDEX_KEYS);
			activityDetailIndexKeys.put(""+activityDetailIndexKeys.size(),key);
			acticityDetailList.put(key, value);
		}else{
			acticityDetailList = new HashMap();
			HashMap activityDetailIndexKeys = new HashMap();
			
			activityDetailIndexKeys.put("0",key);
			acticityDetailList.put(ACTIVITY_DETAIL_INDEX_KEYS, activityDetailIndexKeys);
			acticityDetailList.put(key, value);
		}
		
		acticityDetail.put(tracingTag, acticityDetailList);
	}
	
	public HashMap getActivityDetail(){
		return acticityDetail;
	}
	
	public void clearDetails(){
		acticityDetail.clear();
	}
	
	public void setRepeatCount(String tracingTag, String countMessage){
		repeatCountMessage.put(tracingTag, countMessage);
	}

	public HashMap getRepeatCount(){
		return repeatCountMessage;
	}
	
	public void setPortraitURL(String tracingTag,String userName, String portraitURLString){
		HashMap userInfo = new HashMap();
		userInfo.put(USER_NAME, userName);
		userInfo.put(USER_PORTRAIT, portraitURLString);
		
		portraitURL.put(tracingTag, userInfo);
	}
	
	public HashMap getPortraitURL(){
		return portraitURL;
	}

	public Map getFlowchartOptions() {
		return flowchartOptions;
	}

	public void setFlowchartOptions(Map flowchartOptions) {
		this.flowchartOptions = flowchartOptions;
	}

}
