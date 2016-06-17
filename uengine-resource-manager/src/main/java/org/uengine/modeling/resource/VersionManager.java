package org.uengine.modeling.resource;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.Label;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jjy on 2016. 1. 15..
 */
public class VersionManager implements ContextAware{

    public static final String VERSION_DIR = "_versions";

    List<Version> versions;
        public List<Version> getVersions() {
            return versions;
        }
        public void setVersions(List<Version> versions) {
            this.versions = versions;
        }

    String rootPath;
        public String getRootPath() {
            return rootPath;
        }
        public void setRootPath(String rootPath) {
            this.rootPath = rootPath;
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


    public void load(ResourceNavigator resourceNavigator) throws Exception {

        setMetaworksContext(new MetaworksContext());
        getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);

        String rootPath = resourceNavigator.getRoot().getPath();
        setRootPath(rootPath);

        String versionPath = versionDirectoryOf();

        IContainer versionRoot = new ContainerResource();
        versionRoot.setPath(versionPath);

        Version productionVersion = getProductionVersion();

        setVersions(new ArrayList<Version>());
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

                getVersions().add(version);
            }
        }

        Collections.sort(versions, new Comparator<Version>(){

            @Override
            public int compare(Version o2, Version o1) {
                int comp = (o1.getMajor() - o2.getMajor());

                if(comp!=0) return comp;

                comp = (o1.getMinor() - o2.getMinor());

                return comp;
            }
        });

        setMakeThisVersionAsProduction(true);
    }

    protected String versionDirectoryOf() {
        return rootPath + VERSION_DIR;
    }

    protected String versionDirectoryOf(Version version) {
        return versionDirectoryOf() + "/" + version.getMajor() + "." + version.getMinor();
    }


    @ServiceMethod(callByContent = true, target=ServiceMethod.TARGET_SELF)
    public void minorVersionUp() throws Exception {
        Version lastVersion = getVersions().get(0);
        lastVersion.setMinor(lastVersion.getMinor() + 1);
        versionUp(lastVersion);
    }


    @ServiceMethod(callByContent = true, target=ServiceMethod.TARGET_SELF)
    public void majorVersionUp() throws Exception {
        Version lastVersion;

        if(getVersions()!=null && getVersions().size() > 0) {
            lastVersion = getVersions().get(0);
        }else{
            lastVersion = new Version();
            lastVersion.setMajor(0);
            lastVersion.setMinor(0);
        }

        lastVersion.setMajor(lastVersion.getMajor() + 1);
        versionUp(lastVersion);

    }

    private void versionUp(Version lastVersion) throws Exception {
        //copy dev to new version.
        IContainer dev = new ContainerResource();
        dev.setPath(rootPath);

        IContainer newVersion = new ContainerResource();
        newVersion.setPath(versionDirectoryOf(lastVersion));

        resourceManager.getStorage().copy(dev, newVersion.getPath());

        if(isMakeThisVersionAsProduction()){
            MetaworksRemoteService.autowire(lastVersion);
            lastVersion.makeAsProduction(this);
        }

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

    protected DefaultResource getProductionVersionInfo() {
        DefaultResource productionInfoXML = new DefaultResource();
        productionInfoXML.setPath(getRootPath() + ".version.info.xml");

        return productionInfoXML;
    }

    public static String getProductionResourcePath(String appName, String resourcePath){

        resourcePath = withoutVersionPath(appName, resourcePath);

        Boolean isDevelopmentTime = (Boolean) TransactionContext.getThreadLocalInstance().getSharedContext("isDevelopmentTime");
        if(isDevelopmentTime!=null && isDevelopmentTime)
            return resourcePath;

        VersionManager versionManager = new VersionManager();
        MetaworksRemoteService.autowire(versionManager);
        versionManager.setRootPath(appName);
        Version productionVersion = versionManager.getProductionVersion();

        if(productionVersion!=null) {
            String versionDirectory = productionVersion.getVersionDirectory(versionManager);

            resourcePath = ("../" + versionDirectory + (resourcePath!=null && resourcePath.length() > 0 ? "/" + resourcePath : ""));
        }else{
            String prefix = appName + "/";
            int wherePrefix = resourcePath.indexOf(prefix);
            if(wherePrefix == 0){
                resourcePath = resourcePath.substring(prefix.length(), resourcePath.length());
            }
        }

        return resourcePath;
    }

    public static String withoutVersionPath(String appName, String resourcePath) {
        String prefix = appName + "/../" + appName + VERSION_DIR;
        int wherePrefix = resourcePath.indexOf(prefix);

        if(wherePrefix == 0){
            resourcePath = resourcePath.substring(prefix.length() + 1, resourcePath.length());
            int whereAfterVersionFolder = resourcePath.indexOf("/");

            resourcePath = resourcePath.substring(whereAfterVersionFolder + 1, resourcePath.length());
        }else {

            prefix = appName + "/";
            wherePrefix = resourcePath.indexOf(prefix);

            if (wherePrefix == 0) {

                resourcePath = resourcePath.substring(prefix.length());
            }
        }

        return resourcePath;
    }
}
