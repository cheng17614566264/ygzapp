<!-- file: <%=request.getRequestURI()%> -->
<%@page import="java.util.Map"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@page import="com.cjit.vms.trans.util.DataUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
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
	//MetLife版本 Check校验
	function checktransToEachMetlife(chkBoxName) {
		var chkBoexes = document.getElementsByName(chkBoxName);
		for (i = 0; i < chkBoexes.length; i++) {
			if (chkBoexes[i].checked) {
				//var accAmt = $(chkBoexes[i]).closest("tr").find("[name=amt]").text().replace(",","").trim();
				var accAmt = $.trim($(chkBoexes[i]).closest("tr").find(
						"[name=amt]").text().replace(",", ""))
				if (parseFloat(accAmt) < 0) {
					return "负值交易不能单独开票";
				}
				return true;
			}
		}
		return false;
	}
	function checktransToOneMetlife(chkBoxName) {
		var amt = 0;
		var cherNum = "";
		var feeTyp = "";
		var leng = 0;
		var dj = "";
		var array = new Array();
		var chkBoexes = document.getElementsByName(chkBoxName);
		for (i = 0; i < chkBoexes.length; i++) {
			if (chkBoexes[i].checked) {
				//dj = $(chkBoexes[i]).closest("tr").find("[name='feeTyp']").text().trim();
				dj = $.trim($(chkBoexes[i]).closest("tr").find(
						"[name='feeTyp']").text());
				leng++;
				feeTyp += $.trim($(chkBoexes[i]).closest("tr").find(
						"[name=feeTyp]").text())
						+ ",";
				amt += parseFloat($.trim($(chkBoexes[i]).closest("tr").find(
						"[name=amt]").text().replace(",", "")));
				cherNum += $.trim($(chkBoexes[i]).closest("tr").find(
						"[name=cherNum]").text())
						+ ",";
			}
		}
		var p = cherNum.substr(0, cherNum.length - 1).split(",");
		for (var i = 0; i < p.length; i++) {
			if (i > 0) {
				if (p[0] != p[i]) {
					return "不同保单不能合并开票";
				}
			}
		}

		var f = feeTyp.substr(0, feeTyp.length - 1).split(",");
		for (var i = 0; i < f.length; i++) {
			if (i > 0) {
				if (f[0] != f[i]) {
					return "不同费用类型不能合并开票";
				}
			}
		}

		if (amt < 0) {
			return "合计为负值，不能开票";
		} else {
			if (leng > 0) {
				if (dj == '015') {
					if (confirm("请确认发票金额（ " + amt + " ）与实际收费是否一致")) {
						return true;
					} else {
						return null;
					}
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
	}

	jQuery.extend({
		checkTransDate : function() {
			//提示逻辑
			return true;
		}
	});

	// [查询]按钮
	function submit() {
		document.forms[0].submit();
	}
	//检查犹豫期
	function checkYouYuQi(chkBoxName){
		var message="";
		var chkBoexes= document.getElementsByName(chkBoxName);
		var hesitatePeriods=document.getElementsByName("hesitatePeriod");
		for(i=0;i<chkBoexes.length;i++){
			if(chkBoexes[i].checked){
				if (hesitatePeriods[i].innerText.trim()=="是") {
					var j=i+1;
					message=message+j+",";
				}
			}
		}
		return message;
	}
	// [开票]按钮
	function transToEachBill() {
		
		if (checkChkBoxesSelected("selectTransIds")) {
			var message=checkYouYuQi("selectTransIds");
			if (message.length>0) {
				message=message.substring(0, message.length-1);
				message="第"+message+"行数据在犹豫期内，是否继续？"
				var r=confirm(message);
				if (r==false) {
					return false;
				}
			}
			submitAction(document.forms[0],
					"transToEachBill.action?paginationList.showCount="
							+ "false");
			document.forms[0].action = "listTrans.action?paginationList.showCount="
					+ "false";
		} else {
			alert("请选择交易记录！");
		}
	}
	function transtoCustmer() {
		if (!confirm("确定对选中交易进行关联客户吗？")) {
			return false;
		}
		var flag = false;
		var count = 1;
		var checkboxes = document.getElementsByName("selectTransIds");
		var selectedIds = "";
		var customerIds = document.getElementsByName("selectCustomers");
		var customerId = "";
		var customerTypes = document.getElementsByName("selectCustomerTypes");
		var selectDataStatuses = document
				.getElementsByName("selectDataStatuses");
		var customerType = "";
		var selectIncome = document.getElementsByName("selectIncome");
		var selectBalance = document.getElementsByName("selectBalance");
		for (var i = 0; i < checkboxes.length; i++) {
			var box = checkboxes[i];
			if (box.checked) {
				flag = true;
				if (count == 1) {
					customerId = customerIds[i].value;
					customerType = customerTypes[i].value;
				}
				if (i == checkboxes.length - 1) {
					selectedIds = selectedIds + box.value;
				} else {
					selectedIds = selectedIds + box.value + ",";
				}
				if (customerId != customerIds[i].value) {
					alert("请选择相同客户所发生的交易");
					return false;
				}
				if (customerType != customerTypes[i].value) {
					alert("请选择相同客户类型的交易");
					return false;
				}
				count = count + 1;
			}
		}
		if (!flag) {
			alert("请选择至少一条交易记录");
			return false;
		}
		var newUrl = "connectCustomer.action?transIds=" + selectedIds
				+ "&orgCustomerId=" + customerId + "&orgCustomerType="
				+ customerType;
		submitAction(document.forms[0], newUrl);
		document.forms[0].action = "listTrans.action?paginationList.showCount="
				+ "false";
	}
	
	
	

	//添加申请方法通过接口向核心获取数据
	function applyInvoice() {
		var busCode = $('#business_code').val();

		if (busCode == null || busCode == "") {
			alert("请填写保单号1");
			return;
		}

		$('#business_code2').val(busCode);

		//首先根据业务编号查询开票数据中是否存在已开票的数据
		document.forms[1].submit();
	}

	function checkChkBoxesSelectedOne(chkBoexName) {
		var j = 0;
		var chkBoexes = document.getElementsByName(chkBoexName);
		for (i = 0; i < chkBoexes.length; i++) {
			if (chkBoexes[i].checked) {
				j++;
			}
		}
		return j;
	}

	function transToOneBill() {
		var selectTransIds = "";
		var selectCustomers = "";
		var cherNum = "";
		var cherNums = document.getElementsByName("selectCherNum");
		var transIds = document.getElementsByName("selectTransIds");
		var customers = document.getElementsByName("selectCustomers");
		for (var i = 0; i < customers.length; i++) {
			if (transIds[i].checked) {
				selectCustomers += customers[i].value + ",";
			}
		}
		for (var i = 0; i < transIds.length; i++) {
			if (transIds[i].checked) {
				selectTransIds += transIds[i].value + ",";
			}
		}

		$
				.ajax({
					url : "selectTransToOneBill.action",
					type : 'POST',
					async : false,
					data : {
						selectTransIds : selectTransIds.substring(0,
								selectTransIds.length - 1),
						selectCustomers : selectCustomers.substring(0,
								selectCustomers.length - 1)
					},
					dataType : "json",
					error : function() {
						return false;
					},
					success : function(result) {
						if (result.checkFlag == "N") {
							alert(result.checkResultMsg);
							return;
						}
						if (result.checkFlag == "Y") {
							if (confirm(result.checkResultMsg)) {
								submitAction(document.forms[0],
										"transToOneBill.action?paginationList.showCount="
												+ "false");
								document.forms[0].action = "listTrans.action?paginationList.showCount="
										+ "false";
							}
						}

					}
				});
	}
	// [合并开票]按钮
	function transToOneBills() {
		
		var flag = checktransToOneMetlife("selectTransIds");
		if (flag == true) {
			var message=checkYouYuQi("selectTransIds");
			if (message.length>0) {
				message=message.substring(0, message.length-1);
				message="第"+message+"行数据在犹豫期内，是否继续？"
				var r=confirm(message);
				if (r==false) {
					return false;
				}
			}
			var transIds = document.Form1.selectTransIds;
			var customers = document.Form1.selectCustomers;
			var fapiaoTypes = document.Form1.selectFapiaoTypes;
			//保单号
			var cherNums = document.getElementsByName("selectCherNum");
			var cherNum = "";
			var customer = "";
			var fapiaoType = "";
			var selectTransIds = "";
			var selectPremTerm = document.Form1.selectPremTerm;
			var premTermStr = "";
			if (selectPremTerm.length > 0) {
				for (var i = 0; i < selectPremTerm.length; i++) {
					if (document.Form1.selectTransIds[i].checked) {
						premTermStr += document.Form1.selectPremTerm[i].value
								+ ",";
					}
				}
				var premTermArray = premTermStr.substring(0,
						premTermStr.length - 1).split(",");
				for (var i = 0; i < premTermArray.length; i++) {
					for (var j = i + 1; j < premTermArray.length; j++) {
						if (premTermArray[i] > premTermArray[j]) {
							var temp = premTermArray[i];
							premTermArray[i] = premTermArray[j];
							premTermArray[j] = temp;
						}
					}
				}
			}
			$("#premTermArray").val(premTermArray);
			if (!isNaN(transIds.length)) {
				for (var i = 0; i < transIds.length; i++) {
					if (document.Form1.selectTransIds[i].checked) {
						selectTransIds += document.Form1.selectCustomers[i].value
								+ ",";
						if (customer == "") {
							customer = document.Form1.selectCustomers[i].value;
							cherNum = cherNums[i].value;
						} else if (customer != document.Form1.selectCustomers[i].value) {
							alert("所选交易属于不同客户，不能合并开票！");
							return false;
						} else if (cherNum != cherNums[i].value) {
							alert("所选交易的保单号不相同，不能合并开票");
							return false;
						}
						if (fapiaoType == "") {
							fapiaoType = document.Form1.selectFapiaoTypes[i].value;
						} else if (fapiaoType != document.Form1.selectFapiaoTypes[i].value) {
							alert("所选交易之发票类型不一致，不能合并开票！");
							return false;
						}
					}
				}
			} else {
				// 列表中仅一笔可供选交易记录
			}
			transToOneBill()
		} else {
			if (flag != false) {
				if (flag != null) {
					alert(flag);
				}
			} else {
				alert("请选择交易记录！");
			}
		}
	}
	// [拆分开票]按钮
	function transToManyBill() {
		var message=checkYouYuQi("selectTransIds");
		if (message.length>0) {
			message=message.substring(0, message.length-1);
			message="第"+message+"行数据在犹豫内，是否继续？"
			var r=confirm(message);
			if (r==false) {
				return false;
			}
		}
		if (checkChkBoxesSelected("selectTransIds")) {
			var transIds = document.Form1.selectTransIds;
			var selectTransId = "";
			if (!isNaN(transIds.length)) {
				var selectCount = 0;
				for (var i = 0; i < transIds.length; i++) {
					if (document.Form1.selectTransIds[i].checked) {
						if (selectCount == 1) {
							alert("只可选择一笔交易进行拆分！");
							return false;
						} else {
							selectCount = selectCount + 1;
							selectTransId = document.Form1.selectTransIds[i].value;
						}
					}
				}
			} else {
				selectTransId = document.Form1.selectTransIds.value;
			}

			$
					.ajax({
						url : "selectTransToOneBill.action",
						type : 'POST',
						async : false,
						data : {
							selectTransIds : selectTransId
						},
						dataType : "json",
						error : function() {
							return false;
						},
						success : function(result) {
							//alert(result.checkFlag);
							if (result.checkFlag == "N") {
								alert(result.checkResultMsg);
								return;
							}
							if (result.checkFlag == "Y") {
								var newUrl = "splitTrans.action?selectTransIds="
										+ selectTransId;
								OpenModalWindowSubmit(newUrl, 600, 400, true);
								document.forms[0].action = "listTrans.action?paginationList.showCount="
										+ "false";
							}

						}
					});

		} else {
			alert("请选择交易记录！");
		}
	}
	//数据同步按钮
	/* 
	修改
	日期：2018-09-03
	作者：刘俊杰
	说明：将根据保单号同步核心数据、同步所有核心数据整合到同一个按钮上
	function synchData() { */
	function batchRun(){
		var startDate = $.trim($("#startDate").val());
		var endDate = $.trim($("#endDate").val());
		var repNum = $.trim($("#repnum").val());
		var cherNum = $.trim($("#cherNum").val());
		var customerName = $.trim($("#customerName").val());
		var batchNo=$.trim($("#batchNo").val());
		var isYK=$("#isYK").val();
		/* if (!(startDate != "" || endDate != "" || repNum != "" || cherNum != "" || customerName != ""||batchNo!="")) {
			alert("请填写需要同步的数据");
			return;
		} */
		$.ajax({
			url : 'synchTransInfo.action',// 跳转到 action    
			type : 'post',
			data : {
				repNum : repNum,
				cherNum : cherNum,
				startDate : startDate,
				endDate : endDate,
				customerName : customerName,
				isYK:isYK,
				batchNo:batchNo
			},
			dataType : 'json',
			success : function(ajaxReturn) {
				var d = eval("("+ajaxReturn+")");
				alert(d.message);
				//刷新页面
				document.forms[0].action = "listTrans.action";
				document.forms[0].submit();
			},
			error : function(ajaxReturn) {
				var d = eval("("+ajaxReturn+")");
				alert(d.message);
				//刷新页面
				document.forms[0].action = "listTrans.action";
				document.forms[0].submit();
			}
		});
		

	}
	//【删除】按钮 
	function transtoDelete() {
		if (checkChkBoxesSelected("selectTransIds")) {
			var transIds = document.getElementsByName("selectTransIds");
			var selectTransIds = "";
			//var dataStatus = document.Form1.selectDataStatuses;
			var dataStatus = document.getElementsByName("selectDataStatuses");
			if (transIds.length > 0) {
				for (var i = 0; i < transIds.length; i++) {
					if (transIds[i].checked) {
						if (dataStatus[i].value == '1') {
							selectTransIds += transIds[i].value + ",";
						} else {
							alert("你选择的记录中有数据处于开票流程中，不能删除");
							return;
						}
					}
				}
				if (selectTransIds != "") {
					if (confirm("确定删除该条记录？")) {
						$.ajax({
							url : "deleteTransData.action",
							type : 'POST',
							async : false,
							data : {
								selectTransIds : selectTransIds.substr(0,
										selectTransIds.length - 1)
							},
							dataType : "text",
							error : function() {
								return false;
							},
							success : function(result) {
								if (result == 'Y') {
									alert("删除成功");
									document.forms[0].submit();
									return;
								} else if (result == 'N') {
									alert("对不起,删除过程发生错误,未能删除成功");
									return;
								}
							}
						});
					}
				}
			}
		} else {
			alert("请选择要删除的数据");
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
	// 刷新本页面
	function splitTransSuccess() {
		alert("拆分开票保存成功！");
		document.forms[0].submit();
	}

	// [交易冲账]按钮
	function transToReverse() {
		if (checkChkBoxesSelected("selectTransIds")) {
			var transIds = document.Form1.selectTransIds;
			var customers = document.Form1.selectCustomers;
			if (!isNaN(transIds.length)) {
				var customer = "";
				var transId = "";
				for (var i = 0; i < transIds.length; i++) {
					if (document.Form1.selectTransIds[i].checked) {
						if (customer == "") {
							customer = document.Form1.selectCustomers[i].value;
							transId = document.Form1.selectTransIds[i].value
									.substring(0, 44);
						} else if (customer != document.Form1.selectCustomers[i].value) {
							alert("所选交易属于不同客户，不能一同冲账！");
							return false;
						} else {
							var thisTransId = document.Form1.selectTransIds[i].value
									.substring(0, 44);
							if (transId != thisTransId) {
								alert("所选交易流水号不通，不能一同冲账！");
								return false;
							}
						}
					}
				}
			}
			if (!confirm("确定对选中交易进行冲账？")) {
				return false;
			}
			submitAction(document.forms[0],
					"transToReverse.action?paginationList.showCount=" + "false");
			document.forms[0].action = "listTrans.action?paginationList.showCount="
					+ "false";
		} else {
			alert("请选择交易记录！");
		}
	}

	$(function() {
		function showMsg() {
			var message = $("#message").val();
			message = message.replace(/^\s*/, '').replace(/\s*$/, '');
			if ("NotExistsTaxRate" == message) {
				message = "存在税目信息表中无对应税率信息的交易，不能进行开票。";
			}
			if ("NotExistsGoods" == message) {
				message = "存在无对应发票商品的交易信息，不能进行开票。";
			}
			if ("NotExistsTrans" == message) {
				message = "不存在对应交易交易信息，不能进行开票。";
			}
			if ("TransStatusError" == message) {
				message = "交易信息不是未开票状态，不能进行开票。";
			}

			if (null != message && '' !== message) {
				alert(message);
			}

		}

		showMsg();
	})
	<%--  function  importFJ() {
		var fileId = document.getElementsByName("theFile")[0];
		var cherNum="";
		var val="Y";
		if (fileId.value.length > 0) {
			 if(fileId.value.lastIndexOf(".jpg") > -1||fileId.value.lastIndexOf(".jpeg")>-1||fileId.value.lastIndexOf(".png")){
				$selectTransIds =$("input[name='selectTransIds']:checked")
				if($selectTransIds.size()==0){
					alert("请选择要上传附件所属保单");
					return false;
				}
				$selectTransIds.each(function(){
					var selectTransId=$(this).val();
					$("input[name='cherNumhidden']").each(function(){
						 var datastatus = $(this).val();
						 datastatus=datastatus.split("-");
						 if(datastatus[1]==selectTransId){
							 if(""==cherNum){
								 cherNum=datastatus[0];
								 alert(cherNum)
							 }else{
								 if(cherNum==datastatus[0]){
									 cherNum
								 }else{
									 val="N";
								 }
							 }
						 }
					}) 
				})
				if("Y"==val){
					alert("cherNum\t"+cherNum)
					document.forms[0].action="<%=webapp%>/upload.action?fileName="+cherNum
					document.forms[0].submit();
				}else{
					alert("不是同一保单不可合并传附件")
				}
			}else{
				alert("文件格式不对，请上传图片。");
			} 
		} else{
			alert("请先选择要上传的文件。");
		}
	} --%>
</script>
<script type="text/javascript">
	function importFJ(){
		var selectBillIds =document.getElementsByName("selectTransIds");
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
					document.forms[0].action="<%=webapp%>/upload.action?fileName="+fileName
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
   
  //跑批按钮
   /* function batchRun() {
		if (!confirm("确定马上进行中间表数据同步吗？")) {
			return false;
		}
		
		document.forms[0].action = "batchRun.action";
		document.forms[0].submit();
	} */
	
	
   
</script>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listTrans.action" id="Form1"
		enctype="multipart/form-data">
		<s:hidden id="message" value="%{message}"></s:hidden>
		<input name="fromFlag" type="hidden" value="list" />
		<input id="premTermArray" name="premTermArray" type="hidden" />
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition"><s:component
						template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">开票管理</span> <span
							class="current_status_submenu">开票申请</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%"
							border="0">
							<tr>
								<td style="text-align: right; width: 6%;">投保单号</td>
								<td style="text-align: left; width: 14%;"><input
									id="repnum" class="tbl_query_text" type="text" name="ttmpRcno"
									value="<s:property value='ttmpRcno'/>" /></td>
								<td style="text-align: right; width: 6%;">保单号</td>
								<td style="text-align: left; width: 14%;"><input
									id="cherNum" class="tbl_query_text" type="text" name="cherNum"
									value="<s:property value='cherNum'/>" /></td>
								<td style="text-align: right; width: 6%;">交易日期</td>
								<td style="text-align: left; width: 20%;">
									<input class="tbl_query_time" id="startDate" type="text"
										name="startDate" value="<s:property value='startDate'/>"
										onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'})"
										size='11' /> -- 	
									<input class="tbl_query_time" id="endDate" type="text" 
									    name="endDate" value="<s:property value='endDate'/>"
										onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'})"
										size='11' />
						        </td>
								<td style="text-align: right; width: 6%;"></td>
							</tr>
							<tr>
								<td style="text-align: right;">客户名称</td>
								<td style="text-align: left;"><input type="text"
									id="customerName" class="tbl_query_text" name="customerName"
									value="<s:property value='customerName'/>" maxlength="100" /></td>
								<td style="text-align: right;">交易状态</td>
								<td style="text-align: left;"><s:select
										id="transInfo.dataStatus" name="transInfo.dataStatus"
										list="transDataStatusList" headerKey="" headerValue="所有"
										listKey='value' listValue='text' cssClass="tbl_query_text" />
								</td>
								<td style="text-align: right;">批单号:</td>
								<td style="text-align: left;"><input type="text"
									id="batchNo" class="tbl_query_text" name="batchNo"
									value="<s:property value='batchNo'/>" maxlength="100" /></td>
								<%-- <td style="text-align: right;">票据类型</td>
								<td style="text-align: left;">
									<s:select id="isYK" name="isYk"
										list="#{'0':'正常开票','1':'预开票'}" headerKey='' headerValue="全部"
										listKey='key' listValue='value' cssClass="tbl_query_text" />
								</td> --%>
							</tr>
							<tr>
								<td style="text-align: right;" colspan="2"><input
									type="button" class="tbl_query_button" value="查询"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="btSubmit"
									id="btSubmit" onclick="submit()" style="margin-right: 60px" /></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left"><a href="#" onclick="transToEachBill()">
									<img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1010.png" />
									开票
							</a> <a href="#" onclick="transToOneBills()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1011.png" />
									合并开票
							</a> <a href="#" onclick="transToManyBill()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1012.png" />
									拆分开票
							</a> <%-- <a href="#" onclick="synchData()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1012.png" />
									数据同步
							</a> --%> <a href="#" onclick="transtoCustmer()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1013.png" />
									关联客户
							</a><a href="#" onclick="batchRun()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1013.png" />
									数据获取
							</a> <%-- <s:file id="theFile" name="theFile" size="30"
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
							</a> --%></td>

						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto">
						<div id="rDiv" style="width: 0px; height: auto;">
							<table class="lessGrid" cellspacing="0" rules="all" border="0"
								cellpadding="0" style="border-collapse: collapse; width: 100%;">
								<tr class="lessGrid head">
									<th style="text-align: center"><input
										style="width: 13px; height: 13px;" id="CheckAll"
										type="checkbox" onclick="cbxselectall(this,'selectTransIds')" />
									</th>
									<th style="text-align: center">序号</th>
									<th style="text-align: center">投保单号</th>
									<th style="text-align: center">保单号</th>
									<th style="text-align: center">批单号</th>
									<!-- <th style="text-align:center">保全受理号</th> -->
									<th style="text-align: center">交易日期</th>
									<th style="text-align: center">交易类型</th>
									<th style="text-align: center">客户名称</th>
									<th style="text-align: center">交易金额</th>
									<th style="text-align: center">税率</th>
									<th style="text-align: center">税额</th>
									<th style="text-align: center">价税合计</th>
									<th style="text-align: center">未开票金额</th>
									<th style="text-align: center">累计未开票总额</th>
									<th style="text-align: center">交易状态</th>
									<th style="text-align: center">承保日期</th>
									<th style="text-align: center">是否在犹豫期内</th>
									<th style="text-align: center">是否为预开票</th>
									<th style="text-align: center">明细</th>
								</tr>
								<s:iterator value="paginationList.recordList" id="iList"
									status="stuts">
									<tr align="center" title="<s:property value="transId"/>"
										id='<s:property value="transId"/>'
										class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
										<s:set
											value="@com.cjit.vms.trans.util.DataUtil@TRANS_STATUS_2"
											id="status2"></s:set>
										<s:set value="#status2 eq dataStatus?true:false"
											id="isDataStatus2"></s:set>
										<s:set value="balance==0?true:false" id="isEmptyBalance"></s:set>
										<!--新增
											日期：2018-09-03
											作者：刘俊杰
											功能：新增判断如果在犹豫期内,让其执行电子发票开具,在此页面中只进行展示,并不让开票
										 -->
										 <s:set value="@com.cjit.vms.trans.util.JSPUtil@isInHesitatePeriodForBoolean(hesitatePeriod)?true:false" id="isHesitatePeriod"></s:set>
										<td align="center"><input
											style="width: 13px; height: 13px;" class="selectTransIds"
											type="checkbox" name="selectTransIds"
											value='<s:property value="transId"/>'
											<s:property value="#isDataStatus2&&#isEmptyBalance||#isHesitatePeriod?'disabled':''"/> />
											<s:hidden name="selectCustomers" value="%{customerId}"></s:hidden>
											<!-- 保单号 --> <s:hidden name="selectCherNum"
												value="%{cherNum}"></s:hidden> <s:hidden
												name="selectCustomerTypes" value="%{customerType}"></s:hidden>
											<s:hidden name="selectFapiaoTypes" value="%{fapiaoType}"></s:hidden>
											<s:hidden name="selectDataStatuses" value="%{dataStatus}"></s:hidden>
											<s:hidden name="selectIncome" value="%{income}"></s:hidden> <s:hidden
												name="selectBalance" value="%{balance}"></s:hidden> <s:hidden
												name="selectPremTerm" value="%{premTerm}"></s:hidden> <input
											name="selectTaxRate" type="hidden"
											value="<s:property value='@com.cjit.common.util.NumberUtils@format(taxRate,"", 2)' />">

											<input type="hidden" name="cherNumhidden"
											value="<s:property value='cherNum' />-<s:property value='ttmpRcno'/>" />
										</td>
										<s:property value="" />
										<td align="center"><s:property value="#stuts.index+1" /></td>
										<td align="center"><s:property value="ttmpRcno" /></td>
										<td align="center" name="cherNum"><s:property
												value="cherNum" /></td>
										<td align="center" name="batchNo"><s:property
												value="batchNo" /></td>
										<td align="center" class="transDate"><s:property
												value="transDate" /></td>
										<td align="center"><s:property value="transTypeName" /></td>
										<td align="left"><s:property value="customerName" /></td>
										<td align="right" name="amt"><s:property value="amt" /></td>
										<td align="right"><s:property value="taxRate" /></td>
										<td align="right" name="taxAmt"><s:property
												value="taxAmt" /></td>
										<td align="right"><s:property value="reverseAmt" /></td>
										<td align="right"><s:property value="balance" /></td>
										<td align="right"><s:property value="wkze" /></td>
										<td align="center"><s:property value="dataStatusCH" /></td>
										<td align="center"><s:property value="hissDte" /></td>
										<td align="center" name="hesitatePeriod"><s:property
												value="@com.cjit.vms.trans.util.JSPUtil@isInHesitatePeriod(hesitatePeriod)" />
										</td>
										<td align="center"><s:property
												value="@com.cjit.vms.trans.util.JSPUtil@isYK(isYK)" /></td>
										<td align="center"><a href="#"
											onclick="goToPage('transDetail.action?transId=<s:property value="transId"/> ')">
												<img
												src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
												title="查看" style="border-width: 0px;" />
										</a></td>
									</tr>
								</s:iterator>
							</table>
						</div>
					</div> <input type="hidden" name="paginationList.showCount" value="false">
					<div id="anpBoud" align="Right"
						style="overflow: auto; width: 100%;">
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
	<form name="Form2" method="post" action="applyInvoice.action"
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
						
						
	<%-- <c:import url="http://10.10.100.32:8080/vmss/page/webctrl/instTree/tree_include.jsp"
		charEncoding="UTF-8">
		<c:param name="treeType" value="radio" />
		<c:param name="bankName_tree" value="inst_Name" />
		<c:param name="bankId_tree" value="inst_id" />
		<c:param name="taskId_tree" value="" />
		<c:param name="task_tree" value="task_tree" />
		<c:param name="webapp" value="http://10.10.100.32:8080/vmss" />
	</c:import>  --%>
						
						
</body>
</html>