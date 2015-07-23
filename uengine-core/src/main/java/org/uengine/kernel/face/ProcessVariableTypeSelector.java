package org.uengine.kernel.face;

import org.metaworks.Face;
import org.metaworks.annotation.*;
import org.metaworks.component.SelectBox;
import org.uengine.contexts.ComplexType;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.ResourceNavigator;
import org.uengine.modeling.resource.SelectedResource;
import processadmin.ProcessAdminContainerResource;
import processadmin.ProcessAdminResourceNavigator;

import java.util.ArrayList;

/**
 * Created by jangjinyoung on 15. 7. 18..
 */
public class ProcessVariableTypeSelector implements Face<String> {

    String primitypeTypeName;
    @Range(options={"Text","Number", "Date","Complex"}, values={"java.lang.String","java.lang.Long", "java.util.Date","org.uengine.contexts.ComplexType"})
        public String getPrimitypeTypeName() {
            return primitypeTypeName;
        }
        public void setPrimitypeTypeName(String primitypeTypeName) {
            this.primitypeTypeName = primitypeTypeName;
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


    @ServiceMethod(callByContent = true)
    public void select(@AutowiredFromClient SelectedResource selectedComplexClassResource){

        if(ComplexType.class.getName().equals(getPrimitypeTypeName())){
            if(selectedComplexClassResource!=null)
                setSelectedClassName(selectedComplexClassResource.getPath());
        }else{

            setSelectedClassName(getPrimitypeTypeName());
        }



    }


    public ProcessVariableTypeSelector(){

        setClassResourceNavigator(new ProcessAdminResourceNavigator());

    }


    @Override
    public void setValueToFace(String value) {
//        setOptionNames(new ArrayList<String>());
//        getOptionNames().add("Text");
//        getOptionNames().add("Number");
//        getOptionNames().add("Date");



    }



    @Override
    public String createValueFromFace() {

        return getSelectedClassName();

    }
}
