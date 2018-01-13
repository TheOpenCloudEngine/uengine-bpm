package org.uengine.kernel.bpmn;

import org.uengine.kernel.*;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by uengine on 2017. 12. 4..
 */
public class SendTask extends DefaultActivity {

    @Override
    protected void executeActivity(ProcessInstance instance) throws Exception {

        String payload =
                evaluateContent(instance, getInputPayloadTemplate()).toString();

        if(getDataInput()!=null) {
            getDataInput().set(instance, "", payload);
        }

        List<String> messages = getMessageQueue(instance);
        if(messages==null){
            messages = new ArrayList<String>();
            instance.getRootProcessInstance().getProcessTransactionContext().setSharedContext("messages", messages);
        }

        messages.add(payload);


        super.executeActivity(instance);
    }

    public static List<String> getMessageQueue(ProcessInstance instance) throws Exception {
        return (List<String>) instance.getRootProcessInstance().getProcessTransactionContext().getSharedContext("messages");
    }

    String inputPayloadTemplate;
        public String getInputPayloadTemplate() {
            return inputPayloadTemplate;
        }
        public void setInputPayloadTemplate(String inputPayloadTemplate) {
            this.inputPayloadTemplate = inputPayloadTemplate;
        }

    ProcessVariable dataInput;
        public ProcessVariable getDataInput() {
            return dataInput;
        }
        public void setDataInput(ProcessVariable dataInput) {
            this.dataInput = dataInput;
        }

}
