package org.uengine.kernel.test;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.webservice.MethodProperty;
import org.uengine.webservice.ParameterProperty;

import java.util.ArrayList;

public class RestWebServiceActivityTest extends UEngineTest{

    ProcessDefinition processDefinition;

    RestWebServiceActivity a1;

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

        a1 = new RestWebServiceActivity(); //도서정보 생성 (POST, Create)
//        RestWebServiceActivity a2 = new RestWebServiceActivity(); //도서정보 읽기 (GET, Read)
//        RestWebServiceActivity a3 = new RestWebServiceActivity(); //도서정보 갱신 (PUT, Update)
//        RestWebServiceActivity a4 = new RestWebServiceActivity(); //도서정보 삭제 (DELETE, Delete)
//
//        MethodProperty method = null;
//
//        //a1. POST
//        method = new MethodProperty();
//        method.setName("POST");
//        method.setBasePath("http://10.0.1.212:9090");
//        method.setCallPath("/books");
//        method.setResponseClass("String");
//        //POST 100 ---> GET 100 ---> PUT 100 ---> DELETE 101
//        String content = "{\"id\":8,\"title\":\"바라야 내전\",\"author\":\"로이스 맥마스터 부졸드\",\"publicationYear\":2015,\"comment\":\"외국판타지소설\"}";
//        //POST 101 ---> GET 101 ---> PUT 101 ---> DELETE 100
//        //String content = "{\"id\":9,\"title\":\"바라야 내전2\",\"author\":\"로이스 맥마스터 부졸드\",\"publicationYear\":\"2015\",\"comment\":외국판타지소설}";
//        method.setModelProperty(content);
//        ParameterProperty param = new ParameterProperty();
//        param.setParamType("body");
//        method.setRequest(new ParameterProperty[]{param});
//        a1.setMethod(method);
//
//        //a2. GET
//        method = new MethodProperty();
//        method.setName("GET");
//        method.setBasePath("http://10.0.1.212:9090");
//        //POST 100 ---> GET 100 ---> PUT 100 ---> DELETE 101
//        method.setCallPath("/books/8");
//        //POST 101 ---> GET 101 ---> PUT 101 ---> DELETE 100
//        //method.setCallPath("/books/9");
//        method.setResponseClass("String");
//        a2.setMethod(method);
//
//        //a3. PUT
//        method = new MethodProperty();
//        method.setName("PUT");
//        method.setBasePath("http://10.0.1.212:9090");
//        method.setResponseClass("String");
//        //POST 100 ---> GET 100 ---> PUT 100 ---> DELETE 101
//        method.setCallPath("/books/8");
//        String content3 = "{\"id\":8,\"title\":\"바라야 내전\",\"author\":\"로이스 맥마스터 부졸드\",\"publicationYear\":2014,\"comment\":\"외국소설\"}";
//        //POST 101 ---> GET 101 ---> PUT 101 ---> DELETE 100
//        //method.setCallPath("/books/9");
//        //String content3 = "{\"id\":9,\"title\":\"바라야 내전2\",\"author\":\"로이스 맥마스터 부졸드\",\"publicationYear\":\"2014\",\"comment\":외국소설}";
//        method.setModelProperty(content3);
//        ParameterProperty param3 = new ParameterProperty();
//        param3.setParamType("body");
//        method.setRequest(new ParameterProperty[]{param3});
//        a3.setMethod(method);
//
//        //a4. DELETE
//        method = new MethodProperty();
//        method.setName("DELETE");
//        method.setBasePath("http://10.0.1.212:9090");
//        //POST 100 ---> GET 100 ---> PUT 100 ---> DELETE 101
//        method.setCallPath("/books/8");
//        //POST 101 ---> GET 101 ---> PUT 101 ---> DELETE 100
//        //method.setCallPath("/books/8");
//        method.setResponseClass("String");
//        a4.setMethod(method);

//        a1.setTracingTag("a1");
//        a2.setTracingTag("a2");
//        a3.setTracingTag("a3");
//        a4.setTracingTag("a4");
//        processDefinition.addChildActivity(a1);
//        processDefinition.addChildActivity(a2);
//        processDefinition.addChildActivity(a3);
//        processDefinition.addChildActivity(a4);
//
//        {
//            SequenceFlow t1 = new SequenceFlow();
//            t1.setSourceRef("a1");
//            t1.setTargetRef("a2");
//            processDefinition.addSequenceFlow(t1);
//        }
//
//        {
//            SequenceFlow t1 = new SequenceFlow();
//            t1.setSourceRef("a2");
//            t1.setTargetRef("a3");
//            processDefinition.addSequenceFlow(t1);
//        }
//
//        {
//            SequenceFlow t1 = new SequenceFlow();
//            t1.setSourceRef("a3");
//            t1.setTargetRef("a4");
//            processDefinition.addSequenceFlow(t1);
//        }

        processDefinition.afterDeserialization();
        ProcessInstance.USE_CLASS = DefaultProcessInstance.class;

    }

    public void testCreate() throws Exception {
        MethodProperty method = null;
        method = new MethodProperty();
        method.setName("POST");
        method.setBasePath("http://10.0.1.212:9090");
        method.setCallPath("/books");
        method.setResponseClass("String");
        //POST 100 ---> GET 100 ---> PUT 100 ---> DELETE 101
        String content = "{\"id\":2,\"title\":\"Barrayar\",\"author\":\"MacMaster\",\"publicationYear\":2015,\"comment\":\"fantasy\"}";
        //POST 101 ---> GET 101 ---> PUT 101 ---> DELETE 100
        //String content = "{\"id\":9,\"title\":\"바라야 내전2\",\"author\":\"로이스 맥마스터 부졸드\",\"publicationYear\":\"2015\",\"comment\":외국판타지소설}";
        method.setModelProperty(content);
        ParameterProperty param = new ParameterProperty();
        param.setParamType("body");
        method.setRequest(new ParameterProperty[]{param});
        a1.setMethod(method);

        a1.setTracingTag("a1");
        processDefinition.setChildActivities(new ArrayList<Activity>());
        processDefinition.addChildActivity(a1);

        ProcessInstance instance = processDefinition.createInstance();
        instance.execute();
    }

    public void testRead() throws Exception {
        MethodProperty method = null;
        method = new MethodProperty();
        method.setName("GET");
        method.setBasePath("http://10.0.1.212:9090");
        //POST 100 ---> GET 100 ---> PUT 100 ---> DELETE 101
        method.setCallPath("/books/2");
        //POST 101 ---> GET 101 ---> PUT 101 ---> DELETE 100
        //method.setCallPath("/books/9");
        method.setResponseClass("String");
        a1.setMethod(method);

        a1.setTracingTag("a1");
        processDefinition.setChildActivities(new ArrayList<Activity>());
        processDefinition.addChildActivity(a1);

        ProcessInstance instance = processDefinition.createInstance();
        instance.execute();
    }

    public void testUpdate() throws Exception {
        MethodProperty method = null;
        method = new MethodProperty();
        method.setName("PUT");
        method.setBasePath("http://10.0.1.212:9090");
        method.setResponseClass("String");
        //POST 100 ---> GET 100 ---> PUT 100 ---> DELETE 101
        method.setCallPath("/books/2");
        String content3 = "{\"id\":8,\"title\":\"Barrayar\",\"author\":\"MacMaster\",\"publicationYear\":2004,\"comment\":\"novel\"}";
        //POST 101 ---> GET 101 ---> PUT 101 ---> DELETE 100
        //method.setCallPath("/books/9");
        //String content3 = "{\"id\":9,\"title\":\"바라야 내전2\",\"author\":\"로이스 맥마스터 부졸드\",\"publicationYear\":\"2014\",\"comment\":외국소설}";
        method.setModelProperty(content3);
        ParameterProperty param3 = new ParameterProperty();
        param3.setParamType("body");
        method.setRequest(new ParameterProperty[]{param3});
        a1.setMethod(method);

        a1.setTracingTag("a1");
        processDefinition.setChildActivities(new ArrayList<Activity>());
        processDefinition.addChildActivity(a1);

        ProcessInstance instance = processDefinition.createInstance();
        instance.execute();
    }

    public void testDelete() throws Exception {
        MethodProperty method = null;
        method = new MethodProperty();
        method.setName("DELETE");
        method.setBasePath("http://10.0.1.212:9090");
        //POST 100 ---> GET 100 ---> PUT 100 ---> DELETE 101
        method.setCallPath("/books/2");
        //POST 101 ---> GET 101 ---> PUT 101 ---> DELETE 100
        //method.setCallPath("/books/8");
        method.setResponseClass("String");
        a1.setMethod(method);

        a1.setTracingTag("a1");
        processDefinition.setChildActivities(new ArrayList<Activity>());
        processDefinition.addChildActivity(a1);

        ProcessInstance instance = processDefinition.createInstance();
        instance.execute();
    }

    public void testRSForCRUD() throws Exception {


//        assertExecutionPathEquals(new String[]{"a1","a2","a3","a4"}, instance);

        //POST 101 ---> GET 101 ---> PUT 101 ---> DELETE 100
        //assertEquals((int)instance.get("id"), 8);

        //POST 100 ---> GET 100 ---> PUT 100 ---> DELETE 101
//        assertEquals(instance.get("id"), "8");

        testCreate();
        testRead();
        testUpdate();
        testDelete();

    }

}
