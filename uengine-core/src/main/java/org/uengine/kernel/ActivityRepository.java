package org.uengine.kernel;

import java.util.ArrayList;

public class ActivityRepository extends ArrayList<Activity>{
	transient private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	transient ComplexActivity parent;
		
	public ActivityRepository(ComplexActivity parent){
		super();
		this.parent = parent;
	}
	
	public ActivityRepository(){
		super();
	}
	
	public void setComplexActivityParent(ComplexActivity parent){
		this.parent = parent;
	}
		

	public void add(int index, Activity obj){
		Activity child = (Activity)obj;
		
		//I'm your parent...
		//if(parent!=null)
		child.setParentActivity(parent);
		
		if(index>-1)
			super.add(index, child);
		else
			super.add(child);
			
		//TODO: tracingtag
		//child.setTracingTag(parent.getTracingTag() + "_" + size());
	}

	public boolean add(Activity child){
		add(-1, child);
		
		return true;
	}
}

