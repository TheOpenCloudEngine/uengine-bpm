package org.uengine.processmanager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jjy on 2016. 5. 25..
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface StepProceed {
    int correlationKeyOrder() default 0;

    String value();
}
