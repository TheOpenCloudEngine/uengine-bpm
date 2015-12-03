package org.uengine.modeling.layout;

import org.uengine.modeling.ElementView;

import java.util.List;

/**
 * Created by jjy on 2015. 11. 25..
 */
public class FlowLayout extends Layout{

    static final int elementMinW = 9000/4,
            spacingW = 300,
            elementCntOfOneStack = 10000 / (elementMinW + spacingW),
            elementMinH = 1500,
            spacingH = 300;


    int fullWidthOfEntryElements;
    int stackCntOfEntryElements;
    int startXOfLastStack;
    int startXOfFullStack;

    int w, h;


    public void init(){
        // all following coordinate system is 10000 (relative) system. not the absolute position.
        fullWidthOfEntryElements = getElementViews().size() * (elementMinW + spacingW);
        stackCntOfEntryElements =  getElementViews().size() / elementCntOfOneStack;
        startXOfLastStack = 5000 - ( getElementViews().size() % elementCntOfOneStack * (elementMinW + spacingW)) / 2;  //50 means 50% --> thus, means center.
        startXOfFullStack = 5000 - (elementCntOfOneStack * (elementMinW + spacingW)) / 2;  //50 means 50% --> thus, means center.

        w = getCanvasWidth() * elementMinW / 10000;
        h = getCanvasHeight() * elementMinH / 10000;

    }

    @Override
    public LayoutGroup layout(){

        init();

        int elementIndex = 0;
        for (ElementView view : getElementViews()) {

            int stack = elementIndex / elementCntOfOneStack;
            boolean elementLocatesLastStack = (stack == stackCntOfEntryElements);
            int x = (elementLocatesLastStack ? startXOfLastStack : startXOfFullStack) + (elementIndex % elementCntOfOneStack * (elementMinW + spacingW)) + elementMinW / 2;
            int y = stack * (elementMinH + spacingH) + elementMinH / 2;  //since the coordinate system is center-aligned for any SVG elements.

            x = getAbsoluteX(x);
            y = getAbsoluteY(y);

            view.setX((x));
            view.setY((y));
            view.setWidth((w));
            view.setHeight((h));

            elementIndex++;
        }

        return createLayoutGroup();

    }


    private int getAbsoluteX(int x) {
        return canvasW * x / 10000 + getStartX();
    }
    private int getAbsoluteY(int y) {
        return canvasH * y / 10000 + getStartY();
    }


}
