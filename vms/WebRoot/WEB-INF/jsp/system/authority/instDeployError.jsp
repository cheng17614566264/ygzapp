<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="fmss.dao.entity.LoginDO,fmss.common.util.Constants"%>

<%@include file="../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="../../common/include.jsp"%>
	<title>新增机构</title>
	<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="css/easyui.css">
	<link rel="stylesheet" type="text/css" href="css/icon.css">
	<script type="text/javascript" src="js/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<c:out value='${webapp}'/>/js/jquery/region.js" charset="GBK"></script>
	<script language="javascript" type="text/javascript">
		$(document).ready(function(){
			  var instBlank = $("#instBlank").val();
			  var instError = $("#instError").val();
			  if("0" == instBlank){
				  alert("未选择机构，请选择某一公司机构!");
			  }
			  if("1" == instError){
				  alert("所选机构不是分公司，不能进行机构关联配置!");
			  }
			});
	</script>
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">
		<form id="main" method="post" action="updateDatastatus.action">
			<div id="editsubpanel" class="editsubpanel">
				<div style="overflow: auto; width: 100%;">
					<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
						<tr class="header">
							<th colspan="4">机构关联配置</th>
						</tr>
						<tr>
							<td >
								<s:if test="vei.instName == 0">
									<input id="instBlank" value="0" type="hidden" readonly/>
								</s:if>
								<s:if test="vei.instName == 1">
									<input id="instError" value="1" type="hidden" readonly/>
								</s:if>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</form>
	</div>

</body>
</html>
