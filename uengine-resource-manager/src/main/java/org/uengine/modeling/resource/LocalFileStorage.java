package org.uengine.modeling.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.uengine.modeling.IModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
@Component
public class LocalFileStorage implements Storage{

    @Autowired
    ResourceManager resourceManager;

    String localBasePath;
        public String getLocalBasePath() {
            return localBasePath;
        }
        public void setLocalBasePath(String localBasePath) {
            this.localBasePath = localBasePath;
        }



    @Override
    public void delete(IResource fileResource) {

        getFile(fileResource).delete();

    }

    @Override
    public void rename(IResource fileResource, String newName) {
        getFile(fileResource).renameTo(new File(newName));

    }

    @Override
    public List<IResource> listFiles(IContainer containerResource) throws Exception {
        List<IResource> resourceList = new ArrayList<IResource>();

        for(File file : getFile(containerResource).listFiles()){
            resourceList.add(DefaultResource.createResource(file.getPath()));
        }

        return resourceList;
    }

    @Override
    public IModel getObject(IResource resource) throws Exception {
        return (IModel) Serializer.deserialize(new FileInputStream(getFile(resource)));
    }

    private File getFile(IResource fileResource) {
        return new File(getLocalBasePath() + fileResource.getPath());
    }
}
