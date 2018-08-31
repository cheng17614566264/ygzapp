<!--file: <%=request.getRequestURI()%> -->
<%@page import="com.cjit.vms.trans.model.RedReceiptApplyInfo"%>
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/page/include.jsp"%>
<OBJECT id='DocCenterCltObj' width=100 height=100
	classid="clsid:1E25664C-EA53-4F39-8575-684737626D6F"
	style="display: none;"></OBJECT>
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
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/window.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/taxSelver/validatorTax.js"></script>
	
	
<!--引入 bw_service.js  cheng 20180829-->
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/billinterface/bw_servlet.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/billinterface/billInterfaceAjax.js"></script>

<script type="text/javascript">

	// [查询]按钮
	function search() {
		//document.forms[0].submit();
		submitAction(document.forms[0], "billRedIssueList.action");
		document.forms[0].action = "billRedIssueList.action";
	}
	function exportExcel() {
		submitAction(document.forms[0],
				"redReceiptBillIssueToExcel.action?type=redReceiptExcel");
		document.forms[0].action = "billRedIssueList.action";
	}
	// 开具按钮
	function issueRedBill() {
		if (checkChkBoxesSelected("selectBillIds")) {
			//税控盘号查询
			var taxDiskNo = "";
			try {
				taxDiskNo = DocCenterCltObj.FunGetPara('', 'taxDiskNo');
				var arr = new Array();
				arr = taxDiskNo.split("|");
				if (arr[0] == 0) {
					alert("请连接税控盘 and 以管理员身份运行");
					return false;
				}

				taxDiskNo = arr[1];
			} catch (e) {
				return alert("请安装税控插件!!");
			}

			var chkBoexes = document.getElementsByName("selectBillIds");
			var j = 0;
			var billId = "";
			for (i = 0; i < chkBoexes.length; i++) {
				if (chkBoexes[i].checked) {
					j++;
					billId = chkBoexes[i].value;
				}
				if (j > 1) {
					alert("请选择单条记录进行操作！");
					return false;
				}
			}

			alert("税控盘号：" + taxDiskNo);
			//获取选中的billId、数量、发票类型
			var billIds = document.getElementsByName("selectBillIds");
			var fapiaoTypes = document.getElementsByName("fapiaoTypes");
			var invalidInvoiceNum = document.Form1.invalidInvoiceNum.value;
			var fapiaoType = fapiaoTypes[0].value;
			var billId = billIds[0].value;

			//转action查询信息
			$.ajax({
				url : 'getRegisteredInfo.action',
				type : 'POST',
				async : false,
				data : {
					taxDiskNo : "1060",
					fapiaoType : fapiaoType
				},
				dataType : 'text',
				timeout : 1000,
				error : function() {
					return false;
				},
				success : function(result) {
					alert("查询发票库存发送数据：" + result);
					if (result == '') {
						alert("查询信息失败。");
						return false;
					} else if (result == 'lock') {
						alert("不能同时操作数据。");
						return false;
					} else if (result == 'registeredInfoError') {
						alert("获取税控盘注册码失败。");
						return false;
					} else if (result == 'taxPwdError') {
						alert("获取税控盘口令和证书口令失败。");
						return false;
					} else {
						//数控盘查询发票库存量surplusInvoice
						var invoiceStockNum = DocCenterCltObj.FunGetPara(
								result, 'surplusInvoice');
						alert("发票库存数量:" + invoiceStockNum);
						getIssueRedBillInfo(result, billId);
					}
				}
			});
			document.forms[0].action = "billRedIssueList.action";
		} else {
			alert("请选择票据记录！");
		}
	}

	function getIssueRedBillInfo(preStr, billId) {
		//获取选中的billId、数量、发票类型
		if (!confirm("是否确认开具？")) {
			return false;
		}

		//转action查询信息
		$.ajax({
			url : 'issueRedBill.action',
			type : 'POST',
			async : false,
			data : {
				preStr : preStr,
				billId : billId
			},
			dataType : 'text',
			timeout : 1000,
			error : function() {
				return false;
			},
			success : function(result) {
				if (result == '') {
					alert("查询信息失败。");
					return false;
				} else if (result == 'billItemError') {
					alert("无法查询发票对应的商品。");
					return false;
				} else if (result == 'billItemNum') {
					alert("发票对应的商品数量不能超过9个。");
					return false;
				} else {
					alert("红票开具发送数据：" + result);
					var issueRes = DocCenterCltObj.FunGetPara(result,
							'issueInvoice');
					alert("红票开具结果：" + issueRes);
					updateIssueRedBillResult(issueRes);
				}
			}
		});
		document.forms[0].action = "billRedIssueList.action";
	}

	function updateIssueRedBillResult(issueRes) {
		$.ajax({
			url : 'updateIssueRedBillResult.action',
			type : 'POST',
			async : false,
			data : {
				issueRes : issueRes
			},
			dataType : 'html',
			timeout : 1000,
			error : function() {
				return false;
			},
			success : function(result) {
				alert(result);
				submitAction(document.forms[0], "billRedIssueList.action");
				document.forms[0].action = "billRedIssueList.action";
			}
		});
	}
	
	var msg = '<s:property value="message" escape="false"/>';
	if (msg != null && msg != '') {
		alert(msg);
	}
	
	
	/* 程  20180829    引进国宝项目  重写该方法 调用核心 */

	 var billInterface = new BillInterface();
    // billInterface.init({});
	 
	function issueRedBillSX(){
		var billIds = document.getElementsByName("selectBillIds");
		var fapiaoType = $("#fapiao_Type").val();
		alert("发票类型~"+fapiaoType);
		alert("selectBillIds~"+billIds);
		var ids="";
		var flag = true;
		for(var i=0;i<billIds.length;i++){
			if (billIds[i].checked){
				ids = ids === "" ? billIds[i].value : ids + "," + billIds[i].value;
			}
		}
		if(ids==''){
		alert("请选择票据记录");
			return false;
		}
		alert("进。。。。。cheng。。");
		billInterface.createRedInvoice({billIds:ids,fapiaoType:fapiaoType});
//		billInterface.createRedInvoice({ids:ids,fapiaoType:fapiaoType,flag:flag});
		alert("出。。。。。cehng。。");
//		var j = 0;
//		var ids = "";
//		for (i = 0; i < billIds.length; i++) {
//			if (billIds[i].checked) {
//				j++;
//				ids = billIds[i].value;
//			}
//			if (j > 1) {
//				alert("请选择单条记录进行操作！");
//				return false;
//			}
//		}
//		if (j==0) {
//			alert("请选中一条记录进行操作！")
//		}
//			var flag = true;
//	 		billInterface.createRedBillissue({billIds:ids,fapiaoType:fapiaoType,flag:flag});
	 		if(this.flag) {
	 			submitAction(document.forms[0], "updateIssueRedBillResult.action");
	 		}
			submitAction(document.forms[0], "billRedIssueList.action");
//		}
	} 
	
	
	
	
	 /* 
	*三峡开具临时做法
	*//*
	function issueRedBillSX(){
		var chkBoexes = document.getElementsByName("selectBillIds");
		var j = 0;
		var billId = "";
		for (i = 0; i < chkBoexes.length; i++) {
			if (chkBoexes[i].checked) {
				j++;
				billId = chkBoexes[i].value;
			}
			if (j > 1) {
				alert("请选择单条记录进行操作！");
				return false;
			}
		}
		if (j==0) {
			alert("请选中一条记录进行操作！")
		}
		if(j==1){
			var billCode=$("#billCode").val().trim();
			var billNo=$("#billNo").val().trim() ;
			if (billCode.length==0&&billNo.length==0) {
				alert("请输入发票代码和发票号码");
				return false;
			}
			$.ajax({
				url : 'updateIssueRedBillResult.action',
				type : 'POST',
				async : false,
				data : {
					billId:billId,
					billCode:billCode,
					billNo:billNo
				},
				dataType : 'html',
				timeout : 1000,
				success : function(ajaxReturn) {
     				var returnJson = $.parseJSON(ajaxReturn);
     				if (returnJson.isNormal != null) {
     					alert(returnJson.message);
     				}
     				submitAction(document.forms[0], "billRedIssueList.action");
					document.forms[0].action = "billRedIssueList.action";
     			},
     			error : function(ajaxReturn) {
     				var returnJson = $.parseJSON(ajaxReturn);
     				if (returnJson.isNormal != null) {
     					alert(returnJson.message);
     				}
     				submitAction(document.forms[0], "billRedIssueList.action");
					document.forms[0].action = "billRedIssueList.action";
     			}
			});
		}
	}  */
</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" id="Form1">
		<%
			String currMonth = (String) request.getAttribute("currMonth");
			Long invalidInvoiceNum = (Long) request.getAttribute("invalidInvoiceNum");
		%>
		<input type="hidden" name="submitFlag" id="submitFlag" value="" /> <input
			type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>" />
		<input type="hidden" name="invalidInvoiceNum" id="invalidInvoiceNum"
			value="<%=invalidInvoiceNum%>" /> <input type="hidden" name="flag"
			value="true">
		<table id="tbl_main" cellpadding=0 cellspacing=0>
			<tr>
				<td align="left"><s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">红冲管理</span> <span
							class="current_status_submenu">红票开具</span>
					</div>
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
								value="<s:property value='redReceiptApplyInfo.insureId'/>" /></td>
							<td align="left">客户名称</td>
							<td><input type="text" class="tbl_query_text"
								name="redReceiptApplyInfo.customerName"
								value="<s:property value='redReceiptApplyInfo.customerName'/>" />
							</td>
						</tr>
						<tr>
							<td align="left">原始发票代码</td>
							<td><input type="text" class="tbl_query_text"
								name="redReceiptApplyInfo.oriBillCode" maxlength="10"
								value="<s:property value='redReceiptApplyInfo.oriBillCode'/>" />
							</td>
							<td align="left">原始发票号码</td>
							<td><input type="text" class="tbl_query_text"
								name="redReceiptApplyInfo.oriBillNo" maxlength="8"
								value="<s:property value='redReceiptApplyInfo.oriBillNo'/>" />
							</td>
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
						</tr>
						<tr>
							<s:if test='configCustomerFlag.equals("KBC")'>
								<td align="left">客户号</td>
								<td><input type="text" class="tbl_query_text"
									name="redReceiptApplyInfo.customerId"
									value="<s:property value='redReceiptApplyInfo.customerId'/>" />
								</td>
							</s:if>
							<td>开具类型</td>
							<td><s:select list="#{'':'全部','1':'单笔','2':'合并','3':'拆分'}"
									style="width:125px" name="redReceiptApplyInfo.issueType"
									label="abc" listKey="key" listValue="value" /></td>
							<td align="right" colspan="2" style="padding-left: 14.5%"><input
								type="button" class="tbl_query_button" value="查询"
								onMouseMove="this.className='tbl_query_button_on'"
								onMouseOut="this.className='tbl_query_button'" name="BtnView"
								id="BtnView" onclick="search()" /></td>
						</tr>
					</table>


					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left">
								<%-- <a href="#" name="BtnView" id="BtnView" onclick="exportExcel()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />导出
								</a> --%> <a href="#" name="BtnView" id="BtnView"
								onclick="issueRedBillSX()"><img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1010.png" />开具</a>
								<%-- <label>发票代码</label> <input type="text" id="billCode"
								class="tbl_query_text" name="redReceiptApplyInfo.billCode"
								maxlength="12"
								value="<s:property value='redReceiptApplyInfo.billCode'/>" />
								&nbsp;&nbsp; <label>发票号码</label> <input type="text" id="billNo"
								class="tbl_query_text" name="redReceiptApplyInfo.billNo"
								maxlength="8"
								value="<s:property value='redReceiptApplyInfo.billNo'/>" /> --%>
							</td>
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
								<th style="text-align: center">投保单号</th>
								<th style="text-align: center">保单号</th>
								<th style="text-align: center">原始发票代码</th>
								<th style="text-align: center">原始发票号码</th>
								<!-- <th style="text-align: center">开票日期</th> -->
								<th style="text-align: center">客户名称</th>
								<th style="text-align: center">合计金额</th>
								<th style="text-align: center">合计税额</th>
								<th style="text-align: center">价税合计</th>
								<!-- <th style="text-align: center">是否手工录入</th> -->
								<th style="text-align: center">开具类型</th>
								<th style="text-align: center">发票类型</th>
								<!-- <th style="text-align: center">状态</th> -->
								<th style="text-align: center">承保日期</th>
								<th style="text-align: center">操作</th>

							</tr>
							<%
								PaginationList paginationList = (PaginationList) request.getAttribute("paginationList");
								if (paginationList != null) {
									List billInfoList = paginationList.getRecordList();
									if (billInfoList != null && billInfoList.size() > 0) {
										for (int i = 0; i < billInfoList.size(); i++) {
											RedReceiptApplyInfo bill = (RedReceiptApplyInfo) billInfoList.get(i);
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
									name="selectBillIds" 
									value="<%=BeanUtils.getValue(bill, "billId")%>" /> <input
									type="hidden" name="fapiaoTypes" id="fapiao_Type"
									value="<%=bill.getFapiaoType()%>"></td>
								<td align="center"><%=(i + 1)%></td>
								<!-- 									metlife  begin -->
								<td align="center"><%=bill.getTtmprcno() == null ? "" : bill.getTtmprcno()%></td>
								<td align="center"><%=bill.getInsureId() == null ? "" : bill.getInsureId()%></td>
								<td align="center"><%=bill.getOriBillCode() == null ? "" : bill.getOriBillCode()%></td>
								<td align="center"><%=bill.getOriBillNo() == null ? "" : bill.getOriBillNo()%></td>
								<%-- <td align="center"><%=bill.getBillDate() == null ? "" : bill.getBillDate()%></td> --%>
								<td align="center"><%=bill.getCustomerName() == null ? "" : bill.getCustomerName()%></td>
								<td align="right"><%=NumberUtils.format(bill.getAmtSum(), "", 2)%></td>
								<td align="right"><%=NumberUtils.format(bill.getTaxAmtSum(), "", 2)%></td>
								<td align="right"><%=NumberUtils.format(bill.getSumAmt(), "", 2)%></td>
								<%-- <td align="center"><%=DataUtil.getIsHandiworkCH(bill.getIsHandiwork())%></td> --%>
								<td align="center"><%=DataUtil.getIssueTypeCH(bill.getIssueType())%></td>
								<td align="center"><%=DataUtil.getFapiaoTypeCH(bill.getFapiaoType()) %></td>
								<%-- <td align="center"><%=bill.getDatastatus()%></td> --%>
								<td align="center"><%=bill.getHissDte() == null?"":bill.getHissDte()%></td>
								<td><a
									href="viewIssueTrans.action?curPage=<s:property value='paginationList.currentPage'/>&billId=<%=BeanUtils.getValue(bill, "billId")%>">
										<img
										src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
										title="查看交易" style="border-width: 0px;" />
								</a> <a href="javascript:void(0);"
									onClick="OpenModalWindow('viewImgFromBillEdit.action?fromFlag=red&billId=<%=bill.getBillId()%>',1000,650,false)">
										<img
										src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png"
										title="查看票样" style="border-width: 0px;" />
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
					</div></td>
			</tr>
		</table>
	</form>

</body>
</html>