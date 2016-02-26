package org.uengine.processadmin;

import com.itextpdf.text.Meta;
import org.metaworks.Remover;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;
import org.uengine.codi.CodiProcessDefinitionFactory;
import org.uengine.codi.mw3.model.ProcessMap;
import org.uengine.codi.mw3.model.ProcessMapList;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.IResource;
import org.uengine.modeling.resource.ResourceControlDelegate;
import org.uengine.modeling.resource.VersionManager;
import org.uengine.processmanager.ProcessManagerBean;

import java.io.File;

import static org.metaworks.dwr.MetaworksRemoteService.*;

/**
 * Created by jangjinyoung on 15. 8. 14..
 */
public class ResourceControlDelegateForAddingProcessMap implements ResourceControlDelegate {
    @Override
    public void onDoubleClicked(IResource resource) {
        if(resource instanceof DefaultResource){
            String alias = resource.getName();
            String name = alias;

            ProcessMap processMap = new ProcessMap();

            autowire(processMap);

            String mapId = String.valueOf((processMap.session.getCompany().getComCode() + "." + VersionManager.withoutVersionPath("codi", resource.getPath())).hashCode());

            processMap.setMapId(mapId);
            processMap.setDefId(resource.getPath());
            processMap.setName(name);
            processMap.setComCode(processMap.session.getCompany().getComCode());

            try {
                if(processMap.databaseMe()!=null)
                    throw new Exception("$AlreadyAddedApp");

                processMap.createMe();

                ProcessMapList processMapList = new ProcessMapList();
                processMapList.load(processMap.session);


                wrapReturn(new Remover(new ModalWindow()));

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onClicked(IResource resource) {

    }
}
