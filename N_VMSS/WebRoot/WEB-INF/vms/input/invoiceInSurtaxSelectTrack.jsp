<!-- 进项转出比例比例值轨迹查询页面-->
<%@page import="java.util.Map"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.cjit.vms.input.model.Proportionality"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Set"%>
<%@page import="com.cjit.vms.input.model.InputInfo"%>
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
<title>进项税转出比例轨迹查看</title>
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
		form.action = "listInvoiceInSurtaxBLSelect.action";
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
							class="current_status_submenu">进项转出比例轨迹查询</span>
					</div> <!-- 查询条件功能框 -->
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td style="text-align: right; width: 10%;">报税机构</td>
								<td style="text-align: right; width: 14%;"><input
									type="text" class="tbl_query_text" id="inst_Name" name="instId"
									value='' onclick="setOrg(this);" readonly="readonly"></td>

								<td>日期</td>
								<td><input type="text" style="width: 110px"
									class="tbl_query_text" name="StartDate" id="StartDate"
									value="<s:property value='StartDate'/>" class="tbl_query_time"
									onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" /> &nbsp;-&nbsp;
									<input type="text" style="width: 110px" class="tbl_query_text"
									name="EndDate" id="EndDate"
									value="<s:property value='EndDate'/>" class="tbl_query_time"
									onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" /></td>
								<td colspan="4"><input type="button"
									class="tbl_query_button"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'"
									onclick="submitForm('listInvoiceInSurtaxSelectTrack.action');"
									name="cmdQueryBtn" value="查询" id="cmdQueryBtn" /></td>
							</tr>
						</table>
					</div> <!-- 功能框 -->
					<table id="tbl_tools" width="100%" border="0">
						<tr align="left">
							<td style="text-align: left">
								<%-- <a href="javascript:void();"
								onclick="submitForm('exportBLSelect.action')"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
							</a> --%>
							</td>
						</tr>
					</table> <!-- 展示区 -->
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align: center"><input
									id="CheckAll" style="width: 13px; height: 13px;"
									type="checkbox" onClick="checkAll(this,'billId')" /></th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">月度</th>
								<th style="text-align: center">报税机构</th>
								<th style="text-align: center">计算类型</th>
								<th style="text-align: center">项目</th>
								<th style="text-align: center">数据值</th>
							</tr>
							<%  List<Proportionality> blList=(List)request.getAttribute("track");
									if(blList.size()>0&&blList!=null){
										for(int i=0;i<blList.size();i++){
											Proportionality prop =blList.get(i);
											List<String> ls=prop.getObj(); 
											%>
							<tr>
								<td rowspan="<%= ls.size()*4+1 %>"><input type="checkbox"
									style="width: 13px; height: 13px;" name="selects"
									value="<%= i+1 %>" /></td>
								<td style="text-align: center" rowspan="<%= ls.size()*4+1 %>"><%= i+1 %></td>
								<td style="text-align: center" rowspan="<%= ls.size()*4+1%>"><%= prop.getYearMonth() %></td>
								<td style="text-align: center" rowspan="<%= ls.size()*4+1 %>"><%= prop.getInstName() %></td>
							</tr>
							<%  
													for(int k=0 ;k<ls.size() ;k++){
														String val = ls.get(k);
														%>
							<tr>
								<td style="text-align: center" rowspan="4"><%=  com.cjit.vms.input.util.Util.getDatasource(val.split("-")[0])%></td>
								<td style="text-align: center">分子</td>
								<td style="text-align: center"><%= val.split("-")[1]%></td>
							<tr>
								<td style="text-align: center">分母</td>
								<td style="text-align: center"><%=  val.split("-")[2] %></td>
							</tr>
							<tr>
								<td style="text-align: center">比例值</td>
								<td style="text-align: center"><%= val.split("-")[3] %></td>

							</tr>
							<tr>
								<td style="text-align: center">有效性</td>
								<td style="text-align: center"><%= com.cjit.vms.input.util.Util.getavailableCH(val.split("-")[4])%></td>
							</tr>
							<%
														}
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
