<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.electronics.model.InstUtil"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<OBJECT id='ocxObj' width=100 height=100
	classid="clsid:1E25664C-EA53-4F39-8575-684737626D6F"
	style="display: none;"></OBJECT>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/editview.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
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
<!-- 税控服务器 -->
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/taxSelver/currentbill.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/taxSelver/validatorTax.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/taxSelver/compareCurrentBill.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/taxSelver/billCancel.js"></script>
<!-- 税控软件 -->
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/billinterface/billInterfaceAjax.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/billinterface/bw_disk.js"></script>

<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/vms.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/billinterface/<%=(String) request.getAttribute("jspParam")%>.js"></script>

<script type="text/javascript">
	/* var billInterface = new BillInterface();
	billInterface.init({}); */
	// [查询]按钮  
	function submitForm(actionUrl) {
		submitAction(document.forms[0], actionUrl);
		document.forms[0].action = "findElectronics.action?paginationList.showCount="
				+ "false";
	}
	/*导出按钮*/
	function exportwl() {
		if (checkChkBoxesSelected("selectBillIds")) {
			transInfo = document.getElementsByClassName("selectTransIds");
			var stringAppend = "";
			for (info in transInfo) {
				if (transInfo[info].checked) {
					stringAppend = transInfo[info].value + "_" + stringAppend;
				}
			}
			submitForm("exportElectronics.action?number=" + stringAppend);
		} else {
			alert("请至少选中一个来导出");
		}
	}
	/*电子发票开具*/
	function electronicsIssue() {
		if (checkChkBoxesSelected("selectBillIds")) {
			cks = true;
			object = document.getElementsByClassName("selectTransIds");
			var i=0;
			for (i=0; i<object.length; i++) {
				if (object[i].checked) {
					if (parseFloat($("#vehicles").val()) < 0) {
						cks = false;
						alert("负数交易，无法进行开具！");
						return;
					}
				}
			}
			if (cks == true) {
				var transIds = "";
				var fapiaoType = object[0].getAttribute("fapiaoType");
				var j=0;
				/* for (j=0; j<object.length; j++) {
					if (object[j].checked) {
						transIds = object[j].getAttribute("rm") + "/"
								+ transIds;
					}
				} */
				for (i = 0; i < object.length; i++) { 
					   if (object[i].checked){ 
					   transIds = object[i].getAttribute("rm") + "/" 
					   + transIds;  
					   }
				}
				//生成票据信息,执行电子发票开具
				$.ajax({
					url : "transToElectronicsBill.action",
					type : "POST",
					async : true,
					data : {
						"fapiaoType" : fapiaoType,
						"transIds" : transIds
					},
					dataType : "text",
					success : function(ajaxReturn) {
						var ajaxReturns = $.parseJSON(ajaxReturn);
						alert(ajaxReturns.message);
						//刷新页面
						submitAction( document.forms[0], "findElectronics.action?fromFlag=menu&paginationList.showCount=FALSE");
					},
					error : function() {
						alert("false");
						//刷新页面
						submitAction( document.forms[0], "findElectronics.action?fromFlag=menu&paginationList.showCount=FALSE");
					}
				});
			}
		} else {
			alert("请至少选中一个信息来进行开具操作");
		}
	}

	/*电子发票红冲*/
	function RedelectronicsIssue() {
		if (checkChkBoxesSelected("selectBillIds")) {
	/* 		cks = true;
			object = document.getElementsByClassName("selectTransIds");
			for (ckecks in object) {
				if (object[ckecks].checked) {
					if (parseFloat($("#vehicles").val()) > 0) {
						cks = false;
						alert("正数交易，无法进行红冲！");
						return;
					}
				}
			}
			if (cks == true) {
				var transIds = "";
				var fapiaoType = object[0].getAttribute("fapiaoType");
				for (cke in object) {
					if (object[cke].checked) {
						transIds = object[cke].getAttribute("rm") + "/"
								+ transIds;
					}
				}
				*/
				/* 生成红色票据保存 */ 

				/* 执行红冲*/
				/* submitAction(
						document.forms[0],
						"listElectroniceRedToBill.action?fromFlag=menu&paginationList.showCount=FALSE"); */
			/* } */
			
			//新增
			//日期：2018-09-06
			//说明：更新状态为ELECTRONICS_REDBILL_STATUS_302, 生成红冲票据
			object = document.getElementsByClassName("selectTransIds");
			var fapiaoType = object[0].getAttribute("fapiaoType");
			var transIds = "";
			var i = 0;
			for (i=0; i<object.length; i++) {
				if (object[i].checked) {
					transIds = object[i].getAttribute("rm") + "/"
							+ transIds;
				}
			}
			$.ajax({
				url : "listElectroniceRedToBill.action",
				type : "POST",
				async : true,
				data : {
					"fapiaoType" : fapiaoType,
					"transIds" : transIds
				},
				dataType : "text",
				success : function(ajaxReturn) {
					var ajaxReturns = $.parseJSON(ajaxReturn);
					alert(ajaxReturns.message);
					//刷新页面
					submitAction( document.forms[0], "findElectronics.action?fromFlag=menu&paginationList.showCount=FALSE");
				},
				error : function() {
					alert("false");
					//刷新页面
					submitAction( document.forms[0], "findElectronics.action?fromFlag=menu&paginationList.showCount=FALSE");
				}
			});
		} else {
			alert("请至少选中一个信息来进行冲操作");
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
</script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$("#CheckAll")
								.click(
										function() {
											CheckButton = document
													.getElementsByClassName("selectTransIds");
											if ($("#CheckAll").is(':checked')) {
												for (var i = 0; i < CheckButton.length; i++) {
													if (CheckButton[i].type == "checkbox") {
														CheckButton[i].checked = true;
													}
												}
											} else if ($("#CheckAll").is(
													':checked') == false) {
												for (var i = 0; i < CheckButton.length; i++) {
													if (CheckButton[i].type == "checkbox") {
														CheckButton[i].checked = false;
													}
												}
											}

										});
						$(".selectTransIds")
								.click(
										function() {

											obj = document
													.getElementsByClassName("selectTransIds");
											num = 0.0;
											nums = 1;
											for (k in obj) {
												if (obj[k].checked) {

													var cherNum = $("input[name='selectBillIds']:checked");
													if (cherNum.attr("cherNum") == obj[k]
															.getAttribute("cherNum")) {
														num = parseFloat(obj[k]
																.getAttribute(
																		"amt")
																.replace(",",
																		""))
																+ parseFloat(num);
														if (num >= 10000) {
															if (nums == 1) {
																if (confirm("交易已经超过最大限额10000，将自动拆分，是否继续？")) {
																	nums = 0;
																} else {
																	ass(obj[k]
																			.getAttribute("rm"));
																}
															}

														} else {
															nums = 1;
														}
													} else {
														alert("非相同保单下的交易信息不能进行开具操作。");
														ass(obj[k]
																.getAttribute("rm"));
														cherNum = 0;
													}
												}
											}
											$("#vehicles").val(num);
										});
						function ass(ss) {
							$("input[rm=" + ss + "]").attr("checked", false);
							$("input[rm=" + ss + "]").attr("checked");

						}

					});
</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="findElectronics.action"
		id="Form1">
		<input type="hidden" name="taxParam" id="taxParam"
			value="<s:property value='taxParam'/>" />
		<%
			String currMonth = (String) request.getAttribute("currMonth");
			Long invalidInvoiceNum = (Long) request
					.getAttribute("invalidInvoiceNum");
		%>
		<input type="hidden" name="submitFlag" id="submitFlag" value="" /> <input
			type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>" />
		<input type="hidden" name="invalidInvoiceNum" id="invalidInvoiceNum"
			value="<%=invalidInvoiceNum%>" />
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition"><s:component
						template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">电票管理</span> <span
							class="current_status_submenu">电子发票手动开具</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%"
							border="0">
							<tr>
								<td style="text-align: right; width: 6%;">客户名称</td>
								<td style="text-align: left; width: 14%;"><input
									type="text" class="tbl_query_text"
									name="transInfo.customerName"
									value="<s:property value='transInfo.customerName'/>"
									maxlength="100" /></td>
								<td style="text-align: right; width: 6%;">保单号</td>
								<td style="text-align: left; width: 14%;"><input
									class="tbl_query_text" type="text" name="transInfo.cherNum"
									value="<s:property value='transInfo.cherNum'/>" maxlength="20" /></td>

								<td style="text-align: right; width: 6%;">批单号</td>
								<td style="text-align: left; width: 14%;"><input
									class="tbl_query_text" type="text" name="transInfo.batchNo"
									value="<s:property value='transInfo.batchNo'/>" maxlength="20" /></td>
							</tr>
							<tr>
								<td style="text-align: right; width: 8%;">起止日期</td>
								<td style="text-align: left; width: 20%;"><input
									class="tbl_query_time" id="transBeginDate" type="text"
									name="transInfo.transBeginDate"
									value="<s:property value='transInfo.transBeginDate'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'transBeginDate\')}'})"
									size='11' /> -- <input class="tbl_query_time"
									id="transEndDate" type="text" name="transInfo.transEndDate"
									value="<s:property value='transInfo.transEndDate'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'transEndDate\')}'})"
									size='11' /></td>
								<td style="text-align: right; width: 6%;">录单机构</td>
								<td style="text-align: left; width: 14%;"><select
									name="transInfo.instCode" style="width: 135px">
										<option value=""
											<s:if test='transInfo.instCode==null'>selected</s:if>
											<s:if test='transInfo.instCode==""'>selected</s:if>
											<s:else></s:else>>全部</option>
										<%
											List list = (List) request.getAttribute("instcodes");
											if (list != null) {
												for (int i = 0; i < list.size(); i++) {
													InstUtil inst = (InstUtil) list.get(i);
										%>
										<option value="<%=inst.getInstCode()%>"
											<s:if test='transInfo.instCode=="<%=inst.getInstCode()%>"'>selected</s:if>
											<s:else></s:else>><%=inst.getInstCode() + ": " + inst.getInstName()%></option>
										<%
											}
											}
										%>
								</select></td>
								<td style="text-align: right; width: 6%;">状态</td>
								<td style="text-align: left; width: 14%;"><select
									name="transInfo.dataStatus" style="width: 135px">
										<option value="201"
											<s:if test='transInfo.dataStatus=="1"'>selected</s:if>
											<s:else></s:else>>自动开具失败</option>
										<option value="202"
											<s:if test='transInfo.dataStatus=="2"'>selected</s:if>
											<s:else></s:else>>手动开具失败</option>
										<option value="203"
											<s:if test='transInfo.dataStatus=="7"'>selected</s:if>
											<s:else></s:else>>保全增人</option>
										<option value="204"
											<s:if test='transInfo.dataStatus=="8"'>selected</s:if>
											<s:else></s:else>>保全减人</option>
										<option value="205"
											<s:if test='transInfo.dataStatus=="9"'>selected</s:if>
											<s:else></s:else>>保全增费</option>
										<option value="206"
											<s:if test='transInfo.dataStatus=="10"'>selected</s:if>
											<s:else></s:else>>保全减费</option>
										<option value=""
											<s:if test='transInfo.dataStatus==""'>selected</s:if>
											<s:else></s:else>>全部</option>
								</select></td>
								<td style="text-align: left; width: 14%;"><input
									type="button" class="tbl_query_button" value="查询"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="BtnView"
									id="BtnView" onclick="submitForm('findElectronics.action')" /></td>
							</tr>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left"><a href="#" name="BtnView" id="BtnView"
								onclick="electronicsIssue()"><img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1002.png" />电子发票开具</a>
								<a href="#" name="BtnView" id="BtnView"
								onclick="RedelectronicsIssue()"><img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1002.png" />电子发票红冲</a>
								<a href="#" name="BtnView" id="BtnView" class="export"
								onclick="exportwl()"><img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />导出</a>

								合并金额显示： <input type="text" class="tbl_query_text" id="vehicles"
								maxlength="100" readonly="readonly" /></td>
						</tr>
					</table>

					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<div id="rDiv" style="width: 0px; height: auto;">
							<table class="lessGrid" cellspacing="0" rules="all" border="0"
								cellpadding="0" style="border-collapse: collapse; width: 100%;">
								<tr class="lessGrid head">
									<th style="text-align: center"><input
										style="width: 13px; height: 13px;" id="CheckAll"
										type="checkbox" /></th>
									<th style="text-align: center">序号</th>
									<th style="text-align: center">录单机构</th>
									<th style="text-align: center">投保单号</th>
									<th style="text-align: center">保单号</th>
									<th style="text-align: center">批单号</th>
									<th style="text-align: center">交易日期</th>
									<th style="text-align: center">交易类型</th>
									<th style="text-align: center">客户名称</th>
									<th style="text-align: center">交易金额</th>
									<th style="text-align: center">税率</th>
									<th style="text-align: center">税额</th>
									<th style="text-align: center">价税合计</th>
									<th style="text-align: center">未开票金额</th>
									<th style="text-align: center">累计未开票金额</th>
									<th style="text-align: center">交易状态</th>
									<th style="text-align: center">承保日期</th>
									<th style="text-align: center">操作</th>
								</tr>
								<%
									PaginationList paginationList = (PaginationList) request
											.getAttribute("paginationList");
									if (paginationList != null) {
										List transInfoList = paginationList.getRecordList();
										if (transInfoList != null && transInfoList.size() > 0) {
											for (int i = 0; i < transInfoList.size(); i++) {
												TransInfo bill = (TransInfo) transInfoList.get(i);
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
										name="selectBillIds" class="selectTransIds"
										cherNum="<%=bill.getCherNum()%>"
										value="<%=bill.getCherNum() + "-" + bill.getTtmpRcno()%>"
										amt=<%=NumberUtils.format(bill.getAmt(), "", 2)%>
										rm="<%=bill.getTransId()%>"
										fapiaoType="<%=bill.getFapiaoType()%>" /> <input
										type="hidden" name="fapiaoTypes"
										value="<%=bill.getFapiaoType()%>">
									<td align="center" id='<%=bill.getBillId() + "Count"%>'><%=i + 1%>
									</td>
									<!--录单机构-->
									<td align="center"><%=bill.getInstCode() == null ? "" : bill
								.getInstCode()%></td>
									<!--投保单号-->
									<td align="center"><%=bill.getTtmpRcno() == null ? "" : bill
								.getTtmpRcno()%></td>
									<!--保单号-->
									<td align="center"><%=bill.getCherNum() == null ? "" : bill
								.getCherNum()%></td>
									<!--批单号-->
									<td align="center"><%=bill.getBatchNo() == null ? "" : bill
								.getBatchNo()%></td>
									<!-- 交易日期 -->
									<td align="center"><%=bill.getTransDate() == null ? "" : bill
								.getTransDate()%></td>

									<!--交易类型-->
									<td align="center"><%=bill.getTransTypeName() == null ? "" : bill
								.getTransTypeName()%></td>
									<!-- 客户名称 -->
									<td align="center"><%=bill.getCustomerName() == null ? "" : bill
								.getCustomerName()%></td>
									<!--交易金额  -->
									<td align="center"><%=NumberUtils.format(bill.getAmt(), "", 2)%>
									</td>
									<!--税率  -->
									<td align="center"><%=NumberUtils.format(bill.getTaxRate(), "", 2)%>
									</td>
									<!--税额  -->
									<td align="center"><%=NumberUtils.format(bill.getTaxAmt(), "", 2)%>
									</td>
									<!-- 价税合计 -->
									<td align="center"><%=NumberUtils.format(bill.getReverseAmt(), "",
								2)%></td>
									<!-- 未开票金额 -->
									<td align="center"><%=NumberUtils.format(bill.getBalance(), "", 2)%>
										<!--累计未开票金额 -->
									<td align="center"><%=NumberUtils.format(bill.getWkze(), "", 2)%></td>
									<!--交易状态 -->
									<td align="center"><%=bill.getDataStatusCH() == null ? "" : bill
								.getDataStatusCH()%></td>
									<!--承保日期 -->
									<td align="center"><%=bill.getHissDte() == null ? "" : bill
								.getHissDte()%></td>

									<td align="center"><a href="javascript:void(0);"
										onClick="OpenModalWindowSubmit('billElectronicsCancelReason.action?transId=<%=bill.getTransId()%>&fapiaoType=<%=bill.getFapiaoType()%>',500,300,true)">
											<img
											src="<c:out value="${bopTheme}"/>/themes/images/icons/info.png"
											title="查看失败原因" style="border-width: 0px;" />
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