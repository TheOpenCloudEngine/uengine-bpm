package org.uengine.modeling.resource;

import com.thoughtworks.xstream.XStream;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.uengine.modeling.resource.resources.ProcessResource;

import java.util.List;

/**
 * Created by hoo.lim on 7/21/2015.
 */
public class AmazonS3StorageTest {
    static ApplicationContext ctx;


    @BeforeClass
    public static void beforeMethod(){
        ctx =  new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    @Ignore
    @Test
    public void testSave() throws Exception {
        AmazonS3Storage amazonS3Storage = (AmazonS3Storage) ctx.getBean("storage");
        // resource object contain file location and others.
        ProcessResource processResource = new ProcessResource();
        processResource.setPath("practice/testProcess.process");

        // sample object to save in aws s3
        EditorPanel editorPanel = new EditorPanel();
        editorPanel.setResourcePath("???");
        editorPanel.setNew(true);

        amazonS3Storage.save(processResource,editorPanel);
    }

    @Ignore
    @Test
    public void testDelete(){
        AmazonS3Storage amazonS3Storage = (AmazonS3Storage) ctx.getBean("storage");

        // resource object contain file location and others.
        ProcessResource processResource = new ProcessResource();
        processResource.setPath("practice/testProcess.process");

        amazonS3Storage.delete(processResource);
    }

    @Ignore
    @Test
    public void testRename(){
        AmazonS3Storage amazonS3Storage = (AmazonS3Storage) ctx.getBean("storage");

        // resource object contain file location and others.
        ProcessResource processResource = new ProcessResource();
        processResource.setPath("practice/testProcess.process");

        amazonS3Storage.rename(processResource,"practice/newProcess.process");
    }

    @Ignore
    @Test
    public void testGetObject() throws Exception {
        AmazonS3Storage amazonS3Storage = (AmazonS3Storage) ctx.getBean("storage");

        // resource object contain file location and others.
        ProcessResource processResource = new ProcessResource();
        processResource.setPath("practice/testProcess.process");

        EditorPanel editorPanel = (EditorPanel) amazonS3Storage.getObject(processResource);

        XStream xStream = new XStream();
        xStream.toXML(editorPanel,System.out);
    }

    @Ignore
    @Test
    public void testListFile() throws Exception {
        AmazonS3Storage amazonS3Storage = (AmazonS3Storage) ctx.getBean("storage");

        List<IResource> resourceList = amazonS3Storage.listFiles(null);

        for(IResource resource : resourceList){
            XStream xStream = new XStream();
            xStream.toXML(resource,System.out);
        }
    }
}
