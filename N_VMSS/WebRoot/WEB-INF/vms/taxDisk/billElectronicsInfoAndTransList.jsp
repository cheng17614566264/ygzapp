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
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
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
	function checkReverseTrans() {
		var reverseTransIdList = $("[name=selectBillIds]").closest("tr").find(
				".reverseTransId");
		var result = true;
		reverseTransIdList.each(function() {
			var isReversed = $(this).html();
			isReversed = isReversed.replace(/^(\s)*|(\s)*$/g, "");
			if (isReversed == "是") {
				var isNotChecked = $(this).closest("tr").find(
						"[name=selectBillIds]:checked").size() == 0 ? true
						: false;
				if (isNotChecked) {
					result = false;
					alert("冲账交易必须选择");
					return false;
				}

			}
		})
		return result;

	}
	//红票关联
	function commit() {
		var checkedList = $("[name=selectBillIds]:checked");
		if (checkedList.size() == 0) {
			alert("请选择数据");
			return false;
		}

		if (!checkReverseTrans()) {
			return false;
		}

		var checkedRadioSize = $("[name=specialTicket.level1Option]:checked")
				.size();
		var fapiaoTypeHD = $("#fapiaoTypeHD").val();
		if ("0" == fapiaoTypeHD && 0 == checkedRadioSize) {
			alert("请选择发票状态");
			return false;
		}
		var billId = document.getElementById("billId").value;
		
		
		
		
		
		
	}

	function cancel() {
		window.location
				.href("listElectroniceRedBillSelect.action?fromFlag=menu&paginationList.showCount=FALSE");
	}
	function search() {
		var cherNum = document.getElementById("cherNums");
		submitAction(document.forms[0],
				"billElectronicsInfoAndTransList.action?cherNum="
						+ cherNum.value);
	}
	$(function() {
		var val = $("#fapiao").val();
		if ("0" == val) {
			$("#list1").show();
		}
	})
</script>
<style type="text/css">
.style1 {
	width: 242px;
}

#list1 {
	padding: 0 5 0 5;
}

#list1 span {
	line-height: 35px;
	font-weight: bold;
	color: #252525;
	white-space: nowrap;
	cursor: default;
}

.lessGridListPri {
	padding: 0 7px 0 7px;
	margin-bottom: 20px;
	cursor: default;
	border: #DDDDDD solid 1px;
	background: #FFF;
	overflow: auto;
	width: 98%;
	cursor: default;
}
</style>
</head>
<body onkeydow="enterkey(event)">
	<!-- <form name="Form1" id="Form1" method="post" action="pageEvent()" > -->
	<form name="Form1" id="Form1" method="post"
		action="billElectronicsInfoAndTransList.action">
		<input type="hidden" id="type" name="type"
			value="<s:property value='#request.type' />" /> <input type="hidden"
			id="ids" name="ids" value="<s:property value='#request.ids' />" /> <input
			type="hidden" value="<%=request.getParameter("cherNum")%>"
			id="cherNums" />
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition"><s:component
						template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">红冲管理</span> <span
							class="current_status_submenu">红冲申请</span> <span
							class="current_status_submenu">蓝票关联</span>
					</div> </br>
					<div class="lessGridListPri">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%"
							border="0">
							<tr>

								<!-- 发票号码 -->
								<td align="center">发票代码</td>
								<td><input style="width: 135px" type="text"
									class="tbl_query_text" name="billInfo.billCode"
									value="<s:property value='billInfo.billCode'/>" maxlength="10" /></td>
								<!-- 发票代码 -->
								<td align="center">发票号码</td>
								<td><input type="text" style="width: 125px"
									class="tbl_query_text" name="billInfo.billNo"
									value="<s:property value='billInfo.billNo'/>" maxlength="8" /></td>
								<td align="left">起止日期</td>
								<td><input class="tbl_query_time"
									id="billInfo.applyBeginDate" type="text"
									name="billInfo.applyBeginDate"
									value="<s:property value='billInfo.applyBeginDate'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billInfo.applyBeginDate\')}'})"
									size='11' /> -- <input class="tbl_query_time"
									id="billInfo.applyEndDate" type="text"
									name="billInfo.applyEndDate"
									value="<s:property value='billInfo.applyEndDate'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billInfo.applyEndDate\')}'})"
									size='11' /></td>

							</tr>
							<tr>
								<td style="text-align: center; width: 6%;">开票机构</td>
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
								<td style="text-align: center; width: 6%;">状态</td>
								<td style="text-align: left; width: 14%;"><select
									name="billInfo.dataStatus" style="width: 135px"
									id="data_Status">
										<option value="1"
											<s:if test='billInfo.dataStatus=="1"'>selected</s:if>
											<s:else></s:else>>未红冲</option>
										<option value="2"
											<s:if test='billInfo.dataStatus=="2"'>selected</s:if>
											<s:else></s:else>>已红冲</option>
										<option value=""
											<s:if test='billInfo.dataStatus==""'>selected</s:if>
											<s:else></s:else>>全部</option>
								</select></td>
								<!-- 开票日期 -->
								<td align="center">开票日期</td>
								<td><input class="tbl_query_time" id="billInfo.billDate"
									type="text" name="billInfo.billDate"
									value="<s:property value='billInfo.billDate'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billInfo.billDate\')}'})"
									size='11' /></td>
								<td></td>
								<td align="left"><input type="button"
									class="tbl_query_button" value="查询"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="BtnView"
									id="BtnView" onclick="search()" /></td>



							</tr>
						</table>
					</div>
					<div id="lessGridList5"
						style="overflow: auto; width: 100%; height: 300px;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align: center"><input
									style="width: 13px; height: 13px;" id="CheckAll"
									type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
								</th>
								<th style="text-align: center">序号</th>
								<!-- 申请开票日期 -->
								<th style="text-align: center">申请开票日期</th>
								<!-- 开票日期 -->
								<th style="text-align: center">开票日期</th>
								<!-- 开票机构 -->
								<th style="text-align: center">开票机构</th>
								<!-- 录单机构 -->
								<th style="text-align: center">录单机构</th>
								<!--客户名称-->
								<th style="text-align: center">客户名称</th>
								<!--发票代码-->
								<th style="text-align: center">发票代码</th>
								<!--发票号码-->
								<th style="text-align: center">发票号码</th>
								<th style="text-align: center">合计金额</th>
								<th style="text-align: center">合计税额</th>
								<th style="text-align: center">价税合计</th>
								<th style="text-align: center">发票类型</th>
								<th style="text-align: center">状态</th>
								<th style="text-align: center">已红冲金额</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">
								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td align="center" style="width: 30px;"><input
										type="checkbox" name="selectBillIds"
										value="<s:property value="transId"/>" /></td>
									<td><s:property value="#stuts.index+1" /></td>

									<td><s:property value="applyDate" /></td>

									<td><s:property
											value="@com.cjit.vms.trans.util.DataUtil@getDateFormat(billDate)" /></td>


									<td><s:property value="instCode" /></td>


									<td><s:property value="instFrom" /></td>

									<td><s:property value="customerName" /></td>

									<td><s:property value="billCode" /></td>

									<td><s:property value="billNo" /></td>

									<td><s:property
											value='@com.cjit.common.util.NumberUtils@format(amtSum,"", 2)' />
									</td>
									<td><s:property
											value='@com.cjit.common.util.NumberUtils@format(taxAmtSum,"", 2)' />
									</td>
									<td><s:property
											value='@com.cjit.common.util.NumberUtils@format(sumAmt,"", 2)' />
									</td>

									<!-- 发票类型 -->
									<td align="center" id="fptype"><s:property
											value="@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(fapiaoType)" />
									</td>
									<!-- 状态-->
									<td align="center"><s:property
											value="@com.cjit.vms.trans.util.DataUtil@getDataStatusCH(dataStatus,'BILL')" />
									</td>

									<td><s:property value="redAmt" /></td>

								</tr>
							</s:iterator>
						</table>
					</div> <input type="hidden" value="<s:property value="flag"/>" id="flag"
					name="flag" />
					<div id="anpBoud" align="Right"
						style="overflow: auto; width: 100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="right"><input type="button"
								class="tbl_query_button" value="关联"
								onMouseMove="this.className='tbl_query_button_on'"
								onMouseOut="this.className='tbl_query_button'" name="BtnView"
								id="BtnView" onclick="commit()" /> <input type="button"
								class="tbl_query_button" value="返回"
								onMouseMove="this.className='tbl_query_button_on'"
								onMouseOut="this.className='tbl_query_button'" name="BtnView"
								id="BtnView" onclick="cancel()" /></td>
						</tr>
					</table></td>
			</tr>
		</table>
	</form>
</body>
</html>