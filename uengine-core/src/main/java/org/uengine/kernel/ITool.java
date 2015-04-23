package org.uengine.kernel;
import java.io.Serializable;

public interface ITool extends /*ContextAware, */Serializable{
	
	public static final String ITOOL_MAP_KEY 					= "mapForITool";
	public static final String ITOOL_INSTANCEID_KEY 			= "instanceId";
	public static final String ITOOL_TRACINGTAG_KEY 			= "tracingtag";
	public static final String ITOOL_TASKID_KEY 				= "taskId";
	public static final String ITOOL_SESSION_KEY 				= "session";
	public static final String ITOOL_PROCESS_MANAGER_KEY 		= "processManager";
	
	
	public static final String ITOOL_ACTIVITY_EXT1_KEY 			= "activityExt1";
	public static final String ITOOL_ACTIVITY_EXT2_KEY 			= "activityExt2";
	public static final String ITOOL_ACTIVITY_EXT3_KEY 			= "activityExt3";
	public static final String ITOOL_ACTIVITY_EXT4_KEY 			= "activityExt4";
	public static final String ITOOL_ACTIVITY_EXT5_KEY 			= "activityExt5";
	public static final String ITOOL_ACTIVITY_EXT6_KEY 			= "activityExt6";
	public static final String ITOOL_ACTIVITY_EXT7_KEY 			= "activityExt7";
	public static final String ITOOL_ACTIVITY_EXT8_KEY 			= "activityExt8";
	public static final String ITOOL_ACTIVITY_EXT9_KEY 			= "activityExt9";
	public static final String ITOOL_ACTIVITY_EXT10_KEY 		= "activityExt10";
	
	
	public void onLoad() throws Exception;
	
	public void beforeComplete() throws Exception;
	
	public void afterComplete() throws Exception;

}
