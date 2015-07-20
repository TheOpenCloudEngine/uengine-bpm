package org.uengine.kernel.view;

import org.uengine.kernel.Activity;

import java.util.ArrayList;

public interface DynamicDrawGeom {

	public String getParentGeomId();
	public void setParentGeomId(String parentGeomId);

	public String getEditorId();
	public void setEditorId(String editorId);
	
	public String getViewType();
	public void setViewType(String viewType);
	
	public ArrayList<Activity> getActivityList();
	public void setActivityList(ArrayList<Activity> activityList);
}
