package org.uengine.kernel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

        for(int i=0; i<10; i++) {/// retry 10 times if fails

            Thread.sleep(2000);


            try {
                ProcessInstance instance = pm.getProcessInstance(tracingTagAndInstanceIdArr[1]);
                Activity act = instance.getProcessDefinition().getActivity(tracingTagAndInstanceIdArr[0]);

                long timeInMillis_start = System.currentTimeMillis();

                System.out.println("- [uEngine] Start Executing Activity: " + act.getName() + " (" + act.getTracingTag() + ")");


                instance.execute(act.getTracingTag());

                long elapsedTime = (System.currentTimeMillis() - timeInMillis_start);

                PrintStream logWriter = (elapsedTime < ERROR_LEVEL_TIMEINMS ? System.out : System.err);

                logWriter.println("- [uEngine] End Executing Activity: " + act.getName() + " (" + act.getTracingTag() + ") - Elapsed Time : " + elapsedTime);

                break;

            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        return null;

    }

}




