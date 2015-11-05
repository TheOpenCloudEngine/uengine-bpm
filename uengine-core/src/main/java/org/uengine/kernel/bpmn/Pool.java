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
import org.uengine.webservice.ApplyProperties;

@Face(ejsPath="genericfaces/ActivityFace.ejs", options={"fieldOrder"},values={"name,description"})
public class Pool implements IElement, java.io.Serializable, ContextAware{

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
		
	TextContext description = TextContext.createInstance();
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
		
	PoolResolutionContext poolResolutionContext;
	@Hidden
	@Face(displayName="웹서비스 선택")
	@Order(3)
		public PoolResolutionContext getPoolResolutionContext() {
			if( poolResolutionContext == null ){
				String poolResolutionContexts = GlobalContext.getPropertyString("poolresolutioncontexts", null);
				if( poolResolutionContexts != null ){
					try{
						Class<?> clazz = Class.forName(poolResolutionContexts);
//						if( clazz.equals(DefaultCompanyPoolResolutionContext.class)){
							poolResolutionContext = (PoolResolutionContext) clazz.newInstance();
//						}
					}catch(Exception e){
						//e.printStackTrace();
					}
				}
			}
			return poolResolutionContext;
		}
		public void setPoolResolutionContext(PoolResolutionContext poolResolutionContext) {
			this.poolResolutionContext = poolResolutionContext;
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

	@Hidden
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] apply() throws Exception {

		if ( this.getDescription() == null || "".equals(this.getDescription())){
			this.setDescription(this.getName());
		}

		DynamicDrawGeom ddg = this.getPoolResolutionContext().drawActivitysOnDesigner();
		ddg.setParentGeomId(this.getViewId());

		return new Object[]{ddg};
	}

	@Hidden
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] cancel(){
		ModalWindow modalWindow = new ModalWindow();
		return new Object[]{new Remover(modalWindow , true)};
		
	}
	
	ElementView elementView;

	public ElementView createView(){
		ElementView elementView = (ElementView) UEngineUtil.getComponentByEscalation(getClass(), "view");
		elementView.setElement(this);

		return elementView;
	}
	@Hidden
	public ElementView getElementView() {
		return this.elementView;
	}
	public void setElementView(ElementView elementView) {
		this.elementView = elementView;
	}
	
	public void createDocument() {
		// TODO Auto-generated method stub
	}
}
