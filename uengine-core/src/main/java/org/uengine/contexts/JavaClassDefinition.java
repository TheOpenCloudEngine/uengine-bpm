package org.uengine.contexts;

import java.io.Serializable;

/**
 * Created by jjy on 2016. 4. 7..
 */
public class JavaClassDefinition implements Serializable {

    String className;
        public String getClassName() {
            return className;
        }
        public void setClassName(String className) {
            this.className = className;
        }

}
