package org.uengine.kernel;

import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.uengine.processmanager.ProcessManagerRemote;

import javax.ejb.RemoveException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.rmi.RemoteException;

/**
 * Created by jjy on 2016. 7. 27..
 */
@Component
public class ProcessExecutionThread {

    private static long ERROR_LEVEL_TIMEINMS = 20000;

    @Autowired
    @Qualifier("processManagerBeanForQueue")
    ProcessManagerRemote pm;

    @Transactional
    public String run(String[] tracingTagAndInstanceIdArr) throws Exception {

        ProcessInstance instance = null;

        try {
            instance = pm.getProcessInstance(tracingTagAndInstanceIdArr[1]);

        }catch (Exception e){
            Thread.sleep(5000);

            // forward the message
            QueueChannel inputChannel = MetaworksRemoteService.getInstance().getBeanFactory().getBean("inputChannel", QueueChannel.class);

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

                act.executeActivity(instance);

                act.afterExecute(instance);

                pm.applyChanges();


//                break;

            } catch (Exception e) {
                e.printStackTrace();
                //throw e;
            }
 //       }

        return null;

    }

}




