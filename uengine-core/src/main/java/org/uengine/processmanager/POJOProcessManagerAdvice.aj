package org.uengine.processmanager;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Hashtable;


@Aspect
public class POJOProcessManagerAdvice {

    static Hashtable<String, String> mappingTable = new Hashtable<String, String>();

    @Autowired
    ProcessManagerRemote pm;

    @AfterReturning(value = "@annotation(processExecute)", returning = "returnValue")
    public void processExecute(ProcessExecute processExecute, JoinPoint joinPoint, Object returnValue) throws RemoteException {
        String instId = pm.initializeProcess(processExecute.value());

        if(returnValue==null){
            throw new RuntimeException("Return value must be not null for correlation of instanceId");
        }

        mappingTable.put(returnValue.toString(), instId);
    }


    @AfterReturning("@annotation(stepProceed)")
    public void stepProceed(StepProceed stepProceed, JoinPoint joinPoint) throws RemoteException {

        Object correlationKey = joinPoint.getArgs()[stepProceed.correlationKeyOrder()];

        String instId = mappingTable.get(correlationKey);

        pm.completeWorkitem(instId, stepProceed.value(), null, null);

    }

}
