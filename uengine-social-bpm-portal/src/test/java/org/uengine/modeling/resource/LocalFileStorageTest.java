package org.uengine.modeling.resource;

import com.thoughtworks.xstream.XStream;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.metaworks.dao.TransactionAdvice;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.modeling.resource.resources.ProcessResource;

import java.util.List;

/**
 * Created by jjy
 */
public class LocalFileStorageTest {


    LocalFileStorage localFileStorage;
    ResourceManager resourceManager;

    @Before
    public void initialize() throws Exception{
        new TransactionAdvice().initiateTransaction();

        localFileStorage = new LocalFileStorage();
        localFileStorage.setDoNotOverwrite(false);
        localFileStorage.setLocalBasePath("./target/resource_framework_test_disposable");

        resourceManager = new ResourceManager();
        resourceManager.setStorage(localFileStorage);

    }

    @Test
    public void testSave() throws Exception {

        // resource object contain file location and others.
        ProcessResource processResource = new ProcessResource();
        processResource.setPath("practice/testProcess.process");

        resourceManager.save(processResource, new ProcessDefinition());
    }

    @Test
    public void testCopy() throws Exception {
        ProcessResource processResource = new ProcessResource();
        processResource.setPath("practice/testProcess.process");

        resourceManager.copy(processResource, "practice/testProcess_copy.process");
    }
}
