package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.model.TServiceTask;
import org.omg.spec.bpmn._20100524.model.TTask;
import org.uengine.kernel.Activity;
import org.uengine.kernel.DefaultActivity;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.WebServiceActivity;

import java.util.Hashtable;

public class TServiceTaskAdapter extends TTaskAdapter{

    public static final String SERVICE_IMPL_JAVA = "java:";

    @Override
    protected Activity create(TTask src, Hashtable keyedContext) {
        TServiceTask serviceTask = (TServiceTask) src;

        //in case of Java Activity
        if(serviceTask.getImplementation()!=null && serviceTask.getImplementation().startsWith(SERVICE_IMPL_JAVA)){
            String className = serviceTask.getImplementation().substring(SERVICE_IMPL_JAVA.length());

            try {
                Class theActivityClass = Thread.currentThread().getContextClassLoader().loadClass(className);
                if(Activity.class.isAssignableFrom(theActivityClass)){
                    return (Activity) theActivityClass.newInstance();
                }else{
                    throw new RuntimeException(className + " is not an Activity");
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("No such activity class : " + className, e);

            } catch (InstantiationException e) {
                throw new RuntimeException("Activity class : " + className + " is not initialized.", e);

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Activity class : " + className + " is not initialized.", e);
            }

        } else{
            return new DefaultActivity(src.getName());
        }

        //return new WebServiceActivity();
    }

//    @Override
//    public Activity convert(TTask src, Hashtable keyedContext) throws Exception {
//        WebServiceActivity webServiceActivity = (WebServiceActivity) super.convert(src, keyedContext);
//
//
//       // src.getDataInputAssociation()
//
//
//        return webServiceActivity;
//    }


}
