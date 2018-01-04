package org.uengine.modeling.cnv.layout;

import org.uengine.modeling.ElementView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjy on 2015. 11. 25..
 */
public abstract class CnvLayout {

    public CnvLayout(){
        setElementViews(new ArrayList<ElementView>());
    }

    List<ElementView> elementViews;
        public List<ElementView> getElementViews() {
            return elementViews;
        }

        public void setElementViews(List<ElementView> elementViews) {
            this.elementViews = elementViews;
        }

    int startX = 150;
    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    int startY = 50;
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


    abstract public CnvLayoutGroup layout();

    public CnvLayoutGroup createLayoutGroup(){
        CnvLayoutGroup layoutGroup = new CnvLayoutGroup();
        double x = 0, y = 0;
        for(ElementView view : getElementViews()){
            if( x == 0 || ( x > 0 && x > view.getX() ) ){
                x = view.getX();
            }
            if( y == 0 || ( y > 0 && y > view.getY() ) ){
                y = view.getY();
            }
        }
        layoutGroup.setX(x);
        layoutGroup.setY(y);
        layoutGroup.setElementViews(getElementViews());

        if( this instanceof CnvGridLayout ){
            layoutGroup.setLayoutGroupType(layoutGroup.getGRID_TYPE());
        }else{
            layoutGroup.setLayoutGroupType(layoutGroup.getFLOW_TYPE());
        }
        return layoutGroup;
    }

    public void add(ElementView elementView){
        getElementViews().add(elementView);
    }

    public void add(CnvLayout layout){
        add(layout.layout());
    }

    public String getElemnetId(int pos){
        String rv = null;
        int cnt = 0;
        for(ElementView elementView : getElementViews()){
            if( ++cnt == pos ){
                if( elementView instanceof CnvLayoutGroup){
                    rv = ((CnvLayoutGroup) elementView).getElementId(pos);
                } else {
                    rv = elementView.getId();
                }
            }
            cnt++;
        }
        return rv;
    }

    //첫번째 그룹의 마지막 엘리먼트ID
    public String getLastElementIdInFirstGroup(){
        String rv = null;
        int size = getElementViews().size();
        ElementView elementView = getElementViews().get(size-1);
        if( elementView instanceof CnvLayoutGroup ){
            rv = ((CnvLayoutGroup) elementView).getLastElementIdInFirstGroup();
        }else{
            rv = elementView.getId();
        }
        return rv;
    }

}
