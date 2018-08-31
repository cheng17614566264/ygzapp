<!-- file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.text.SimpleDateFormat"%>
<%@ include file="../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
<script type="text/javascript">
	var cou = "";
	$(function(){
		$("#count2").attr("value",$("#count").val());
		cou =$("#count").val();
	})
	function button(val){
		$count = $("#count2").val();
		if("0" == $count && "1"==val){
			alert("已经到最后一张了!");
			return false ;
		}
		if(cou == $count && "0"==val){
			alert("已经到第一张了!");
			return false ;
		}
		if(document.getElementById("pdf2")!=null){
			var p=document.getElementById("pdf2").parentNode;
			p.removeChild(document.getElementById("pdf2"));
		}
		if(document.getElementById("img2")!=null){
			var pl=document.getElementById("img2").parentNode;
			pl.removeChild(document.getElementById("img2"));
		}
		$.ajax({
			url : 'viewImgFromInvoiceFJAjax.action',   // 跳转到 action    
			type : 'post',
			data : {index : $count,val:val},
			dataType : 'text',
			cache:false, 
		    ifModified :true ,
			success : function(date) {
				if(date!=null){
					$("#list1").hide();
					$("#list2").show();
					$("#count2").attr("value",date.split("-")[1]);
					 if("Y"==date.split("-")[0]){
						//pdf 
						$("#list2").append("<iframe id='pdf2' width='100%' height='100%' title='附件' src=''></iframe>");
						$("#pdf2").attr("src","<%= request.getContextPath() %>/NewFile.jsp?dl="+getNowFormatDate());
					 }else{
						$("#list2").append("<img id='img2' width='100%' height='100%' title='附件' src=''/>");
						$("#img2").attr("src","<%= request.getContextPath() %>/NewFile.jsp?dl="+getNowFormatDate());
						
					} 
				}
			},
			error : function(date) {
			
			}
		})
	}
	//当前时间
	function getNowFormatDate() {
	    var date = new Date();
	    var seperator1 = "-";
	    var seperator2 = ":";
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
	            + " " + date.getHours() + seperator2 + date.getMinutes()
	            + seperator2 + date.getSeconds();
	    return currentdate;
	}
	</script>
</head>
<body>
	<form name="Form1" method="post" action="" id="Form1">
		<input type="button" value="上一张" onclick="button(0)"> <input
			type="button" value="下一张" onclick="button(1)"> <input hidden
			id="count" value="<%= request.getAttribute("count")%>"> <input
			hidden id="count2" value="">
		<div style="width: 100%; height: 100%; vertical-align: middle;"
			id="list1" align="center">
			<% if ("Y".equals(request.getAttribute("imageType"))){
			%>
			<iframe id="pdf" width="100%" height="100%" title="附件"
				src="<%= request.getContextPath() %>/NewFile.jsp?<%= new Date()%>"></iframe>
			<%
		} else{%>
			<img id="img" width="100%" height="100%" title="附件"
				src="<%= request.getContextPath() %>/NewFile.jsp?<%= new Date()%>" />
			<%} %>
		</div>
		<div style="width: 100%; height: 100%; vertical-align: middle;"
			id="list2" align="center" hidden></div>
	</form>
</body>
</html>