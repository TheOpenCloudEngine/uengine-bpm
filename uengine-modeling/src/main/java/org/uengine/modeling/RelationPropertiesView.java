package org.uengine.modeling;

import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;

public class RelationPropertiesView extends ModalWindow {

    RelationView relationView;

    public RelationView getRelationView() {
        return relationView;
    }

    public void setRelationView(RelationView relationView) {
        this.relationView = relationView;
    }

    public RelationPropertiesView() {
    }

    public RelationPropertiesView(RelationView relationView) {
        this.setRelationView(relationView);
        setPanel(this.getRelationView().getRelation());
    }

    @ServiceMethod(callByContent = true, target= ServiceMethodContext.TARGET_APPEND)
    public Object apply(){
        IRelation relation = (IRelation)getPanel();

        getRelationView().setRelation(relation);

        return new Object[]{new Remover(new PropertySettingDialog()), new Refresh(getRelationView(), true)};

    }
}
