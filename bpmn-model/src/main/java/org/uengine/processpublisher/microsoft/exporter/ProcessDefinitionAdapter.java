package org.uengine.processpublisher.microsoft.exporter;

import net.sf.mpxj.*;
import org.uengine.kernel.Activity;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.Role;
import org.uengine.processpublisher.microsoft.Adapter;

/**
 * Created by MisakaMikoto on 2015. 10. 21..
 */
public class ProcessDefinitionAdapter implements Adapter<ProcessDefinition, ProjectFile> {

    @Override
    public ProjectFile convert(ProcessDefinition src) throws Exception {
        // base data-structure for project files
        ProjectFile projectfile = new ProjectFile();

        // find Activity
        if (src.getChildActivities() != null && src.getChildActivities().size() > 0) {
            for (Activity activity : src.getChildActivities()) {
                // if HumanActivity
                if (activity instanceof HumanActivity) {
                    HumanActivity humanActivity = (HumanActivity) activity;

                    // the same task as in ms project
                    Task task = projectfile.addTask();
                    task.setName(humanActivity.getName());

                    if (humanActivity.getRole() != null) {
                        Resource resource = projectfile.addResource();
                        resource.setName(humanActivity.getRole().getName());
                        task.addResourceAssignment(resource);
                    }

                }
            }
        }
        return projectfile;
    }
}
