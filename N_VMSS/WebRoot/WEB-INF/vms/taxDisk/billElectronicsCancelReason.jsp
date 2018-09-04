<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.model.createBill.TransInfo"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
</head>
<body>
	<form name="Form1" method="post" action="updateRemark.action"
		id="Form1">
		<input type="hidden" id="billId"
			value="<s:property value='transInfo.transId'/>" /> <input
			type="hidden" id="fapiaoType"
			value="<s:property value='transInfo.fapiaoType'/>" />
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
				class="current_status_menu">当前位置：</span> <span
				class="current_status_submenu1">销项税管理</span> <span
				class="current_status_submenu">电票管理</span> <span
				class="current_status_submenu">电票手动开票</span> <span
				class="current_status_submenu">查看失败原因</span>
		</div>
		<%
			String cancelReason = (String) request.getAttribute("transInfo");
		%>
		<div class="centercondition">
			<div>
				<table id="contenttable" class="lessGridS" cellspacing="0"
					width="100%" align="center" cellpadding="0"
					style="border-collapse: collapse;">
					<tr>
						<td></td>
					
					</tr>
					<tr>
						<td></td>
					
					</tr>
				
					<tr>
						<td align="right" style="vertical-align: middle">失败原因</td>
						<td><textarea name="transInfo.cancelReason"
								id="transInfo.cancelReason" cols="70" rows="5"
								readonly="readonly"><%=cancelReason%></textarea></td>
					</tr>
					<tr>
						<td></td>
					
					</tr>
					
				</table>
			</div>
			<div class="bottombtn">
				<input type="button" class="tbl_query_button" value="返回"
					onMouseMove="this.className='tbl_query_button_on'"
					onMouseOut="this.className='tbl_query_button'" name="btCancel"
					id="btCancel" onclick="window.close();" />
			</div>
		</div>
	</form>

</body>
</html>