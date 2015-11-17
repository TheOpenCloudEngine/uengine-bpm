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
            try {
//                String alias = resource.getName();
                String alias = resource.getPath().substring(resource.getPath().indexOf(File.separator) + 1);

               // if(alias.endsWith(".process")) {
                    String name = alias.substring(0, alias.length() - 8).replace(File.separator,".");


                    ProcessMap processMap = new ProcessMap();

                    autowire(processMap);

                    processMap.setMapId(processMap.session.getCompany().getComCode() + "." + name);
                    processMap.setDefId(alias);
                    processMap.setName(name);
                    processMap.setComCode(processMap.session.getCompany().getComCode());


                    if(!processMap.confirmExist())
                        throw new Exception("$AlreadyAddedApp");

                    processMap.createMe();

                    ProcessMapList processMapList = new ProcessMapList();
                    processMapList.load(processMap.session);


                    wrapReturn(new Remover(new ModalWindow()));
                //}

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClicked(IResource resource) {

    }
}
