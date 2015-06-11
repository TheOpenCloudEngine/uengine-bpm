package org.uengine.kernel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringBufferInputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.ObjectNotFoundException;

import org.uengine.persistence.dao.DAOFactory;
import org.uengine.persistence.dao.UniqueKeyGenerator;
import org.uengine.persistence.processdefinition.ProcessDefinitionRepositoryHomeLocal;
import org.uengine.persistence.processdefinition.ProcessDefinitionRepositoryLocal;
import org.uengine.persistence.processdefinitionversion.ProcessDefinitionVersionRepositoryHomeLocal;
import org.uengine.persistence.processdefinitionversion.ProcessDefinitionVersionRepositoryLocal;
import org.uengine.persistence.processinstance.ProcessInstanceDAO;
import org.uengine.persistence.processinstance.ProcessInstanceDAOType;
import org.uengine.processmanager.ProcessDefinitionRemote;
import org.uengine.processmanager.ProcessTransactionContext;
import org.uengine.util.ActivityForLoop;
import org.uengine.util.InfiniteRecursiveCallSafeBlock;
import org.uengine.util.UEngineUtil;
import org.uengine.util.ZipEntryMapper;

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

		if (USE_CLASS == null) {
			try {
				USE_CLASS = Thread.currentThread().getContextClassLoader().loadClass(GlobalContext
						.getPropertyString("processdefinitionfactory.class","org.uengine.codi.CodiProcessDefinitionFactory"));
			} catch (Exception e) {
				USE_CLASS = ProcessDefinitionFactory.class;
			}
		}

		try {
			return (ProcessDefinitionFactory) USE_CLASS.getConstructor(
					new Class[] { ProcessTransactionContext.class })
					.newInstance(new Object[] { tc });
		} catch (Exception e) {

			return new ProcessDefinitionFactory(tc);
		}
	}

	protected ProcessDefinitionFactory(ProcessTransactionContext tc) {
		if (tc == null)
			throw new RuntimeException(
					"TransactionContext must be provided for initializing ProcessDefinitionFactory.");
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


	public InputStream getResourceStream(String pdvid) throws Exception {
		return (InputStream) getDefinitionSourceImpl(pdvid, false, false);
	}

	public static ProcessDefinition masterProcessDefinition;
	
	public Activity getActivity(String pdvid, boolean caching) throws Exception {
		return getActivity(pdvid, caching, false);
	}

	public Activity getActivity(String pdvid, boolean caching, boolean withoutInheritance) throws Exception {
		// TODO: should be done only once when the server initialized
		if (masterProcessDefinition == null) {
			final String masterProcessAlias = GlobalContext
					.getPropertyString("master.process.definition.alias");
			if (masterProcessAlias != null) {
				new InfiniteRecursiveCallSafeBlock() { // will prevent immense
														// recursive call
					public void logic() throws Exception {
						masterProcessDefinition = tc.getProcessManager()
								.getProcessDefinition(
										"[" + masterProcessAlias + "]");
					}
				}.run();
			}
		}

		if (caching) {
			synchronized (processDefinitions) {
				synchronized (processDefinitionList) {
					if (processDefinitions.containsKey(pdvid)) {
						// move recently used definition to the bottom of cache
						// list
						ProcessDefinition recentUsed = (ProcessDefinition) processDefinitions
								.get(pdvid);

						// check the cached definition is exactly same with the
						// database's one
						// it might happen when you configure multiple
						// application server e.g. Cluster
						/*
						 * ProcessDefinitionVersionRepositoryHomeLocal pdvrh =
						 * GlobalContext
						 * .createProcessDefinitionVersionRepositoryHomeLocal();
						 * ProcessDefinitionVersionRepositoryLocal pdvrl =
						 * pdvrh.findByPrimaryKey(new Long(pdvid)); Date
						 * modifiedDateInDB = pdvrl.getModifiedDate();
						 * 
						 * if(recentUsed.getModifiedDate()==null ||
						 * recentUsed.getModifiedDate
						 * ().getTime().equals(modifiedDateInDB)){
						 */
						processDefinitionList.remove(recentUsed);
						processDefinitionList.add(recentUsed);

						return recentUsed;
						/*
						 * }else{ processDefinitionList.remove(recentUsed);
						 * processDefinitions.remove(pdvid); }
						 */
					}
				}
			}
		}

		ProcessDefinition pd;

		pd = getDefinitionObject(pdvid, true);
		pd.setId(pdvid);

		if (caching) {
			synchronized (processDefinitions) {
				synchronized (processDefinitionList) {
					int currSize = processDefinitionList.size();
					if (currSize >= MAX_PROCESS_DEFINITION_POOL) {
						// remove the least-used definition from the cache list
						ProcessDefinition leastUsed = (ProcessDefinition) processDefinitionList
								.get(0);
						processDefinitionList.remove(0);
						processDefinitions.remove(leastUsed.getId());
					}
					// and put the new definition
					processDefinitions.put(pdvid, pd);
					processDefinitionList.add(pd);
				}
			}
		}

		if (ProcessDefinitionFactory.masterProcessDefinition != null && !withoutInheritance) {

			ScopeActivity scopeActivity = (ScopeActivity) ProcessDefinitionFactory.masterProcessDefinition
					.getChildActivities().get(0);
			ScopeActivity clonedOne = (ScopeActivity) scopeActivity.clone();

			// UEngineUtil.addArrayElement(pd.eventHandlers,
			// scopeActivity.getEventHandlers());
			EventHandler[] ehs = clonedOne.getEventHandlers();
			for (EventHandler eh : ehs) {

				new ActivityForLoop() {

					@Override
					public void logic(Activity activity) {
						// TODO Auto-generated method stub
						activity.setTracingTag(null);
					}
				}.run(eh.getHandlerActivity());

			}

			pd.eventHandlers = clonedOne.getEventHandlers(); // very important
																// not to be
																// "pd.setEventHandler(clonedOne.getEventHandlers());
																// since if
																// wholechildactivities
																// hashtable set
																// by this,
			// the processdefinition.getActivity doesn't try to register child
			// activities properly.
			pd.registerToProcessDefinition(true, false);
		}

		return pd;

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

	protected InputStream getDefinitionSource(String pdvid) throws Exception {
		return (InputStream) getDefinitionSourceImpl(pdvid, false, false);
	}

	protected ProcessDefinition getDefinitionObject(String pdvid,
			boolean fromCompiledFile) throws Exception {
		return (ProcessDefinition) getDefinitionSourceImpl(pdvid,
				fromCompiledFile, true);
	}

	public String getResourcePath(String resourceId) throws Exception {
		ProcessDefinitionVersionRepositoryHomeLocal pdvhr = GlobalContext
				.createProcessDefinitionVersionRepositoryHomeLocal(tc);

		ProcessDefinitionVersionRepositoryLocal pdvr;
		try {
			pdvr = pdvhr.findByPrimaryKey(new Long(resourceId));
		} catch (ObjectNotFoundException onfe) {
			throw new UEngineException("No such Resource Version id ["
					+ resourceId + "]", onfe);
		}

		String def = (String) pdvr.getFilePath();

		if (def.startsWith(LINK_SIGN)) {
			String resourceLocation = def.substring(LINK_SIGN.length());
			return resourceLocation = DEFINITION_ROOT + resourceLocation;
		}

		return null;
	}

	protected Object getDefinitionSourceImpl(String pdvid,
			boolean fromCompilationVersion, boolean shouldBeObjectResult)
			throws Exception {
		ProcessDefinitionVersionRepositoryHomeLocal pdvhr = GlobalContext
				.createProcessDefinitionVersionRepositoryHomeLocal(tc);

		ProcessDefinitionVersionRepositoryLocal pdvr;
		try {
			pdvr = pdvhr.findByPrimaryKey(new Long(pdvid));
		} catch (ObjectNotFoundException onfe) {
			throw new UEngineException(
					"No such Process Definition version id [" + pdvid + "]",
					onfe);
		}

		String def = (String) pdvr.getFilePath();

		if (def.startsWith(LINK_SIGN)) {
			String resourceLocation = def.substring(LINK_SIGN.length());

			// try to load cached(binary) version of definition
			if (fromCompilationVersion) {
				ObjectInputStream ow = null;
				try {
					resourceLocation = DEFINITION_ROOT + resourceLocation;

					String fileName = resourceLocation + ".cached";

					if (processDefinitions.containsKey(pdvid))
						return getActivity(pdvid, true);
					ow = new ObjectInputStream(new FileInputStream(fileName));
					if (processDefinitions.containsKey(pdvid))
						return getActivity(pdvid, true);

					ProcessDefinition objectDefinition = (ProcessDefinition) ow
							.readObject();
					objectDefinition.setBelongingDefinitionId(pdvr.getDefId()
							.toString());

					Calendar rightModifiedDate = Calendar.getInstance();
					{
						Date md = pdvr.getModDate();
						if (md != null)
							rightModifiedDate.setTime(md);
					}
					objectDefinition.setModifiedDate(rightModifiedDate);

					System.out.println("load '" + resourceLocation
							+ "' from cached file..succeed!");
					return objectDefinition;
				} catch (Exception ice) {
					if (ice instanceof java.io.InvalidClassException)
						System.out
								.println("XML-Bean version is loaded for '"
										+ resourceLocation
										+ "'..since the class version has been modified.");

					try {						
						ProcessDefinition objectDefinition = getDefinition(new FileInputStream(
								resourceLocation));
						objectDefinition.setBelongingDefinitionId(pdvr
								.getDefId().toString());
						objectDefinition.setName(pdvr.getDefName());

						// dynamically create the cached(compiled) version
						compileDefinition(resourceLocation, objectDefinition);

						Calendar rightModifiedDate = Calendar.getInstance();
						{
							Date md = pdvr.getModDate();
							if (md != null)
								rightModifiedDate.setTime(md);
						}
						objectDefinition.setModifiedDate(rightModifiedDate);

						return objectDefinition;
					} catch (Exception e) {
						throw new UEngineException(
								"Error when to deserialize definition file '"
										+ resourceLocation + "': "
										+ e.getMessage(), e);
					}
				} finally {
					try {
						ow.close();
					} catch (Exception e) {
					}
				}
			}

			if (shouldBeObjectResult) {
				ProcessDefinition pd = getDefinition(new FileInputStream(
						DEFINITION_ROOT + resourceLocation));
				pd.setBelongingDefinitionId(pdvr.getDefId().toString());
				return pd;
			} else
				return new FileInputStream(DEFINITION_ROOT + resourceLocation);
		}

		StringBufferInputStream is = new java.io.StringBufferInputStream(def);

		if (shouldBeObjectResult) {
			ProcessDefinition pd = getDefinition(is);
			pd.setBelongingDefinitionId(pdvr.getDefId().toString());
			pd.setName(pdvr.getDefName());
			return pd;
		} else
			return is;
	}

	public static ProcessDefinition getDefinition(InputStream is)
			throws Exception {
		return (ProcessDefinition) getActivity(is);
	}

	public static Activity getActivity(InputStream is) throws Exception {
		ProcessDefinition pd = (ProcessDefinition) GlobalContext.deserialize(
				is, String.class);
		return pd;
	}

	public static ProcessDefinition loadDefinitionFromFile(String fName)
			throws Exception {
		return getDefinition(new FileInputStream(fName));
	}

	public void removeDefinition(String pdvid) throws Exception {
		// Object[] nameAndVersion = UEngineUtil.getProcessNameAndVersion(pdid);
		// String name = (String)nameAndVersion[0];
		// int version = ((Integer)nameAndVersion[1]).intValue();

		ProcessDefinitionVersionRepositoryHomeLocal pdvh = GlobalContext
				.createProcessDefinitionVersionRepositoryHomeLocal(tc);
		ProcessDefinitionVersionRepositoryLocal pdvl = pdvh
				.findByPrimaryKey(new Long(pdvid));
		Long pdid = pdvl.getDefId();

		try {
			ProcessDefinitionVersionRepositoryLocal pdir = pdvh
					.findByPrimaryKey(new Long(pdvid));
			// don't remove to prevent danggling instances
			// pdir.remove();
			pdir.setIsDeleted(true);

			Collection versions = pdvh.findAllVersions(pdid);
			// force to remove the definition if there's no more version.
			if (!versions.iterator().hasNext())
				throw new ObjectNotFoundException();
		} catch (javax.ejb.ObjectNotFoundException e) {
			ProcessDefinitionRepositoryHomeLocal pdhr = GlobalContext
					.createProcessDefinitionRepositoryHomeLocal(tc);
			ProcessDefinitionRepositoryLocal pdr = pdhr.findByPrimaryKey(pdid);

			// don't remove
			// pdr.remove();
			pdr.setIsDeleted(true);
		}

		removeFromCache(pdvid);
	}

	public String[] addDefinition(String belongingPdid, String pdvid,
			int version, String name, String description, boolean isAdhoc,
			String definition, String folder) throws Exception {
		return addDefinition(belongingPdid, pdvid, version, name, description,
				isAdhoc, definition, folder, false);
	}

	public String[] addDefinition(String belongingPdid, String pdvid,
			int version, String name, String description, boolean isAdhoc,
			String definition, String folder, boolean overwrite)
			throws Exception {
		return addDefinitionImpl(belongingPdid, pdvid, version, name,
				description, isAdhoc, definition, folder, overwrite, null);
	}

	public String[] addDefinitionImpl(String belongingPdid, String pdvid,
			int version, String name, String description, boolean isAdhoc,
			Object definition, String folder, boolean overwrite, Map options)
			throws Exception {

		ProcessDefinitionRepositoryHomeLocal pdhr = GlobalContext
				.createProcessDefinitionRepositoryHomeLocal(tc);
		boolean verifyModifiedDateWhenOverwriting = true;

		boolean isOtherResourceType = options != null
				&& options.containsKey("objectType");
		String objectType = "upd";
		if (isOtherResourceType)
			objectType = (String) options.get("objectType");

		// if there is no parent folder, this will throw an exception to break
		// this try~catch block
		if (UEngineUtil.isNotEmpty(folder) && !folder.equals("-1")) {
			try {
				pdhr.findByPrimaryKey(new Long(folder));
			} catch (javax.ejb.ObjectNotFoundException e) {
				throw new UEngineException("No such folder");
			}
		} else {
			folder = "-1";
		}

		// if there is no definition for this version, create new one
		boolean definitionExist = false;
		if (belongingPdid != null) {
			try {
				pdhr.findByPrimaryKey(new Long(belongingPdid));
				definitionExist = true;
			} catch (Exception e) {
			}
		}
		if (!definitionExist) {
			belongingPdid = ""
					+ UniqueKeyGenerator.issueProcessDefinitionKey(tc);
			ProcessDefinitionRepositoryLocal pdr = pdhr.create(new Long(
					belongingPdid));
			pdr.setName(name);
			pdr.setParentFolder(new Long(folder));
			if (UEngineUtil.isNotEmpty(description))
				pdr.setDescription(description);

			if (options != null && options.containsKey("objectType")) {
				objectType = (String) options.get("objectType");
				pdr.setObjType(objectType);
			}
			if (options != null && options.containsKey("alias")) {
				String alias = (String) options.get("alias");
				pdr.setAlias(alias);
				ProcessDefinitionRepositoryLocal pdrl = null;
				try {
					pdrl = pdhr.findByAlias(alias);
				} catch (Exception e) {
				}
				if (pdrl != null)
					throw new UEngineException("This alias(" + alias
							+ ") already exists, Choose another alias!");
			}
			if ("upd".equals(objectType)) {
				pdr.setObjType("process");
			}

			if (options != null && options.containsKey("superDefId")) {
				String superDefId = (String) options.get("superDefId");
				pdr.setSuperDefId(superDefId);
			}

			if (isAdhoc)
				pdr.setIsAdhoc(true);
		}

		Calendar modifiedDate = Calendar.getInstance();
		modifiedDate.set(Calendar.MILLISECOND, 0); // ignore millisecond for
													// some database. e.g.
													// Oracle

		// create a version
		if (pdvid == null)
			pdvid = ""
					+ UniqueKeyGenerator.issueProcessDefinitionVersionKey(tc);
		ProcessDefinitionVersionRepositoryHomeLocal pdvhr;
		ProcessDefinitionVersionRepositoryLocal pdvr = null;
		pdvhr = GlobalContext
				.createProcessDefinitionVersionRepositoryHomeLocal(tc);
		if (!overwrite) {
			if (version != -1) {
				Collection existingVersions = pdvhr.findByDefinitionAndVersion(
						new Long(belongingPdid), new Long(version));
				if (existingVersions.iterator().hasNext()) {
					pdvr = (ProcessDefinitionVersionRepositoryLocal) existingVersions
							.iterator().next();
					pdvid = "" + pdvr.getDefVerId();
					// throw new
					// UEngineException("There is same version already exists. Choose another version.");

					// FIXME: implement for non-ejb version.
					if (!"org.uengine.kernel.DefaultProcessInstance"
							.equals(GlobalContext
									.getPropertyString("processinstance.class"))) {
						ProcessInstanceDAOType pidaotype = ProcessInstanceDAOType
								.getInstance(tc);
						ProcessInstanceDAO existingInstances = pidaotype
								.findByDefinitionVersion(pdvr.getDefVerId().toString());
						if (existingInstances.size() > 0) {
							throw new UEngineException(
									"There is some instances belonged to this definition already exists. Choose another version number.");
						}
					}
					overwrite = true;
					verifyModifiedDateWhenOverwriting = false;

				}
			}

			if (pdvr == null) {
				pdvr = pdvhr.create(new Long(pdvid));
				pdvr.setVer(new Long(version));
				pdvr.setDefId(new Long(belongingPdid));
				pdvr.setDefName(name);
			}

		} else {
			pdvr = pdvhr.findByPrimaryKey(new Long(pdvid));
		}

		String fullFileName;
		String relativeFileName;
		if (overwrite) {
			removeFromCache(pdvid);
			relativeFileName = pdvr.getFilePath().substring(LINK_SIGN.length());
			int extPos = relativeFileName.lastIndexOf(".");
			if (extPos == -1) {
				relativeFileName = relativeFileName + "_rev1";
			} else {
				int revPos = relativeFileName.indexOf("_rev");

				int overWriteIndex = 0;
				if (revPos != -1)
					overWriteIndex = Integer.parseInt(relativeFileName
							.substring(revPos + 4, extPos));
				else
					revPos = extPos;

				overWriteIndex++;
				relativeFileName = relativeFileName.substring(0, revPos)
						+ "_rev" + overWriteIndex
						+ relativeFileName.substring(extPos);
			}
		} else if (isAdhoc) {
			String calDir = UEngineUtil.getCalendarDir() + "/";
			String dir = DEFINITION_ROOT + calDir;
			(new File(dir)).mkdirs();

			relativeFileName = calDir + pdvid + "." + objectType;
		} else {
			relativeFileName = pdvid + "." + objectType;
		}

		fullFileName = DEFINITION_ROOT + relativeFileName;

		// store a binary version (to reduce serialization cost)

		boolean isProcessDefinitionType = false;
		if (definition instanceof String) {
			try {
				if (GlobalContext.deserialize(definition.toString(),
						Object.class) instanceof ProcessDefinition) {
					isProcessDefinitionType = true;
				}
			} catch (Exception e) {
				isProcessDefinitionType = false;
			}
		} else if (definition instanceof ProcessDefinition) {
			isProcessDefinitionType = true;
		}

		if (isProcessDefinitionType) {
			ProcessDefinition objectDefinition;
			if (definition instanceof String) {
				ByteArrayInputStream bis = new ByteArrayInputStream(
						((String) definition).getBytes("UTF-8"));
				try {
					objectDefinition = getDefinition(bis);
				} catch (Exception e) {
					throw new UEngineException(
							"Error to parse definition: "
									+ definition
									+ " as Process Definition. Please check if the stream is not form of Process Definition.",
							e);
				}
			} else/* if(defintion instanceof ProcessDefinition) */{
				objectDefinition = (ProcessDefinition) definition;
			}

			// TODO: [performance] Definition in object form is here gotten. So
			// it's no need to load again in the next transaction. Why not just
			// put this in the cache?

			// TODO: [disabled now]
			// if(overwrite &&
			// verifyModifiedDateWhenOverwriting &&
			// objectDefinition.getModifiedDate()!=null &&
			// !objectDefinition.getModifiedDate().getTime().equals(pdvr.getModDate())
			// )
			// throw new
			// UEngineException("This definition is modified by someone during you're editing.");

			objectDefinition.setBelongingDefinitionId(belongingPdid);
			objectDefinition.setId(pdvid);
			objectDefinition.setVersion(version);
			objectDefinition.setModifiedDate(modifiedDate);

			String[] deployFilters = GlobalContext
					.getPropertyStringArray("deployfilters");
			if (deployFilters != null) {
				for (int i = 0; i < deployFilters.length; i++) {
					try {
						DeployFilter deployFilter = (DeployFilter) Class
								.forName(deployFilters[i]).newInstance();
						deployFilter.beforeDeploy(objectDefinition, tc, folder,
								!definitionExist);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			compileDefinition(fullFileName, objectDefinition);

			if (options != null && options.containsKey("associatedInstance")) {
				ProcessInstance instance = (ProcessInstance) options
						.get("associatedInstance");
				instance.setProcessDefinition(objectDefinition);
			}

			if (definition instanceof String) {
				definition = GlobalContext.serialize(objectDefinition,
						String.class);
			}

		}else{
			String[] deployFilters = GlobalContext
					.getPropertyStringArray("deployfilters");
			if (deployFilters != null) {
				for (int i = 0; i < deployFilters.length; i++) {
					try {
						ObjectDeployFilter deployFilter = (ObjectDeployFilter) Class
								.forName(deployFilters[i]).newInstance();
						deployFilter.beforeDeploy(description, belongingPdid, pdvid, tc, folder,
								!definitionExist);
					} catch (ClassCastException cce){
						
					} catch(Exception e){
						e.printStackTrace();
					}
					
					
				}
			}
		}
			
		Timestamp timestamp = new Timestamp(modifiedDate.getTimeInMillis());
		if (!DAOFactory.getInstance(tc).getDBMSProductName().startsWith("DB2"))
			pdvr.setModDate(timestamp);

		// store definition as XML-Bean text file
		// if(version!=-1){ //only when the definition is adhoc instance
		// //changed to alwalys
		String strDef;
		if (definition instanceof String) {
			strDef = (String) definition;
		} else {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			GlobalContext.serialize((ProcessDefinition) definition, bao,
					String.class);
			strDef = bao.toString("UTF-8");
		}

		OutputStreamWriter bw = null;
		try {
			bw = new OutputStreamWriter(new FileOutputStream(fullFileName),
					"UTF-8");
			bw.write(strDef);
			bw.close();
		} catch (Exception e) {
			throw e;
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (Exception e) {
				}
			;
		}
		// }

		// verify consistency with referencing instances /////////////////// -->
		// disabled
		/*
		 * if(overwrite && options!=null){ String instanceId =
		 * (options.containsKey("instanceId") ?
		 * (String)options.get("instanceId") : null); if(instanceId!=null){
		 * ProcessInstance instance =
		 * ProcessInstance.create().getInstance(instanceId); Vector
		 * runningActivityTracingTags =
		 * instance.getRunningOrCompletedActivityTracingTags();
		 * 
		 * for(int i=0; i<runningActivityTracingTags.size(); i++){ String
		 * tracingTagToVerify = (String)runningActivityTracingTags.elementAt(i);
		 * Activity activityNew =
		 * objectDefinition.getActivity(tracingTagToVerify); if(activityNew ==
		 * null){ throw newUEngineException(
		 * "The changed definition is not consistent with running instance. REASON: Though activity with tracingtag '"
		 * + tracingTagToVerify +
		 * "' is already running or completed, it is ommitted in the changed process."
		 * ); } } } }
		 */

		// if(!overwrite){
		pdvr.setFilePath(LINK_SIGN + relativeFileName);
		// }

		return new String[] { pdvid, belongingPdid,
				LINK_SIGN + relativeFileName };
	}

	public void compileDefinition(String fileName,
			ProcessDefinition objectDefinition) throws Exception {
		ObjectOutputStream ow = null;
		try {
			fileName = fileName + ".cached";
			ow = new ObjectOutputStream(new FileOutputStream(fileName));
			ow.writeObject(objectDefinition);
			ow.close();
		} catch (Exception e) {
			throw e;
		} finally {
			if (ow != null)
				try {
					ow.close();
				} catch (Exception e) {
				}
			;
		}
	}

	public ProcessDefinition getDefinitionWithPath(String path)
			throws Exception {
		String resourceLocation = DEFINITION_ROOT + path;
		try {
			ProcessDefinition objectDefinition = getDefinition(new FileInputStream(
					resourceLocation));

			return objectDefinition;
		} catch (Exception e) {
			throw new UEngineException(
					"Error when to deserialize definition file '"
							+ resourceLocation + "': " + e.getMessage(), e);
		}
	}

	public String storeProcessDefinition(String path,
			ProcessDefinition definition) throws Exception {
		String absPath = DEFINITION_ROOT + path;

		try {
			File file = new File(absPath);
			file.getParentFile().mkdirs();

			FileOutputStream fao = new FileOutputStream(file);
			GlobalContext.serialize((ProcessDefinition) definition, fao,
					String.class);
			fao.close();

			return absPath;
		} catch (Exception e) {
			throw new UEngineException(
					"Error when to store some file. Please check the permission or owner of the folders or your file system.");
		}
	}

	public ArrayList getChildDefinitionStreams(String parentDefid,
			String parentDefName, String parentDefAlias,
			ProcessDefinitionRemote[] pds, boolean allVersion) throws Exception {

		ArrayList zipEntryMapperList = new ArrayList();
		zipEntryMapperList.add(new ZipEntryMapper(parentDefName, "",
				parentDefAlias, ZipEntryMapper.TYPE_FOLDER, 0, null));

		getChildStreamsImpl(parentDefid, parentDefName, pds,
				zipEntryMapperList, allVersion);
		return zipEntryMapperList;
	}

	protected void getChildStreamsImpl(String parentDefid,
			String parentDefName, ProcessDefinitionRemote[] pds,
			ArrayList zipEntryMapperList, boolean allVersion) throws Exception {
		for (int i = 0; i < pds.length; i++) {
			if (pds[i].getParentFolder().equals(parentDefid)) {
				String defName = parentDefName + File.separatorChar
						+ pds[i].getName();
				String defAlias = pds[i].getAlias();
				String defType = pds[i].getObjType();

				if (pds[i].isFolder()) {
					zipEntryMapperList.add(new ZipEntryMapper(defName, "",
							defAlias, ZipEntryMapper.TYPE_FOLDER, 0, null));
					getChildStreamsImpl(pds[i].getId(), defName, pds,
							zipEntryMapperList, allVersion);
				} else {
					if (!allVersion && !pds[i].isProduction())
						continue;

					InputStream in = getResourceStream(pds[i].getId());
					zipEntryMapperList.add(new ZipEntryMapper(defName, "",
							defAlias, defType, pds[i].getVersion(), in));
				}
			}
		}
	}

}