package org.uengine.kernel;

import org.metaworks.dwr.MetaworksRemoteService;
import org.oce.garuda.multitenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.uengine.processmanager.ProcessManagerRemote;
import org.uengine.util.UEngineUtil;

import javax.ejb.RemoveException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.rmi.RemoteException;

/**
 * Created by jjy on 2016. 7. 27..
 */
@Component
@Scope("prototype")
public class ProcessExecutionThread {

    private static long ERROR_LEVEL_TIMEINMS = 20000;

//    @Autowired
//    @Qualifier("processManagerBeanForQueue")
//    ProcessManagerRemote pm;

    @Transactional
    public String run(String[] tracingTagAndInstanceIdArr) throws Exception {

        ProcessManagerRemote pm = null; //MetaworksRemoteService.getComponent(ProcessManagerRemote.class);
        ProcessInstance instance = null;

        try {
            pm = (ProcessManagerRemote) org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(org.directwebremoting.ServerContextFactory.get().getServletContext()).getBean("processManagerBeanForQueue");
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }

        try {
            instance = pm.getProcessInstance(tracingTagAndInstanceIdArr[1]);

        }catch (Exception e){
            Thread.sleep(5000);

            // forward the message
            ExecutorChannel inputChannel = MetaworksRemoteService.getInstance().getBeanFactory().getBean("inputChannelFor" + getClass().getSimpleName(), ExecutorChannel.class);

            int count = 0;

            if(tracingTagAndInstanceIdArr.length > 2){
                count = Integer.parseInt(tracingTagAndInstanceIdArr[2]) + 1;
            }

            if(count > 2){
                throw new Exception("instance [" + tracingTagAndInstanceIdArr[1] + "] is not initialized.");
            }

            inputChannel.send(new GenericMessage<String[]>(new String[]{tracingTagAndInstanceIdArr[0], tracingTagAndInstanceIdArr[1], ""+ count} ));

            return null;
        }


        if(instance == null){
            throw new Exception("instance [" + tracingTagAndInstanceIdArr[1] + "] is not initialized.");
        }

            try {

                Activity act = instance.getProcessDefinition().getActivity(tracingTagAndInstanceIdArr[0]);

                //set the tenant id
                if(instance.getRootProcessInstance().getLocalInstance() instanceof EJBProcessInstance) {
                    EJBProcessInstance ejbProcessInstance = (EJBProcessInstance) instance.getRootProcessInstance().getLocalInstance();
                    String initCompanyCode = (String)ejbProcessInstance.getProcessInstanceDAO().getInitComCd();
                    if (initCompanyCode != null) {
                        new TenantContext(initCompanyCode);
                    }
                }

                logic(instance, act, tracingTagAndInstanceIdArr);

                pm.applyChanges();


//                break;

            } catch (Exception e) {
                e.printStackTrace();

                (new FaultMarker()).queue(instance.getInstanceId(), tracingTagAndInstanceIdArr[0], 0, new String[]{e.getMessage()});
            }
 //       }

        return null;

    }

    protected void logic(ProcessInstance instance, Activity act, String[] parameters) throws Exception {
        if(!instance.getRootProcessInstance().isRunning(""))
            return; // skip if the instance is stopped or completed
        if(!instance.isRunning(""))
            return; // skip if the instance is stopped or completed

        act.executeActivity(instance);
        act.afterExecute(instance);
    }


    public void queue(String instanceId, String tracingTag, int retryingCount, String[] additionalParameters){

        ExecutorChannel inputChannel = MetaworksRemoteService.getInstance().getBeanFactory().getBean("inputChannelFor" + getClass().getSimpleName(), ExecutorChannel.class);

        String[] newArray;
        if(additionalParameters!=null && additionalParameters.length>0){
            newArray = additionalParameters;
        }else{
            newArray = new String[]{};
        }


        newArray = (String[]) UEngineUtil.addArrayElementAtFirst(newArray, ""+retryingCount);
        newArray = (String[]) UEngineUtil.addArrayElementAtFirst(newArray, instanceId);
        newArray = (String[]) UEngineUtil.addArrayElementAtFirst(newArray, tracingTag);

        inputChannel.send(new GenericMessage<String[]>(newArray));
    }


    public void queue(String instanceId, String tracingTag){

       queue(instanceId, tracingTag, 0, null);
    }




}




