package org.uengine.kernel;

import org.uengine.persistence.dao.DAOFactory;
import org.uengine.persistence.processdefinition.*;
import org.uengine.persistence.processdefinitionversion.ProcessDefinitionVersionRepositoryHomeLocal;
import org.uengine.persistence.processdefinitionversion.ProcessDefinitionVersionRepositoryHomeLocalImpl;
import org.uengine.persistence.processinstance.ProcessInstanceRepositoryHomeLocal;
import org.uengine.persistence.processvariable.ProcessVariableRepositoryHomeLocal;
import org.uengine.persistence.rolemapping.RoleMappingRepositoryHomeLocal;
import org.uengine.processmanager.SimulatorTransactionContext;
import org.uengine.processmanager.TransactionContext;
import org.uengine.util.UEngineUtil;
import org.uengine.util.dao.ConnectiveDAO;
import org.uengine.util.dao.GenericDAO;
import org.uengine.util.dao.IDAO;
import org.uengine.util.resources.PropertyResourceBundle;
import org.uengine.util.resources.ResourceBundle;
import org.uengine.webservice.*;
//import java.util.*;

//for the method 'getServiceProvider()' 

import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
//end

/**
 * @author Jinyoung Jang
 */

import javax.naming.InitialContext;
import javax.xml.namespace.*;

public class GlobalContext{
	
	//for backward-compatibility
	public final static int UENGINE_VER = 1020;
	public final static String STR_UENGINE_VER = "3.0";
	
	
	//fixed value. This value never be changed even if uEngine version goes up.
	public final static long SERIALIZATION_UID = 1234L;
	//

	public final static String SETTING_FILE = "c:/uengine/settings.properties";
	public final static String RECENT_URLS_FILE = "recenturls.properties";
	public final static String PROPERTY_STUB_DIR = "uEngine.stub.directory";
	public final static String PROPERTY_ANT_PATH = "uEngine.ant.path";
	public final static String PROPERTY_TEMP_PATH = "uEngine.temp.path";
	public final static String PROPERTY_AXIS_WEBAPP_DIR = "uEngine.axis.directory";
	public final static String PROPERTY_JBOSS_HOME_DIR = "uEngine.jboss.home";
	public final static String PROPERTY_UENGINE_HOME_DIR = "uEngine.home";
	public final static String PROPERTY_PREVIEW_SERVER_HOST = "preview.server.host";
	

	public final static String PROPERTY_FASTCAT_SERVER_HOST = "fastcat.server.host";
	public final static String PROPERTY_FASTCAT_SERVER_PORT = "fastcat.server.port";
	public final static String PROPERTY_FASTCAT_CONTEXT_ROOT = "fastcat.context.root";
	public final static String PROPERTY_FASTCAT_SEARCH_CONTEXTNAME = "fastcat.search.contextname";
	
	//public final static String ENCODING = "ISO-8859-1";
	public final static String ENCODING = "UTF-8";
	public final static String DATABASE_ENCODING = "UTF-8";
	public final static int DATABASE_MAXSTRLENGTH = 1300;
	public static final String GLOBAL_PROCESS_PATH = GlobalContext.getPropertyString("global.process.name", "_global.process");
	public static String WEB_CONTEXT_ROOT = GlobalContext.getPropertyString("web.context.root", "/uengine-web");
	
	public final static boolean useEJB = "true".equals(GlobalContext.getPropertyString("use.ejb", "false"));
	public final static boolean useManagedTransaction = "true".equals(GlobalContext.getPropertyString("use.managedtransaction", "false"));
	public final static boolean useAutoUserTransactionDemarcation = "true".equals(GlobalContext.getPropertyString("use.auto.usertransaction.demarcation", "false"));
	public final static boolean captureSystemOut = "true".equals(GlobalContext.getPropertyString("capture.system.out", "false"));
	public final static boolean logLevelIsDebug = "debug".equals(GlobalContext.getPropertyString("log.level", "prod"));
	public final static boolean CONNECTION_LEAKAGE_DETECT = "true".equals(GlobalContext.getPropertyString("connection.leakage.detect", "false"));
	public final static String FILE_SYSTEM_PATH = GlobalContext.getPropertyString("filesystem.path", "./");
	
	public final static String USERTRANSACTION_JNDI_NAME = GlobalContext.getPropertyString("usertransaction.jndiname", "UserTransaction");
	public final static String ACTIVITY_DESCRIPTION_COMPONENT_OVERRIDER_PACKAGE = GlobalContext.getPropertyString("activitydescriptioncomponent.override.package");
	public final static String DEFAULT_LOCALE = GlobalContext.getPropertyString("default.locale", Locale.getDefault().getLanguage());
	
	public final static String DATASOURCE_JNDI_NAME = GlobalContext.getPropertyString("datasource.jndiname", null);
	public final static String JDBC_DRIVERCLASSNAME = GlobalContext.getPropertyString("jdbc.driverClassName", null);
	public final static String JDBC_URL = GlobalContext.getPropertyString("jdbc.url", null);
	public final static String JDBC_USERNAME = GlobalContext.getPropertyString("jdbc.username", null);
	public final static String JDBC_PASSWORD = GlobalContext.getPropertyString("jdbc.password", null);
	public final static String JDBC_VALIDATION_QUERY = GlobalContext.getPropertyString("jdbc.validationQuery", null);
	public final static int JDBC_MAX_ACTIVE = Integer.parseInt(GlobalContext.getPropertyString("jdbc.maxActive", "20"));
	public final static int JDBC_MAX_IDLE = Integer.parseInt(GlobalContext.getPropertyString("jdbc.maxIdle", "10"));
	public final static long JDBC_MAX_WAIT = Long.parseLong(GlobalContext.getPropertyString("jdbc.maxWait", "-1"));
	
	public static ProcessDefinitionRepositoryHomeLocal processDefinitionRepositoryHomeLocal;
	public static ProcessDefinitionVersionRepositoryHomeLocal processDefinitionVersionRepositoryHomeLocal;
	public static ProcessInstanceRepositoryHomeLocal processInstanceRepositoryHomeLocal;
	public static ProcessVariableRepositoryHomeLocal processVariableRepositoryHomeLocal;
	public static RoleMappingRepositoryHomeLocal roleMappingRepositoryHomeLocal;

	static Hashtable serializers;
	static Hashtable serviceProviders;
	static Hashtable resources;
	static Properties properties;
	static Properties sqls;
	static{
		serializers = new Hashtable();		
		resources = new Hashtable();	
		serviceProviders = new Hashtable();		
	}
	
	private static ResourceBundle messages = null;
	private static Hashtable webMessageBundles = new Hashtable();
	public static boolean multiTenant = "true".equals(GlobalContext.getPropertyString("multi-tenant", "true"));


	protected GlobalContext(){}

/*	public static Serializer getSerializer(String mechanism){
		try{
			Class SerCls = Thread.currentThread().getContextClassLoader().loadClass("org.uengine.components.serializers."+ mechanism);
				
			Serializer ser = (Serializer)SerCls.getConstructor(new Class[]{}).newInstance(new Object[]{});
			return ser;
		}catch(Exception e){e.printStackTrace();}
		
		return null;
	}
*/
	public static String getDefaultLocale(){
		return DEFAULT_LOCALE;
	}
	
	public static void setDefaultLocale(String locale) {
		if (properties != null && !Locale.getDefault().getLanguage().equals(locale)) {
			properties.put("default.locale", locale);
		}
		if (messages != null && !messages.getLocale().getLanguage().equals(locale)) {
			messages = PropertyResourceBundle.getBundle("org.uengine.resources.messages", new Locale(locale), GlobalContext.class.getClassLoader());
		}
	}
	
	static boolean isDesignTime;
		public static boolean isDesignTime() {
			return isDesignTime;
		}
		public static void setDesignTime(boolean isDesignTime_) {
			isDesignTime = isDesignTime_;
		}
		
	static boolean wasIsJeus = false;
	public static URL getResourceURL(String path) throws ClassNotFoundException{
		URL url = null;
		if(!wasIsJeus)
			url= GlobalContext.getClassLoader().getResource(path);
		
		if(url ==null){
			url = Thread.currentThread().getContextClassLoader().getResource(path);
			wasIsJeus = true;
		}
		
		return  url;
	}
	
	public static Class loadClass(String className) throws ClassNotFoundException {
		Class type = null;
		if(!wasIsJeus)
			type = Thread.currentThread().getContextClassLoader().loadClass(className);
		
		if(type ==null){
			type =  Thread.currentThread().getContextClassLoader().loadClass(className);
			wasIsJeus = true;
		}
		
		return  type;
	}
	
	public static ClassLoader getClassLoader() throws ClassNotFoundException{
		
		ClassLoader loader = null;
		
		if(!wasIsJeus)
			loader = Thread.currentThread().getContextClassLoader();
		
		if(loader ==null){
			loader =  Thread.currentThread().getContextClassLoader();
			wasIsJeus = true;
		}
		
		return loader;
	}
		
	public static Properties getProperties() {
		if(properties==null){
			try {
				URL url = null;
				
				url = getResourceURL("org/uengine/uengine.properties");

				if (url != null) {
					InputStream is = url.openStream();
					properties = new Properties();
					properties.load(is);
					is.close();
					
					if(properties.containsKey("content.reference.url")){
						String realFileURL = (String) properties.get("content.reference.url");
						is = new URL(realFileURL).openStream();
						properties.load(is);
						is.close();
					}

					System.out.println("Loading " + url);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		return properties;
	}
		
	public static String getPropertyString(String key, String defaultValue){
		Properties sysProp = getProperties();
			
		if(sysProp!=null)
			return sysProp.getProperty(key, defaultValue);
				
		return defaultValue;
	}
	
	public static String getPropertyString(String key){
		return getPropertyString(key, null);
	}
	
	public static String[] getPropertyStringArray(String key, String[] defaultValue){
		String value = getPropertyString(key);
				
		if(value!=null)
			return value.split("[, ]");
				
		return defaultValue;
	}
	
	public static String[] getPropertyStringArray(String key){
		return getPropertyStringArray(key, null);
	}

	public static final String getLocalizedMessage(String key, String defaultValue) {
		try{
			Locale locale = new Locale(properties.getProperty("default.locale", Locale.getDefault().getLanguage()));
			if(messages==null)
				messages = PropertyResourceBundle.getBundle("org.uengine.resources.messages", locale, GlobalContext.class.getClassLoader());
				
			return messages.getString(key);
		}catch(Exception e){
			return defaultValue;
		}
	}
	
	public static final String getLocalizedMessage(String key, String _locale, String defaultValue) {
		try{
			Locale locale = null;
			if (UEngineUtil.isNotEmpty(_locale)) {
				locale = new Locale(_locale);
			} else {
				locale = new Locale(Locale.getDefault().getLanguage());
			}
//			if(messages==null)
				messages = PropertyResourceBundle.getBundle("org.uengine.resources.messages", locale, GlobalContext.class.getClassLoader());
				
			return messages.getString(key);
		}catch(Exception e){
			return defaultValue;
		}
	}
	
	public static final String getLocalizedMessageForWeb(String key, String language, String defaultValue) {
		try{
			if(!webMessageBundles.containsKey(language)){
				Locale locale = new Locale(language);
				PropertyResourceBundle propertyResourceBundle = (PropertyResourceBundle) PropertyResourceBundle.getBundle("org.uengine.messages", locale, GlobalContext.getClassLoader());
				
				if(!propertyResourceBundle.getLocale().getLanguage().equals(language)) return defaultValue;
				
				webMessageBundles.put(language, propertyResourceBundle);
			}
			ResourceBundle webMessageBundle = (ResourceBundle) webMessageBundles.get(language);
			
			return webMessageBundle.getString(key);

		}catch(Exception e){
			return defaultValue;
		}
	}
	
	public static final String getMessageForWeb(String key, String language) {
		try {
			String tmpKey = key.toLowerCase();
			tmpKey = tmpKey.replaceAll(" ", "_");
			
			key = key.replaceAll("_", " ");
			
			if (!webMessageBundles.containsKey(language)) {
				Locale locale = new Locale(language);
				PropertyResourceBundle propertyResourceBundle = (PropertyResourceBundle) PropertyResourceBundle.getBundle("org.uengine.messages", locale, GlobalContext.getClassLoader());
				
				if (!propertyResourceBundle.getLocale().getLanguage().equals(language)) return key;
				
				webMessageBundles.put(language, propertyResourceBundle);
			}
			ResourceBundle webMessageBundle = (ResourceBundle) webMessageBundles.get(language);
			
			return webMessageBundle.getString(tmpKey);

		} catch(Exception e) {
			return key;
		}
	}
	
	public static final String getSQL(String key){
		if(sqls==null){
			try {
				String resourceURL = "org/uengine/resources/sqls/" + DAOFactory.getInstance(null).getDBMSProductName().toLowerCase() +".properties";
				URL url = null;
				
				url = GlobalContext.class.getClassLoader().getResource(resourceURL);

				if (url != null) {
					InputStream is = url.openStream();
					sqls = new Properties();
					sqls.load(is);
					is.close();

					System.out.println("Loading sqls : " + url);
				}else{
					System.out.println("Please check whether resource '" + resourceURL +"' exists. It is very important to start uEngine.");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
					
		return sqls.getProperty(key);
	}
	
	public static final String getLocalizedMessage(String key) {
		return getLocalizedMessage(key, null);
	}
	
	public static ServiceProvider getServiceProvider(String serviceKey) throws Exception{
		//TODO: caching mechanism required
		Class SPCls = getComponentClass(serviceKey);
System.out.println("GlobalContext::getServiceProvider SPCls is : "+ SPCls.getSuperclass().getName());

		ServiceProvider sp = (ServiceProvider)SPCls.getConstructor(new Class[]{}).newInstance(new Object[]{});
		return sp;
	}
	
	public static ServiceProvider getServiceProvider(ServiceDefinition serviceDefinition, String portType) throws Exception{
		//TODO: caching mechanism required
		if(serviceDefinition==null) return getServiceProvider(portType); //for old version
		
		return new DefaultServiceProvider(serviceDefinition, portType);
	}
	
	public static ClassLoader getComponentClassLoader() throws Exception{
		//I decided not to use proprietary class loading
		return GlobalContext.class.getClassLoader();
		
		/*
		File stubDir = new File("."+ System.getProperty("file.separator") + "servicestubs" + System.getProperty("file.separator"));
		File[] fs = (stubDir).listFiles();
		Vector urlVector = new Vector();
		
System.out.println("GlobalContext::getServiceProvider stub directory = "+ stubDir);
		if(fs.length > 0)
		for( int i=0; i< fs.length; i++){
			if( fs[i].getName().endsWith(".jar")){
				String path = "jar:file:/" + fs[i].getAbsolutePath() + "!/";
				path = path.replace('\\', '/');
System.out.println("	found a file : "+ path);
				
				urlVector.add(new URL(path));
			}
		}		
		URL[] urls = new URL[urlVector.size()];
		urlVector.toArray(urls);
				
		URLClassLoader ucl = new URLClassLoader(urls, GlobalContext.class.getClassLoader());
		
		return ucl;*/
	}
	
	public static Class getComponentClass(String compName) throws Exception{
		//TODO: caching mechanism required
//		Class SPCls = Thread.currentThread().getContextClassLoader().loadClass("org.uengine.components.serviceproviders."+ serviceKey);
		Class compCls = getComponentClassLoader().loadClass(compName);

		return compCls;
	}
	

	public static Serializer getSerializer(QName qname) throws Exception{	
		if(serializers.containsKey(qname.getNamespaceURI())){
			return (Serializer)serializers.get(qname);
		}else{
			try{
				Class serCls = getXMLBindingClass(qname);
				serializers.put(qname.getNamespaceURI(), serCls.newInstance());
				
				return (Serializer)serializers.get(qname);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public static Class getXMLBindingClass(QName qname) throws Exception{
		String namespaceURI = qname.getNamespaceURI();
		String localPart = qname.getLocalPart();		
		String clsPackageName = namespaceURI.replace(':', '_').replace('/', '_').replace('\\', '_').replace('.', '_');

		//try getting already compiled binding classes
		try{
			return _getXMLBindingClass(clsPackageName, localPart);
		}catch(Exception e){
			e.printStackTrace();
		}
				
		//if fails, dynamically generates binding classes
		//let's try this feature in the next version
/*		org.exolab.castor.builder.SourceGenerator.main(
			new String[]{
				"-i", namespaceURI,
				"-f",
				"-package", "xmlbindings."+clsPackageName
			}
		);

//recode required: performance
		String absolutePath = "xmlbindings"+ System.getProperties().get("file.separator")+ clsPackageName;

		File f = new File(absolutePath);
		File[] fs = f.listFiles();		
//System.out.println(fs.length);

		com.sun.tools.javac.Main compiler = new com.sun.tools.javac.Main();
		for( int i=0; i < fs.length; i++){
System.out.println("compiling:" + fs[i]);
			if( fs[i].getName().endsWith(".java")){
				compiler.compile(
					new String[]{
						"-classpath", System.getProperties().get("java.class.path").toString(),
						absolutePath + "\\" + fs[i].getName()
					}
				);
			}
		}
System.out.println("compilation done");

		//recode require:
		//try getting already compiled binding classes
		try{
			return _getXMLBindingClass(clsPackageName, localPart);
		}catch(Exception e){
			e.printStackTrace();
		}*/
		
		return null;
	}
	
	private static Class _getXMLBindingClass(String clsPackageName, String localPart) throws Exception{
		File f = new File("xmlbindings"+ System.getProperties().get("file.separator"));
		String xmlBindingClassPath = f.getAbsolutePath().replace('\\', '/');
		
		URL[] urls = {new URL(xmlBindingClassPath)};
		URLClassLoader ucl = new URLClassLoader(urls);
		
		return ucl.loadClass("xmlbindings."+clsPackageName+"."+localPart);
	}
	
	static final Serializer beanSerializerInstance = new org.uengine.components.serializers.BeanSerializer();
	static Serializer xpdSerializerInstance;
	static{
		try {
			xpdSerializerInstance = (Serializer) Class.forName(getPropertyString("default.serializer.class", "org.uengine.components.serializers.XPDSerializer")).newInstance();
		} catch (Exception e) {
			xpdSerializerInstance = new org.uengine.components.serializers.XPDSerializer();
		}
	}
	static final Serializer roleMappingSerializerInstance = new org.uengine.components.serializers.org_uengine_kernel_RoleMappingSerializer();
//	static final Serializer axisBeanSerializerInstance = new org.uengine.components.serializers.AxisBeanSerializer();
	public static Serializer getSerializer(Class cls){
		//TODO: hard-coded now but serializers for each class type may be vary.
		//now we hard-coded
		
//Our Serializer/Deserializer for Axis object doesn't work properly now.
//		if(axisBeanSerializerInstance.isSerializable(cls)) return axisBeanSerializerInstance;
		if(cls==RoleMapping.class){
			return roleMappingSerializerInstance;
		}
		
		//return beanSerializerInstance;
		return xpdSerializerInstance;
	}
	
	public static Serializer getSerializer(String encodingStyle){
		if(serializers.containsKey(encodingStyle))
			return (Serializer)serializers.get(encodingStyle);
		
		try{
			Serializer ser = (Serializer)Thread.currentThread().getContextClassLoader().loadClass("org.uengine.components.serializers." + encodingStyle + "Serializer").newInstance();
			serializers.put(encodingStyle, ser);
			return ser;
		}catch(Exception e){
			return null;
		}
	}
	
	//default serialization
	public static void serialize(Object obj, OutputStream os, Class cls) throws Exception{
		getSerializer(cls).serialize(obj, os, null);
	}

	public static void serialize(Object obj, OutputStream os, String encodingStyle) throws Exception{
		serialize(obj, os, encodingStyle, null);
	}
	
	public static void serialize(Object obj, OutputStream os, String encodingStyle, Hashtable option) throws Exception{
		getSerializer(encodingStyle).serialize(obj, os, option);
	}

	public static String serialize(Object obj, Class cls) throws Exception{
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		serialize(obj, bao, cls);		
		return bao.toString(DATABASE_ENCODING);
	}
	
	public static Object deserialize(String src, Class cls) throws Exception{
		ByteArrayInputStream bis = new ByteArrayInputStream(src.getBytes(DATABASE_ENCODING));
		
		return deserialize(bis, cls);
	}
	
	public static Object deserialize(String src) throws Exception{
		return deserialize(src, String.class);
	}
	
	public static Object deserialize(InputStream is, Class cls) throws Exception{
		Hashtable ctx = new Hashtable();
		if(cls!=null){
			ctx.put("targetClass", cls);
		}	
		return getSerializer(cls).deserialize(is, ctx);
	}
	
	public static Object deserialize(InputStream is, String encodingStyle) throws Exception{
		return getSerializer(encodingStyle).deserialize(is, null);
	}
	
	//serialization with Qualified Name (QName)
	public static void serialize(Object obj, OutputStream os, QName qName) throws Exception{
		Hashtable extCtx = new Hashtable();
		if(qName!=null){
			extCtx.put("qName", qName);
		}
		
		getSerializer(obj.getClass()).serialize(obj, os, extCtx);
	}
	
	public static Object deserialize(InputStream is, QName qName) throws Exception{
		return getSerializer(qName).deserialize(is, null);
	}
	
	//TODO: robust and generic XML processing is required
	//serialzation with ProcessVariableDescriptor's type infomation
	public static void serialize(Object obj, OutputStream os, ProcessVariable pd) throws Exception{
		if(pd==null)
			serialize(obj, os, String.class);
		else
			
		/*if(pd.getXmlBindingClassName()!=null){ //if the target variable is a business document*/
		if(pd.getQName()!=null){
			serialize(obj, os, pd.getQName()); //xml serialization with custom serializer
		}else{ 			 //if the target variable is a simple property			
			if(obj!=null)
				serialize(obj, os, obj.getClass()); //use BeanSerializer (which is generic but not powerful**)
			/*else
				serialize(obj, os, String.class);*/
		}		
	}
	
	public static Object deserialize(InputStream is, ProcessVariable pd) throws Exception{
		if(pd==null)
			return deserialize(is, String.class);
	
		/*if(pd.getXmlBindingClassName()!=null){ //if the source is a business document
System.out.println("GlobalContext::deserialize: the case that source is a business document");
			return deserialize(is, getComponentClass(pd.getXmlBindingClassName()));
		}else{ 			 //if the source is a simple property*/
			return deserialize(is, pd.getType()); //use BeanSerializer (which is generic but not powerful**)
//		}
	}
	//
	
	public static ProcessDefinitionVersionRepositoryHomeLocal createProcessDefinitionVersionRepositoryHomeLocal(TransactionContext tc) throws Exception{
		if(!useEJB)
			return new ProcessDefinitionVersionRepositoryHomeLocalImpl(tc);
		
		if(processDefinitionVersionRepositoryHomeLocal==null){
			processDefinitionVersionRepositoryHomeLocal =
			(ProcessDefinitionVersionRepositoryHomeLocal)getInitialContext().lookup("ProcessDefinitionVersionRepositoryHomeLocal");
		}

		return processDefinitionVersionRepositoryHomeLocal;
	}

	public static ProcessDefinitionRepositoryHomeLocal createProcessDefinitionRepositoryHomeLocal(TransactionContext tc) throws Exception{
		
		if(!useEJB)
			return new ProcessDefinitionRepositoryHomeLocalImpl(tc);
		
		if(processDefinitionRepositoryHomeLocal==null){
			processDefinitionRepositoryHomeLocal =
				(ProcessDefinitionRepositoryHomeLocal)
				getInitialContext().lookup("ProcessDefinitionRepositoryHomeLocal");
		}
		
		return processDefinitionRepositoryHomeLocal;
	}

	public static ProcessInstanceRepositoryHomeLocal createProcessInstanceRepositoryHomeLocal() throws Exception{
		if(processInstanceRepositoryHomeLocal==null){
			processInstanceRepositoryHomeLocal =
			(ProcessInstanceRepositoryHomeLocal)getInitialContext().lookup("ProcessInstanceRepositoryHomeLocal");
		}

		return processInstanceRepositoryHomeLocal;
	}
	
	public static ProcessVariableRepositoryHomeLocal createProcessVariableRepositoryHomeLocal() throws Exception{
		if(processVariableRepositoryHomeLocal==null){
			processVariableRepositoryHomeLocal =
			(ProcessVariableRepositoryHomeLocal)getInitialContext().lookup("ProcessVariableRepositoryHomeLocal");
		}

		return processVariableRepositoryHomeLocal;
	}

	public static RoleMappingRepositoryHomeLocal createRoleMappingRepositoryHomeLocal() throws Exception{
		if(roleMappingRepositoryHomeLocal==null){
			roleMappingRepositoryHomeLocal =
			(RoleMappingRepositoryHomeLocal)getInitialContext().lookup("RoleMappingRepositoryHomeLocal");
		}

		return roleMappingRepositoryHomeLocal;
	}

	public static InitialContext getInitialContext() throws Exception{
		return new InitialContext();
	}

//	public static void updateLog(int countId, String logid, long time) throws Exception {
//		IDAO logDAO = GenericDAO.createDAOImpl("java:/uEngineDS", "update BPM_PERFORMANCE set "+logid+"=?"+logid+" where countid=?countid", IDAO.class);
//		logDAO.set("countId", new BigDecimal(countId));
//		logDAO.set(logid, new BigDecimal(time));
//		logDAO.update();
//	}
	
	public static Calendar getNow(TransactionContext tc) throws Exception{
		
		if(tc==null || tc instanceof SimulatorTransactionContext) return Calendar.getInstance();
		
		DAOFactory daoFactory = DAOFactory.getInstance(tc);
		return daoFactory.getNow();
	}


}