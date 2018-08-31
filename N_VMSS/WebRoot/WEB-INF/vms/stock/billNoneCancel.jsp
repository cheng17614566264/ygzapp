<!-- 发票作废记录 -->
<!-- file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.billInvalid.BillCancelInfo"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
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
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript">
	var msg = '<s:property value="message" escape="false" />';
	if (msg != null && msg != '') {
		alert(msg);
	}
	function openPaperInvoice(url) {
		OpenModalWindow(url, 650, 350, true);
		document.forms[0].action="SelectNoneAction";
	}
	function saveVendorSuccess() {
		document.forms[0].submit();
	}
	// [查询]按钮
	function query() {
		submitAction(document.forms[0], "SelectNoneAction.action");
	}

	// 撤销按钮
	function revokeBill() {
		var t = "";
		var inputs = document.getElementsByName("selectBillIds");
		for (var i = 0; i < inputs.length; i++) {
			if (inputs[i].checked == true) {
				t += inputs[i].value + ",";
			}
		}
		if (t.length == 0) {
			alert("请选择票据记录！");
			return;
		}
		if (confirm("确定将选中票据进行作废处理？")) {
			$.ajax({
				url : 'revokeBillNoneCancel.action',
				type : 'POST',
				async : false,
				data : {
					invoiceId : t.substring(0, t.length - 1)
				},
				dataType : 'text',
				error : function() {
					return false;
				},
				success : function(result) {
					if (result == '1') {
						alert("撤销成功!");
						document.forms[0].submit();
					} else {
						alert("撤销失败!");
					}
				}
			});
		} else {
			return;
		}
	}

	// 导出按钮
	function exportCancelBill() {
		submitAction(document.forms[0], "cancelNoneBillToExcel.action");
		document.forms[0].action="SelectNoneAction";
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
			document.forms[0].action="SelectNoneAction";
		}
	}
	
	function submitForm(actionURL){
		var form = document.getElementById("Form1");
		form.action=actionURL;
		form.submit();
	}
	/**  
	* 确认
	*/
	function updatestar(id) {
		$.ajax({
			type: 'POST', 
			url: 'updatestar.action', 
			data: {Id:id}, 
			dataType: 'json', 
			async: false, 
			success: function(ajaxReturn) {
					alert("确认操作成功,请在税控系统作废发票！");
					//window.location.href="https://hao.360.cn/?src=lm&ls=n478bfd1a95";
			},
			error:function(ajaxReturn){
				alert("确认操作失败");
			}
	     });
		submitForm('SelectAction.action');
	}
	
	//库存管理附件查看
	function viewImgFromInvoiceJF(){
		 $selects = $("input[name='selects']:checked")
		 if($selects.length>1){
			 alert("请选择单条记录查看");
			 return ;
		 }else if($selects.length==0){
			 alert("请选择数据");
			 return ;
		 }else{
			 OpenModalWindowSubmit('#.action?fileName='+$selects.val(),1000,650,true);
		 }
	}
</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="SelectNoneAction.action"
		id="Form1">
		<%
			String currMonth = (String) request.getAttribute("currMonth");
		%>
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition"><s:component
						template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">库存管理</span> <span
							class="current_status_submenu">空白发票作废确认</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%"
							border="0">
							<tr>
								<td style="text-align: right; width: 6%;">报税机构</td>
								<td style="text-align: left; width: 14%;"><input
									type="text" class="tbl_query_text" id="inst_Name"
									name="lostRecycle.instId" value='' onclick="setOrg(this);"
									readonly="readonly"></td>
								<td style="text-align: right; width: 6%;">录入日期</td>
								<td style="text-align: left; width: 20%;"><input
									class="tbl_query_time" id="lostRecycle.operateStartDate"
									type="text" name="lostRecycle.operateStartDate"
									value="<s:property value='lostRecycle.operateStartDate'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'lostRecycle.operateEndDate\')}'})"
									size='11' /> -- <input class="tbl_query_time"
									id="lostRecycle.operateEndDate" type="text"
									name="lostRecycle.operateEndDate"
									value="<s:property value='lostRecycle.operateEndDate'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'lostRecycle.operateStartDate\')}'})"
									size='11' /></td>
								<td style="text-align: right; width: 6%;">发票类型</td>
								<td style="text-align: left; width: 14%;"><s:select
										id="billType" name="lostRecycle.billType"
										list="#{'1':'增值税普通发票','0':'增值税专用发票'}" headerKey=''
										headerValue="全部" listKey='key' listValue='value'
										cssClass="tbl_query_text" /></td>
							</tr>
							<tr>
								<td width="25" style="text-align: right;">发票代码</td>
								<td width="130"><input id="billId" class="tbl_query_text"
									name="lostRecycle.billId" type="text"
									value="<s:property value='lostRecycle.billId' />" /></td>

								<td width="25" style="text-align: right;">发票起始号码</td>
								<td width="130"><input id="billStartNo"
									class="tbl_query_text" name="lostRecycle.billStartNo"
									type="text"
									value="<s:property value='lostRecycle.billStartNo' />" /></td>
								<td width="25" style="text-align: right;">发票截止号码</td>
								<td width="130"><input id="billEndNo"
									class="tbl_query_text" name="lostRecycle.billEndNo" type="text"
									value="<s:property value='lostRecycle.billEndNo' />" /></td>
							</tr>
							<tr>
								<td width="25" style="text-align: right;">开票员名称</td>
								<td width="130"><input id="kpyName" class="tbl_query_text"
									name="lostRecycle.kpyName" type="text"
									value="<s:property value='lostRecycle.kpyName' />" /></td>
								<td width="25" style="text-align: right;">申请状态</td>
								<td width="130"><s:select id="state"
										name="lostRecycle.state"
										list="#{'0':'已申请','1':'申请成功','3':'申请拒绝'}" headerKey=''
										headerValue="全部" listKey='key' listValue='value'
										cssClass="tbl_query_text" /></td>
								<td colspan="2" style="padding-left: 10.5%"><input
									type="button" onclick="submitForm('SelectAction.action')"
									class="tbl_query_button" value="查询"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="btSubmit"
									id="btSubmit" style="margin-right: 30px" /></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left"><a href="#"
								onclick="submitForm('createExecllostRecycle.action?massage=ZFMC');"
								name="cmdExcel" id="cmdExcel"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />导出
							</a> <%-- <a href="#" onclick="viewImgFromInvoiceJF()" name="cmdR" id="cmdR">
	                               	<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1027.png"/>
	                       				 附件查看
	                       		</a> --%></td>
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
								<th style="text-align: center">报税机构</th>
								<th style="text-align: center">开票员编号</th>
								<th style="text-align: center">开票员名称</th>
								<th style="text-align: center">数据录入日期</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票起始号码</th>
								<th style="text-align: center">发票截止号码</th>
								<th style="text-align: center">发票类型</th>
								<th style="text-align: center">数量统计</th>
								<th style="text-align: center">作废原因</th>
								<th style="text-align: center">状态</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">
								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td><input type="checkbox"
										style="width: 13px; height: 13px;" name="id"
										value="<s:property value="id"/>" /></td>
									<td align="center"><s:property value='#stuts.count' /></td>
									<td align="center"><s:property value='instName' /></td>
									<td align="center"><s:property value='kpyId' /></td>
									<td align="center"><s:property value='kpyName' /></td>
									<td align="center"><s:date name="operateDate"
											format="yyyy-MM-dd" /></td>
									<td align="center"><s:property value='billId' /></td>
									<td align="center"><s:property value='billStartNo' /></td>
									<td align="center"><s:property value='billEndNo' /></td>
									<td align="center"><s:property
											value='@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(billType)' /></td>
									<td align="center"><s:property value='count' /></td>
									<td align="center"><s:property value='remark' /></td>
									<td align="center"><s:if test='state=="0"'>
											已申请
										</s:if>
										<s:elseif test='state=="1"'>
											申请成功
										</s:elseif>
										<s:else>
											申请拒绝
										</s:else></td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="width: 100%;vlign=top;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="left"></td>
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
					</div></td>
			</tr>
		</table>
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