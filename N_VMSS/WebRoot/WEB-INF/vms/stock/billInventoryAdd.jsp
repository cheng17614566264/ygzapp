<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增空白发票</title>
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
// 判空验证
function Checknull(obj,massage){
	var str=obj;
	if(str.length<0||str==""){
		alert(massage);
	 	return false;
	}else{
		return true;
	}
}
// 类型验证
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
// 长度验证
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
//校验
function Check(){
	//发票号码经行验证
	if(Checknull(document.getElementById("billId").value,"请输入发票代码")==false){
		  return false;
	}
    if(Checkinteger(document.getElementById("billId").value,"发票代码应该为整数")==false){
		   return false;
	}
    if(Checkleng(document.getElementById("billId").value,"10","发票代码长度必须是10位")==false){
			return false;
	}
	//发票起始号码是否为空
	if(Checknull(document.getElementById("billStarNo").value,"请输入发票起始号")==false){
		    return false;
	}
	if(Checkinteger(document.getElementById("billStarNo").value,"发票起始号码应该为整数")==false){
			 return false;
	}
	if(Checkleng(document.getElementById("billStarNo").value,"8","发票起始号码长度必须是8位")==false){
			return false;
	}
	//发票终止号码是否为空
	if(Checknull(document.getElementById("billEndNo").value,"请输入发票终止号码")==false){
		    return false;
	}
	if(Checkinteger(document.getElementById("billEndNo").value,"发票终止号码应该为整数")==false){
			return false;
	}
	if(Checkleng(document.getElementById("billEndNo").value,"8","发票终止号码长度必须是8位")==false){
			return false;
	}
	if(document.getElementById("billEndNo").value<document.getElementById("billStarNo").value){
			alert("发票终止号码不能小于发票起始号码");
			return false;
	}
	if(Checknull(document.getElementById("billType").value,"请选择发票类型")==false){
			return false;
	}
	if(Checknull(document.getElementById("inst_Name").value,"请选择机构")==false){
	    return false;
    }
	return true;
}
// [提交]
function submitform(){
 	if(true==Check()){
 	 	document.forms[0].action = "<%=webapp%>/savebillInventory.action"
 	    document.forms[0].submit();
 	}
}
// [返回]
function submitForm(actionUrl){
	document.forms[0].action = actionUrl;
	document.forms[0].submit();
	document.forms[0].action = '';
}
// 判断号码
function jy(){
	$billId =$("#billId").val();
	$billType =$("#billType").val();
	$billStarNo= $("#billStarNo").val();
	$billEndNo=$("#billEndNo").val();
	$.ajax({
		url:'addjy.action',
		type:'post',
		async: false,
		data:{billId:$billId,billType : $billType, billStarNo: $billStarNo , billEndNo: $billEndNo},
		dataType: 'text',
        timeout: 1000,
        error: function () {
            return false;
        },
        success:function(result){
        	if(result=="N"){
        		alert("信息添加有重叠，请查验后进行新增 ！")
        	}else if(result=="Y"){
        		if(true==Check()){
        			alert("新增成功")
        	 	  	document.forms[0].action = "<%=webapp%>/savebillInventory.action";
        	 	    document.forms[0].submit();
        	 	}
        	}
        }
	})
}
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
						class="current_status_submenu">发票管理</span> <span
						class="current_status_submenu">数据添加</span>
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
							<td width="20px"></td>
							<td width="15%"
								style="text-align: right; background-color: #ebebed"
								class="listbar">报税机构:</td>
							<td width="25%"><input type="text" class="tbl_query_text"
								id="inst_Name" name="instId"
								value='<s:property value="billInventory.instId"/>'
								onclick="setOrg(this);" readonly="readonly"> <span
								class="spanstar">*</span></td>
							<td width="20px" class="listbar"></td>
							<td></td>
						</tr>
						<tr>
							<td width="20px"></td>
							<td width="15%" style="text-align: right" class="listbar">发票类型:</td>
							<td width="25%"><select id="billType" style="width: 127px"
								name="billType">
									<option value="">请选择</option>
									<option value="0">增值税专用发票</option>
									<option value="1">增值税普通发票</option>
							</select> <span class="spanstar">*</span></td>
							<td width="20px" class="listbar"></td>
							<td></td>
						</tr>
						<tr>
							<td width="20px"></td>
							<td width="15%" style="text-align: right" class="listbar">发票代码:</td>
							<td width="25%"><input id="billId" name="billId" type="text"
								class="tbl_query_text" /> <span class="spanstar">*</span></td>
							<td width="15%" style="text-align: right" class="listbar"></td>
							<td></td>
						</tr>
						<tr>
							<td width="20px"></td>
							<td width="15%" style="text-align: right" class="listbar">发票起始号码:</td>
							<td width="25%"><input id="billStarNo" name="billStarNo"
								type="text" class="tbl_query_text" /> <span class="spanstar">*</span>
							</td>
							<td width="15%" style="text-align: right" class="listbar">发票终止号码:</td>
							<td width="25%"><input id="billEndNo" name="billEndNo"
								type="text" class="tbl_query_text" /> <span class="spanstar">*</span>
							</td>
						</tr>
						<tr style="height: 5px">
						</tr>
					</table>
				</div>
				<div id="ctrlbutton" class="ctrlbutton"
					style="border: 0px; margin-top: 3%">
					<input type="button" name="tijiao" class="tbl_query_button"
						onMouseMove="this.className='tbl_query_button_on'"
						onMouseOut="this.className='tbl_query_button'" onclick="jy();"
						name="BtnSave" value="保存" id="BtnSave" /> <input type="button"
						class="tbl_query_button" value="返回"
						onMouseMove="this.className='tbl_query_button_on'"
						onMouseOut="this.className='tbl_query_button'"
						onclick="submitForm('billInventoryList.action?type=back')">

				</div>
			</div>
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
	</div>
</body>
</html>