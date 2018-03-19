package org.uengine.kernel;

public class MessageFlow extends AbstractFlow {

    boolean localCall;
        public boolean isLocalCall() {
            return localCall;
        }
        public void setLocalCall(boolean localCall) {
            this.localCall = localCall;
        }

}
