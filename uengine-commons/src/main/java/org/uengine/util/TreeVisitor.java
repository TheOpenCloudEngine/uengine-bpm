package org.uengine.util;

import java.util.List;

/**
 * Created by jjy on 2016. 10. 11..
 */
public abstract class TreeVisitor<T> {


    public void run(T escTree) {

        for(T child : getChild(escTree)){
            logic(child);

            run(child);
        }

    }

    public abstract List<T> getChild(T parent);

    public abstract void logic(T elem);
}
