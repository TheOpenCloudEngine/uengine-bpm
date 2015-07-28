package org.uengine.modeling.resource;

import org.metaworks.MetaworksFile;
import org.oce.garuda.multitenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
@Component
public class ResourceManager {

    @Autowired
    Storage storage;

        public Storage getStorage() {
            return storage;
        }

        public void setStorage(Storage storage) {
            this.storage = storage;
        }



}
