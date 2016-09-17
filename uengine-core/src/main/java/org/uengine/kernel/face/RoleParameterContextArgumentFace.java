package org.uengine.kernel.face;

import org.metaworks.Face;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Payload;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.component.SelectBox;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.kernel.*;
import org.uengine.processmanager.ProcessManagerRemote;
import org.uengine.security.ISession;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by jjy on 2012. 1. 2..
 */
public class RoleParameterContextArgumentFace extends SelectBox implements Face<String> {
    @Override
    public void setValueToFace(String value) {
        setSelected(value);
    }

    @Override
    public String createValueFromFace() {
        return getSelected();
    }

    @ServiceMethod(onLoad=true)
    public void onLoad(@Payload("selected") String selected, @AutowiredFromClient Activity activity, @AutowiredFromClient ISession session){

        setMetaworksContext(new MetaworksContext());
        getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);

        if(activity instanceof SubProcessActivity){
            SubProcessActivity subProcessActivity = (SubProcessActivity) activity;

            String subProcessDefinitionId = subProcessActivity.getDefinitionId();

            if(subProcessDefinitionId==null) return;

            ProcessManagerRemote processManagerRemote = MetaworksRemoteService.getComponent(ProcessManagerRemote.class);
            try {
                ProcessDefinition processDefinition = processManagerRemote.getProcessDefinition(subProcessDefinitionId);

                if(processDefinition.getRoles()!=null){

                    setOptionValues(new ArrayList<String>());
                    setOptionNames(new ArrayList<String>());

                    for(Role role : processDefinition.getRoles()){
                        getOptionValues().add(role.getName());
                        getOptionNames().add(role.getDisplayName().getText(session.getLocale()));
                    }

                    setSelected(selected);
                }


            } catch (RemoteException e) {
                throw new RuntimeException("Failed to get process definition object :" + e.getMessage(), e);
            }
        }

    }
}
