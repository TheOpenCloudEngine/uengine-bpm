package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.model.TTask;
import org.uengine.kernel.*;

import java.util.Hashtable;

public class TUserTaskAdapter extends TTaskAdapter{
    @Override
    protected Activity createActivity(TTask src, Hashtable keyedContext) {
        HumanActivity humanActivity = new HumanActivity();
        initializeActivity(humanActivity, src);

        return humanActivity;
    }

    @Override
    public Activity convert(TTask src, Hashtable keyedContext) throws Exception {
        HumanActivity humanActivity = (HumanActivity) super.convert(src, keyedContext);

        ProcessDefinition processDefinition = (ProcessDefinition) keyedContext.get("processDefinition");

        if(src.getResourceRole() != null && src.getResourceRole().size() > 0){
            String roleId = src.getResourceRole().get(0).getValue().getId();

            Role role = processDefinition.getRole(roleId);

            humanActivity.setRole(role);
        }else{ //if there's no semantic information from the original model, find the role (lane) by position of activity.

            // if there's just one role has been defined in the process definition, definitely the role is that one.
            if(processDefinition.getRoles()!=null && processDefinition.getRoles().length == 1){
                humanActivity.setRole(processDefinition.getRoles()[0]);
            }

        }

        return humanActivity;
    }


}
