package org.uengine.modeling;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.ServiceMethod;


public class DefaultModeler extends Modeler {

    private int lastTracingTag;

    public DefaultModeler(){
        super();
    }

    public int getLastTracingTag() {
        return lastTracingTag;
    }
    public void setLastTracingTag(int lastTracingTag) {
        this.lastTracingTag = lastTracingTag;
    }

    @Override
    @Available(when={MetaworksContext.WHEN_EDIT, MetaworksContext.WHEN_NEW})
    public Palette getPalette() {
        return super.getPalette();
    }

    public List<ElementView> obtainElementViewList(){
        return getCanvas().getElementViewList();
    }

    public List<RelationView> obtainRelationViewList(){
        return getCanvas().getRelationViewList();
    }


}
