package org.uengine.webservice;

import java.io.*;
import java.util.ArrayList;

import org.metaworks.MetaworksException;
import org.metaworks.Refresh;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.Popup;
import org.uengine.contexts.MappingContext;
import org.uengine.kernel.Activity;
import org.uengine.kernel.RestWebServiceActivity;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("resources")
public class WebServiceDefinition implements Cloneable ,Serializable {
	
	@XStreamOmitField
	transient String id;
		@Id
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
	@XStreamAsAttribute
	String base;
		public String getBase() {
			return base;
		}
		public void setBase(String base) {
			this.base = base;
		}
		
	String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	ArrayList<ResourceProperty> resourceList;
		public ArrayList<ResourceProperty> getResourceList() {
			return resourceList;
		}
		public void setResourceList(ArrayList<ResourceProperty> resourceList) {
			this.resourceList = resourceList;
		}
		
	@XStreamOmitField
	Activity parentActivity;
		@Hidden
		public Activity getParentActivity() {
			return parentActivity;
		}
		public void setParentActivity(Activity parentActivity) {
			this.parentActivity = parentActivity;
		}
		
	@XStreamOmitField
	MethodProperty targetMethod;
		@Hidden
		public MethodProperty getTargetMethod() {
			return targetMethod;
		}
		public void setTargetMethod(MethodProperty targetMethod) {
			this.targetMethod = targetMethod;
		}
		
	public WebServiceDefinition(){
		resourceList = new ArrayList<ResourceProperty>();
	}
	
	public WebServiceDefinition loadWithPath(String filePath){
		WebServiceDefinition webDefinition = null;
		FileInputStream fin = null;
		File file = new File(filePath);
		if(file.exists()){
			try {
				fin = new FileInputStream(filePath);
				webDefinition = loadWithInputstream(fin);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}finally{
				if( fin != null ){
					try { fin.close(); } catch (IOException e) { e.printStackTrace(); }
					fin = null;
				}
			}
		}
		return webDefinition;
	}
	
	public WebServiceDefinition loadWithInputstream(InputStream stream){
		WebServiceDefinition webDefinition = null;
		try{
			XStream xstream = new XStream();
			xstream.alias("resources", WebServiceDefinition.class);
			xstream.alias("resource", ResourceProperty.class);
			xstream.alias("method", MethodProperty.class);
			xstream.alias("param", ParameterProperty.class);
			xstream.autodetectAnnotations(true);
			webDefinition = (WebServiceDefinition)xstream.fromXML( stream );
		}catch(Exception e){
			System.err.println(new MetaworksException("파일이 없거나 온전하지 않습니다."));
		}
		return webDefinition;
	}
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_STICK)
	public Object openPicker() throws Exception{
//
//		Popup popup = new Popup();
//
//		WebServiceDefinition webServiceDefinition = new WebServiceDefinition();
//		webServiceDefinition.setId("dummy");
//		webServiceDefinition.setBase(this.getBase());
//		webServiceDefinition.setName(this.getName());
//		webServiceDefinition.setResourceList(this.getResourceList());
//
//		WSDPickerPanel wsdPickerPanel = new WSDPickerPanel();
//		wsdPickerPanel.setWebServiceDefinition(webServiceDefinition);
//		wsdPickerPanel.init();
//
//		popup.setPanel(wsdPickerPanel);
//		popup.setName("앱 선택");
//
//		return popup;
		return null;
		
	}
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object changeMappingContext() throws Exception{
		Activity activity = getParentActivity();
		if( activity instanceof RestWebServiceActivity){
			if( targetMethod.getResponseClass() != null && !"void".equalsIgnoreCase(targetMethod.getResponseClass())){
				((RestWebServiceActivity) activity).setMappingContextOut(new MappingContext());
			}else{
				((RestWebServiceActivity) activity).setMappingContextOut(null);
			}
			
			((RestWebServiceActivity) activity).setMethod(getTargetMethod());
			((RestWebServiceActivity) activity).drawInit();
		}
		
		return new Refresh(activity, true);
		
	}
	
	public void injectionPathInfo(ResourceProperty resourceProperty, String basePath){
		ArrayList<MethodProperty> mpList = resourceProperty.getMethods();
		for(MethodProperty mp : mpList){
			mp.setBasePath(basePath);
			mp.setCallPath(resourceProperty.getPath());
		}

		ArrayList<ResourceProperty> childRpList = resourceProperty.getChildResources();
		for(ResourceProperty childRp : childRpList){
			injectionPathInfo(childRp, basePath);
		}

	}
}
