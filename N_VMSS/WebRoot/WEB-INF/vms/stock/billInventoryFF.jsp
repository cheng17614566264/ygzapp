<%@page import="com.cjit.vms.trans.util.DataUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.cjit.common.util.PaginationList"%>
<%@page import="com.cjit.vms.stock.entity.BillInventory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<link href="<%=webapp%>/page/subwindow.css" type="text/css"
	rel="stylesheet">
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js"
	type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>


<meta http-equiv="Pragma" content="no-cache" />
<base target="_self">
<title>纸质发票分发</title>
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

.unSelectBox {
	text-align: center;
	vertical-align: middle;
	height: 39px;
	overflow: hidden;
	moz-user-select: -moz-none;
	-moz-user-select: none;
	-o-user-select: none;
	-khtml-user-select: none; /* you could also put this in a class */
	-webkit-user-select: none; /* and add the CSS class here instead */
	-ms-user-select: none;
	user-select: none; /**禁止选中文字*/
}

.user_class {
	font-size: 12px;
	font-family: Arial, Verdana, 宋体;
	vertical-align: middle;
	border: 1px solid #CCCCCC;
	height: 26px;
	width: 126px;
	line-height: 24px;
	padding: 0 3px;
}

.page_used_invoice_num {
	font-size: 12px;
	font-family: Arial, Verdana, 宋体;
	vertical-align: middle;
	border: 1px solid #CCCCCC;
	height: 26px;
	width: 126px;
	line-height: 24px;
	padding: 0 3px;
}

#div {
	height: 100px;
}

.lessGrid {
	background-color: #ffffff;
	border-color: #ffffff;
	border: 0
}
</style>
<script type="text/javascript">
//发票总张数
var valuess=0;
var res=false;
var obj=null;
function massage(){
	$("select[name='kpyId']").empty(); 
	$("select[name='kpyName']").empty();
	var $value=$("input[name='instName']").val().split(" ")[0];
	$.ajax({
       url: 'massageinstId.action',
       type: 'POST',
       async: false,
       data: {instId: $value},
       dataType: 'text',
       timeout: 1000,
       error: function () {
           return false;
       },
       success: function (result) {
    	   obj = eval('(' + result + ')');
    	   for(var i in obj){
    		   if(i=="N"){
    			   alert("机构开票员信息不存在，请联系机构负责人以免造成损失！");
        		   return;
    		   }
    		    $("select[name='kpyId']").append("<option value="+i+">"+i+"</option>");
    	  	    $("select[name='kpyName']").append("<option value="+obj[i]+">"+obj[i]+"</option>")
    	  	    
    	  	    var arr=new Array();
    	  	   
    	    }
	    	 res=true;
	    	 ff();
	     
       }
    });
}
function setInsertName(){
	var select=$("select[name='kpyId']").val();
	$("select[name='kpyName']").empty();
	$("select[name='kpyName']").append("<option value="+obj[select]+">"+obj[select]+"</option>")
}

function ff(){
	 var $count=$(document.getElementById("billInventory.count"));
	 valuess=$count.val();
	 $count.attr("value","").css("border-color","red").focus();
}
function change(){
	var $billStartNo=$(document.getElementById("billInventory.billStartNo")).val();
	var $billEndNo=$(document.getElementById("BillDistribution.billEndNo"));
	var $count=$(document.getElementById("billInventory.count")).val();
	var newbillEndNo=$billStartNo*1+$count*1-1;
	$billEndNo.attr("value",newbillEndNo);
}
/*
 * [分发]按钮
 */
$(function(){
	$("#fenFa").click(function(){
		if(false==res){
			alert("信息不可为空");
			return;
		}else{
			if(true==Check()&&true==Checkinteger($(document.getElementById("billInventory.count")),"请输入正整数")){
				   if(true==Checknull($("#taxNo").val(),"税控钥匙号不可为空")){
					  var val= confirm("确定 分发   "+$(document.getElementById("billInventory.count")).val()+" 张？");
					 
					  if(val==true){
						   alert("分发成功")
					  	   document.forms[0].action="<%=webapp%>/doDistrubute.action?massage=frm&&valuess="+valuess;
						   document.forms[0].submit();
					   }
					 }
			}
		}
	})
})
// 类型验证
function  Checkinteger(obj,massage){
	var str=obj.val();
	str=str.replace(/^(\s)*|(\s)*$/g,"");
	var newPar=/^(-|\+)?\d+$/
	if(str.length>0 && newPar.test(str)==false)
	{
		alert(massage);
		obj.focus();
		return false;
	}else{
		return true;
	}
}
// 校验
function  Check(){
	 var count=$(document.getElementById("billInventory.count")).val();
	 var $kpyid=$("select[name='kpyId']").val()
	 var $kpyname=$("select[name='kpyName']").val()
	 if(count*1>valuess*1){
		 alert("发票数量不足 ！！发票共有  : "+valuess+" 张 ！！");
		 $(document.getElementById("billInventory.count")).focus();
		 return false;
	 }else if(count*1<=0){
		 alert("发票分发张数不可小于等于 0 ！！");
		 $(document.getElementById("billInventory.count")).focus();
		 return false;
	 }else if(obj[$kpyid]!=$kpyname){
		 alert("开票员编号与开票员名称不对应！")
		 return false;
	 }else if(count*1<=valuess*1&&0<count*1){
		 return true;
     }
}
// 判空校验
function Checknull(obj,massage){
	var str=obj;
	if(str.length<=0||str==""){
		alert(massage);
	 	return false;
	}else{
		return true;
	}
}
//[返回]按钮
function submitForm(actionUrl){
	document.forms[0].action = actionUrl;
	document.forms[0].submit();
	document.forms[0].action = '';
}
</script>
</head>
<body scroll="no" style="overflow: hidden;">
	<div class="showBoxDiv">
		<form id="frm"
			action="<c:out value='${webapp}'/>/doDistrubute.action?massage=frm"
			method="post">

			<div id="editpanel">
				<div id="editsubpanel" class="editsubpanel">
					<div style="width: 100%;">
						<div id="tbl_current_status">
							<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
								class="current_status_menu">当前位置：</span> <span
								class="current_status_submenu1">销项税管理</span> <span
								class="current_status_submenu">库存管理</span> <span
								class="current_status_submenu">发票管理</span> <span
								class="current_status_submenu">发票分发</span>
						</div>
						<div class="lessGrid">
							<table id="contenttable" class="lessGrid" cellspacing="0"
								width="100%" align="center" cellpadding="0">
								<tr>
									<td colspan="6" style="background-color: #004c7e; height: 45px"></td>
								</tr>
								<tr>
									<td colspan="6"></td>
								</tr>
								<tr>
									<td width="15%" class="lessGrid"></td>
									<td align="right" width="10%" class="lessGrid">报税机构</td>
									<td align="left" width="25%" class="lessGrid"><input
										style="height: 21px;" type="text"
										class="tbl_query_text_readonly" id="inst_Name" name="instName"
										value='<s:property value="billInventory.instId"/>'
										onclick="setOrg(this);" readonly="readonly"> <!--  <input type="text" style="height: 21px;" class="tbl_query_text_readonly" id="instId" name="instName" value=""> -->
										<span style="color: red" align="right">*</span></td>
									<td align="left" width="10%" class="lessGrid"><input
										type="button" class="tbl_query_button" value="获取详细"
										onMouseMove="this.className='tbl_query_button_on'"
										onMouseOut="this.className='tbl_query_button'" name="etInfo"
										id="getInfo" onclick="massage();" style="margin-left: 30%" />
									</td>
									<td width="25%" class="lessGrid"></td>
									<td class="lessGrid"></td>
								</tr>
								<tr>
									<td width="15%" class="lessGrid"></td>
									<td align="right" width="10%" class="lessGrid">开票员编号</td>
									<td align="left" width="25%" class="lessGrid">
										<%-- 	<input type="text" style="height: 21px;" class="tbl_query_text_readonly" name="kpyId" id="billDistribution.kpyId"  size="100" readonly/>--%>
										<select name="kpyId" id="billDistribution.kpyId"
										style="height: 21px; width: 125px" onclick="setInsertName()"></select>
									</td>
									<td style="height: 21px;" align="right" class="lessGrid">开票员名称</td>
									<td style="height: 21px;" align="left" class="lessGrid">
										<%-- 	<input type="text" style="height: 21px;" class="tbl_query_text_readonly" name="kpyName" id="billDistribution.kpyName"  size="100" readonly class="readonly"/> --%>
										<select name="kpyName" id="billDistribution.kpyName"
										style="height: 21px; width: 125px"></select>
									<td class="lessGrid"></td>
								</tr>
								<%  List list=(List)request.getAttribute("list") ;
				BillInventory billInventory=null;
				if(list!=null){
					 billInventory=(BillInventory)list.get(0);
				}
				%>
								<tr>
									<td width="15%" class="lessGrid"></td>
									<td align="right" class="lessGrid">发票类型</td>
									<td align="left" class="lessGrid">
										<% if(billInventory.getBillType().equals("0")){
			 %> <input id="billType" name="billType" type="text"
										style="height: 21px;" type="text"
										class="tbl_query_text_readonly" value="增值税专用发票" /> <% }else{%>
										<input id="billType" name="billType" type="text"
										style="height: 21px;" type="text"
										class="tbl_query_text_readonly" value="增值税普通发票" /> <%} %>
									</td>
									<td width="20px" style="text-align: right" class="listbar">税控钥匙编号</td>
									<td width="15%"><input id="taxNo" name="taxNo" type="text"
										style="height: 21px;" type="text"
										class="tbl_query_text_readonly" /> <span id="span"
										style="color: red" align="right">*</span></td>

									<td class="lessGrid"></td>
								</tr>
								<tr style="height: 21px;" class="row1">
									<td width="15%" class="lessGrid"></td>
									<td style="height: 21px;" align="right" class="lessGrid">发票代码</td>
									<td style="height: 21px;" align="left" class="lessGrid"><input
										style="height: 21px;" type="text"
										class="tbl_query_text_readonly" name="billId"
										id="billInventory.billId"
										value="<%= billInventory.getBillId() %>" size="100" readonly
										class="readonly" /></td>
									<td style="height: 21px;" align="right" class="lessGrid">来源库存编号</td>
									<td style="height: 21px;" align="left" class="lessGrid"><input
										style="height: 21px;" type="text"
										class="tbl_query_text_readonly" name="inventoryId"
										id="billInventory.inventoryId"
										value="<%= billInventory.getInventoryId() %>" size="100"
										readonly class="readonly" /></td>
									<td class="lessGrid"></td>
								</tr>
								<tr style="height: 21px;" class="row1">
									<td width="15%" class="lessGrid"></td>
									<td style="height: 21px;" align="right" class="lessGrid">当前发票起始号码</td>
									<td style="height: 21px;" align="left" class="lessGrid"><input
										style="height: 21px;" type="text"
										class="tbl_query_text_readonly" name="billStartNo"
										id="billInventory.billStartNo"
										value="<%= (int)billInventory.getCount()-Integer.valueOf(billInventory.getSyCount())+Integer.valueOf(billInventory.getBillStartNo())%>"
										size="100" readonly /></td>
									<td style="height: 21px;" align="right" class="lessGrid">发票截止号码</td>
									<td style="height: 21px;" align="left" class="lessGrid"><input
										type="text" style="height: 21px;"
										class="tbl_query_text_readonly" name="billEndNo"
										id="BillDistribution.billEndNo"
										value="<%= billInventory.getBillEndNo() %>" size="100"
										readonly /></td>
									<td class="lessGrid"></td>
								</tr>
								<tr>
									<td width="15%" class="lessGrid"></td>
									<td style="height: 21px;" align="right" class="lessGrid">发票分发张数</td>
									<td><input input type="text" style="height: 21px;"
										class="tbl_query_text_readonly" name="count"
										id="billInventory.count"
										value="<%= billInventory.getSyCount()%>" size="100"
										onchange="change()" /> <span id="span" style="color: red"
										align="right">*</span></td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
								<%
	
	%>
							</table>
							<div></div>
							<div style="margin-top: 2%">
								<input type="button" class="tbl_query_button" value="分发"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="fenFa"
									id="fenFa" style="margin-left: 70%" /> <input type="button"
									class="tbl_query_button" value="返回"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="retrue"
									id="retrue" onclick="submitForm('billInventoryList.action')" />
							</div>
							<div style="margin-top: 3%"></div>
						</div>
					</div>
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