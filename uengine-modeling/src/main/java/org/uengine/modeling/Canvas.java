package org.uengine.modeling;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.metaworks.*;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.Clipboard;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jyj
 */
public class Canvas {

    List<ElementView> elementViewList;
    private String thumbnailURL;

    //네비게이터의 사용 여부를 결정하는 함수.
    private boolean navigator = true;

    public List<ElementView> getElementViewList() {
        return elementViewList;
    }

    public void setElementViewList(List<ElementView> elementViewList) {
        this.elementViewList = elementViewList;
    }

    List<RelationView> relationViewList;

    public List<RelationView> getRelationViewList() {
        return relationViewList;
    }

    public void setRelationViewList(List<RelationView> relationViewList) {
        this.relationViewList = relationViewList;
    }

    public final static String CANVAS_DROP = "canvasdrop";

    @AutowiredFromClient
    public Clipboard clipboard;
    private MetaworksContext metaworksContext;
    private String modelerType;

    public Canvas() {
        this.elementViewList = new ArrayList<ElementView>();
        this.relationViewList = new ArrayList<RelationView>();
    }

    public Canvas(String modelerType) {
        this();
        this.setModelerType(modelerType);
    }

    String id;
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }



    public MetaworksContext getMetaworksContext() {
        return metaworksContext;
    }

    public void setMetaworksContext(MetaworksContext metaworksContext) {
        this.metaworksContext = metaworksContext;
    }

    @Id
    public String getModelerType() {
        return modelerType;
    }

    public void setModelerType(String modelerType) {
        this.modelerType = modelerType;

    }

    @Available(when = {MetaworksContext.WHEN_NEW, MetaworksContext.WHEN_EDIT})
    @ServiceMethod(payload = {"clipboard", "modelerType"}, target = ServiceMethodContext.TARGET_APPEND, eventBinding = CANVAS_DROP)
    public void drop() {

        ElementView elementView = null;

        Object content = clipboard.getContent();
        if (content instanceof Symbol) {
            Symbol symbol = (Symbol) content;

            if ("EDGE".equals(symbol.getShapeType())) {
                RelationView relationView = null;

                try {
                    IRelation relation = (IRelation) Thread.currentThread().getContextClassLoader().loadClass(symbol.getElementClassName()).newInstance();
                    relationView = relation.createView();
                    relationView.fill(symbol);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                MetaworksRemoteService.wrapReturn(new Refresh(new Clipboard(CANVAS_DROP)), new ToAppend(ServiceMethodContext.TARGET_SELF, relationView));

            } else {

                try {
                    IElement element = (IElement) Thread.currentThread().getContextClassLoader().loadClass(symbol.getElementClassName()).newInstance();
                    if (element instanceof ContextAware) {
                        if (((ContextAware) element).getMetaworksContext() == null)
                            ((ContextAware) element).setMetaworksContext(new MetaworksContext());
                        ((ContextAware) element).getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
                    }

                    elementView = element.createView();
                    elementView.fill(symbol);
                    elementView.setByDrop(true);

                    if (elementView.getMetaworksContext() == null)
                        elementView.setMetaworksContext(new MetaworksContext());
                    elementView.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


                MetaworksRemoteService.wrapReturn(new Refresh(new Clipboard(CANVAS_DROP)), new ToAppend(ServiceMethodContext.TARGET_SELF, elementView));

                return;
            }
        }

        MetaworksRemoteService.wrapReturn(new Refresh(new Clipboard(CANVAS_DROP)));
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public boolean getNavigator() {
        return navigator;
    }

    public void setNavigator(boolean navigator) {
        this.navigator = navigator;
    }

    /**
     * 캔버스가 도형의 추가/수정/삭제/프로퍼티 설정 이 발생되었을 때 변경 여부를 알려주는 프로퍼티
     */
    private boolean isChanged = false;

    public boolean getIsChanged() {
        return isChanged;
    }

    public void setIsChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }
}
