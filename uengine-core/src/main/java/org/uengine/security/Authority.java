package org.uengine.security;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.uengine.util.UEngineUtil;

public class Authority {

	private Authority() {
		// TODO Auto-generated constructor stub
	}
	
	private Authority(String companyCode){
		this.setComcode(companyCode);
	}
	
	/**
	 * <pre>
	 * 	Create company code base Authority Single Object
	 * </pre>
	 * @param companyCode
	 * @return {@link Authority} 
	 */
	public static Authority createAuthrity(String companyCode){
		return new Authority(companyCode);
	}
	
	public Authority cloneAuthority(){
		Authority authority = new Authority();
		authority.setComcode(getComcode());
		authority.setAclid(getAclid());
		authority.setComname(getComname());
		authority.setDefaultuser(getDefaultuser());
		authority.setEmpcode(getEmpcode());
		authority.setEmpname(getEmpname());
		authority.setPartcode(getPartcode());
		authority.setPartname(getPartname());
		authority.setRolecode(getRolecode());
		authority.setRolename(getRolename());
		authority.setPermission(getPermission());
		return authority;
	}
	
	/**
	 * <pre>
	 * 	Create company code base Authority List
	 * </pre>
	 * @param companyCode
	 * @param aclField
	 * @param userCodes
	 * @param permission
	 * @return {@link List}<{@link Authority}>
	 */
	public static List<Authority> createAuthorityList(String companyCode, String aclField , String userCodes, String[] permission){
		List<Authority> authorities = null;
		if(UEngineUtil.isNotEmpty(userCodes)){
			authorities = new ArrayList<Authority>();
			aclField = aclField.toUpperCase();
			String[] userCodeList = userCodes.split(";");
			
			
			for(int i=0, n=userCodeList.length; i<n; i++){
				Authority authority = new Authority(companyCode);
				if(AclManager.ACL_FIELD_PART.equals(aclField)){
					authority.setPartcode(userCodeList[i]);
				}else if(AclManager.ACL_FIELD_EMP.equals(aclField)){
					authority.setEmpcode(userCodeList[i]);
				}else if(AclManager.ACL_FIELD_ROLE.equals(aclField)){
					authority.setRolecode(userCodeList[i]);
				}else if(AclManager.ACL_FIELD_DEFAULT.equals(aclField)){
					authority.setDefaultuser(userCodeList[i]);
				}
				for(int j=0, m=permission.length; j<m; j++){
					Authority authorityPermission = authority.cloneAuthority();
					authorityPermission.setPermission(permission[j]);
					authorities.add(authorityPermission);
				}
			}
		}
		return authorities;
	}
	
	private String aclid;
	private String comcode;
	private String comname;
	private String partcode;
	private String partname;
	private String empcode;
	private String empname;
	private String rolecode;
	private String rolename;
	private String defaultuser;
	private String permission;

	public String getAclid() {
		return aclid;
	}

	public void setAclid(String aclid) {
		this.aclid = aclid;
	}

	public String getComcode() {
		return comcode;
	}

	private void setComcode(String comcode) {
		this.comcode = comcode;
	}

	public String getComname() {
		return comname;
	}

	public void setComname(String comname) {
		this.comname = comname;
	}

	public String getPartcode() {
		return partcode;
	}

	public void setPartcode(String partcode) {
		this.partcode = partcode;
	}

	public String getPartname() {
		return partname;
	}

	public void setPartname(String partname) {
		this.partname = partname;
	}

	public String getEmpcode() {
		return empcode;
	}

	public void setEmpcode(String empcode) {
		this.empcode = empcode;
	}

	public String getEmpname() {
		return empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

	public String getRolecode() {
		return rolecode;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getDefaultuser() {
		return defaultuser;
	}

	public void setDefaultuser(String defaultuser) {
		this.defaultuser = defaultuser;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	private void encode() {
		if (UEngineUtil.isNotEmpty(empname)) {
			try {
				empname = URLEncoder.encode(empname, "UTF-8");
			} catch (UnsupportedEncodingException e) { }
		}
		if (UEngineUtil.isNotEmpty(partname)) {
			try {
				partname = URLEncoder.encode(partname, "UTF-8");
			} catch (UnsupportedEncodingException e) { }
		}
		if (UEngineUtil.isNotEmpty(comname)) {
			try {
				comname = URLEncoder.encode(comname, "UTF-8");
			} catch (UnsupportedEncodingException e) { }
		}
		if (UEngineUtil.isNotEmpty(rolename)) {
			try {
				rolename = URLEncoder.encode(rolename, "UTF-8");
			} catch (UnsupportedEncodingException e) { }
		}
	}
	
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		encode();
		
		sb.append("{");
		sb.append("type : 'authority', ");
		sb.append("aclid : '" + aclid + "', ");
		sb.append("comcode : '" + comcode + "', ");
		sb.append("comname : '" + comname + "', ");
		sb.append("partcode : '" + partcode + "', ");
		sb.append("partname : '" + partname + "', ");
		sb.append("empcode : '" + empcode + "', ");
		sb.append("empname : '" + empname + "', ");
		sb.append("rolecode : '" + rolecode + "', ");
		sb.append("rolename : '" + rolename + "', ");
		sb.append("defaultuser : '" + defaultuser + "', ");
		sb.append("permission : '" + permission + "', ");
		sb.append("}");
		
		return sb.toString();
	}

	public String toXML() {
		StringBuilder sb = new StringBuilder();
		encode();
		
		sb.append("<authority>").append("\r\n");
		sb.append("  <aclid>").append(aclid).append("</aclid>").append("\r\n");
		sb.append("  <comcode>").append(comcode).append("</comcode>").append("\r\n");
		sb.append("  <comname>").append(comname).append("</comname>").append("\r\n");
		sb.append("  <partcode>").append(partcode).append("</partcode>").append("\r\n");
		sb.append("  <partname>").append(partname).append("</partname>").append("\r\n");
		sb.append("  <empcode>").append(empcode).append("</empcode>").append("\r\n");
		sb.append("  <empname>").append(empname).append("</empname>").append("\r\n");
		sb.append("  <rolecode>").append(rolecode).append("</rolecode>").append("\r\n");
		sb.append("  <rolename>").append(rolename).append("</rolename>").append("\r\n");
		sb.append("  <defaultuser>").append(defaultuser).append("</defaultuser>").append("\r\n");
		sb.append("  <permission>").append(permission).append("</permission>").append("\r\n");
		sb.append("</authority>");
		
		return sb.toString();
	}
}
