package org.uengine.processadmin;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mspdi.MSPDIWriter;
import org.directwebremoting.io.FileTransfer;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.Download;
import org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.processpublisher.Adapter;
import javax.activation.MimetypesFileTypeMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by jjy on 2016. 1. 28..
 */
public class ProcessExporter {
    public ProcessExporter() {} // for dwr invocation

    @ServiceMethod(target = ServiceMethod.TARGET_APPEND)
    public Download downloadSource(@AutowiredFromClient ProcessAdminEditorPanel processAdminEditorPanel) throws Exception {
        return processAdminEditorPanel.download();
    }

    @ServiceMethod()
    public Download downloadAsBPMN(@AutowiredFromClient ProcessAdminEditorPanel processAdminEditorPanel){
        Adapter processDefinitionAdapter = new org.uengine.processpublisher.bpmn.exporter.ProcessDefinitionAdapter();
        ProcessDefinition processDefinition = (ProcessDefinition) processAdminEditorPanel.getEditor().createEditedObject();

        String fileName = processAdminEditorPanel.getResourcePath().substring(processAdminEditorPanel.getResourcePath().lastIndexOf("\\") + 1, processAdminEditorPanel.getResourcePath().lastIndexOf("."));
        fileName = fileName + "(BPMN).xml";

        ByteArrayOutputStream bao = null;
        ByteArrayInputStream bio = null;

        try {
            TDefinitions tDefinitions = (TDefinitions) processDefinitionAdapter.convert(processDefinition, null);
            org.omg.spec.bpmn._20100524.model.ObjectFactory objectFactory = new org.omg.spec.bpmn._20100524.model.ObjectFactory();

            JAXBElement<TDefinitions> element = objectFactory.createDefinitions(tDefinitions);
            JAXBContext jaxbContext = JAXBContext.newInstance(TDefinitions.class);

            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            bao = new ByteArrayOutputStream();
            bio = new ByteArrayInputStream(bao.toByteArray());

            marshaller.marshal(element, bao);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Download(new FileTransfer(fileName, MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(fileName), bio));
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

        String fileName = processAdminEditorPanel.getResourcePath().substring(processAdminEditorPanel.getResourcePath().lastIndexOf("\\") + 1, processAdminEditorPanel.getResourcePath().lastIndexOf("."));
        fileName = fileName + "(MS).xml";

        return new Download(new FileTransfer(fileName, MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(fileName), bio));
    }


}
