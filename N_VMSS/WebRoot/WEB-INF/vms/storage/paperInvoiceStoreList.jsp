<!--file: <%=request.getRequestURI() %> -->
<%@page import="com.cjit.vms.trans.model.storage.PaperInvoiceListInfo"%>
<%@page import="java.util.List"%>
<%@page import="com.cjit.common.util.PaginationList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>纸质发票管理</title>

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
	function openPaperInvoice(url){
		OpenModalWindow(url,650,350,true);
	}
	// 打开空白发票作废页面
	function invalidBlankPaperInvoice(){
		var url = 'invalidBlankPaperInvoice.action';
		OpenModalWindow(url,650,250,true);
	}
	function submitForm(actionUrl){
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action="listPageInvoice.action";
	}
	function deleteTransBatch(actionUrl){
	
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
	}
  function hideDetailInfoDIV(){
		document.getElementById("detailInfoDIV").style.display='none';
	}

	function showDetailInfoDIV(logID){
	 	var currRow =  window.event.srcElement.parentElement.parentElement;// 获取当前行

	 	document.getElementById("_td2").innerHTML=logID;

	 	for(var i=3;i<16;i++){
	 		document.getElementById("_td"+i).innerHTML=currRow.cells[i-1].title;
	 	}
	 	
	 	document.getElementById("detailInfoDIV").style.display='block';
	}
	
	function output() {
		//拷贝 
		var elTable = document.getElementById("lessGridList"); //这里的page1 是包含表格的Div层的ID
		var oRangeRef = document.body.createTextRange();
		oRangeRef.moveToElementText( elTable );
		oRangeRef.execCommand("Copy");
		
		
		//粘贴 
		try{
			var appExcel = new ActiveXObject( "Excel.Application" ); 
			appExcel.Visible = true; 
			appExcel.Workbooks.Add().Worksheets.Item(1).Paste(); 
		//appExcel = null; 
		}catch(e){
			alert("使用此功能必须在浏览器中设置:Internet选项->安全->将本站加入“受信任的站点”。"); 
		}
		
		
		var elTable1 = document.getElementById("lessGridList"); 
		var oRangeRef1 = document.body.createTextRange(); 
		oRangeRef1.moveToElementText( elTable1 ); 
		oRangeRef1.execCommand( "Copy" );
		
		
		//粘贴 
		try{ 
		appExcel.Visible = true; 
		appExcel.Worksheets.Item(2).Paste(); 
		appExcel1 = null; 
		}catch(e){ 
		alert("使用此功能必须在浏览器中设置:Internet选项->安全->将本站加入“受信任的站点”。"); 
		} 
	}
	$(function(){
		$("#cmdDistribute").click(function(){
			$checkedStore=$("input[name='paper_invoice_stock_id']:checked");
			if($checkedStore.size()==0){
				alert("请选择您要分发的库存发票");
				return;
			}
			$store_ids="";
			for($idx=0;$idx<$checkedStore.size();$idx++){
				if(($idx+1)!=$checkedStore.size()){
					$store_ids+=$($checkedStore[$idx]).val()+",";
				}else{
					$store_ids+=$($checkedStore[$idx]).val();
				}
			}
			OpenModalWindow("<c:out value='${webapp}'/>/initDistrubute.action?paper_invoice_stock_ids="+$store_ids,800,600,true);
		});
	});
 	
	// [导入]按钮
	function importData(webroot){
		var fileId = document.getElementsByName("attachmentCustomer")[0];
		if(fileId.value.length > 0){
			if(fileId.value.lastIndexOf(".XLS") > -1||fileId.value.lastIndexOf(".xls")>-1){
				document.forms[0].action = webroot+"/importPageInvoiceExcel.action";
				document.forms[0].submit();
				document.forms[0].action="listPageInvoice.action";
			}else{
				alert("文件格式不对，请上传Excel文件。");
			}
		}else{
			alert("请先选择要上传的文件。");
		}
	}
</script>
</head>
<body>
	<form id="main"
		action="<c:out value='${webapp}'/>/listPageInvoice.action"
		method="post" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">库存管理</span> <span
							class="current_status_submenu">发票管理</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td width="25">机构名称</td>
								<td width="130"><input type="hidden" id="instName"
									name="instName" value='<s:property value="instName"/>'>
									<input type="text" class="tbl_query_text" id="inst_Name"
									name="instName" value='<s:property value="instName"/>'
									onclick="setOrg(this);" readonly="readonly"> <%-- 	<s:if test="authInstList != null && authInstList.size > 0">
						<s:select name="instId" list="authInstList" listKey='id' listValue='name' headerKey="" headerValue="所有"/>
						</s:if>
						<s:if test="authInstList == null || authInstList.size == 0">
						<select name="instId" class="readOnlyText">
						<option value="">请分配机构权限</option>
						</select>
						</s:if> --%> <%--					<input id="instId" class="tbl_query_text" name="instId" type="text" value="<s:property value='instId' />" />--%>
								</td>
								<td width="50">领购时间</td>
								<td width="280"><input id="receiveInvoiceTime"
									name="receiveInvoiceTime" type="text"
									value="<s:property value='receiveInvoiceTime' />"
									class="tbl_query_time1"
									onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:document.getElementById('receiveInvoiceEndTime').value == '' ? new Date():document.getElementById('receiveInvoiceEndTime').value})" />
									&nbsp;&nbsp;~&nbsp;&nbsp; <input id="receiveInvoiceEndTime"
									name="receiveInvoiceEndTime" type="text"
									value="<s:property value='receiveInvoiceEndTime' />"
									class="tbl_query_time1"
									onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:document.getElementById('receiveInvoiceTime').value,maxDate:new Date()})" /></td>
								<td width="50">纳税人识别号</td>
								<td width="130"><input id="taxpayerNo"
									class="tbl_query_text" name="taxpayerNo" type="text"
									value="<s:property value='taxpayerNo' />" /></td>
							</tr>
							<tr>
								<td width="50">发票类型</td>
								<td width="130"><select id="invoiceType" name="invoiceType"
									style="width: 125px">
										<option value="" <s:if test='invoiceType==""'>selected</s:if>
											<s:else></s:else>>所有</option>
										<s:iterator value="mapVatType" id="entry">
											<option value="<s:property value="key"/>"
												<s:if test='invoiceType==#entry.key'>selected</s:if>
												<s:else></s:else>><s:property value="value" /></option>
										</s:iterator>
								</select></td>
								<td width="50">纳税人名称</td>
								<td width="130"><input id="taxpayerCame"
									class="tbl_query_text" name="taxpayerCame" type="text"
									value="<s:property value='taxpayerCame' />" /></td>

								<td width="50">发票代码</td>
								<td width="130"><input id="invoiceCode"
									class="tbl_query_text" name="invoiceCode" type="text"
									value="<s:property value='invoiceCode' />" /></td>
							</tr>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><input type="button" class="tbl_query_button"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'"
									onclick="submitForm('listPageInvoice.action');"
									name="cmdSelect" value="查询" id="cmdSelect" /></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr align="left">
							<td style="text-align: left">
								<%--<a href="#" onclick="openPaperInvoice('addPaperInvoice.action');" name="cmdFilter" id="cmdFilter"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>新增</a>
				<a href="#" name="cmdDistribute" id="cmdDistribute"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1001.png"/>分发</a><%--onclick="submitForm('<c:out value='${webapp}'/>/initDistrubute.action');"  --%>
								<!-- <a href="#" onclick="invalidBlankPaperInvoice();" name="cmdInvalid" id="cmdInvalid"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1002.png"/>空白发票作废</a> -->
								<a href="#"
								onclick="submitForm('createPageInvoiceExcel.action');"
								name="cmdExcel" id="cmdExcel"><img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />导出</a>
							</td>

							<td align="right" width="255"><s:file
									name="attachmentCustomer" size="30"
									style="height:26px; line-height:30px; "></s:file></td>
							<td><a href="#" onclick="importData('<%=webapp%>')"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png" />导入
							</a></td>
						</tr>
					</table>
					<div id="lessGridList1" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align: center"><input
									id="CheckAll" style="width: 13px; height: 13px;"
									type="checkbox" onClick="checkAll(this,'batchIdList')" /></th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">机构名称</th>
								<th style="text-align: center">领购日期</th>
								<!--  	<th style="text-align: center">领购人员</th> -->
								<th style="text-align: center">纳税人识别号</th>
								<th style="text-align: center">纳税人名称</th>
								<th style="text-align: center">发票类型</th>
								<th style="text-align: center">单张开票限额</th>
								<!--  	<th style="text-align: center">是否已分发</th>  -->
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票起始号码</th>
								<th style="text-align: center">发票终止号码</th>
								<!-- 	<th style="text-align: center">已分发张数</th>   -->
								<th style="text-align: center">剩余张数</th>
								<th style="text-align: center">已用张数</th>
								<!--	<th style="text-align: center">未用张数</th>
				  	<th style="text-align: center">已用百分比(%)</th>  -->
								<%--<th style="text-align: center">编辑</th>
					--%>
								<th style="text-align: center; display: none">操作状态</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">

								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td><input type="checkbox"
										style="width: 13px; height: 13px;"
										name="paper_invoice_stock_id"
										value="<s:property value="#iList.paperInvoiceId"/>" /></td>
									<td align="center"><s:property value='#stuts.count' /></td>
									<td><s:property value="instName" /></td>
									<td><s:date name="receiveInvoiceTime" format="yyyy-MM-dd" /></td>
									<td><s:property value='taxpayerNo' /></td>
									<td><s:property value='taxpayerCame' /></td>
									<td><s:iterator value="mapVatType" id="entry">
											<s:if test='invoiceType==#entry.key'>
												<s:property value="value" />
											</s:if>
										</s:iterator>
									<td><s:property value='maxMoney' /></td>
									<%--  	<td>
							<s:if test='distributeFlag=="0"'>未分发</s:if>
							<s:elseif test='distributeFlag=="1"'>分发完成</s:elseif>
							<s:elseif test='distributeFlag=="2"'>部分分发</s:elseif>
							<s:else></s:else></td>
						<td><s:property value='billNoField'/></td>
						  --%>
									<td><s:property value='invoiceCode' /></td>
									<td><s:property value='invoiceBeginNo' /></td>
									<td><s:property value='invoiceEndNo' /></td>
									</td>
									<!--  		<td><s:if test='distributeNum==null'>0</s:if><s:else><s:property value='distributeNum'/></s:else></td>  -->
									<td><s:property value='invoiceNum' /></td>
									<td><s:property value='userdNum' /></td>
									<%-- 	<td><s:property value='unUserdNum'/></td>
						<td><s:if test='userRatioNum==null'></s:if><s:else><s:property value='userRatioNum'/>%</s:else></td> --%>
									<%--<td>
							<s:if test='distributeFlag=="0"'>
								<a href="javascript:void(0);" onClick="openPaperInvoice('editPaperInvoice.action?stockId=<s:property value="#iList.paperInvoiceId" />');"><img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑票据" style="border-width: 0px;" /></a>
							</s:if>
						</td>
						<td style="display: none"><s:property value='batchId'/></td>
					</tr> --%>
							</s:iterator>
						</table>
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
</html>















