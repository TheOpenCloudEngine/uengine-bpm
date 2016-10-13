package org.uengine.social;

import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.codi.mw3.model.IInstance;
import org.uengine.codi.mw3.model.Instance;
import org.uengine.kernel.Activity;
import org.uengine.kernel.ExecutionScopeContext;
import org.uengine.kernel.ProcessInstance;
import org.uengine.processmanager.ProcessManagerRemote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jjy on 2016. 9. 19..
 */
public class ProcessInstanceExplorer {

    ProcessInstanceExplorerNode root;

    public ProcessInstanceExplorerNode getRoot() {
            return root;
        }
        public void setRoot(ProcessInstanceExplorerNode root) {
            this.root = root;
        }


    public void load(long rootInstanceId) throws Exception {
        load(rootInstanceId, true);
    }

    public void load(long instanceId, boolean fromRootProcessInstance) throws Exception {

        //replace real rootInstanceId -- select rootInstId bpm_procinst where instid = ?rootInstanceId
        setRoot(new ProcessInstanceExplorerNode());

        long rootInstanceId = instanceId;

        //if(fromRootProcessInstance) {
        Instance instance = new Instance();
        instance.setInstId(rootInstanceId);
        rootInstanceId = instance.databaseMe().getRootInstId();
        //}

        List<ProcessInstanceExplorerNode> processInstanceExplorerNodeList = new ArrayList<ProcessInstanceExplorerNode>();
        Map<String,ProcessInstanceExplorerNode > processInstanceNodeByInstanceId = new HashMap<String, ProcessInstanceExplorerNode>();

        // load execution scopes first
        ProcessManagerRemote pm = MetaworksRemoteService.getComponent(ProcessManagerRemote.class);
        ProcessInstance processInstance = pm.getProcessInstance(String.valueOf(
                fromRootProcessInstance ? rootInstanceId : instanceId));

        List<ExecutionScopeContext> executionScopeContexts = processInstance.getExecutionScopeContexts();


        for(ExecutionScopeContext executionScopeContext : executionScopeContexts){
            ProcessInstanceExplorerNode processInstanceExplorerNode = new ProcessInstanceExplorerNode();
            processInstanceExplorerNode.setName(executionScopeContext.getName());

            if(executionScopeContext.getTriggerActivityTracingTag()!=null) {
                Activity triggerActivity = processInstance.getProcessDefinition().getActivity(executionScopeContext.getTriggerActivityTracingTag());

                if(triggerActivity==null) continue; //case when the process definition is changed during simulation

                processInstanceExplorerNode.setName("[" + triggerActivity.getName() + "]" + processInstanceExplorerNode.getName());
            }
            processInstanceExplorerNode.setInstanceId(processInstance.getInstanceId() + "@" + executionScopeContext.getExecutionScope());


            processInstanceExplorerNode.setMainInstId(processInstance.getInstanceId() + ( executionScopeContext.getParent()!=null ? "@" + executionScopeContext.getParent().getExecutionScope() : ""));

            processInstanceExplorerNodeList.add(processInstanceExplorerNode);

            processInstanceNodeByInstanceId.put(processInstanceExplorerNode.getInstanceId(), processInstanceExplorerNode);
        }
        //



        IInstance instances = Instance.loadForAllChildInstances(rootInstanceId);


        while(instances.next()){
            ProcessInstanceExplorerNode processInstanceExplorerNode = new ProcessInstanceExplorerNode();
            processInstanceExplorerNode.setName("[" + instances.getDefName() + "] " + instances.getName());
            processInstanceExplorerNode.setInstanceId(String.valueOf(instances.getInstId()));

            String mainExecutionScope = instances.getMainExecScope();

            if(mainExecutionScope==null)
                processInstanceExplorerNode.setMainInstId(String.valueOf(instances.getMainInstId()));
            else
                processInstanceExplorerNode.setMainInstId(rootInstanceId + "@" + mainExecutionScope);

            processInstanceNodeByInstanceId.put(processInstanceExplorerNode.getInstanceId(), processInstanceExplorerNode);


            if(instances.getMainInstId()==null) setRoot(processInstanceExplorerNode);
            else
                processInstanceExplorerNodeList.add(processInstanceExplorerNode);
        }


        for(ProcessInstanceExplorerNode processInstanceExplorerNode : processInstanceExplorerNodeList){
            ProcessInstanceExplorerNode mainProcess = processInstanceNodeByInstanceId.get(processInstanceExplorerNode.getMainInstId());

            if(mainProcess==null) continue;

            if(mainProcess.getChildInstances()==null){
                mainProcess.setChildInstances(new ArrayList<ProcessInstanceExplorerNode>());
            }
            mainProcess.getChildInstances().add(processInstanceExplorerNode);
        }


        if(!fromRootProcessInstance){
            setRoot(processInstanceNodeByInstanceId.get(""+instanceId));

            //ProcessInstanceExplorerNode uncollapsing = processInstanceNodeByInstanceId.get("" + instanceId);


        }

    }


}
