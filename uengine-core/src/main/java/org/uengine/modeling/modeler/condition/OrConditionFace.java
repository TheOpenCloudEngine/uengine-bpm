package org.uengine.modeling.modeler.condition;

import org.metaworks.Face;
import org.metaworks.annotation.Children;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Name;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.kernel.Condition;
import org.uengine.kernel.Evaluate;
import org.uengine.kernel.Or;

import java.util.ArrayList;
import java.util.List;


@org.metaworks.annotation.Face(ejsPath="dwr/metaworks/genericfaces/TreeFace.ejs")
public class OrConditionFace implements Face<Or> {

    String name;
    @Name
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }


    List<Condition> childs;
    @Children
        public List<Condition> getChilds() {
            return childs;
        }
        public void setChilds(List<Condition> childs) {
            this.childs = childs;
        }

    Or or;
    @Hidden
        public Or getOr() {
            return or;
        }
        public void setOr(Or or) {
            this.or = or;
        }


    @ServiceMethod(inContextMenu = true, callByContent = true)
    public void newAnd(){
        if(getChilds()==null)
            setChilds(new ArrayList<Condition>());

        //getChilds().add(new Or());

        getChilds().add(new Evaluate());
    }


    @Override
    public void setValueToFace(Or value) {
        or =  value;

        setName("OR");
    }

    @Override
    public Or createValueFromFace() {
        return or;
    }
}
