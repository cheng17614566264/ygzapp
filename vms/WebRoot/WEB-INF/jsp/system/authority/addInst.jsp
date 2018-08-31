<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="fmss.dao.entity.LoginDO,fmss.common.util.Constants"%>

<%@include file="../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="../../common/include.jsp"%>
	<title>新增关联机构</title>
	<link type="text/css" href="<c:out value="${sysTheme}"/>/css/subWindow.css" rel="stylesheet">
	<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="css/easyui.css">
	<link rel="stylesheet" type="text/css" href="css/icon.css">
	<script type="text/javascript" src="js/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<c:out value='${webapp}'/>/js/jquery/region.js" charset="GBK"></script>
	<script language="javascript" type="text/javascript">
		//标识页面是否已提交
		var subed = false;
		function check(){
			//验证是否重复提交
			if (subed == true){
				alert("信息正在发送给服务器，请不要重复提交信息！");
				return false;
			}
			//机构编码不能为空
			/* if(fucCheckNull(document.getElementById("instId"),"业务机构代码不能为空")==false){
				return false;
			} */
			
			var objs=document.getElementsByName("inst.isHead");
			if(objs[0]&&objs[0].checked){
			       if(document.getElementById("parentInstId").value!=""){
			          alert("只有根机构才能设置为总行");
			          return;
			       }
			 }
			/* if(/\W/.test(document.getElementById("instId").value)){
				var m = new MessageBox(document.getElementById("instId"));
				m.Show("业务机构代码不能含数字字母之外的字符");
				return false;
			} */
			
			
			//机构编码超出长度, 机构编码超出指定长度,最大20字节
			/* if(fucCheckLength(document.getElementById("instId"),20,"业务机构代码超出指定长度,最大20字节")==false){
				return false;
			} */
			
			//机构名称不能为空
			/* if(fucCheckNull(document.getElementById("instName"),"业务机构名称不能为空")==false){
				return false;
			} */
			
			//机构名称超出长度, 机构名称超出指定长度,最大50字节
			/* if(fucCheckLength(document.getElementById("instName"),50,"业务机构名称超出指定长度,最大50字节")==false){
				return false;
			} */
			
			//财务或费控机构代码不能为空
			if(fucCheckNull(document.getElementById("costCenter"),"机构代码不能为空")==false){
				return false;
			}
			
			//财务或费控机构代码超出指定长度,最大20字节
			if(fucCheckLength(document.getElementById("costCenter"),20,"机构代码超出指定长度,最大20字节")==false){
				return false;
			}
			
			//财务或费控机构名称不能为空
			if(fucCheckNull(document.getElementById("costCenterName"),"机构名称不能为空")==false){
				return false;
			}
			
			//财务或费控机构名称超出指定长度,最大50字节
			if(fucCheckLength(document.getElementById("costCenterName"),50,"机构名称超出指定长度,最大50字节")==false){
				return false;
			}
			
			//财务或费控上级机构代码不能为空
			if(fucCheckNull(document.getElementById("parentInstId"),"不能为空，若无则填0")==false){
				return false;
			}
			
			//财务或费控上级机构代码超出指定长度,最大20字节
			if(fucCheckLength(document.getElementById("parentInstId"),20,"超出指定长度,最大20字节")==false){
				return false;
			}
			

			//获取机构级别id
			var instLevel=document.getElementById("instLevel").value;
			//获取来源
			var dsource=document.getElementById("dsource").value;
			
			subed=true;
			return subed;
		}
		
		function findOutSubmit() {		
			if(check()) {	
				document.formInst.action="insertInst.action";
				document.formInst.method="post";
				document.formInst.submit();
				document.getElementById('BtnSave').disabled=true;
			}
		}
		/* $("#BtnReturn").click=function(){
			alert("hiaah ");
			var instId =  document.getElementById("instId");
			var act = "instDeploy.action"+instId;
			document.formInst.action=act;
			document.formInst.method="post";
			document.formInst.submit();
			document.getElementById('BtnReturn').disabled=true;
		} */
		
	</script>
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">
<form name="formInst" id="formInst" action="saveInst.action" method="post">
<input type="hidden" name="editType" id="editType" value="<s:property value="editType"/>" />
<input type="hidden" name="inst.instPath" value="<s:property value="inst.instPath" />" /> 
<input type="hidden" name="inst.instLevel" value="<s:property value="inst.instLevel" />" /> 
<div id="editpanel">
<div id="editsubpanel" class="editsubpanel">
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="header">
		<th colspan="4" >
			新增机构
		</th>
	</tr>
	<%-- <tr>
		<td width="15%" align="right" class="listbar">业务机构代码:</td>
		<td width="35%">
			<input id="instId" name="instId" type="text" value="<s:property value="instIdDel"/>" readonly/>
			&nbsp;<span class="spanstar">*</span>
		</td>
		<td width="15%" align="right" class="listbar">业务机构名称:</td>
		<td>
			<input id="instName" name="instName" type="text"  /> 
			&nbsp;<span class="spanstar">*</span>
		</td>
	</tr> --%>
	<tr>
		<td align="right" class="listbar">财务或费控机构代码:</td>
		<td>
			<input id="instId" name="instId" type="hidden" value="<s:property value="instIdDel"/>" readonly/>
			<input id="costCenter" name="costCenter" type="text" />
           &nbsp;<span class="spanstar">*</span>
        </td>
		<td align="right" class="listbar">财务或费控机构名称:</td>
		<td>
			<input id="costCenterName" name="costCenterName" type="text"  /> 
			&nbsp;<span class="spanstar">*</span>
		</td>
		
	</tr>
	<tr>
		<td align="right" class="listbar">财务或费控上级机构代码:</td>
		<td>
		    <input id="parentInstId" name="parentInstId" type="text" />
		    &nbsp;<span class="spanstar">*</span>
        </td>
		<td align="right" class="listbar">机构级别:</td>
		<td>
			<select id="instLevel" name="instLevel">
				<option value="总部" selected>总部</option>
				<option value="一级机构" >一级机构</option>
				<option value="二级机构" >二级机构</option>
				<option value="三级机构" >三级机构</option>
			</select>
		</td>
	</tr>
	<tr>
		<td align="right" class="listbar">来源:</td>
		<td><select id="dsource" name="dsource">
				<option value="总账" selected>总账</option>
				<option value="费控" >费控</option>
			</select>
		</td>
	</tr>
	
</table>
</div>
</div>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">		
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="findOutSubmit()" name="BtnSave" value="保存" id="BtnSave"/>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"/>		
	</div>
</form>
</div>
</body>
</html>
