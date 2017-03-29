package org.uengine.modeling.resource;

import org.apache.commons.io.FileUtils;
import org.metaworks.MetaworksContext;
import org.oce.garuda.multitenancy.TenantContext;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
@Component
public abstract class AbstractStorage implements Storage{

    boolean doNotOverwrite;
        public boolean isDoNotOverwrite() {
            return doNotOverwrite;
        }
        public void setDoNotOverwrite(boolean doNotOverwrite) {
            this.doNotOverwrite = doNotOverwrite;
        }

    String basePath;
        public String getBasePath() {
            return basePath;
        }
        public void setBasePath(String basePath) {
            this.basePath = basePath;
        }


    @Override
    public Object getObject(IResource resource) throws Exception {
        return Serializer.deserialize(getInputStream(resource));
    }

    @Override
    public void save(IResource resource, Object object) throws Exception {
        Serializer.serialize(object, getOutputStream(resource));

    }

    protected String getTenantBasePath() {
        String tenantId = TenantContext.getThreadLocalInstance().getTenantId();

        if(tenantId==null){
            tenantId = "default";
        }

        return getBasePath() + (getBasePath().endsWith("/") ? "":"/") + tenantId + "/";
    }

    protected String getAbsolutePath(IResource resource){
        String path = (resource!=null ? resource.getPath() : "");

        if(path.startsWith("/")) path = path.substring(1);

        return getTenantBasePath()
                + path;
    }

    protected String getParentAbsolutePath(IResource resource){
        if(resource==null) return "";

        String path = resource.getPath();

        int lastFolderNmIdx = path.lastIndexOf("/");

        if(lastFolderNmIdx==-1) return "";

        path = path.substring(0, lastFolderNmIdx);
        IResource parentResource = new DefaultResource(path);

        return getAbsolutePath(parentResource);
    }
}
