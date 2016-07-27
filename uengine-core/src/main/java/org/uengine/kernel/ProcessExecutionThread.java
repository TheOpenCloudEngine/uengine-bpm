package org.uengine.kernel;

import org.springframework.beans.factory.annotation.Autowired;
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
public class ProcessExecutionThread extends Thread {

    private static long ERROR_LEVEL_TIMEINMS = 20000;

    @Autowired
    ProcessManagerRemote pm;

    Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    ProcessInstance finalInstance;

    public ProcessInstance getFinalInstance() {
        return finalInstance;
    }

    public void setFinalInstance(ProcessInstance finalInstance) {
        this.finalInstance = finalInstance;
    }


    public void run() {
        Activity act = getActivity();

        boolean success = false;
        int maxRetry = (act.isQueuingEnabled() ? act.getRetryLimit() : 1);
        for (int retCnt = -1; !success && retCnt < maxRetry; retCnt++) {
            if (retCnt > 0)
                try {
                    sleep(act.getRetryDelay() * 1000);
                } catch (InterruptedException e5) {
                    e5.printStackTrace();
                }

            final boolean isRetrying = (retCnt > 0 && retCnt < maxRetry - 1);

            ProcessInstance instance = null;

            try {

                if (act.isQueuingEnabled()) {
                    instance = pm.getProcessInstance(instanceId);
                } else {
                    instance = finalInstance;
                }

                long timeInMillis_start = System.currentTimeMillis();

                System.out.println("- [uEngine] Start Executing Activity: " + act.getName() + " (" + act.getTracingTag() + ")");


                instance.execute(act.getTracingTag());

                long elapsedTime = (System.currentTimeMillis() - timeInMillis_start);

                PrintStream logWriter = (elapsedTime < ERROR_LEVEL_TIMEINMS ? System.out : System.err);

                logWriter.println("- [uEngine] End Executing Activity: " + act.getName() + " (" + act.getTracingTag() + ") - Elapsed Time : " + elapsedTime);

                success = true;

            } catch (Exception e) {

                UEngineException ue = null;
                if (!(e instanceof UEngineException)) {
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    e.printStackTrace(new PrintStream(bao));
                    try {
                        ue = new UEngineException("uEngine Exception: " + e + "(" + e.getMessage() + ")", e);
                        ue.setDetails(bao.toString());
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }

                } else {
                    ue = (UEngineException) e;

                    final UEngineException finalUE = ue;
                }


                if (!act.isQueuingEnabled() && instance.getProcessTransactionContext().getSharedContext("faultTolerant") == null) {

                    UEngineException richException = new UEngineException(e.getMessage(), null, e, instance, act);
                    throw new RuntimeException(richException);
                }

            }
        }//end of try
    }//end of for-loop

    @Transactional
    public synchronized void start() {
        super.start();
    }
}




