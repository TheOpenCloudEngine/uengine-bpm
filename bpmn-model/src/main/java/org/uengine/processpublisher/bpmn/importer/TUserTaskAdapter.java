package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.model.TTask;
import org.uengine.kernel.*;

import java.util.Hashtable;

public class TUserTaskAdapter extends TTaskAdapter{
    @Override
    protected Activity create(TTask src, Hashtable keyedContext) {
        return new HumanActivity();
    }

    @Override
    public Activity convert(TTask src, Hashtable keyedContext) throws Exception {
        HumanActivity humanActivity = (HumanActivity) super.convert(src, keyedContext);

        if(src.getResourceRole() != null && src.getResourceRole().size() > 0){
            String roleId = src.getResourceRole().get(0).getValue().getId();
            ProcessDefinition processDefinition = (ProcessDefinition) keyedContext.get("processDefinition");

            Role role = processDefinition.getRole(roleId);

            humanActivity.setRole(role);
        }

        return humanActivity;
    }


}
