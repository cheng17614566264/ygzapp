<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<link href="<%=webapp%>/page/common.css" type="text/css"
	rel="stylesheet">

<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>进项税管理</title>

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
	function submitForm(actionUrl){
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
	}	
</script>
</head>
<body>
	<form id="main"
		action="<c:out value='${webapp}'/>/listParamInSurtax.action"
		method="post">
		<input type="hidden" name="o_bill_id"
			value="<s:property value='o_bill_id'/>" /> <input type="hidden"
			name="billDate" value="<s:property value='billDate'/>" /> <input
			type="hidden" name="customerName"
			value="<s:property value='customerName'/>" /> <input type="hidden"
			name="datastatus" value="<s:property value='datastatus'/>" /> <input
			type="hidden" name="instId" value="<s:property value='instId'/>" /> <input
			type="hidden" name="billCode" value="<s:property value='billCode'/>" />
		<input type="hidden" name="billNo"
			value="<s:property value='billNo'/>" /> <input type="hidden"
			name="fapiaoType" value="<s:property value='fapiaoType'/>" /> <input
			type="hidden" name="identifyDate"
			value="<s:property value='identifyDate'/>" /> <input type="hidden"
			name="paginationList.currentPage"
			value="<s:property value='currentPage'/>" />
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td align="left">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">进项税管理</span> <span
							class="current_status_submenu">进项管理</span> <span
							class="current_status_submenu">扫描认证</span> <span
							class="current_status_submenu">进项详细</span>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr style="margin-top: 10px;">
							<td align="left"><input type="button"
								class="tbl_query_button"
								onMouseMove="this.className='tbl_query_button_on'"
								onMouseOut="this.className='tbl_query_button'"
								onclick="submitForm('listInvoiceScanAuth.action');"
								name="cmdDelbt" value="返回" id="cmdDelbt" />
							</th>
							</td>
						</tr>
					</table>
					<div id="whitebox">
						<div class="boxline">基本信息</div>
						<table id="tbl_context" cellspacing="0" width="100%"
							align="center" cellpadding="0">
							<tr>
								<td align="left">
									<table id="contenttable" cellpadding="0" cellspacing="0"
										border="0" width="100%">
										<tr>
											<td width="10%" align="right"
												style="background-color: #F0F0F0; font-weight: bold; color: #727375;">发票代码:&nbsp;&nbsp;&nbsp;</td>
											<td width="15%"><s:property
													value='inputInfoView.billId' /></td>
											<td width="10%" align="right"
												style="background-color: #F0F0F0; font-weight: bold; color: #727375;">发票号码:&nbsp;&nbsp;&nbsp;</td>
											<td><s:property value='inputInfoView.billCode' /></td>
											<td width="10%" align="right"
												style="background-color: #F0F0F0; font-weight: bold; color: #727375;">开票日期:&nbsp;&nbsp;&nbsp;</td>
											<td width="15%"><s:property
													value='inputInfoView.billDate' /></td>
										</tr>
										<tr>
											<td width="10%" align="right"
												style="background-color: #F0F0F0; font-weight: bold; color: #727375;">供应商名称:&nbsp;&nbsp;&nbsp;</td>
											<td width="15%"><s:property value='inputInfoView.name' />
											</td>
											<td width="10%" align="right"
												style="background-color: #F0F0F0; font-weight: bold; color: #727375;">供应商纳税人识别号:&nbsp;&nbsp;&nbsp;</td>
											<td><s:property value='inputInfoView.taxNo' /></td>
											<td width="10%" align="right"
												style="background-color: #F0F0F0; font-weight: bold; color: #727375;">供应商电话:&nbsp;&nbsp;&nbsp;</td>
											<td width="15%"><s:property
													value='inputInfoView.customerTel' /></td>
										</tr>
										<tr>
											<td width="10%" align="right"
												style="background-color: #F0F0F0; font-weight: bold; color: #727375;">供应商地址:&nbsp;&nbsp;&nbsp;</td>
											<td><s:property value='inputInfoView.customerAdd' /></td>
											<td width="10%" align="right"
												style="background-color: #F0F0F0; font-weight: bold; color: #727375;">供应商银行账号:&nbsp;&nbsp;&nbsp;</td>
											<td width="15%"><s:property
													value='inputInfoView.bankNo' /></td>
											<td width="10%" align="right"
												style="background-color: #F0F0F0; font-weight: bold; color: #727375;">
												认证状态 :&nbsp;&nbsp;&nbsp;</td>
											<td><s:if test='inputInfoView.billStatu=="1"'>已认证</s:if>
												<s:else>未认证</s:else></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<div class="boxline">票据交易数据</div>
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align: center">报销机构</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票号码</th>
								<th style="text-align: center">金额</th>
								<th style="text-align: center">税率</th>
								<th style="text-align: center">税额</th>
								<th style="text-align: center">价税合计金额</th>
								<th style="text-align: center">是否抵免</th>
								<th style="text-align: center">用途</th>

							</tr>
							<s:iterator value="inputInfoView.billDetailList" id="iList"
								status="stuts">
								<tr align="center">
									<td align="center"><s:property value="shareInst" /></td>
									<td align="center"><s:property
											value="inputInfoView.billId" /></td>
									<td align="center"><s:property
											value="inputInfoView.billCode" /></td>
									<td align="center"><s:property
											value='@com.cjit.common.util.NumberUtils@format(amt," ", 2)' /></td>
									<td align="center"><s:property
											value='@com.cjit.common.util.NumberUtils@format(taxRate," ", 2)' /></td>
									<td align="center"><s:property
											value='@com.cjit.common.util.NumberUtils@format(tax," ", 2)' /></td>
									<td align="center"><s:property
											value='@com.cjit.common.util.NumberUtils@format(sumAmt," ", 2)' /></td>
									<td align="center"><s:property
											value="@com.cjit.vms.trans.util.JSPUtil@getValue(@com.cjit.vms.input.model.InputInfoUtil@isCreditMap,isCredit)" /></td>
									<td align="center"><s:property
											value="@com.cjit.vms.trans.util.JSPUtil@getValue(@com.cjit.vms.input.model.InputInfoUtil@purposeMap,purpose)" /></td>
								</tr>
							</s:iterator>
						</table>
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>