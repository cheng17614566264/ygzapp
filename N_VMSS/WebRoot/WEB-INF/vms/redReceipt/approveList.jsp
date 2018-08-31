<!--file: <%=request.getRequestURI()%> -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/comm.js"></script>
<script type="text/javascript">
	// [查询]按钮
	function search() {
		//document.forms[0].submit();
		submitAction(document.forms[0], "listRedReceiptApprove.action");
		document.forms[0].action = "listRedReceiptApprove.action";
	}
	function addResonSuccess() {
		submitAction(document.forms[0], "listRedReceiptApprove.action");
		document.forms[0].action = "listRedReceiptApprove.action";
	}
	function exportExcel() {
		submitAction(document.forms[0], "redReceiptBillToExcel.action");
		document.forms[0].action = "listRedReceiptApprove.action";
	}
	// 审核按钮
	function cancel(result) {
		if (checkChkBoxesSelected("selectBillIds")) {
			var billIds = document.Form1.selectBillIds;
			var billDates = document.Form1.selectBillDates;
			var canCancel = true;

			var chkBoexes = document.getElementsByName("selectBillIds");
			var j = 0;
			var billId = "";
			for (i = 0; i < chkBoexes.length; i++) {
				if (chkBoexes[i].checked) {
					j++;
					billId += "," + chkBoexes[i].value;
				}
			}
			if (result == 17) {
				if (!confirm("确定将选中票据进行审核处理？")) {
					return false;
				}
				submitAction(document.forms[0],
						"redReceiptApprove.action?billId=" + billId
								+ "&result=" + result);
				document.forms[0].action = "listRedReceiptApprove.action";
			} else {

				OpenModalWindowSubmit("toRedReceiptRefuse.action?billId="
						+ billId + "&result=" + result, 500, 250, true);
				//document.forms[0].action = "listRedReceiptApprove.action"; 
				//submitAction(document.forms[0], "toRedReceiptRefuse.action?billId="+billId +"&result="+result);
			}
		} else {
			alert("请选择审核记录！");
		}
	}
	function exportWord() {
		if (checkChkBoxesSelected("selectBillIds")) {
			var billIds = document.Form1.selectBillIds;
			var billDates = document.Form1.selectBillDates;
			var canCancel = true;

			var chkBoexes = document.getElementsByName("selectBillIds");
			var j = 0;
			var billId = "";

			for (i = 0; i < chkBoexes.length; i++) {
				if (chkBoexes[i].checked) {
					j++;
					billId = chkBoexes[i].value;
				}
				if (j > 1) {
					alert("请选择单条记录进行提交！");
					return false;
				}

			}
			var ticket = document.getElementById("bill" + billId).value;
			if (ticket == "0") {
				submitAction(document.forms[0], "exportToWord.action?billId="
						+ billId);
				document.forms[0].action = "listRedReceiptApprove.action";
			} else {
				alert("您所选票据为普票,没有申请信息！");
				return false;
			}

		} else {
			alert("请选择要导出的记录！");
		}
	}
	var msg = '<s:property value="message" escape="false"/>';
	if (msg != null && msg != '') {
		alert(msg);
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
				+ "px;center=1;scroll=0;help=0;status=0;");
		if (needReload && retData) {
			window.document.forms[0].submit();
			document.forms[0].action = "listRedReceiptApprove.action";
		}
	}
	
	//销项管理附件查看
	function viewImgFromInvoiceJF(){
		var selects = document.getElementsByName("selectBillIds");
		var count = 0 ;
		var index = 0 ;
		for(var i=0 ;i<selects.length ;i++){
			if(selects[i].checked==true){
				count++;
				index = i ;
			}
		}
		if(count>1){
			alert("请选择单条挑记录")
		}else if(count== 0){
			alert("请选择记录")
		}else{
			 var val ="N-"+document.getElementsByName("selects")[index].value ;
			 OpenModalWindowSubmit('viewImgFromBillOfFuJian.action?fileName='+val,1000,650,true);
		}
	}
</script>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listRedReceiptApprove.action"
		id="Form1">
		<input type="hidden" name="submitFlag" id="submitFlag" value="" /> <input
			type="hidden" name="currMonth" id="currMonth"
			value="<s:property value="currMonth"/>" />
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
							class="current_status_submenu">红冲审核</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%"
							border="0">
							<tr>
								<td>投保单号</td>
								<td><input type="text" class="tbl_query_text"
									name="redReceiptApplyInfo.ttmprcno"
									value="<s:property value='redReceiptApplyInfo.ttmprcno'/>" /></td>
								<td align="left">保单号</td>
								<td><input type="text" class="tbl_query_text"
									name="redReceiptApplyInfo.insureId"
									value="<s:property value='redReceiptApplyInfo.insureId'/>" />
								</td>
								<td align="left">开票日期</td>
								<td><input class="tbl_query_time" id="billBeginDate"
									type="text" name="redReceiptApplyInfo.billBeginDate"
									value="<s:property value='redReceiptApplyInfo.billBeginDate'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billEndDate\')}'})"
									size='11' "/> -- <input class="tbl_query_time"
									id="billEndDate" type="text"
									name="redReceiptApplyInfo.billEndDate"
									value="<s:property value='redReceiptApplyInfo.billEndDate'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billBeginDate\')}'})"
									size='11' "/></td>
							</tr>
							<tr>
								<td align="left">发票代码</td>
								<td><input type="text" class="tbl_query_text"
									name="redReceiptApplyInfo.billCode" maxlength="10"
									value="<s:property value='redReceiptApplyInfo.billCode'/>" />
								</td>
								<td align="left">发票号码</td>
								<td><input type="text" class="tbl_query_text"
									name="redReceiptApplyInfo.billNo" maxlength="8"
									value="<s:property value='redReceiptApplyInfo.billNo'/>" /></td>

								<td align="left">客户名称</td>
								<td><input type="text" class="tbl_query_text"
									name="redReceiptApplyInfo.customerName"
									value="<s:property value='redReceiptApplyInfo.customerName'/>" />
								</td>
							</tr>
							<tr>
								<td align="left">发票类型</td>
								<td><select id="redReceiptApplyInfo.fapiaoType"
									name="redReceiptApplyInfo.fapiaoType" style="width: 125px">
										<option value="">全部</option>
										<option value="0"
											<s:if test='redReceiptApplyInfo.fapiaoType=="0"'>selected</s:if>
											<s:else></s:else>>增值税专用发票</option>
										<option value="1"
											<s:if test='redReceiptApplyInfo.fapiaoType=="1"'>selected</s:if>
											<s:else></s:else>>增值税普通发票</option>
								</select></td>
								<s:if test='configCustomerFlag.equals("KBC")'>
									<td align="left">客户号</td>
									<td><input type="text" class="tbl_query_text"
										name="redReceiptApplyInfo.customerId"
										value="<s:property value='redReceiptApplyInfo.customerId'/>" />
									</td>
								</s:if>

								<td align="left">数据来源</td>
								<td><select id="dSource" name="redReceiptApplyInfo.dSource"
									style="width: 125px">
										<option value="">全部</option>
										<option value="SG">手工</option>
										<option value="HX">核心</option>
								</select></td>
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
							<td align="left"><a href="#" onclick="cancel(17)"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1017.png" />
									审核通过
							</a> <a href="#" onclick="cancel(1)"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1018.png" />
									审核打回
							</a> <a href="#" onclick="exportExcel()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
							</a> <a href="#" onclick="viewImgFromInvoiceJF()" name="cmdR"
								id="cmdR"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1027.png" />
									附件查看
							</a></td>
						</tr>
					</table>
					<div id="lessGridList1" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align: center"><input
									style="width: 13px; height: 13px;" id="CheckAll"
									type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
								</th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">投保单号</th>
								<th style="text-align: center">保单号</th>
								<th style="text-align: center">开票日期</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票号码</th>
								<th style="text-align: center">客户纳税人名称</th>
								<s:if test='configCustomerFlag.equals("KBC")'>
									<th style="text-align: center">客户号</th>
								</s:if>
								<th style="text-align: center">合计金额</th>
								<th style="text-align: center">合计税额</th>
								<th style="text-align: center">价税合计</th>
								<th style="text-align: center">冲帐金额</th>
								<th style="text-align: center">发票类型</th>
								<th style="text-align: center">状态</th>
								<th style="text-align: center">承保日期</th>
								<th style="text-align: center">数据来源</th>
								<th style="text-align: center">操作</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">
								<tr align="center"
									class="<s:if test='#stuts.odd'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td align="center" style="width: 30px;"><input
										style="width: 13px; height: 13px;" type="checkbox"
										name="selectBillIds" value="<s:property value="billId"/>" />
										<s:hidden name="selectBillDates" value="%{billDate}"></s:hidden>
										<input name="selects"
										value="<s:property value="insureId" />-<s:property value="ttmprcno" />-16"
										hidden /></td>
									<td align="center"><s:property value="#stuts.index+1" />
									</td>
									<td align="center"><s:property value="ttmprcno" /></td>
									<td align="center"><s:property value="insureId" /></td>
									<td align="center"><s:property
											value="@com.cjit.vms.trans.util.DataUtil@getDateFormat(billDate)" />
									</td>
									<td align="center"><s:property value="billCode" /></td>
									<td align="center"><s:property value="billNo" /></td>
									<td align="center"><s:property value="customerName" /></td>
									<td align="right"><s:property
											value='@com.cjit.common.util.NumberUtils@format(amtSum,"", 2)' />
									</td>
									<td align="right"><s:property
											value='@com.cjit.common.util.NumberUtils@format(taxAmtSum,"", 2)' />
									</td>
									<td align="right"><s:property
											value='@com.cjit.common.util.NumberUtils@format(sumAmt,"", 2)' />
									</td>
									<td align="right"><s:property
											value='@com.cjit.common.util.NumberUtils@format(reverseSumAmt*-1,"", 2)' />
									</td>
									<td align="center"><s:property
											value="@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(fapiaoType)" />
									</td>
									</td>
									<td align="center"><s:property value="datastatus" /> <s:property
											value="@com.cjit.vms.trans.util.DataUtil@getDataStatusCH(datastatus,'BILL')" />
									</td>
									<td align="center"><s:property value="hissDte" /></td>
									<td align="center"><s:property
											value="@com.cjit.vms.trans.util.DataUtil@getDsource(dSource)" />
									</td>
									<td align="center"><a
										href="viewTransList.action?curPage=<s:property value='paginationList.currentPage'/>&billId=<s:property value="reverseBillId"/>">
											<img
											src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
											title="查看交易" style="border-width: 0px;" />
									</a> <a href="javascript:void(0);"
										onClick="OpenModalWindowSubmit('viewImgFromBillEdit.action?fromFlag=red&billId=<s:property value="reverseBillId"/>',1000,650,false)">
											<img
											src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png"
											title="查看票样" style="border-width: 0px;" />
									</a></td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right"
						style="overflow: auto; width: 100%;">
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