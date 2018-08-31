<!-- file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.text.SimpleDateFormat"%>
<%-- <%@ include file="../../../../page/include.jsp"%> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
	String webapp = request.getScheme() + "://" + request.getServerName() + ":"
		+ request.getServerPort() + path;
	pageContext.setAttribute("webapp", webapp);
  	/*String bopTheme=(String)application.getAttribute("themepath");
	pageContext.setAttribute("bopTheme",bopTheme);*/
	String sysTheme =(String)application.getAttribute("sysTheme");
	String bopTheme2 =(String)application.getAttribute("themepath");
	String bopTheme =(String)application.getAttribute("globalpath");
	//System.out.println("sysTheme === " + sysTheme);
	//System.out.println("bopTheme2 === " + bopTheme2);
	//System.out.println("bopTheme === " + bopTheme);
	if (bopTheme2 == null){
		// resin
		//bopTheme2 = "http://localhost:8086/fmss/theme/default";
		// tomcat
		bopTheme2 = "http://" + request.getServerName() + ":" + request.getServerPort() + "/vms/theme/default";
	}
	if (bopTheme == null){
		// resin
		//bopTheme = "http://localhost:8086/fmss";
		// tomcat
		bopTheme = "http://" + request.getServerName() + ":" + request.getServerPort() + "/vms";
	}
	//System.out.println("bopTheme2 === " + bopTheme2);
	//System.out.println("bopTheme === " + bopTheme);
	pageContext.setAttribute("bopTheme2",bopTheme2);
	pageContext.setAttribute("bopTheme",bopTheme);
%>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/Comm.js" charset="utf-8"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/validator.js" charset="uft-8"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js" charset="uft-8"></script>
<script type="text/javascript">
	var screenHeight = screen.availHeight;
</script>
<script language="javascript" type="text/javascript" charset="UTF-8">
	<%
	if(request.getParameter("RESULT_MESSAGE") !=null && !"".equals(request.getParameter("RESULT_MESSAGE"))){
	%>
		alert("<%=java.net.URLDecoder.decode(request.getParameter("RESULT_MESSAGE"),"utf-8")%>");
	<%
	}else if(request.getAttribute("RESULT_MESSAGE") !=null && !"".equals(request.getAttribute("RESULT_MESSAGE"))){	
	%>
	    alert("<%=java.net.URLDecoder.decode(request.getAttribute("RESULT_MESSAGE").toString(),"utf-8")%>");
	<%
	}else if(request.getParameter("resultMessages") !=null && !"".equals(request.getParameter("resultMessages"))){	
	%>
		alert("<%=request.getParameter("resultMessages")%>");
	<%
	}else if(request.getAttribute("resultMessages") !=null && !"".equals(request.getAttribute("resultMessages"))){	
	%>
	    alert("<%=request.getAttribute("resultMessages")%>");
	<%
	}	
	%>
	function Timeajax(){
				$.ajax({
							   type: "get",
							   async:true,
							   cache:false,
							   url: 'timeAjax.action',
							   //data:{billId:id},
							   dataType: 'html',
							   success : function(result){
								   alert(result);
							 
							   },
							   error:function(){
							   }
							 });
				setInterval(Timeajax,10000);
		}
</script>
<link type="text/css"
	href="<c:out value="${bopTheme}"/>/themes/css/default.css"
	rel="stylesheet" />
<link type="text/css"
	href="<c:out value="${bopTheme}"/>/themes/css/menu.css"
	rel="stylesheet" />
<script language="javascript" type="text/javascript" charset="UTF-8">
	function doSomething(){
		alert(1);
	}
window.onload = function(){
	var hightValue = screen.availHeight - 5 - 280;
	var hightValueStr = "height:"+ hightValue + "px";
	
	var hightValue1 = screen.availHeight - 5 - 313;
	var hightValueStr1 = "height:"+ hightValue1 + "px";
	
	var hightValue2 = screen.availHeight - 5 - 227;
	var hightValueStr2 = "height:"+ hightValue2 + "px";
	
	var hightValue3 = screen.availHeight - 5 - 395;
	var hightValueStr3 = "height:"+ hightValue3 + "px";
	
	var hightValue4 = screen.availHeight - 5 - 353;
	var hightValueStr4 = "height:"+ hightValue4 + "px";

	var hightValue5 = screen.availHeight - 5 - 400;
	var hightValueStr5 = "height:"+ hightValue5 + "px";
	
	var hightValue6 = screen.availHeight - 5 - 320;
	var hightValueStr6 = "height:"+ hightValue6 + "px";
	
	var hightValue7 = screen.availHeight - 5 - 340;
	var hightValueStr7 = "height:"+ hightValue7 + "px";
		
	var hightValue8 = screen.availHeight - 5 - 460;
	var hightValueStr8 = "height:"+ hightValue8 + "px";
		
	var hightValue9 = screen.availHeight - 5 - 245;
	var hightValueStr9 = "height:"+ hightValue9 + "px";
		
	var hightValue10 = screen.availHeight - 5 - 270;
	var hightValueStr10 = "height:"+ hightValue10 + "px";
	
	var hightValue12 = screen.availHeight - 5 - 280;
	var hightValueStr12 = "height:"+ hightValue12 + "px";
	
	var hightValue13 = screen.availHeight - 5 - 565;
	var hightValueStr13 = "height:"+ hightValue13 + "px";
	
	var widthValue14 = screen.availWidth - 264;
	var widthValueStr14 = "width:"+ widthValue14 + "px;height:"+hightValue1+"px;";
	
	var hightValue15 = screen.availHeight - 5 - 525;
	var hightValueStr15 = "height:"+ hightValue15 + "px";
	
	var hightValue16 = screen.availHeight - 5 - 240;
	var hightValueStr16 = "height:"+ hightValue16 + "px";
	
	var hightValue17 = screen.availHeight - 5 - 382;
	var hightValueStr17 = "height:"+ hightValue17 + "px";
	
	var widthValue18 = screen.availWidth - 264;
	var widthValueStr18 = "width:"+ widthValue18 + "px;height:"+hightValue5+"px;";
	
	var hightValue19 = screen.availHeight - 5 - 380;
	var hightValueStr19 = "height:"+ hightValue19 + "px";

	
	if (typeof(eval("document.all.lessGridList"))!= "undefined"){
		document.getElementById("lessGridList").setAttribute("style", hightValueStr);
	}
	if (typeof(eval("document.all.lessGridList1"))!= "undefined"){
		document.getElementById("lessGridList1").setAttribute("style", hightValueStr1);
	}
	if (typeof(eval("document.all.lessGridList2"))!= "undefined"){
		document.getElementById("lessGridList2").setAttribute("style", hightValueStr2);
	}
	if (typeof(eval("document.all.lessGridList3"))!= "undefined"){
		document.getElementById("lessGridList3").setAttribute("style", hightValueStr3);
	}
	if (typeof(eval("document.all.lessGridList4"))!= "undefined"){
		document.getElementById("lessGridList4").setAttribute("style", hightValueStr4);
	}
	if (typeof(eval("document.all.lessGridList5"))!= "undefined"){
		document.getElementById("lessGridList5").setAttribute("style", hightValueStr5);
	}
	if (typeof(eval("document.all.lessGridList6"))!= "undefined"){
		document.getElementById("lessGridList6").setAttribute("style", hightValueStr6);
	}
	if (typeof(eval("document.all.lessGridList7"))!= "undefined"){
		document.getElementById("lessGridList7").setAttribute("style", hightValueStr7);
	}
	if (typeof(eval("document.all.blankbox"))!= "undefined"){
		document.getElementById("blankbox").setAttribute("style", hightValueStr);
	}
	if (typeof(eval("document.all.lessGridList8"))!= "undefined"){
		document.getElementById("lessGridList8").setAttribute("style", hightValueStr8);
	}
	if (typeof(eval("document.all.lessGridList30"))!= "undefined"){
		document.getElementById("lessGridList30").setAttribute("style", hightValueStr); 
	}
	if (typeof(eval("document.all.whitebox"))!= "undefined"){
		document.getElementById("whitebox").setAttribute("style", hightValueStr9); 
	}
	if (typeof(eval("document.all.whitebox1"))!= "undefined"){
		document.getElementById("whitebox1").setAttribute("style", hightValueStr10); 
	}
	if (typeof(eval("document.all.lessGridList12"))!= "undefined"){
		document.getElementById("lessGridList12").setAttribute("style", hightValueStr12);
	}
	if (typeof(eval("document.all.lessGridList13"))!= "undefined"){
		document.getElementById("lessGridList13").setAttribute("style", hightValueStr13);
	}
	if (typeof(eval("document.all.lessGridList14"))!= "undefined"){
		document.getElementById("lessGridList14").setAttribute("style", widthValueStr14);
	}
	if (typeof(eval("document.all.lessGridList15"))!= "undefined"){
		document.getElementById("lessGridList15").setAttribute("style", hightValueStr15);
	}
	if (typeof(eval("document.all.lessGridList16"))!= "undefined"){
		document.getElementById("lessGridList16").setAttribute("style", hightValueStr16);
	}
	if (typeof(eval("document.all.lessGridList17"))!= "undefined"){
		document.getElementById("lessGridList17").setAttribute("style", hightValueStr17);
	}
	if (typeof(eval("document.all.lessGridList18"))!= "undefined"){
		document.getElementById("lessGridList18").setAttribute("style", widthValueStr18);
	}
	if (typeof(eval("document.all.lessGridList19"))!= "undefined"){
		document.getElementById("lessGridList19").setAttribute("style", hightValueStr19);
	}

	var indexFrameWidth = parent.parent.document.getElementById("indexFrame").offsetWidth;
	var leftWidth = parent.document.getElementById("leftFramePage").offsetWidth;
	var midWidth = parent.document.getElementById("midFramePage").offsetWidth;
	var mainHeight = parent.document.getElementById("mainFramePage").offsetHeight;
	var mainWidth = parent.document.getElementById("mainFramePage").offsetWidth;
	if(typeof(eval("document.all.rDiv"))== "undefined"
			&& $("div[id*='lessGridList']").width() < $("div[id*='lessGridList']").find("table").width()
			|| typeof(eval("document.all.rDiv")) == "undefined" && mainWidth < $("div[id*='lessGridList']").width() ){
		if($("div[id*='lessGridList']").width() > indexFrameWidth){
			$($("div[id*='lessGridList']").find("table")).wrap("<div id='rDiv' style='width:0px;height:auto;'></div>")
		}else{
			var hei = 0;
			if($("div[id*='lessGridList']").width() > indexFrameWidth - leftWidth - midWidth){
				hei = (indexFrameWidth - leftWidth - midWidth) * 0.985;
			}else{
				hei = $("div[id*='lessGridList']").width() * 0.985;
			}
			$($("div[id*='lessGridList']").find("table")).wrap("<div id='rDiv' style='width:"+ hei +"px;height:auto;'></div>")
		}
	}

	var heigth = 5;
	var anpBoudHeight = 0;
	$("div[id*='lessGridList']").siblings().each(function(){
		heigth += $(this).height();
	});

	if(typeof(eval("document.all.anpBoud"))!= "undefined"){
		anpBoudHeight = document.getElementById("anpBoud").offsetHeight;
		if(anpBoudHeight == 0){
			anpBoudHeight = 35;
		}
		$("div[id*='lessGridList']").attr("style","overflow: auto;height:"+(mainHeight - heigth - anpBoudHeight)+"px;");
	}else{
		$("div[id*='lessGridList']").attr("style","overflow: auto;height:"+(mainHeight - heigth)+"px;");
	}
}


$(function(){
	var ifrm = parent.document.getElementById("frame");
	$("#rDiv").attr("style","width:0px;height:auto;");
	var lessGridList = $("div[id*='lessGridList']").attr("id");
	var width = document.getElementById(lessGridList).offsetWidth;
	var table = $("div[id*='lessGridList']").find("div table");
	if(null!=ifrm){
		if(ifrm.cols!="230,10,*" || Number(table.width()) < Number(width)){
			$("#rDiv").attr("style","width:" + Number(width) * 0.978	 + "px;height:auto;");
		}
	}
	

});

function mess(flag) {
	var lessGridList = $("div[id*='lessGridList']").attr("id");
	if (flag == 2) {
		$("#rDiv").attr("style","width:0px;height:auto;");
		var width = document.getElementById(lessGridList).offsetWidth;
		$("#rDiv").attr("style","width:" + Number(width) * 0.978 + "px;height:auto;");
	} else {
		var width = document.getElementById(lessGridList).offsetWidth;
		$("#rDiv").attr("style","width:" + Number(width) * 0.978 + "px;height:auto;");
	}
}
</script>
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
			url : 'viewImgFromBillFJAjax.action',   // 跳转到 action    
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
			type="button" value="下一张" onclick="button(1)"> <input type="hidden"
			id="count" value="<%= request.getAttribute("count")%>"> <input
			type="hidden" id="count2" value="">
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