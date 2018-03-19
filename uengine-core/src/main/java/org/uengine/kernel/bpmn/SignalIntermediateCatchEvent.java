package org.uengine.kernel;

import com.jayway.jsonpath.JsonPath;
import org.codehaus.jackson.JsonNode;
import org.uengine.kernel.bpmn.Event;
import org.uengine.processdesigner.mapper.TransformerMapping;
import org.uengine.uml.model.ObjectInstance;

import java.io.Serializable;
import java.util.HashMap;

public class CatchingMessageEvent extends Event implements MessageListener {

	public static final String DIRECT_VALUE = "[direct].value";

	public CatchingMessageEvent(){
		super();
		if( this.getName() == null ){
			setName(this.getClass().getSimpleName());
		}
	}

	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
		//start listens...

		System.out.print("inside " + getClass().getName());
	}

	ProcessVariable dataOutput;
		public ProcessVariable getDataOutput() {
			return dataOutput;
		}
		public void setDataOutput(ProcessVariable dataOutput) {
			this.dataOutput = dataOutput;
		}


	ParameterContext[] dataOutputMapping;
		public ParameterContext[] getDataOutputMapping() {
			return dataOutputMapping;
		}
		public void setDataOutputMapping(ParameterContext[] dataOutputMapping) {
			this.dataOutputMapping = dataOutputMapping;
		}


	public boolean onMessage(ProcessInstance instance, Object payload) throws Exception {

		if(getDataOutput()!=null)
			getDataOutput().set(instance, "", (Serializable) payload);

		if (getDataOutputMapping()!=null && payload instanceof BeanPropertyResolver) {

			BeanPropertyResolver payload_ = (BeanPropertyResolver) payload;

			for (ParameterContext parameterContext : getDataOutputMapping()) {
				Object value = payload_.getBeanProperty(
						parameterContext.getArgument().getText()
				);

				HashMap options = new HashMap<>();

				if(parameterContext.getTransformerMapping()!=null && parameterContext.getTransformerMapping().getTransformer()!=null){
					TransformerMapping transformerMapping = parameterContext.getTransformerMapping();
					if(transformerMapping.getTransformer().getArgumentSourceMap()==null || transformerMapping.getTransformer().getArgumentSourceMap().size() < transformerMapping.getTransformer().getInputArguments().length){
						transformerMapping.getTransformer().setArgumentSourceMap(new HashMap());
						options.put(DIRECT_VALUE, value);
						transformerMapping.getTransformer().getArgumentSourceMap().put(transformerMapping.getTransformer().getInputArguments()[0], DIRECT_VALUE);
					}

					value = transformerMapping.getTransformer().letTransform(instance, options);
				}


				if(parameterContext.getVariable()==null)
					throw new UEngineException("Mapping variable is not set");

				parameterContext.getVariable().set(instance, "", (Serializable) value!=null ? value.toString(): null);
			}
		}

		fireComplete(instance);
		return true;
	}

	public String getMessage() {
		return this.getName();
	}
}
