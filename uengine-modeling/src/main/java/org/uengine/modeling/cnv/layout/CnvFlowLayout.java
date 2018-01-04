package org.uengine.modeling.cnv.layout;

import org.uengine.modeling.ElementView;

/**
 * Created by jjy on 2015. 11. 25..
 */
public class CnvFlowLayout extends CnvLayout {

    static final int elementMinW = 9000/4, //2250
            spacingW = 1000,
            //elementCntOfOneStack = 10000 / (elementMinW + spacingW), //10000/(2250+300) = 3.92...
            elementCntOfOneStack = 1000000000,
            elementMinH = 1500,
            spacingH = 300;


    int fullWidthOfEntryElements;
    int stackCntOfEntryElements;
    int startXOfLastStack;
    int startXOfFullStack;

    int w, h;


    public void init(){
        // all following coordinate system is 10000 (relative) system. not the absolute position.
        fullWidthOfEntryElements = getElementViews().size() * (elementMinW + spacingW);         //전체 넓이
        stackCntOfEntryElements =  getElementViews().size() / elementCntOfOneStack;
        startXOfLastStack = 5000 - ( getElementViews().size() % elementCntOfOneStack * (elementMinW + spacingW)) / 2;  //50 means 50% --> thus, means center.
        startXOfFullStack = 5000 - (elementCntOfOneStack * (elementMinW + spacingW)) / 2;  //50 means 50% --> thus, means center.

        w = getCanvasWidth() * elementMinW / 10000;
        h = getCanvasHeight() * elementMinH / 10000;

    }

    @Override
    public CnvLayoutGroup layout(){
        //단독 레이아웃 그룹일때는 처리안함..
        if(getElementViews().size() == 1 && getElementViews().get(0) instanceof CnvLayoutGroup){
            return createLayoutGroup();
        }

        init();

        int elementIndex = 0;
        int layoutGroupMaxX = 0;
        int layoutDiffX = 0;
        int valueXBeforeSetting = 0;
        boolean isPreviousViewLayoutGroup = false;

        for (ElementView view : getElementViews()) {
            int stack = elementIndex / elementCntOfOneStack;
            boolean elementLocatesLastStack = (stack == stackCntOfEntryElements);
            int x = (elementLocatesLastStack ? startXOfLastStack : startXOfFullStack) + (elementIndex % elementCntOfOneStack * (elementMinW + spacingW)) + elementMinW / 2;
            int y = stack * (elementMinH + spacingH) + elementMinH / 2;  //since the coordinate system is center-aligned for any SVG elements.

            //좌표계산
            //전 엘리먼트가 레이아웃그룹인 경우는 최대엑스값에 차이값만 더해 좌표계산
            if( isPreviousViewLayoutGroup ) {
                //첫번째가 레이아웃그룹인경우 diff값=0 이므로 값 보정
                if( layoutDiffX == 0 ) {
                    layoutDiffX = getAbsoluteX(x) - valueXBeforeSetting;
                }
                x = layoutGroupMaxX + layoutDiffX;
                //조정된 값이 최대치에서 표준 크기 범위안에 있으면 재조정
                if( x < (layoutGroupMaxX + w + getAbsoluteSpacingW()) ){
                    x = layoutGroupMaxX + w + getAbsoluteSpacingW();
                }
                layoutGroupMaxX = x; //최대값이 바꼈으므로 재설정
            }else {
                x = getAbsoluteX(x) + layoutGroupMaxX;
            }

            y = getAbsoluteY(y);
            valueXBeforeSetting = (int) view.getX();

            //좌표설정
            //첫번째가 레이아웃 그룹인 경우 재설정 안함
            if ( !( elementIndex == 0 && view instanceof CnvLayoutGroup) ) {
                view.setX((x));
            }

            if(view instanceof CnvLayoutGroup){
                isPreviousViewLayoutGroup = true;
                layoutGroupMaxX = (int)((CnvLayoutGroup) view).getMaxX();
                layoutDiffX = x - valueXBeforeSetting; //SET 하기전값으로 DIFF값 설정
            }else {
                view.setY((y));
                view.setWidth((w));
                view.setHeight((h));
            }

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

    private int getAbsoluteSpacingW(){
        return canvasW * spacingW / 10000;
    }


}
