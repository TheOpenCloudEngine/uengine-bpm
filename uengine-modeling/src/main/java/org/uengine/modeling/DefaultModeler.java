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

    @Override
    public IModel getModel() throws Exception {
//        if(this.model  == null)
//            this.model = createModel();

        return this.model;
    }

    public IModel createModel(){
        return new IModel() {
            @Override
            public String getAlias() {
                return null;
            }
        };
    }

    public List<ElementView> obtainElementViewList(){
        return getCanvas().getElementViewList();
    }

    public List<RelationView> obtainRelationViewList(){
        return getCanvas().getRelationViewList();
    }


    @ServiceMethod(keyBinding = "Ctrl+S", callByContent = true)
    public void save() throws Exception {
        XStream xStream = new XStream();
        xStream.toXML(getModel(), new FileWriter("model.xml"));
    }



    @ServiceMethod(keyBinding = "Ctrl+O")
    public void load() throws Exception {
        XStream xStream = new XStream();
        IModel model = (IModel) xStream.fromXML(new FileReader("model.xml"));

        setModel(model);
    }
}
