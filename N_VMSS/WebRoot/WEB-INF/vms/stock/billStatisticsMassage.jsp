<!-- 发票 统计数据详情页面 -->
<%@page import="com.cjit.vms.stock.util.StockUtil"%>
<%@page import="com.cjit.vms.trans.util.DataUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.cjit.vms.stock.entity.BillDistribution"%>
<%@page import="com.cjit.vms.trans.model.BillInfo"%>
<%@page import="com.cjit.vms.stock.entity.LostRecycle"%>
<%@page import="java.util.List"%>
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
							class="current_status_submenu1">库存管理</span> <span
							class="current_status_submenu">发票统计</span> <span
							class="current_status_submenu">数据详情</span>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr style="margin-top: 10px;">
							<td align="left"><input type="button"
								class="tbl_query_button"
								onMouseMove="this.className='tbl_query_button_on'"
								onMouseOut="this.className='tbl_query_button'"
								onclick="submitForm('billStatisticsList.action');"
								name="cmdDelbt" value="返回" id="cmdDelbt" />
							</th>
							</td>
						</tr>
					</table>
					<div id="whitebox">
						<div class="boxline">基本信息</div>
						<%  String JCXX=(String)request.getAttribute("JCXX") ;%>
						<table id="tbl_context" cellspacing="0" width="100%"
							align="center" cellpadding="0">
							<tr>
								<td align="left">
									<table id="contenttable" cellpadding="0" cellspacing="0"
										border="0" width="100%">
										<tr>
											<td width="10%" align="right"
												style="background-color: #F0F0F0; font-weight: bold; color: #727375;">发票代码:&nbsp;&nbsp;&nbsp;</td>
											<td width="15%"><%= JCXX.split("-")[1] %></td>
											<td width="10%" align="right"
												style="background-color: #F0F0F0; font-weight: bold; color: #727375;">发票起始号码:&nbsp;&nbsp;&nbsp;</td>
											<td width="15%"><%= JCXX.split("-")[2] %></td>
											<td width="10%" align="right"
												style="background-color: #F0F0F0; font-weight: bold; color: #727375;">发票截止号码:&nbsp;&nbsp;&nbsp;</td>
											<td width="15%"><%= JCXX.split("-")[3] %></td>
										</tr>
										<tr>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<div class="boxline">空白发票详情</div>
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align: center">申请机构名称</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票起始号码</th>
								<th style="text-align: center">发票截止号码</th>
								<th style="text-align: center">数据录入时间</th>
								<th style="text-align: center">发票类型</th>
								<th style="text-align: center">发票标记</th>
								<th style="text-align: center">状态</th>

							</tr>
							<%  List List=(List)request.getAttribute("List");
						if(List!=null&&List.size()>0 ){
							for(int i=0;i<List.size();i++){
								LostRecycle lRecycle=(LostRecycle)List.get(i);
								SimpleDateFormat sip=new SimpleDateFormat("yyyy-MM-dd");
								 String date=sip.format(lRecycle.getOperateDate());
								%>
							<tr>
								<td><%= lRecycle.getInstName() %></td>
								<td><%= lRecycle.getBillId() %></td>
								<td><%= lRecycle.getBillStartNo() %></td>
								<td><%= lRecycle.getBillEndNo() %></td>
								<td><%= date %></td>
								<td><%= DataUtil.getFapiaoTypeCH_M(lRecycle.getBillType()) %></td>
								<td><%= StockUtil.getflag(lRecycle.getFlag()) %></td>
								<td><%= StockUtil.getstate(lRecycle.getState()) %></td>
							</tr>
							<%
							}
						}else{%>
							<tr>
								<div>无数据</div>
							</tr>
							<%		}			
					%>
						</table>
						<div class="boxline">开具发票详情</div>
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">

							<tr class="lessGrid head">
								<th style="text-align: center">开票员</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票号码</th>
								<th style="text-align: center">发票状态</th>
								<th style="text-align: center">开票时间</th>
								<th style="text-align: center">发票类型</th>
							</tr>
							<% List billInfos=(List)request.getAttribute("kjList");
						if(billInfos!=null&&billInfos.size()>0){
							for(int i=0;i<billInfos.size();i++){
								BillInfo billInfo=(BillInfo)billInfos.get(i);
								%>
							<tr>
								<td><%= billInfo.getDrawer() %></td>
								<td><%= billInfo.getBillCode() %></td>
								<td><%= billInfo.getBillNo() %></td>
								<td><%= DataUtil.getDataStatusCH(billInfo.getDataStatus(), "BILL") %></td>
								<td><%= billInfo.getBillDate() %></td>
								<td><%= DataUtil.getFapiaoTypeName(billInfo.getFapiaoType()) %></td>
							</tr>
							<%
							}
						}else{ 
							%>
							<tr>
								<div>无数据</div>
							</tr>
							<%
						}
					
					%>
						</table>
						<div class="boxline">作废发票详情</div>
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">

							<tr class="lessGrid head">
								<th style="text-align: center">开票员</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票号码</th>
								<th style="text-align: center">发票状态</th>
								<th style="text-align: center">开票时间</th>
								<th style="text-align: center">发票类型</th>
							</tr>
							<% List kjzfList=(List)request.getAttribute("kjzfList");
						if(kjzfList!=null&&kjzfList.size()>0){
							for(int i=0;i<kjzfList.size();i++){
								BillInfo billInfo=(BillInfo)kjzfList.get(i);
								%>
							<tr>
								<td><%= billInfo.getDrawer() %></td>
								<td><%= billInfo.getBillCode() %></td>
								<td><%= billInfo.getBillNo() %></td>
								<td><%= DataUtil.getDataStatusCH(billInfo.getDataStatus(), "BILL") %></td>
								<td><%= billInfo.getBillDate() %></td>
								<td><%= DataUtil.getFapiaoTypeName(billInfo.getFapiaoType()) %></td>
							</tr>
							<%
							}
						}else{ 
							%>
							<tr>
								<div>无数据</div>
							</tr>
							<%
						}
					
					%>
						</table>
						<div class="boxline">红冲发票详情</div>
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">

							<tr class="lessGrid head">
								<th style="text-align: center">开票员</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票号码</th>
								<th style="text-align: center">发票状态</th>
								<th style="text-align: center">开票时间</th>
								<th style="text-align: center">发票类型</th>
							</tr>
							<% List kjhcList=(List)request.getAttribute("kjhcList");
						if(kjhcList!=null&&kjhcList.size()>0){
							for(int i=0;i<kjhcList.size();i++){
								BillInfo billInfo=(BillInfo)kjhcList.get(i);
								%>
							<tr>
								<td><%= billInfo.getDrawer() %></td>
								<td><%= billInfo.getBillCode() %></td>
								<td><%= billInfo.getBillNo() %></td>
								<td><%= DataUtil.getDataStatusCH(billInfo.getDataStatus(), "BILL") %></td>
								<td><%= billInfo.getBillDate() %></td>
								<td><%= DataUtil.getFapiaoTypeName(billInfo.getFapiaoType()) %></td>
							</tr>
							<%
							}
						}else{ 
							%>
							<tr>
								<div>无数据</div>
							</tr>
							<%
						}
					
					%>
						</table>
						<div class="boxline">打印发票详情</div>
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">

							<tr class="lessGrid head">
								<th style="text-align: center">开票员</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票号码</th>
								<th style="text-align: center">发票状态</th>
								<th style="text-align: center">开票时间</th>
								<th style="text-align: center">发票类型</th>
							</tr>
							<% List qtList=(List)request.getAttribute("qtList");
						if(qtList!=null&&qtList.size()>0){
							for(int i=0;i<qtList.size();i++){
								BillInfo billInfo=(BillInfo)qtList.get(i);
								%>
							<tr>
								<td><%= billInfo.getDrawer() %></td>
								<td><%= billInfo.getBillCode() %></td>
								<td><%= billInfo.getBillNo() %></td>
								<td><%= DataUtil.getDataStatusCH(billInfo.getDataStatus(), "BILL") %></td>
								<td><%= billInfo.getBillDate() %></td>
								<td><%= DataUtil.getFapiaoTypeName(billInfo.getFapiaoType()) %></td>
							</tr>
							<%
							}
						}else{ 
							%>
							<tr>
								<div>无数据</div>
							</tr>
							<%
						}
					
					%>
						</table>
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>