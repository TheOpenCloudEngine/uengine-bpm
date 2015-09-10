package org.uengine.application;

import org.metaworks.annotation.Face;
import org.metaworks.widget.IFrame;
import org.uengine.uml.model.AttributeInstance;

import java.net.URLEncoder;

/**
 * Created by jangjinyoung on 15. 9. 10..
 */
@Face(ejsPath="") //giving empty value to make it use the default which means "URLApplication.ejs"
public class URLApplication extends ProcessApplication {

    IFrame iFrame;
        public IFrame getiFrame() {
            return iFrame;
        }
        public void setiFrame(IFrame iFrame) {
            this.iFrame = iFrame;
        }

    String url;
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }


    public URLApplication(String url){
        setUrl(url);
    }

    public URLApplication(){
    }


    @Override
    public void onLoad() throws Exception {
        StringBuffer queryString = new StringBuffer();

        for(AttributeInstance attributeInstance : getAttributeInstanceList()){
            queryString
                    .append(attributeInstance.getName())
                    .append("=")
                    .append(attributeInstance.getValue());

        }

        String finalURL = getUrl() + "?" + URLEncoder.encode(queryString.toString(), "UTF-8");

        setiFrame(new IFrame());
        getiFrame().setSrc(finalURL);
    }

    @Override
    public void beforeComplete() throws Exception {

    }

    @Override
    public void afterComplete() throws Exception {

    }
}
