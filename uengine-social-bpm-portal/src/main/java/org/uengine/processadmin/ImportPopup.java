package org.uengine.processadmin;

import org.metaworks.MetaworksContext;
import org.metaworks.Remover;
import org.metaworks.annotation.Face;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.website.MetaworksFile;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.modeling.resource.ResourceManager;
import org.uengine.modeling.resource.Serializer;
import org.uengine.modeling.resource.resources.ProcessResource;
import org.uengine.processpublisher.BPMNUtil;
import org.uengine.util.UEngineUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by jjy on 2015. 10. 15..
 */
@Face(ejsPath="genericfaces/CleanObjectFace.ejs")
public class ImportPopup {

    public ImportPopup(){}

    public ImportPopup(ProcessAdminContainerResource processAdminContainerResource) {
        setSelectFile(new MetaworksFile());
        getSelectFile().setMetaworksContext(new MetaworksContext());
        getSelectFile().getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
    }

    MetaworksFile selectFile;
        public MetaworksFile getSelectFile() {
            return selectFile;
        }
        public void setSelectFile(MetaworksFile selectFile) {
            this.selectFile = selectFile;
        }

    @ServiceMethod(callByContent = true)
    public void importTheFile() throws Exception {
        if(getSelectFile().getFileTransfer()!=null){
            ProcessResource processResource = new ProcessResource();

            String fileNameToUpload = getSelectFile().getFilename().replaceFirst("[.][^.]+$", "") + ".process";

            processResource.setPath("codi/" + fileNameToUpload);

            ResourceManager resourceManager = MetaworksRemoteService.getComponent(ResourceManager.class);

            if(resourceManager.getStorage().exists(processResource)){
                throw new Exception("$ExistingResourceName");
            }


            getSelectFile().upload();

            File file = new File(getSelectFile().getUploadedPath());

            ProcessDefinition definition = null;

            if(getSelectFile().getFilename().endsWith(".bpmn")) {
                definition = BPMNUtil.adapt(file);
            }else{
                definition = (ProcessDefinition) Serializer.deserialize(new FileInputStream(file));
            }

            MetaworksRemoteService.autowire(processResource);


            processResource.save(definition);

            MetaworksRemoteService.wrapReturn(new Remover(new ModalWindow()));

        }

    }

}
