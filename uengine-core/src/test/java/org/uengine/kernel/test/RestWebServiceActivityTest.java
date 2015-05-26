package org.uengine.kernel.test;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.webservice.MethodProperty;
import org.uengine.webservice.ParameterProperty;

public class RestWebServiceActivityTest extends UEngineTest{

    ProcessDefinition processDefinition;

    /**
     * build a graph as follows:
     *
     *
     *     POST ---> GET ---> PUT ---> DELETE
     *
     *   * all the activities are RestWebServiceActivity
     *
     * @throws Exception
     */
    public void setUp() throws Exception {

        processDefinition = new ProcessDefinition();

        processDefinition.setProcessVariables(new ProcessVariable[]{
                ProcessVariable.forName("id")
        });

        RestWebServiceActivity a1 = new RestWebServiceActivity(); //도서정보 생성 (POST, Create)
        RestWebServiceActivity a2 = new RestWebServiceActivity(); //도서정보 읽기 (GET, Read)
        RestWebServiceActivity a3 = new RestWebServiceActivity(); //도서정보 갱신 (PUT, Update)
        RestWebServiceActivity a4 = new RestWebServiceActivity(); //도서정보 삭제 (DELETE, Delete)

        MethodProperty method = null;

        //a1. POST
        method = new MethodProperty();
        method.setName("POST");
        method.setBasePath("http://localhost:8080/restapp");
        method.setCallPath("/books");
        method.setResponseClass("String");
        //POST 100 ---> GET 100 ---> PUT 100 ---> DELETE 101
		//String content = "{\"id\":100,\"title\":\"바라야 내전\",\"creator\":\"로이스 맥마스터 부졸드\",\"type\":\"외국판타지소설\",\"date\":1313378460000}";
		//POST 101 ---> GET 101 ---> PUT 101 ---> DELETE 100
		String content = "{\"id\":101,\"title\":\"바라야 내전\",\"creator\":\"로이스 맥마스터 부졸드\",\"type\":\"외국판타지소설\",\"date\":1313378460000}";
		method.setModelProperty(content);
		ParameterProperty param = new ParameterProperty();
		param.setParamType("body");
		method.setRequest(new ParameterProperty[]{param});
        a1.setMethod(method);

        //a2. GET
        method = new MethodProperty();
        method.setName("GET");
        method.setBasePath("http://localhost:8080/restapp");
        //POST 100 ---> GET 100 ---> PUT 100 ---> DELETE 101
        //method.setCallPath("/books/100");
        //POST 101 ---> GET 101 ---> PUT 101 ---> DELETE 100
        method.setCallPath("/books/101");
        method.setResponseClass("String");
        a2.setMethod(method);

        //a3. PUT
        method = new MethodProperty();
        method.setName("PUT");
        method.setBasePath("http://localhost:8080/restapp");
        method.setResponseClass("String");
        //POST 100 ---> GET 100 ---> PUT 100 ---> DELETE 101
        //method.setCallPath("/books/100");
		//String content3 = "{\"id\":100,\"title\":\"어스시의 마법사\",\"creator\":\"어슐러 K. 르귄\",\"type\":\"판타지소설\",\"date\":1313378470000}";
        //POST 101 ---> GET 101 ---> PUT 101 ---> DELETE 100
        method.setCallPath("/books/101");
		String content3 = "{\"id\":101,\"title\":\"어스시의 마법사\",\"creator\":\"어슐러 K. 르귄\",\"type\":\"판타지소설\",\"date\":1313378470000}";
		method.setModelProperty(content3);
		ParameterProperty param3 = new ParameterProperty();
		param3.setParamType("body");
		method.setRequest(new ParameterProperty[]{param3});
        a3.setMethod(method);

        //a4. DELETE
        method = new MethodProperty();
        method.setName("DELETE");
        method.setBasePath("http://localhost:8080/restapp");
        //POST 100 ---> GET 100 ---> PUT 100 ---> DELETE 101
        //method.setCallPath("/books/101");
        //POST 101 ---> GET 101 ---> PUT 101 ---> DELETE 100
        method.setCallPath("/books/100");
        method.setResponseClass("String");
        a4.setMethod(method);

        a1.setTracingTag("a1");
        a2.setTracingTag("a2");
        a3.setTracingTag("a3");
        a4.setTracingTag("a4");
        processDefinition.addChildActivity(a1);
        processDefinition.addChildActivity(a2);
        processDefinition.addChildActivity(a3);
        processDefinition.addChildActivity(a4);

        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a1");
            t1.setTargetRef("a2");
            processDefinition.addSequenceFlow(t1);
        }

        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a2");
            t1.setTargetRef("a3");
            processDefinition.addSequenceFlow(t1);
        }

        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a3");
            t1.setTargetRef("a4");
            processDefinition.addSequenceFlow(t1);
        }

        processDefinition.afterDeserialization();
        ProcessInstance.USE_CLASS = DefaultProcessInstance.class;

    }

    public void testRSForCRUD() throws Exception {
        ProcessInstance instance = processDefinition.createInstance();
        instance.execute();

        assertExecutionPathEquals(new String[]{"a1","a2","a3","a4"}, instance);

        //POST 101 ---> GET 101 ---> PUT 101 ---> DELETE 100
        assertEquals((int)instance.get("id"), 100);

        //POST 100 ---> GET 100 ---> PUT 100 ---> DELETE 101
        //assertEquals((int)instance.get("id"), 101);

    }

}
