package org.uengine.modeling.resource;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by jjy on 2016. 1. 15..
 */
public class Version implements Serializable{

    int major;
    int minor;
    String description;
    Calendar date;
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

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }



    @ServiceMethod(callByContent = true, target = ServiceMethod.TARGET_SELF)
    public void makeAsProduction(@AutowiredFromClient/*(payload="appName,moduleName")*/ VersionManager versionManager) throws Exception {

        versionManager.makeProductionVersion(this);

        setProduction(true);
    }

    @ServiceMethod(callByContent = true, target = ServiceMethod.TARGET_SELF)
    public void restore(@AutowiredFromClient/*(payload="appName,moduleName")*/ VersionManager versionManager) throws Exception {
        //VersionManager versionManager = MetaworksRemoteService.getComponent(VersionManager.class);
        MetaworksRemoteService.autowire(versionManager);
        versionManager.restore(this);
    }


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
