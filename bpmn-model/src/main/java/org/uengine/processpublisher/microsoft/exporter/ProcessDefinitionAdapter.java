package org.uengine.processpublisher.microsoft.exporter;

import net.sf.mpxj.*;
import org.uengine.kernel.Activity;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.Role;
import org.uengine.kernel.bpmn.StartEvent;
import org.uengine.processpublisher.microsoft.Adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MisakaMikoto on 2015. 10. 21..
 */
public class ProcessDefinitionAdapter implements Adapter<ProcessDefinition, ProjectFile> {
    public final static String ROLE_NAME = "RoleName";

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
    public ProjectFile convert(ProcessDefinition src) throws Exception {
        // base data-structure for project files
        ProjectFile projectFile = new ProjectFile();
        // find Activity
        if (src.getChildActivities() != null && src.getChildActivities().size() > 0) {
            for (Activity activity : src.getChildActivities()) {
                // if HumanActivity
                if (activity instanceof HumanActivity) {
                    HumanActivity humanActivity = (HumanActivity) activity;

                    // task add to project
                    Task task = projectFile.addTask();
                    this.setTaskUniqueID(task, humanActivity);
                    this.setTaskName(task, humanActivity);
                    this.setTaskDuration(task, humanActivity);
                    this.setTaskPredecessor(task, humanActivity, projectFile);

                }
            }
        }
        return projectFile;
    }

    private void setTaskUniqueID(Task currentTask, HumanActivity currentHumanActivity) {
        // humanActivity's tracingtag == task's uniqueID
        currentTask.setUniqueID(Integer.parseInt(currentHumanActivity.getTracingTag()));
    }

    private void setTaskName(Task currentTask, HumanActivity currentHumanActivity) {
        // if humanActivty mapped role, task name = task name + role name
        if (currentHumanActivity.getRole() != null) {
            Role role = currentHumanActivity.getRole();
            currentTask.setName(currentHumanActivity.getName() + "(" + ROLE_NAME + " : " + role.getName() + ")");

        } else {
            currentTask.setName(currentHumanActivity.getName());
        }
    }

    private void setTaskDuration(Task currentTask, HumanActivity currentHumanActivity) {
        // set duration
        currentTask.setDuration(Duration.getInstance(currentHumanActivity.getDuration(), TimeUnit.DAYS));
    }

    private void setTaskPredecessor(Task currentTask, HumanActivity currentHumanActivity, ProjectFile projectFile) {
        if(currentHumanActivity.getIncomingSequenceFlows().get(0).getSourceElement() instanceof HumanActivity) {
            HumanActivity incomingHumanActivity = (HumanActivity) currentHumanActivity.getIncomingSequenceFlows().get(0).getSourceElement();
            // humanActivity's tracingtag == task's uniqueID
            int taskUniqueID = Integer.parseInt(incomingHumanActivity.getTracingTag());
            currentTask.addPredecessor(projectFile.getTaskByUniqueID(taskUniqueID), RelationType.FINISH_START, Duration.getInstance(incomingHumanActivity.getDuration(), TimeUnit.HOURS));
        }
    }
}
