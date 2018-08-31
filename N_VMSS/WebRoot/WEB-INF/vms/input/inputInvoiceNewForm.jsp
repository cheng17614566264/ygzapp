<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);
%>
<%@include file="../../../../page/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<%@ include file="../../../../page/include.jsp"%>

<script language="javascript" type="text/javascript"
	src="<%=bopTheme %>/js/main.js"></script>
<link href="<c:out value="${bopTheme2}"/>/css/main.css" rel="stylesheet"
	type="text/css" />
<script language="javascript" type="text/javascript"
	src="<%=bopTheme %>/js/window.js"></script>
<script charset="gb2312" language="javascript" type="text/javascript"
	src="<%=bopTheme %>/js/validator.js"></script>

<script
	src="<c:out value="${bopTheme}"/>/js/MessageBox/js/messageBox.js"
	type="text/javascript"></script>
<link
	href="<c:out value="${bopTheme}"/>/js/MessageBox/css/messageBox.css"
	rel="stylesheet" type="text/css" />
<title>发票信息单</title>
</head>
<script type="text/javascript">
function findOutSubmit() {

	//document.getElementById("fapiaoType").removeAttribute("disabled");
	
	
	var $form = $("#formBusiness");
	//$("select[name='customerNationality']",$form).attr("disabled",false);
		
	//var customerNationality = $("select[name='customerNationality']",$form).val();//国籍，专用发票必须为中国	
	
	//var fapiaoType = $("#fapiaoType",$form).val();
	/* if(fapiaoType&&fapiaoType=='0'){
	
		//if($("#customerTaxno1",$form)&&$("#customerTaxno1",$form).val().trim().length<=0){//客户识别码
		if($("#customerTaxno",$form)&&!fucChecTNull($("#customerTaxno",$form)[0])){//客户识别码
			$("#taxNo1",$form).css("display","inline");
			$("#taxNo",$form).css("display","none");
		 	return false;
		
		}else{
			$("#taxNo1",$form).css("display","none");
			$("#taxNo",$form).css("display","none");
		}
		if($("#customerAddress",$form)&&!fucChecTNull($("#customerAddress",$form)[0])){//地址
			$("#address1",$form).css("display","inline");
		 	return false;
		
		}else{
			$("#address1",$form).css("display","none");
		}
		
		if($("#customerPhone",$form)&&!fucChecTNull($("#customerPhone",$form)[0])){//电话
			$("#phone1",$form).css("display","inline");
			$("#phone",$form).css("display","none");
		 	return false;
		
		}else{
			$("#phone1",$form).css("display","none");
			$("#phone",$form).css("display","none");
		}
		
		if($("#customerEmail",$form)&&!fucChecTNull($("#customerEmail",$form)[0])){//邮箱
			$("#email1",$form).css("display","inline");
			$("#email",$form).css("display","none");
		 	return false;
		
		}else{
			$("#email1",$form).css("display","none");
			$("#email",$form).css("display","none");
		}
		
		if($("#customerCBank",$form)&&!fucChecTNull($("#customerCBank",$form)[0])){//开户行
			$("#bank1",$form).css("display","inline");
			$("#bank",$form).css("display","none");
		 	return false;
		
		}else{
			$("#bank1",$form).css("display","none");
			$("#bank",$form).css("display","none");
		}
		
		if($("#customerAccount",$form)&&!fucChecTNull($("#customerAccount",$form)[0])){//开户账号
			$("#account1",$form).css("display","inline");
			$("#account",$form).css("display","none");
		 	return false;
		
		}else{
			$("#account1",$form).css("display","none");
			$("#account",$form).css("display","none");
		}
		
		if(customerNationality!='CHN'){
		 	alert("国籍请选择中国");
		 	return false;
		}
		
	}*/
	var form = document.getElementById("formBusiness");
	form.action='saveInputInvoiceNew.action';
	form.submit();
	document.getElementById("btn_edit").disabled=true;
	}

</script>
<body>
	<form name="formBusiness" id="formBusiness" method="post">
		<div class="windowtitle"
			style="background: #004C7E; height: 45px; line-height: 45px; text-align: center; color: #FFF; font-weight: bold; font-size: 14px;">修改发票信息</div>
		<div class="windowtitle"
			style="background: #004C7E; height: 30px; line-height: 30px; text-align: left; color: #FFF; font-size: 12px; margin-top: 10px;">发票基本信息</div>
		<table id="contenttable" class="lessGrid" cellspacing="0" width="100%"
			align="center" cellpadding="0">
			<tr>

			</tr>
			<tr>
				<td width="15%" align="right" class="listbar">机构代码:</td>
				<td><input size="30" id="instId" class="tbl_query_text2"
					name="instId" type="text" maxlength="20"
					value="<s:property value='inputInvoiceNew.instId'/>"
					readonly="readonly" /></td>
				<td width="15%" align="right" class="listbar">机构名称:</td>
				<td width="35%"><input size="30" id="instName"
					class="tbl_query_text2" name="instName" type="text" maxlength="30"
					value="<s:property value='inputInvoiceNew.instName'/>"
					readonly="readonly" /></td>
			</tr>


			<tr>
				<td width="15%" align="right" class="listbar">发票代码:</td>
				<td width="35%"><input size="30" id="billCode"
					class="tbl_query_text2" name="billCode" type="text"
					value="<s:property value='inputInvoiceNew.billCode'  />"
					maxlength="18" readonly="readonly" /></td>
				<td width="15%" align="right" class="listbar">发票号码:</td>
				<td width="35%"><input size="30" id="billNo"
					class="tbl_query_text2" name="billNo" type="text"
					value="<s:property value='inputInvoiceNew.billNo'  />"
					maxlength="18" readonly="readonly" /></td>
			</tr>


			<tr>
				<td width="15%" align="right" class="listbar">行业类型:</td>
				<td><input size="30" class="tbl_query_text2"
					value="<s:property value='inputInvoiceNew.industryName'/>"
					type="text" readonly="readonly" /></td>

				<td width="15%" align="right" class="listbar">销方纳税人名称:</td>
				<td><input size="30" class="tbl_query_text2"
					value="<s:property value='inputInvoiceNew.vendorName'/>"
					type="text" readonly="readonly" /></td>
			</tr>

			<tr>

				<td width="15%" align="right" class="listbar">金额:</td>
				<td><input size="40" id="amt" class="tbl_query_text2"
					name="amt" type="text" maxlength="20"
					value="<s:property value='inputInvoiceNew.amt'/>"
					readonly="readonly" /></td>
				<td width="15%" align="right" class="listbar">税率:</td>
				<td><input size="30" id="taxRate" class="tbl_query_text2"
					name="taxRate" type="text" maxlength="20"
					value="<s:property value='inputInvoiceNew.taxRate'/>"
					readonly="readonly" /></td>
			</tr>
			<tr>
				<td width="15%" align="right" class="listbar">税额:</td>
				<td width="35%"><input size="30" id="taxAmt"
					class="tbl_query_text2" name="tatAmt" type="text"
					value="<s:property value='inputInvoiceNew.tatAmt' />"
					readonly="readonly" /></td>
				<td width="15%" align="right" class="listbar">价税合计:</td>
				<td width="35%"><input size="30" id="amtTaxSum"
					class="tbl_query_text2" name="amtTaxSum" type="text"
					value="<s:property value='inputInvoiceNew.amtTaxSum'/>"
					readonly="readonly" /> &nbsp;</td>
			</tr>
			<tr>
				<td width="15%" align="right" class="listbar">开票日期:</td>
				<td><input size="30" id="billDate" class="tbl_query_text2"
					name="deducDate" type="text" maxlength="20"
					value="<s:property value='inputInvoiceNew.deducDate'/>"
					readonly="readonly" /> &nbsp;
				<td width="15%" align="right" class="listbar">状态:</td>
				<td width="35%"><input size="30" id="dataStatus"
					class="tbl_query_text2" name="dataStatus" type="text"
					maxlength="20"
					value="<s:property value='inputInvoiceNew.dataStatus'/>"
					readonly="readonly" /></td>
			</tr>

			<tr>
				<td width="15%" align="right" class="listbar">抵扣日期:</td>
				<td><input size="30" id="deducDate" class="tbl_query_text2"
					name="deducDate" type="text" maxlength="20"
					value="<s:property value='inputInvoiceNew.deducDate' />"
					readonly="readonly" /></td>
				<td width="15%" align="right" class="listbar">抵扣是否通过:</td>
				<td width="15%"><input size="30" id="ifEstateDeduc"
					class="tbl_query_text2" name="ifEstateDeduc" type="text"
					maxlength="20"
					value="<s:property value='inputInvoiceNew.ifEstateDeduc'/>"
					readonly="readonly" /></td>
			</tr>
			<tr>
				<td class="listbar" align="right">抵扣比率:</td>
				<td><input type="text" class="tbl_query_text2"
					value="<s:property value='inputInvoiceNew.deducRatio'/>"
					name="deducRatio" readonly="readonly"></td>
				<td width="15%" align="right" class="listbar">抵扣额:</td>
				<td><input type="text" class="tbl_query_text2"
					value="<s:property value='inputInvoiceNew.deducAmt'/>"
					name="deducAmt" readonly="readonly"></td>
			</tr>
		</table>
		<div id="ctrlbutton" class="ctrlbutton" style="border: 0px">
			<input type="button" class="tbl_query_button" id="btn_edit"
				onMouseMove="this.className='tbl_query_button_on'"
				onMouseOut="this.className='tbl_query_button'"
				onclick="findOutSubmit()" name="BtnSave" value="保存" id="BtnSave" />
			<input type="button" class="tbl_query_button"
				onMouseMove="this.className='tbl_query_button_on'"
				onMouseOut="this.className='tbl_query_button'"
				onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn" />
		</div>
	</form>
</body>
</html>