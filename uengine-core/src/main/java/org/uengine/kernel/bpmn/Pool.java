package org.uengine.kernel.bpmn;

import org.metaworks.*;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Name;
import org.metaworks.annotation.Order;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.uengine.contexts.TextContext;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.view.DynamicDrawGeom;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.IElement;
import org.uengine.util.UEngineUtil;

@Face(ejsPath="genericfaces/ActivityFace.ejs", options={"fieldOrder"}, values={"name,description"})
public class Pool{

	transient MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}

	String name;
	@Face(displayName="$poolName")
	@Order(1)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
		
	TextContext description = org.uengine.contexts.TextContext.createInstance();
	@Name
	@Face(displayName="$poolDisplayName")
	@Order(2)
		public String getDescription() {
			return description.getText();
		}
		public void setDescription(TextContext string) {
			description = string;
		}
		public void setDescription(String string) {
			description.setText(string);
		}
		


	transient String currentEditorId;
	@Hidden
		public String getCurrentEditorId() {
			return currentEditorId;
		}
		public void setCurrentEditorId(String currentEditorId) {
			this.currentEditorId = currentEditorId;
		}	
	public Pool(){
		setMetaworksContext(new MetaworksContext());
		this.setDescription("");
	}

	String viewId;
	@Hidden
		public String getViewId() {
			return viewId;
		}
		public void setViewId(String viewId) {
			this.viewId = viewId;
		}


	public void createDocument() {
		// TODO Auto-generated method stub
	}
}
