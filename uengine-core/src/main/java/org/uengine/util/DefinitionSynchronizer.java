package org.uengine.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.uengine.kernel.UEngineException;
import org.uengine.util.ForLoop;
import org.uengine.util.dao.AbstractGenericDAO;
import org.uengine.util.dao.ConnectionFactory;
import org.uengine.util.dao.ConnectiveDAO;
import org.uengine.util.dao.GenericDAO;
import org.uengine.util.dao.IDAO;
import org.uengine.util.dao.JDBCConnectionFactory;

public class DefinitionSynchronizer {
	
	static String SQL_RETREIVETARGETSTARTINGPOINTS = 
		"select * from																																												"+	
		" (																																															"+
		"  select 1 as sychOrder, max(modifieddate) as recentmodified, null as lastkey	,  'bpm_procfld' as objectname		, 'folderid' as keyfieldname 			from bpm_procfld				"+
		"  union																																													"+
		"  select 2 as sychOrder, max(modifieddate) as recentmodified, null as lastkey	,  'bpm_procdef' as objectname		, 'definitionid' as keyfieldname 		from bpm_procdef				"+
		"  union																																													"+
		"  select 3 as sychOrder, max(modifieddate) as recentmodified, null as lastkey	,  'bpm_procdefver' as objectname	, 'definitionversionid' as keyfieldname from bpm_procdefver				"+
		"  union																																													"+
		"  select 4 as sychOrder, max(modifieddate) as recentmodified, null as lastkey	,  'bpm_formfld' as objectname		, 'folderid' as keyfieldname 			from bpm_formfld				"+
		"  union																																													"+
		"  select 5 as sychOrder, max(modifieddate)	as recentmodified, null as lastkey	,  'bpm_form' as objectname			, 'formid' as keyfieldname 				from bpm_form					"+
		"  union																																													"+
		"  select 6 as sychOrder, max(modifieddate) as recentmodified, null as lastkey	,  'bpm_formver' as objectname		, 'formversionid' as keyfieldname 		from bpm_formver				"+
		"  union																																													"+
		"  select 7 as sychOrder, null as recentmodified	  , max(attrid) as lastkey	,  'bpm_form_attr' as objectname	, 'attrid' as keyfieldname 				from bpm_form_attr				"+
		" )	order by sychOrder																																										"
	;
	
	public class SychronizationStartingPoint{
		public String objectName;
		public String keyFieldName;
		public Object startingPoint;
		
		public String getKeyFieldName() {
			return keyFieldName;
		}
		public void setKeyFieldName(String keyFieldName) {
			this.keyFieldName = keyFieldName;
		}
		public String getObjectName() {
			return objectName;
		}
		public void setObjectName(String objectName) {
			this.objectName = objectName;
		}
		public Object getStartingPoint() {
			return startingPoint;
		}
		public void setStartingPoint(Object startingPoint) {
			this.startingPoint = startingPoint;
		}
	}

	ConnectionFactory sourceConnectionFactory;
		public ConnectionFactory getSourceConnectionFactory() {
			return sourceConnectionFactory;
		}
		public void setSourceConnectionFactory(ConnectionFactory connectionFactory) {
			this.sourceConnectionFactory = connectionFactory;
		}
		
	ConnectionFactory targetConnectionFactory;
		public ConnectionFactory getTargetConnectionFactory() {
			return targetConnectionFactory;
		}
		public void setTargetConnectionFactory(ConnectionFactory targetConnectionFactory) {
			this.targetConnectionFactory = targetConnectionFactory;
		}	
		
	boolean stopSignaled = false;

	protected void run() throws Exception{

		JFrame frm = new JFrame("stopper");
		JButton stopBtn = new JButton("STOP");
		frm.getContentPane().add(stopBtn);
		stopBtn.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				System.out.println(". User cancelled this process.");
				stop();
				//System.exit(0);
			}
			
		});
		frm.pack();
		frm.setVisible(true);
		
		final Connection targetConnection = getTargetConnectionFactory().getConnection();
		targetConnection.setAutoCommit(false);
		try{
			final Map synchronizationTargets = retreiveTargetStartingPoints();
	
		    Iterator i = synchronizationTargets.keySet().iterator();
		    while (i.hasNext() && !stopSignaled) {
		    	String objectName = (String) i.next();
				SychronizationStartingPoint startingPoint = (SychronizationStartingPoint)synchronizationTargets.get(objectName);
				synchronize(startingPoint.getStartingPoint(), objectName, startingPoint.getKeyFieldName(), targetConnection);
			};
			
			String answer = "R";
			if(!stopSignaled){
				System.out.println("? Commit or rollback these changes? (C/R)");
				answer = getNewStringFromConsole();
			}
			
			if("C".equalsIgnoreCase(answer)){
				targetConnection.commit();
				System.out.println(". Committed.");
			}else{
				targetConnection.rollback();
				System.out.println(". Rolled back.");
			}

		}catch(Exception e){			
			targetConnection.rollback();

			e.printStackTrace();
			System.out.println(". All changes are rolled back.");
			throw e;
		}finally{
			targetConnection.close();
			System.exit(0);
		}
	}
	
	protected void stop(){
		stopSignaled = true;
	}
		
	protected Map retreiveTargetStartingPoints() throws Exception{
		Map targetKeys = new HashMap();
		
		IDAO targetKeysDAO = GenericDAO.createDAOImpl(getTargetConnectionFactory(), SQL_RETREIVETARGETSTARTINGPOINTS, IDAO.class);
		targetKeysDAO.select();
		if(targetKeysDAO.size() ==0) throw new UEngineException("Failed to retreive starting points from source server: No rows are returned.");
		
		while(targetKeysDAO.next()){
			Date recentModified = (Date)targetKeysDAO.get("recentModified");
			Number lastKey = (Number)targetKeysDAO.get("lastkey");
			String objectName = (String)targetKeysDAO.get("objectName");
			
			SychronizationStartingPoint startingPoint = new SychronizationStartingPoint();
			
			startingPoint.setKeyFieldName((String)targetKeysDAO.get("keyFieldName"));
			startingPoint.setObjectName((String)targetKeysDAO.get("objectName"));
			
			if(recentModified != null)
				startingPoint.setStartingPoint(recentModified);
			else
				startingPoint.setStartingPoint(lastKey);
			
			targetKeys.put(objectName, startingPoint);
		}
		
		//close CachedRowset
		targetKeysDAO.releaseResource();
		
		return targetKeys;
	}
	
	protected void synchronize(Object startingKey, String objectName, String keyFieldName, final Connection targetConnectionInTransaction) throws Exception{
		
		boolean startingKeyIsModifiedDate = startingKey instanceof Date;
		String SQL_RETREIVE_SOURCE = null;
		
		if(startingKeyIsModifiedDate){
			System.out.println("Synchronizing ["+ objectName +"] data from " + startingKey + " (Modified Date)...");
			SQL_RETREIVE_SOURCE = "select * from " + objectName + " where modifiedDate > ?key";
		}else{
			System.out.println("Synchronizing ["+ objectName +"] data from " + startingKey + " (Last Key)...");
			SQL_RETREIVE_SOURCE = "select * from " + objectName + " where " + keyFieldName + " > ?key";
		}

		/**
		 * Retreives the source data first.
		 */
		
		IDAO sourceDAO = GenericDAO.createDAOImpl(getSourceConnectionFactory(), SQL_RETREIVE_SOURCE, IDAO.class);
		sourceDAO.set("key", startingKey);
		sourceDAO.select();
		
		AbstractGenericDAO implSourceDAO = (AbstractGenericDAO)sourceDAO.getImplementationObject();
		
		/**
		 * Change the connection information of the source data in order to store to the target server.
		 */
		ConnectionFactory targetConnectionFactoryInTransaction = new ConnectionFactory(){
			public Connection getConnection() throws Exception{				
				return targetConnectionInTransaction;
			}
		};
		
		implSourceDAO.setConnectionFactory(targetConnectionFactoryInTransaction);

		/**
		 * Let the SQLs auto-generated again.
		 */
		implSourceDAO.setConnective(true);
		implSourceDAO.setSqlStmt(null);
		implSourceDAO.setAutoSQLGeneration(true);
		implSourceDAO.setTableName(objectName);
		implSourceDAO.setKeyField(keyFieldName);
		
		/** Create and store the insert and update SQL in order to use them later
		 * since we don't have to generate them again and again within the following while-loop.
		 */		
		implSourceDAO.createInsertSql();
		String insertSql = implSourceDAO.getSqlStmt();
		
		implSourceDAO.createUpdateSql();
		String updateSql = implSourceDAO.getSqlStmt();
		
		
		/**
		 * this loop will insert(if new) or update(if exist) all of the source rowset
		 */		
		while(sourceDAO.next() && !stopSignaled){
			System.out.print("	. "+ keyFieldName +"=["+ sourceDAO.get(keyFieldName) + "]");

			// this will check if the source value already exists
			IDAO testDAO = ConnectiveDAO.createDAOImpl(targetConnectionFactoryInTransaction, "select 1 from " + objectName + " where " + keyFieldName +" = ?key", IDAO.class);
			testDAO.set("key", sourceDAO.get(keyFieldName));
			testDAO.select();

			if(testDAO.size() > 0){
				implSourceDAO.setSqlStmt(updateSql);
				System.out.println(" [UPDATE]" + sourceDAO);
			}else{
				implSourceDAO.setSqlStmt(insertSql);
				System.out.println(" [INSERT]" + sourceDAO);
			}

			sourceDAO.update();
		}
		
		//close CachedRowset
		sourceDAO.releaseResource();
	}
	
	public static void main(String args[]) throws Exception{
		DefinitionSynchronizer ds = new DefinitionSynchronizer();
		
		JDBCConnectionFactory srcConnFactory = new JDBCConnectionFactory();{
			srcConnFactory.setConnectionString		("jdbc:oracle:thin:@10.10.8.6:1521:orcl");
			srcConnFactory.setDriverClass			("oracle.jdbc.OracleDriver");
			srcConnFactory.setUserId				("eagles_ep");
			srcConnFactory.setPassword				("eagles_ep_dba");
		}
		
		JDBCConnectionFactory targetConnFactory = new JDBCConnectionFactory();{
			targetConnFactory.setConnectionString	("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=10.10.9.219)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.10.9.220)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=epdb)))");
			targetConnFactory.setDriverClass		("oracle.jdbc.OracleDriver");
			targetConnFactory.setUserId				("eagles_ep");
			targetConnFactory.setPassword			("eagles_ep_dba");
		}
		
		System.out.println("uEngine Process Definition Synchronizer\n");
		System.out.println(". SOURCE database server info: \n" + srcConnFactory);
		System.out.println(". TARGET database server info: \n" + targetConnFactory);
		
		System.out.println("\n? All of above information is correct? (Y/N)");
		String answer = getNewStringFromConsole();
		
		if(!"Y".equalsIgnoreCase(answer)) return;
		
		System.out.println("? Are you sure to begin synchronization process? (Y/N)");
		answer = getNewStringFromConsole();
		
		if(!"Y".equalsIgnoreCase(answer)) return;
		
		ds.setSourceConnectionFactory(srcConnFactory);
		ds.setTargetConnectionFactory(targetConnFactory);
		ds.run();
	}

	public static String getNewStringFromConsole() throws Exception{
		DataInputStream bis = new DataInputStream(new BufferedInputStream(System.in));
		//String temp = bis.readLine();
		
		return null;
	}

}
