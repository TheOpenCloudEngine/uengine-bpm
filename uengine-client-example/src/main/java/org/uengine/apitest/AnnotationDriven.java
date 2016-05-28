package org.uengine.apitest;

import org.uengine.processmanager.ProcessExecute;
import org.uengine.processmanager.StepProceed;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Created by jjy on 2016. 5. 25..
 */
public class AnnotationDriven {


    @ProcessExecute("vacationRequest")
    @Path("/vacation")
    @POST
    public String vacationRequest(){

        String vacationRequestId = "1";
        //....

        return vacationRequestId;
    }

    @StepProceed(value = "step1", correlationKeyOrder = 1)
    @Path("/vacation/approve")
    @POST
    public void vacationApprove(@FormParam("vacationRequestId") String vacationRequestId){
        //....
    }


}
