package org.uengine.contexts;

import java.io.Serializable;

import org.metaworks.annotation.Id;
import org.metaworks.component.Tree;
import org.metaworks.component.TreeNode;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.kernel.Activity;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ParameterContext;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.designer.MappingCanvas;
import org.uengine.webservice.PoolMappingTree;


public class MappingContext implements Serializable{

	public MappingContext(){}

	public MappingContext(Activity activity, ProcessInstance instance){

		MappingTree leftTree;
		MappingTree rightTree;

		leftTree = new MappingTree();
		leftTree.setId(TreeNode.ALIGN_LEFT);
		leftTree.setAlign(TreeNode.ALIGN_LEFT);

		MetaworksRemoteService.autowire(leftTree);
//		try {
//			leftTree.init();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		rightTree = new MappingTree();
		rightTree.setId(TreeNode.ALIGN_RIGHT);
		rightTree.setAlign(TreeNode.ALIGN_RIGHT);

		MetaworksRemoteService.autowire(rightTree);
//
//		try {
//			rightTree.init();
//		}catch (Exception e){
//			e.printStackTrace();
//		}

		MappingCanvas canvas = new MappingCanvas();
		canvas.setCanvasId("mappingCanvas");
		canvas.setLeftTreeId(leftTree.getId());
		canvas.setRightTreeId(rightTree.getId());
		setMappingCanvas(canvas);

		setMappingTreeLeft(leftTree);
		setMappingTreeRight(rightTree);


	}


	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

	ParameterContext[] mappingElements;

	public ParameterContext[] getMappingElements() {
		return mappingElements;
	}

	public void setMappingElements(ParameterContext[] mappingElements) {
		this.mappingElements = mappingElements;
	}


	String id;
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	transient MappingTree mappingTreeLeft;
		public MappingTree getMappingTreeLeft() {
			return mappingTreeLeft;
		}

		public void setMappingTreeLeft(MappingTree mappingTreeLeft) {
			this.mappingTreeLeft = mappingTreeLeft;
		}

	transient MappingTree mappingTreeRight;
		public MappingTree getMappingTreeRight() {
			return mappingTreeRight;
		}

		public void setMappingTreeRight(MappingTree mappingTreeRight) {
			this.mappingTreeRight = mappingTreeRight;
		}

	MappingCanvas mappingCanvas;
	public MappingCanvas getMappingCanvas() {
		return mappingCanvas;
	}
	public void setMappingCanvas(MappingCanvas mappingCanvas) {
		this.mappingCanvas = mappingCanvas;
	}

}