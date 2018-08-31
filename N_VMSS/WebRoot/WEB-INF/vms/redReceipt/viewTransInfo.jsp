<!-- file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.TransInfo"
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
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<script>
	function revokeToList() {
		submitAction(document.forms[0],
				"listRedReceiptApply.action?fromViewFlg=first");
	}
</script>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listBillTransInfo.action"
		id="Form1">
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">红冲管理</span> <span
							class="current_status_submenu">红冲申请</span> <span
							class="current_status_submenu">查看交易</span>
					</div> <input type="hidden" id="type" name="billId"
					value="<s:property value='#request.billId' />" />
					<div id="lessGridList" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align: center">序号</th>
								<th style="text-align: center">交易时间</th>
								<th style="text-align: center">客户名称</th>
								<th style="text-align: center">保单号</th>
								<th style="text-align: center">投保单号</th>
								<th style="text-align: center">数据来源</th>
								<th style="text-align: center">交易类型</th>
								<th style="text-align: center">收入</th>
								<th style="text-align: center">税率</th>
								<th style="text-align: center">税额</th>
								<th style="text-align: center">价税合计</th>
								<th style="text-align: center">发票类型</th>
								<th style="text-align: center">承保日期</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">
								<tr align="center"
									class="<s:if test='#stuts.odd'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td align="center"><s:property value="%{#stuts.index+1}" />
									</td>
									<td align="center"><s:property value="transDate" /></td>
									<td align="left"><s:property value="customerName" /></td>
									<td align="left"><s:property value="cherNum" /></td>
									<td align="left"><s:property value="ttmpRcno" /></td>
									<td align="left"><s:property
											value="@com.cjit.vms.trans.util.DataUtil@getDsource(dsouRce)" />
									</td>
									<td align="center"><s:property value="transTypeName" /></td>
									<td align="right"><s:property
											value='@com.cjit.common.util.NumberUtils@format(income,"", 2)' />
									</td>
									<td align="right"><s:property
											value='@com.cjit.common.util.NumberUtils@format(taxRate,"", 2)' />
									</td>
									<!-- 税额 -->
									<td align="right"><s:property
											value='@com.cjit.common.util.NumberUtils@format(taxAmt,"", 2)' />
									</td>
									<!-- 价税合计 -->
									<td align="right"><s:property
											value='@com.cjit.common.util.NumberUtils@format(taxAmt+income,"", 2)' />
									</td>
									<td align="center"><s:property
											value="@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(fapiaoType)" />
									</td>
									<td align="left"><s:property value="hissDte" /></td>
								</tr>
							</s:iterator>
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
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left"><input type="button"
								class="tbl_query_button" value="返回"
								onMouseMove="this.className='tbl_query_button_on'"
								onMouseOut="this.className='tbl_query_button'" name="btCancel"
								id="btCancel" onclick="revokeToList();" /></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>