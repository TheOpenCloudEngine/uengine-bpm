package org.uengine.kernel.bpmn;

import org.uengine.contexts.MappingContext;
//import org.uengine.kernel.designer.ui.PoolTransitionView;

public class PoolSequenceFlow extends SequenceFlow implements java.io.Serializable {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

//	PoolTransitionView poolTransitionView;
//		public PoolTransitionView getPoolTransitionView() {
//			return poolTransitionView;
//		}
//		public void setPoolTransitionView(PoolTransitionView poolTransitionView) {
//			this.poolTransitionView = poolTransitionView;
//		}
	MappingContext mappingContext;
		public MappingContext getMappingContext() {
			return mappingContext;
		}
		public void setMappingContext(MappingContext mappingContext) {
			this.mappingContext = mappingContext;
		}
}
