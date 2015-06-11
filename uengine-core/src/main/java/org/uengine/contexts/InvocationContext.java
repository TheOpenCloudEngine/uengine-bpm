package org.uengine.contexts;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Id;
//import org.uengine.codi.mw3.webProcessDesigner.ClassResourceTree;
//import org.uengine.codi.mw3.webProcessDesigner.MappingCanvas;
//import org.uengine.codi.mw3.webProcessDesigner.MappingTree;

import java.io.Serializable;

public class InvocationContext implements ContextAware ,Serializable{
	
	transient MetaworksContext metaworksContext;
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
		
	transient MappingTree mappingTree;
		public MappingTree getMappingTree() {
			return mappingTree;
		}
		public void setMappingTree(MappingTree mappingTree) {
			this.mappingTree = mappingTree;
		}
		
//	MappingCanvas mappingCanvas;
//		public MappingCanvas getMappingCanvas() {
//			return mappingCanvas;
//		}
//		public void setMappingCanvas(MappingCanvas mappingCanvas) {
//			this.mappingCanvas = mappingCanvas;
//		}

//	transient ClassResourceTree classResourceTree;
//		public ClassResourceTree getClassResourceTree() {
//			return classResourceTree;
//		}
//		public void setClassResourceTree(ClassResourceTree classResourceTree) {
//			this.classResourceTree = classResourceTree;
//		}
	
//	public void loadCanvas() throws Exception{
//		mappingCanvas = new MappingCanvas();
//		if( this.id != null ){
//			mappingCanvas.setCanvasId( "canvas" + id);
//		}
//	}
//	public void loadTree() throws Exception{
//		mappingTree = new MappingTree();
//		classResourceTree = new ClassResourceTree();
//	}
//	public void treeSetId(String id){
//		mappingTree.setId( "mapping" + id);
//		classResourceTree.setId( "classResource" + id);
//	}
	
}
