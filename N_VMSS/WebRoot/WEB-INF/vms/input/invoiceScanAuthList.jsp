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
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js"
	type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/editview.js"></script>
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
	function reset(url) {
		//OpenModalWindow(url,650,350,true);
		location.href = url;
	}
	function submitForm(actionUrl) {
		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
		form.action = "listInvoiceScanAuth.action";
	}

	function deleteTransBatch(actionUrl) {

		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
		form.action = "listInvoiceScanAuth.action";
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
		var elTable = document.getElementById("lessGridList1"); //这里的page1 是包含表格的Div层的ID
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
</script>
<script type="text/javascript">
	$(function() {
		$(".cmdEditBt")
				.click(
						function() {
							$o_bill_id = $(this).attr("rel");
							$("#o_bill_id").val($o_bill_id);
							submitForm("<c:out value='${webapp}'/>/editInvoiceScanAuth.action");
						});
		$(".cmdViewBt")
				.click(
						function() {
							$o_bill_id = $(this).attr("rel");
							$("#o_bill_id").val($o_bill_id);
							alert($o_bill_id+",rel..................");
							alert($("#o_bill_id").val($o_bill_id)+",val............");
							submitForm("<c:out value='${webapp}'/>/viewInvoiceScanAuth.action");
						});

		$("#cmdAuthSubmitBtn").click(function() {
			$items = $("input[name='billId']:checked");

			if ($items.size() == 0) {
				alert("请选择您要认证数据！");
				return false;

			}
//			if ($items.size() > 1) {
//				alert("请选中一条数据进行认证！");
//				return false;

//			}
			//$item_datastatus = $("input[name='billStatu']");
			//$item_fapiaoType = $("input[name='billType']");
			var data="";
			
			var inventoryId = document.getElementsByName("billId");
			for (var i = 0; i < inventoryId.length; i++) {
				if (inventoryId[i].checked == true) {
					$("input[name='billStatu']").each(function(){
						 var datastatus = $(this).val();
						 datastatus=datastatus.split("-");
						 var val=datastatus[1]+"-"+datastatus[2]
						 if(val==inventoryId[i].value){
		      				if(datastatus[0]=="1"){
			         				alert("已经通过认证的数据，不可以进行重复认证！");//已认证
			    					return false;
		      				}else if(datastatus[0]=="2"){
		      					alert("是无需认证数据，不可以进行认证！");//无需认证
			    					return false;
		      				}else if(datastatus[0]=="3"){
		      					alert("是认证不通过数据，不可以进行重复认证！");//认证不通过
			    					return false;
		      				}else{
	//	      					if (confirm("确认认证提交吗？")) {
	//	      						submitForm("<c:out value='${webapp}'/>/authSubmit.action");
	//	      						}
								if(""==data){
									data=inventoryId[i].value;
								}else{
									data=data+" "+inventoryId[i].value;
								}
		      				}
		      			}
		   		    });
				}
			}
			
			
			if (confirm("确认认证提交吗？")) {
//				submitForm("<c:out value='${webapp}'/>/authSubmit.action");
				authsubmit(data);
			}
		});

		$("#cmdImportBt").click(
				function() {
					if (fucCheckNull(document.getElementById("theFile"),
							"导入文件不能为空") == false) {
						return false;
					}
					submitForm('importInvoiceScanAuth.action');
				});
	});
	
	function authsubmit(data){
		 $.ajax({
		         url: 'authSubmit.action',
		         type: 'POST',
		         async: false,
		         data: {selects:data},
		         dataType: 'text',
		         timeout: 1000,
		         error: function () {
		             return "认证成功";
		         },
		         success: function (result) {
		        	 return "认证成功";
		        	 
		         }
		     });
		 submitForm('listInvoiceScanAuth.action');
	}
</script>
<script type="text/javascript">
function cmdAuthSubmitBtnNo(){
	$items = $("input[name='billId']:checked");
	if ($items.size() == 0) {
		alert("请选择您要认证数据！");
		return false;
	}
	if ($items.size() > 1) {
		alert("请选中一条数据进行认证！");
		return false;
	}
	
	$("input[name='billStatu']").each(function(){
		 var datastatus = $(this).val();
		 datastatus=datastatus.split("-");
		 var val=datastatus[1]+"-"+datastatus[2]
			if(val==$items.val()){
				if(datastatus[0]=="1"){
    				alert("已经通过认证的数据，不可以进行重复认证！");//已认证
					return false;
				}else if(datastatus[0]=="2"){
					alert("是无需认证数据，不可以进行认证！");//无需认证
					return false;
				}else if(datastatus[0]=="3"){
					alert("是认证不通过数据，不可以进行重复认证！");//认证不通过
					return false;
				}else{
					if (confirm("确认认证提交吗？")) {
						submitForm("<c:out value='${webapp}'/>/authSubmitRZBTG.action");
						}
				}
			}
	    });
}

</script>
</head>
<body>
	<form id="main"
		action="<c:out value='${webapp}'/>/listInvoiceScanAuth.action"
		method="post" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">进项税管理</span> <span
							class="current_status_submenu">进项管理</span> <span
							class="current_status_submenu">扫描证认</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0"
							width="100%">
							<tr>
								<td>开票日期</td>
								<td><input type="text" style="width: 110px"
									class="tbl_query_text" name="inputInfo.transBeginDate"
									id="inputInfo.transBeginDate"
									value="<s:property value='inputInfo.transBeginDate'/>"
									class="tbl_query_time"
									onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" /> &nbsp;-&nbsp;
									<input type="text" style="width: 110px" class="tbl_query_text"
									name="inputInfo.transEndDate" id="inputInfo.transEndDate"
									value="<s:property value='inputInfo.transEndDate'/>"
									class="tbl_query_time"
									onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" /></td>
								<td>报税机构</td>
								<td style="text-align: left; width: 14%;"><input
									type="text" class="tbl_query_text" id="inst_Name"
									name="inputInfo.billInst" value='' onclick="setOrg(this);"
									readonly="readonly"></td>
								<td>供应商名称</td>
								<td><input type="text" class="tbl_query_text"
									name="inputInfo.name" id="inputInfo.name"
									value="<s:property value='inputInfo.name'/>" maxlength="100" />
								</td>

							</tr>
							<tr>
								<td>发票代码</td>
								<td><input type="text" class="tbl_query_text"
									name="inputInfo.billId" id="inputInfo.billId"
									value="<s:property value='inputInfo.billId'/>" maxlength="12"
									onkeypress="checkkey(value);" /></td>
								<td>发票号码</td>
								<td><input type="text" class="tbl_query_text"
									name="inputInfo.billCode" id="inputInfo.billCode"
									value="<s:property value='inputInfo.billCode'/>" maxlength="8"
									onkeypress="checkkey(value);" /></td>
								<td>发票种类</td>
								<td><s:select id="inputInfo.billType"
										name="inputInfo.billType"
										list="#{'0':'增值税专用发票','1':'增值税普通发票','2':'通行费发票'}" headerKey=''
										headerValue="全部" listKey='key' listValue='value'
										cssClass="tbl_query_text" /></td>
							</tr>
							<tr>
								<td>发票状态</td>
								<td><s:select id="inputInfo.billStatu"
										name="inputInfo.billStatu"
										list="#{'1':'已认证','0':'未认证','2':'无需认证','3':'认证不通过'}"
										headerKey='' headerValue="全部" listKey='key' listValue='value'
										cssClass="tbl_query_text" /></td>
								<td colspan=2><input type="button" class="tbl_query_button"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'"
									onclick="submitForm('listInvoiceScanAuth.action');"
									name="cmdQueryBtn" value="查询" id="cmdQueryBtn" /></td>
								<td colspan=2><input type="button" class="tbl_query_button"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'"
									onclick="submitForm('dataUpdate.action');"
									name="dataUpdate" value="数据更新" id="dataUpdate" /></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left" width="168"><a href="javascript:;"
								id="cmdAuthSubmitBtn" name="cmdAuthSubmitBtn"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1036.png" />
									发票认证
							</a></td>
							<td><a href="javascript:;" onclick="cmdAuthSubmitBtnNo()"
								id="cmdAuthSubmitBtnNo" name="cmdAuthSubmitBtnNo"><img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1037.png" />
									认证不通过 </a></td>
							<td width="234"><input type="file" name="theFile"
								id="theFile" style="height: 26px;" /></td>
							<td><a href="javascript:;" id="cmdImportBt"
								name="cmdImportBt"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png" />
									导入
							</a> <a href="javascript:void();"
								onclick="submitForm('exportInvoiceScanAuth.action')"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
							</a></td>
						</tr>
					</table>
					<div id="lessGridList3" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align: center"><input
									id="CheckAll" style="width: 13px; height: 13px;"
									type="checkbox" onClick="checkAll(this,'billId')" /></th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票号码</th>
								<th style="text-align: center">开票日期</th>
								<!-- <th style="text-align: center">所属机构</th> -->
								<th style="text-align: center">金额</th>
								<th style="text-align: center">税额</th>
								<th style="text-align: center">价税合计</th>
								<th style="text-align: center">发票种类</th>
								<!-- <th style="text-align: center">用途</th> -->
								<th style="text-align: center">供应商名称</th>
								<th style="text-align: center">供应商纳税人识别号</th>
								<th style="text-align: center;">发票状态</th>
								<th style="text-align: center;">认证日期</th>
								<th style="text-align: center;">操作</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">
								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td>
										<!-- disabled --> <s:if test='#iList.billStatu!="0"'>
											<input type="checkbox" style="width: 13px; height: 13px;"
												name="billId"
												value="<s:property value="billId"/>-<s:property value="billCode"/>"
												disabled />
										</s:if>
										<s:else>
											<input type="checkbox" style="width: 13px; height: 13px;"
												name="billId"
												value="<s:property value="billId"/>-<s:property value="billCode"/>" />
										</s:else>
									</td>
									<td align="center"><s:property value='billId' /></td>
									<td align="center"><s:property value='billCode' /></td>
									<td><s:property value='billDate' /></td>
									<%-- <td><s:property value="billInst" /></td> --%>
									<td><s:property
											value='@com.cjit.common.util.NumberUtils@format(amt," ", 2)' />
									</td>
									<td><s:property
											value='@com.cjit.common.util.NumberUtils@format(tax," ", 2)' />
									</td>
									<td><s:property
											value='@com.cjit.common.util.NumberUtils@format(sumTax," ", 2)' />
									</td>
									<td align="center"><input type="hidden" name="billType"
										value="<s:property value='billType' />" /> <s:property
											value='mapVatType[billType]' /></td>
									<%-- <td><s:property
											value='@com.cjit.vms.trans.util.JSPUtil@getValue(@com.cjit.vms.input.model.InputInfoUtil@purposeMap,purpose)' />
									</td> --%>
									<td><s:property value='name' /></td>
									<td><s:property value="taxNo" /></td>
									<td><input type="hidden" name="billStatu"
										value="<s:property value='billStatu' />-<s:property value="billId"/>-<s:property value="billCode"/>" />
										<s:property
											value="@com.cjit.vms.trans.util.DataUtil@getbillStatuIn(billStatu)" />
									</td>
									<td><s:property value='dealNo' /></td>
									<td><a href="javascript:;" class="cmdViewBt"
										rel="<s:property value="billId"/>-<s:property value="billCode"/>">
											<img
											src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
											title="查看" style="border-width: 0px;" />
									</a></td>
								</tr>
							</s:iterator>
							<input type="hidden" name="currentPage"
								value="<s:property value="paginationList.currentPage"/>" />
							<input type="hidden" name="o_bill_id" id="o_bill_id" value="" />
						</table>
					</div>
					<div id="anpBoud" align="Right" style="width: 100%;background: #abcdef">
					hello world!!!!
						<table width="100%" cellspacing="0" border="0">
							<tr>行
								<td align="right"><s:component template="pagediv" />你好！！！！</td>
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