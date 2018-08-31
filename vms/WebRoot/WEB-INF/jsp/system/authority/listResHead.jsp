<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="../../common/include.jsp"%>
	<%@include file="../../common/commen-ui.jsp"%>
	<title>资源配置</title>
	<script language="javascript">
		function goFrame(url){
			parent.location=url;
		}
		function doClick(obj){
			if(obj.id=="userSpan"){
				obj.className="";
				var roleSpan = document.getElementById("roleSpan");
				roleSpan.className="current_status_submenu";
				parent.listResTree.location="listResTreeUser.action";
			}
			if(obj.id=="roleSpan"){
				obj.className="";
				var userSpan = document.getElementById("userSpan");
				userSpan.className="current_status_submenu";
				
				parent.listResTree.location="listResTreeRole.action";
			}
		}
	</script>
</head>
<body>
<form name="frm" action="<c:out value='${webapp}'/>/listUserSearchResult.action" method="post" target="userSearchResultFrame">
<table id="tbl_main" cellpadding="0" cellspacing="0">
	<tr>
		<td align="left">
			<div id="tbl_current_status">
				<span><img src="<c:out value="${webapp}"/>/themes/images/icons/icon13.png" /></span>
				<span class="current_status_menu">当前位置：</span>
				<span class="current_status_submenu1">系统管理</span>
				<span class="current_status_submenu">基础信息管理</span>
				<span class="current_status_submenu">角色资源管理</span>
			</div>
		</td>
	</tr>
</table>
</form>
</body>
</html>
