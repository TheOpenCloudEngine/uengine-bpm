package org.uengine.persistence.processdefinition;

import java.util.Date;

import org.uengine.persistence.processdefinitionversion.ProcessDefinitionVersionDAO;
import org.uengine.processmanager.ProcessTransactionContext;
import org.uengine.processmanager.TransactionContext;
import org.uengine.util.dao.ConnectiveDAO;

public class ProcessDefinitionDAOType {

	private TransactionContext tc = null;
	
	private ProcessDefinitionDAOType(TransactionContext ptc) {
		this.tc = ptc;
	}
	
	public static ProcessDefinitionDAOType getInstance(TransactionContext ptc) {
		return new ProcessDefinitionDAOType(ptc);
	}	
		
	private ProcessDefinitionDAO createDAOImpl(String sql) throws Exception {
		ProcessDefinitionDAO pd = (ProcessDefinitionDAO)ConnectiveDAO.createDAOImpl(tc, sql, ProcessDefinitionDAO.class);
		return pd;
	}	
		
	private ProcessDefinitionVersionDAO createVersionDAOImpl(String sql) throws Exception {
		ProcessDefinitionVersionDAO pdv = (ProcessDefinitionVersionDAO)ConnectiveDAO.createDAOImpl(tc, sql, ProcessDefinitionVersionDAO.class);
		return pdv;
	}
	
	private ProductionDefinitionDAO createProductionDefDAOImpl(String sql) throws Exception {
		ProductionDefinitionDAO productionDefDAO = (ProductionDefinitionDAO)ConnectiveDAO.createDAOImpl(tc, sql, ProductionDefinitionDAO.class);
		return productionDefDAO;
	}	
	


	public static final String INSERT_PROCESSDEFINITION_SQL =
		"INSERT INTO BPM_PROCDEF " +
		"(DefinitionId, folderId, DefinitionName, description, CreatorUserId, CreatorUserName, defcategoryid, deftypeid, modifieddate) values " +
		"(?DefinitionId, ?folderId, ?DefinitionName, ?description, ?CreatorUserId, ?CreatorUserName, ?defcategoryid, ?deftypeid, sysdate)";
	
	public static final String INSERT_PROCESSDEFVER_SQL =
		"INSERT INTO BPM_PROCDEFVER " +
		"(DefinitionVersionId, DefinitionId, DefinitionPath, modifieddate, version, CreatorUserId, CreatorUserName) values " +
		"(?DefinitionVersionId, ?DefinitionId, ?DefinitionPath, sysdate, ?version, ?CreatorUserId, ?CreatorUserName)";

	public static final String GET_PROCESSDEFLIST_SQL =
		"SELECT DefinitionId, DefinitionName, Description, ProductionVersion, DisplayOption, " +
		"CreatorUserId, CreatorUserName, ModifierUserId, ModifierUserName, FolderId, Alias " +
		"FROM BPM_PROCDEF " +
		"WHERE  FolderId = ?FolderId " + 
		"AND IsDeleted = 0";
	
	public static final String GET_MAXPROCESSDEFVERSION_SQL = 
		"SELECT MAX(VERSION) AS MAXVER FROM BPM_PROCDEFVER WHERE DefinitionId = ?DefinitionId AND IsDeleted = 0";	
	
	public static final String PRODUCTION_DEF_COMMON_SQL = 
		"SELECT defMain.DefinitionId, defMain.DefinitionName, defMain.Description, " +
		"defMain.ProductionVersion, defMain.DisplayOption, " +
		"defMain.CreatorUserId, defMain.CreatorUserName, defMain.ModifierUserId, defMain.ModifierUserName, " +
		"defMain.folderId, defMain.DefCategoryId, defMain.DefTypeId, defMain.Alias, " +
	    "defVer.DefinitionVersionId, defVer.DefinitionPath, defVer.ModifiedDate, defVer.Version " +
		"FROM BPM_PROCDEF defMain, BPM_PROCDEFVER defVer ";	
	
	public static final String GET_PRODUCTION_DEF_SQL =
		PRODUCTION_DEF_COMMON_SQL + 
		"WHERE defMain.DefinitionId = ?DefinitionId " +
		"AND defMain.DefinitionId = defVer.DefinitionId " +
		"AND defMain.ProductionVersion = defVer.Version " +
		"AND defMain.IsDeleted = 0 " +
		"AND defVer.IsDeleted = 0";	
	
	public static final String GET_PRODUCTION_DEF_AT_THAT_TIME_SQL =
		"SELECT * FROM (" +
		PRODUCTION_DEF_COMMON_SQL + 
		"WHERE defMain.DefinitionId = ?DefinitionId " +
		"AND defMain.DefinitionId = defVer.DefinitionId " +
		"AND defMain.IsDeleted = 0 " +
		"AND defVer.IsDeleted = 0" +
		" AND defVer.prodPeriodStart < ?prodPeriodStart " +
		" ORDER BY defVer.prodPeriodStart DESC"	+
		") WHERE rowNum = 1";
	
	public static final String GET_FIRST_PRODUCTION_DEF_SQL =
		PRODUCTION_DEF_COMMON_SQL + 
		"WHERE defMain.DefinitionId = ?DefinitionId " +
		"AND defMain.DefinitionId = defVer.DefinitionId " +
		"AND defMain.IsDeleted = 0 " +
		"AND defVer.IsDeleted = 0" +
		" AND defVer.prodPeriodStart is not null " +
		" AND rowNum = 1";
	
	
	public static final String GET_JOINED_DEF_SQL =
		PRODUCTION_DEF_COMMON_SQL + 
		"WHERE defVer.DefinitionVersionId = ?DefinitionVersionId " +
		"AND defMain.DefinitionId = defVer.DefinitionId " +
		"AND defVer.IsDeleted = 0";	
	
	public static final String GET_INITIAL_DEF_SQL =
		PRODUCTION_DEF_COMMON_SQL + 
		"WHERE defMain.DefinitionId = ?DefinitionId " +
		"AND defMain.DefinitionId = defVer.DefinitionId " +
		"AND defMain.IsDeleted = 0 " +
		"AND defVer.IsDeleted = 0";

	public static final String GET_PRODUCTION_DEF_BY_DEFVERSIONID_SQL =
		PRODUCTION_DEF_COMMON_SQL + 
		"WHERE defVer.DefinitionVersionId = ?DefinitionVersionId " +
		"AND defMain.DefinitionId = defVer.DefinitionId";
	
	public static String GET_DEFVERIONLIST_SQL = 
		"SELECT * " +
		"FROM BPM_PROCDEFVER " +
		"WHERE DefId = ?DefId " +
		"AND IsDeleted = 0 " +
		"ORDER BY Ver DESC";
	
	public static final String findVersionByPrimaryKey_SQL =
		"SELECT DefinitionVersionId, DefinitionPath, Version, DefinitionId, ModifiedDate, " +
		"CreatorUserId, CreatorUserName, ModifierUserId, ModifierUserName " +
		"FROM BPM_PROCDEFVER " +
		"WHERE DefinitionVersionId = ?DefinitionVersionId " +
		"AND IsDeleted = 0";
	
	/** Production ��d 1*/
	public static final String SETASPRODUCTION_SQL = 
		"UPDATE BPM_PROCDEF " + 
		"SET  modifieddate=sysdate, ProductionVersion = ?ProductionVersion " +
		",modifieruserid = ?modifieruserid " +
		",modifierusername = ?modifierusername " +
		"WHERE DefinitionId = ?DefinitionId";

	/** Production ��d 2*/
	public static final String SETASPRODUCTION_TO_VER_SQL = 
		"UPDATE BPM_PROCDEFVER " + 
		"SET prodPeriodStart = sysdate " +
		"WHERE Version = ?Version " +
		"AND DefinitionId = ?DefinitionId ";

	/** �wμ��� d�� �̵� */
	public static final String MOVE_PROCDEF_SQL = 
		"UPDATE BPM_PROCDEF " + 
		"SET modifieddate=sysdate,  folderid = ?folderid " +
		",modifieruserid = ?modifieruserid " +
		",modifierusername = ?modifierusername " +
		"WHERE definitionId = ?definitionId";
	
	/** �wμ��� d�� ��f. */
	public static final String DELETE_PROCDEF_SQL = 
		"UPDATE BPM_PROCDEF " +
		"SET modifieddate=sysdate,  IsDeleted = 1 " +
		",modifieruserid = ?modifieruserid " +
		",modifierusername = ?modifierusername " +
		"WHERE definitionId = ?definitionId";
	
	/** �wμ��� d�� ���� ��f(By ProcDefVersionId). */
	public static final String DELETE_PROCDEFVERSION_SQL = 
		"UPDATE BPM_PROCDEFVER " +
		"SET modifieddate=sysdate,  IsDeleted = 1 " +
		",modifieruserid = ?modifieruserid " +
		",modifierusername = ?modifierusername " +
		"WHERE DefinitionVersionId = ?DefinitionVersionId";
	
	/** �wμ��� d�� ���� ��f.(By ProcDefId) */
	public static final String DELETE_PROCDEFVERSION_BY_PROCDEFID_SQL = 
		"UPDATE BPM_PROCDEFVER " +
		"SET  modifieddate=sysdate, IsDeleted = 1 " +
		",modifieruserid = ?modifieruserid " +
		",modifierusername = ?modifierusername " +
		"WHERE DefinitionId = ?DefinitionId";
	
	/** �wμ��� d�� �̸� ��d */
	public static final String UPDATE_PROCDEF_NAME_SQL = 
		"UPDATE BPM_PROCDEF " + 
		"SET modifieddate=sysdate, definitionName = ?definitionName " +
		",modifieruserid = ?modifieruserid " +
		",modifierusername = ?modifierusername " +
		"WHERE definitionId = ?definitionId";

	/** Category ��d */
	public static final String UPDATE_PROCDEF_CATEGORY_SQL = 
		"UPDATE BPM_PROCDEF " + 
		"SET modifieddate=sysdate,  defcategoryid = ?defcategoryid " +
		",deftypeid        = ?deftypeid " +
		",modifieruserid = ?modifieruserid " +
		",modifierusername = ?modifierusername " +
		"WHERE definitionId = ?definitionId";

	/** �wμ��� d�� �з� ��ȯ */
	public static final String GET_DEF_CATEGORY_LIST_SQL =
		"SELECT DefCategoryId, DefCategoryName, IsDeleted, ModifiedDate, ISDEFAULT " +
		"FROM BPM_PROCDEF_CATEGORY " +
		"WHERE IsDeleted = 0";

	/** �wμ��� d�� �з� ��ȯ */
	public static final String GET_DEF_CATEGORY_LIST_NEW_SQL =

		"select  to_char(defdispfldid) defdispfldid, defcategoryid, foldername " +
		"from( " +
		"select level lv, defdispfldid, defcategoryid, foldername " +
		"from bpm_defdispfld " +
		"where owncompany = ?owncompany "  +
		"and ISDELETED=0 "  +
		"connect by prior defdispfldid = parentfolderid  " +
		"start with parentfolderid = -1 "  +
		")folderview " +
		"where folderview.lv=3 ";

	
	
	/** �wμ��� d�� ~�� ��ȯ */
	public static final String GET_DEF_TYPE_LIST_SQL =
		"SELECT DefTypeId, DefTypeName, DefCategoryId, IsDeleted, ModifiedDate " +
		"FROM BPM_PROCDEF_TYPE " +
		"WHERE DefCategoryId = ?DefCategoryId " +
		"AND IsDeleted = 0";
	/** �wμ��� d�� ~�� ��ȯ */
	public static final String GET_DEF_TYPE_LIST_NEW_SQL =
		"select distinct procdef.definitionid, procdef.definitionname, procdef.description  " +
		"from bpm_procdef procdef, bpm_defdispmap defdispmap  " +
		"where procdef.definitionid = defdispmap.definitionid    " +
		"and defdispmap.defdispfldid in  " +
		"(  " +
		"select defdispfldid  " +
		"from bpm_defdispfld " +
		"where owncompany = ?owncompany " +
		"and ISDELETED=0  " +
		"connect by prior defdispfldid = parentfolderid  " +
		"start with defdispfldid = ?defdispfldid " +
		")  " +  
		"and procdef.isdeleted = 0  order by definitionname asc ";
	
	
	
	/** Category vȸ */
	public static final String GET_DEF_CATEGORY_VIEW_SQL =
		" SELECT a.definitionid definitionid, a.defcategoryid defcategoryid, a.deftypeid deftypeid, " +
    	"        b.defcategoryname defcategoryname, c.deftypename deftypename              " +
		"   FROM BPM_PROCDEF_TYPE c,                           " +
		"        BPM_PROCDEF_CATEGORY b,                       " +
		"        BPM_PROCDEF a                                 " +
		"  WHERE a.definitionid  = ?definitionid               " +  
		"    AND b.defcategoryid (+)= a.defcategoryid          " +
		"    AND c.deftypeid     (+)= a.deftypeid              ";

	/** �wμ��� d��ID ��ȯ */
	public static final String GET_DEF_DEFID_SQL =
		"SELECT Definitionid " +
		"FROM BPM_PROCDEF " +
		"WHERE Alias = ?Alias " +
		"AND IsDeleted = 0";


	public ProductionDefinitionDAO getProductionDefinition(long definitionId) throws Exception {
		ProductionDefinitionDAO productionDefDAO = createProductionDefDAOImpl("GET_PRODUCTION_DEF_SQL");
		productionDefDAO.setDefId(new Long(definitionId));
		productionDefDAO.select();
		if ( productionDefDAO.size() > 0 ) productionDefDAO.next();
		return productionDefDAO;
	}	
	
	public ProductionDefinitionDAO getProductionDefinitionAtThatTime(long definitionId, Date thatTime) throws Exception {
		ProductionDefinitionDAO productionDefDAO = createProductionDefDAOImpl(GET_PRODUCTION_DEF_AT_THAT_TIME_SQL);
		productionDefDAO.setDefId(new Long(definitionId));
		productionDefDAO.setProdStartDate(thatTime);
		productionDefDAO.select();
		if ( productionDefDAO.size() > 0 ) productionDefDAO.next();
		return productionDefDAO;
	}	
	
	public ProductionDefinitionDAO getFirstProductionDefinition(long definitionId) throws Exception {
		ProductionDefinitionDAO productionDefDAO = createProductionDefDAOImpl(GET_FIRST_PRODUCTION_DEF_SQL);
		productionDefDAO.setDefId(new Long(definitionId));
		productionDefDAO.select();
		if ( productionDefDAO.size() > 0 ) productionDefDAO.next();
		return productionDefDAO;
	}	
	
	
	public ProductionDefinitionDAO getInitialDefinition(long defId) throws Exception {
		ProductionDefinitionDAO productionDefDAO = createProductionDefDAOImpl("GET_INITIAL_DEF_SQL");
		productionDefDAO.setDefId(new Long(defId));
		productionDefDAO.select();
		if ( productionDefDAO.size() > 0 ) productionDefDAO.next();
		return productionDefDAO;
	}	

	public ProductionDefinitionDAO getProductionDefinitionByDefVersionId(long defVerionId) throws Exception {
		ProductionDefinitionDAO productionDefDAO = createProductionDefDAOImpl("GET_PRODUCTION_DEF_BY_DEFVERSIONID_SQL");
		productionDefDAO.setDefVerId(new Long(defVerionId));
		productionDefDAO.select();
		if ( productionDefDAO.size() > 0 ) productionDefDAO.next();
		return productionDefDAO;
	}	
	
	public ProcessDefinitionVersionDAO getAllVersions(long defId) throws Exception {
		ProcessDefinitionVersionDAO versionDAO = createVersionDAOImpl(GET_DEFVERIONLIST_SQL);
		versionDAO.setDefId(new Long(defId));
		versionDAO.select();

		return versionDAO;
	}	

}
