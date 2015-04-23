package org.uengine.modeling;

import org.metaworks.*;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;

public class PropertySettingDialog extends ModalWindow{


//
//    IElement element;
//        public IElement getElement() {
//            return element;
//        }
//        public void setElement(IElement element) {
//            this.element = element;
//        }
//

    ElementView elementView;
    @Hidden
        public ElementView getElementView() {
            return elementView;
        }
        public void setElementView(ElementView elementView) {
            this.elementView = elementView;
        }




    public PropertySettingDialog(){
    }

    public PropertySettingDialog(ElementView elementView){
        setElementView(elementView);

        IElement element = elementView.getElement();

        if(element instanceof ContextAware){
            ContextAware contextAwaredElement = (ContextAware) element;

            if(contextAwaredElement.getMetaworksContext()==null){
                contextAwaredElement.setMetaworksContext(new MetaworksContext());
            }
            contextAwaredElement.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);

        }

        setPanel(element);
    }

    @ServiceMethod(callByContent = true, target= ServiceMethodContext.TARGET_APPEND)
    public Object apply(){
        IElement element = (IElement)getPanel();

        getElementView().setElement(element);

        return new Object[]{new Remover(new PropertySettingDialog()), new Refresh(getElementView(), true)};

    }

}
