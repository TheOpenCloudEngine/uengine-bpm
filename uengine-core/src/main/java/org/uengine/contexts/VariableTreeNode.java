package org.uengine.contexts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.metaworks.*;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.component.Tree;
import org.metaworks.component.TreeNode;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.contexts.ComplexType;
import org.uengine.kernel.ProcessVariable;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.ResourceManager;
import org.uengine.uml.model.Attribute;
import org.uengine.uml.model.ClassDefinition;
import org.uengine.uml.model.ObjectInstance;

public class VariableTreeNode extends TreeNode {
	
	String className;
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}
	
	
	public void load(List<ProcessVariable> prcsValiableList) throws ClassNotFoundException{
		this.setName("variable");
		this.setLoaded(true);
		this.setExpanded(true);
		
		for(int i = 0; i < prcsValiableList.size(); i++){
			ProcessVariable processVariable = prcsValiableList.get(i);
			String nameAttr = processVariable.getName();
//			// TODO 처음에 로딩할 필요가 없다면 아래 루프 부분은 클릭시 작동하는걸로 뺀다.
//			Class type = Class.forName(processVariable.getTypeInputter());
//			if( type == null ) continue;
			
			VariableTreeNode node = new VariableTreeNode();
			node.setId(nameAttr);
			node.setTreeId(this.getTreeId());
			node.setName(nameAttr);
			node.setParentId(this.getName());
			node.setType(TreeNode.TYPE_FILE_HTML);
			node.setLoaded(true);
			node.setExpanded(true);
			node.setFolder(true);
			node.setAlign(this.getAlign());

			Class type = processVariable.getType();
			
			if( type == ComplexType.class){
				try {
					Object typeAttr = processVariable.getDefaultValue();

					// this is old version
					if(typeAttr!=null && typeAttr instanceof ComplexType){
						ComplexType complexType = (ComplexType)typeAttr;
						String typeIdAttr = complexType.getTypeId();
						if( typeIdAttr != null && !"".equals(typeIdAttr) ){
							String formName = typeIdAttr.substring(1, typeIdAttr.length() -1);
							node.setClassName(formName);
						}
						node.setChild(loadJavaClassProperties(node));
					}else
					// new version of class system
					if(processVariable.getTypeClassName()!=null &&
							(	processVariable.getTypeClassName().indexOf(".class") > 0
								|| processVariable.getTypeClassName().indexOf(".urlapp") > 0
							)
							){
						ResourceManager resourceManager = MetaworksRemoteService.getComponent(ResourceManager.class);
						ClassDefinition classDefinition = (ClassDefinition) resourceManager.getStorage().getObject(new DefaultResource(processVariable.getTypeClassName()));

						if(classDefinition!=null){
							node.setChild(loadClassProperties(node, classDefinition.metaworks2Type()));
						}
					}else
					if(processVariable.getTypeClassName()!=null){

						WebObjectType webObjectType = MetaworksRemoteService.getInstance().getMetaworksType(processVariable.getTypeClassName());
						if(webObjectType!=null){
							node.setChild(loadClassProperties(node, webObjectType.metaworks2Type()));
						}

					}else
					if (processVariable.getDefaultValue()!=null && processVariable.getDefaultValue() instanceof ObjectInstance){
						ObjectInstance objectInstance = (ObjectInstance) processVariable.getDefaultValue();
						ClassDefinition classDefinition = objectInstance.getClassDefinition();

						if(classDefinition!=null){
							node.setChild(loadClassProperties(node, classDefinition.metaworks2Type()));
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			this.add(node);
		}
	}


	private ArrayList<TreeNode> loadClassProperties(VariableTreeNode node, Type classDefinition) throws Exception{

		ArrayList<TreeNode> child = new ArrayList<TreeNode>();

		FieldDescriptor[] fields = classDefinition.getFieldDescriptors();
		for(FieldDescriptor attribute : fields){
			VariableTreeNode childNode = new VariableTreeNode();

			childNode.setId(node.getId() + "-" + attribute.getName());
			childNode.setTreeId(node.getTreeId());
			childNode.setName(attribute.getName());
			childNode.setParentId(node.getId());
			childNode.setAlign(node.getAlign());

			childNode.setFolder(false);
			childNode.setType(TreeNode.TYPE_FILE_TEXT);

			child.add(childNode);
		}

		return child;
	}


	private ArrayList<TreeNode> loadJavaClassProperties(VariableTreeNode node) throws Exception{
		ArrayList<TreeNode> child = new ArrayList<TreeNode>();
		WebObjectType wot = MetaworksRemoteService.getInstance().getMetaworksType( node.getClassName() ); 
		WebFieldDescriptor wfields[] = wot.getFieldDescriptors();
		FieldDescriptor fields[] = wot.metaworks2Type().getFieldDescriptors();
		for(int j=0; j<fields.length; j++){
			WebFieldDescriptor wfd = wfields[j];
			VariableTreeNode childNode = new VariableTreeNode();
			// 주의 : id에 "." 이 들어간다면 Tree 에서 Id검색을 할수가 없다. 그리하여 "-" 으로 데이터 셋팅함
			childNode.setId(node.getId() + "-" + wfd.getName());
			childNode.setTreeId(node.getTreeId());
			childNode.setName(wfd.getName());
			childNode.setParentId(node.getId());
			childNode.setAlign(node.getAlign());
			if( wfd.getClassName().startsWith("org.uengine.codi.mw3")){
				childNode.setFolder(true);
				childNode.setLoaded(false);
				childNode.setExpanded(false);
				childNode.setClassName(wfd.getClassName());
				childNode.setType(TreeNode.TYPE_FOLDER);
			}else{
				childNode.setFolder(false);
				childNode.setType(TreeNode.TYPE_FILE_TEXT);
			}
			child.add(childNode);
		}
		return child;
	}
	/*
	 * service method
	 */
	@ServiceMethod( callByContent=true ,target=ServiceMethodContext.TARGET_APPEND)
	public Object expand() throws Exception {
		if( this.getClassName() != null ){
			Tree parentTree = new Tree();
			parentTree.setId(this.getTreeId());
			//return new ToAppend( parentTree , this.loadClassProperties(this));
		}
		return null;
	}
}
