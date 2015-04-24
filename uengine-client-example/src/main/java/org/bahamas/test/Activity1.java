package org.bahamas.test;


import org.uengine.kernel.DefaultActivity;
import org.uengine.kernel.ProcessInstance;

import java.io.Serializable;

/**
 * Created by cloudine on 2015. 2. 13..
 */
public class Activity1 extends DefaultActivity {

    @Override
    protected void executeActivity(final ProcessInstance instance) throws Exception {
        //System.out.println("들어옴");

        //System.out.println(getTracingTag());

        //System.out.println(instance.get("data"));

        System.out.println("쓰레드 시작 전");
        new Thread()
        {
            @Override
            public void run() {

                try {
                    System.out.println("시작");
                    Thread.sleep(5000);

                    System.out.println("종료");
                    fireComplete(instance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        System.out.println("쓰레드 시작 후");

    }
//    @Override
//    public void run(DelegateExecution execution) throws Exception {
////////////////////////////////////////////
//        // Spring Framework의 서비스 객체를 가져온다.
//        //////////////////////////////////////////
//
//        // Spring Framework Application Context
//        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
//
//        // Application Context에서 관련 클래스 요청
//        GlobalAttributes globalAttributes = applicationContext.getBean(GlobalAttributes.class);
//
//
//        //임시 저장소에 노드 결과물 등록
//        globalAttributes.setNodeTaskResult(
//                execution,
//                this.mergedParams
//        );
//
//    }
}
