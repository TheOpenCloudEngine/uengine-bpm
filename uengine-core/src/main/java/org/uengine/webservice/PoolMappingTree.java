package org.uengine.webservice;

import java.util.Iterator;

import net.sf.json.JSONObject;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.component.TreeNode;
import org.uengine.contexts.MappingTree;
import org.uengine.kernel.Activity;
import org.uengine.kernel.RestWebServiceActivity;
import org.uengine.webservice.MethodProperty;
import org.uengine.webservice.ParameterProperty;

public class PoolMappingTree extends MappingTree {
	
	Activity activity;
		public Activity getActivity() {
			return activity;
		}
		public void setActivity(Activity activity) {
			this.activity = activity;
		}
		
	String inOut;
		public String getInOut() {
			return inOut;
		}
		public void setInOut(String inOut) {
			this.inOut = inOut;
		}
		
	@Override
	@ServiceMethod(payload={"id", "align", "parentEditorId", "activity", "inOut"}, target=ServiceMethodContext.TARGET_SELF)
	public void init() throws Exception{
		RestWebServiceActivity activity = (RestWebServiceActivity)this.getActivity();
		TreeNode rootnode = new TreeNode();
		rootnode.setRoot(true);
		
		if( MappingTree.MAPPING_OUT.equals(inOut)){
			
			rootnode.setId(activity.getName() + "OutRoot");
			rootnode.setName(activity.getDescription() != null && activity.getDescription() != null ? activity.getDescription() : activity.getName() );
			rootnode.setType(TreeNode.TYPE_FOLDER);
			rootnode.setLoaded(true);
			rootnode.setFolder(true);
			rootnode.setExpanded(true);
			rootnode.setAlign(TreeNode.ALIGN_LEFT);
					
			MethodProperty mp = activity.getMethod();
			if( mp != null && mp.getId() != null ){
				TreeNode mpNode = new TreeNode();
				String nodeName = mp.getId();
				if( mp.getProduces() != null && mp.getProduces().size()>0){
					String changeRootNodeName = mp.getProduces().getString(0);
					if( mp.getProduces().size() > 1){
						for(int i=1; i < mp.getProduces().size(); i++){
							changeRootNodeName += "," + mp.getProduces().getString(i);
						}
					}
					rootnode.setName("["+changeRootNodeName+"]");
					
				}
				String responseClass = mp.getResponseClass();
				if( responseClass != null && responseClass.equalsIgnoreCase(String.class.getSimpleName())){
					
					mpNode = new TreeNode();
					mpNode.setId("String");
					mpNode.setName("String");
					mpNode.setLoaded(true);
					mpNode.setExpanded(true);
					mpNode.setType(TreeNode.TYPE_FILE_TEXT);
					mpNode.setAlign(TreeNode.ALIGN_LEFT);
					
					rootnode.add(mpNode);
					
				}else if(responseClass != null && !responseClass.equalsIgnoreCase("void")){
					if( mp.getModelProperty() != null && !"".equals(mp.getModelProperty())){
						JSONObject modelsApi = JSONObject.fromObject(mp.getModelProperty());
						Iterator iterator = modelsApi.keys();
						while(iterator.hasNext()){
							String propName = (String) iterator.next();
							mpNode = new TreeNode();
							mpNode.setId(propName);
							mpNode.setName(propName);
							mpNode.setLoaded(true);
							mpNode.setExpanded(true);
							mpNode.setType(TreeNode.TYPE_FILE_TEXT);
							mpNode.setAlign(TreeNode.ALIGN_LEFT);
							
							rootnode.add(mpNode);
						}
					}else{
						mpNode.setId(mp.getId() + "Out");
						mpNode.setName(nodeName);
						mpNode.setLoaded(true);
						mpNode.setExpanded(true);
						mpNode.setType(TreeNode.TYPE_FILE_TEXT);
						mpNode.setAlign(TreeNode.ALIGN_LEFT);
						
						rootnode.add(mpNode);
					}
				}
				
			}
		}else{
			
			rootnode.setId(activity.getName() + "Root");
			rootnode.setName(activity.getDescription() != null && activity.getDescription() != null ? activity.getDescription() : activity.getName() );
			rootnode.setType(TreeNode.TYPE_FOLDER);
			rootnode.setLoaded(true);
			rootnode.setFolder(true);
			rootnode.setExpanded(true);
			rootnode.setAlign(TreeNode.ALIGN_RIGHT);
			
			MethodProperty mp = activity.getMethod();
			if( mp != null && mp.getId() != null ){
				TreeNode mpNode = new TreeNode();
				mpNode.setId(mp.getId());
				mpNode.setName(mp.getId() );
				mpNode.setLoaded(true);
				mpNode.setFolder(true);
				mpNode.setExpanded(true);
				mpNode.setType(TreeNode.TYPE_FOLDER);
				mpNode.setAlign(TreeNode.ALIGN_RIGHT);
				
				if( mp.getRequest() != null ){
					ParameterProperty[] pp = mp.getRequest();
					for(int i=0; i < pp.length; i++){
						//TODO:hardcode
						if( pp[i].getDataType().startsWith("org") || pp[i].getDataType().startsWith("B")){
							if( mp.getModelProperty() != null && !"".equals(mp.getModelProperty())){
								JSONObject modelsApi = JSONObject.fromObject(mp.getModelProperty());
								Iterator iterator = modelsApi.keys();
								while(iterator.hasNext()){
									String propName = (String) iterator.next();
									TreeNode propNode = new TreeNode();
									propNode.setId(propName);
									propNode.setName(propName);
									propNode.setLoaded(true);
									propNode.setExpanded(true);
									propNode.setType(TreeNode.TYPE_FILE_TEXT);
									propNode.setAlign(TreeNode.ALIGN_RIGHT);
									
									mpNode.add(propNode);
								}
							}
						}else{
							TreeNode childNode = new TreeNode();
							childNode.setId(pp[i].getName());
							childNode.setName(pp[i].getName() );
							childNode.setLoaded(true);
							childNode.setExpanded(true);
							childNode.setType(TreeNode.TYPE_FILE_TEXT);
							childNode.setAlign(TreeNode.ALIGN_RIGHT);
							mpNode.add(childNode);
						}
					}
				}
				
				rootnode.add(mpNode);
			}
		}
		
		this.setNode(rootnode);
		
		setPreLoaded(true);
	}
}
