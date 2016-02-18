package org.uengine.kernel;

import java.io.Serializable;
import java.util.ArrayList;

import org.metaworks.ContextAware;
import org.metaworks.FieldDescriptor;
import org.metaworks.MetaworksContext;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.Type;
import org.metaworks.annotation.*;
import org.metaworks.component.SelectBox;
import org.metaworks.inputter.RadioInput;
import org.metaworks.widget.ModalWindow;
import org.uengine.contexts.TextContext;
import org.uengine.kernel.bpmn.face.ProcessVariableSelectorFace;
import org.uengine.processdesigner.mapper.TransformerMapping;

/**
 * @author Jinyoung Jang
 */
public class ParameterContext implements Serializable , ContextAware{
	
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	public static final String DIRECTION_IN = "in".intern();
	public static final String DIRECTION_OUT = "out".intern();
	public static final String DIRECTION_INOUT = "in-out".intern();
	private boolean multipleInput;


	public ParameterContext(){
		this.setMetaworksContext(new MetaworksContext());
	}
	transient MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	
	TextContext argument = org.uengine.contexts.TextContext.createInstance();
		public TextContext getArgument() {
			if(argument.getText()==null && getVariable()!=null){
				return getVariable().getDisplayName();
			}
			
			return argument;
		}
		public void setArgument(TextContext string) {
			argument = string;
		}

	ProcessVariable variable;
	@Face(faceClass = ProcessVariableSelectorFace.class)
		public ProcessVariable getVariable() {
			return variable;
		}
		public void setVariable(ProcessVariable variable) {
			this.variable = variable;
		}

	transient Object type;
	@Hidden
		public Object getType() {
			return type;
		}
		public void setType(Object name) {
			type = name;
		}
	
	String direction;
	@Range(options={"IN-OUT", "IN", "OUT", }, values={ "in-out", "in", "out",})
		public String getDirection() {
			return direction;
		}
		public void setDirection(String i) {
			direction = i;
		}
		
	TransformerMapping transformerMapping;
	@Hidden
		public TransformerMapping getTransformerMapping() {
			return transformerMapping;
		}
		public void setTransformerMapping(TransformerMapping transformerMapping) {
			this.transformerMapping = transformerMapping;
		}

	public boolean isMultipleInput() {
		return multipleInput;
	}

	public void setMultipleInput(boolean multipleInput) {
		this.multipleInput = multipleInput;
	}
}
