<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.input.model.InputVatInfo"
	import="com.cjit.vms.trans.model.BillItemInfo"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.input.model.InputInvoiceNew"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
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
//				if(t=="text" ){<typeAlias alias="informationBills" type="com.cjit.vms.input.model.InformationBills" />
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
			if(confirm("确认要修改吗？")){
				document.getElementById("Form1").submit();
			}
			
		}
		function back(){
			submitAction(document.forms[0], "listInputInvoiceNew.action?fromFlag=menu");
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

		function taxRateAndAmt(){
			var amt = $("#amt").val();
			var amtRate = $("#amtRate").val();
				$("#taxAmt").val(Math.round(amt*amtRate*100)/100);
				$("#amtTaxAmt").val(Math.round(amt*amtRate*100)/100+Math.round(amt*100)/100);
			
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
	<form name="Form1" method="post" action="saveInputTransNew.action"
		id="Form1">
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">进项税管理</span> <span
							class="current_status_submenu">发票管理</span> <span
							class="current_status_submenu">数据编辑</span>
					</div> </br> <input type="hidden" id="pk" name="pk"
					value='<s:property value="inputInvoiceNew.pk"/>'></input>
					<table id="tbl_context" cellspacing="0" width="100%" align="center"
						cellpadding="0">
						<tr>
							<td align="right">机构名称</td>
							<td width="280"><input type="hidden" id="inst_id"
								name="instId"
								value='<s:property value="inputInvoiceNew.instId"/>'> <input
								type="text" class="tbl_query_text_readonly1" id="inst_Name"
								name="instName"
								value='<s:property value="inputInvoiceNew.instName"/>'>
								&nbsp; <span style="color: red;" id="nm">*</span></td>
							<!-- <td width="15%" align="right" class="listbar">用途:</td>
					<td width="35%">
					<select id="porpuseCode" name="porpuseCode">
					
						<option value="Y01" <s:if test='inputInvoiceNew.porpuseCode=="Y01"'> selected </s:if>>应税</option>
					
					
						<option value="Y02" <s:if test='inputInvoiceNew.porpuseCode=="Y02"'> selected </s:if>>进项转出</option>
						
					
						<option value="Y03"  <s:if test='inputInvoiceNew.porpuseCode=="Y03"'>selected </s:if>>视同销售</option>
					
					
						
						
					</select>
				</td> -->
							<td width="15%" align="right">行业类型:</td>
							<td width="35%"><select id="industry" name="industry">
									<option value="HY0001"
										<s:if test='inputInvoiceNew.industryType=="HY0001"'> selected </s:if>>有形动产租赁服务</option>
									<option value="HY0002"
										<s:if test='inputInvoiceNew.industryType=="HY0002"'> selected </s:if>>运输服务</option>
									<option value="HY0003"
										<s:if test='inputInvoiceNew.industryType=="HY0003"'> selected </s:if>>电信服务</option>
									<option value="HY0004"
										<s:if test='inputInvoiceNew.industryType=="HY0004"'> selected </s:if>>建筑安装服务</option>
									<option value="HY0005"
										<s:if test='inputInvoiceNew.industryType=="HY0005"'> selected </s:if>>不动产租赁服务</option>
									<option value="HY0006"
										<s:if test='inputInvoiceNew.industryType=="HY0006"'> selected </s:if>>受让土地使用权</option>
									<option value="HY0007"
										<s:if test='inputInvoiceNew.industryType=="HY0007"'> selected </s:if>>金融保险服务</option>
									<option value="HY0008"
										<s:if test='inputInvoiceNew.industryType=="HY0008"'> selected </s:if>>生活服务</option>
									<option value="HY0009"
										<s:if test='inputInvoiceNew.industryType=="HY0009"'> selected </s:if>>取得无形资产</option>
									<option value="HY00010"
										<s:if test='inputInvoiceNew.industryType=="HY00010"'> selected </s:if>>货物及加工</option>
									<option value="HY00011"
										<s:if test='inputInvoiceNew.industryType=="HY00011"'> selected </s:if>>修理修陪劳务</option>
									<option value="HY00012"
										<s:if test='inputInvoiceNew.industryType=="HY00012"'> selected </s:if>>其他</option>
							</select> &nbsp; <span style="color: red;" id="nm">*</span></td>
						</tr>
						<tr>
							<td width="15%" align="right" class="listbar">发票代码:</td>
							<td width="35%"><input size="30" id="billCode"
								style="height: 21px;" readonly class="tbl_query_text_readonly1"
								name="billCode" type="text" maxlength="20"
								value='<s:property value="inputInvoiceNew.billCode"/>' />
								&nbsp; <span style="color: red;" id="nm">*</span></td>
							<td width="15%" align="right" class="listbar">发票号码:</td>
							<td width="35%"><input size="30" id="billNo"
								style="height: 21px;" readonly class="tbl_query_text_readonly1"
								class="tbl_query_text" name="billNo" type="text" maxlength="20"
								value='<s:property value="inputInvoiceNew.billNo"/>' /> &nbsp; <span
								style="color: red;" id="nm">*</span></td>
						</tr>
						<tr>

							<td width="15%" align="right" class="listbar">金额:</td>
							<td width="35%"><input size="40" id="amt"
								class="tbl_query_text" name="amt" type="text"
								onblur="taxRateAndAmt()" maxlength="100"
								value='<s:property value="inputInvoiceNew.amt"/>'
								onblur='taxRateAndAmt()' /> &nbsp; <span style="color: red;"
								id="nm">*</span></td>
							<td width="15%" align="right" class="listbar">税率:</td>
							<td width="35%"><input size="30" id="amtRate"
								class="tbl_query_text" name="taxRate" onblur="taxRateAndAmt()"
								type="text" maxlength="20"
								value='<s:property value="inputInvoiceNew.taxRate" />' /> &nbsp;
								<span style="color: red;" id="nm">*</span></td>
						</tr>
						<tr>


							<td width="15%" align="right" class="listbar">税额:</td>
							<td width="35%"><input size="30" id="taxAmt"
								class="tbl_query_text" name="taxAmt" type="text" maxlength="20"
								onclick="SumTaxAmt()"
								value='<s:property value="inputInvoiceNew.taxAmt"/>' /> &nbsp; <span
								style="color: red;" id="nm">*</span></td>
							<td width="15%" align="right" class="listbar">发票总额:</td>
							<td width="35%"><input size="30" id="amtTaxAmt"
								class="tbl_query_text" name="amtTaxSum" type="text"
								maxlength="20" onclick="sumAmtTaxt()"
								value='<s:property value="inputInvoiceNew.amtTaxSum"/>' />
								&nbsp; <span style="color: red;" id="nm">*</span></td>
						</tr>
						<tr>
							<td width="15%" align="right">开票日期:</td>

							<td width="35%"><input id="billDate" name="billDate"
								type="text"
								value="<s:property value='inputInvoiceNew.billDate' />"
								class="tbl_query_time1"
								onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billDate\')}'})" />
								&nbsp;<span style="color: red;" id="nm">*</span></td>
							<td width="15%" align="right" class="listbar">发票状态:</td>
							<td width="35%"><select id="dataStatus" name="dataStatus">
									<option value="0"
										<s:if test='inputInvoiceNew.dataStatus=="0"'> selected </s:if>>待认证</option>
									<option value="1"
										<s:if test='inputInvoiceNew.dataStatus=="1"'> selected </s:if>>已认证</option>
									<option value="2"
										<s:if test='inputInvoiceNew.dataStatus=="2"'> selected </s:if>>已转出</option>
									<option value="3"
										<s:if test='inputInvoiceNew.dataStatus=="3"'> selected </s:if>>已抵扣</option>
							</select> &nbsp; <span style="color: red;" id="nm">*</span></td>

						</tr>

						<tr>

							<td width="15%" align="right">费用明细编码:</td>
							<td width="35%"><input size="30" id="directionID"
								class="tbl_query_text" name="directionID" type="text"
								maxlength="20"
								value='<s:property value="inputInvoiceNew.directionId"/>' />
								&nbsp;<span style="color: red;" id="nm">*</span></td>
							<td width="15%" align="right">进项转出科目明细编码:</td>
							<td width="35%"><input size="30" id="trans_item"
								class="tbl_query_text" name="trans_item" type="text"
								maxlength="20"
								value='<s:property value="inputInvoiceNew.temp2"/>' /> &nbsp;<span
								style="color: red;" id="nm">*</span></td>

						</tr>
						<tr>
							<td width="15%" align="right" class="listbar">发票形式:</td>
							<td width="35%"><select id="billType" name="billType">
									<option value="0"
										<s:if test='inputInvoiceNew.billType=="0"'> selected </s:if>>专票</option>
									<option value="1"
										<s:if test='inputInvoiceNew.billType=="1"'> selected </s:if>>普票</option>
							</select> &nbsp; <span style="color: red;" id="nm">*</span></td>
							<td width="15%" align="right" class="listbar">数据来源:</td>
							<td width="35%"><select id="temp1" disabled name="temp1">
									<option value="A"
										<s:if test='inputInvoiceNew.temp1=="A"'> selected </s:if>>费控系统</option>
									<option value="B"
										<s:if test='inputInvoiceNew.temp1=="B"'> selected </s:if>>手动输入</option>
							</select> &nbsp; <span style="color: red;" id="nm">*</span></td>
						</tr>
						<tr>
							<td width="15%" align="right" class="listbar">转出比例:</td>
							<td width="35%"><input size="30" id="transferRatio"
								<s:if test='inputInvoiceNew.temp1=="A"'>readonly class="tbl_query_text_readonly1" </s:if>
								<s:else >class="tbl_query_text" </s:else> name="transferRatio"
								type="text" maxlength="20"
								value='<s:property value="inputInvoiceNew.transferRatio"/>' />
								&nbsp;<span style="color: red;" id="nm">*</span></td>
							<td width="15%" align="right" class="listbar">转出金额:</td>
							<td width="35%"><input size="30" id="transferAmt"
								<s:if test='inputInvoiceNew.temp1=="A"'>readonly class="tbl_query_text_readonly1" </s:if>
								<s:else>class="tbl_query_text" </s:else> name="transferAmt"
								type="text" maxlength="20"
								value='<s:property value="inputInvoiceNew.transferAmt"/>' />
								&nbsp;<span style="color: red;" id="nm">*</span></td>


						</tr>
						<tr>
							<td width="15%" align="right">所属账期:</td>

							<td width="35%"><input id="auditDate" name="auditDate"
								type="text"
								value="<s:property value='inputInvoiceNew.auditDate' />"
								class="tbl_query_time1"
								onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'auditDate\')}'})" />
								&nbsp;<span style="color: red;" id="nm">*</span></td>

						</tr>

					</table> </br> </br>
					<div id="ctrlbutton" class="ctrlbutton" style="border: 0px">
						<input type="button" class="tbl_query_button" value="保存"
							onMouseMove="this.className='tbl_query_button_on'"
							onMouseOut="this.className='tbl_query_button'" name="btSave"
							id="btSave" onclick="save()" /> <input type="button"
							class="tbl_query_button" value="返回"
							onMouseMove="this.className='tbl_query_button_on'"
							onMouseOut="this.className='tbl_query_button'" name="btBack"
							id="btBack" onclick="back()" />
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