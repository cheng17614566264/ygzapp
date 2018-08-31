<%@page import="com.cjit.vms.input.model.SubjectEntity"%>
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
<title>纸质发票分发</title>
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
<script type="text/javascript">
//[返回]按钮
function submitForm(actionUrl){
	document.forms[0].action = actionUrl;
	document.forms[0].submit();
	document.forms[0].action = '';
}
</script>
</head>
<body scroll="no" style="overflow: hidden;">
	<div class="showBoxDiv">
		<form id="frm"
			action="<c:out value='${webapp}'/>/doDistrubute.action?massage=frm"
			method="post">
			<table id="tbl_main" cellpadding="0" cellspacing="0"
				class="tablewh100">
				<tr>
					<td class="centercondition">
						<div id="tbl_current_status">
							<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
								class="current_status_menu">当前位置：</span> <span
								class="current_status_submenu1">进项税管理</span> <span
								class="current_status_submenu">进项管理</span> <span
								class="current_status_submenu">进项转出比例计算</span> <span
								class="current_status_submenu">数据明细</span>
						</div> <!-- 查询条件框 -->
						<div class="widthauto1">
							<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
								<tr>
									<td style="text-align: right; width: 8%;">报税机构</td>
									<td style="text-align: left; width: 14%;"><input
										type="text" class="tbl_query_text" id="inst_Name"
										name="instId" value='' onclick="setOrg(this);"
										readonly="readonly"></td>
									<td colspan="4"><input type="button"
										class="tbl_query_button"
										onMouseMove="this.className='tbl_query_button_on'"
										onMouseOut="this.className='tbl_query_button'"
										onclick="submitForm('invoiceSelectMassage.action');"
										name="cmdQueryBtn" value="查询" id="cmdQueryBtn" /></td>
								</tr>
							</table>
						</div> <!-- 功能实现框 -->
						<table id="tbl_tools" width="100%" border="0">
							<tr align="left">
								<td style="text-align: left" colspan="4"><input
									type="button" class="tbl_query_button"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'"
									onclick="submitForm('listInvoiceInSurtaxBLJS.action');"
									name="cmdQueryBtn" value="返回" id="cmdQueryBtn" /></td>
							</tr>
						</table>
						<div id="lessGridList4" style="overflow: auto; width: 100%;">

							<table class="lessGrid" cellspacing="0" rules="all" border="0"
								cellpadding="0" style="border-collapse: collapse; width: 100%;">
								<tr class="lessGrid head">
									<th width="3%" style="text-align: center"><input
										id="CheckAll" style="width: 13px; height: 13px;"
										type="checkbox" onClick="checkAll(this,'billId')" /></th>
									<th style="text-align: center">报税机构</th>
									<th style="text-align: center">科目</th>
									<th style="text-align: center">月度</th>
									<th style="text-align: center">险种名称</th>
									<th style="text-align: center">税率编码</th>
									<th style="text-align: center">税率</th>
									<th style="text-align: center">贷方本位币发生额</th>
								</tr>
								<%  List<SubjectEntity> list=(List)request.getAttribute("invoiceSelectMassage");
									if(list!=null&&list.size()>0){
										for(int i=0 ;i<list.size();i++){
											SubjectEntity sEntity=list.get(i);
									%>
								<tr>
									<td style="text-align: center"><input type="checkbox"
										style="width: 13px; height: 13px;" name="billId"
										value="<s:property value='#iList.billId'/>-<s:property value='#iList.billCode' />" />
									</td>
									<td style="text-align: center"><%= sEntity.getInstName() %></td>
									<td style="text-align: center"><%= sEntity.getDirectionName() %></td>
									<td style="text-align: center"><%= sEntity.getYearMonth() %></td>
									<td style="text-align: center"><%= sEntity.getPlanLongDescName() %></td>
									<td style="text-align: center"><%= sEntity.getTaxRateCode() %></td>
									<td style="text-align: center"><%= sEntity.getTaxRateName()==null?"":sEntity.getTaxRateName()%></td>
									<td style="text-align: center"><%= sEntity.getCreditDesc() %></td>
								</tr>
								<%
								}
							}
						%>
							</table>
							<input type="hidden" name="currentPage"
								value="<s:property value="paginationList.currentPage"/>" /> <input
								type="hidden" name="o_bill_id" id="o_bill_id" value="" />
						</div>
						<div id="anpBoud" align="Right" style="width: 100%;">
							<table width="100%" cellspacing="0" border="0">
								<tr>
									<td align="right"><s:component template="pagediv" /></td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
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
</html>