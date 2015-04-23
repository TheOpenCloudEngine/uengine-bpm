package org.uengine.kernel;

import java.util.List;

import org.uengine.modeling.IModel;

public interface IDefinitionModel extends IModel {

	public String getName();
	public List<Activity> getChildActivities();
	
}
