package org.uengine.kernel.designer;

import org.metaworks.annotation.Id;
import org.uengine.kernel.ParameterContext;

import java.io.Serializable;

public class MappingCanvas implements Serializable {
	
	String canvasId;
	@Id
		public String getCanvasId() {
			return canvasId;
		}
		public void setCanvasId(String canvasId) {
			this.canvasId = canvasId;
		}
	String leftTreeId;
		public String getLeftTreeId() {
			return leftTreeId;
		}
		public void setLeftTreeId(String leftTreeId) {
			this.leftTreeId = leftTreeId;
		}
	String rightTreeId;
		public String getRightTreeId() {
			return rightTreeId;
		}
		public void setRightTreeId(String rightTreeId) {
			this.rightTreeId = rightTreeId;
		}
	boolean inout;
		public boolean isInout() {
			return inout;
		}
		public void setInout(boolean inout) {
			this.inout = inout;
		}
	ParameterContext[] mappingElements;
		public ParameterContext[] getMappingElements() {
			return mappingElements;
		}

		public void setMappingElements(ParameterContext[] mappingElements) {
			this.mappingElements = mappingElements;
		}
}
