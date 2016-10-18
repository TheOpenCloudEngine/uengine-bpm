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
public class PackageVersionManager extends VersionManager{

    @Override
    protected String versionDirectoryOf() {

        String[] pathElements = getRootPath().split("/");

        String appName = pathElements[0];
        String packageName = pathElements[1];

        String versionPath = appName + VERSION_DIR + "/" + packageName;

        return versionPath;
    }


}
