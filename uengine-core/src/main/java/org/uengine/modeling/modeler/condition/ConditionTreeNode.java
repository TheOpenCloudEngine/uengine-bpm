package org.uengine.modeling.modeler.condition;

import org.metaworks.*;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Face;
import org.metaworks.component.TreeNode;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.Role;

import java.util.ArrayList;
import java.util.Date;

@Face(ejsPath="dwr/metaworks/org/metaworks/component/TreeNode.ejs")
public class ConditionTreeNode  implements ContextAware{
	
	public final static String CONDITION_AND				= "And";
	public final static String CONDITION_OR				= "Or";
	public final static String CONDITION_OTHERWISE	= "Otherwise";
	public final static String CONDITION_EXPRESSION	= "Expression";
	public final static String CONDITION_DEFAULT_EXPRESSION	= "make expression and save";
	
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	String id;
		@Id
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
	String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	String type;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	
	String parentId;
		public String getParentId() {
			return parentId;
		}
		public void setParentId(String parentId) {
			this.parentId = parentId;
		}
		
	String path;
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}

	boolean root;
		public boolean isRoot() {
			return root;
		}
		public void setRoot(boolean root) {
			this.root = root;
		}
		
	boolean expanded;
		public boolean isExpanded() {
			return expanded;
		}
		public void setExpanded(boolean expanded) {
			this.expanded = expanded;
		}

	boolean selected;
		public boolean isSelected() {
			return selected;
		}
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		
	boolean loaded;
		public boolean isLoaded() {
			return loaded;
		}
		public void setLoaded(boolean loaded) {
			this.loaded = loaded;
		}

	boolean folder;
		public boolean isFolder() {
			return folder;
		}
		public void setFolder(boolean folder) {
			this.folder = folder;
		}
		
	ConditionTreeNode parentNode;
		public ConditionTreeNode getParentNode() {
			return parentNode;
		}
		public void setParentNode(ConditionTreeNode parentNode) {
			this.parentNode = parentNode;
		}
		
	ArrayList<ConditionTreeNode> child;
		public ArrayList<ConditionTreeNode> getChild() {
			return child;
		}
		public void setChild(ArrayList<ConditionTreeNode> child) {
			this.child = child;
		}
		
	String expression;
		public String getExpression() {
			return expression;
		}
		public void setExpression(String expression) {
			this.expression = expression;
		}
	String conditionType;
		public String getConditionType() {
			return conditionType;
		}
		public void setConditionType(String conditionType) {
			this.conditionType = conditionType;
		}
	ConditionNode conditionNode;
		public ConditionNode getConditionNode() {
			return conditionNode;
		}
		public void setConditionNode(ConditionNode conditionNode) {
			this.conditionNode = conditionNode;
		}
	public ArrayList<Role>	 roleList;
		public ArrayList<Role> getRoleList() {
			return roleList;
		}
		public void setRoleList(ArrayList<Role> roleList) {
			this.roleList = roleList;
		}
	public ArrayList<ProcessVariable> variableList;
		public ArrayList<ProcessVariable> getVariableList() {
			return variableList;
		}
		public void setVariableList(ArrayList<ProcessVariable> variableList) {
			this.variableList = variableList;
		}
	public ConditionTreeNode() {
		this.setMetaworksContext(new MetaworksContext());
		ArrayList<ConditionTreeNode> child = new ArrayList<ConditionTreeNode>();
		
		this.setChild(child);
		this.setLoaded(true);
	}
	public void add(ConditionTreeNode node) {
		node.setParentId(this.getId());
		this.getChild().add(node);
	}
		
	public void conditionInit() throws Exception{
		ConditionNode conditionNode = new ConditionNode();
		conditionNode.setRoleList(getRoleList());
		conditionNode.setVariableList(getVariableList());
		conditionNode.init();
		
		setConditionNode(conditionNode);
	}
	
	public ConditionTreeNode makeConditionNode() throws Exception{
		ConditionTreeNode node = new ConditionTreeNode();
		Long idByTime = new Date().getTime();
		node.setId(idByTime.toString());
		node.setParentNode(this);
		node.setParentId(this.getId());
		node.setVariableList(this.getVariableList());
		node.setRoleList(this.getRoleList());
		node.setLoaded(true);
		node.setType("page_white_text");	// TODO 아이콘 관련이기때문에.. 추후 변경
		return node;
	}
	
	@ServiceMethod(inContextMenu=true, callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	@Available(how={"folder","root"})
	public Object newAND() throws Exception{
		ConditionTreeNode node = this.makeConditionNode();
		node.setName(CONDITION_AND);
		node.setConditionType(CONDITION_AND);
		node.setFolder(true);
		node.setExpanded(true);
		node.setType(TreeNode.TYPE_FOLDER);
		node.getMetaworksContext().setHow("folder");
		return new ToAppend(this , node);
	}
	
	@ServiceMethod(inContextMenu=true, callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	@Available(how={"folder","root"})
	public Object newOR() throws Exception{
		ConditionTreeNode node = this.makeConditionNode();
		node.setName(CONDITION_OR);
		node.setConditionType(CONDITION_OR);
		node.setFolder(true);
		node.setExpanded(true);
		node.setType(TreeNode.TYPE_FOLDER);
		node.getMetaworksContext().setHow("folder");
		return new ToAppend(this , node);
	}
	
	@ServiceMethod(inContextMenu=true, callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	@Available(how={"root"})
	public Object newOtherwise() throws Exception{
		ConditionTreeNode node = this.makeConditionNode();
		node.setName(CONDITION_OTHERWISE);
		node.setConditionType(CONDITION_OTHERWISE);
		node.setType("page_white_text");
		return new ToAppend(this , node);
	}
	
	@ServiceMethod(inContextMenu=true, callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	@Available(how={"folder","root"})
	public Object[] newExpression() throws Exception{
		if( this.getConditionType() == null ){
			return null;
		}else{
			ConditionTreeNode node = this.makeConditionNode();
			node.setName(CONDITION_DEFAULT_EXPRESSION);
			node.setConditionType(CONDITION_EXPRESSION);
			node.conditionInit();
			node.getConditionNode().setConditionType(this.getConditionType());
			node.getConditionNode().setParentTreeNode(node);
			return new Object[]{new ToAppend(this , node) , new Refresh(node.select())};
		}
	}
	
	@ServiceMethod(inContextMenu=true, payload={"id","parentNode"}, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] delete() throws Exception{
		ConditionTreeNode parentNode = this.getParentNode();
		parentNode.setSelected(true);
		
		ConditionExPressionPanel conditionExPressionPanel = new ConditionExPressionPanel();
		ConditionTreeNodeView conditionTreeNode = conditionExPressionPanel.getConditionTreeNode();
		conditionTreeNode.setParentId(parentNode.getId());
		conditionTreeNode.setParentNode(parentNode);
		conditionTreeNode.getConditionNode().setParentTreeNode(parentNode);
		conditionTreeNode.getConditionNode().setRoleList(parentNode.getRoleList());
		conditionTreeNode.getConditionNode().setVariableList(parentNode.getVariableList());
		conditionTreeNode.getConditionNode().getMetaworksContext().setHow("folder");

		return new Object[]{new Remover(this, true), new Refresh(conditionExPressionPanel)};
	}

	@ServiceMethod(callByContent = true , target=ServiceMethodContext.TARGET_AUTO)
	public Object select() throws Exception {
		ConditionExPressionPanel conditionExPressionPanel = new ConditionExPressionPanel();
		ConditionTreeNodeView conditionTreeNode = conditionExPressionPanel.getConditionTreeNode();
		if( this.folder ){
			conditionTreeNode.setParentId(this.getId());
			conditionTreeNode.setParentNode(this);
			conditionTreeNode.getConditionNode().setParentTreeNode(this);
			conditionTreeNode.getConditionNode().setRoleList(getRoleList());
			conditionTreeNode.getConditionNode().setVariableList(getVariableList());
			conditionTreeNode.getConditionNode().getMetaworksContext().setHow("folder");
			
			return conditionExPressionPanel;
		}else if( CONDITION_OTHERWISE.equals(this.getConditionType())){
			return conditionExPressionPanel;
		}else{
			conditionTreeNode.setId("expression");
			conditionTreeNode.setRoleList(getRoleList());
			conditionTreeNode.setVariableList(getVariableList());
			conditionTreeNode.setConditionNode(getConditionNode());
			conditionTreeNode.getConditionNode().getMetaworksContext().setHow("expression");
			conditionExPressionPanel.setConditionTreeNode(conditionTreeNode);
			
			return conditionExPressionPanel;
		}
	}
	
	@ServiceMethod(callByContent = true , target=ServiceMethodContext.TARGET_AUTO)
	public Object expand() throws Exception { 
		return null;
	}
	
	@ServiceMethod(callByContent = true, target=ServiceMethodContext.TARGET_APPEND)
	public Object action() throws Exception {
		return null;
	}	
}
