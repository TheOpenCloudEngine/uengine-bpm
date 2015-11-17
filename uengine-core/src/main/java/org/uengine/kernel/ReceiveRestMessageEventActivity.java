package org.uengine.kernel;

/**
 * Created by soo on 2015. 7. 8..
 */
public class ReceiveRestMessageEventActivity extends ReceiveMessageEventActivity{

    String url;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public ReceiveRestMessageEventActivity(){
        this.setName("On Message");
    }
}

