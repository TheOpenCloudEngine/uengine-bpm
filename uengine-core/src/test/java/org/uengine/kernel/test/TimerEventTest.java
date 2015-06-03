package org.uengine.kernel.test;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.commons.lang.UnhandledException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.metaworks.spring.SpringConnectionFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.Event;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.kernel.bpmn.TimerEvent;
import org.uengine.processmanager.ProcessManagerBean;
import org.uengine.scheduler.WaitJob;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class TimerEventTest extends UEngineTest {

    ProcessDefinition processDefinition;

    /**
     * build a graph as follows:
     * <p>
     * <p>
     * 1 -> 2 -> 3 --------> 4
     *          (+)
     *           |
     *           +-> 5 (timer -> onMessage())
     *
     * @throws Exception
     */
    public void setUp() throws Exception {

        processDefinition = new ProcessDefinition();

        processDefinition.setProcessVariables(new ProcessVariable[]{
//                ProcessVariable.forName("var1"),
        });


        for (int i = 1; i < 6; i++) {
            Activity a1 = new DefaultActivity();

            if (i == 2 || i == 3) {
                ReceiveActivity rcv = new ReceiveActivity();
                rcv.setMessage("receive");

                a1 = rcv;

            }

            if (i == 5) {
                TimerEvent event = new TimerEvent();
                event.setName("a5");
                event.setAttachedToRef("a3");

                event.setDuration(TimerEvent.WAITING_TYPE_UNTIL);
                event.setExpression("10 0 0 0");

                a1 = event;
            }

            a1.setTracingTag("a" + i);
            processDefinition.addChildActivity(a1);

        }

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

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    DataSource dataSource = null;


    public void testTimerEvent() throws Exception {

        final ProcessInstance instance = processDefinition.createInstance();
        instance.execute();
            assertExecutionPathEquals("Running Before Event", new String[]{
                "a1"
        }, instance);


        instance.getProcessDefinition().fireMessage("receive", instance, null);
        assertExecutionPathEquals("Running After Event", new String[]{
                "a1", "a2"
        }, instance);

        // call timer event - onMessage()
        instance.getProcessDefinition().fireMessage("timer", instance, "a5");
        assertExecutionPathEquals("Triggering Event Once", new String[]{
                "a1", "a2", "a5"
        }, instance);




        String sql = " SELECT * FROM SCHEDULE_TABLE WHERE INSTID = ? AND TRCTAG = ?";
//        String sql = " SELECT * FROM SCHEDULE_TABLE WHERE INSTID = ? AND TRCTAG = ? AND STARTDATE <= ?";

        String instId = "";
        String trctag = "";

        try {
            ApplicationContext xml = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
            dataSource = (DataSource) xml.getBean("dataSource");
            conn = dataSource.getConnection();

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, instance.getInstanceId().split("_")[1]);
            pstmt.setString(2, "a5");
//            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                instId = rs.getString(1);
                trctag = rs.getString(2);
            }
            assertNotNull(instId);
            assertNotNull(trctag);
            rs.close();
            pstmt.close();

        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }if (conn != null) try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }
}
