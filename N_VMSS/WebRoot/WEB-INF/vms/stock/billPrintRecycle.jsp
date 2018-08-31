<!-- 已打印发票回收界面 -->
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

<script type="text/javascript">
	/**
 	* [查询]按钮
 	*/
	function submit() {
		document.forms[0].submit();
	}
   /**  
	*接收确认
	*/
	function recycleEnter(billId) {
		$.ajax({
			type: 'POST', 
			url: 'recycleEnter.action', 
			data: {billId:billId}, 
			dataType: 'json', 
			async: false, 
			success: function(ajaxReturn) {
				if (ajaxReturn.isNormal) {
					alert("回收确认成功");
				}
			},
			error:function(ajaxReturn){
				alert("回收确认失败");
			}
         });
		submit();
	}
   /**
   *导出票据使用统计信息
   */
   function exportPrintBill(){
	   var form = document.getElementById("Form1");
		form.action="exportPrintBill.action";
		form.submit();
		form.action="billPrintRecycleList.action";
   }
</script>
</head>
<body>
	<form name="Form1" method="post" action="billPrintRecycleList.action"
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
							class="current_status_submenu">已打印发票回收确认</span>
					</div> <!-- 查询框 -->
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%"
							border="0">
							<tr>
								<td style="text-align: right; width: 6%;">投保单号</td>
								<td style="text-align: left; width: 14%;"><input
									id="printBill.ttmprcNo" class="tbl_query_text" type="text"
									name="printBill.ttmprcNo"
									value="<s:property value='printBill.ttmprcNo'/>" /></td>
								<td style="text-align: right; width: 6%;">保单号</td>
								<td style="text-align: left; width: 14%;"><input
									id="printBill.insureId" class="tbl_query_text" type="text"
									name="printBill.insureId"
									value="<s:property value='printBill.insureId'/>" /></td>
								<td style="text-align: right; width: 6%;">开票日期</td>
								<td style="text-align: left; width: 20%;"><input
									class="tbl_query_time" id="printBill.billStartDate" type="text"
									name="printBill.billStartDate"
									value="<s:property value='printBill.billStartDate'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'printBill.billEndDate\')}'})"
									size='11' /> -- <input class="tbl_query_time"
									id="printBill.billEndDate" type="text"
									name="printBill.billEndDate"
									value="<s:property value='printBill.billEndDate'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'printBill.billStartDate\')}'})"
									size='11' /></td>
							</tr>
							<tr>
								<td style="text-align: right; width: 6%;">发票代码</td>
								<td style="text-align: left; width: 14%;"><input
									id="printBill.billCode" class="tbl_query_text" type="text"
									name="printBill.billCode"
									value="<s:property value='printBill.billCode'/>" /></td>
								<td style="text-align: right; width: 6%;">发票号码</td>
								<td style="text-align: left; width: 14%;"><input
									id="printBill.billNo" class="tbl_query_text" type="text"
									name="printBill.billNo"
									value="<s:property value='printBill.billNo'/>" /></td>
								<td style="text-align: right; width: 6%;">回收状态</td>
								<td style="text-align: left; width: 14%;"><s:select
										id="printBill.recycleStatus" name="printBill.recycleStatus"
										list="#{'1':'已回收','0':'未回收'}" headerKey='' headerValue="全部"
										listKey='key' listValue='value' cssClass="tbl_query_text" />
								</td>
							</tr>
							<tr>
								<td style="text-align: right; width: 6%;">发票类型</td>
								<td style="text-align: left; width: 14%;"><s:select
										id="printBill.billType" name="printBill.billType"
										list="#{'1':'增值税普通发票','0':'增值税专用发票'}" headerKey=''
										headerValue="全部" listKey='key' listValue='value'
										cssClass="tbl_query_text" /></td>

								<%-- <td style="text-align: right; width: 6%;">发票起始号段</td>
							<td style="text-align: left; width: 14%;">
								<input id="SbillStartNo" class="tbl_query_text" type="text" name="printBill.SbillStartNo" value="<s:property value='SbillStartNo'/>" size="11"/>
							</td>
							<td style="text-align: right; width: 6%;">发票截止号段</td>
							<td style="text-align: left; width: 14%;">
								<input id="SbillEndNo" class="tbl_query_text" type="text" name="printBill.SbillEndNo" value="<s:property value='SbillEndNo'/>" size="11"/>
							</td> --%>
								<td colspan="2" style="padding-left: 10.5%"><input
									type="button" onclick="submit()" class="tbl_query_button"
									value="查询" onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="btSubmit"
									id="btSubmit" style="margin-right: 30px" /></td>
							</tr>
							<!-- <tr>	
						</tr> -->
						</table>
					</div> <!-- 功能框 -->
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left"><a href="#" onclick="exportPrintBill()">
									<img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
							</a>
						</tr>
					</table> <!-- 主页面展示区 -->
					<div id="lessGridList4" style="overflow: auto">
						<!-- 主列表 -->
						<div id="rDiv" style="width: 0px; height: auto;">
							<table class="lessGrid" cellspacing="0" rules="all" border="0"
								cellpadding="0" style="border-collapse: collapse; width: 100%;">
								<tr class="lessGrid head">
									<th style="text-align: center"><input
										style="width: 13px; height: 13px;" id="CheckAll"
										type="checkbox" onclick="cbxselectall(this,'selectIds')" /></th>
									<th style="text-align: center">序号</th>
									<th style="text-align: center">投保单号</th>
									<th style="text-align: center">保单号</th>
									<th style="text-align: center">发票代码</th>
									<th style="text-align: center">发票起始号</th>
									<th style="text-align: center">发票截止号</th>
									<th style="text-align: center">发票号码</th>
									<th style="text-align: center">开票机构</th>
									<th style="text-align: center">开票日期</th>
									<th style="text-align: center">金额</th>
									<th style="text-align: center">税额</th>
									<th style="text-align: center">价税合计</th>
									<th style="text-align: center">客户名称</th>
									<th style="text-align: center">发票类型</th>
									<th style="text-align: center">发票状态</th>
									<th style="text-align: center">回收状态</th>
									<th style="text-align: center">操作</th>
								</tr>
								<%-- 	<s:iterator value="paginationList.recordList" id="iList" status="stuts"> --%>
								<s:iterator value="#request.list" id="iList" status="stuts">
									<tr align="center" id='<s:property value="billId" />'
										class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
										<td align="center"><input
											style="width: 13px; height: 13px;" class="billId"
											type="checkbox" name="selectIds"
											value='<s:property value="billId"/>'></td>
										<td align="center"><s:property value="#stuts.index+1" /></td>
										<td align="center"><s:property value="ttmprcNo" /></td>
										<td align="center"><s:property value="insureId" /></td>
										<td align="center"><s:property value="billCode" /></td>

										<td align="center"><s:property value="SbillStartNo" /></td>
										<td align="center"><s:property value="SbillEndNo" /></td>

										<td align="center"><s:property value="billNo" /></td>
										<td align="center"><s:property value="instId" /></td>
										<td align="center"><s:property value="billDate" /></td>
										<td align="center"><s:property
												value='@com.cjit.common.util.NumberUtils@format(amtSum," ", 2)' /></td>
										<td align="center"><s:property
												value='@com.cjit.common.util.NumberUtils@format(taxAmtSum," ", 2)' /></td>
										<td align="center"><s:property
												value='@com.cjit.common.util.NumberUtils@format(sumAmt," ", 2)' /></td>
										<td align="center"><s:property value="customerName" /></td>
										<td align="center"><s:property
												value="@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(billType)" /></td>
										<td align="center"><s:property
												value="@com.cjit.vms.trans.util.DataUtil@getDataStatusCH(datastatus,'BILL')" /></td>
										<td align="center"><s:property
												value="@com.cjit.vms.trans.util.JSPUtil@getValue(@com.cjit.vms.stock.util.StockUtil@PRINT_BILL_RECYCLE_MAP,recycleStatus)" /></td>

										<td align="center"><s:if
												test="recycleStatus==@com.cjit.vms.stock.util.StockUtil@PRINT_BILL_RECYCLE_NO">
												<a href="#"
													onclick="recycleEnter('<s:property value="billId"/>')">
													<img
													src="<c:out value="${bopTheme}"/>/themes/images/icons/confirm.png"
													title="回收确认" style="border-width: 0px;" />
												</a>
										&nbsp;
									</s:if></td>
									</tr>
								</s:iterator>
							</table>
						</div>
					</div> <input type="hidden" name="paginationList.showCount" value="false">
					<!-- 分页 -->
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
	<form name="Form2" method="post" action="billStatisticsList.action"
		id="Form2">
		<s:hidden id="message" value="%{message}"></s:hidden>
		<input name="fromFlag" type="hidden" value="list" /> <input
			name="business_code2" id="business_code2" type="hidden" value="" />
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
