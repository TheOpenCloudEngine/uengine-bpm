package org.uengine.uml.model.face;

import org.metaworks.Face;
import org.metaworks.annotation.Hidden;
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
        options.add("Real");
        options.add("Date");
        options.add("Boolean");

        ArrayList<String> values = new ArrayList<String>();
        values.add("java.lang.String");
        values.add("java.lang.Long");
        values.add("java.lang.Double");
        values.add("java.util.Date");
        values.add("java.lang.Boolean");

        setOptionNames(options);
        setOptionValues(values);
    }

    @Override
    public void setValueToFace(String value) {
        set_realValue(value);
        setSelected(value);
    }

    @Override
    public String createValueFromFace() {
        return getSelected();
    }


    String _realValue;
    @Hidden
        public String get_realValue() {
            return _realValue;
        }
        public void set_realValue(String _realValue) {
            this._realValue = _realValue;
        }

}
