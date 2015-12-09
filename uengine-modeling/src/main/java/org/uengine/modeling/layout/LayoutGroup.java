package org.uengine.modeling.layout;

import org.uengine.modeling.ElementView;
import org.uengine.modeling.Symbol;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjy on 2015. 11. 25..
 */
public class LayoutGroup extends ElementView{
    @Override
    public Symbol createSymbol() {
        return null;
    }

    List<ElementView> elementViews = new ArrayList<ElementView>();
        public List<ElementView> getElementViews() {
            return elementViews;
        }
        public void setElementViews(List<ElementView> elementViews) {
            this.elementViews = elementViews;
        }



    @Override
    public void setX(double x) {

        double diffX = x - getX();

        for(ElementView elementView : getElementViews()){
            elementView.setX(elementView.getX() + diffX);
        }

        super.setX(x);
    }

    @Override
    public void setY(double y) {
        double diffY = y - getY();

        for(ElementView elementView : getElementViews()){
            elementView.setY(elementView.getY() + diffY);
        }

        super.setY(y);
    }

    @Override
    public double getWidth() {

        return getDimension().getWidth();
    }

    @Override
    public double getHeight() {
        return getDimension().getHeight();
    }


    private Dimension getDimension(){

        if(getElementViews()==null  || getElementViews().size()==0)
            return new Dimension(0,0);

        double minX = 500000, minY = 500000, maxX = 0, maxY = 0;

        Dimension dimension = new Dimension();

        for(ElementView elementView : getElementViews()){

            double elemX = elementView.getX() - elementView.getWidth() / 2;
            double elemY = elementView.getY() - elementView.getHeight() / 2;
            double elemX_ = elementView.getX() + (elementView.getWidth()) / 2;;
            double elemY_ = elementView.getY() + (elementView.getHeight()) / 2;;


            if(minX > elemX) minX = elemX;
            if(minY > elemY) minY = elemY;
            if(maxX < elemX_) maxX = elemX_;
            if(maxY < elemY_) maxY = elemY_;
        }
        dimension.setSize(maxX-minX, maxY-minY);

        return dimension;
    }
}
