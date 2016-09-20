package org.uengine.social;

import org.metaworks.annotation.*;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.codi.mw3.model.IInstance;
import org.uengine.codi.mw3.model.Instance;
import org.uengine.codi.mw3.model.InstanceView;
import org.uengine.codi.mw3.model.InstanceViewDetail;

import java.util.List;

/**
 * Created by jjy on 2016. 9. 19..
 */
@Face(ejsPath="dwr/metaworks/genericfaces/TreeFace.ejs")
public class ProcessInstanceExplorerNode{

    Long instanceId;
    @Id
        public Long getInstanceId() {
            return instanceId;
        }
        public void setInstanceId(Long instanceId) {
            this.instanceId = instanceId;
        }

    String name;
    @Name
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

    Long mainInstId;
        public Long getMainInstId() {
            return mainInstId;
        }
        public void setMainInstId(Long mainInstId) {
            this.mainInstId = mainInstId;
        }


    List<ProcessInstanceExplorerNode> childInstances;
    @Children
        public List<ProcessInstanceExplorerNode> getChildInstances() {
            return childInstances;
        }
        public void setChildInstances(List<ProcessInstanceExplorerNode> childInstances) {
            this.childInstances = childInstances;
        }


    @ServiceMethod(mouseBinding = "dblclick")
    public void choose() throws Exception {
        //TODO refresh the instance view.

        MetaworksRemoteService.wrapReturn(Instance.createInstanceViewDetail(getInstanceId()));
    }


}
