package org.uengine.kernel.bpmn;

import org.metaworks.annotation.Range;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.uengine.kernel.DefaultActivity;
import org.uengine.kernel.MessageListener;
import org.uengine.kernel.ProcessInstance;
import org.uengine.scheduler.SchedulerUtil;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;


public class TimerEvent extends Event{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	public static final String WAITING_TYPE_PERIOD 				= "WAIT_PERIOD";
	public static final String WAITING_TYPE_UNTIL 					= "WAIT_UNTIL";
	public static final String WAITING_TYPE_UNTIL_WITH_DATE 	= "WAIT_UNTIL_WITH_DATE";
	public static final String STOP_INSTANCE = "STOP_INSTANCE";
	public static final String PASS_INSTANCE = "PASS_INSTANCE";

	String expression;
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}

	String duration;
	//	@Face(displayName="기간설정",
//			ejsPath="dwr/metaworks/genericfaces/RadioButton.ejs",
//			options={"for the next occurrance set by 'Cron Expression'","until given 'WaitUntil'","until given 'variable date + WaitUntil'"},
//			values={WAITING_TYPE_PERIOD, WAITING_TYPE_UNTIL, WAITING_TYPE_UNTIL_WITH_DATE})
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}

	String instanceStop;
	//	@Face(displayName="인스턴스 종료", ejsPath="dwr/metaworks/genericfaces/RadioButton.ejs",
//			options={"stop Instance after wait give 'Date'", "no"},
//			values={STOP_INSTANCE, PASS_INSTANCE})
	public String getInstanceStop() {
		return instanceStop;
	}
	public void setInstanceStop(String instanceStop) {
		this.instanceStop = instanceStop;
	}

	@Override
	public boolean onMessage(ProcessInstance instance, Object payload) throws Exception {
//TODO: quartz job 에 등록
//		job.register("timer_" + getTracingTag(), instanceId);

		String resultExpression = expression;
		if( WAITING_TYPE_PERIOD.equals(duration)){
			Calendar modifyCal = SchedulerUtil.getCalendarByCronExpression(resultExpression);
			this.addSchedule(instance, this.getTracingTag(), modifyCal, resultExpression);
		}else if( WAITING_TYPE_UNTIL.equals(duration) || WAITING_TYPE_UNTIL_WITH_DATE.equals(duration)){
			Calendar modifyCal = Calendar.getInstance();

			String[] exp = resultExpression.split(" ");
			if( WAITING_TYPE_UNTIL_WITH_DATE.equals(duration) ){
				String srcVariableName = exp[4];
				Object val = instance.getBeanProperty(srcVariableName);
				if( val != null && val instanceof Calendar){
					modifyCal = (Calendar)val;
				}else if(val != null && val instanceof Date){
					modifyCal.setTime((Date)val);
				}
			}
			int minutes = Integer.parseInt(exp[0]) ;
			int hours = Integer.parseInt(exp[1]) ;
			int days = 0;
			if(!exp[2].contentEquals("-")){
				days = Integer.parseInt(exp[2]) ;
			}
			int months = 0;
			if(!exp[3].contentEquals("-")){
				months = Integer.parseInt(exp[3]) ;
			}

			if( minutes != 0 ){
				modifyCal.add(Calendar.MINUTE, minutes);
			}
			if( hours != 0 ){
				modifyCal.add(Calendar.HOUR_OF_DAY, hours);
			}
			if( days != 0 ){
				modifyCal.add(Calendar.DATE, days);
			}
			if( months != 0 ){
				modifyCal.add(Calendar.MONTH, months-1);
			}

			this.addSchedule(instance, this.getTracingTag(), modifyCal, null);
		}else{
			fireComplete(instance);
		}


// quartz 에서는 해당 Timer Event 를 호출한다:
//		instance.getProcessDefinition().fireMessage("timer", instance, data.getTracingTag());
		if(payload instanceof String){
			String message = (String)payload;

			if(getName().equals(message)){

				fireComplete(instance);  //let event starts

				return true;

			}


		}

		return false;
	}

	@Override
	public String getMessage() {
		return "timer";
	}

	protected void addSchedule(ProcessInstance instance, String tracingTag, Calendar modifyCal, String expression) throws Exception {

		DataSource dataSource = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlOldSchDelete =  "DELETE FROM SCHEDULE_TABLE WHERE INSTID = ? AND TRCTAG = ? ";
		String sqlSEQ = "SELECT MAX(SCHE_IDX) FROM SCHEDULE_TABLE";
		String sql = " INSERT INTO SCHEDULE_TABLE(SCHE_IDX, INSTID, TRCTAG, STARTDATE, expression, newInstance,defId,GLOBALCOM) VALUES(?, ?, ?, ?, ?, ?, ?, ?) ";

		try {
			ApplicationContext xml = new ClassPathXmlApplicationContext("spring-config.xml");
			dataSource = (DataSource) xml.getBean("dataSource");
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sqlSEQ);
			rs = pstmt.executeQuery();

			int max = 0;
			if (rs.next()) {
				max = rs.getInt(1);
			}
			rs.close();
			pstmt.close();

			pstmt = conn.prepareStatement(sqlOldSchDelete);
			//TODO:
			pstmt.setString(1, instance.getInstanceId().split("_")[1]);
			pstmt.setString(2, tracingTag);
			pstmt.executeUpdate();
			pstmt.close();

//			int newInstance = 0;
//			if( this.getIncomingTransitions().size() == 0 ){
//				// TODO intermidediate 일 경우에는 incomming이 없고, 엑티비티안쪽에서 작용하는 것이라 새로운 인스턴스를 발행하는게 아니라 다른 방식이 필요함
//				newInstance = 1;
//			}
//			String globalcom = null;
//			ActivityReference ref =instance.getProcessDefinition().getInitiatorHumanActivityReference(instance.getProcessTransactionContext());
//			Activity humanActivity = ref.getActivity();
//			if( humanActivity != null ){
//				RoleMapping rm = instance.getRoleMapping(((HumanActivity)humanActivity).getRole().getName());
//				Employee emp = new Employee();
//				emp.setEmpCode(rm.getEndpoint());
//				globalcom = emp.databaseMe().getGlobalCom();
//			}

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, max == 0 ? 1 : max + 1);
			//TODO :
			pstmt.setString(2, instance.getInstanceId().split("_")[1]);
			pstmt.setString(3, tracingTag);
			pstmt.setTimestamp(4, new Timestamp(modifyCal.getTimeInMillis()));
			pstmt.setString(5, expression);
			pstmt.setInt(6, 1);
			pstmt.setString(7, instance.getProcessDefinition().getId());
			//TODO :
			pstmt.setString(8, "uengine");
			pstmt.executeUpdate();

		} catch (Exception e) {
			throw e;
		}  finally {
			if (rs!= null) try { rs.close(); } catch (Exception e) { }
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) { }
			if (conn != null) try { conn.close(); } catch (Exception e) { }
		}


	}
}
