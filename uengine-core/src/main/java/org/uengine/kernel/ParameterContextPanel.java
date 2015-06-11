package org.uengine.kernel;

import java.util.ArrayList;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.component.SelectBox;
import org.metaworks.widget.ModalWindow;

public class ParameterContextPanel  implements ContextAware{
	
	
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	String editorId;
	@Id
		public String getEditorId() {
			return editorId;
		}
		public void setEditorId(String editorId) {
			this.editorId = editorId;
		}
	String parentEditorId;
		public String getParentEditorId() {
			return parentEditorId;
		}
		public void setParentEditorId(String parentEditorId) {
			this.parentEditorId = parentEditorId;
		}	
	ParameterContext[] parameterContext;
		public ParameterContext[] getParameterContext() {
			return parameterContext;
		}
		public void setParameterContext(ParameterContext[] parameterContext) {
			this.parameterContext = parameterContext;
		}
	ParameterContext selectedContext;
		public ParameterContext getSelectedContext() {
			return selectedContext;
		}
		public void setSelectedContext(ParameterContext selectedContext) {
			this.selectedContext = selectedContext;
		}
		
	public ParameterContextPanel(){
		this.setMetaworksContext(new MetaworksContext());
		this.getMetaworksContext().setWhen("edit");
	}
	
	public void load() throws Exception{
		if( parameterContext == null ){
			parameterContext = new ParameterContext[0];
		}
	}
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object addActivityVariable() throws Exception{
		
		ParameterContext parameterContext = new ParameterContext();
		parameterContext.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		parameterContext.getMetaworksContext().setHow("add");
<<<<<<< HEAD
		
=======

>>>>>>> c85c3036a4c18b8ee81f7cf8af970f013d8bf07e
		SelectBox valBox = new SelectBox();
		
		parameterContext.setType(valBox);
		// TODO role 추가
		// TODO instance 정보 추가
		
		ModalWindow modalWindow = new ModalWindow();
		modalWindow.setWidth(400);
		modalWindow.setHeight(400);
		modalWindow.setTitle("");
		modalWindow.setPanel(parameterContext);
		
		return modalWindow;
	}
	@ServiceMethod(callByContent=true)
	public void removeActivityVariable() throws Exception{
		if( selectedContext != null){
			ParameterContext[] parameterContexts = this.getParameterContext();
			for(int i=0; i < parameterContexts.length; i++){
				if( selectedContext == parameterContexts[i] ){
					parameterContexts[i] = null;
				}
			}
			
			// array null remove
			ArrayList<ParameterContext> removed = new ArrayList<ParameterContext>();
			for (ParameterContext val : parameterContexts){
				if (val != null){
					removed.add(val);
				}
			}
			this.setParameterContext(removed.toArray(new ParameterContext[0]));
		}
	}
	
}
