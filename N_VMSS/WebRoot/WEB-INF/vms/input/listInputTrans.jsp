<!-- file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.input.model.InputInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	import="com.cjit.vms.input.model.InputInfoUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.opensymphony.util.BeanUtils"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
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
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<c:out value='${webapp}'/>/page/js/window.js" charset="UTF-8"></script>
<script type="text/javascript">
	// [查询]按钮
	function submit() {
		document.forms[0].submit();
	}

	function exportInputTrans() {
		submitAction(document.forms[0], "exportInputTrans.action");
		document.forms[0].action = "listInputTrans.action";
	}
	// [新增]按钮
	function createInVat() {
		submitAction(document.forms[0], "createInputVat.action");
		document.forms[0].action = "listInputTrans.action";
	}
	// [删除]按钮
	function deleteInputTrans() {
		var ids = document.getElementsByName("selectInputTransIds");
		var selectedIds = null;
		if (ids.length > 0) {
			for (var i = 0; i < ids.length; i++) {
				var id = ids[i];
				var j = 0;
				if (id.checked) {
					if (selectedIds != null) {
						selectedIds = selectedIds + "," + id.value;
					} else {
						selectedIds = id.value;
					}
					j++;
				}
			}
		}
		if (selectedIds == null) {
			alert("请选择要删除的记录");
		}
		submitAction(document.forms[0], "deleteInputTrans.action?selectedIds="
				+ selectedIds);
		document.forms[0].action = "listInputTrans.action";
	}
	// [导入]按钮
	function importData(webroot) {
		var fileId = document.getElementById("fileId");
		var fileNameLast = fileId.value.toLowerCase();
		if (fileNameLast.length > 0) {
			if (fileNameLast.lastIndexOf(".xls") > -1) {
				document.forms[0].action = webroot + "/importInputTrans.action";
				document.forms[0].submit();
				//document.forms[0].action = 'listInputVat.action';
				document.forms[0].action = "listInputTrans.action";
			} else {
				alert("文件格式不对，请上传Excel文件。");
			}
		} else {
			alert("请先选择要上传的文件。");
		}
	}
</script>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listInputTrans.action"
		id="Form1" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<%
				String currMonth = (String) request.getAttribute("currMonth");
			%>
			<input type="hidden" name="currMonth" id="currMonth"
				value="<%=currMonth%>" />
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">进项税管理</span> <span
							class="current_status_submenu">认证管理</span> <span
							class="current_status_submenu">数据导入</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td width="50">交易时间</td>
								<td width="280"><input class="tbl_query_time1"
									id="transBeginDate" type="text" name="transBeginDate"
									value="<s:property value='transBeginDate'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'transEndDate\')}'})"
									size='11' "/> -- <input class="tbl_query_time1"
									id="transEndDate" type="text" name="transEndDate"
									value="<s:property value='transEndDate'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'transBeginDate\')}'})"
									size='11' "/></td>
								<td width="50">交易机构</td>
								<td width="130"><input type="hidden" id="inst_id"
									name="bankCode" value='<s:property value="bankCode"/>'>
									<input type="text" class="tbl_query_text" id="inst_Name"
									name="bankName" value='<s:property value="bankName"/>'
									onclick="setOrg(this);" readonly="readonly"></td>
								<td width="72">供应商名称</td>
								<td width="90"><input type="text" class="tbl_query_text"
									name="vendorName" value="<s:property value='vendorName'/>" />
								</td>
								<%-- <td width="50">交易编号</td>
								<td><input type="text" class="tbl_query_text" name="dealNo"
									value="<s:property value='dealNo'/>" /></td> --%>

							</tr>
							<tr>
								<td style="padding-left: 88%" colspan="6"><input
									type="button" class="tbl_query_button" value="查询"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="BtnView"
									id="BtnView" onclick="submit()" /></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left" width="168">
								<!--
				<input type="button" class="tbl_query_button" value="新增"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView"  /> --> <%-- <a href="#" onclick="deleteInputTrans()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png" />
									删除
							</a> --%>
							</td>
							<td width="234"><input type='file' name='theFile'
								id='fileId' size='25' style="height: 26px;" /></td>
							<td><a href="#" onclick="importData('<%=webapp%>')"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png" />
									导入
							</a> <%-- <a href="#" onclick="exportInputTrans()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
							</a> --%></td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align: center"><input
									style="width: 13px; height: 13px;" id="CheckAll"
									type="checkbox"
									onclick="cbxselectall(this,'selectInputTransIds')" /></th>
								<th style="text-align: center">交易日期</th>
								<!-- <th style="text-align:center">交易类型</th> -->
								<th style="text-align: center">交易发生机构</th>
								<th style="text-align: center">发票号码</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">金额_人民币</th>
								<th style="text-align: center">税额_人民币</th>
								<th style="text-align: center">支出_人民币</th>
								<th style="text-align: center">供应商名称</th>
								<th style="text-align: center">发票状态</th>
								<th style="text-align: center" colspan="2">操作</th>
							</tr>
							<%
								PaginationList paginationList = (PaginationList) request.getAttribute("paginationList");
								if (paginationList != null) {
									List inputTransList = (List) paginationList.getRecordList();
									if (inputTransList != null && inputTransList.size() > 0) {
										for (int i = 0; i < inputTransList.size(); i++) {
											InputInfo inputTrans = (InputInfo) inputTransList.get(i);
											if (i % 2 == 0) {
							%>
							<tr class="lessGrid rowA">
								<%
									} else {
								%>
							
							<tr class="lessGrid rowB">
								<%
									}
								%>
								<td align="center"><input
									style="width: 13px; height: 13px;" type="checkbox"
									name="selectInputTransIds"
									value="<%=inputTrans.getBillId()+inputTrans.getBillCode() %>" /></td>
								<td align="left"><%=inputTrans.getBillDate()%>
								<td align="left"><%=inputTrans.getBillInst()%></td>
								<td align="left"><%=inputTrans.getBillId()%></td>

								<td align="center"><%=inputTrans.getBillCode()%></td>
								<td align="center"><%=inputTrans.getzAmt()%></td>
								<td align="center"><%=inputTrans.getzTax()%></td>
								<td align="center"><%=inputTrans.getzSumTax()%></td>
								<td align="center"><%=inputTrans.getName()%></td>
								<td align="left"><%=InputInfoUtil.billStatuMap.get(inputTrans.getBillStatu())%></td>
								<%-- <td align="center">
									<a href="editInputTrans.action?curPage=<s:property value='paginationList.currentPage'/>&id=<%=inputTrans.getId() %>&fromFlag=editList">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" />
									</a>
								</td> --%>
								<td align="center"><a href="javascript:void(0)"
									onClick="goToPage('<%=webapp%>/inputTransDetail.action?billCode=<%=inputTrans.getBillCode()%>&billId=<%=inputTrans.getBillId()%>') ">
										<img
										src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
										title="查看" style="border-width: 0px;" />
								</a></td>
							</tr>
							<%
								}
									}
								}
							%>
							</tr>
						</table>
					</div>
					<div id="anpBoud" align="Right"
						style="overflow: auto; width: 100%;">
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