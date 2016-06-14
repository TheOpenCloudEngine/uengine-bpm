package org.uengine.modeling.resource;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.modeling.HasThumbnail;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.ResourceManager;
import org.uengine.modeling.resource.Describable;

import java.rmi.server.ExportException;

/**
 * Created by jangjinyoung on 2016. 6. 11..
 */
public class ResourcePreviewer {

    String name;
    String path;
    private String description;

    public ResourcePreviewer(){}

    public ResourcePreviewer(DefaultResource defaultResource) {
        setName(defaultResource.getName());
        setPath(defaultResource.getPath());
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    String thumbnailURL;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Id
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    @ServiceMethod(onLoad = true, inContextMenu = true, target = ServiceMethod.TARGET_SELF)
    public void fill() throws Exception {
        try {
            Object resource = resourceManager.getObject(new DefaultResource(getPath()));

            if (resource instanceof HasThumbnail) {
                setThumbnailURL(((HasThumbnail) resource).getThumbnailURL());

            }

            if (resource instanceof Describable) {
                Describable describable = (Describable) resource;

                setName(describable.getName());
                setDescription(describable.getDescription());
            }
        }catch (Exception e){setDescription("Not available anymore.");}
    }

    @ServiceMethod(target= ServiceMethodContext.TARGET_POPUP)
    public void open() throws Exception{
        DefaultResource defaultResource = new DefaultResource(getPath());
        MetaworksRemoteService.autowire(defaultResource);
        defaultResource.reopen();
    }

    @Autowired
    public ResourceManager resourceManager;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
