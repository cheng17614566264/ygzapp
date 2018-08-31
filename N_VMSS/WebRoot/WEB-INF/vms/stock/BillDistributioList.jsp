<!--file: <%=request.getRequestURI() %> -->
<%@page import="com.cjit.vms.trans.model.storage.PaperInvoiceListInfo"%>
<%@page import="java.util.List"%>
<%@page import="com.cjit.common.util.PaginationList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/lhb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>纸质发票管理</title>

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
</style>
<script type="text/javascript">
function check(){
	var inventoryId=document.getElementsByName("inventoryId");
	var count=0;
	for(var i=0;i<inventoryId.length;i++){
		if(inventoryId[i].checked==false){
			alert("请选择信息要操作的数据");
		}else{
			count++;
		}
	}
}
</script>
</head>
<body>
	<form id="main"
		action="<c:out value='${webapp}'/>/listPageInvoice.action"
		method="post" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">库存管理</span> <span
							class="current_status_submenu">发票跟踪</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
							<tr>
								<td width="60">开票员所属机构</td>
								<td width="130"><input type="hidden" id="receiveInstId"
									name="invoiceDistribute.receiveInstId"
									value='<s:property value="invoiceDistribute.receiveInstId"/>'>
									<input type="text" class="tbl_query_text" id="receiveInstName"
									name="invoiceDistribute.receiveInstName"
									value='<s:property value="invoiceDistribute.receiveInstName"/>'
									onclick="setOrg(this, '#receiveInstId', '#receiveInstName');"
									readonly="readonly"></td>
								<td width="50">开票员编号</td>
								<td width="130"><input id="receiveUserId"
									class="tbl_query_text" name="invoiceDistribute.receiveUserId"
									type="text"
									value="<s:property value='invoiceDistribute.receiveUserId' />" /></td>
								<td width="50">开票员名称</td>
								<td width="130"><input id="receiveUserId"
									class="tbl_query_text" name="invoiceDistribute.receiveUserId"
									type="text"
									value="<s:property value='invoiceDistribute.receiveUserId' />" /></td>
							</tr>
							<tr>
								<td width="50">发票代码</td>
								<td width="130"><input id="invoiceCode"
									class="tbl_query_text" name="invoiceDistribute.invoiceCode"
									type="text"
									value="<s:property value='invoiceDistribute.invoiceCode' />"
									onkeypress="checkkey(value);" maxlength="10" /></td>
								<td width="50">分发日期</td>
								<td width="190"><input type="hidden" id="createTime"
									value="<s:property value='invoiceDistribute.createTime' />" />
									<input name="invoiceDistribute.createTime" type="text"
									value="<s:property value='invoiceDistribute.createTime' />"
									class="tbl_query_time1"
									onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:document.getElementById('createTime').value == '' ? new Date():document.getElementById('createTime').value})" />
								</td>

								<td width="50">发票起始号码</td>
								<td width="130"><input id="invoiceNo"
									class="tbl_query_text" name="invoiceDistribute.invoiceNo"
									type="text"
									value="<s:property value='invoiceDistribute.invoiceNo' />"
									onkeypress="checkkey(value);" maxlength="8" /></td>
							</tr>
							<tr>
								<td width="50">发票截止号码</td>
								<td width="130"><input id="invoiceNo"
									class="tbl_query_text" name="invoiceDistribute.invoiceNo"
									type="text"
									value="<s:property value='invoiceDistribute.invoiceNo' />"
									onkeypress="checkkey(value);" maxlength="8" /></td>
								<td width="50">接受状态</td>
								<td width="130"><select name="" style="width: 125px">
										<option value="">所有</option>
										<option value="0">未接收</option>
										<option value="1">已接收</option>
								</select></td>
								<td width="50">发票类型</td>
								<td width="130"><select name="">
										<option value="">所有</option>
										<option value="0">增值税专用发票</option>
										<option value="1">增值税普通发票</option>
								</select></td>
							</tr>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td colspan="2"><input type="button"
									class="tbl_query_button"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'"
									onclick="submitForm('listDistrubute.action');" name="cmdSelect"
									value="查询" id="cmdSelect" /></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr align="left">
							<td style="text-align: left"><a href="#" name="check"
								id="check" onclick="check()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1016.png" />
									确认接收
							</a></td>
						</tr>
					</table>
					<div id="lessGridList1" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align: center"><input
									id="CheckAll" style="width: 13px; height: 13px;"
									type="checkbox" onClick="checkAll()" /></th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">开票员所属机构</th>
								<th style="text-align: center">开票员名称</th>
								<th style="text-align: center">分发日期</th>
								<th style="text-align: center">发票类型</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票起始号码</th>
								<th style="text-align: center">发票终止号码</th>
								<th style="text-align: center">开票终端编号</th>
								<th style="text-align: center">发票数量</th>
								<th style="text-align: center">是否接收</th>
								<th style="text-align: center">操作状态</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">

								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td><input type="checkbox"
										style="width: 13px; height: 13px;" name="inventoryId"
										value="<s:property value="#iList.inventoryId"/>" /></td>
									<td align="center"><s:property value='#stuts.count' /></td>
									<td><s:property value="instId" /></td>
									<td><s:property value="kpyName" /></td>
									<td><s:date name="disDate" format="yyyy-MM-dd" /></td>
									<td><s:property
											value='@com.cjit.vms.stock.entity.stockUtil@getbilltype(billType)' /></td>
									<td><s:property value='billId' /></td>
									<td><s:property value='billStartNo' /></td>
									<td><s:property value='billEndNo' /></td>
									<td><s:property value='taxNo' /></td>
									<td><s:property value='ffCount' /></td>
									<td><s:property
											value='@com.cjit.vms.stock.entity.stockUtil@getjsenter(jsEnter)' /></td>
									<td><a
										href="#.action?reqSource=billCancelApply&billId=<s:property value='inventoryId'/>">
											<img
											src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
											title="" style="border-width: 0px;" />
									</a></td>
							</s:iterator>
						</table>
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
</body>
</html>