<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/../../page/include.jsp"%>
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
<body onLoad="javascript:tips()" onmousemove="MM(event)"
	onmouseout="MO(event)">
	<form name="Form1" method="post" action="" id="Form1">
		<input type="hidden" id="billId"
			value='<%=request.getAttribute("customerid") %>' />
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
				class="current_status_menu">当前位置：</span> <span
				class="current_status_submenu1">销项税管理</span> <span
				class="current_status_submenu">客户管理</span> <span
				class="current_status_submenu">信息审核</span> <span
				class="current_status_submenu">添加退回原因</span>
		</div>
		<div class="centercondition">
			<!-- <div class="blankbox" id="blankbox" style="overflow:auto;height: 100%;" > -->
			<div id="bankbox" style="width: 100%; height: 150px;">
				<table id="contenttable" class="lessGridS" cellspacing="0"
					width="100%" align="center" cellpadding="0"
					style="border-collapse: collapse;">
					<tr>
						<td align="right" style="vertical-align: middle" width="15%">
							退回原因</td>
						<%-- 	</tr>
			<tr>
				<td>客户编号</td>
				<td><%=request.getAttribute("customerid") %></td>
			</tr>
			<tr>
				<td>客户名称</td>
				<td><%=request.getAttribute("customerName") %></td>
			</tr> 
			<tr>  --%>
						<td><textarea id="cancelReason" cols="100" rows="8"></textarea>
						</td>
					</tr>
				</table>
			</div>
			<!-- </div> -->
			<div class="bottombtn" style="height: 50px;">
				<input type="button" class="tbl_query_button" value="保存"
					onMouseMove="this.className='tbl_query_button_on'"
					onMouseOut="this.className='tbl_query_button'" name="btSave"
					id="btSave" onclick="cancel()" /> <input type="button"
					class="tbl_query_button" value="返回"
					onMouseMove="this.className='tbl_query_button_on'"
					onMouseOut="this.className='tbl_query_button'" name="btCancel"
					id="btCancel" onclick="window.close();" />
			</div>
		</div>
	</form>
	<script language="javascript" type="text/javascript" charset="UTF-8">
window.onload = function(){
	var hightValue = screen.availHeight - 270;
	var hightValueStr = "height:"+ hightValue + "px";
	
	
	if (typeof(eval("document.all.blankbox"))!= "undefined"){
		document.getElementById("blankbox").setAttribute("style", hightValueStr);
	}
}

function cancel() {
	var billId = document.getElementById("billId").value;
	var cancelReason = document.getElementById("cancelReason").value;
	if (cancelReason == ''){
		alert("退回原因不能为空");
		return false;
	}
	$.ajax({url: 'updateCustomerX.action',
			type: 'POST',
			async:false,
			data:{billId:billId, cancelReason:cancelReason},
			dataType: 'html',
			timeout: 1000,
			error: function(){return false;},
			success: function(result){
				if (result == 'success') {
			  		//window.dialogArguments.query();
					//window.close();
window.opener=null;  
window.open('','_self');  
window.close();
  
					
					return true;
				} else {
					alert("添加退回原因失败");
					return false;
				}
			}
			});
}
</script>
</body>
</html>