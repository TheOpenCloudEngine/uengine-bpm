package org.uengine.kernel.test;

import junit.framework.TestCase;
import org.uengine.kernel.*;

import java.util.*;

public class UEngineTest extends TestCase{


    public void assertExecutionPathEquals(String[] expectedPaths, ProcessInstance instance) {
        assertExecutionPathEquals(null, expectedPaths, instance);
    }

    public void assertExecutionPathEquals(String message, String[] expectedPaths, ProcessInstance instance) {
        List expectedPathsInSet = new ArrayList();
        for(int i=0; i<expectedPaths.length; i++) {
            String path = expectedPaths[i];
            expectedPathsInSet.add(path);
        }

        if(message!=null)
            assertEquals(message, expectedPathsInSet, instance.getActivityCompletionHistory());
        else
            assertEquals(expectedPathsInSet, instance.getActivityCompletionHistory());
    }


}
