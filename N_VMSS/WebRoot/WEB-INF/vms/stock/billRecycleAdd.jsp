<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>遗失发票信息录入</title>
</head>
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

<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js"
	type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css"
	rel="stylesheet" type="text/css" />
<style type="text/css">
.lessGrid {
	
}

.listbar {
	background-color: #ebebed;
}

.spanstar {
	color: red
}
</style>
<script type="text/javascript">
// [保存]按钮
function submitform(){
	if(true==Check()){
		alert("success")
	}
	
}
// [返回]按钮
function submitForm(actionURL){
	
	
}
// 校验
function Check(){
	// 代码
	if(Checknull($("#billId").val(),"发票代码不可为空")==false){
		return false;
	}
	if(Checkleng($("#billId").val(),"10","发票代码长度必须是10位")==false){
		return false;
	}
	if(Checkinteger($("#billId").val(),"发票代码应该为整数")==false){
		return false;
	}
	//起始号
	if(Checknull($("#billStarNo").val(),"请输入发票起始号")==false){
	    return false;
	}
	if(Checkinteger($("#billStarNo").val(),"发票起始号码应该为整数")==false){
			 return false;
	}
	if(Checkleng($("#billStarNo").val(),"8","发票起始号码长度必须是8位")==false){
			return false;
	}
	//终止号
	if(Checknull($("#billEndNo").val(),"请输入发票终止号码")==false){
		    return false;
	}
	if(Checkinteger($("#billEndNo").val(),"发票终止号码应该为整数")==false){
			return false;
	}
	if(Checkleng($("#billEndNo").val(),"8","发票终止号码长度必须是8位")==false){
			return false;
	}
	if($("#billEndNo").val()*1<$("#billStarNo").val()*1){
			alert("发票终止号码不能小于发票起始号码");
			return false;
	}
	// 发票
	if(Checknull(document.getElementById("billType").value,"请选择发票类型")==false){
		return false;
	}
	//开票员
	if(Checknull($("#kpyName"),"开票员信息不可为空")==false){
		return false;
	}
	if(Checknull($("#kpyId"),"开票员信息不可为空")==false){
		return false;
	}
	// 票据数
	if(Checknull($("count").val(),"数量统计不正确")==false){
		return false;
	}
	return true;
}

// 数量  校验
function change(){
	var $billEndNo= $("#billEndNo").val();
	var $billStarNo=$("#billStarNo").val();
	$("#count").attr("value",$billEndNo*1-$billStarNo*1)
}
	

// 空校验
function Checknull(obj,massage){
	var str=obj;
	if(str.length<0||str==""){
		alert(massage);
	 	return false;
	}else{
		return true;
	}
}
// 长度校验
function Checkleng(obj,leng,massage){
	var str=obj;
	str=str.replace(/^(\s)*|(\s)*$/g,"");
	if(str.length!=leng){
		alert(massage);
	 	return false;
	}else{
		return true;
	}
}
// 类型校验
function  Checkinteger(obj,massage){
	var str=obj;
	str=str.replace(/^(\s)*|(\s)*$/g,"");
	var newPar=/^(-|\+)?\d+$/
	if(str.length>0 && newPar.test(str)==false)
	{
		alert(massage);
		return false;
	}else{
		return true;
	}
}
// 票据信息校验

</script>
<body>
	<div class="showBoxDiv">
		<form id="frm"
			action="<c:out value='${webapp}'/>/billInventoryList.action"
			method="post">
			<div id="editsubpanel" class="editsubpanel">
				<div id="tbl_current_status">
					<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
						class="current_status_menu">当前位置：</span> <span
						class="current_status_submenu1">销项税管理</span> <span
						class="current_status_submenu">库存管理</span> <span
						class="current_status_submenu">发票回收</span> <span
						class="current_status_submenu">回收录入</span>
				</div>
				<div style="height: 50px"></div>
				<div style="overflow: auto; width: 100%;">
					<table id="contenttable" class="lessGrid" cellspacing="0"
						width="100%" align="center" cellpadding="0"
						style="background-color: #ffffff;">
						<tr>
							<td colspan="5" class="listbar"
								style="background-color: #004c7e; height: 45px"></td>
						</tr>
						<tr>
							<td width="10px"></td>
							<td width="15%"
								style="text-align: right; background-color: #ebebed"
								class="listbar">报税机构:</td>
							<td width="25%"><select style="width: 125px">
									<option><%=request.getAttribute("instId")+" - "+request.getAttribute("instName")%></option>
							</select> <span class="spanstar">*</span></td>
							<td width="20px" class="listbar" style="text-align: right">开票员名称</td>
							<td><input id="kpyName" name="kpyName" type="text"
								class="tbl_query_text" /> <span class="spanstar">*</span></td>
						</tr>
						<tr>
							<td width="10px"></td>
							<td width="15%" style="text-align: right" class="listbar">发票类型:</td>
							<td width="25%"><select id="billType" style="width: 127px"
								name="billType">
									<option value="">请选择</option>
									<option value="0">增值税专用发票</option>
									<option value="1">增值税普通发票</option>
							</select> <span class="spanstar">*</span></td>
							<td width="20px" class="listbar" style="text-align: right">开票员编号</td>
							<td><input id="kpyId" name="kpyId" type="text"
								class="tbl_query_text" /> <span class="spanstar">*</span></td>
						</tr>
						<tr>
							<td width="10px"></td>
							<td width="15%" style="text-align: right" class="listbar">发票代码:</td>
							<td width="25%"><input id="billId" name="billId" type="text"
								class="tbl_query_text" /> <span class="spanstar">*</span></td>
							<td width="15%" style="text-align: right" class="listbar">标志:</td>
							<td width="25%"><s:select id="flag" name="flag"
									list="#{'1':'回收'}" listKey='key' listValue='value'
									cssClass="tbl_query_text" /> <span class="spanstar">*</span></td>
						</tr>
						<tr>
							<td width="10px"></td>
							<td width="15%" style="text-align: right" class="listbar">发票起始号码:</td>
							<td width="25%"><input id="billStarNo" name="billStarNo"
								type="text" class="tbl_query_text" /> <span class="spanstar">*</span>
							</td>
							<td width="15%" style="text-align: right" class="listbar">发票终止号码:</td>
							<td width="25%"><input id="billEndNo" name="billEndNo"
								type="text" class="tbl_query_text" onchange="change()" /> <span
								class="spanstar">*</span></td>
						</tr>
						<tr>
							<td width="10px"></td>
							<td width="15%" style="text-align: right" class="listbar">数量统计:</td>
							<td width="25%"><input id="count" name="count" type="text"
								class="tbl_query_text" value="" readonly /> <span
								class="spanstar" hidden>*</span></td>
							<td width="20px" style="text-align: right" class="listbar"></td>
							<td width="15%"></td>
						</tr>

						<tr style="height: 5px">
						</tr>
					</table>
				</div>
				<div id="ctrlbutton" class="ctrlbutton"
					style="border: 0px; margin-top: 3%">
					<input type="button" name="tijiao" class="tbl_query_button"
						onMouseMove="this.className='tbl_query_button_on'"
						onMouseOut="this.className='tbl_query_button'"
						onclick="submitform();" name="BtnSave" value="保存" id="BtnSave" />
					<input type="button" class="tbl_query_button" value="返回"
						onMouseMove="this.className='tbl_query_button_on'"
						onMouseOut="this.className='tbl_query_button'"
						onclick="submitForm('billInventoryList.action?type=back')">

				</div>
			</div>
		</form>
	</div>
</body>
</html>