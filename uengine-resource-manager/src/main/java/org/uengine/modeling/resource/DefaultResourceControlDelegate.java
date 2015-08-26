package org.uengine.modeling.resource;

/**
 * Created by jangjinyoung on 15. 7. 23..
 */
public class DefaultResourceControlDelegate implements ResourceControlDelegate{


    @Override
    public void onDoubleClicked(IResource resource) {

        if(resource instanceof DefaultResource){
            try {
                ((DefaultResource) resource)._newAndOpen(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClicked(IResource resource) {

    }
}
