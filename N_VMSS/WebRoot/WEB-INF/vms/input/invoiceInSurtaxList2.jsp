<%@page import="com.cjit.vms.input.model.InputInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.cjit.vms.input.model.InputInfoUtil"%>
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
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/editview.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>进项税发票信息展示</title>
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

.ellipsis_div {
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}
</style>
<script type="text/javascript">
	function reset(url) {
		//OpenModalWindow(url,650,350,true);
		location.href = url;
	}
	// 打开空白发票作废页面
	function invalidBlankPaperInvoice() {
		var url = 'invalidBlankPaperInvoice.action';
		OpenModalWindow(url, 650, 250, true);
	}
	function submitForm(actionUrl) {
		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
		form.action = "listInvoiceInSurtax.action";
	}

	function deleteTransBatch(actionUrl) {

		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
		form.action = "listInvoiceInSurtax.action";
	}

	function hideDetailInfoDIV() {
		document.getElementById("detailInfoDIV").style.display = 'none';
	}

	function showDetailInfoDIV(logID) {
		var currRow = window.event.srcElement.parentElement.parentElement;// 获取当前行

		document.getElementById("_td2").innerHTML = logID;

		for (var i = 3; i < 16; i++) {
			document.getElementById("_td" + i).innerHTML = currRow.cells[i - 1].title;
		}

		document.getElementById("detailInfoDIV").style.display = 'block';
	}

	function output() {
		//拷贝 
		var elTable = document.getElementById("lessGridList"); //这里的page1 是包含表格的Div层的ID
		var oRangeRef = document.body.createTextRange();
		oRangeRef.moveToElementText(elTable);
		oRangeRef.execCommand("Copy");

		//粘贴 
		try {
			var appExcel = new ActiveXObject("Excel.Application");
			appExcel.Visible = true;
			appExcel.Workbooks.Add().Worksheets.Item(1).Paste();
			//appExcel = null; 
		} catch (e) {
			alert("使用此功能必须在浏览器中设置:Internet选项->安全->将本站加入“受信任的站点”。");
		}

		var elTable1 = document.getElementById("lessGridList");
		var oRangeRef1 = document.body.createTextRange();
		oRangeRef1.moveToElementText(elTable1);
		oRangeRef1.execCommand("Copy");

		//粘贴 
		try {
			appExcel.Visible = true;
			appExcel.Worksheets.Item(2).Paste();
			appExcel1 = null;
		} catch (e) {
			alert("使用此功能必须在浏览器中设置:Internet选项->安全->将本站加入“受信任的站点”。");
		}
	}
	/* 
	 *进项转出
	 */
	function transInput() {
		var ids = document.getElementsByName("billId");
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
			alert("请选择要转出的记录");
		}
		submitAction(document.forms[0], "transInputInfo.action?selectedIds="
				+ selectedIds);
		document.forms[0].action = "listInvoiceInSurtax.action";
	}
	
	//转出比例计算
	function cmdRollBackBtn() {
		$.ajax({
			url : 'reckonAction.action',// 跳转到 action    
			type : 'post',
			dataType : 'text',
			success : function(ajaxReturn) {
				var returnJson = $.parseJSON(ajaxReturn);
					if (returnJson.isNormal) {
						alert("转出比例计算已完成");
					}else{
						alert(returnJson.message);
					}
			},
			error : function(ajaxReturn) {
				var returnJson = $.parseJSON(ajaxReturn);
				if (returnJson.isNormal) {
					alert("转出比例计算已完成");
				}else{
					alert(returnJson.message);
				}
			}
		});
	}
	/* $(function(){
		alert("0")
		$("#spacetr").css("height",$("#head").css("height"));
		$("#h2").css("width",$("#2").css("width"));
		$("#h3").css("width",$("#3").css("width"));
		$("#h4").css("width",$("#4").css("width"));
		$("#h5").css("width",$("#5").css("width"));
		$("#h6").css("width",$("#6").css("width"));
		$("#h7").css("width",$("#7").css("width"));
		$("#h8").css("width",$("#8").css("width"));
		$("#h9").css("width",$("#9").css("width"));
		$("#h10").css("width",$("#10").css("width"));
		$("#h11").css("width",$("#11").css("width"));
		$("#h12").css("width",$("#12").css("width"));
		$("#h13").css("width",$("#13").css("width"));
		$("#h14").css("width",$("#14").css("width"));
		$("#h15").css("width",$("#15").css("width"));
		$("#h16").css("width",$("#16").css("width"));
		$("#h17").css("width",$("#17").css("width"));
		$("#h18").css("width",$("#18").css("width"));
	}) */
</script>
</head>
<body>
	<form id="main"
		action="<c:out value='${webapp}'/>/listInvoiceInSurtax.action"
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
							class="current_status_submenu">进项发票信息展示</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td style="text-align: right; width: 6%;">认证日期</td>
								<td style="text-align: left; width: 20%;"><input
									class="tbl_query_time" id="inputInfo.dealNoStarDate"
									type="text" name="inputInfo.dealNoStarDate"
									value="<s:property value='inputInfo.dealNoStarDate'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'inputInfo.dealNoEndDate\')}'})"
									size='11' /> -- <input class="tbl_query_time"
									id="inputInfo.dealNoEndDate" type="text"
									name="inputInfo.dealNoEndDate"
									value="<s:property value='inputInfo.dealNoEndDate'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'inputInfo.dealNoStarDate\')}'})"
									size='11' /></td>
								<td style="text-align: right; width: 6%;">报税机构</td>
								<td style="text-align: left; width: 14%;"><input
									type="text" class="tbl_query_text" id="inst_Name"
									name="inputInfo.billInst" value='' onclick="setOrg(this);"
									readonly="readonly"></td>
								<td style="text-align: right; width: 6%;">供应商名称</td>
								<td style="text-align: left; width: 14%;"><input
									id="inputInfo.name" class="tbl_query_text" type="text"
									name="inputInfo.name"
									value="<s:property value='inputInfo.name'/>" /></td>
							</tr>
							<tr>
								<td style="text-align: right; width: 6%;">发票代码</td>
								<td style="text-align: left; width: 14%;"><input
									id="inputInfo.billId" class="tbl_query_text" type="text"
									name="inputInfo.billId"
									value="<s:property value='inputInfo.billId'/>" /></td>
								<td style="text-align: right; width: 6%;">发票号码</td>
								<td style="text-align: left; width: 14%;"><input
									id="inputInfo.billCode" class="tbl_query_text" type="text"
									name="inputInfo.billCode"
									value="<s:property value='inputInfo.billCode'/>" /></td>
								<td style="text-align: right; width: 6%;">发票状态</td>
								<td style="text-align: left; width: 14%;"><s:select
										id="inputInfo.rollOutStatus" name="inputInfo.rollOutStatus"
										list="#{'1':'已转出','0':'未转出'}" headerKey="" headerValue="全部"
										listKey='key' listValue='value' cssClass="tbl_query_text" /></td>
							</tr>
							<tr>
								<td colspan="4"><input type="button"
									class="tbl_query_button"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'"
									onclick="submitForm('listInvoiceInSurtax.action');"
									name="cmdQueryBtn" value="查询" id="cmdQueryBtn" /></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr align="left">
							<td style="text-align: left">
								<%-- <%if("Y".equals(request.getAttribute("mess"))){ %>
							<a href="#" onclick="cmdRollBackBtn()"> <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1032.png" />
									比例计算
							</a>
							<%} %>
							<a href="#" onclick="sgrollout()"> <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1023.png" />
									比例查看
							</a> --%> <a href="javascript:void();"
								onclick="submitForm('exportJX.action')"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
							</a> <input type="text"
								value="<%= request.getAttribute("Year-Moth")%>"
								readonly="readonly" size='4' style="text-align: right" />&nbsp;/月
								&nbsp; 转出税额合计：&nbsp;&nbsp;<input type="text"
								value="<%= request.getAttribute("rollOutAmtSum")%>"
								readonly="readonly" size='10' style="text-align: right" />
								&nbsp;/元
							<!--新增
								日期：2018-08-22
								作者：刘俊杰
								说明：将费控数据更新迁移到此页面
							 -->
							<!-- start -->
							<a href="javascript:void();"
								onclick="submitForm('dataUpdate.action')"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									费控数据更新
							</a>
							<!-- end -->
							
							</td>
						</tr>
					</table>
					<div>
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head" id="head">
								<th width="3%" style="text-align: center"><input
									id="CheckAll" style="width: 13px; height: 13px;"
									type="checkbox" onClick="checkAll(this,'billId')" /></th>
								<th width="4%" style="text-align: center" id="h2">报销机构</th>
								<th width="4%" style="text-align: center" id="h3">发票代码</th>
								<th width="4%" style="text-align: center" id="h4">发票号码</th>
								<th width="4%" style="text-align: center" id="h5">开票日期</th>
								<th width="3%" style="text-align: center" id="h6">金额</th>
								<th width="3%" style="text-align: center" id="h7">税率</th>
								<th width="3%" style="text-align: center" id="h8">税额</th>
								<th width="4%" style="text-align: center" id="h9">价税合计金额</th>
								<th width="4%" style="text-align: center" id="h10">发票类型</th>
								<th width="9%" style="text-align: center" id="h11">供应商名称</th>
								<th width="9%" style="text-align: center" id="h12">供应商纳税人识别号</th>
								<th width="6%" style="text-align: center" id="h13">认证结果</th>
								<th width="6%" style="text-align: center" id="h14">认证时间</th>
								<th width="6%" style="text-align: center" id="h15">转出状态</th>
								<th width="6%" style="text-align: center" id="h16">转出原因</th>
								<th width="6%" style="text-align: center" id="h17">转出税额</th>
								<th width="6%" style="text-align: center" id="h18">进项转出率</th>
								<!-- <th style="text-align: center">操作</th> -->
							</tr>
						</table>
					</div>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<!-- <thead style="position: fixed; z-index: 2;" hidden>
								<tr class="lessGrid head" id="head">
									<th width="3%" style="text-align: center"><input
										id="CheckAll" style="width: 13px; height: 13px;"
										type="checkbox" onClick="checkAll(this,'billId')" /></th>
									<th style="text-align: center" id="h2">报销机构</th>
									<th style="text-align: center" id="h3" width="3%">发票代码</th>
									<th style="text-align: center" id="h4" width="3%">发票号码</th>
									<th style="text-align: center" id="h5" width="3%">开票日期</th>
									<th style="text-align: center" id="h6" width="3%">金额</th>
									<th style="text-align: center" id="h7" width="3%">税率</th>
									<th style="text-align: center" id="h8" width="3%">税额</th>
									<th style="text-align: center" id="h9" width="3%">价税合计金额</th>
									<th style="text-align: center" id="h10" width="3%">发票类型</th>
									<th style="text-align: center" id="h11" width="3%">供应商名称</th>
									<th style="text-align: center" id="h12" width="3%">供应商纳税人识别号</th>
									<th style="text-align: center" id="h13" width="3%">认证结果</th>
									<th style="text-align: center" id="h14" width="3%">认证时间</th>
									<th style="text-align: center" id="h15" width="3%">转出状态</th>
									<th style="text-align: center" id="h16" width="3%">转出原因</th>
									<th style="text-align: center" id="h17" width="3%">转出税额</th>
									<th style="text-align: center" id="h18" width="3%">进项转出率</th>
									<th style="text-align: center">操作</th>
								</tr>
							</thead> -->
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">
								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td width="3%"><input type="checkbox"
										style="width: 13px; height: 13px;" name="billId"
										value="<s:property value='#iList.billId'/>-<s:property value='#iList.billCode' />" />
									</td>
									<td width="4%"><s:property value='#iList.shareInst' /></td>
									<td width="4%"><s:property value='#iList.billId' /></td>
									<td width="4%"><s:property value='#iList.billCode' /></td>
									<td width="4%"><s:property value="#iList.billDate" /></td>
									<td width="3%"><s:property
											value='@com.cjit.common.util.NumberUtils@format(amt," ", 2)' /></td>
									<td width="3%"><s:property
											value='@com.cjit.common.util.NumberUtils@format(taxRate," ", 2)' /></td>
									<td width="3%"><s:property
											value='@com.cjit.common.util.NumberUtils@format(tax," ", 2)' /></td>
									<td width="4%"><s:property
											value='@com.cjit.common.util.NumberUtils@format(sumAmt," ", 2)' /></td>
									<td width="4%"><input type="hidden" name="billType"
										value="<s:property value='billType' />" /> <s:property
											value='@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(billType)' />
									<td width="9%">
										<div class=ellipsis_div style="width: 120px;"
											title="<s:property value='#iList.name'/>">
											<s:property value='#iList.name' />
										</div>
									</td>
									<td width="9%"><s:property value='#iList.taxNo' /></td>
									<td width="6%"><s:property
											value='@com.cjit.vms.trans.util.JSPUtil@getValue(@com.cjit.vms.input.model.InputInfoUtil@billStatuMap,billStatu)' />
									</td>
									<td width="6%"><s:property value='#iList.dealNo' /></td>
									<td width="6%"><s:property
											value='@com.cjit.vms.trans.util.JSPUtil@getValue(@com.cjit.vms.input.model.InputInfoUtil@rollOutStatusMap,rollOutStatus)' />
									</td>
									<%-- <td width="6%"><s:property
											value='@com.cjit.vms.trans.util.JSPUtil@getValue(@com.cjit.vms.input.model.InputInfoUtil@remarkMap,remark)' />
									</td> --%>
									<td width="6%"><s:property
											value='#iList.remark' />
									</td>
									<td width="6%"><s:property
											value='@com.cjit.common.util.NumberUtils@format(rollOutAmt," ", 2)' /></td>
									<td width="6%"><s:property value='#iList.rollOutval' /></td>
								</tr>
							</s:iterator>
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
</body>
<script type="text/javascript">
	$(".cmdViewBt").click(function() {
		$o_bill_id = $(this).attr("rel");
		$("#o_bill_id").val($o_bill_id);
		submitForm("<c:out value='${webapp}'/>/viewInvoiceScanAuth.action");
	});

	$(function() {
		$(".edit_insurtax").click(
				function() {
					bill_id = $(this).attr("rel");
					OpenModalWindow(
							"<c:out value='${webapp}'/>/editInvoiceInSurtax.action?bill_id="
									+ bill_id, 800, 600, true);
				});
		//转出提交
		$("#cmdRollOutSubmitBtn").click(function() {
			$billIds = $("input[name='billId']:checked");
			if ($billIds.size() == 0) {
				alert("请选择您要转出的数据");
				return false;
			}
			submitForm('rollOutSubmitInvoiceInSurtax.action');
		});
	});
	function sgrollout(){
		submitForm("<c:out value='${webapp}'/>/sgrollout.action");
	}
	
</script>
</html>
