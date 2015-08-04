package org.uengine.modeling.resource.resources;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.uengine.modeling.resource.AmazonS3Storage;
import org.uengine.modeling.resource.EditorPanel;

/**
 * Created by hoo.lim on 7/23/2015.
 */
public class ProcessResourceTest {
    static ApplicationContext ctx;

    @BeforeClass
    public static void beforeMethod(){
        ctx =  new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    @Test
    public void testSave() throws Exception {
        // resource object contain file location and others.
        ProcessResource processResource = (ProcessResource) ctx.getBean("ProcessResource");
        processResource.setPath("practice/testProcess.process");

        // sample object to save in aws s3
        EditorPanel editorPanel = new EditorPanel();
        editorPanel.setResourcePath("테스트 케이스");
        editorPanel.setIsNew(true);

        processResource.save(editorPanel);
    }

    @Ignore
    @Test
    public void testDelete(){
        // resource object contain file location and others.
        ProcessResource processResource = (ProcessResource) ctx.getBean("ProcessResource");
        processResource.setPath("practice/testProcess.process");

        processResource.delete();
    }

    @Ignore
    @Test
    public void testRename(){
        // resource object contain file location and others.
        ProcessResource processResource = (ProcessResource) ctx.getBean("ProcessResource");
        processResource.setPath("practice/testProcess.process");

        processResource.rename("practice/newProcess.process");
    }
}
