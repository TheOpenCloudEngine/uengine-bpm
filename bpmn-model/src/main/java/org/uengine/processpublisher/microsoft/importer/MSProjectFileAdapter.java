package org.uengine.processpublisher.microsoft.importer;

import net.sf.mpxj.*;
import net.sf.mpxj.mpp.MPPReader;
import net.sf.mpxj.mpx.MPXReader;
import net.sf.mpxj.mspdi.MSPDIReader;
import net.sf.mpxj.reader.ProjectReader;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.Role;
import org.uengine.processpublisher.Adapter;

import java.io.File;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by MisakaMikoto on 2015. 10. 21..
 */
public class MSProjectFileAdapter implements Adapter<File, ProcessDefinition> {
    // .mpx
    ProjectReader mpxReader;
    public ProjectReader getMpxReader() {
        return mpxReader;
    }
    public void setMpxReader(ProjectReader mpxReader) {
        this.mpxReader = mpxReader;
    }

    // .mpp
    ProjectReader mppReader;
    public ProjectReader getMppReader() {
        return mppReader;
    }
    public void setMppReader(ProjectReader mppReader) {
        this.mppReader = mppReader;
    }

    // .xml
    ProjectReader mspdiReader;
    public ProjectReader getMspdiReader() {
        return mspdiReader;
    }
    public void setMspdiReader(ProjectReader mspdiReader) {
        this.mspdiReader = mspdiReader;
    }

    @Override
    public ProcessDefinition convert(File src, Hashtable keyedContext) throws Exception {
            this.createReader(src);
            ProjectFile projectFile = this.createProjectFile(src);

            ProcessDefinition processDefinition = new ProcessDefinition();

            // task and task's resource
            if(projectFile.getAllTasks() != null && projectFile.getAllTasks().size() > 0) {
                List<Task> taskList = projectFile.getAllTasks();

                for(Task task : taskList) {
                    HumanActivity humanActivity = new HumanActivity();
                    List<ResourceAssignment> resourceAssignmentList = task.getResourceAssignments();

                    for(ResourceAssignment resourceAssignment : resourceAssignmentList) {
                        Resource resource = resourceAssignment.getResource();

                        Role role = new Role();
                        role.setName(resource.getName());

                        humanActivity.setRole(role);
                    }
                    processDefinition.addChildActivity(humanActivity);
                }
            }

        // resource
        if(projectFile.getAllResources() != null &&  projectFile.getAllResources().size() > 0) {
            for(Resource resource : projectFile.getAllResources()) {
                Role role = new Role();
                role.setName(resource.getName());

                processDefinition.addRole(role);
            }
        }
        return processDefinition;
    }

    private void createReader(File file) {
        int lastDot = file.getName().lastIndexOf('.');
        if (lastDot == file.getName().length() - 1) {
            System.out.println("check the file extension");
        }

        String fileExt = file.getName().substring(lastDot + 1).toLowerCase();
        if ("mpp".equals(fileExt)) {
            this.setMppReader(new MPPReader());
        }
        else if ("xml".equals(fileExt) || "process".equals(fileExt)) {
            this.setMspdiReader(new MSPDIReader());
        }
        else if ("mpx".equals(fileExt)) {
            this.setMpxReader(new MPXReader());

        } else {
            ;
        }
    }

    private ProjectFile createProjectFile(File file) {
        ProjectFile projectFile = null;

        // mpx
        if(this.getMpxReader() != null && this.getMppReader() == null && this.getMspdiReader() == null) {
            try {
                projectFile = this.getMpxReader().read(file);
            } catch (MPXJException e) {
                e.printStackTrace();
            }

            // mpp
        } else if(this.getMpxReader() == null && this.getMppReader() != null && this.getMspdiReader() == null) {
            try {
                projectFile = this.getMppReader().read(file);
            } catch (MPXJException e) {
                e.printStackTrace();
            }

            // mspdi
        } else {
            try {
                projectFile = this.getMspdiReader().read(file);
            } catch (MPXJException e) {
                e.printStackTrace();
            }
        }
        return projectFile;
    }

}
