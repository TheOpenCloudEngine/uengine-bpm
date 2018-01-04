package org.uengine.modeling.resource;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by jjy on 2016. 10. 20..
 */
@Component
@Scope("prototype")
public class SimpleVersionManager implements VersionManager{
    public static final String VERSION_DIR = "_versions";

    List<Version> versions;
    public List<Version> getVersions() {
        return versions;
    }
    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    //String rootPath;
    public String getRootPath() {
        return getAppName() + "/" + (getModuleName() !=null ? getModuleName() + "/" : "");
    }
//    public void setRootPath(String rootPath) {
//        this.rootPath = rootPath;
//    }

    String appName;
        public String getAppName() {
            return appName;
        }
        public void setAppName(String appName) {
            this.appName = appName;
        }


    String moduleName;
        public String getModuleName() {
            return moduleName;
        }
        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }


    MetaworksContext metaworksContext;
    @Override
    public MetaworksContext getMetaworksContext() {
        return metaworksContext;
    }
    @Override
    public void setMetaworksContext(MetaworksContext metaworksContext) {
        this.metaworksContext = metaworksContext;
    }


    @Autowired
    public ResourceManager resourceManager;


    @Override
    public void load(String appName, String moduleName) throws Exception {
        setAppName(appName);
        setModuleName(moduleName);

        setMetaworksContext(new MetaworksContext());
        getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);

        setVersions(listVersions());

        Collections.sort(versions, new Comparator<Version>() {

            @Override
            public int compare(Version o2, Version o1) {
                int comp = (o1.getMajor() - o2.getMajor());

                if (comp != 0) return comp;

                comp = (o1.getMinor() - o2.getMinor());

                return comp;
            }
        });

        setMakeThisVersionAsProduction(true);
    }

    @Override
    public List<Version> listVersions() throws Exception {

        String versionPath = versionDirectoryOf(null);

        IContainer versionRoot = new ContainerResource();
        versionRoot.setPath(versionPath);

        List<Version> versionList = new ArrayList<Version>();


        Version productionVersion = getProductionVersion();

        List<IResource> versionDirs = resourceManager.getStorage().listFiles(versionRoot);
        for(IResource versionDir : versionDirs){
            if(versionDir instanceof IContainer){
                String fullVersionName = versionDir.getName();
                String[] majorAndMinor = fullVersionName.split("\\.");

                Version version = new Version();
                version.setMajor(Integer.valueOf(majorAndMinor[0]));
                version.setMinor(Integer.valueOf(majorAndMinor[1]));

                if(version.equals(productionVersion)){
                    version.setProduction(true);
                }

                versionList.add(version);
            }
        }

        return versionList;
    }

    @Override
    public String versionDirectoryOf(Version version) {
        if(version!=null)
            return getRootPath() + VERSION_DIR + "/" + version.getMajor() + "." + version.getMinor();
        else
            return getRootPath() + VERSION_DIR;
    }

    @ServiceMethod(callByContent = true, target=ServiceMethod.TARGET_SELF)
    public void minorVersionUp() throws Exception {

        Version lastVersion = getLastVersion();

        lastVersion.setMinor(lastVersion.getMinor() + 1);
        lastVersion.setDate(Calendar.getInstance());
        versionUp(lastVersion);

    }


    @ServiceMethod(callByContent = true, target=ServiceMethod.TARGET_SELF)
    public void majorVersionUp() throws Exception {
        Version lastVersion = getLastVersion();

        lastVersion.setMajor(lastVersion.getMajor() + 1);
        lastVersion.setDate(Calendar.getInstance());
        versionUp(lastVersion);

    }

    @ServiceMethod(callByContent = true, target=ServiceMethod.TARGET_SELF)
    public void restore(Version version) throws Exception {
        IContainer dev = new ContainerResource();
        dev.setPath(getRootPath());

        IContainer versionDirectory = new ContainerResource();
        versionDirectory.setPath(versionDirectoryOf(version));

        Version prodVersion = getProductionVersion();

        majorVersionUp(); // version up for restore later for this version.

        resourceManager.getStorage().delete(dev);
        resourceManager.getStorage().copy(versionDirectory, dev.getPath()); //TODO: filter interface is needed.
        prodVersion.makeAsProduction(this);

        MetaworksRemoteService.wrapReturn(new Label("<div class='alert alert-success' role='alert'>Version has been restored.</div>"));
    }

    private Version getLastVersion() {
        Version lastVersion;

        if(getVersions()!=null && getVersions().size() > 0) {
            lastVersion = getVersions().get(0);
        }else{
            lastVersion = new Version();
            lastVersion.setMajor(0);
            lastVersion.setMinor(0);
        }
        return lastVersion;
    }

    private void versionUp(Version lastVersion) throws Exception {
        //copy dev to new version.
        IContainer dev = new ContainerResource();
        dev.setPath(getRootPath());

        IContainer newVersion = new ContainerResource();
        newVersion.setPath(versionDirectoryOf(lastVersion));

        List<IResource> firstLevelFiles = resourceManager.getStorage().listFiles(dev);
        for(IResource file : firstLevelFiles) {
            if(!file.getName().startsWith(VERSION_DIR))
                resourceManager.getStorage().copy(file, newVersion.getPath() + "/" + file.getName());
        }

        if(isMakeThisVersionAsProduction()){
            MetaworksRemoteService.autowire(lastVersion);
            lastVersion.makeAsProduction(this);
        }

        if(TransactionContext.getThreadLocalInstance()!=null)
            MetaworksRemoteService.wrapReturn(new Label("<div class='alert alert-success' role='alert'>Version has been set as " + lastVersion.getMajor() + "." + lastVersion.getMinor() + "</div>"));
    }


    boolean makeThisVersionAsProduction;
    public boolean isMakeThisVersionAsProduction() {
        return makeThisVersionAsProduction;
    }
    public void setMakeThisVersionAsProduction(boolean makeThisVersionAsProduction) {
        this.makeThisVersionAsProduction = makeThisVersionAsProduction;
    }


    public Version getProductionVersion() {
        try {
            return (Version) resourceManager.getStorage().getObject(getProductionVersionInfo());
        } catch (Exception e) {

            if(!(e instanceof FileNotFoundException))
                e.printStackTrace();

            return null;
        }
    }

    @Override
    public void makeProductionVersion(Version version) throws Exception {
        DefaultResource productionInfoXML = getProductionVersionInfo();

        ResourceManager resourceManager = MetaworksRemoteService.getComponent(ResourceManager.class);

        resourceManager.getStorage().save(productionInfoXML, version);

    }

    protected DefaultResource getProductionVersionInfo() {
        DefaultResource productionInfoXML = new DefaultResource();
        productionInfoXML.setPath(getRootPath() + ".version.info.xml");

        return productionInfoXML;
    }

    public String getProductionResourcePath(String resourcePath){
        String original = resourcePath;

        resourcePath = getLogicalPath(resourcePath);

//        detectModuleName(resourcePath);



        Boolean isDevelopmentTime = (Boolean) (TransactionContext.getThreadLocalInstance()!=null && (Boolean)TransactionContext.getThreadLocalInstance().getSharedContext("isDevelopmentTime"));
        if(isDevelopmentTime!=null && isDevelopmentTime)
            return original;

        Version productionVersion = getProductionVersion();

        if(productionVersion!=null) {
            String versionDirectory = versionDirectoryOf(productionVersion);

            resourcePath = (getAppName() + "/../" + versionDirectory + (resourcePath!=null && resourcePath.length() > 0 ? "/" + resourcePath : ""));
        }else{
            resourcePath = (getAppName() + (getModuleName()!=null ? "/" + getModuleName() : "") + (resourcePath!=null && resourcePath.length() > 0 ? "/" + resourcePath : ""));
        }

        {// if there's no resource in production, return original one.
            ResourceManager resourceManager1 = MetaworksRemoteService.getComponent(ResourceManager.class);
            try {
                if(!resourceManager1.getStorage().exists(new DefaultResource(resourcePath))){
                    return original;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return resourcePath;
    }


    public String getLogicalPath(String resourcePath) {
        {
            String appName_versions = versionDirectoryOf(null);

            if (resourcePath.startsWith(appName_versions)) {
                String withoutPrefixPath = resourcePath.substring(appName_versions.length()+1);


                return withoutPrefixPath;
            }

            String prefixCandidate2 = getAppName() + "/../" + appName_versions;

            if (resourcePath.startsWith(prefixCandidate2)) {
                String withoutPrefixPath = resourcePath.substring(prefixCandidate2.length()+1);


                return withoutPrefixPath;
            }


        }


        {
            String prefixCandidate = getAppName() + "/";

            if (resourcePath.startsWith(prefixCandidate)) {
                return resourcePath.substring(prefixCandidate.length());
            }
        }

        return resourcePath;

    }

//
//    protected int getWhereAfterVersionPath(String appName, String resourcePath){
//
//        String prefix = appName + "/../" + appName + VERSION_DIR;
//        int wherePrefix = resourcePath.indexOf(prefix);
//
//        if(wherePrefix == 0) {
//            resourcePath = resourcePath.substring(prefix.length() + 1, resourcePath.length());
//            return resourcePath.indexOf("/");
//        }else{
//
//            prefix = appName + "/";
//            wherePrefix = resourcePath.indexOf(prefix);
//
//            if(wherePrefix == 0)
//                return appName.length();
//            else
//                return -1;
//        }
//    }



}