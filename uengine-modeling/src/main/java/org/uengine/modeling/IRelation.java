package org.uengine.modeling;

/**
 * @author jyj
 */
public interface IRelation {

    IElement getSourceElement();

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
