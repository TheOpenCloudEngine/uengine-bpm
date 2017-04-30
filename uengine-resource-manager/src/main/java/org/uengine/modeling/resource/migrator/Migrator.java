package org.uengine.modeling.resource.migrator;

import org.metaworks.dao.TransactionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.uengine.modeling.resource.ContainerResource;
import org.uengine.modeling.resource.IContainer;
import org.uengine.modeling.resource.IResource;
import org.uengine.modeling.resource.ResourceManager;

import java.util.List;

/**
 * Created by uengine on 2017. 4. 4..
 */
public class Migrator {

    ResourceManager sourceResourceManager;

    ResourceManager targetResourceManager;

    public ResourceManager getSourceResourceManager() {
        return sourceResourceManager;
    }

    public void setSourceResourceManager(ResourceManager sourceResourceManager) {
        this.sourceResourceManager = sourceResourceManager;
    }

    public ResourceManager getTargetResourceManager() {
        return targetResourceManager;
    }

    public void setTargetResourceManager(ResourceManager targetResourceManager) {
        this.targetResourceManager = targetResourceManager;
    }

    public void run(ContainerResource parent) throws Exception {

        List<IResource> resourceList = getSourceResourceManager().listFiles(parent);

        for(IResource resource : resourceList){

            try{
                System.out.print(" copying " + resource.getPath() + " ...");

                if(resource instanceof ContainerResource){
                    getTargetResourceManager().createFolder((IContainer) resource);
                    run((ContainerResource) resource);
                }else{
                    Object sourceObject = getSourceResourceManager().getObject(resource);
                    getTargetResourceManager().save(resource, sourceObject);
                }

                System.out.println(" DONE.");

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void run() throws Exception{
        ContainerResource root = new ContainerResource();
        root.setPath("");
        run(root);
    }

    public static void main(String... args) throws Exception{

        new TransactionContext();

        if(args.length == 0){
            System.out.println(" USAGE:  mvn exec:java -Dexec.mainClass=\"org.uengine.modeling.resource.migrator.Migrator\" -Dexec.args=\"context.xml\"");
        }

        ApplicationContext applicationContext = new FileSystemXmlApplicationContext(args[0]);
        Migrator migrator = applicationContext.getBean(Migrator.class);

        migrator.run();

    }
}
