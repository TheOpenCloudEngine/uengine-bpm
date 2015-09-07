package org.uengine.kernel.face;

import com.itextpdf.text.Meta;
import org.metaworks.ContextAware;
import org.metaworks.Face;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.NonEditable;
import org.metaworks.annotation.Range;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.contexts.ComplexType;
import org.uengine.modeling.resource.ResourceNavigator;
import org.uengine.modeling.resource.SelectedResource;
import org.uengine.processadmin.ProcessAdminResourceNavigator;
import org.uengine.processadmin.ResourceControlDelegateForProcessVariableSelector;

/**
 * Created by jangjinyoung on 15. 7. 18..
 */
@Component
@Scope("prototype")
public class CodiProcessVariableTypeSelector extends ProcessVariableTypeSelector implements ContextAware{

    String type;
    @Range(options={"Text","Number", "Date","Complex"}, values={"java.lang.String","java.lang.Long", "java.util.Date","org.uengine.contexts.ComplexType"})
        public String getType() {
            return type;
        }
        public void setType(String primitypeTypeName) {
            this.type = primitypeTypeName;
        }


//    ProcessAdminContainerResource classRoot;
//    //@Available(condition = "primitypeTypeName == 'org.uengine.contexts.ComplexType'")
//        public ProcessAdminContainerResource getClassRoot() {
//            return classRoot;
//        }
//        public void setClassRoot(ProcessAdminContainerResource classRoot) {
//            this.classRoot = classRoot;
//        }

    ResourceNavigator classResourceNavigator;
        public ResourceNavigator getClassResourceNavigator() {
            return classResourceNavigator;
        }
        public void setClassResourceNavigator(ResourceNavigator classResourceNavigator) {
            this.classResourceNavigator = classResourceNavigator;
        }

    String selectedClassName;
    @NonEditable
        public String getSelectedClassName() {
            return selectedClassName;
        }
        public void setSelectedClassName(String selectedClassName) {
            this.selectedClassName = selectedClassName;
        }


    MetaworksContext metaworksContext;
        @Override
        public MetaworksContext getMetaworksContext() {
            return metaworksContext;
        }
        @Override
        public void setMetaworksContext(MetaworksContext metaworksContext) {
            this.metaworksContext = metaworksContext;
        }



    @ServiceMethod(callByContent = true, eventBinding = "change", bindingFor = "primitypeTypeName")
    public void select(@AutowiredFromClient SelectedResource selectedComplexClassResource){

        if(ComplexType.class.getName().equals(getType())){
            if(selectedComplexClassResource!=null)
                setSelectedClassName(selectedComplexClassResource.getPath());
        }else{

            setSelectedClassName(getType());
        }

        setMetaworksContext(new MetaworksContext());
        getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);

    }


    public CodiProcessVariableTypeSelector(){

        ProcessAdminResourceNavigator classResourceNavigator = new ProcessAdminResourceNavigator();

        MetaworksRemoteService.autowire(classResourceNavigator);

        classResourceNavigator.setResourceControlDelegate(new ResourceControlDelegateForProcessVariableSelector());

        setClassResourceNavigator(classResourceNavigator);

    }


    @Override
    public void setValueToFace(String value) {
        setSelectedClassName(value);

        if(value!=null && value.indexOf(".") > 0){
            setType("org.uengine.contexts.ComplexType");
        }
    }



    @Override
    public String createValueFromFace() {

        return getSelectedClassName();

    }
}
