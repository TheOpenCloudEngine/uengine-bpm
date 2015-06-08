package org.uengine.kernel.view;

import org.uengine.modeling.ElementView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class ReferenceActivityView extends ActivityView {

	String className;
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}

	
	public ReferenceActivityView(){
		setShapeId(SHAPE_ID);
	}
	
	public ReferenceActivityView(IElement element){
		super(element);
	}
	
	public ReferenceActivityView(ElementView elementView){
		this.setId(elementView.getId());
		this.setClassName(elementView.getElement().getClass().getName());
		this.setHeight(elementView.getHeight());
		this.setWidth(elementView.getWidth());
		this.setShapeId(elementView.getShapeId());
		this.setX(elementView.getX());
		this.setY(elementView.getY());
	}


}
