package org.uengine.kernel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.processmanager.ProcessTransactionContext;

/**
 * @author Jinyoung Jang
 */

public class ProcessDefinitionFactory {

	public static Class USE_CLASS = null;

	// for LRU mechanism
	static int MAX_PROCESS_DEFINITION_POOL = Integer.parseInt(GlobalContext
			.getPropertyString("server.definition.pooling", "10"));

	static Map processDefinitions = new HashMap(MAX_PROCESS_DEFINITION_POOL);
	static ArrayList processDefinitionList = new ArrayList(
			MAX_PROCESS_DEFINITION_POOL);

	static String LINK_SIGN = "LINK:";
	public static String DEFINITION_ROOT;

	static {
		DEFINITION_ROOT = GlobalContext.getPropertyString(
				"server.definition.path", "." + File.separatorChar + "uengine"
						+ File.separatorChar + "definition"
						+ File.separatorChar);

		if (!DEFINITION_ROOT.endsWith("/") && !DEFINITION_ROOT.endsWith("\\")) {
			DEFINITION_ROOT = DEFINITION_ROOT + "/";
		}
	}

	private ProcessTransactionContext tc;

	public static ProcessDefinitionFactory getInstance(
			ProcessTransactionContext tc) {

		return MetaworksRemoteService.getComponent(ProcessDefinitionFactory.class);
	}

	protected ProcessDefinitionFactory(ProcessTransactionContext tc) {
		if (tc == null)
			throw new RuntimeException(
					"DefaultTransactionContext must be provided for initializing ProcessDefinitionFactory.");
		this.tc = tc;
	}

	public ProcessDefinition getDefinition(String id) throws Exception {
		return (ProcessDefinition) getActivity(id, true);
	}

	public ProcessDefinition getDefinition(String id, boolean caching)
	throws Exception {
		return (ProcessDefinition) getActivity(id, caching);
	}

	public ProcessDefinition getDefinition(String id, boolean caching, boolean withoutInheritance)
		throws Exception {
		return (ProcessDefinition) getActivity(id, caching, withoutInheritance);
	}



	public static ProcessDefinition masterProcessDefinition;
	
	public Activity getActivity(String pdvid, boolean caching) throws Exception {
		return getActivity(pdvid, caching, false);
	}

	public Activity getActivity(String pdvid, boolean caching, boolean withoutInheritance) throws Exception {
//		// TODO: should be done only once when the server initialized
//		if (masterProcessDefinition == null) {
//			final String masterProcessAlias = GlobalContext
//					.getPropertyString("master.process.definition.alias");
//			if (masterProcessAlias != null) {
//				new InfiniteRecursiveCallSafeBlock() { // will prevent immense
//														// recursive call
//					public void logic() throws Exception {
//						masterProcessDefinition = tc.getProcessManager()
//								.getProcessDefinition(
//										"[" + masterProcessAlias + "]");
//					}
//				}.run();
//			}
//		}
//
//		if (caching) {
//			synchronized (processDefinitions) {
//				synchronized (processDefinitionList) {
//					if (processDefinitions.containsKey(pdvid)) {
//						// move recently used definition to the bottom of cache
//						// list
//						ProcessDefinition recentUsed = (ProcessDefinition) processDefinitions
//								.get(pdvid);
//
//						// check the cached definition is exactly same with the
//						// database's one
//						// it might happen when you configure multiple
//						// application server e.g. Cluster
//						/*
//						 * ProcessDefinitionVersionRepositoryHomeLocal pdvrh =
//						 * GlobalContext
//						 * .createProcessDefinitionVersionRepositoryHomeLocal();
//						 * ProcessDefinitionVersionRepositoryLocal pdvrl =
//						 * pdvrh.findByPrimaryKey(new Long(pdvid)); Date
//						 * modifiedDateInDB = pdvrl.getModifiedDate();
//						 *
//						 * if(recentUsed.getModifiedDate()==null ||
//						 * recentUsed.getModifiedDate
//						 * ().getTime().equals(modifiedDateInDB)){
//						 */
//						processDefinitionList.remove(recentUsed);
//						processDefinitionList.add(recentUsed);
//
//						return recentUsed;
//						/*
//						 * }else{ processDefinitionList.remove(recentUsed);
//						 * processDefinitions.remove(pdvid); }
//						 */
//					}
//				}
//			}
//		}
//
//		ProcessDefinition pd;
//
//		pd = getDefinitionObject(pdvid, true);
//		pd.setId(pdvid);
//
//		if (caching) {
//			synchronized (processDefinitions) {
//				synchronized (processDefinitionList) {
//					int currSize = processDefinitionList.size();
//					if (currSize >= MAX_PROCESS_DEFINITION_POOL) {
//						// remove the least-used definition from the cache list
//						ProcessDefinition leastUsed = (ProcessDefinition) processDefinitionList
//								.get(0);
//						processDefinitionList.remove(0);
//						processDefinitions.remove(leastUsed.getId());
//					}
//					// and put the new definition
//					processDefinitions.put(pdvid, pd);
//					processDefinitionList.add(pd);
//				}
//			}
//		}
//
//		if (ProcessDefinitionFactory.masterProcessDefinition != null && !withoutInheritance) {
//
//			ScopeActivity scopeActivity = (ScopeActivity) ProcessDefinitionFactory.masterProcessDefinition
//					.getChildActivities().get(0);
//			ScopeActivity clonedOne = (ScopeActivity) scopeActivity.clone();
//
//			// UEngineUtil.addArrayElement(pd.eventHandlers,
//			// scopeActivity.getEventHandlers());
//			EventHandler[] ehs = clonedOne.getEventHandlers();
//			for (EventHandler eh : ehs) {
//
//				new ActivityForLoop() {
//
//					@Override
//					public void logic(Activity activity) {
//						// TODO Auto-generated method stub
//						activity.setTracingTag(null);
//					}
//				}.run(eh.getHandlerActivity());
//
//			}
//
//			pd.eventHandlers = clonedOne.getEventHandlers(); // very important
//																// not to be
//																// "pd.setEventHandler(clonedOne.getEventHandlers());
//																// since if
//																// wholechildactivities
//																// hashtable set
//																// by this,
//			// the processdefinition.getActivity doesn't try to register child
//			// activities properly.
//			pd.registerToProcessDefinition(true, false);
//		}

		return null;

	}

	// it may not work properly in clustered servers.
	public void removeFromCache(String definitionId) {
		if (processDefinitions.containsKey(definitionId)) {
			ProcessDefinition def = (ProcessDefinition) processDefinitions
					.get(definitionId);
			processDefinitionList.remove(def);
			processDefinitions.remove(definitionId);
		}
	}
//
//	protected InputStream getDefinitionSource(String pdvid) throws Exception {
//		return (InputStream) getDefinitionSourceImpl(pdvid, false, false);
//	}
//
//	protected ProcessDefinition getDefinitionObject(String pdvid,
//			boolean fromCompiledFile) throws Exception {
//		return (ProcessDefinition) getDefinitionSourceImpl(pdvid,
//				fromCompiledFile, true);
//	}


//	public static ProcessDefinition getDefinition(InputStream is)
//			throws Exception {
//		return (ProcessDefinition) getActivity(is);
//	}
//
//	public static Activity getActivity(InputStream is) throws Exception {
//		ProcessDefinition pd = (ProcessDefinition) GlobalContext.deserialize(
//				is, String.class);
//		return pd;
//	}
//
//	public static ProcessDefinition loadDefinitionFromFile(String fName)
//			throws Exception {
//		return getDefinition(new FileInputStream(fName));
//	}

	public void removeDefinition(String pdvid) throws Exception {

		removeFromCache(pdvid);
	}



}