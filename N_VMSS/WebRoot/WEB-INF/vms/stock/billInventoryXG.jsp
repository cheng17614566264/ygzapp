<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.cjit.vms.trans.util.DataUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.cjit.common.util.PaginationList"%>
<%@page import="com.cjit.vms.stock.entity.BillInventory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<link href="<%=webapp%>/page/subwindow.css" type="text/css"
	rel="stylesheet">
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js"
	type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>


<meta http-equiv="Pragma" content="no-cache" />
<base target="_self">
<title>票据信息修改</title>
<style type="text/css">
.detailInfoDIV {
	border: 1px solid green;
	background-color: khaki;
	top: 110px;
	left: 150px;
	width: 450px;
	height: 300px;
	position: absolute;
	z-index: 2;
	filter: alpha(opacity =     90);
	-moz-opacity: 0.9;
	display: none;
}

.unSelectBox {
	text-align: center;
	vertical-align: middle;
	height: 39px;
	overflow: hidden;
	moz-user-select: -moz-none;
	-moz-user-select: none;
	-o-user-select: none;
	-khtml-user-select: none; /* you could also put this in a class */
	-webkit-user-select: none; /* and add the CSS class here instead */
	-ms-user-select: none;
	user-select: none; /**禁止选中文字*/
}

.user_class {
	font-size: 12px;
	font-family: Arial, Verdana, 宋体;
	vertical-align: middle;
	border: 1px solid #CCCCCC;
	height: 26px;
	width: 126px;
	line-height: 24px;
	padding: 0 3px;
}

.page_used_invoice_num {
	font-size: 12px;
	font-family: Arial, Verdana, 宋体;
	vertical-align: middle;
	border: 1px solid #CCCCCC;
	height: 26px;
	width: 126px;
	line-height: 24px;
	padding: 0 3px;
}

#div {
	height: 100px;
}

.lessGrid {
	background-color: #ffffff;
	border-color: #ffffff;
	border: 0
}
</style>
</head>
<body scroll="no" style="overflow: hidden;">
	<div class="showBoxDiv">
		<form id="Form1"
			action="<c:out value='${webapp}'/>/doDistrubute.action?massage=frm"
			method="post">

			<div id="editpanel">
				<div id="editsubpanel" class="editsubpanel">
					<div style="width: 100%;">
						<div id="tbl_current_status">
							<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
								class="current_status_menu">当前位置：</span> <span
								class="current_status_submenu1">销项税管理</span> <span
								class="current_status_submenu">库存管理</span> <span
								class="current_status_submenu">发票入库</span> <span
								class="current_status_submenu">票据信息修改</span>
						</div>
						<div class="lessGrid">
							<table cellspacing="0" width="100%" align="center"
								cellpadding="0">
								<% BillInventory billInventory=(BillInventory)request.getAttribute("billInventory"); %>
								<tr>
									<td colspan="6" style="background-color: #004c7e; height: 45px"></td>
								</tr>
								<tr>
									<td colspan="6"></td>
								</tr>
								<tr>
								<tr>
									<td width="25%" class="lessGrid"></td>
									<td style="text-align: center" width="25%">库存编号</td>
									<td align="left" width="25%" class="lessGrid"><input
										id="inventoryId" class="tbl_query_text" type="text"
										name="inventoryId"
										value="<%= billInventory.getInventoryId()%>" readonly /></td>
									<td class="lessGrid" width="25%"></td>
								</tr>
								<tr>
									<td width="25%" class="lessGrid"></td>
									<td style="text-align: center" width="25%">报税机构</td>
									<td align="left" width="25%" class="lessGrid"><input
										id="instName" class="tbl_query_text" type="text"
										name="instName"
										value="<%= billInventory.getInstId()+" - "+billInventory.getInstName()%>"
										readonly /></td>
									<td class="lessGrid" width="25%"></td>
								</tr>
								<tr>
									<td class="lessGrid" width="25%"></td>
									<td style="text-align: center" width="25%">入库时间</td>
									<td align="left" width="25%" class="lessGrid">
										<% SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf.format(billInventory.getPutInDate()) ;
			%> <input id="putInDate" class="tbl_query_text" type="text"
										name="putInDate" value="<%= date%>" readonly />

									</td>
									<td class="lessGrid" width="25%"></td>
								</tr>
								<tr>
									<td class="lessGrid" width="25%"></td>
									<td style="text-align: center" width="25%">发票代码</td>
									<td><input id="billId" class="tbl_query_text" type="text"
										name="billId" value="<%= billInventory.getBillId()%>" /></td>
									<td class="lessGrid" width="25%"></td>
								</tr>
								<tr>
									<td class="lessGrid" width="25%"></td>
									<td style="text-align: center" width="25%">发票起始号码</td>
									<td><input id="billStartNo" class="tbl_query_text"
										type="text" name="billStartNo"
										value="<%= billInventory.getBillStartNo()%>" /></td>
									<td class="lessGrid" width="25%"></td>
								</tr>
								<tr>
									<td class="lessGrid" width="25%"></td>
									<td style="text-align: center" width="25%">发票截止号码</td>
									<td><input id="billEndNo" class="tbl_query_text"
										type="text" name="billEndNo"
										value="<%= billInventory.getBillEndNo()%>" /></td>
									<td class="lessGrid" width="25%"></td>
								</tr>
								<tr>
									<td class="lessGrid" width="25%"></td>
									<td style="text-align: center" width="25%">发票类型</td>
									<td><input id="billType" class="tbl_query_text"
										type="text" name="billTypeY"
										value="<%=DataUtil.getFapiaoTypeCH(billInventory.getBillType())%>"
										hidden readonly> <select name="billType" id="billType"
										width="130px" hidden>
											<option value="0">增值税专用发票</option>
											<option value="1">增值税普通发票</option>
									</select> <input id="xg" type="button" class="tbl_query_button"
										value="修改" onMouseMove="this.className='tbl_query_button_on'"
										onMouseOut="this.className='tbl_query_button'"></td>
									<td class="lessGrid" width="25%"></td>
								</tr>
							</table>
							<div></div>
							<div style="margin-top: 2%">
								<input type="button" class="tbl_query_button" value="保存"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="update"
									id="update" style="margin-left: 70%" /> <input type="button"
									class="tbl_query_button" value="返回"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'"
									onclick="submitForm()">
							</div>
							<div style="margin-top: 3%"></div>
						</div>
					</div>
				</div>
			</div>
		</form>
		<c:import url="${webapp}/page/webctrl/instTree/tree_include.jsp"
			charEncoding="UTF-8">
			<c:param name="treeType" value="radio" />
			<c:param name="bankName_tree" value="inst_Name" />
			<c:param name="bankId_tree" value="inst_id" />
			<c:param name="taskId_tree" value="" />
			<c:param name="task_tree" value="task_tree" />
			<c:param name="webapp" value="${webapp}" />
		</c:import>
	</div>
</body>
<script type="text/javascript">
/*
 * [保存]按钮
 */
$(function(){
	$("#update").click(function(){
		if(true==Check()){
			document.forms[0].action="<%=webapp%>/updatbill.action"
			document.forms[0].submit();
		}
})
	
})
// 类型验证
function  Checkinteger(obj,massage){
	var str=obj;
	str=str.replace(/^(\s)*|(\s)*$/g,"");
	var newPar=/^(-|\+)?\d+$/
	if(str.length>0 && newPar.test(str)==false)
	{
		alert(massage);
		obj.focus();
		return false;
	}else{
		return true;
	}
}
// 校验
function  Check(){
	if(false==Checknull($("#instName").val(), "请选择机构")){
		return false;
	}
	if(false==Checknull($("#billId").val(), "发票代码不可为空")){
		return false;
	}
	 if(Checkinteger($("#billId").val(),"发票代码应该为整数")==false){
		   return false;
	}
    if(Checkleng($("#billId").val(),"10","发票代码长度必须是10位")==false){
			return false;
	}
    if(false==Checknull($("#billStartNo").val(), "发票起始号不可以为空")){
    	return false;
    }
    if(Checkinteger($("#billStartNo").val(),"发票起始号码应该为整数")==false){
			 return false;
	}
	if(Checkleng($("#billStartNo").val(),"8","发票起始号码长度必须是8位")==false){
			return false;
	}
	if(Checknull($("#billEndNo").val(), "发票截止号不可以为空")==false){
		return false;
	}
	if(Checkinteger($("#billEndNo").val(),"发票终止号码应该为整数")==false){
		return false;
	}
	if(Checkleng($("#billEndNo").val(),"8","发票终止号码长度必须是8位")==false){
			return false;
	}
	if($("#billEndNo").val()*1<$("#billStartNo").val()*1){
			alert("发票终止号码不能小于发票起始号码");
			return false;
	}
	return true;
}
// 判空校验
function Checknull(obj,massage){
	var str=obj;
	if(str.length<=0||str==""){
		alert(massage);
	 	return false;
	}else{
		return true;
	}
}
//长度验证
function Checkleng(obj,leng,massage){
	var str=obj;
	str=str.replace(/^(\s)*|(\s)*$/g,"");
	if(str.length!=leng){
		alert(massage);
	 	return false;
	}else{
		return true;
	}
}
//[返回]
function submitForm(){
	document.forms[0].action = "<%=webapp%>/billInventoryList.action"
	document.forms[0].submit();
}

$(function(){
	$("input[name='billTypeY']").show();
	$("#xg").click(function(){
		if(true==confirm("确定要修改当前发票类型？")){
		  	$("input[name='billTypeY']").hide();
			$("select[name='billType']").show();
			$("#xg").hide();
		}
	})
})

</script>
</html>