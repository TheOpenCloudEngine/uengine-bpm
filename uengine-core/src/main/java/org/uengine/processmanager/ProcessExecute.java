package org.uengine.processmanager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jjy on 2016. 5. 25..
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ProcessExecute {
    String value();
}
