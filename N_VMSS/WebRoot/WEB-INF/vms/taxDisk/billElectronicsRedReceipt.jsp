<!--file: <%=request.getRequestURI()%> -->
<%@page language="java" import="java.util.*"
	import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.electronics.model.InstUtil"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/page/include.jsp"%>
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
	src="<%=webapp%>/page/js/lhb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/comm.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<script type="text/javascript">
	// [查询]按钮
	function search() {
		submitAction(document.forms[0], 
		   'listElectroniceRedBillSelect.action?fromFlag=chaxun'); 
	}
	//导出
	function exportExcel() {
		/* billIdCode */
		if (checkChkBoxesSelected("selectBillIds")) {

			BillInfos = document.getElementsByClassName("selectBillIds");
			var stringAppend = "";
			for (info in BillInfos) {
				if (BillInfos[info].checked) {
					stringAppend = BillInfos[info].value + "/" + stringAppend;
				}
			}
			alert(stringAppend);
			submitAction(document.forms[0],
					"redElectronicsReceiptBillToExcel.action?billId="
							+ stringAppend);
			document.forms[0].action = "listElectroniceRedBillSelect.action";
		} else {
			alert("请至少选中一个来导出");
		}
	}
	// [红冲]按钮
	function redReceipt() {
		if (checkChkBoxesSelected("selectBillIds")) {
			var billIds = document.Form1.selectBillIds;
			var billDates = document.Form1.selectBillDates;
			var canCancel = true;
			var chkBoexes = document.getElementsByName("selectBillIds");
			var j = 0;
			var BlueBillId = "";

			var index = 0;
			for (i = 0; i < chkBoexes.length; i++) {
				if (chkBoexes[i].checked) {
					j++;
					BlueBillId = chkBoexes[i].getAttribute("billId") + "/"
							+ BlueBillId;
					which = i;
				}
			}
			alert(BlueBillId)
			if (confirm("确定将选中票据进行红冲申请？")) {
				/* 执行红冲 */
				$.ajax({
					url : "redEclectronicsbillRedCancel.action",
					type : "POST",
					async : true,
					data : {
						"billIds" : BlueBillId
					},
					dataType : "JSON",
					success : function(ajaxReturn) {
						var ajaxReturns = $.parseJSON(ajaxReturn);
						alert(ajaxReturns.message);
						search();
					},
					error : function() {
						alert("红冲申请失败！");
					}
				});
			} else {
				return false;
			}
		} else {
			alert("请选择交易记录！");
		}
	}
	function OpenModalWindowSubmit(newURL, width, height, needReload) {
		var retData = false;
		if (typeof (width) == 'undefined') {
			width = screen.width * 0.9;
		}
		if (typeof (height) == 'undefined') {
			height = screen.height * 0.9;
		}
		if (typeof (needReload) == 'undefined') {
			needReload = false;
		}
		retData = showModalDialog(newURL, window, "dialogWidth:" + width
				+ "px;dialogHeight:" + height
				+ "px;center=1;scroll=1;help=0;status=0;");
		if (needReload && retData) {
			window.document.forms[0].submit();
		}
	}
	var msg = '<s:property value="message" escape="false"/>';
	if (msg != null && msg != '') {
		alert(msg);
	}
</script>
<script type="text/javascript">
	/* 类型为蓝字票的展示*/
	$(document).ready(function() {
		if ($("#data_Status").val() == "302") {
			$("#hideButton").show();
		} else {
			$("#hideButton").hide();
		}
		/* 	$("#data_Status").change(function (){
		 if($("#data_Status").val()=="2"){
		 submitAction(document.forms[0], "listElectroniceRedBillSelect.action");
		 $("#hideButton").show();
		 }else{
		 $("#hideButton").hide();
		 }
		 }); */
	})
</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post"
		action="listElectroniceRedBillSelect.action" id="Form1"
		enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition"><s:component
						template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">开票申请</span> <span
							class="current_status_submenu">发票红冲</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%"
							border="0">
							<tr>
								<!--  客户名称 -->
								<td align="left">客户名称</td>
								<td><input type="text" class="tbl_query_text"
									name="billInfo.customerName"
									value="<s:property value='billInfo.customerName'/>" /></td>
								<!-- 发票号码 -->
								<td align="left">发票代码</td>
								<td><input style="width: 135px" type="text"
									class="tbl_query_text" name="billInfo.billCode"
									value="<s:property value='billInfo.billCode'/>" maxlength="10" /></td>
								<!-- 发票代码 -->
								<td align="left">发票号码</td>
								<td><input type="text" style="width: 125px"
									class="tbl_query_text" name="billInfo.billNo"
									value="<s:property value='billInfo.billNo'/>" maxlength="8" /></td>
								<!--  保单号 -->
								<td align="left">保单号</td>
								<td><input type="text" class="tbl_query_text"
									name="billInfo.cherNum"
									value="<s:property value='billInfo.cherNum'/>" /></td>
							</tr>
							<tr>
								<!-- 起止日期 -->
								<td align="left">起止日期</td>
								<td><input class="tbl_query_time"
									id="billInfo.applyBeginDate" type="text"
									name="billInfo.applyBeginDate"
									value="<s:property value='billInfo.applyBeginDate'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billInfo.applyBeginDate\')}'})"
									size='11' "/> -- <input class="tbl_query_time"
									id="billInfo.applyEndDate" type="text"
									name="billInfo.applyEndDate"
									value="<s:property value='billInfo.applyEndDate'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billInfo.applyEndDate\')}'})"
									size='11' "/></td>

								<!-- 批单号 -->
								<td align="left">批单号</td>
								<td><input type="text" class="tbl_query_text"
									name="billInfo.batchNo"
									value="<s:property value='billInfo.batchNo'/>" /></td>

								<!-- 开票机构 -->


								<td style="text-align: right; width: 6%;">录单机构</td>
								<td style="text-align: left; width: 14%;"><select
									name="billInfo.instCode" style="width: 135px">
										<option value=""
											<s:if test='billInfo.instCode==null'>selected</s:if>
											<s:if test='billInfo.instCode==""'>selected</s:if>
											<s:else></s:else>>全部</option>
										<%
											List list = (List) request.getAttribute("instcodes");
											if (list != null) {
												for (int i = 0; i < list.size(); i++) {
													InstUtil inst = (InstUtil) list.get(i);
										%>
										<option value="<%=inst.getInstCode()%>"
											<s:if test='billInfo.instCode=="<%=inst.getInstCode()%>"'>selected</s:if>
											<s:else></s:else>><%=inst.getInstCode() + ": " + inst.getInstName()%></option>
										<%
											}
											}
										%>
								</select></td>
								<!-- 状态 -->
								<td style="text-align: right; width: 6%;">状态</td>
								<td style="text-align: left; width: 14%;"><select
									name="billInfo.dataStatus" style="width: 135px"
									id="data_Status">
										<option value="302"
											<s:if test='billInfo.dataStatus=="302"'>selected</s:if>
											<s:else></s:else>>未开具红票</option>
										<option value="301"
											<s:if test='billInfo.dataStatus=="301"'>selected</s:if>
											<s:else></s:else>>已开具蓝票</option>
										<option value="303"
											<s:if test='billInfo.dataStatus=="303"'>selected</s:if>
											<s:else></s:else>>审核中</option>
										<option value="304"
											<s:if test='billInfo.dataStatus=="304"'>selected</s:if>
											<s:else></s:else>>审核退回</option>
										<option value="305"
											<s:if test='billInfo.dataStatus=="305"'>selected</s:if>
											<s:else></s:else>>合并红冲开票中</option>
										<option value="306"
											<s:if test='billInfo.dataStatus=="306"'>selected</s:if>
											<s:else></s:else>>单笔红冲开票中</option>
										<option value=""
											<s:if test='billInfo.dataStatus==""'>selected</s:if>
											<s:else></s:else>>全部</option>
								</select></td>


							</tr>
							<tr>
								<!-- 发票类型 -->
								<td align="left">发票类型</td>
								<td><select id="billInfo.fapiaoType"
									name="billInfo.fapiaoType" style="width: 125px">
										<option value="">全部</option>
										<option value="0"
											<s:if test='billInfo.fapiaoType=="0"'>selected</s:if>
											<s:else></s:else>>增值税专用发票</option>
										<option value="1"
											<s:if test='billInfo.fapiaoType=="1"'>selected</s:if>
											<s:else></s:else>>增值税普通发票</option>
										<option value="2"
											<s:if test='billInfo.fapiaoType=="2"'>selected</s:if>
											<s:else></s:else>>增值税电子发票</option>
								</select></td>
								<!-- 开票日期 -->
								<td align="left">开票日期</td>
								<td><input class="tbl_query_time" id="billInfo.billDate"
									type="text" name="billInfo.billDate"
									value="<s:property value='billInfo.billDate'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billInfo.billDate\')}'})"
									size='11' /></td>
								<td></td>
								<td align="right"><input type="button"
									class="tbl_query_button" value="查询"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="BtnView"
									id="BtnView" onclick="search()" /></td>

							</tr>

						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left"><a href="#" onclick="redReceipt()"
								id="hideButton" display:none> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1033.png" />
									电子发票红冲
							</a> <a href="#" onclick="exportExcel()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
							</a></td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align: center"><input
									style="width: 13px; height: 13px;" id="CheckAll"
									type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
								</th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">申请开票日期</th>
								<th style="text-align: center">开票日期</th>
								<th style="text-align: center">开票机构</th>
								<th style="text-align: center">录单机构</th>
								<th style="text-align: center">客户名称</th>
								<th style="text-align: center">保单号</th>
								<th style="text-align: center">批单号</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票号码</th>
								<th style="text-align: center">合计金额</th>
								<th style="text-align: center">合计税额</th>
								<th style="text-align: center">价税合计</th>
								<th style="text-align: center">发票类型</th>
								<th style="text-align: center">状态</th>
								<th style="text-align: center">操作</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">
								<tr align="center"
									class="<s:if test='#stuts.odd'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<!-- 序号 -->
									<td align="center" style="width: 30px;"><input
										type="checkbox" class="selectBillIds" name="selectBillIds"
										billId="<s:property value='billId' />"
										billIdCode="<s:property value='billCode' />-<s:property value='billNo' />"
										value="<s:property value='billCode' />-<s:property value='billNo' />-<s:property value='billId' />" />
										<s:hidden name="selectDataStatus" value="%{datastatus}"></s:hidden>
										<s:hidden name="selectBillDate" value="%{billDate}"></s:hidden>
										<input hidden name="cherNumhidden"
										value="<s:property value='insureId' />-<s:property value='ttmprcno'/>" />
									</td>
									<!-- 序号 -->
									<td align="center"><s:property value="#stuts.index+1" />
									</td>
									<!-- 申请开票日期 -->
									<td align="center"><s:property value="applyDate" /></td>
									<!-- 开票日期 -->
									<td align="center"><s:property
											value="@com.cjit.vms.trans.util.DataUtil@getDateFormat(billDate)" />
									</td>
									<!-- 开票机构 -->
									<td align="center"><s:property value="instCode" /></td>
									<!-- 录单机构 -->

									<td align="center"><s:property value="instFrom" /></td>

									<!-- 客户名称 -->
									<td align="center"><s:property value="customerName" /></td>
									<!-- 保单号 -->
									<td align="center"><s:property value="cherNum" /></td>
									<!-- 批单号 -->
									<td align="center"><s:property value="batchNo" /></td>

									<!-- 发票代码 -->
									<td align="center"><s:property value="billCode" /></td>

									<!-- 发票号码 -->
									<td align="center"><s:property value="billNo" /></td>

									<!-- 合计金额 -->
									<td align="right"><s:property
											value='@com.cjit.common.util.NumberUtils@format(amtSum,"", 2)' />
									</td>
									<!-- 合计税额 -->
									<td align="right"><s:property
											value='@com.cjit.common.util.NumberUtils@format(taxAmtSum,"", 2)' />
									</td>
									<!--价税合计  -->
									<td align="right"><s:property
											value='@com.cjit.common.util.NumberUtils@format(sumAmt,"", 2)' />
									</td>
									<!-- 发票类型 -->
									<td align="center" id="fptype"><s:property
											value="@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(fapiaoType)" />
									<!-- <input id="fp" -->
									<s:property value='fapiaoType' /> <input id="fp"
										value="<s:property value='fapiaoType' />-<s:property value='billId'/>"
										hidden /></td>
									<!-- 状态-->
									<td align="center"><s:property
											value="@com.cjit.vms.trans.util.DataUtil@getDataStatusCH(dataStatus,'BILL')" />
										<s:if test='dataStatus=="302"'>未开具红票</s:if> <s:elseif 
										   test='dataStatus=="304"'>审核退回</s:elseif> <s:elseif 
										   test='dataStatus=="303"'>审核中</s:elseif> <s:elseif 
										   test='dataStatus=="305"'>合并红冲开票中</s:elseif> <s:elseif 
										   test='dataStatus=="306"'>单笔红冲开票中</s:elseif> <s:elseif 
										   test='dataStatus=="301"'>已开具蓝票</s:elseif> 
									</td> 
									<!--操作-->
									<td align="center"><a
										href="listElectronicsBillTransInfo.action?fromFlag=first&billId=<s:property value='billId'/>">
											<img
											src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
											title="查看交易" style="border-width: 0px;" />
									</a> 
									
									<s:if test='dataStatus=="304"'>
												<a href="javascript:void(0);"     
													onClick="OpenModalWindowSubmit('billElectronicsCancelReason_shdh.action?billId=<s:property value='billId'/>&fapiaoType=<s:property value='fapiaoType' />',500,300,true)">
												<img
												src="<c:out value="${bopTheme}"/>/themes/images/icons/info.png"
												title="查看驳回原因" style="border-width: 0px;" />
												</a>
										</s:if>
									
									
									<s:if test="fapiaoType==0">
											<a
												href="billElectronicsInfoAndTransList.action?fromFlag=menu&cherNum=<s:property value='cherNum'/>">
												<img
												src="<c:out value="${bopTheme}"/>/themes/images/icons/info.png"
												title="查看蓝票" style="border-width: 0px;" />
											</a>
										</s:if></td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right"
						style="overflow: auto; width: 100%;">
						<input type="hidden" name="paginationList.showCount" value="false">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
					</div></td>
			</tr>
		</table>
	</form>
</body>
</html>