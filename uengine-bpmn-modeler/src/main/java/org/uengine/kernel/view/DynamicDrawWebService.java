package org.uengine.kernel.view;

import org.uengine.kernel.Activity;

import java.util.ArrayList;

public class DynamicDrawWebService implements DynamicDrawGeom {
	
	String parentGeomId;
		public String getParentGeomId() {
			return parentGeomId;
		}
		public void setParentGeomId(String parentGeomId) {
			this.parentGeomId = parentGeomId;
		}

	String editorId;
		public String getEditorId() {
			return editorId;
		}
		public void setEditorId(String editorId) {
			this.editorId = editorId;
		}
		
	String viewType;
		public String getViewType() {
			return viewType;
		}
		public void setViewType(String viewType) {
			this.viewType = viewType;
		}
		
	ArrayList<Activity> activityList;
	public ArrayList<Activity> getActivityList() {
		return activityList;
	}
	public void setActivityList(ArrayList<Activity> activityList) {
		this.activityList = activityList;
	}

}
