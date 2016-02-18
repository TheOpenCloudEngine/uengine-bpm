package org.uengine.modeling.modeler.condition;

import org.metaworks.*;
import org.metaworks.annotation.*;
import org.metaworks.annotation.Face;
import org.metaworks.component.TreeNode;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.Role;
import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.kernel.bpmn.face.RolePanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	protected List<Role> roleList;
        public List<Role> getRoleList() {
            return roleList;
        }
        public void setRoleList(List<Role> roleList) {
            this.roleList = roleList;
        }

    protected List<ProcessVariable> processVariableList;
        public List<ProcessVariable> getProcessVariableList() {
            return processVariableList;
        }
        public void setProcessVariableList(List<ProcessVariable> processVariableList) {
            this.processVariableList = processVariableList;
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
		conditionNode.setRoleList(this.getRoleList());
		conditionNode.setProcessVariableList(this.getProcessVariableList());
		conditionNode.init();
		
		setConditionNode(conditionNode);
	}
	
	public ConditionTreeNode makeConditionNode() throws Exception{
		ConditionTreeNode node = new ConditionTreeNode();
		Long idByTime = new Date().getTime();
		node.setId(idByTime.toString());
		node.setParentNode(this);
		node.setParentId(this.getId());
        node.setRoleList(this.getRoleList());
        node.setProcessVariableList(this.getProcessVariableList());
        node.setLoaded(true);
		node.setType("page_white_text");	// TODO 아이콘 관련이기때문에.. 추후 변경
		return node;
	}
	
	@ServiceMethod(inContextMenu=true, callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	@Available(how={"folder", "root"})
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
	@Available(how={"folder", "root"})
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
	
	/**
	 * 
	 * @param processVariablePanel
	 * @param rolePanel
	 */
	@ServiceMethod(inContextMenu=true, callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	@Available(how={"folder", "root"})
	public Object[] newExpression(@AutowiredFromClient ProcessVariablePanel processVariablePanel, @AutowiredFromClient RolePanel rolePanel) throws Exception{
		if( this.getConditionType() == null ){
			return null;
		}else{
            this.setProcessVariableList(processVariablePanel.getProcessVariableList());
            this.setRoleList(rolePanel.getRoleList());

			ConditionTreeNode node = this.makeConditionNode();
			node.setName(CONDITION_DEFAULT_EXPRESSION);
			node.setConditionType(CONDITION_EXPRESSION);
			node.conditionInit();
			node.getConditionNode().setConditionType(this.getConditionType());
			node.getConditionNode().setParentTreeNode(node);
			return new Object[]{new ToAppend(this , node) , new Refresh(node.select())};
		}
	}
	
	@ServiceMethod(inContextMenu=true, payload={"id", "parentNode"}, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] delete() throws Exception{
		ConditionTreeNode parentNode = this.getParentNode();
		parentNode.setSelected(true);
		
		ConditionEditorExtension conditionExPressionPanel = new ConditionEditorExtension();
		ConditionTreeNodeView conditionTreeNode = conditionExPressionPanel.getConditionTreeNode();
		conditionTreeNode.setParentId(parentNode.getId());
		conditionTreeNode.setParentNode(parentNode);
		conditionTreeNode.getConditionNode().setParentTreeNode(parentNode);
		conditionTreeNode.getConditionNode().setRoleList(parentNode.getRoleList());
		conditionTreeNode.getConditionNode().setProcessVariableList(parentNode.getProcessVariableList());
		conditionTreeNode.getConditionNode().getMetaworksContext().setHow("folder");

		return new Object[]{new Remover(this, true), new Refresh(conditionExPressionPanel)};
	}

	@ServiceMethod(callByContent = true , target=ServiceMethodContext.TARGET_AUTO)
	public Object select() throws Exception {
		ConditionEditorExtension conditionExPressionPanel = new ConditionEditorExtension();
		ConditionTreeNodeView conditionTreeNode = conditionExPressionPanel.getConditionTreeNode();

		if( this.folder ){
			conditionTreeNode.setParentId(this.getId());
			conditionTreeNode.setParentNode(this);
			conditionTreeNode.getConditionNode().setParentTreeNode(this);
			conditionTreeNode.getConditionNode().setRoleList(this.getRoleList());
			conditionTreeNode.getConditionNode().setProcessVariableList(this.getProcessVariableList());
			conditionTreeNode.getConditionNode().getMetaworksContext().setHow("folder");
			
			return conditionExPressionPanel;

		}else if( CONDITION_OTHERWISE.equals(this.getConditionType())){
			return conditionExPressionPanel;

		}else{
			conditionTreeNode.setId("expression");
			conditionTreeNode.setRoleList(this.getRoleList());
			conditionTreeNode.setProcessVariableList(this.getProcessVariableList());
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
