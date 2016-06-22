package org.uengine.processpublisher.microsoft.exporter;

import net.sf.mpxj.*;
import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.SubProcess;
import org.uengine.processpublisher.Adapter;
import java.util.Hashtable;

/**
 * Created by MisakaMikoto on 2015. 10. 21..
 */
public class ProcessDefinitionAdapter implements Adapter<ProcessDefinition, ProjectFile> {
    // mpxj structure
    /****************************************************************************************
     *           -------------------------->  project <---------------------                *
     *           |                                    <------------        |                *
     *           |                                                |        |                *
     *           |        Resource Assignment -------------->  Resource    |                *
     *           |             |                                 |         |                *
     *           |             |                                 |         |                *
     *           |             |                                 |         |                *
     *           |    <--------|                                 ----->  Calendar           *
     *        task                                                         |                *
     *               <------------------------------------------------------                *
     *                                                                                      *
     *                                                                                      *
     *****************************************************************************************/

    @Override
    public ProjectFile convert(ProcessDefinition src, Hashtable keyedContext) throws Exception {
        // base data-structure for project files
        ProjectFile projectFile = new ProjectFile();

        // find Activity
        if (src.getChildActivities() != null && src.getChildActivities().size() > 0) {
            for (Activity activity : src.getChildActivities()) {
                // if subProcess
                if(activity instanceof SubProcess) {
                    subProcessConvert((SubProcess) activity, projectFile);
                }

                // if HumanActivity
                if (activity instanceof HumanActivity) {
                    setTask((HumanActivity) activity, projectFile);

                }
            }
        }

        // find role
        if(src.getRoles() != null && src.getRoles().length > 0) {
            setResource(src, projectFile);
        }
        return projectFile;
    }

    private void subProcessConvert(SubProcess subProcess, ProjectFile projectFile) {
        // find Activity
        if (subProcess.getChildActivities() != null && subProcess.getChildActivities().size() > 0) {
            for (Activity activity : subProcess.getChildActivities()) {
                // if subProcess
                if(activity instanceof SubProcess) {
                    subProcessConvert((SubProcess) activity, projectFile);
                }

                // if HumanActivity
                if (activity instanceof HumanActivity) {
                    setTask((HumanActivity) activity, projectFile);

                }
            }
        }
    }

    private void setTask(HumanActivity humanActivity, ProjectFile projectFile) {
        // task add to project
        Task task = projectFile.addTask();
        this.setTaskUniqueID(task, humanActivity);
        this.setTaskName(task, humanActivity);
        this.setTaskDuration(task, humanActivity);
        this.setTaskPredecessor(task, humanActivity, projectFile);
    }

    private void setResource(ProcessDefinition src, ProjectFile projectFile) {
        for(Role role : src.getRoles()) {
            Resource resource = projectFile.addResource();
            this.setResourceName(resource, role);
        }
    }

    private void setTaskUniqueID(Task currentTask, HumanActivity currentHumanActivity) {
        // humanActivity's tracingtag == task's uniqueID
        currentTask.setUniqueID(Integer.parseInt(currentHumanActivity.getTracingTag()));
    }

    private void setTaskName(Task currentTask, HumanActivity currentHumanActivity) {
        // if humanActivity mapped role, task name = task name + role name
        currentTask.setName(currentHumanActivity.getName());
    }

    private void setTaskDuration(Task currentTask, HumanActivity currentHumanActivity) {
        // set duration
        currentTask.setDuration(Duration.getInstance(currentHumanActivity.getDuration(), TimeUnit.DAYS));
    }

    private void setTaskPredecessor(Task currentTask, HumanActivity currentHumanActivity, ProjectFile projectFile) {
        if(currentHumanActivity.getIncomingSequenceFlows().size() > 0 && currentHumanActivity.getIncomingSequenceFlows().get(0).getSourceElement() instanceof HumanActivity) {
            HumanActivity incomingHumanActivity = (HumanActivity) currentHumanActivity.getIncomingSequenceFlows().get(0).getSourceElement();
            // humanActivity's tracingtag == task's uniqueID
            int taskUniqueID = Integer.parseInt(incomingHumanActivity.getTracingTag());
            currentTask.addPredecessor(projectFile.getTaskByUniqueID(taskUniqueID), RelationType.FINISH_START, Duration.getInstance(0, TimeUnit.DAYS));
        }
    }

    private void setResourceName(Resource currentResource, Role currentRole) {
        currentResource.setName(currentRole.getName());
    }

}
