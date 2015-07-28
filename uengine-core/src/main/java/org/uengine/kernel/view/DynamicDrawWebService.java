package org.uengine.kernel.view;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.kernel.Activity;
import org.uengine.modeling.Canvas;
import org.uengine.modeling.ElementView;

import java.util.ArrayList;
import java.util.Iterator;

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

	@ServiceMethod(callByContent = true)
	public void load(@AutowiredFromClient Canvas canvas){

		Iterator iterator = canvas.getElementViewList().iterator();
		while(iterator.hasNext()){
			ElementView elementView = (ElementView) iterator.next();
			if(this.getParentGeomId().equals(elementView.getId())){

				break;
			}
		}
	}
}
