package org.uengine.kernel;

import java.io.Serializable;

import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.RadioInput;
import org.metaworks.inputter.SelectInput;

public class JMSQueueActivity extends DefaultActivity {

	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	public JMSQueueActivity(){
		setName("JMS");
	}
	
	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd;
		//type.setName("JMS Queueing");
		
		fd = type.getFieldDescriptor("AcknowledgeType");	
		fd.setInputter(
			new RadioInput(
				new String[]{"Auto(Recommended)", "Client acknowledge", "Dups Ok"}, 
				new Integer[]{
					new Integer(Session.AUTO_ACKNOWLEDGE), 
					new Integer(Session.CLIENT_ACKNOWLEDGE),
					new Integer(Session.DUPS_OK_ACKNOWLEDGE),
				}
			)
		);

		fd = type.getFieldDescriptor("JmsProvider");	
		fd.setInputter(
			new SelectInput(
				new Object[]{"JBossMQ", "SonicMQ", "ActiveMQ"} 
			)
		);
	}

	protected void executeActivity(ProcessInstance instance) throws Exception {
		InitialContext jndiContext = GlobalContext.getInitialContext();
		QueueConnectionFactory factory = (QueueConnectionFactory)jndiContext.lookup (getQueueConnectionFactoryJNDIName());
				
		Queue resultQueue = (Queue)jndiContext.lookup (getResultQueueJNDIName());
		QueueConnection connect = factory.createQueueConnection ();
														   //it doesn't need to issue new transaction context. it may occur an error in such env doesn't support two-phase commit.
		QueueSession session = connect.createQueueSession (isIssueNewTransaction(), getAcknowledgeType());
		QueueSender sender = session.createSender (resultQueue);
				
		MapMessage message = session.createMapMessage();
		
		if(getMessageFormat()!=null)
			message.setStringProperty ("MessageFormat", getMessageFormat());
		
		message.setJMSRedelivered(isJmsRedelivered());

		for(int i=0; i<propertyValues.length; i++){
			ParameterContext theParameter = propertyValues[i];
			String keyName = theParameter.getArgument().getText();
			Serializable value = theParameter.getVariable().get(instance, "");
			
			if(theParameter.getType()==Number.class){
				Number numValue = (Number) value;
				message.setLongProperty(keyName, numValue.longValue()); 
			}else if(theParameter.getType()==String.class){
				String strValue = (String) value;
				message.setStringProperty(keyName, strValue); 
			}else if(theParameter.getType()==Boolean.class){
				Boolean boolValue = (Boolean) value;
				message.setBooleanProperty(keyName, boolValue.booleanValue()); 
			}
		}

		for(int i=0; i<messageValues.length; i++){
			ParameterContext theParameter = messageValues[i];
			String keyName = theParameter.getArgument().getText();
			Serializable value = theParameter.getVariable().get(instance, "");
			
			if(theParameter.getType()==Number.class){
				Number numValue = (Number) value;
				message.setLong(keyName, numValue.longValue()); 
			}else if(theParameter.getType()==String.class){
				String strValue = (String) value;
				message.setString(keyName, strValue); 
			}else if(theParameter.getType()==Boolean.class){
				Boolean boolValue = (Boolean) value;
				message.setBoolean(keyName, boolValue.booleanValue()); 
			}
		}

		sender.send (message);
		connect.close ();
		
		fireComplete(instance);
	}
	
	
	String queueConnectionFactoryJNDIName;
	String resultQueueJNDIName;
	String jmsProvider;
	String messageFormat;
	boolean jmsRedelivered;
	boolean issueNewTransaction;
	int acknowledgeType;
	ParameterContext[] propertyValues;
	ParameterContext[] messageValues;

	public boolean isJmsRedelivered() {
		return jmsRedelivered;
	}
	public void setJmsRedelivered(boolean jmsRedelivered) {
		this.jmsRedelivered = jmsRedelivered;
	}
	public String getMessageFormat() {
		return messageFormat;
	}
	public void setMessageFormat(String messageFormat) {
		this.messageFormat = messageFormat;
	}

	public String getQueueConnectionFactoryJNDIName() {
		return queueConnectionFactoryJNDIName;
	}
	public void setQueueConnectionFactoryJNDIName(
			String queueConnectionFactoryJNDIName) {
		this.queueConnectionFactoryJNDIName = queueConnectionFactoryJNDIName;
	}
	public String getResultQueueJNDIName() {
		return resultQueueJNDIName;
	}
	public void setResultQueueJNDIName(String resultQueueJNDIName) {
		this.resultQueueJNDIName = resultQueueJNDIName;
	}
	public int getAcknowledgeType() {
		return acknowledgeType;
	}
	public void setAcknowledgeType(int acknowledgeType) {
		this.acknowledgeType = acknowledgeType;
	}
	public boolean isIssueNewTransaction() {
		return issueNewTransaction;
	}
	public void setIssueNewTransaction(boolean issueNewTransaction) {
		this.issueNewTransaction = issueNewTransaction;
	}

	public String getJmsProvider() {
		return jmsProvider;
	}

	public void setJmsProvider(String jmsProvider) {
		this.jmsProvider = jmsProvider;
	}

	public ParameterContext[] getMessageValues() {
		return messageValues;
	}

	public void setMessageValues(ParameterContext[] messageValues) {
		this.messageValues = messageValues;
	}

	public ParameterContext[] getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(ParameterContext[] propertyValues) {
		this.propertyValues = propertyValues;
	}
	
}
