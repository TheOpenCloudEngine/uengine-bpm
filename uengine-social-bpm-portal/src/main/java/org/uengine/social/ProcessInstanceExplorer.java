package org.uengine.social;

import org.uengine.codi.mw3.model.IInstance;
import org.uengine.codi.mw3.model.Instance;

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

        //replace real rootInstanceId -- select rootInstId bpm_procinst where instid = ?rootInstanceId

        Instance instance = new Instance();
        instance.setInstId(rootInstanceId);
        rootInstanceId = instance.databaseMe().getRootInstId();


        setRoot(new ProcessInstanceExplorerNode());

        IInstance instances = Instance.loadForAllChildInstances(rootInstanceId);
        List<ProcessInstanceExplorerNode> processInstanceExplorerNodeList = new ArrayList<ProcessInstanceExplorerNode>();

        Map<Long,ProcessInstanceExplorerNode > processInstanceNodeByInstanceId = new HashMap<Long, ProcessInstanceExplorerNode>();

        while(instances.next()){
            ProcessInstanceExplorerNode processInstanceExplorerNode = new ProcessInstanceExplorerNode();
            processInstanceExplorerNode.setName("[" + instances.getDefName() + "] " +instances.getName());
            processInstanceExplorerNode.setInstanceId(instances.getInstId());
            processInstanceExplorerNode.setMainInstId(instances.getMainInstId());

            processInstanceNodeByInstanceId.put(processInstanceExplorerNode.getInstanceId(), processInstanceExplorerNode);


            if(instances.getMainInstId()==null) setRoot(processInstanceExplorerNode);
            else
                processInstanceExplorerNodeList.add(processInstanceExplorerNode);
        }


        for(ProcessInstanceExplorerNode processInstanceExplorerNode : processInstanceExplorerNodeList){
            ProcessInstanceExplorerNode mainProcess = processInstanceNodeByInstanceId.get(processInstanceExplorerNode.getMainInstId());
            if(mainProcess.getChildInstances()==null){
                mainProcess.setChildInstances(new ArrayList<ProcessInstanceExplorerNode>());
            }
            mainProcess.getChildInstances().add(processInstanceExplorerNode);
        }



    }


}
