<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/page/include.jsp"%>
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
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/lhb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript">
	// [查询]按钮  [导出]按钮
	function submitForm(actionUrl) {
		submitAction(document.forms[0], actionUrl);
		document.forms[0].action = "billPreInvoice.action?paginationList.showCount="
				+ "false";
	}

	function regTest() {
		var flag = true;
		var customerIds = document.getElementsByName("selectTransIds");
		for (i = 0; i < customerIds.length; i++) {
			if (customerIds[i].checked) {
				$("#" + customerIds[i].value).find("td").each(
						function(index, element) {
							if (index == 3) {
								alert(/^[\u4e00-\u9fa5]{0,}$/.test($.trim($(
										this).text())));
								if (!/^[\u4e00-\u9fa5]{0,}$/.test($
										.trim($(this).text()))
										|| $.trim($(this).text()) == null) {
									flag = false;
									return alert("客户名称必须为中文,请修改!");
									;
								}
							}
						});
			}
		}
		return flag;
	}
	// [提交/删除]按钮
	function submitBill(value) {
		var t = "";
		var msg = "";
		var res = "";
		var resError = "";
		if (value == 'delete') {
			msg = "是否确认删除?";
			res = "删除成功";
			resError = "删除失败";
		} else {
			if (!regTest()) {
				return;
			}
			msg = "请确认是否提交？";
			res = "提交成功等待审核!";
			resError = "提交失败,请重新提交!";
		}
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
		if (confirm(msg)) {
			if (value == 'delete') {
				$.ajax({
					url : 'deletePreInvoiceList.action',
					type : 'POST',
					async : false,
					data : {
						billId : t.substring(0, t.length - 1)
					},
					dataType : 'text',
					error : function() {
						return false;
					},
					success : function(result) {
						if (result == 'Y') {
							alert(res);
							document.forms[0].submit();
						} else if (result == 'N') {
							alert(resError);
						}
					}
				});
			} else {
				$.ajax({
					url : 'commitBillInfo.action',
					type : 'POST',
					async : false,
					data : {
						billId : t.substring(0, t.length - 1)
					},
					dataType : 'text',
					error : function() {
						return false;
					},
					success : function(result) {
						if (result == 'Y') {
							alert(res);
							document.forms[0].submit();
						} else if (result == 'N') {
							alert(resError);
						}
					}
				});
			}
			document.forms[0].action = "billPreInvoice.action?paginationList.showCount="
					+ "false";
		} else {
			return;
		}
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
		}
	}
	function exportCancelBill() {
		submitAction(document.forms[0], "billListOpenToExcel.action");
		document.forms[0].action = "billPreInvoice.action?paginationList.showCount="
				+ "false";
	}
	
</script>
<script type="text/javascript">
	function importFJ(){
		var selectBillIds =document.getElementsByName("selectBillIds");
		 var count = 0 ;
		   var index = "";
		   for(var i=0 ;i<selectBillIds.length ;i++){
			   if(true == selectBillIds[i].checked){
				   count++;
			   	   index=i;
			   }
		   }
		   
		   if(count>1){
			   alert("选择单条记录!")
		   }else if(count==0){
			   alert("请选择记录!")
		   }else{
			   var fileName = document.getElementsByName("cherNumhidden")[index].value;
				if(true==checkfile()){
					document.forms[0].action="<%=webapp%>/billPreApplyupload.action?fileName="+fileName
					document.forms[0].submit();
				}
		   }
	}
   function buttonAdd(){
	    var addfile=document.getElementById("cmdRollBackBtnAdd");
		var file=document.createElement("input");
		file.setAttribute("type", "file");
		file.setAttribute("name", "theFile");
		file.setAttribute("size", "30");
		file.setAttribute("style", "height:26px; line-height:30px;");
		addfile.parentNode.insertBefore(file,addfile); 
   }
   
   function buttondown(){
	   var fileId = $(document.getElementsByName("theFile"));
	   if(fileId.length==1){
		   alert("至少上传一张附件!")
	   }else{
		  var obj=document.getElementsByName("theFile");
		  var prop =obj[obj.length-1].parentNode ;
		  prop.removeChild(obj[obj.length-1]);
		   
	   }
   }
   
   function checkfile(){
		 var count = 0;
		 var colum = 0;
		 var fileId = $(document.getElementsByName("theFile"));
			fileId.each(function(){
				var file = $(this).val();
				if(file.length > 0){
					if(!/\.(jpg|jpeg|png|pdf)$/.test(file)){
						count=count+1;
						return false ;
					}
				}else{
					colum=colum+1 ;
					return false ;
				} 
			}); 
			if(colum>0){
				alert("请上传文件 ！");
				return false ;
			}else{
				if(count>0){
					alert("文件格式不对，请上传png/jpeg/pdf文件 ！");
					return false ;
				}
			}
			return true ;
	 }
</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="billPreInvoice.action"
		id="Form1" enctype="multipart/form-data">
		<%
			String currMonth = (String) request.getAttribute("currMonth");
		%>
		<input type="hidden" name="currMonth" id="currMonth"
			value="<%=currMonth%>" /> <input type="hidden" name="reasionInfo"
			id="reasionInfo" value="" />
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition"><s:component
						template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">开票管理</span> <span
							class="current_status_submenu">手工开票</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%"
							border="0">
							<tr>
								<td align="left">申请开票日期</td>
								<td><input class="tbl_query_time"
									id="billCondiction.applyDate" type="text"
									name="billCondiction.applyDate"
									value="<s:property value='billCondiction.applyDate'/>"
									onfocus="WdatePicker()" size='11' /></td>
								<td align="left">客户名称</td>
								<td><input type="text" class="tbl_query_text"
									name="billCondiction.customerName"
									value="<s:property value='billCondiction.customerName'/>" /></td>
								<s:if test='configCustomerFlag.equals("KBC")'>
									<td align="left">客户号</td>
									<td><input type="text" class="tbl_query_text"
										name="billCondiction.customerId"
										value="<s:property value='billCondiction.customerId'/>" /></td>
								</s:if>
								<s:else>
									<td align="left">客户纳税人识别号</td>
									<td><input type="text" class="tbl_query_text"
										name="billCondiction.customerTaxno"
										value="<s:property value='billCondiction.customerTaxno'/>" />
									</td>
								</s:else>
							</tr>
							<tr>
								<td align="left">数据来源</td>
								<td>
									<!-- // 票据审核画面case追加      at lee start  --> <select
									name="billCondiction.isHandiwork" style="width: 133px">
										<option value=""
											<s:if test='billCondiction.isHandiwork==""'>selected</s:if>
											<s:else></s:else>>全部</option>
										<option value="3"
											<s:if test='billCondiction.isHandiwork=="3"'>selected</s:if>
											<s:else></s:else>>人工开票</option>
								</select> <!--  // 票据审核画面case追加      at lee start -->
								</td>
								<td align="left">开具类型</td>
								<td><select name="billCondiction.issueType"
									style="width: 133px">
										<option value=""
											<s:if test='billCondiction.issueType==""'>selected</s:if>
											<s:else></s:else>>全部</option>
										<option value="1"
											<s:if test='billCondiction.issueType=="1"'>selected</s:if>
											<s:else></s:else>>单笔</option>
										<option value="2"
											<s:if test='billCondiction.issueType=="2"'>selected</s:if>
											<s:else></s:else>>合并</option>
										<option value="3"
											<s:if test='billCondiction.issueType=="3"'>selected</s:if>
											<s:else></s:else>>拆分</option>
								</select></td>
								<td align="left">发票类型</td>
								<td><select name="billCondiction.fapiaoType"
									style="width: 133px">
										<option value=""
											<s:if test='billCondiction.fapiaoType==""'>selected</s:if>
											<s:else></s:else>>全部</option>
										<option value="0"
											<s:if test='billCondiction.fapiaoType=="0"'>selected</s:if>
											<s:else></s:else>>增值税专用发票</option>
										<option value="1"
											<s:if test='billCondiction.fapiaoType=="1"'>selected</s:if>
											<s:else></s:else>>增值税普通发票</option>
								</select></td>
								<td style="width: 80px;" align="right"><input type="button"
									class="tbl_query_button" value="查询"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="BtnView"
									id="BtnView" onclick="submitForm('billPreInvoice.action')" />
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left"><a href="billPreInvoiceForm.action"
								name="btAdd"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png" />
									新增
							</a> <a href="#" name="btDelete" id="btDelete"
								onclick="submitBill('delete')"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png" />
									删除
							</a> <!-- a href="#" name="BtnView" id="BtnView" onclick="exportCancelBill();"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a -->
								<a href="#" name="btSubmit" id="btSubmit"
								onclick="submitBill('submit')"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1016.png" />
									提交
							</a> <s:file id="theFile" name="theFile" size="30"
									style="height:26px; line-height:30px; "></s:file> <input
								type="button" id="cmdRollBackBtnAdd" class="tbl_query_button"
								onMouseMove="this.className='tbl_query_button_on'"
								onMouseOut="this.className='tbl_query_button'" value="新增"
								onclick="buttonAdd()" /> <input type="button"
								id="cmdRollBackBtndown" class="tbl_query_button"
								onMouseMove="this.className='tbl_query_button_on'"
								onMouseOut="this.className='tbl_query_button'" value="删除"
								onclick="buttondown()" /> <a href="javascript:;"
								id="cmdImportBt" onclick="importFJ()" name="cmdImportBt"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png" />
									上传
							</a></td>
						</tr>
					</table>
					<div id="lessGridList4"
						style="width: 100%; height: 360px; overflow: auto;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align: center"><input
									style="width: 13px; height: 13px;" id="CheckAll"
									type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
								</th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">申请开票日期</th>
								<th style="text-align: center">客户名称</th>
								<s:if test='configCustomerFlag.equals("KBC")'>
									<th style="text-align: center">客户号</th>
								</s:if>
								<s:else>
									<th style="text-align: center">客户纳税人识别号</th>
								</s:else>
								<th style="text-align: center">合计金额</th>
								<th style="text-align: center">合计税额</th>
								<th style="text-align: center">价税合计</th>
								<th style="text-align: center">数据来源</th>
								<th style="text-align: center">开具类型</th>
								<th style="text-align: center">发票类型</th>
								<th style="text-align: center">状态</th>
								<th style="text-align: center">操作</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">
								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>"
									id="<s:property value="#iList.billId"/>">
									<td><input type="checkbox"
										style="width: 13px; height: 13px;" name="selectBillIds"
										value="<s:property value="#iList.billId"/>" /> <!-- SG2222-2017-05-16 -->
										<input type="hidden" name="cherNumhidden"
										value="<s:property value='billId' />" /></td>
									<td align="center"><s:property value='#stuts.count' /></td>
									<td align="center"><s:property value="applyDate" /></td>
									<td align="center"><s:property value="customerName" /></td>
									<s:if test='configCustomerFlag.equals("KBC")'>
										<td align="center"><s:property value="customerId" /></td>
									</s:if>
									<s:else>
										<td align="center"><s:property value="customerTaxno" />
										</td>
									</s:else>
									<td align="center"><s:if test="amtSum==''||amtSum==null">0</s:if>
										<s:else>
											<s:property value="amtSum" />
										</s:else></td>
									<td align="center"><s:if
											test="taxAmtSum==''||taxAmtSum==null">0</s:if> <s:else>
											<s:property value="taxAmtSum" />
										</s:else></td>
									<td align="center"><s:if test="sumAmt==''||sumAmt==null">0</s:if>
										<s:else>
											<s:property value="sumAmt" />
										</s:else></td>
									<td align="center"><s:property
											value="@com.cjit.vms.trans.util.DataUtil@getIsHandiworkCH(isHandiwork)" />
									</td>
									<td align="center">
										<%-- <s:property value="issueType" /> --%> <s:if
											test="issueType==1">单笔</s:if> <s:if test="issueType==2">合并</s:if>
										<s:if test="issueType==3">拆分</s:if> <%-- <s:property value="@com.cjit.vms.trans.util.DataUtil@getIssueTypeName(issueType)" /> --%>
									</td>
									<td align="center">
										<%-- <s:property value="fapiaoType" /> --%> <s:if
											test="fapiaoType==0">增值税专用发票</s:if> <s:if
											test="fapiaoType==1">增值税普通发票</s:if> <%-- <s:property value="@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(fapiaoType)" /> --%>
									</td>
									<td align="center">
										<%-- <s:property value="dataStatus" /> --%> <s:if
											test="datastatus==1">添加待提交</s:if> <s:if test="datastatus==-1">审核拒绝</s:if>
										<%-- 	<s:property value="@com.cjit.vms.trans.util.DataUtil@getDataStatusCH(datastatus,'BILL')" /> --%>
									</td>
									<td><s:if test='datastatus=="1"'>
											<a
												href="editOpenBill.action?curPage=<s:property value='paginationList.currentPage'/>&instCode=<s:property value='instCode'/>&billInfo.billId=<s:property value="billId"/>&type=open&customerId=<s:property value="customerId"/>">
												<img
												src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png"
												title="编辑票据" style="border-width: 0px;" />
											</a>
										</s:if> <a href="javascript:void(0);"
										onClick="OpenModalWindowSubmit('viewImgFromBillEdit.action?billId=<s:property value="billId"/>',1000,650,true)">
											<img
											src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png"
											title="查看票样" style="border-width: 0px;" />
									</a></td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right"
						style="overflow: auto; width: 100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<input type="hidden" name="paginationList.showCount"
									value="false" />
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
					</div></td>
			</tr>
		</table>
	</form>
</body>
</html>