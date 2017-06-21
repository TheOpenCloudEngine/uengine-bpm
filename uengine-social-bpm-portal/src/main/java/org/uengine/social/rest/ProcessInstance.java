package org.uengine.social.rest;

import org.uengine.persistence.processinstance.ProcessInstanceDAO;
import org.uengine.util.dao.AbstractGenericDAO;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.RemoveException;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by uengine on 2017. 6. 19..
 */
@Entity
@Table(name = "BPM_PROCINST")
public class ProcessInstance implements ProcessInstanceDAO {

    @Id
    Long instId;

    String defVerId;

    String defId;

    String defPath;

    String defName;

    Date startedDate;

    Date finishedDate;

    Date dueDate;

    Date defModDate;

    Date modDate;

    String status;

    String info;

    String name;

    boolean deleted;

    boolean adhoc;

    boolean subProcess;

    boolean eventHandler;

    Long rootInstId;

    Long mainInstId;

    String mainActTrcTag;

    String mainExecScope;

    Long mainDefVerId;

    boolean archive;

    String absTrcPath;

    boolean dontReturn;

    String ext1;

    String ext2;

    String ext3;

    String ext4;

    String ext5;

    String initComCd;


    @Override
    public Long getInstId() {
        return instId;
    }

    @Override
    public void setInstId(Long instId) {
        this.instId = instId;
    }

    @Override
    public String getDefVerId() {
        return defVerId;
    }

    @Override
    public void setDefVerId(String defVerId) {
        this.defVerId = defVerId;
    }

    @Override
    public String getDefId() {
        return defId;
    }

    @Override
    public void setDefId(String defId) {
        this.defId = defId;
    }

    @Override
    public String getDefPath() {
        return defPath;
    }

    @Override
    public void setDefPath(String defPath) {
        this.defPath = defPath;
    }

    @Override
    public String getDefName() {
        return defName;
    }

    @Override
    public void setDefName(String defName) {
        this.defName = defName;
    }

    @Override
    public Date getStartedDate() {
        return startedDate;
    }

    @Override
    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    @Override
    public Date getFinishedDate() {
        return finishedDate;
    }

    @Override
    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    @Override
    public Date getDueDate() {
        return dueDate;
    }

    @Override
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public Date getDefModDate() {
        return defModDate;
    }

    @Override
    public void setDefModDate(Date defModDate) {
        this.defModDate = defModDate;
    }

    @Override
    public Date getModDate() {
        return modDate;
    }

    @Override
    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean getIsDeleted() {
        return false;
    }

    @Override
    public void setIsDeleted(boolean isDeleted) {

    }

    @Override
    public boolean getIsAdhoc() {
        return false;
    }

    @Override
    public void setIsAdhoc(boolean isAdhoc) {

    }

    @Override
    public boolean getIsSubProcess() {
        return false;
    }

    @Override
    public void setIsSubProcess(boolean isSubProcess) {

    }

    @Override
    public boolean getIsEventHandler() {
        return false;
    }

    @Override
    public void setIsEventHandler(boolean isEventHandler) {

    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isAdhoc() {
        return adhoc;
    }

    public void setAdhoc(boolean adhoc) {
        this.adhoc = adhoc;
    }

    public boolean isSubProcess() {
        return subProcess;
    }

    public void setSubProcess(boolean subProcess) {
        this.subProcess = subProcess;
    }

    public boolean isEventHandler() {
        return eventHandler;
    }

    public void setEventHandler(boolean eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public Long getRootInstId() {
        return rootInstId;
    }

    @Override
    public void setRootInstId(Long rootInstId) {
        this.rootInstId = rootInstId;
    }

    @Override
    public Long getMainInstId() {
        return mainInstId;
    }

    @Override
    public void setMainInstId(Long mainInstId) {
        this.mainInstId = mainInstId;
    }

    @Override
    public String getMainActTrcTag() {
        return mainActTrcTag;
    }

    @Override
    public void setMainActTrcTag(String mainActTrcTag) {
        this.mainActTrcTag = mainActTrcTag;
    }

    @Override
    public String getMainExecScope() {
        return mainExecScope;
    }

    @Override
    public void setMainExecScope(String mainExecScope) {
        this.mainExecScope = mainExecScope;
    }

    @Override
    public Long getMainDefVerId() {
        return mainDefVerId;
    }

    @Override
    public void setMainDefVerId(Long mainDefVerId) {
        this.mainDefVerId = mainDefVerId;
    }

    @Override
    public boolean getIsArchive() {
        return false;
    }

    @Override
    public void setIsArchive(boolean isArchive) {

    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    @Override
    public String getAbsTrcPath() {
        return absTrcPath;
    }

    @Override
    public void setAbsTrcPath(String absTrcPath) {
        this.absTrcPath = absTrcPath;
    }

    @Override
    public boolean getDontReturn() {
        return false;
    }

    public boolean isDontReturn() {
        return dontReturn;
    }

    @Override
    public void setDontReturn(boolean dontReturn) {
        this.dontReturn = dontReturn;
    }

    @Override
    public String getExt1() {
        return ext1;
    }

    @Override
    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    @Override
    public String getExt2() {
        return ext2;
    }

    @Override
    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    @Override
    public String getExt3() {
        return ext3;
    }

    @Override
    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    @Override
    public String getExt4() {
        return ext4;
    }

    @Override
    public void setExt4(String ext4) {
        this.ext4 = ext4;
    }

    @Override
    public String getExt5() {
        return ext5;
    }

    @Override
    public void setExt5(String ext5) {
        this.ext5 = ext5;
    }

    @Override
    public String getExt6() {
        return null;
    }

    @Override
    public void setExt6(String ext6) {

    }

    @Override
    public String getExt7() {
        return null;
    }

    @Override
    public void setExt7(String ext7) {

    }

    @Override
    public String getExt8() {
        return null;
    }

    @Override
    public void setExt8(String ext8) {

    }

    @Override
    public String getExt9() {
        return null;
    }

    @Override
    public void setExt9(String ext9) {

    }

    @Override
    public String getExt10() {
        return null;
    }

    @Override
    public void setExt10(String ext10) {

    }

    @Override
    public Number getStrategyId() {
        return null;
    }

    @Override
    public void setStrategyId(Number strategyId) {

    }


    @Override
    public String getInitComCd() {
        return initComCd;
    }

    @Override
    public void setInitComCd(String initComCd) {
        this.initComCd = initComCd;
    }

    @Override
    public EJBLocalHome getEJBLocalHome() throws EJBException {
        return null;
    }

    @Override
    public Object getPrimaryKey() throws EJBException {
        return null;
    }

    @Override
    public void remove() throws RemoveException, EJBException {

    }

    @Override
    public boolean isIdentical(EJBLocalObject ejbLocalObject) throws EJBException {
        return false;
    }

    @Override
    public void select() throws Exception {

    }

    @Override
    public int insert() throws Exception {
        return 0;
    }

    @Override
    public int update() throws Exception {
        return 0;
    }

    @Override
    public int update(String stmt) throws Exception {
        return 0;
    }

    @Override
    public int call() throws Exception {
        return 0;
    }

    @Override
    public void addBatch() throws Exception {

    }

    @Override
    public int[] updateBatch() throws Exception {
        return new int[0];
    }

    @Override
    public void beforeFirst() throws Exception {

    }

    @Override
    public boolean previous() throws Exception {
        return false;
    }

    @Override
    public boolean next() throws Exception {
        return false;
    }

    @Override
    public boolean first() throws Exception {
        return false;
    }

    @Override
    public void afterLast() throws Exception {

    }

    @Override
    public boolean last() throws Exception {
        return false;
    }

    @Override
    public boolean absolute(int pos) throws Exception {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Object get(String key) throws Exception {
        return null;
    }

    @Override
    public Object set(String key, Object value) throws Exception {
        return null;
    }

    @Override
    public String getString(String key) throws Exception {
        return null;
    }

    @Override
    public Integer getInt(String key) throws Exception {
        return null;
    }

    @Override
    public Long getLong(String key) throws Exception {
        return null;
    }

    @Override
    public Boolean getBoolean(String key) throws Exception {
        return null;
    }

    @Override
    public Date getDate(String key) throws Exception {
        return null;
    }

    @Override
    public AbstractGenericDAO getImplementationObject() {
        return null;
    }

    @Override
    public void releaseResource() throws Exception {

    }
}
