package org.uengine.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

		
public class CRUDAppGenerator {
	public static String generateCRUDDataListPage(String sql,
												  String addDefAlias,
												  String updateDefVerAlias,
												  String deleteDefVerAlias,
												  String[] fieldNames,
												  String[] pkColumns,
												  String[] displayFieldNames) {
		String displayFieldName = "";
		for(int i = 0 ; i < displayFieldNames.length; i++){
			displayFieldName+=displayFieldNames[i]+"/";
		}
		
		String fieldName = "";
		for(int i = 0 ; i < fieldNames.length; i++){
			fieldName+=fieldNames[i]+"/";
		}
		
		String pkColumn="";
		for(int i = 0 ; i < pkColumns.length; i++){
			pkColumn+=pkColumns[i]+"/";
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("<%@ page pageEncoding=\"UTF-8\" %>");
		sb.append("<%@include file=\"../../../common/header.jsp\"%>"+"\r\n"+"\r\n");
		sb.append("<%@include file=\"../../../common/getLoggedUser.jsp\"%>"+"\r\n");
		sb.append("<%@page import=\"java.util.*\"%>"+"\r\n");
		sb.append("<%@page import=\"org.uengine.util.dao.*\"%>"+"\r\n");
		sb.append("<%@page import=\"org.uengine.persistence.dao.*\"%>"+"\r\n");
		sb.append("<%@page import=\"org.uengine.persistence.processinstance.*\"%>"+"\r\n");
		sb.append("<%@page import=\"org.uengine.ui.list.datamodel.DataMap\"%>"+"\r\n");
		sb.append("<%@page import=\"org.uengine.ui.list.datamodel.DataList\"%>"+"\r\n");
		sb.append("<%@page import=\"org.uengine.ui.list.datamodel.QueryCondition\"%>"+"\r\n");
		sb.append("<%@page import=\"org.uengine.ui.list.util.HttpUtils\"%>"+"\r\n");
		sb.append("<%@page import=\"org.uengine.ui.list.util.DAOListCursorUtil\"%>"+"\r\n");
		sb.append("<%@page import=\"org.uengine.util.dao.DefaultConnectionFactory\"%>"+"\r\n");
		sb.append("<%@ taglib uri=\"http://www.uengine.org/taglibs/bpm\" prefix=\"bpm\" %>"+"\r\n");
		
		sb.append("<%"+"\r\n");
		sb.append("	DataMap reqMap = HttpUtils.getDataMap(request, true);"+"\r\n");
		sb.append("	QueryCondition condition = new QueryCondition();"+"\r\n");
		sb.append("	DataList dl = null;"+"\r\n");
		sb.append("	int intPageCnt = 10;"+"\r\n");
		sb.append("	int nPagesetSize = 10;"+"\r\n");
		sb.append(" int totalCount = 0;"+"\r\n");
		sb.append("	int totalPageCount = 0;"+"\r\n");
		sb.append(" String sql=\""+sql+"\";"+"\r\n");
		sb.append("	int currentPage = reqMap.getInt(\"CURRENTPAGE\", 1);"+"\r\n");
		sb.append("	String strTarget = reqMap.getString(\"TARGETCOND\", \"procins.instancename\");"+"\r\n");
		sb.append("	String strKeyword = reqMap.getString(\"KEYWORDCOND\", \"\");"+"\r\n");
		sb.append("	String strDateKeyStart=reqMap.getString(\"INIT_START_DATE\", \"\");"+"\r\n");
		sb.append("	String strDateKeyEnd=reqMap.getString(\"INIT_END_DATE\", \"\");"+"\r\n");
		sb.append("	String strDefCategoryId = reqMap.getString(\"DEFCATEGORYID\", \"\");"+"\r\n");
		sb.append("	String strDefTypeId = reqMap.getString(\"DEFTYPEID\", \"\");"+"\r\n");
		sb.append("	String strSortColumn = reqMap.getString(\"SORT_COLUMN\", \"\");"+"\r\n");
		sb.append("	String strSortCond = reqMap.getString(\"SORT_COND\", \"\");"+"\r\n");
		sb.append("	String menuItemId=reqMap.getString(\"MENU_ITEMID\",\"item_bpm\");"+"\r\n");
		sb.append("	condition.setMap(reqMap);"+"\r\n");
		sb.append("	condition.setOnePageCount(intPageCnt);"+"\r\n");
		sb.append("	condition.setPageNo(currentPage);"+"\r\n");
		sb.append(" String displayFieldNamesTemp =\""+displayFieldName+"\";"+"\r\n");
		sb.append(" StringTokenizer st1 = new StringTokenizer( displayFieldNamesTemp ,\"/\");"+"\r\n");
		sb.append(" String[] displayFieldNames = new String[st1.countTokens()];"+"\r\n");
		sb.append(" int displayFieldNamesCount=0;"+"\r\n");
		sb.append(" while ( st1.hasMoreTokens() ) {"+"\r\n");
		sb.append(" 	displayFieldNames[displayFieldNamesCount++]= st1.nextToken();"+"\r\n");
		sb.append(" }"+"\r\n");
		sb.append(" String pkColumnsTemp = \""+pkColumn+"\";"+"\r\n");
		sb.append(" StringTokenizer st2 = new StringTokenizer( pkColumnsTemp ,\"/\");"+"\r\n");
		sb.append(" String[] pkColumns = new String[st2.countTokens()];"+"\r\n");
		sb.append(" int pkCount=0;"+"\r\n");
		sb.append(" while ( st2.hasMoreTokens() ) {"+"\r\n");
		sb.append("	 pkColumns[pkCount++]= st2.nextToken();"+"\r\n");
		sb.append(" }"+"\r\n");
		sb.append(" String fieldNamesTemp =\""+fieldName+"\";"+"\r\n");
		sb.append(" StringTokenizer st3 = new StringTokenizer( fieldNamesTemp ,\"/\");"+"\r\n");
		sb.append(" String[] fieldNames = new String[st3.countTokens()];"+"\r\n");
		sb.append(" int fieldNamesCount=0;"+"\r\n");
		sb.append(" while ( st3.hasMoreTokens() ) {"+"\r\n");
		sb.append(" 	fieldNames[fieldNamesCount++]= st3.nextToken();"+"\r\n");
		sb.append(" }"+"\r\n");		
		sb.append("%>"+"\r\n");
		sb.append("<form name=\"form\">"+"\r\n");
		sb.append("<html>"+"\r\n");
		sb.append("	<head>"+"\r\n");
		sb.append("  <link rel=\"stylesheet\" type=\"text/css\" href=\"<%=GlobalContext.WEB_CONTEXT_ROOT%>/style/bbs.css\">"+"\r\n");
		sb.append("	 <script language=\"JavaScript\" src=\"<%=GlobalContext.WEB_CONTEXT_ROOT%>/scripts/bbs.js\"></script>"+"\r\n");
		sb.append("	 <LINK href=\"<%=GlobalContext.WEB_CONTEXT_ROOT%>/style/uengine.css\" type=text/css rel=stylesheet>"+"\r\n");
		sb.append("	</head>"+"\r\n");
		sb.append("<body>"+"\r\n");
		sb.append("	<img src=\"<%=GlobalContext.WEB_CONTEXT_ROOT%>/images/icon_root.gif\" align=\"middle\" width=\"10\" height=\"11\" border=\"0\">"+"\r\n");
		sb.append("	<b>Data List</b>"+"\r\n");
		sb.append("	<table width=100% border=0 cellspacing=0 cellpadding=0>"+"\r\n");
		sb.append("  <tr height=1>"+"\r\n");
		sb.append("   <td align=center colspan=3 height=1 bgcolor=#dddddd></td>"+"\r\n");
		sb.append("	 </tr>"+"\r\n");
		sb.append(" </table>"+"\r\n");
		sb.append(" <input type=button name=List_Add value=ADD onclick=\"addColumn()\" >&nbsp;"+"\r\n");
		sb.append(" <input type=button name=List_Update value=UPDATE onclick=\"modifyColumn(this)\" >&nbsp;"+"\r\n");
		sb.append(" <input type=button name=List_Delete value=DELETE onclick=\"modifyColumn(this)\" >"+"\r\n");
		sb.append(" <p>"+"\r\n");
		sb.append(" <table width=100%>"+"\r\n");
		sb.append("  <tr>"+"\r\n");
		sb.append("   <td bgcolor=#B6CBEB>"+"\r\n");
		sb.append("    <table border=0 cellspacing=1 cellpadding=4 width=100%>"+"\r\n");
		sb.append("     <tr>"+"\r\n");
		sb.append("      <td><center>column</td>"+"\r\n");
		for(int i=0 ; i < displayFieldNames.length ; i++){
		sb.append("      <td><center>"+displayFieldNames[i]+"</td>"+"\r\n");
		}
		sb.append("     </tr>"+"\r\n");
		sb.append("<%"+"\r\n");
		sb.append("  java.sql.Connection con = DefaultConnectionFactory.create().getConnection();"+"\r\n");
		sb.append("  try{"+"\r\n");
		sb.append("      dl = DAOListCursorUtil.executeList(sql, condition, new ArrayList(), con);"+"\r\n");
		sb.append("      totalCount = (int)dl.getTotalCount();"+"\r\n");
		sb.append("      totalPageCount = dl.getTotalPageNo();"+"\r\n");
		sb.append("  }catch(Exception e){"+"\r\n");
		sb.append("      throw e;"+"\r\n");
		sb.append("  }finally{"+"\r\n");
		sb.append("      if( con != null ){con.close();}"+"\r\n");
		sb.append("  }"+"\r\n");
		sb.append("  String style=\"bg\";"+"\r\n");
		sb.append("  boolean isGray = false;"+"\r\n");
		sb.append("  String bgcolor;"+"\r\n");
		sb.append("  if(totalCount > 0){"+"\r\n");
		sb.append("    for( int i=0; i<dl.size(); i++ ){"+"\r\n");
    	sb.append("     bgcolor = (isGray ? \"#e9e9e9\":\"white\");"+"\r\n");
		sb.append("     isGray = !isGray;"+"\r\n");
		sb.append("%>"+"\r\n");
		sb.append("    <tr>"+"\r\n");
		sb.append("     <td bgcolor=\"white\"><input type=\"checkbox\" name=\"List_CheckBox\"></td>"+"\r\n");
		sb.append("<%"+"\r\n");
		sb.append("     DataMap tmpMap = (DataMap)dl.get(i);"+"\r\n");
		sb.append("     for(int j=0 ; j < displayFieldNames.length ; j++){"+"\r\n");
		sb.append("      String fName = fieldNames[j];"+"\r\n");
		sb.append("      Object fieldValue = tmpMap.getString(fName,\"\");"+"\r\n");
		sb.append("      String inputName = \"field_\"+fieldNames[j]+\"_\"+i;"+"\r\n");
		sb.append("%>"+"\r\n");				
		sb.append(" 	<td bgcolor=\"white\"><%=fieldValue%><input type=\"hidden\" name=\"<%=inputName%>\" value=\"<%=fieldValue%>\"></td>"+"\r\n");
		sb.append("<%"+"\r\n");
		sb.append(" 	}"+"\r\n");
		sb.append("%>"+"\r\n");
		sb.append("    </tr>"+"\r\n");
		sb.append("<%"+"\r\n");
		sb.append("	   }"+"\r\n");
		sb.append("	}"+"\r\n");
		sb.append("%>"+"\r\n");
		sb.append("   </table>"+"\r\n");
		sb.append("  </td>"+"\r\n");
		sb.append(" </tr>"+"\r\n");
		sb.append(" <tr>"+"\r\n");
		sb.append("  <td align=center>"+"\r\n");
		sb.append("<%"+"\r\n");
		sb.append("	if(totalCount>0){"+"\r\n");
		sb.append("%>"+"\r\n");	
		sb.append("	<br style=\"line-height:15px;\">"+"\r\n");
		sb.append("	<bpm:page link=\"\" totalcount=\"<%=totalCount%>\" pagecount=\"<%=intPageCnt%>\" pagelimit=\"<%=nPagesetSize%>\" currentpage=\"<%=currentPage%>\" />"+"\r\n");
		sb.append("	<br>"+"\r\n");
		sb.append("<%"+"\r\n");
		sb.append("	}"+"\r\n");
		sb.append("%>"+"\r\n");
		sb.append("  </td>"+"\r\n");
		sb.append(" </tr>"+"\r\n");
		sb.append("</table>"+"\r\n");
		sb.append("<script>"+"\r\n");
		sb.append("function addColumn(){"+"\r\n");
		sb.append("	url = \"<%=GlobalContext.WEB_CONTEXT_ROOT%>/processmanager/initiateForm.jsp?alias="+addDefAlias+"\";"+"\r\n");
		sb.append("	window.open(url,'AddList','width=800,height=600,resizable=yes,scrollbars=yes');"+"\r\n");
		sb.append("}"+"\r\n");
		sb.append("function modifyColumn(obj){"+"\r\n");
		sb.append("	var alias=\"\";"+"\r\n");
		sb.append("	if(obj.name==\"List_Update\"){"+"\r\n");
		sb.append("	 alias = \""+updateDefVerAlias+"\";"+"\r\n");
		sb.append("	}else if(obj.name==\"List_Delete\"){"+"\r\n");
		sb.append("  alias = \""+deleteDefVerAlias+"\";"+"\r\n");		
		sb.append("	}"+"\r\n");
		sb.append("	pageCount = 10*<%=currentPage%>-10;"+"\r\n");
		sb.append("	checkIndex=-1;"+"\r\n");
		sb.append("	for(i=0; i<document.form.List_CheckBox.length; i++){"+"\r\n");
		sb.append("  if(document.form.List_CheckBox[i].checked==true){"+"\r\n");
		sb.append("		checkIndex = i;"+"\r\n");
		sb.append("	 }"+"\r\n");
		sb.append("	 if((i==document.form.List_CheckBox.length-1)&&checkIndex==-1){"+"\r\n");
		sb.append("		return ;"+"\r\n");
		sb.append("	 }"+"\r\n");
		sb.append("	}"+"\r\n");
		sb.append("	if(checkIndex==-1){"+"\r\n");
		sb.append("  if(document.form.List_CheckBox.checked==true){"+"\r\n");
		sb.append("		checkIndex=0;"+"\r\n");
		sb.append("	 }else{"+"\r\n");
		sb.append("		return;"+"\r\n");
		sb.append("  }"+"\r\n");
		sb.append("	}"+"\r\n");
		sb.append("	url = \"<%=GlobalContext.WEB_CONTEXT_ROOT%>/processmanager/initiateForm.jsp?alias=\"+alias;"+"\r\n");
		sb.append("	checkIndex = pageCount+checkIndex;"+"\r\n");
		sb.append("<%"+"\r\n");
		sb.append("	for(int i=0; i < pkCount; i++){"+"\r\n");
		sb.append("  String pkIntputNameTemp = \"field_\"+pkColumns[i];"+"\r\n");
		sb.append("%>"+"\r\n");
		sb.append(" pkIntputName = \"<%=pkIntputNameTemp%>\"+\"_\"+checkIndex;"+"\r\n");
		sb.append("	url = url + \"&pk<%=i+1%>=\" + document.form.all[pkIntputName].value;"+"\r\n");
		sb.append("<%"+"\r\n");	
		sb.append("	}"+"\r\n");
		sb.append("%>"+"\r\n");
		sb.append("	window.open(url,'modifyColumn','width=800,height=600,resizable=yes,scrollbars=yes');"+"\r\n");
		sb.append("}"+"\r\n");
		sb.append("</script>"+"\r\n");
		sb.append("</form>"+"\r\n");
		sb.append("<form name=\"listForm\" method=\"POST\" action=\"?\" onactivate=\"setListForm(this);\">"+"\r\n");
		sb.append("	<input type=\"hidden\" name=\"currentPage\" value=\"<%=currentPage%>\">"+"\r\n");
		sb.append(" <input type=\"hidden\" name=\"sort_column\" value=\"<%=strSortColumn%>\">"+"\r\n");
		sb.append("	<input type=\"hidden\" name=\"sort_cond\" value=\"<%=strSortCond%>\">"+"\r\n");
		sb.append("	<input type=\"hidden\" name=\"listURL\" value=\"<%=GlobalContext.WEB_CONTEXT_ROOT%>/processmanager/processInstanceListDetail.jsp\">"+"\r\n");
		sb.append("	<input type=\"hidden\" name=\"TARGETCOND\" value=\"<%=strTarget%>\">"+"\r\n");
		sb.append("</form>"+"\r\n");
		sb.append("</body>"+"\r\n");
		sb.append("</html>"+"\r\n");
		
		return sb.toString();		
	}
	public static void main(String[] args) {
/*		String sql="select VAR1 as VAR1, VAR2 as VAR2, VAR3 as VAR3, VAR4 as VAR4 from TEST";
		String delete = "DeleteDataProcess_Alias_TEST1184808938625";
		String update = "UpdateDataProcess_Alias_TEST1184808938625";
		String add = "AddDataProcess_Alias_TEST1184808938625";
		String[] pk = {"VAR3","VAR4"};
		String[] display = {"VAR1","VAR2","VAR3","VAR4"};
		String source= generateCRUDDataListPage(sql,add,update,delete,pk,display);
		//System.out.println(source);
		BufferedWriter fileOut;
		try {
			fileOut = new BufferedWriter(new FileWriter("c:\\CRUDDataListTemplate.jsp"));
			fileOut.write(source); 
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    */
	}
}
