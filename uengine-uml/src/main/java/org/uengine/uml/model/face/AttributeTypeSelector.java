package org.uengine.uml.model.face;

import org.metaworks.Face;
import org.metaworks.component.SelectBox;

import java.util.ArrayList;

/**
 * Created by jangjinyoung on 15. 9. 4..
 */
public class AttributeTypeSelector extends SelectBox implements Face<String>{

    public AttributeTypeSelector(){

        ArrayList<String> options = new ArrayList<String>();
        options.add("Text");
        options.add("Number");
        options.add("Date");

        ArrayList<String> values = new ArrayList<String>();
        values.add("java.lang.String");
        values.add("java.lang.Long");
        values.add("java.util.Calendar");

        setOptionNames(options);
        setOptionValues(values);
    }

    @Override
    public void setValueToFace(String value) {
        setSelected(value);
    }

    @Override
    public String createValueFromFace() {
        return getSelected();
    }
}
