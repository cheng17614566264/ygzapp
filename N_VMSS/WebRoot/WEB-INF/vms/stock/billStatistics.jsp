<!-- 发票统计界面 -->
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
		submit();
	}
   /**
   *导出票据使用统计信息
   */
   function exportBillDistribution(){
	   var form = document.getElementById("Form1");
		form.action="exportBillDistribution.action";
		form.submit();
		form.action="billInventoryList.action";
   }
   
   function goSelect(url){
	   alert(url)
	  // document.forms[0].action(url).submit();
	  document.forms[0].action(url);
	  submit();
	  
   }
</script>
</head>
<body>
	<form name="Form1" method="post"
		action="billStatisticsList.action?massage=stat" id="Form1">
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
							class="current_status_submenu">发票统计</span>
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
						<tr>
							<td align="left"><a href="#"
								onclick="exportBillDistribution()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
							</a> <%-- <a href="#" onclick="exportBillDistribution()" name="cmdR" id="cmdR">
	                              <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon20.png"/>
	                       		     	发票申领
	                        </a> --%>
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
									<th style="text-align: center">税控钥匙编号</th>
									<th style="text-align: center">分发日期</th>
									<th style="text-align: center">报税机构</th>
									<th style="text-align: center">开票员名称</th>
									<th style="text-align: center">发票代码</th>
									<th style="text-align: center">发票起始号码</th>
									<th style="text-align: center">发票终止号码</th>
									<th style="text-align: center">总张数</th>
									<th style="text-align: center">发票类型</th>
									<th style="text-align: center">发票状态</th>
									<th style="text-align: center">剩余张数</th>
									<th style="text-align: center">开具张数</th>
									<th style="text-align: center">打印张数</th>
									<th style="text-align: center">作废张数</th>
									<th style="text-align: center">红冲张数</th>
									<th style="text-align: center">空白发票遗失张数</th>
									<th style="text-align: center">空白发票回收张数</th>
									<th style="text-align: center">空白发票作废张数</th>
									<th style="text-align: center">操作</th>
									<th style="text-align: center">查看</th>
								</tr>
								<s:iterator value="paginationList.recordList" id="iList"
									status="stuts">
									<tr align="center"
										id='<s:property value="billDistribution.disId" />'
										class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
										<td align="center"><input
											style="width: 13px; height: 13px;" class="disId"
											type="checkbox" name="selectIds"
											value='<s:property value="disId"/>'></td>
										<td align="center"><s:property value="#stuts.index+1" /></td>
										<td align="center"><s:property value="taxNo" /></td>
										<td align="center"><s:date name="disDate"
												format="yyyy-MM-dd"></s:date></td>
										<td align="center"><s:property value="instName" /></td>
										<td align="center"><s:property value="kpyName" /></td>
										<td align="center"><s:property value="billId" /></td>
										<td align="center"><s:property value="billStartNo" /></td>
										<td align="center"><s:property value="billEndNo" /></td>
										<td align="center"><s:property value="ffCount" /></td>
										<td align="center"><s:property
												value="@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(billType)" /></td>
										<td align="center"><s:property
												value="@com.cjit.vms.trans.util.JSPUtil@getValue(@com.cjit.vms.stock.util.StockUtil@JS_ENTER_MAP,jsEnter)" />
										</td>
										<td align="center"><s:property value="syCount" /></td>
										<td align="center"><s:property value="ykpCount" /></td>
										<td align="center"><s:property value="ydyCount" /></td>
										<td align="center"><s:property value="yffCount"></s:property>
										</td>
										<td align="center"><s:property value="yhcCount" /></td>

										<td align="center"><s:property value="syfpYsCount" /></td>
										<td align="center"><s:property value="syfpHsCount" /></td>
										<td align="center"><s:property value="syfpZfCount" /></td>

										<td align="center"><s:if
												test='jsEnter==@com.cjit.vms.stock.util.StockUtil@JS_ENTENER_NO'>
												<a href="#" onclick="jsEnter('<s:property value="disId"/>')">
													<img
													src="<c:out value="${bopTheme}"/>/themes/images/icons/confirm.png"
													title="接收确认" style="border-width: 0px;" />
												</a>
											</s:if> <s:else>
									   确认成功
									</s:else></td>

										<td align="center"><a
											href="goSelect.action?disId=<s:property value="disId"/>-<s:property value="billId" />-<s:property value="billStartNo" />-<s:property value="billEndNo" />">
												<img
												src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
												title="查看详情" style="border-width: 0px;" />
										</a></td>
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
