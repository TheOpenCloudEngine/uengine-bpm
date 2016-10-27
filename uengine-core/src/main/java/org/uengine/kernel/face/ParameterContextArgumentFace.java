package org.uengine.kernel.face;

import org.metaworks.Face;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Payload;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.component.SelectBox;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.contexts.TextContext;
import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.SubProcessActivity;
import org.uengine.processmanager.ProcessManagerRemote;
import org.uengine.security.ISession;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by jjy on 2016. 9. 14..
 */
public class ParameterContextArgumentFace extends SelectBox implements Face<TextContext> {


    @Override
    public void setValueToFace(TextContext value) {

        if(value!=null) {
            if(getOptionNames()==null || getOptionNames().size()==0){ //if there's no option value list, use the value for the only one option.
                setOptionNames(new ArrayList<String>());
                getOptionNames().add(value.getText());
                setOptionValues(getOptionNames());
            }

            setSelected(value.getText());
        }
    }

    @Override
    public TextContext createValueFromFace() {

        if(getSelected()==null)
            return null;

        TextContext textContext = TextContext.createInstance();
        textContext.setText(getSelected());

        return textContext;
    }

    @ServiceMethod()
    public void loadOptions(@Payload("selected") String selected, @AutowiredFromClient Activity activity, @AutowiredFromClient ISession session){

        setMetaworksContext(new MetaworksContext());
        getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);

        if(activity instanceof SubProcessActivity){
            SubProcessActivity subProcessActivity = (SubProcessActivity) activity;

            String subProcessDefinitionId = "codi/" + subProcessActivity.getDefinitionId();

            if(subProcessDefinitionId==null) return;

            ProcessManagerRemote processManagerRemote = MetaworksRemoteService.getComponent(ProcessManagerRemote.class);
            try {
                ProcessDefinition processDefinition = processManagerRemote.getProcessDefinition(subProcessDefinitionId);

                if(processDefinition.getProcessVariables()!=null){

                    setOptionValues(new ArrayList<String>());
                    setOptionNames(new ArrayList<String>());

                    for(ProcessVariable processVariable : processDefinition.getProcessVariables()){
                        getOptionValues().add(processVariable.getName());
                        getOptionNames().add(processVariable.getDisplayName().getText(session.getLocale()));
                    }

                    setSelected(selected);
                }


            } catch (RemoteException e) {
                throw new RuntimeException("Failed to get process definition object :" + e.getMessage(), e);
            }
        }

    }
}
