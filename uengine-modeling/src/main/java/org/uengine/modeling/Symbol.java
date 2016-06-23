package org.uengine.modeling;

import javax.naming.event.EventContext;

import org.metaworks.Refresh;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToEvent;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.NonEditable;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.Clipboard;

//@AutowiredFromClient(onDrag = true)
public class Symbol {

	String name;
	String shapeId;
	String shapeType;
	String elementClassName;
	int width;
	int height;
	boolean iconResizing;
	
	@NonEditable
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getShapeId() {
		return shapeId;
	}
	public void setShapeId(String shapeId) {
		this.shapeId = shapeId;
	}
	
	public String getShapeType() {
		return shapeType;
	}
	public void setShapeType(String shapeType) {
		this.shapeType = shapeType;
	}

	public String getElementClassName() {
		return elementClassName;
	}
	public void setElementClassName(String elementClassName) {
		this.elementClassName = elementClassName;
	}

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}


	public boolean isIconResizing() {
		return iconResizing;
	}
	public void setIconResizing(boolean iconResizing) {
		this.iconResizing = iconResizing;
	}


	@ServiceMethod(callByContent=true, mouseBinding="drag", target=ServiceMethodContext.TARGET_APPEND, clientSide = true)
	public void drag(){

		/**
		 *
		 * alert('xxxx');
		 *
		 *
		 */
//		return new Refresh(new Clipboard("drop", this), true);
	}
	
}
