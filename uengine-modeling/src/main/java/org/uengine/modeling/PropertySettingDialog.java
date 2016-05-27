package org.uengine.modeling;

import org.metaworks.*;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;

public class PropertySettingDialog extends ModalWindow{

    private final static int DEFAULT_WIDTH = 600;
    private final static int DEFAULT_HEIGHT = 500;
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

        if(element instanceof IModelingTimeSensitive){
            ((IModelingTimeSensitive) element).onModelingTime();
        }

        if(element instanceof ContextAware){
            ContextAware contextAwaredElement = (ContextAware) element;

            if(contextAwaredElement.getMetaworksContext()==null){
                contextAwaredElement.setMetaworksContext(new MetaworksContext());
            }
            contextAwaredElement.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);

        }

        if(elementView.getPropertyDialogHeight() != 0){
            setHeight(elementView.getPropertyDialogHeight());
        }else{
            setHeight(DEFAULT_HEIGHT);
        }

        if(elementView.getPropertyDialogWidth() != 0){
            setWidth(elementView.getPropertyDialogWidth());
        }else{
            setWidth(DEFAULT_WIDTH);
        }

        setTitle(element.getName());
        setPanel(element);
    }

    @ServiceMethod(callByContent = true, target= ServiceMethodContext.TARGET_APPEND/*, validate = true*/, keyBinding = "Enter")
    public Object apply(){
        IElement element = (IElement)getPanel();

        if(element instanceof IModelingTimeSensitive){
            ((IModelingTimeSensitive) element).afterModelingTime();
        }

        getElementView().setElement(element);

        getElementView().setChanged(true);

        return new Object[]{new Remover(new PropertySettingDialog()), new Refresh(getElementView(), true)};

    }

}
