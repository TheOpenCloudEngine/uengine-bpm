package org.uengine.modeling.layout;

import org.uengine.modeling.ElementView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjy on 2015. 11. 25..
 */
public abstract class Layout {

    public Layout(){
        setElementViews(new ArrayList<ElementView>());
    }


    List<ElementView> elementViews;
        public List<ElementView> getElementViews() {
            return elementViews;
        }

        public void setElementViews(List<ElementView> elementViews) {
            this.elementViews = elementViews;
        }

    int startX = 0;
    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    int startY = 0;
    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }


    int canvasW = 400;
    public int getCanvasWidth() {
        return canvasW;
    }
    public void setCanvasWidth(int canvasW) {
        this.canvasW = canvasW;
    }



    int canvasH = 300;

    public int getCanvasHeight() {
        return canvasH;
    }

    public void setCanvasHeight(int canvasH) {
        this.canvasH = canvasH;
    }


    abstract public LayoutGroup layout();


    public LayoutGroup createLayoutGroup(){
        LayoutGroup layoutGroup = new LayoutGroup();
        layoutGroup.setElementViews(getElementViews());

        return layoutGroup;
    }

    public void add(ElementView elementView){
        getElementViews().add(elementView);
    }

    public void add(Layout layout){
        add(layout.layout());
    }
}
