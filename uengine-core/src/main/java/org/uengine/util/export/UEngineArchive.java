package org.uengine.util.export;


import java.util.*;


import org.uengine.kernel.NeedArrangementToSerialize;

public class UEngineArchive implements NeedArrangementToSerialize  {
	public static final String SUB_PROC = "_subproc";
	public static final String REF_FORM = "_refform";	
	public static final String UENGINE_ARCHIVE = "_uenginearchive";
	
	DefinitionArchive mainProcessDefinition;
		public DefinitionArchive getMainProcessDefinition() {
			return mainProcessDefinition;
		}
	
		public void setMainProcessDefinition(DefinitionArchive mainProcessDefinition) {
			this.mainProcessDefinition = mainProcessDefinition;
		}
	
	public void setProcessDefinitions(String defVerId, String xmlFileName) {
		for (int i = 0; i < getDefinitionList().size(); i++) {
			DefinitionArchive da = (DefinitionArchive)getDefinitionList().get(i);
			if(da.getVersionId().equals(defVerId)){
				da.setArchiveFileName(xmlFileName);
			}
		}
	}
		
	ArrayList definitionList;	
		public ArrayList getDefinitionList() {
			if(definitionList ==null) definitionList = new ArrayList();
			return definitionList;
		}
	
		public void setDefinitionList(	String name,
										String alias,
										String belongingId,
										String versionId,
										String objectType,
										String archiveFileName,
										String description,
										String parentFolder,
										String id,
										boolean isRoot) {
			
			if(!containKeys(belongingId)){
				DefinitionArchive da =new DefinitionArchive();
				da.setId(id);
				da.setName(name);
				da.setAlias(alias);
				da.setBelongingId(belongingId);
				da.setVersionId(versionId);
				da.setObjectType(objectType);
				da.setArchiveFileName(archiveFileName);
				da.setRoot(isRoot);
				da.setDescription(description);
				da.setParentFolder(parentFolder);
				getDefinitionList().add(da);
				
				if(isRoot)	setMainProcessDefinition(da);
			}
		}

		public void setDefinitionList(	String name,
							String alias,
							String belongingId,
							String versionId,
							String objectType,
							String archiveFileName,
							String description,
							boolean isRoot) {
			DefinitionArchive da =new DefinitionArchive();
			da.setName(name);
			da.setAlias(alias);
			da.setBelongingId(belongingId);
			da.setVersionId(versionId);
			da.setObjectType(objectType);
			da.setArchiveFileName(archiveFileName);
			da.setRoot(isRoot);
			da.setDescription(description);
			getDefinitionList().add(da);
			
			if(isRoot)	setMainProcessDefinition(da);
		}
	
	public boolean containKeys(String belongingId){
		boolean existId = false;
		for (int i=0; i<getDefinitionList().size(); i++) {
			DefinitionArchive daa = (DefinitionArchive)getDefinitionList().get(i);
			if ( daa.getBelongingId().equals( belongingId ) ) {
				existId = true;
				break;
			}
		}
		
		return existId;
	}
		
	public void afterDeserialization() {
		
		
	}

	public void beforeSerialization() {
		
		
	}





}
