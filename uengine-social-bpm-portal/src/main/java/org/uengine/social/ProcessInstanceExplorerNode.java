package org.uengine.social;

import org.metaworks.Refresh;
import org.metaworks.annotation.*;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.codi.mw3.model.IInstance;
import org.uengine.codi.mw3.model.Instance;
import org.uengine.codi.mw3.model.InstanceView;
import org.uengine.codi.mw3.model.InstanceViewDetail;
import org.uengine.kernel.ExecutionScopeContext;

import java.util.List;

/**
 * Created by jjy on 2016. 9. 19..
 */
@Face(ejsPath="dwr/metaworks/genericfaces/TreeFace.ejs")
public class ProcessInstanceExplorerNode{

    String instanceId;
    @Id
        public String getInstanceId() {
            return instanceId;
        }
        public void setInstanceId(String instanceId) {
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

    String mainInstId;
        public String getMainInstId() {
            return mainInstId;
        }

        public void setMainInstId(String mainInstId) {
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


        if(getInstanceId().indexOf("@") == -1) { //means this instance is a separated sub process instance by CallActivity
            ProcessInstanceExplorer uncollapsedProcessInstanceExplorer = new ProcessInstanceExplorer();
            uncollapsedProcessInstanceExplorer.load(Long.valueOf(getInstanceId()), false);


            InstanceMonitorPanel instanceMonitorPanel = new InstanceMonitorPanel();
            instanceMonitorPanel.setInstanceId(getInstanceId());
            instanceMonitorPanel.load();
            instanceMonitorPanel.setProcessInstanceExplorer(uncollapsedProcessInstanceExplorer);

            MetaworksRemoteService.wrapReturn(instanceMonitorPanel);

        }else{

            InstanceMonitorPanel instanceMonitorPanel = new InstanceMonitorPanel();
            instanceMonitorPanel.setInstanceId(getInstanceId());
            instanceMonitorPanel.load();

            MetaworksRemoteService.wrapReturn(instanceMonitorPanel);

        }
    }

}
