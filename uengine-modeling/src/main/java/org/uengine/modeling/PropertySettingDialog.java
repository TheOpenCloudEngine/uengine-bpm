package org.uengine.modeling;

import org.metaworks.*;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;
import org.uengine.kernel.LeveledException;
import org.uengine.kernel.Validatable;
import org.uengine.kernel.ValidationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.metaworks.dwr.MetaworksRemoteService.wrapReturn;

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

        MetaworksRemoteService.autowire(element);

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

//        PropertySettingPanel propertySettingPanel = new PropertySettingPanel();
//        propertySettingPanel.setElement(element);

        setPanel(element);

        if(element instanceof Validatable){
            ValidationContext validationContext = ((Validatable) element).validate(new HashMap());

            List<LeveledException> exceptions = new ArrayList<LeveledException>();

            if(validationContext.size() > 0){

                exceptions.addAll(validationContext);

                setValidationPanel(new ValidationPanel());

                getValidationPanel().setExceptions(exceptions);
            }

        }

    }

    ValidationPanel validationPanel;
        public ValidationPanel getValidationPanel() {
            return validationPanel;
        }
        public void setValidationPanel(ValidationPanel validationPanel) {
            this.validationPanel = validationPanel;
        }


    @ServiceMethod(payload={"panel", "elementView"}, target= ServiceMethodContext.TARGET_APPEND/*, validate = true*/, keyBinding = "Enter")
    public Object apply(){
        IElement element = (IElement)getPanel();

        if(element instanceof IModelingTimeSensitive){
            ((IModelingTimeSensitive) element).afterModelingTime();
        }

        getElementView().setElement(element);

        if(element instanceof Validatable){
            ValidationContext validationContext = ((Validatable) element).validate(new HashMap());

            List<LeveledException> exceptions = new ArrayList<LeveledException>();

            if(element instanceof IIntegrityElement) {

                if (validationContext.size() > 0) {
                    ((IIntegrityElement) element).setIntegrity(1);
                } else {
                    ((IIntegrityElement) element).setIntegrity(0);
                }
            }

        }

        getElementView().setChanged(true);

        return new Object[]{new Remover(new PropertySettingDialog()), new Refresh(getElementView(), true)};

    }

}
