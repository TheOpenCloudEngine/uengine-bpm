package org.uengine.modeling;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositePalette extends Palette {

    private List<Palette> childPallet;

    public List<Palette> getChildPallet() {
        return childPallet;
    }

    public void setChildPallet(List<Palette> childPallet) {
        this.childPallet = childPallet;
    }

    public CompositePalette() {
        this.setChildPallet(new ArrayList<Palette>());
    }

    public void addPalette(Palette palette) {
        this.getChildPallet().add(palette);
    }
}
