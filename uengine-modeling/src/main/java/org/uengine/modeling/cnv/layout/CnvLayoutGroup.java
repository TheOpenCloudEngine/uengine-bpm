package org.uengine.modeling.cnv.layout;

import org.uengine.modeling.ElementView;
import org.uengine.modeling.Symbol;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjy on 2015. 11. 25..
 */
public class CnvLayoutGroup extends ElementView{

    private final String
            FLOW_TYPE = "FLOW",
            GRID_TYPE = "GRID";

    private String layoutGroupType;
    private String inActivityTracingTag;
    private String outAcitivityTracingTag;

    public String getFLOW_TYPE() {
        return FLOW_TYPE;
    }

    public String getGRID_TYPE() {
        return GRID_TYPE;
    }

    public String getLayoutGroupType() {
        return layoutGroupType;
    }

    public void setLayoutGroupType(String layoutGroupType) {
        this.layoutGroupType = layoutGroupType;
    }

    public String getInActivityTracingTag() {
        return inActivityTracingTag;
    }

    public void setInActivityTracingTag(String inActivityTracingTag) {
        this.inActivityTracingTag = inActivityTracingTag;
    }

    public String getOutAcitivityTracingTag() {
        return outAcitivityTracingTag;
    }

    public void setOutAcitivityTracingTag(String outAcitivityTracingTag) {
        this.outAcitivityTracingTag = outAcitivityTracingTag;
    }



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
        double diffX = x - getX();  // 설정할 x 위치 - LayoutGroup X 값
//System.out.println("LG diffX : " + diffX + "=" +x+"-"+getX());
        for(ElementView elementView : getElementViews()){
//System.out.println(">" + elementView.getId() + " :( "+ elementView.getX() +" )--> " + (elementView.getX() + diffX));
            elementView.setX(elementView.getX() + diffX);

        }

        super.setX(x);
    }


    @Override
    public void setY(double y) {
        double diffY = y - getY();
//System.out.println("LG diffy : " + diffY + "=" + y +"-"+ getY());
        for(ElementView elementView : getElementViews()){
            elementView.setY(elementView.getY() + diffY);
//System.out.println(">" + elementView.getId() + " : " + elementView.getY());
        }

        super.setY(y);
    }

    public double getMinX(){
        double rv = 0;
        double viewX = 0;
        for(ElementView elementView : getElementViews()){
            if( elementView instanceof CnvLayoutGroup){
                viewX = ((CnvLayoutGroup) elementView).getMinX();
            } else {
                viewX = elementView.getX();
            }

            if( rv == 0 || (viewX > 0 && rv > viewX) ){
                rv = viewX;
            }
        }
        return rv;
    }

    public double getMaxX(){
        double rv = 0;
        double viewX = 0;
        for(ElementView elementView : getElementViews()){
            if( elementView instanceof CnvLayoutGroup){
                viewX = ((CnvLayoutGroup) elementView).getMaxX();
            } else {
                viewX = elementView.getX();
            }
            if( rv < viewX ){
                rv = viewX;
            }
        }
        return rv;
    }


    public double getMinY(){
        double rv = 0;
        double viewY = 0;
        for(ElementView elementView : getElementViews()){
            if( elementView instanceof CnvLayoutGroup){
                viewY = ((CnvLayoutGroup) elementView).getMinY();
            } else {
                viewY = elementView.getY();
            }

            if( rv == 0 || (viewY > 0 && rv > viewY) ){
                rv = viewY;
            }
        }
        return rv;
    }

    public double getMaxY(){
        double rv = 0;
        double viewY = 0;
        for(ElementView elementView : getElementViews()){
            if( elementView instanceof CnvLayoutGroup){
                viewY = ((CnvLayoutGroup) elementView).getMaxY();
            } else {
                viewY = elementView.getY();
            }
            if( rv < viewY ){
                rv = viewY;
            }
        }
        return rv;
    }

    public double getMaxXWidth(){
        double viewX = 0;
        double maxX = 0;
        double rv = 0;

        for(ElementView elementView : getElementViews()){
            if( elementView instanceof CnvLayoutGroup){
                viewX = ((CnvLayoutGroup) elementView).getMaxX();
            } else {
                viewX = elementView.getX();
            }
            if( maxX < viewX ){
                maxX = viewX;
                rv = elementView.getWidth();
            }
        }
        return rv;
    }

    public double getMaxYHeight(){
        double viewY = 0;
        double maxY = 0;
        double rv = 0;

        for(ElementView elementView : getElementViews()){
            if( elementView instanceof CnvLayoutGroup){
                viewY = ((CnvLayoutGroup) elementView).getMaxY();
            } else {
                viewY = elementView.getY();
            }
            if( maxY < viewY ){
                maxY = viewY;
                rv = elementView.getHeight();
            }
        }
        return rv;
    }


    public String getElementId(int pos){
        String rv = null;
        int cnt = 0;
        for(ElementView elementView : getElementViews()){
            if( ++cnt == pos ){
                if( elementView instanceof CnvLayoutGroup){
                    rv = getElementId(pos);
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
            rv = getLastElementIdInFirstGroup();
        }else{
            rv = elementView.getId();
        }
        return rv;
    }

    @Override
    public double getWidth() {
            return getMaxX() + getMaxXWidth() - getMinX();
        //return getDimension().getWidth();
    }

    @Override
    public double getHeight() {
            return getMaxY() + getMaxYHeight() - getMinY();
        //return getDimension().getHeight();
    }



    private Dimension getDimension(){

        if(getElementViews()==null  || getElementViews().size()==0)
            return new Dimension(0,0);

        //double minX = 500000, minY = 500000, maxX = 0, maxY = 0;
        double minX = 0, minY = 0, maxX = 0, maxXW = 0, maxY = 0, maxYH = 0;

        Dimension dimension = new Dimension();

        for(ElementView elementView : getElementViews()){

            double elemX = elementView.getX();
            double elemY = elementView.getY();
            double elemW = elementView.getWidth();
            double elemH = elementView.getHeight();

            if( minX == 0 || ( minX > 0 && minX > elemX ) ){
                minX = elemX;
            }

            if( minY == 0 || ( minY > 0 && minY > elemY ) ){
                minY = elemY;
            }

            if( maxX == 0 || ( maxX > 0 && maxX < elemX ) ){
                maxX = elemX;
                maxXW = elemW;
            }

            if( maxY == 0 || ( maxY > 0 && maxY < elemY ) ){
                maxY = elemY;
                maxYH = elemH;
            }
/*            double elemX = elementView.getX() - elementView.getWidth() / 2;
            double elemY = elementView.getY() - elementView.getHeight() / 2;
            double elemX_ = elementView.getX() + (elementView.getWidth()) / 2;;
            double elemY_ = elementView.getY() + (elementView.getHeight()) / 2;;


            if(minX > elemX) minX = elemX;
            if(minY > elemY) minY = elemY;
            if(maxX < elemX_) maxX = elemX_;
            if(maxY < elemY_) maxY = elemY_;*/
        }
        dimension.setSize(maxX + maxXW - minX, maxY + maxYH - minY);
        return dimension;
    }

}
