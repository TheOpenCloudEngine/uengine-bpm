package org.uengine.processadmin;

import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.modeling.resource.ResourcePreviewer;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.EditorPanel;
import org.uengine.modeling.resource.ResourceManager;
import org.uengine.modeling.resource.ResourceNavigator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jangjinyoung on 2016. 6. 11..
 */
public class RecentEditedResourcesPanel extends EditorPanel {

    public RecentEditedResourcesPanel(ResourceNavigator resourceNavigator) throws Exception {

        ResourceManager resourceManager = MetaworksRemoteService.getComponent(ResourceManager.class);

        DefaultResource recentListRS = new DefaultResource("codi" + "_recent.xml");

        try {
            setRecentList((List<String>) resourceManager.getObject(recentListRS));


            setRecentPreviewers(new ArrayList<ResourcePreviewer>());
//            for(String recentItemPath : getRecentList()){
//
//                //String recentItemPath = getRecentList().get(i);
//
//                DefaultResource defaultResource = new DefaultResource(recentItemPath);
//                ResourcePreviewer previewer = new ResourcePreviewer(defaultResource);
//
//                getRecentPreviewers().add(previewer);
//            }


//        }catch (FileNotFoundException e){

            //ignore
        }catch (Exception e){
            //ignore too.
            e.printStackTrace();
        }
    }


    public List<String> getRecentList() {
        return recentList;
    }

    public void setRecentList(List<String> recentList) {
        this.recentList = recentList;
    }

    List<String> recentList;


    public List<ResourcePreviewer> getRecentPreviewers() {
        return recentPreviewers;
    }

    public void setRecentPreviewers(List<ResourcePreviewer> recentPreviewers) {
        this.recentPreviewers = recentPreviewers;
    }

    List<ResourcePreviewer> recentPreviewers;

}
