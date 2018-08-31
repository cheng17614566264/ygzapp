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
			$checkedStore=$("input[name='inventoryId']:checked");
			if($checkedStore.size()==0){
				alert("请选择您要分发的库存发票");
				return;
			}
			var inventoryId=document.getElementsByName("inventoryId");
			var k=0
			var m=0
			for(var i=0;i<inventoryId.length;i++){
				if(inventoryId[i].checked==true){
					k++;
					m=i
				}
			}
			if(k>1){
				alert("请选择一条数据分发");
				return;
			}
			var cmdDistribute=document.getElementById("cmdDistribute");
			cmdDistribute.href="initDistrubute.action?inventoryId="+inventoryId[m].value;
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
	
	 function checkAll(){
		var CheckAll=document.getElementById("CheckAll");
		var inventoryId=document.getElementsByName("inventoryId");
		for(var i=0;i<inventoryId.length;i++){
			 if(CheckAll.checked==true){
				inventoryId[i].checked=true
			 }else{
				inventoryId[i].checked=false
			 }
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
								<td width="25">报税机构</td>
								<td width="130"><input type="hidden" id="instName"
									name="instName" value='<s:property value="instName"/>'>
									<input type="text" class="tbl_query_text" id="inst_Name"
									name="instName" value='<s:property value="instName"/>'
									onclick="setOrg(this);" readonly="readonly"></td>
								<td width="25">入库时间</td>
								<td width="130"><input id="putInDate" name="putInDate"
									type="text" value="<s:property value='putInDate' />"
									class="tbl_query_time1"
									onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:document.getElementById('putInDate').value == '' ? new Date():document.getElementById('putInDate').value})" />
								<td width="25">发票类型</td>
								<td width="130"><select id="billType" style="width: 125px"
									name="billType">
										<option value="">所有</option>
										<option value="0">增值税专用发票</option>
										<option value="1">增值税普通发票</option>
								</select></td>
							</tr>
							<tr>
								<td width="25">发票代码</td>
								<td width="130"><input id="invoiceCode"
									class="tbl_query_text" name="invoiceCode" type="text"
									value="<s:property value='invoiceCode' />" /></td>

								<td width="25">发票起始号码</td>
								<td width="130"><input id="billStartNo"
									class="tbl_query_text" name="billStartNo" type="text"
									value="<s:property value='billStartNo' />" /></td>
								<td width="25">发票截止号码</td>
								<td width="130"><input id="billEndNo"
									class="tbl_query_text" name="billEndNo" type="text"
									value="<s:property value='billEndNo' />" /></td>
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
							<td style="text-align: left"><a
								href="addBillInventory.action" name="cmdFilter" id="cmdFilter">
									<img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png" />
									新增
							</a> <a href="#" name="cmdDistribute" id="cmdDistribute"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1001.png" />
									发布
							</a> <!-- 		<a href="#" onclick="openPaperInvoice('addBillInventory.action');" name="cmdFilter" id="cmdFilter"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>新增</a>   
				<a href="#" name="cmdDistribute" id="cmdDistribute"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1001.png"/>分发</a>  -->
								<a href="#"
								onclick="submitForm('createBillInventoryExcel.action');"
								name="cmdExcel" id="cmdExcel"><img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />导出</a>
							</td>
						</tr>
					</table>
					<div id="lessGridList1" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align: center"><input
									id="CheckAll" style="width: 13px; height: 13px;"
									type="checkbox" onClick="checkAll()" /></th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">报税机构</th>
								<th style="text-align: center">入库日期</th>
								<th style="text-align: center">发票类型</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票起始号码</th>
								<th style="text-align: center">发票终止号码</th>
								<th style="text-align: center">发票数量</th>
								<th style="text-align: center">已分发张数</th>
								<th style="text-align: center">未分发张数</th>
								<th style="text-align: center">操作状态</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">

								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td><input type="checkbox"
										style="width: 13px; height: 13px;" name="inventoryId"
										value="<s:property value="#iList.inventoryId"/>" /></td>
									<td align="center"><s:property value='#stuts.count' /></td>
									<td><s:property value="instName" /></td>
									<td><s:date name="putInDate" format="yyyy-MM-dd" /></td>
									<td><s:property
											value='@com.cjit.vms.stock.entity.stockUtil@getbilltype(billType)' /></td>
									<td><s:property value='billId' /></td>
									<td><s:property value='billStartNo' /></td>
									<td><s:property value='billEndNo' /></td>
									<td><s:property value='count' /></td>
									<td>0</td>
									<td>0</td>
									<td><a
										href="#.action?reqSource=billCancelApply&billId=<s:property value='inventoryId'/>">
											<img
											src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
											title="分发明细" style="border-width: 0px;" />
									</a> <!--  	<a href="javascript:void(0)" 
				onClick="OpenModalWindow('<%=webapp%>/#.action?billId=<s:property value='inventoryId'/>',650,400,'view') ">
			    <img src="<%=bopTheme%>/theme/default//img/jes/icon/view.png" title="分发明细" style="border-width: 0px;"/></a>
						--></td>
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















