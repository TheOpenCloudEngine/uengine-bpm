package org.uengine.kernel;

import java.io.Serializable;

import org.metaworks.FieldDescriptor;
import org.metaworks.ObjectType;
import org.metaworks.Type;
import org.metaworks.annotation.Hidden;
import org.metaworks.inputter.RadioInput;
import org.uengine.contexts.TextContext;
import org.uengine.kernel.GlobalContext;

/**
 * @author Jinyoung Jang
 */
public class SubProcessParameterContext extends ParameterContext{
	
	
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	boolean split;
		public boolean isSplit() {
			return split;
		}
		public void setSplit(boolean split) {
			this.split = split;
		}


	@Override
	@Hidden
	public boolean isMultipleInput() {
		return super.isMultipleInput();
	}
}
