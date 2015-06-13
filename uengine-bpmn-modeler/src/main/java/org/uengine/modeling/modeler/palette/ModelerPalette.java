package org.uengine.modeling.modeler.palette;

import org.uengine.modeling.CompositePalette;
import org.uengine.modeling.Palette;

/**
 * Created by Ryuha on 2015-06-12.
 */
public class ModelerPalette extends CompositePalette {
    public ModelerPalette() {

    }

    public ModelerPalette(String type) {
        super();

        setName("Modeler Palette");

        Palette simplePalette = new SimplePalette(type);
        Palette attributePalette = new AttributePalette();

        getChildPallet().add(simplePalette);
        getChildPallet().add(attributePalette);
    }
}
