package org.uengine.contexts;

import java.util.ArrayList;

import org.metaworks.component.TreeNode;

public class InstanceTreeNode extends TreeNode {
	
	public ArrayList<String> instanceVariableList() {
		ArrayList<String> instVariableList = new ArrayList<String>();
		instVariableList.add("instanceId");
		instVariableList.add("name");
		instVariableList.add("status");
		instVariableList.add("info");
		instVariableList.add("dueDate");
//		instVariableList.add("mainProcessInstanceId");
//		instVariableList.add("mainActivityTracingTag");
//		instVariableList.add("rootProcessInstanceId");
//		instVariableList.add("dummy1");
//		instVariableList.add("");
//		instVariableList.add("");
		
		return instVariableList;
	}
	
	public void load(){
		this.setName("instance");
		this.setLoaded(true);
		this.setExpanded(true);
		ArrayList<String> instVariableList = this.instanceVariableList();
		for(int i = 0; i < instVariableList.size(); i++){
			String instanceVariable = instVariableList.get(i);
			InstanceTreeNode node = new InstanceTreeNode();
			node.setId("[instance]-" + instanceVariable);
			node.setName(instanceVariable);
			node.setParentId(this.getId());
			node.setAlign(this.getAlign());
			node.setType(TreeNode.TYPE_FILE_HTML);
			
			this.add(node);
		}
	}
}
