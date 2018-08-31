<!-- 发票遗失回收作废界面 -->
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
	src="<%=bopTheme%>/js/search.js"></script>
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
	function jsEnter(disId) {
		$.ajax({
			type: 'POST', 
			url: 'jsEnter.action', 
			data: {disId:disId}, 
			dataType: 'json', 
			async: false, 
			success: function(ajaxReturn) {
				if (ajaxReturn.isNormal) {
					alert("接收确认成功");
				}
			},
			error:function(ajaxReturn){
				alert("接收确认失败");
			}
         });
	}
</script>
</head>
<body>
	<form name="Form1" method="post"
		action="billStatisticsList.action?massage=track" id="Form1">
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
							class="current_status_submenu">发票遗失回收作废申请</span>
					</div> <!-- 查询框 -->
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%"
							border="0">
							<tr>
								<td style="text-align: right; width: 6%;">报税机构</td>
								<td style="text-align: left; width: 14%;"><input
									type="text" class="tbl_query_text" id="inst_Name"
									name="billDistribution.instId" value='' onclick="setOrg(this);"
									readonly="readonly"></td>
								<td style="text-align: right; width: 6%;">分发日期</td>
								<td style="text-align: left; width: 20%;"><input
									class="tbl_query_time" id="billDistribution.disStartDate"
									type="text" name="billDistribution.disStartDate"
									value="<s:property value='billDistribution.disStartDate'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billDistribution.disEndDate\')}'})"
									size='11' /> -- <input class="tbl_query_time"
									id="billDistribution.disEndDate" type="text"
									name="billDistribution.disEndDate"
									value="<s:property value='billDistribution.disEndDate'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billDistribution.disStartDate\')}'})"
									size='11' /></td>
								<%-- <td style="text-align: right; width: 6%;">税控钥匙编号</td>
							<td style="text-align: left; width: 14%;">
								 <input id="billDistribution.taxNo" class="tbl_query_text" type="text" name="billDistribution.taxNo" value="<s:property value='billDistribution.taxNo'/>" />
							</td> --%>
								<td style="text-align: right; width: 6%;">开票员名称</td>
								<td style="text-align: left; width: 14%;"><input
									id="billDistribution.kpyName" class="tbl_query_text"
									type="text" name="billDistribution.kpyName"
									value="<s:property value='billDistribution.kpyName'/>" /></td>
							</tr>
							<tr>
								<td style="text-align: right; width: 6%;">发票状态</td>
								<td style="text-align: left; width: 14%;"><s:select
										id="billDistribution.jsEnter" name="billDistribution.jsEnter"
										list="#{'1':'已确认','0':'未确认'}" headerKey='' headerValue="全部"
										listKey='key' listValue='value' cssClass="tbl_query_text" />
								</td>
								<td style="text-align: right; width: 6%;">发票类型</td>
								<td style="text-align: left; width: 14%;"><s:select
										id="billDistribution.billType"
										name="billDistribution.billType"
										list="#{'1':'增值税普通发票','0':'增值税专用发票'}" headerKey=''
										headerValue="全部" listKey='key' listValue='value'
										cssClass="tbl_query_text" /></td>
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
						<tr align="left">
							<td style="text-align: left"><a href="#"
								onclick="submitForm('#.action');" name="cmdExcel" id="cmdExcel"><img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />导出</a>
							</td>
							<%-- 
						<td style="text-align:left">
							<a href="lostRecycleAdd.action"  name="cmdExcel" id="cmdExcel"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>发票操作</a>
						</td>
						--%>
						</tr>
					</table> <!-- 主列表 信息展示-->
					<div id="lessGridList1" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align: center"><input
									id="CheckAll" style="width: 13px; height: 13px;"
									type="checkbox" onClick="checkAll()" /></th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">税控钥匙编号</th>
								<!-- <th style="text-align: center">机构</th> -->
								<th style="text-align: center">报税机构</th>
								<th style="text-align: center">分发日期</th>
								<th style="text-align: center">开票员编号</th>
								<th style="text-align: center">开票员名称</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票起始号码</th>
								<th style="text-align: center">发票截止号码</th>
								<th style="text-align: center">发票类型</th>
								<th style="text-align: center">操作状态</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">
								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td><input type="checkbox"
										style="width: 13px; height: 13px;" name="id"
										value="<s:property value='id'/>" /></td>
									<td align="center"><s:property value='#stuts.count' /></td>
									<td align="center"><s:property value="taxNo" /></td>
									<%-- <td align="center"><s:property value='instId'/></td> --%>
									<td align="center"><s:property value="instName" /></td>
									<td align="center"><s:date name="disDate"
											format="yyyy-MM-dd"></s:date></td>
									<td align="center"><s:property value='kpyId' /></td>
									<td align="center"><s:property value='kpyName' /></td>
									<td align="center"><s:property value='billId' /></td>
									<td align="center"><s:property value='billStartNo' /></td>
									<td align="center"><s:property value='billEndNo' /></td>
									<td align="center"><s:property
											value='@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(billType)' /></td>
									<td align="center"><s:if
											test='jsEnter==@com.cjit.vms.stock.util.StockUtil@JS_ENTENER_NO'>
								      未确认，不可操作
								<%-- 

										<a href="#" onclick="jsEnter('<s:property value="disId"/>')">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/confirm.png" title="接收确认" style="border-width: 0px;" />
										</a>
										--%>
										</s:if> <s:else>
											<a
												href="lostRecycleAdd.action?disId=<s:property value="disId"/>&billType=<s:property value="billType" />
																					&billid=<s:property value="billId" />&billStartNo=<s:property value="billStartNo" />
																								&billEndNo=<s:property value="billEndNo" />">
												<img
												src="<c:out value="${bopTheme}"/>/themes/images/icons/export.png"
												title="回收,遗失,作废操作">
											</a>
										</s:else></td>
								</tr>
							</s:iterator>

						</table>
					</div>
					<div id="anpBoud" align="Right" style="width: 100%;">
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