package org.uengine.modeling.cnv.layout;

import org.uengine.modeling.ElementView;

/**
 * Created by jjy on 2015. 11. 25..
 */
public class CnvGridLayout extends CnvLayout{

    int rows = 1;
        public int getRows() {
            return rows;
        }
        public void setRows(int rows) {
            this.rows = rows;
        }

    int spacing = 20;
        public int getSpacing() {
            return spacing;
        }

        public void setSpacing(int spacing) {
            this.spacing = spacing;
        }

    public CnvLayoutGroup layout(){
        double y = 0; //간격
        int elementIndex = 0;
        for (ElementView view : getElementViews()) {
            if( elementIndex == 0 ){
                y = view.getY();
            }
            view.setY(y);
            double height = view.getHeight();
            y = y + height + getSpacing();
            elementIndex++;
        }

        return createLayoutGroup();
    }




}
