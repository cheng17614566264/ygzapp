<!-- 发票分发页面 -->
<%@page import="com.cjit.vms.stock.util.StockUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增值税管理平台</title>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/editview.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/search.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/vms.js"></script>
</head>
<script type="text/javascript">
	function submitForm(actionUrl) {
		var form = document.getElementById("Form1");
		form.action = actionUrl;
		form.submit();
		form.action = "billInventoryList.action";
	}
	/**
	 * [分发]按钮
	 */
	function cmdDistribute() {
		var inventoryId = document.getElementsByName("inventoryId");
		var m = 0;
		var k = 0;
		for (var i = 0; i < inventoryId.length; i++) {
			if (inventoryId[i].checked == true) {
				m++;
				k = i;
			}
		}
		if (m == 0) {
			alert("请选择您要分发的库存发票");
			return;
		} else if (m > 1) {
			alert("请选择一条数据分发");
			return;
		}
		var cmdDistribute = document.getElementById("cmdDistribute");
		cmdDistribute.href = "initDistrubute.action?inventoryId="
				+ inventoryId[k].value;
	}
</script>
<body>
	<form name="Form1" method="post" action="billInventoryList.action"
		id="Form1">
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition"><s:component
						template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png"> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">库存管理</span> <span
							class="current_status_submenu">发票分发</span>
					</div> <!-- 查询框 -->
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%"
							border="0">
							<tr>
								<td style="text-align: right; width: 6%;">报税机构</td>
								<td style="text-align: left; width: 14%;"><input
									type="text" class="tbl_query_text" id="inst_Name" name="instId"
									value='' onclick="setOrg(this);" readonly="readonly"></td>
								<td style="text-align: right; width: 6%;">入库日期</td>
								<td style="text-align: left; width: 20%;"><input
									class="tbl_query_time" id="billInventory.putInStartDate"
									type="text" name="billInventory.putInStartDate"
									value="<s:property value='billInventory.putInStartDate'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billInventory.putInEndDate\')}'})"
									size='11' /> -- <input class="tbl_query_time"
									id="billInventory.putInEndDate" type="text"
									name="billInventory.putInEndDate"
									value="<s:property value='billInventory.putInEndDate'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billInventory.putInStartDate\')}'})"
									size='11' /></td>
								<td style="text-align: right; width: 6%;">发票类型</td>
								<td style="text-align: left; width: 14%;"><s:select
										id="billInventory.billType" name="billInventory.billType"
										list="#{'1':'增值税普通发票','0':'增值税专用发票'}" headerKey=''
										headerValue="全部" listKey='key' listValue='value'
										cssClass="tbl_query_text" /></td>
							</tr>
							<tr>
								<td width="25" style="text-align: right;">发票代码</td>
								<td width="130"><input id="billInventory.billId"
									class="tbl_query_text" name="billInventory.billId" type="text"
									value="<s:property value='billInventory.billId' />"
									maxlength="10" /></td>

								<td width="25" style="text-align: right; width: 6%;">发票起始号码</td>
								<td width="130"><input id="billInventory.billStartNo"
									class="tbl_query_text" name="billInventory.billStartNo"
									type="text"
									value="<s:property value='billInventory.billStartNo' />"
									maxlength="8" /></td>
								<td width="25" style="text-align: right; width: 6%;">发票截止号码</td>
								<td width="130"><input id="billInventory.billEndNo"
									class="tbl_query_text" name="billInventory.billEndNo"
									type="text"
									value="<s:property value='billInventory.billEndNo' />"
									maxlength="8" /></td>
							</tr>
							<tr>
								<td colspan="2" style="padding-left: 10.5%"><input
									type="button" onclick="submitForm('billDistributeList.action')"
									class="tbl_query_button" value="查询"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="btSubmit"
									id="btSubmit" style="margin-right: 30px" /></td>
							</tr>
						</table>
					</div> <!-- 功能框 -->
					<table id="tbl_tools" width="100%" border="0">
						<tr align="left">
							<td style="text-align: left"><a href="#"
								name="cmdDistribute" id="cmdDistribute"
								onClick="cmdDistribute()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1001.png" />
									分发
							</a></td>
						</tr>
					</table> <!-- 主列表 信息展示-->
					<div id="lessGridList1" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align: center"><input
									id="CheckAll" style="width: 13px; height: 13px;"
									type="checkbox" onClick="checkAll()" /></th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">报税机构</th>
								<th style="text-align: center">入库日期</th>
								<th style="text-align: center">发票类型</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票起始号码</th>
								<th style="text-align: center">发票终止号码</th>
								<th style="text-align: center">发票总张数</th>
								<th style="text-align: center">发票剩余张数</th>
								<%--
								<th style="text-align: center">操作</th>
								 --%>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">

								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td><s:if test='#iList.syCount=="0"'>
											<input type="checkbox" style="width: 13px; height: 13px;"
												name="inventoryId" value="<s:property value='inventoryId'/>"
												disabled="disabled" />
										</s:if> <s:else>
											<input type="checkbox" style="width: 13px; height: 13px;"
												name="inventoryId" value="<s:property value='inventoryId'/>" />
										</s:else></td>
									<td align="center"><s:property value='#stuts.count' /></td>
									<td><s:property value="instName" /></td>
									<td><s:date name="putInDate" format="yyyy-MM-dd" /></td>
									<td><s:property
											value='@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeName(billType)' /></td>
									<td><s:property value='billId' /></td>
									<td><s:property value='billStartNo' /></td>
									<td><s:property value='billEndNo' /></td>
									<td><s:property value='count' /></td>
									<td><s:property value='syCount' /></td>
									<%--
									<s:if
										test='@com.cjit.vms.stock.util.StockUtil@getvalue(@java.lang.String@valueOf(count),syCount)=="Y"'>
										<td align="center">不可修改</td>
									</s:if>
									<s:else>
										<td align="center"><a
											href="updatebillInventory.action?inventoryId=<s:property value="inventoryId"/>">
												<img
												src="<c:out value="${bopTheme}"/>/themes/images/icons/edit.png"
												title="修改">
										</a></td>
									</s:else>
									 --%>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="width: 100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
					</div></td>
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