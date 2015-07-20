package org.uengine.processdesigner;

import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.uengine.contexts.TextContext;
import org.uengine.kernel.Activity;
import org.uengine.kernel.ParameterContextPanel;
import org.uengine.kernel.ReceiveActivity;
import org.uengine.webservice.ApplyProperties;


public class ActivityWindow  {

	String id;
		@Hidden
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
	
	ActivityPanel activityPanel;
		public ActivityPanel getActivityPanel() {
			return activityPanel;
		}
		public void setActivityPanel(ActivityPanel activityPanel) {
			this.activityPanel = activityPanel;
		}

	public ActivityWindow(){
		activityPanel = new ActivityPanel();
	}
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] apply(){
		Activity activity = activityPanel.getActivity();
		if ( activity.getDescription() == null || "".equals(activity.getDescription())){
			TextContext desc = new TextContext();
			desc.setText(activity.getName());
			activity.setDescription(desc);
		}
//		Documentation document = activityPanel.getDocument();
//		if( document != null ){
//			MetaworksFile file1 = document.getAttachfile1();
//			if (file1 != null){
//				if (file1.getFileTransfer() != null
//						&& file1.getFileTransfer().getFilename() != null
//						&& !"".equals(file1.getFileTransfer().getFilename()) ){
//					try {
//						file1.upload();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}else{
//					file1.setFileTransfer(null);
//				}
//			}
//			MetaworksFile file2 = document.getAttachfile2();
//			if (file2 != null){
//				if (file2.getFileTransfer() != null
//						&& file2.getFileTransfer().getFilename() != null
//						&& !"".equals(file2.getFileTransfer().getFilename()) ){
//					try {
//						file2.upload();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}else{
//					file2.setFileTransfer(null);
//				}
//			}
//			MetaworksFile file3 = document.getAttachfile3();
//			if (file3 != null){
//				if (file3.getFileTransfer() != null
//						&& file3.getFileTransfer().getFilename() != null
//						&& !"".equals(file3.getFileTransfer().getFilename()) ){
//					try {
//						file3.upload();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}else{
//					file3.setFileTransfer(null);
//				}
//			}
//			activity.setDocumentation(document);
//		}
		ParameterContextPanel parameterContextPanel = activityPanel.getParameterContextPanel();
		if( parameterContextPanel != null ){
			((ReceiveActivity)activity).setParameters(parameterContextPanel.getParameterContext());
		}
//		if( activity instanceof SubProcessActivity ){
//			SubProcessContext subProcessContext = ((SubProcessActivity)activity).getSubProcessContext();
//			if( subProcessContext != null && subProcessContext.getMappingCanvas() != null ){
//				ParameterContext[] params = subProcessContext.getMappingCanvas().getMappingElements();
//				for (int i = 0; i < params.length; i++) {
//					ParameterContext param = params[i];
//					// TODO something
//				}
//			}
//		}
		ModalWindow modalWindow = new ModalWindow();
		modalWindow.setId(getId());
		return new Object[]{new ApplyProperties( this.getId(), activity), new Remover(modalWindow, true)};
	}
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] cancel(){
		return new Object[]{new Remover(new ModalWindow())};
		
	}
}
