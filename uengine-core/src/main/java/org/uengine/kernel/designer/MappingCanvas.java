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

	String leftTreeJson;
		public String getLeftTreeJson() {
			return leftTreeJson;
		}
		public void setLeftTreeJson(String leftTreeJson) {
			this.leftTreeJson = leftTreeJson;
		}

	String rightTreeJson;
		public String getRightTreeJson() {
			return rightTreeJson;
		}
		public void setRightTreeJson(String rightTreeJson) {
			this.rightTreeJson = rightTreeJson;
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
