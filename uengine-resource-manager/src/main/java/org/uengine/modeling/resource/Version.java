package org.uengine.modeling.resource;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by jjy on 2016. 1. 15..
 */
public class Version implements Serializable{

    int major;
    int minor;
    String description;
    private boolean production;

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ServiceMethod(callByContent = true, target = ServiceMethod.TARGET_SELF)
    public void makeAsProduction(@AutowiredFromClient VersionManager versionManager) throws Exception {

        versionManager.makeProductionVersion(this);

        setProduction(true);
    }

//    public String getVersionDirectory(VersionManager versionManager) {
//        return versionManager.versionDirectoryOf(this, null);
//    }

    public void setProduction(boolean production) {
        this.production = production;
    }

    public boolean isProduction() {
        return production;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj == null || !(obj instanceof Version)) return false;

        Version versionObj = (Version)obj;

        return (getMajor() == versionObj.getMajor() && getMinor() == versionObj.getMinor());
    }
}
