package org.uengine.modeling;

import java.util.ArrayList;
import java.util.List;

public class CompositePalette extends Palette {

    private List<Palette> childPalettes;

    public List<Palette> getChildPalettes() {
        return childPalettes;
    }

    public void setChildPalettes(List<Palette> childPalettes) {
        this.childPalettes = childPalettes;
    }

    public CompositePalette() {
        this.setChildPalettes(new ArrayList<Palette>());
    }

    public void addPalette(Palette palette) {
        this.getChildPalettes().add(palette);
    }
}
