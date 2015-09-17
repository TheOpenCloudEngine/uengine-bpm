package org.uengine.application;

/**
 * Created by jangjinyoung on 15. 9. 10..
 */
public class URLApplicationDefinition extends ProcessApplicationDefinition{

    String url;
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }


    @Override
    protected ProcessApplication newObjectInstance() {
        return new URLApplication(getUrl());
    }


}
