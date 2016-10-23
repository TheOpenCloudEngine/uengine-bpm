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
public interface VersionManager extends ContextAware{

    public static final String VERSION_DIR = "_versions";

    public void load(String appName, String moduleName) throws Exception;

    public List<Version> listVersions() throws Exception;

    public String versionDirectoryOf(Version version);

    public Version getProductionVersion();

    public void makeProductionVersion(Version version) throws Exception;

    public String getLogicalPath(String resourcePath);

    public String getProductionResourcePath(String resourcePath);

    public void setAppName(String appName);

    public void setModuleName(String moduleName);

    String getModuleName();

}
