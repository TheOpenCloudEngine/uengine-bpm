package org.uengine.kernel;

/**
 * Created by soo on 2015. 7. 8..
 */
public class ReceiveMessageEventActivity extends EventActivity implements MessageListener {
    public ReceiveMessageEventActivity(){
        super();
        if( this.getName() == null ){
            setName(this.getClass().getSimpleName());
        }
    }
    @Override
    protected void executeActivity(ProcessInstance instance) throws Exception {
        //start listens...
    }

    @Override
    public boolean onMessage(ProcessInstance instance, Object payload)
            throws Exception {
        fireComplete(instance);
        return true;
    }

    @Override
    public String getMessage() {
        return this.getName();
    }

    @Override
    public void beforeRegistered(ProcessInstance instance) throws Exception {

    }

    @Override
    public void afterRegistered(ProcessInstance instance) throws Exception {

    }

    @Override
    public void afterUnregistered(ProcessInstance instance) throws Exception {

    }
}
