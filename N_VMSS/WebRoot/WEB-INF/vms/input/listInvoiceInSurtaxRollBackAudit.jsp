<!-- 进项--审核回退 -->
<%@page import="java.util.Calendar"%>
<%@page import="com.cjit.vms.input.model.Proportionality"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.cjit.vms.input.model.InputInfoUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/editview.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>进项税转出比例金额</title>
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

.ellipsis_div {
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}
</style>
<script type="text/javascript">
	function reset(url) {
		//OpenModalWindow(url,650,350,true);
		location.href = url;
	}
	// 打开空白发票作废页面
	function invalidBlankPaperInvoice() {
		var url = 'invalidBlankPaperInvoice.action';
		OpenModalWindow(url, 650, 250, true);
	}
	function submitForm(actionUrl) {
		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
		form.action = "listInvoiceInSurtaxRolloutAudit.action";
	}

	function deleteTransBatch(actionUrl) {

		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
		form.action = "listInvoiceInSurtax.action";
	}

	function hideDetailInfoDIV() {
		document.getElementById("detailInfoDIV").style.display = 'none';
	}

	function showDetailInfoDIV(logID) {
		var currRow = window.event.srcElement.parentElement.parentElement;// 获取当前行

		document.getElementById("_td2").innerHTML = logID;

		for (var i = 3; i < 16; i++) {
			document.getElementById("_td" + i).innerHTML = currRow.cells[i - 1].title;
		}

		document.getElementById("detailInfoDIV").style.display = 'block';
	}

	function output() {
		//拷贝 
		var elTable = document.getElementById("lessGridList"); //这里的page1 是包含表格的Div层的ID
		var oRangeRef = document.body.createTextRange();
		oRangeRef.moveToElementText(elTable);
		oRangeRef.execCommand("Copy");

		//粘贴 
		try {
			var appExcel = new ActiveXObject("Excel.Application");
			appExcel.Visible = true;
			appExcel.Workbooks.Add().Worksheets.Item(1).Paste();
			//appExcel = null; 
		} catch (e) {
			alert("使用此功能必须在浏览器中设置:Internet选项->安全->将本站加入“受信任的站点”。");
		}

		var elTable1 = document.getElementById("lessGridList");
		var oRangeRef1 = document.body.createTextRange();
		oRangeRef1.moveToElementText(elTable1);
		oRangeRef1.execCommand("Copy");

		//粘贴 
		try {
			appExcel.Visible = true;
			appExcel.Worksheets.Item(2).Paste();
			appExcel1 = null;
		} catch (e) {
			alert("使用此功能必须在浏览器中设置:Internet选项->安全->将本站加入“受信任的站点”。");
		}
	}
	/* 
	 *进项转出
	 */
	function transInput() {
		var ids = document.getElementsByName("billId");
		var selectedIds = null;
		if (ids.length > 0) {
			for (var i = 0; i < ids.length; i++) {
				var id = ids[i];
				var j = 0;
				if (id.checked) {
					if (selectedIds != null) {
						selectedIds = selectedIds + "," + id.value;
					} else {
						selectedIds = id.value;
					}
					j++;
				}
			}
		}
		if (selectedIds == null) {
			alert("请选择要转出的记录");
		}
		submitAction(document.forms[0], "transInputInfo.action?selectedIds="
				+ selectedIds);
		document.forms[0].action = "listInvoiceInSurtax.action";
	}
	
	//转出比例计算
	function cmdRollBackBtn() {
		$.ajax({
			url : 'reckonAction.action',// 跳转到 action    
			type : 'post',
			dataType : 'text',
			success : function(ajaxReturn) {
				var returnJson = $.parseJSON(ajaxReturn);
					if (returnJson.isNormal) {
						alert("转出比例计算已完成");
					}else{
						alert(returnJson.message);
					}
			},
			error : function(ajaxReturn) {
				var returnJson = $.parseJSON(ajaxReturn);
				if (returnJson.isNormal) {
					alert("转出比例计算已完成");
				}else{
					alert(returnJson.message);
				}
			}
		});
	}
	
	function viewImgFromInvoiceJF(){
		 $selects = $("input[name='selects']:checked")
		 if($selects.length>1){
			 alert("请选择单条记录查看");
			 return ;
		 }else if($selects.length==0){
			 alert("请选择数据");
			 return ;
		 }else{
			 OpenModalWindowSubmit('viewImgFromInvoiceJF.action?fileName='+$selects.val(),1000,650,true);
		 }
	}
	
	function rolloutAudit(val){
		$selects=$("input[name='selects']:checked");
		if($selects.length>1){
			 alert("请选择单条记录查看");
			 return ;
		 }else if($selects.length==0){
			 alert("请选择数据");
			 return ;
		 }
		if("1"==val){
			//审核通过
			audit($selects.val(), "1");
		}else{
			alert($selects.val())
			OpenModalWindowSubmit("InvoiceCancel.action?selects="+$selects.val(),500,250,true);
		}
	}
	//  kuaiji  audit   yuedu 
	function audit(date,audit){
		$.ajax({
		       url: 'AuditRollout.action',
		       type: 'POST',
		       async: false,
		       data: {date: date,audit: audit},
		       dataType: 'text',
		       timeout: 1000,
		       error: function () {
		           alert("操作失败");
		           submitAction(document.forms[0], "listInvoiceInSurtaxRolloutAudit.action");
		       },
		       success: function (result) {
		    	   alert("操作成功！")
		    	   submitAction(document.forms[0], "listInvoiceInSurtaxRolloutAudit.action");
		       }
		    });
	}
	
	function OpenModalWindowSubmit(newURL,width,height,needReload){
		var retData = false;
		if(typeof(width) == 'undefined'){
			width = screen.width * 0.9;
		}
		if(typeof(height) == 'undefined'){
			height = screen.height * 0.9;
		}
		if(typeof(needReload) == 'undefined'){
			needReload = false;
		}
		retData = showModalDialog(newURL, 
					window, 
					"dialogWidth:" + width
						+ "px;dialogHeight:" + height
						+ "px;center=1;scroll=1;help=0;status=0;");
		if(needReload && retData){
			window.document.forms[0].submit();
		}
	}
	function viewImgFromInvoiceJF(){
		 $selects = $("input[name='selects']:checked")
		 if($selects.length>1){
			 alert("请选择单条记录查看");
			 return ;
		 }else if($selects.length==0){
			 alert("请选择数据");
			 return ;
		 }else{
			 OpenModalWindowSubmit('viewImgFromInvoiceJF.action?fileName='+$selects.val(),1000,650,true);
		 }
	}
</script>
</head>
<body>
	<form id="main"
		action="<c:out value='${webapp}'/>/listInvoiceInSurtax.action"
		method="post">
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">进项税管理</span> <span
							class="current_status_submenu">进项管理</span> <span
							class="current_status_submenu">审核详情</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td style="text-align: right; width: 6%;">操作日期</td>
								<td style="text-align: left; width: 20%;"><input
									class="tbl_query_time" id="proportionality.operateStartDate"
									type="text" name="proportionality.operateStartDate"
									value="<s:property value='proportionality.operateStartDate'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'proportionality.operateEndDate\')}'})"
									size='11' /> -- <input class="tbl_query_time"
									id="proportionality.operateEndDate" type="text"
									name="proportionality.operateEndDate"
									value="<s:property value='proportionality.operateEndDate'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'proportionality.operateStartDate\')}'})"
									size='11' /></td>
								<td style="text-align: right; width: 6%;">报税机构</td>
								<td style="text-align: left; width: 14%;"><input
									type="text" class="tbl_query_text" id="inst_Name" name="instId"
									value='' onclick="setOrg(this);" readonly="readonly"></td>
								<td style="text-align: right; width: 6%;">审核状态</td>
								<td style="text-align: left; width: 14%;"><s:select
										id="datasource" name="proportionality.datasource"
										list="#{'3':'审核拒绝','1':'审核通过'}" listKey='key'
										listValue='value' cssClass="tbl_query_text" /></td>
							</tr>
							<tr>
								<td colspan="4"><input type="button"
									class="tbl_query_button"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'"
									onclick="submitForm('listInvoiceInSurtaxRollBackAudit.action');"
									name="cmdQueryBtn" value="查询" id="cmdQueryBtn" /></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr align="left">
							<td style="text-align: left"><a href="javascript:void();"
								onclick="submitForm('exportInvoiceAudit.action')"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
							</a> <%-- <a href="#" onclick="viewImgFromInvoiceJF()" name="cmdR"
								id="cmdR"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1027.png" />
									附件查看
							</a> --%></td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align: center"><input
									id="CheckAll" style="width: 13px; height: 13px;"
									type="checkbox" onClick="checkAll(this,'billId')" /></th>
								<th style="text-align: center" hidden>序号</th>
								<th style="text-align: center">报税机构编号</th>
								<th style="text-align: center">报税机构</th>
								<th style="text-align: center">月度</th>
								<th style="text-align: center">会计机构名称</th>
								<th style="text-align: center">操作员编号</th>
								<th style="text-align: center">操作员姓名</th>
								<th style="text-align: center">复核员编号</th>
								<th style="text-align: center">复核员姓名</th>
								<th style="text-align: center">调整值</th>
								<th style="text-align: center">操作日期</th>
								<th style="text-align: center">机构汇总编号</th>
								<th style="text-align: center">审核状态</th>
								<th style="text-align: center">回退原因</th>
							</tr>
							<%  List<Proportionality> list=(List)request.getAttribute("RolloutBackAudit");
									if(list!=null&&list.size()>0){
										for(int i=0 ;i<list.size();i++){
											Proportionality p =list.get(i);
										%>
							<tr>
								<td><input type="checkbox"
									style="width: 13px; height: 13px;" name="selects"
									value="<%= p.getRelationInstId()+"-"+p.getYearMonth()+"-"+p.getResult()%>" />
								</td>
								<td style="text-align: center" hidden><%=  i+1%></td>
								<td style="text-align: center"><%=  p.getInstId() %></td>
								<td style="text-align: center"><%=  p.getInstName()%></td>
								<td style="text-align: center"><%=  p.getYearMonth()%></td>
								<td style="text-align: center"><%=  p.getKjIstName()==null?"":p.getKjIstName()%></td>

								<td style="text-align: center"><%=  p.getApply_proposer_id()%></td>
								<td style="text-align: center"><%=  p.getApply_proposer_name()%></td>
								<td style="text-align: center"><%=  p.getAuditor_proposer_id()%></td>
								<td style="text-align: center"><%=  p.getAuditor_proposer_name()%></td>

								<td style="text-align: center"><%=  p.getResult()%></td>
								<td style="text-align: center"><%=  p.getOperateDate()%></td>
								<td style="text-align: center"><%=  p.getRelationInstId()%></td>
								<td style="text-align: center"><%=  com.cjit.vms.input.util.Util.getavailableCH(p.getAvailable())%></td>
								<td style="text-align: center"><%=  p.getReason()==null?"":p.getReason()%></td>
							</tr>
							<%
										}
									}
								%>
						</table>

						<input type="hidden" name="currentPage"
							value="<s:property value="paginationList.currentPage"/>" /> <input
							type="hidden" name="o_bill_id" id="o_bill_id" value="" />
					</div>
					<div id="anpBoud" align="Right" style="width: 100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
					</div>
				</td>
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
<script type="text/javascript">
	$(".cmdViewBt").click(function() {
		$o_bill_id = $(this).attr("rel");
		$("#o_bill_id").val($o_bill_id);
		submitForm("<c:out value='${webapp}'/>/viewInvoiceScanAuth.action");
	});

	$(function() {
		$(".edit_insurtax").click(
				function() {
					bill_id = $(this).attr("rel");
					OpenModalWindow(
							"<c:out value='${webapp}'/>/editInvoiceInSurtax.action?bill_id="
									+ bill_id, 800, 600, true);
				});
		//转出提交
		$("#cmdRollOutSubmitBtn").click(function() {
			$billIds = $("input[name='billId']:checked");
			if ($billIds.size() == 0) {
				alert("请选择您要转出的数据");
				return false;
			}
			submitForm('rollOutSubmitInvoiceInSurtax.action');
		});
	});
	function sgrollout(){
		submitForm("<c:out value='${webapp}'/>/sgrollout.action");
	}
	
</script>
</html>
