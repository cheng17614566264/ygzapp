<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="../../../page/include.jsp"%>
	<title>科目字典录入</title>
	<link type="text/css" href="<%=webapp%>/page/subWindow.css" rel="stylesheet">
	<link href="<%=webapp%>/page/subWindow.css" type="text/css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="css/easyui.css">
	<link rel="stylesheet" type="text/css" href="css/icon.css">
	<script type="text/javascript" src="<%=webapp%>/page/js/page/jquery.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/SimpleTree/js/validator.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/page/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/region.js" charset="GBK"></script>
	<script language="javascript" type="text/javascript">
		//标识页面是否已提交
		var subed = false;
		function check(){
			//验证是否重复提交
			if (subed == true){
				alert("信息正在发送给服务器，请不要重复提交信息！");
				return false;
			}
			if(document.getElementById("subjectId").value == null || document.getElementById("subjectId").value ==""){
				alert("科目代码不能为空！");
				return false;
			}
			if(document.getElementById("subjectName").value == null || document.getElementById("subjectName").value ==""){
				alert("科目名称不能为空！");
				return false;
			}
			if(document.getElementById("category").value == null || document.getElementById("category").value ==""){
				alert("科目类别不能为空！");
				return false;
			}
			if(document.getElementById("validState").value == null || document.getElementById("validState").value ==""){
				alert("是否有效不能为空！");
				return false;
			}
			if(document.getElementById("ratio").value == null || document.getElementById("ratio").value ==""){
				alert("比率不能为空！");
				return false;
			}
			subed=true;
			return subed;
		}
		/* function checkId(){
			var c = false;
			$.ajax({
				type : "Post",
				url : "saveSubjectDicBeforeCheckId.action?subjectId=" +document.getElementById("subjectId").value,
				dataType : "text",
				success : function(data) {
					if(data=="true"){
						c = true;
					}
				}
			}
			return c;
		} */
		function findOutSubmit() {
			if(check()) {
				document.formInst.action="saveSubjectDic.action";
				document.formInst.method="post";
				document.formInst.submit();
				document.getElementById('BtnSave').disabled=true;
			}
		}
		
	</script>
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">
<form name="formInst" id="formInst" action="saveSubjectDic.action" method="post">
<div id="editpanel">
<div id="editsubpanel" class="editsubpanel">
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="header">
		<th colspan="4" >
			科目字典录入
		</th>
	</tr>
	<tr>
		<td width="15%" align="right" class="listbar">科目代码:</td>
		<td width="55%">
			<input id="subjectId" name="subjectId" type="text" size="50" placeholder="请输入科目代码"
			style="height:24px;margin:5px;" />
			&nbsp;<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td width="15%" align="right" class="listbar">科目名称:</td>
		<td>
			<input id="subjectName" name="subjectName" type="text" size="50" placeholder="请输入科目名称" 
			style="height:24px;margin:5px;"/>
			&nbsp;<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td align="right" class="listbar">科目类别:</td>
		<td>
			<input id="category" name="category" type="text" size="50" placeholder="请输入科目类别 --1:应税科目 ;2:免税科目"
			 style="height:24px;margin:5px;"/>
			&nbsp;<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td align="right" class="listbar">是否有效:</td>
		<td>
			<input id="validState" name="validState" type="text" size="50" placeholder="是否有效--1:有效 ;0:无效" 
			style="height:24px;margin:5px;"/>
			&nbsp;<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td align="right" class="listbar">比率:</td>
		<td>
			<input id="ratio" name="ratio" type="text" size="50" placeholder="请输入比率--其他科目该列值为1，营业外支出为0.06" 
			style="height:24px;margin:5px;"/>
			&nbsp;<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td align="right" class="listbar">备注:</td>
		<td>
			<input id="remark" name="remark" type="text" size="50" placeholder="请输入备注" 
			style="height:24px;margin:5px;"/>
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
