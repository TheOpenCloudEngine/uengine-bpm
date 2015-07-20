package org.uengine.modeling.modeler.condition;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Id;

import java.util.ArrayList;

@Face(ejsPath="dwr/metaworks/org/metaworks/component/Tree.ejs")
public class ConditionTree{
	public final static String ALIGN_LEFT			= "left";
	public final static String ALIGN_RIGHT			= "right";
	
	String id;
		@Id
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

	String align;
		public String getAlign() {
			return align;
		}
		public void setAlign(String align) {
			this.align = align;
		}

	boolean showCheckBox;
		public boolean isShowCheckBox() {
			return showCheckBox;
		}
		public void setShowCheckBox(boolean showCheckBox) {
			this.showCheckBox = showCheckBox;
		}
		
	boolean hiddenCheckBoxFolder;
		public boolean isHiddenCheckBoxFolder() {
			return hiddenCheckBoxFolder;
		}
		public void setHiddenCheckBoxFolder(boolean hiddenCheckBoxFolder) {
			this.hiddenCheckBoxFolder = hiddenCheckBoxFolder;
		}

	ConditionTreeNode node;
		public ConditionTreeNode getNode() {
			return node;
		}
		public void setNode(ConditionTreeNode node) {
			this.node = node;
		}
	
	ArrayList<ConditionTreeNode> checkNodes;
		public ArrayList<ConditionTreeNode> getCheckNodes() {
			return checkNodes;
		}
		public void setCheckNodes(ArrayList<ConditionTreeNode> checkNodes) {
			this.checkNodes = checkNodes;
		}
		
	ConditionTreeNode selectNode;
		public ConditionTreeNode getSelectNode() {
			return selectNode;
		}
		public void setSelectNode(ConditionTreeNode selectNode) {
			this.selectNode = selectNode;
		}
		
	public ConditionTree() {
		this.setAlign(ALIGN_LEFT);		
	}
}
