<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="../../../page/include.jsp"%>
	<title>科目字典查看</title>
	<%-- <link type="text/css" href="<%=webapp%>/page/subWindow.css" rel="stylesheet">
	<link href="<%=webapp%>/page/subWindow.css" type="text/css" rel="stylesheet"> --%>
	<link rel="stylesheet" type="text/css" href="css/easyui.css">
	<link rel="stylesheet" type="text/css" href="css/icon.css">
	<link href="<c:out value="${sysTheme}"/>/css/main.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=webapp%>/page/js/page/jquery.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/SimpleTree/js/validator.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/page/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/region.js" charset="GBK"></script>
	<script language="javascript" type="text/javascript">
		function editSubjectDic(id){
			var state = document.getElementById("valueState"+id);
			var data = 1;
			if(state.checked){
				data = 1;
			}else{
				data = 0;
			}
			<%-- $(location).attr('href', "<%=webapp%>/editSubjectDic.action?id="+id+"&state="+data); --%>
			document.frm.action="editSubjectDic.action?id="+id+"&state="+data;
			document.frm.method="post";
			document.frm.submit();
		}
	</script>
</head>
<body>
    <form name="frm" id="frm" action="" method="post">
		<div class="centercondition1">
	       <table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" display="none" style="border-collapse: collapse;width: 100%;">
	           <tr class="lessGrid head">
					<!-- <th width="3%" style="text-align:center"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'ids')" /></th> -->
					<th width="10%"  style="text-align:center;font-size:14px;" nowrap>科目代码</th>
					<th width="10%"  style="text-align:center;font-size:14px;" nowrap>科目名称</th>
					<th width="10%" style="text-align:center;font-size:14px;">是否免税</th>
					<th width="10%" style="text-align:center;font-size:14px;">是否启用</th>
					<th width="5%"  style="text-align:center;font-size:14px;" nowrap>操作</th>
	           </tr>
	           <s:iterator value="subjectDicList" id="iList" status="stuts">
	           		<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>" >
						<%-- <td><input type="checkbox" style="width:13px;height:13px;" name="ids" value="<s:property value="#iList.ID"/>" /></td> --%>
						<td><s:property value="#iList.SUBJECT_ID"/></td>
						<td><s:property value="#iList.SUBJECT_NAME"/></td>
						<td><s:if test='#iList.CATEGORY=="1"'>否</s:if><s:elseif test='#iList.CATEGORY=="2"'>是</s:elseif></td>
						<td><input type="checkbox" id="valueState<s:property value='#iList.ID'/>" name="valueState" <s:if test='#iList.VALID_STATE==1'>checked</s:if> ></td>
						<td align="center">
							<a href="javascript:void(0)" style="text-decoration:none;" onClick="editSubjectDic(<s:property value='#iList.ID'/>)">保存</a>
						</td>
					</tr>
	           </s:iterator>
	       </table>
	    </div>
	    <%-- <div id="anpBoud" align="Right" style="width:100%;vlign=top;">
	        <table width="100%" cellspacing="0" border="0">
	            <tr>
	                <td align="left"></td>
	                <td align="right"><s:component template="pagediv"/></td>
	            </tr>
	        </table>
	        </div>--%>
	    </div> 
    </form>
</body>
</html>
