package org.uengine.modeling;

/**
 * @author jyj
 */
public interface IElement {
    ElementView createView();

    ElementView getElementView();

    void setElementView(ElementView elementView);

    String getName();

    String getDescription();

}