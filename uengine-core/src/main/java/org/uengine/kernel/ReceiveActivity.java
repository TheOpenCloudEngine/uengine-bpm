package org.uengine.kernel;

/**
 * @author Jinyoung Jang
 */

import java.io.Serializable;
import java.util.*;

import com.sun.org.apache.regexp.internal.RE;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Group;
import org.metaworks.annotation.Hidden;
import org.uengine.contexts.MappingContext;
import org.uengine.kernel.bpmn.face.ParameterContextArrayFace;
import org.uengine.kernel.bpmn.face.ParameterContextListFace;
import org.uengine.modeling.IModelingTimeSensitive;
import org.uengine.util.UEngineUtil;

import static org.metaworks.dwr.MetaworksRemoteService.autowire;


public class ReceiveActivity extends DefaultActivity implements MessageListener, NeedArrangementToSerialize, IModelingTimeSensitive {
    private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

    String message;

    @Hidden
    public String getMessage() {
        if (message == null && getMessageDefinition() != null)
            setMessage(getMessageDefinition().getName());

        return message;
    }

    public void setMessage(String value) {
        message = value;
    }

    MessageDefinition messageDefinition;

    @Hidden
    public MessageDefinition getMessageDefinition() {
        return messageDefinition;
    }

    public void setMessageDefinition(MessageDefinition definition) {
        if (definition == null) definition = new MessageDefinition();
        messageDefinition = definition;
        setMessage(definition.getName());
    }

    ParameterContext[] parameters; //TODO 이게 널일때 어떻게 할것인가>?????

    @Face(faceClass = ParameterContextArrayFace.class)
    @Group(name = "Data")
    public ParameterContext[] getParameters() {
        return parameters;
    }

    public void setParameters(ParameterContext[] contexts) {
        parameters = contexts;
    }

    Role fromRole;

    @Hidden
    public Role getFromRole() {
        return fromRole;
    }

    public void setFromRole(Role role) {
        fromRole = role;
    }

/////////////////////////

    public ReceiveActivity() {
        super("Receive");
    }


    protected void executeActivity(ProcessInstance instance) throws Exception {
        System.out.println("ReceiveActivity::waiting for message : " + message);
        getProcessDefinition().addMessageListener(instance, this); //subscribes to JMS topic
    }

    //TODO: hot-spot
    protected void onEvent(String command, ProcessInstance instance, Object payload) throws Exception {
        if (!isMyMessage(command, instance, payload)) {
            super.onEvent(command, instance, payload);
            return;
        }

        onReceive(instance, payload);
    }

    protected boolean isMyMessage(String message, ProcessInstance instance, Object payload) {
        return message.equals(Activity.PREPIX_MESSAGE + "_" + getMessage());
    }

    protected void onReceive(ProcessInstance instance, Object payload) throws Exception {

        instance.addDebugInfo(this);

        if (payload != null && (getParameters() != null)) {

            Vector payloads = null;
            ParameterContext[] paramCtxs = getParameters();

            if (payload instanceof Vector) {
                System.out.println("ReceiveActivity::payload is " + payload);
                payloads = (Vector) payload;

                //TODO: test this
                if (!testMyMessage(instance, payloads)) return;

                int i = 0;
                for (Iterator iter = payloads.iterator(); iter.hasNext(); i++) {
                    paramCtxs[i].getVariable().set(instance, "", (java.io.Serializable) iter.next());
                }
            } else if (payload instanceof ResultPayload) {
                savePayload(instance, (ResultPayload) payload);
            }

            //TODO: when user rollback this receive activity, listener should be added again.
            getProcessDefinition().removeMessageListener(getMessage(), instance, getTracingTag());
        }

        fireComplete(instance);
    }

    public void savePayload(ProcessInstance instance, ResultPayload resultPayload) throws Exception {


        KeyedParameter[] processVariableChanges = resultPayload.getProcessVariableChanges();
        if (processVariableChanges != null)
            for (int i = 0; i < processVariableChanges.length; i++) {
                String variableKey = processVariableChanges[i].getKey();
                Object variableValue = processVariableChanges[i].getValue();

                boolean saveVariableValue = true;

                if (variableValue instanceof CommandVariableValue) {
                    saveVariableValue = !((CommandVariableValue) variableValue).doCommand(instance, variableKey);
                }

                if (saveVariableValue)
                    instance.set("", processVariableChanges[i].getKey(), (Serializable) processVariableChanges[i].getValue());
            }
    }

    protected boolean testMyMessage(ProcessInstance instance, Vector payloads) throws Exception {
        if (fromRole == null) return true;

        ProcessVariable identifier = fromRole.getIdentifier();
        System.out.println("	test my role:: fromRole=" + fromRole);
        System.out.println("				:: identifier=" + identifier);

        if (identifier == null) return true;

        ParameterContext[] parameters = getParameters();
        Object identifierValue = identifier.get(instance, "");
        System.out.println("				:: value of identifier=" + identifierValue);

        for (int i = 0; i < parameters.length; i++) {
            if (identifier.equals(parameters[i].getVariable())) {
                Object paramValue = payloads.elementAt(i);
                System.out.println("				:: value of parameter=" + paramValue);
                if (identifierValue.equals(paramValue)) {
                    //when the filter met
                    return true;
                }
            }
        }

        return false;
    }

    public ValidationContext validate(Map options) {
        ValidationContext validationContext = super.validate(options);

//		if(getMessage()== null)
//			validationContext.addWarning(getActivityLabel()+" Message must be specified.");

        if (getParameters() != null) {
            ParameterContext[] parameters = getParameters();
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].getVariable() == null) {
                    validationContext.addWarning(getActivityLabel() + " All of the parameters must be bound with variable.");
                    break;
                }
            }
        }

        return validationContext;
    }

    public void fireReceived(ProcessInstance instance, Object payload) throws Exception {
        onReceive(instance, payload);
    }


    public boolean onMessage(ProcessInstance instance, Object payload) throws Exception {
        onEvent(Activity.PREPIX_MESSAGE + "_" + getMessage(), instance, payload);

        return true;
    }

    @Override
    public void beforeRegistered(ProcessInstance instance) throws Exception {

    }

    @Override
    public void afterRegistered(ProcessInstance instance) throws Exception {

    }

    @Override
    public void afterUnregistered(ProcessInstance instance) throws Exception {

    }


    @Override
    public void beforeSerialization() {

        if (getParameters() != null)
            for (ParameterContext parameterContext : getParameters()) {
                if (parameterContext.getVariable() != null) {
                    //parameterContext.getVariable().setName(parameterContext.getArgument().getText());
                    ProcessVariable realPV = getProcessDefinition().getProcessVariable(parameterContext.getVariable().getName());
                    parameterContext.setVariable(realPV);
                }
            }
    }

    @Override
    public void afterDeserialization() {

    }


    ///// implementation of ModelingTimeSensitive features. /////

    MappingContext mapper;

    //@Group(name="Mapping Out")
    @Hidden
    public MappingContext getMapper() {
        return mapper;
    }

    public void setMapper(MappingContext mapper) {
        this.mapper = mapper;
    }

    MappingContext mapperIn;

    @Group(name = "Mapping")
    public MappingContext getMapperIn() {
        return mapperIn;
    }

    public void setMapperIn(MappingContext mapperIn) {
        this.mapperIn = mapperIn;
    }


    ParameterContext[] mappingContexts;

    @Hidden
    public ParameterContext[] getMappingContexts() {
        return mappingContexts;
    }

    public void setMappingContexts(ParameterContext[] mappingContexts) {
        this.mappingContexts = mappingContexts;
    }

    ParameterContext[] mappingContextsIn;

    @Hidden
    public ParameterContext[] getMappingContextsIn() {
        return mappingContextsIn;
    }

    public void setMappingContextsIn(ParameterContext[] mappingContextsIn) {
        this.mappingContextsIn = mappingContextsIn;
    }


    protected void dataMapping(ProcessInstance instance, ParameterContext[] mappingContexts) throws Exception {

        if (mappingContexts == null) return;

        for (ParameterContext param : mappingContexts) {

            String srcVariableName = null;
            String targetFieldName = param.getArgument().getText();
            Object value = null;

            if (param.getVariable() == null && param.getTransformerMapping() != null) {
                value = param.getTransformerMapping().getTransformer().letTransform(instance, param.getTransformerMapping().getLinkedArgumentName());
            } else {
                srcVariableName = param.getVariable().getName();
                if (srcVariableName.startsWith("[activities]") || srcVariableName.startsWith("[instance]") || srcVariableName.startsWith("[roles]")) {
                    value = instance.getBeanProperty(srcVariableName); // varA
                } else {
                    String[] wholePartPath = srcVariableName.replace('.', '@').split("@");
                    // wholePartPath.length >= 1 이 되는 이유는 안쪽에 객체의 값을 참조하려고 하는 부분이기때문에 따로 값을 가져와야함
                    if (wholePartPath.length >= 2) {
                        String rootObjectName = wholePartPath[1];
                        if (wholePartPath.length > 2) {
                            for (int j = 2; j < wholePartPath.length; j++) {
                                rootObjectName += "." + wholePartPath[j];
                            }
                        }
                        // 이걸 바로 호출
                        Object rootObject = instance.getBeanProperty(wholePartPath[0]);
                        if (rootObject != null) {
                            value = UEngineUtil.getBeanProperty(rootObject, rootObjectName);
                        }
                    } else {
                        value = instance.getBeanProperty(srcVariableName); // varA
                    }
                }
            }

            instance.setBeanProperty(targetFieldName, value);
        }

    }

    @Override
    public void onModelingTime() {
        mapper = new MappingContext(this, null, getMappingContexts());
        mapperIn = new MappingContext(this, null, getMappingContexts());

        autowire(mapper);
        autowire(mapperIn);
    }

    @Override
    public void afterModelingTime() {
        ParameterContext[] mappingElements =
                mapper.getMappingCanvas().getMappingElements();

        setMappingContexts(mappingElements);

        setMapper(null);

        ////

        ParameterContext[] mappingElementsIn =
                mapperIn.getMappingCanvas().getMappingElements();

        setMappingContexts(mappingElementsIn);

        setMapperIn(null);
    }

}