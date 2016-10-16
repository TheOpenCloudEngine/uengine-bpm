package org.uengine.components.deployfilters;

import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.kernel.*;
import org.uengine.processmanager.ProcessTransactionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjy on 2016. 10. 14..
 */
public class GlobalProcessVariableRegistrationFilter implements DeployFilter{
    @Override
    public void beforeDeploy(ProcessDefinition definition, ProcessTransactionContext tc, String folder, boolean isNew) throws Exception {

        ProcessDefinition globalDefinition = new ProcessDefinition();
        globalDefinition.setGlobal(true);

        //extracts global process variables from the definition
        List<ProcessVariable> globalProcessVariables = new ArrayList<ProcessVariable>();
        List<ProcessVariable> locallProcessVariables = new ArrayList<ProcessVariable>();
        if(definition.getProcessVariables()!=null){

            for(ProcessVariable processVariable : definition.getProcessVariables()){
                if(processVariable.isGlobal()) {
                    globalProcessVariables.add(processVariable);
                }else{
                    locallProcessVariables.add(processVariable);
                }
            }

        }

        locallProcessVariables.toArray(definition.getProcessVariables());
        globalProcessVariables.toArray(globalDefinition.getProcessVariables());

        ProcessDefinitionFactory.getInstance(tc).addDefinition(
                "global.process",
                null,
                0,
                "global",
                "",
                false,
                GlobalContext.serialize(definition, ProcessDefinition.class),
                "folder");

    }
}
