<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.input.model.InputVatInfo"
	import="com.cjit.vms.trans.model.BillItemInfo"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	import="com.cjit.vms.input.model.InputInvoiceNew"
	import="com.cjit.vms.input.model.InputInvoiceNew"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<!--添加的  -->
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/calendar_notime.js"></script>

<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<!--添加的  -->
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>

<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/editview.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/vms.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<link href="<%=bopTheme2%>/css/editView.css" type="text/css"
	rel="stylesheet">
<script language="javascript" type="text/javascript">
		var issave = false;
		var msg = '<s:property value="message"/>';
//		function save(){
//			if(issave){
//				alert("数据保存中，请稍候...");
//				return false;
//			}
//			var isblank=true;
//			var oo = document.getElementById("Form1").getElementsByTagName("input");
//			for(var k = 0, len = oo.length; k < len; k++){
//				var t = oo[k].type;
//				if(t=="text" ){
//					var v=oo[k].value;
//					if(v&&v!="" &&v.replace(/(^\s*)|(\s*$)/g, "")!=""){
//						isblank=false;
//						break;
//					}
//				}
//			}
//			if (document.getElementById('submitFlag').value=='S'){
//				if (!submitBillCheck()){
//					return false;
//				}
//			}
//			if(!isblank){
//				document.getElementById("Form1").submit();
//				issave = true;
//			}else{
//				alert("表单没有输入数据,无需保存。 \n\n如要退出，请点击'返回'按钮");
//			}
//		}
		function save(){
			document.getElementById("Form1").submit();
		}
		function back(){
			submitAction(document.forms[0], "listInputTrans.action?fromFlag=menu");
		}
		function loadOurInfo(instCode){
			var webroot = '<%=webapp%>';
			var url = webroot + "/loadOurInfo.action";
			var param = "instCode=" + document.getElementById("instCode").value;
			var result = sendXmlHttpPost(url, param);
			var vOurInfos = result.split("###");
			if (vOurInfos.length == 5){
				if (document.getElementById("inputVatInfo.name") != null){
					document.getElementById("inputVatInfo.name").value = vOurInfos[0];
				}
				if (document.getElementById("inputVatInfo.taxno") != null){
					document.getElementById("inputVatInfo.taxno").value = vOurInfos[1];
				}
				if (document.getElementById("inputVatInfo.addressandphone") != null){
					document.getElementById("inputVatInfo.addressandphone").value = vOurInfos[2] + " " + vOurInfos[3];
				}
				if (document.getElementById("inputVatInfo.bankandaccount") != null){
					document.getElementById("inputVatInfo.bankandaccount").value = vOurInfos[4];
				}
			}
		}
		function loadBillAmtInfo(){
			var webroot = '<%=webapp%>';
			var url = webroot + "/loadBillAmtInfo.action";
			var inVatId = document.forms[0].elements['inputVatInfo.inVatId'].value;
			if (inVatId == null || inVatId == ''){
				return false;
			}
			var result = sendXmlHttpPost(url, "billId=" + inVatId);
			var vAmtInfos = result.split("###");
			if (vAmtInfos.length == 3){
				if (document.getElementById("inputVatInfo.amt") != null){
					document.getElementById("inputVatInfo.amt").value = vAmtInfos[0];
				}
				if (document.getElementById("inputVatInfo.taxAmt") != null){
					document.getElementById("inputVatInfo.taxAmt").value = vAmtInfos[1];
				}
				if (document.getElementById("inputVatInfo.sumAmt") != null){
					document.getElementById("inputVatInfo.sumAmt").value = vAmtInfos[2];
				}
			}
		}
		// 计算税价合计
		function countSumAmt(){
			var vSumAmt = "0.00";
			if (document.getElementById("inputVatInfo.amt") != null
				&& document.getElementById("inputVatInfo.amt").value != ""){
				var vAmt = document.getElementById("inputVatInfo.amt").value;
				vSumAmt = eval(vSumAmt) + eval(vAmt);
			}
			if (document.getElementById("inputVatInfo.taxAmt") != null
				&& document.getElementById("inputVatInfo.taxAmt").value != ""){
				var vTaxAmt = document.getElementById("inputVatInfo.taxAmt").value;
				vSumAmt = eval(vSumAmt) + eval(vTaxAmt);
			}
			document.getElementById("inputVatInfo.sumAmt").value = vSumAmt.toFixed(2);
		}
		
		//保存
		<!-- 中科软20160523-->
		
		function inputInvoiceSaveNew(){
			var instId = document.getElementById("inputInvoiceNew.instId");
			var instName = document.getElementById("inputInvoiceNew.instName");
			var billCode = document.getElementById("inputInvoiceNew.billCode");
			if(instId==null){
				alert("机构代码不能为空");
				return;
			}
			if( instName==null){
				alert("机构名称不能为空");
				return;
			}
			if( billCode==null){
				alert("发票代码不能为空");
				return;
			}
			var dataStatus = document.getElementById("inputInvoiceNew.billNo");//是否通过认证
			var ifEstateDeduc = document.getElementById("inputInvoiceNew.ifEstateDeduc");//是否属于不动产
			var billNo = document.getElementById("inputInvoiceNew.billNo");
			if( billNo==null){
				alert("发票号码不能为空");
				return;
			}
			var industryType = document.getElementById("inputInvoiceNew.industryType");
			if( industryType==null){
				alert("商业类型不能为空");
				return;
			}
			var vendorName = document.getElementById("inputInvoiceNew.vendorName");
			if( vendorName==null){
				alert("商业名称不能为空");
				return;
			}
			var amt = document.getElementById("inputInvoiceNew.amt");
			if( document.getElementById("inputInvoiceNew.amt")==null){
				alert("金额不能为空");
				return;
			}
			var taxRate = document.getElementById("inputInvoiceNew.taxRate");
			if( taxRate==null){
				alert("税率不能为空");
				return;
			}
			var taxAmt = document.getElementById("inputInvoiceNew.taxAmt")
			if(taxAmt ==null){
				alert("额率不能为空");
				return;
			}
			
		     var amtTaxSum = document.getElementById("inputInvoiceNew.amtTaxSum")
			if(amtTaxSum ==null){
				alert("发票总额不能为空");
				return;
			}
		     
		     var deducBeginDate = document.getElementById("inputInvoiceNew.deducBeginDate")
			if(deducBeginDate ==null){
				alert("开票日期不能为空");
				return;
			}
		     var deducDate = document.getElementById("inputInvoiceNew.deducDate")
			if(deducDate ==null){
				alert("抵扣日期不能为空");
				return;
			}   
		     submitAction(document.forms[0], "inputInvoiceSaveNew.action?instId="+instId+
		    		 "&instName="+instName+
		    		 "&billCode="+billCode+
		    		 "&dataStatus="+dataStatus+
		    		 "&ifEstateDeduc="+ifEstateDeduc+
		    		 "&billNo="+billNo+
		    		 "&industryType="+industryType+
		    		 "&vendorName="+vendorName+
		    		 "&amt="+amt+
		    		 "&taxRate="+taxRate+
		    		 "&taxAmt="+taxAmt+
		    		 "&amtTaxSum="+amtTaxSum+
		    		 "&deducBeginDate="+deducBeginDate+
		    		 "&deducDate="+deducDate
		    		 );
					document.forms[0].action="listInputInvoiceNew.action";
					alert("保存成功!")
			
		}
		
		//返回
		function inputInvoiceNewBack() {
			submitAction(document.forms[0], "inputInvoiceNewBack.action");
		}
		
		
	</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body onLoad="javascript:tips()" onmousemove="MM(event)"
	onmouseout="MO(event)">
	<form name="Form1" method="post" action="saveInputTrans.action"
		id="Form1">
		<input type="hidden" name="dealNo"
			value="<s:property value='inputTrans.dealNo'/>" /> <input
			type="hidden" name="submitFlag" id="submitFlag" value="U" />
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">进项税管理</span> <span
							class="current_status_submenu">认证管理</span> <span
							class="current_status_submenu">数据导入 </span> <span
							class="current_status_submenu">数据编辑</span>
					</div> </br>
					<table id="tbl_context" cellspacing="0" width="100%" align="center"
						cellpadding="0">
						<tr class="row1">
							<td align="right"
								style="background-color: #F0F0F0; font-weight: bold; color: #727375;">
								发票代码:&nbsp;&nbsp;&nbsp;</td>
							<td align="left"><input type="text" readonly="readonly"
								class="tbl_query_text" name="inputInvoiceNew.billCode"
								id="inputInvoiceNew.billCode"
								value="<s:property value='inputInvoiceNew.billCode'/>" /></td>
							<td align="right"
								style="background-color: #F0F0F0; font-weight: bold; color: #727375;">
								发票号码:&nbsp;&nbsp;&nbsp;</td>
							<td align="left"><input type="text" readonly="readonly"
								class="tbl_query_text" name="inputInvoiceNew.billNo"
								id="inputInvoiceNew.billNo"
								value="<s:property value='inputInvoiceNew.billNo'/>" /></td>
						</tr>
						<tr class="row1">
							<td align="right" width="20%"
								style="background-color: #F0F0F0; font-weight: bold; color: #727375;">
								机构代码:&nbsp;&nbsp;&nbsp;</td>
							<td align="left" width="30%"><input type="text"
								class="tbl_query_text" name="inputInvoiceNew.instId"
								id="inputInvoiceNew.instId"
								value="<s:property value='inputInvoiceNew.instId'/>" /></td>
							<td align="right" width="20%"
								style="background-color: #F0F0F0; font-weight: bold; color: #727375;">
								机构名称:&nbsp;&nbsp;&nbsp;</td>
							<td align="left" width="30%"><input type="text"
								class="tbl_query_text" name="inputInvoiceNew.instName"
								id="inputInvoiceNew.instName"
								value="<s:property value='inputInvoiceNew.instName'/>" /></td>
						</tr>

						<tr class="row1">
							<td align="right"
								style="background-color: #F0F0F0; font-weight: bold; color: #727375;">
								行业类型:&nbsp;&nbsp;&nbsp;</td>
							<td align="left"><input type="text" class="tbl_query_text"
								name="inputInvoiceNew.industryType"
								id="inputInvoiceNew.industryType"
								value="<s:property value='inputInvoiceNew.industryType'/>" /></td>
							<td align="right"
								style="background-color: #F0F0F0; font-weight: bold; color: #727375;">
								应商的名称:&nbsp;&nbsp;&nbsp;</td>
							<td align="left"><input type="text" class="tbl_query_text"
								name="inputInvoiceNew.vendorName"
								id="inputInvoiceNew.vendorName"
								value="<s:property value='inputInvoiceNew.vendorName'/>" /></td>
						</tr>
						<tr class="row1">
							<td align="right"
								style="background-color: #F0F0F0; font-weight: bold; color: #727375;">
								金额:&nbsp;&nbsp;&nbsp;</td>
							<td align="left"><input type="text" class="tbl_query_text"
								name="inputInvoiceNew.amt" id="inputInvoiceNew.amt"
								value="<s:property value='inputInvoiceNew.amt'/>"
								onkeypress="checkkey(value);"
								onblur="formatAmount(this,2,0,'true');" /></td>
							<td align="right"
								style="background-color: #F0F0F0; font-weight: bold; color: #727375;">
								税率:&nbsp;&nbsp;&nbsp;</td>
							<td align="left"><input type="text" class="tbl_query_text"
								name="inputInvoiceNew.taxRate" id="inputInvoiceNew.taxRate"
								value="<s:property value='inputInvoiceNew.taxRate'/>"
								onkeypress="checkkey(value);"
								onblur="formatAmount(this,2,0,'true');" /></td>
						</tr>
						<tr class="row1">
							<td align="right"
								style="background-color: #F0F0F0; font-weight: bold; color: #727375;">
								率额:&nbsp;&nbsp;&nbsp;</td>
							<td align="left"><input type="text" class="tbl_query_text"
								name="inputInvoiceNew.taxAmt" id="inputInvoiceNew.taxAmt"
								value="<s:property value='inputInvoiceNew.taxAmt'/>"
								onkeypress="checkkey(value);"
								onblur="formatAmount(this,2,0,'true');" /></td>
							<td align="right"
								style="background-color: #F0F0F0; font-weight: bold; color: #727375;">
								发票总额:&nbsp;&nbsp;&nbsp;</td>
							<td><input type="text" class="tbl_query_text"
								name="inputInvoiceNew.amtTaxSum" id="inputInvoiceNew.amtTaxSum"
								value="<s:property value='inputInvoiceNew.amtTaxSum'/>"
								onkeypress="checkkey(value);"
								onblur="formatAmount(this,2,0,'true');" /></td>
						</tr>
						<tr class="row1">
							<td align="right"
								style="background-color: #F0F0F0; font-weight: bold; color: #727375;">
								开票的日期:&nbsp;&nbsp;&nbsp;</td>
							<td width="280"><input class="tbl_query_time"
								id="inputInvoiceNew.deducBeginDate" type="text"
								name="inputInvoiceNew.deducBeginDate"
								value="<s:property value='inputInvoiceNew.billdate'/>"
								onfocus="WdatePicker({lang:'zh-cn'})" size='11' /></td>
							<td align="right"
								style="background-color: #F0F0F0; font-weight: bold; color: #727375;">
								是否通过认证:&nbsp;&nbsp;&nbsp;</td>
							<td align="left"><s:select id="inputInvoiceNew.dataStatus"
									name="inputInvoiceNew.dataStatus" list="#{'是':'是','否':'否'}"
									listKey='key' listValue='value' cssClass="tbl_query_text" /></td>
						</tr>
						<tr class="row1">
							<td align="right"
								style="background-color: #F0F0F0; font-weight: bold; color: #727375;">
								抵扣日期:&nbsp;&nbsp;&nbsp;</td>
							<td width="280"><input class="tbl_query_time"
								id="inputInvoiceNew.deducDate" type="text"
								name="inputInvoiceNew.deducDate"
								value="<s:property value='inputInvoiceNew.deducDate'/>"
								onFocus="WdatePicker({lang:'zh-cn'})" size='11' /></td>
							<td align="right"
								style="background-color: #F0F0F0; font-weight: bold; color: #727375;">
								是否属于不动产:&nbsp;&nbsp;&nbsp;</td>
							<td align="left"><s:select
									id="inputInvoiceNew.ifEstateDeduc"
									name="inputInvoiceNew.ifEstateDeduc" list="#{'是':'是','否':'否'}"
									listKey='key' listValue='value' cssClass="tbl_query_text" /></td>
						</tr>
					</table> </br> </br>
					<div id="ctrlbutton" class="ctrlbutton" style="border: 0px">
						<input type="button" class="tbl_query_button" value="保存"
							onMouseMove="this.className='tbl_query_button_on'"
							onMouseOut="this.className='tbl_query_button'" name="btSave"
							id="btSave" onclick="inputInvoiceSaveNew()" /> <input
							type="button" class="tbl_query_button" value="返回"
							onMouseMove="this.className='tbl_query_button_on'"
							onMouseOut="this.className='tbl_query_button'" name="btBack"
							id="btBack" onclick="inputInvoiceNewBack()" />
					</div> <script language="javascript" type="text/javascript">
				//var vDataStatus = document.forms[0].elements['inputVatInfo.dataStatus'].value;
				//if (vDataStatus == "<%=DataUtil.BILL_STATUS_2%>" || vDataStatus == "<%=DataUtil.BILL_STATUS_4%>"){
				//	document.getElementById("btSave").disabled = true;
				//	document.getElementById("btSubmit").disabled = true;
				//	document.getElementById("btCancel").disabled = true;
				//}
			</script>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>