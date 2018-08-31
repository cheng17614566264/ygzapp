<%@page import="com.cjit.vms.stock.entity.LostRecycle"%>
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
var flag="";
// [返回]按钮
function submitForm(actionURL){
	document.forms[0].action = "<%=webapp%>/billTrackaction.action"
 	document.forms[0].submit();
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
	var startno=$("#billStarNoX").val();
	if($("#billStarNo").val()*1!=startno*1){
		if($("#billStarNo").val()*1<startno*1||$("#billStarNo").val()*1<0){
			 	alert("输入的发票起始号超出票据范围");
				return false;
		}
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
	var endno=$("#billEndNoX").val();
	if($("#billEndNo").val()*1!=endno*1){
		if($("#billEndNo").val()*1>endno*1||$("#billEndNo").val()*1<0){
			alert("输入的发票截止号超出票据范围");
			return false;
		}
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
	if(Checknull($("#kpyName").val(),"开票员信息不可为空")==false){
		return false;
	}
	if(Checknull($("#kpyId").val(),"开票员信息不可为空")==false){
		return false;
	}
	// 票据数
	if(Checknull($("#count").val(),"数量统计不正确")==false){
		return false;
	}
	// 标志
	if(Checknull($("#flag").val(),"标志不可为空")==false){
		return false;
	}
	//备注校验
	if("2"==$("#flag").val()){
	    var $mark = $("#mark").val();
	    if($mark.replace(/(^\s*)|(\s*$)/g,"")==""){
	    	alert("请填写申请作废原因！")
	    	return false ;
	    }
		return true ;
	}
	var $billEndNo= $("#billEndNo").val();
	var $billStarNo=$("#billStarNo").val();
	$("#count").attr("value",$billEndNo*1-$billStarNo*1+1)
	return true;
}

// 数量  校验
function change(){
	var $billEndNo= $("#billEndNo").val();
	var $billStarNo=$("#billStarNo").val();
	$("#count").attr("value",$billEndNo*1-$billStarNo*1+1)
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
// [提交]按钮    及票据信息校验 
function checkjy(){
	$billType=$("#billType").val();
	$billId=$("#billId").val();
	$billStarNo=$("#billStarNo").val();
	$billEndNo=$("#billEndNo").val();
	var bool ="N"
	 $.ajax({
         url: 'checkjy.action',
         type: 'POST',
         async: false,
         data: {billType:$billType ,billId:$billId ,billStarNo:$billStarNo ,billEndNo:$billEndNo},
         dataType: 'text',
         timeout: 1000,
         error: function () {
             return false;
         },
         success: function (result) {
        	 if(true==Check()&&true==checkfile()){
        			if("N"==result){
        				alert("信息重叠，请查验后操作")
        			}else{
        				if(true==Check()){
        					flag=$("#flag").val();
        					if(flag=="0"){
        						flag="遗失"
        					}else if(flag=="1"){
        						flag="回收"
        					}else{
        						flag="作废"
        					}
        					var val=confirm("确定将：发票起始号"+$("#billStarNo").val()
        											+" - 截止号"+$("#billEndNo").val()+"进行"+flag+"操作？");
        					if(val&&true==checkfile()){
								 document.forms[0].action = "<%=webapp%>/savelostRecycle.action"
								 document.forms[0].submit();
								 submitForm("");
								 bool="Y";
        					}
        				}
        			}
        		}
         }
     });
	if("Y"==bool){
		 var fileName = $("#billId").val()+"-"+$("#billStarNo").val()+"-"+$("#billEndNo").val()
		 document.forms[1].action = "<%=webapp%>/billTrackUploaAaction.action?fileName="+fileName
		 document.forms[1].submit();
	}
}

function ff(){
	$("#billStarNo").attr("value","");
}
function fff(){
	$("#billEndNo").attr("value","");
}

function kzp(){
	if("2"==$("#flag").val()){
		$("tr[name='trq']").show();
	}else{
		$("tr[name='trq']").hide();
	}
}

</script>
<script type="text/javascript">
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
<body>
	<div class="showBoxDiv">
		<form id="frm"
			action="<c:out value='${webapp}'/>/billTrackaction.action"
			method="post">
			<div id="editsubpanel" class="editsubpanel">
				<div id="tbl_current_status">
					<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
						class="current_status_menu">当前位置：</span> <span
						class="current_status_submenu1">销项税管理</span> <span
						class="current_status_submenu">库存管理</span> <span
						class="current_status_submenu">发票遗失回收作废申请</span> <span
						class="current_status_submenu">数据录入</span>
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
							<td width="25%"><input id="instId" name="instId" type="text"
								class="tbl_query_text"
								value="<%=request.getAttribute("instId")+" - "+request.getAttribute("instName")%>" />
								<span class="spanstar">*</span></td>
							<td width="20px" class="listbar" style="text-align: right">开票员名称</td>
							<td><input id="kpyName" name="kpyName" type="text"
								class="tbl_query_text"
								value="<%= request.getAttribute("name")%>" readonly /> <span
								class="spanstar">*</span></td>
						</tr>
						<%   LostRecycle lostRecycle=(LostRecycle)request.getAttribute("lostRecycle"); %>
						<tr>
							<td width="10px"></td>
							<td width="15%" style="text-align: right" class="listbar">发票类型:</td>
							<td width="25%">
								<% if(lostRecycle.getBillType().equals("0")){
					
					%> <input id="billType" name="billType" type="text"
								class="tbl_query_text" value="增值税专用发票" /> <%
				} else{%> <input id="billType" name="billType" type="text"
								class="tbl_query_text" value="增值税普通发票" /> <%
				}
			   %> <span class="spanstar">*</span>
							</td>
							<td width="20px" class="listbar" style="text-align: right">开票员编号</td>
							<td><input id="kpyId" name="kpyId" type="text"
								class="tbl_query_text" value="<%= request.getAttribute("id")%>"
								readonly /> <span class="spanstar">*</span></td>
						</tr>
						<tr>
							<td width="10px"></td>
							<td width="15%" style="text-align: right" class="listbar">标志:</td>
							<td width="25%"><s:select id="flag" name="flag"
									list="#{'0':'空白遗失','1':'空白回收','2':'空白作废'}" headerKey=""
									headerValue="" listKey='key' listValue='value'
									cssClass="tbl_query_text" onclick="kzp()" /> <span
								class="spanstar">*</span></td>
							<td width="15%" style="text-align: right" class="listbar">发票代码:</td>
							<td width="25%"><input id="billId" name="billId" type="text"
								class="tbl_query_text" value="<%= lostRecycle.getBillId() %>"
								readonly /> <input id="billId" name="billId" type="text"
								class="tbl_query_text" value="<%= lostRecycle.getBillId() %>"
								hidden /> <span class="spanstar">*</span></td>

						</tr>
						<tr>
							<td width="10px"></td>
							<td width="15%" style="text-align: right" class="listbar">发票起始号码:</td>
							<td width="25%"><input id="billStarNo" name="billStarNo"
								type="text" class="tbl_query_text"
								value="<%= lostRecycle.getBillStartNo()%>" onclick="ff()" /> <input
								id="billStarNoX" name="billStarNo" type="text"
								class="tbl_query_text"
								value="<%= lostRecycle.getBillStartNo()%>" hidden /> <span
								class="spanstar">*</span></td>
							<td width="15%" style="text-align: right" class="listbar">发票截止号码:</td>
							<td width="25%"><input id="billEndNo" name="billEndNo"
								type="text" class="tbl_query_text"
								value="<%= lostRecycle.getBillEndNo()%>" onchange="change()"
								onclick="fff()" /> <input id="billEndNoX" name="billEndNo"
								type="text" class="tbl_query_text"
								value="<%= lostRecycle.getBillEndNo()%>" onchange="change()"
								hidden /> <span class="spanstar">*</span></td>
						</tr>
						<tr>
							<td width="10px"></td>
							<td width="20px" style="text-align: right" class="listbar">分发编号</td>
							<td width="15%"><input id="billId" name="disid" type="text"
								class="tbl_query_text" value="<%= lostRecycle.getDisId()%>"
								readonly /></td>
							<td width="15%" style="text-align: right" class="listbar">数量统计:</td>
							<td width="25%"><input id="count" name="count" type="text"
								class="tbl_query_text" readonly
								value="<%= Integer.valueOf(lostRecycle.getBillEndNo())-Integer.valueOf(lostRecycle.getBillStartNo())+1 %>" />
								<span class="spanstar">*</span></td>
						</tr>
						<tr name="trq" hidden>
							<td width="10px"></td>
							<td width="20px" style="text-align: right" class="listbar">备注</td>
							<td width="15%"><textarea name="mark" id="mark" cols="50"
									rows="1" style="height: 65px">
			</textarea></td>
							<td width="20px" style="text-align: right" class="listbar"></td>
							<td width="15%"></td>
						</tr>
						<tr style="height: 5px">
						</tr>
					</table>
				</div>
			</div>
		</form>
		<form id="frm1"
			action="<c:out value='${webapp}'/>/billTrackUploaAaction.action"
			method="post" enctype="multipart/form-data">
			<div id="upload">
				<div class="ctrlbutton" style="border: 0px; margin-top: 3%">
					<s:file id="theFile" name="theFile" size="30"
						style="height:26px; line-height:30px; "></s:file>
					<input type="button" id="cmdRollBackBtnAdd"
						class="tbl_query_button"
						onMouseMove="this.className='tbl_query_button_on'"
						onMouseOut="this.className='tbl_query_button'" value="新增"
						onclick="buttonAdd()" /> <input type="button"
						id="cmdRollBackBtndown" class="tbl_query_button"
						onMouseMove="this.className='tbl_query_button_on'"
						onMouseOut="this.className='tbl_query_button'" value="删除"
						onclick="buttondown()" />
				</div>
			</div>
		</form>
		<div id="ctrlbutton" class="ctrlbutton"
			style="border: 0px; margin-top: 3%">
			<input type="button" class="tbl_query_button"
				onMouseMove="this.className='tbl_query_button_on'"
				onMouseOut="this.className='tbl_query_button'" onclick="checkjy()"
				name="BtnSave" value="保存" id="BtnSave" /> <input type="button"
				class="tbl_query_button" value="返回"
				onMouseMove="this.className='tbl_query_button_on'"
				onMouseOut="this.className='tbl_query_button'"
				onclick="submitForm()">
		</div>
	</div>
</body>
</html>