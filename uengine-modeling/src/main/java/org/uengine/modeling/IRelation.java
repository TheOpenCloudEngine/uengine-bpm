package org.uengine.modeling;

import org.metaworks.annotation.Hidden;

/**
 * @author jyj
 */
public interface IRelation {

    @Hidden
    IElement getSourceElement();

    @Hidden
    IElement getTargetElement();

    RelationView createView();

    /**
     * convert main object from IRelation to RelationView
     *
     * @return RelationView which have this class as relation
     */
    RelationView asView();

    RelationView getRelationView();

    void setRelationView(RelationView relationView);
//	String getFromTerminalId();
//	String getToTerminalId();


}
