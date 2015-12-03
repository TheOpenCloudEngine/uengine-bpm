package org.uengine.modeling.layout;

import org.uengine.modeling.ElementView;

import java.util.List;

/**
 * Created by jjy on 2015. 11. 25..
 */
public class GridLayout extends Layout{

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




    public LayoutGroup layout(){
        double y = 30;
        int elementIndex = 0;
        for (ElementView view : getElementViews()) {

         //   double width = view.getWidth();
            double height = view.getHeight();

         //   view.setX((getCanvasWidth() - width) / 2);
            view.setY(y);

            elementIndex++;

            y = y + height + getSpacing();

        }

        return createLayoutGroup();
    }




}
