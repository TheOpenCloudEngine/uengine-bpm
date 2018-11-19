package org.uengine.kernel.test;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.BlockFinder;
import org.uengine.kernel.bpmn.Gateway;
import org.uengine.kernel.bpmn.SequenceFlow;

import java.util.*;

public class ExpressionEvaluateTest extends UEngineTest{

    ProcessDefinition processDefinition;

    public void setUp() throws Exception {

        processDefinition = new ProcessDefinition();
        processDefinition.setVolatile(true);

        processDefinition.setProcessVariables(new ProcessVariable[]{
                ProcessVariable.forName("courses"),
                ProcessVariable.forName("choice"),
        });

//        {
//            processDefinition.addChildActivity();
//        }

        processDefinition.addChildActivity(new DefaultActivity());

        processDefinition.afterDeserialization();

        AbstractProcessInstance.USE_CLASS = DefaultProcessInstance.class;


    }

    public void testSpELExpression() throws Exception {

        String expression = "/courses/{courses[choice]._links.self}/clazzes";
        String expected = "/courses/my_url/clazzes";

        Map<String, Object> course = new HashMap();
        List<Map> courses = new ArrayList<>();
        courses.add(course);

        Map<String, Object> links = new HashMap<>();
        links.put("self", "my_url");

        course.put("_links", links);


        ProcessInstance instance = processDefinition.createInstance();
        instance.set("courses", courses);
        instance.set("choice", 0);


        String actual = processDefinition.getChildActivities().get(0).evaluateContent(instance, expression).toString();

        assertEquals(expected, actual);
    }



}
