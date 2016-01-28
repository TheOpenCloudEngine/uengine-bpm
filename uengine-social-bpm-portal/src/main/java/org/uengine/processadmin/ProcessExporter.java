package org.uengine.processadmin;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mspdi.MSPDIWriter;
import org.directwebremoting.io.FileTransfer;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.Download;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.processpublisher.Adapter;

import javax.activation.MimetypesFileTypeMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

/**
 * Created by jjy on 2016. 1. 28..
 */
public class ProcessExporter {

    String resourcePath;
        public String getResourcePath() {
            return resourcePath;
        }
        public void setResourcePath(String resourcePath) {
            this.resourcePath = resourcePath;
        }

    public ProcessExporter() {} // for dwr invocation

    public ProcessExporter(String resourcePath) {
        setResourcePath(resourcePath);
    }

    @ServiceMethod(target = ServiceMethod.TARGET_APPEND)
    public Download downloadSource(@AutowiredFromClient ProcessAdminEditorPanel processAdminEditorPanel) throws Exception {
        return processAdminEditorPanel.download();
    }

    @ServiceMethod()
    public void downloadAsBPMN(@AutowiredFromClient ProcessAdminEditorPanel processAdminEditorPanel){

    }


    @ServiceMethod(target = ServiceMethod.TARGET_APPEND)
    public Download exportToMicrosoftProject(@AutowiredFromClient ProcessAdminEditorPanel processAdminEditorPanel) throws Exception {

        Adapter processDefinitionAdapter = new org.uengine.processpublisher.microsoft.exporter.ProcessDefinitionAdapter();
        ProcessDefinition processDefinition = (ProcessDefinition) processAdminEditorPanel.getEditor().createEditedObject();

        ProjectFile projectFile = (ProjectFile) processDefinitionAdapter.convert(processDefinition, null);

        MSPDIWriter mspdiWriter = new MSPDIWriter();

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        mspdiWriter.write(projectFile, bao);
        ByteArrayInputStream bio = new ByteArrayInputStream(bao.toByteArray());

        String fileName = getResourcePath().substring(getResourcePath().lastIndexOf("/"), getResourcePath().lastIndexOf("."));

        return new Download(new FileTransfer(fileName, MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(fileName), bio));
    }


}
