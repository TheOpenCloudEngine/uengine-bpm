package org.uengine.modeling;

import org.uengine.kernel.LeveledException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjy on 2016. 9. 2..
 */
public class ValidationPanel {

    List<LeveledException> exceptions = new ArrayList<LeveledException>();
        public List<LeveledException> getExceptions() {
            return exceptions;
        }

        public void setExceptions(List<LeveledException> exceptions) {
            this.exceptions = exceptions;
        }



}
