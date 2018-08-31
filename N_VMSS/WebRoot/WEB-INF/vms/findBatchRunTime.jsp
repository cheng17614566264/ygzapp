<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
	
<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>


<script type="text/javascript">
function showClock()
{
 d       = new Date();
 hours   = d.getHours();
 minutes = d.getMinutes();
 seconds = d.getSeconds();
 clck = (hours>=12)?"下午":"上午";
 hours = (hours>12)?hours-12:hours;
 hours = (hours<10)?"0"+hours:hours; 
 minutes = (minutes<10)?"0"+minutes:minutes;
 seconds = (seconds<10)?"0"+seconds:seconds;
 time = clck + " " + hours + ":" + minutes + ":" + seconds;
 time = "<font size='5' face='Arial'><b>" + time + "</b></font>";
 myclock.innerHTML = time;
 setTimeout("showClock()",1000);
}

$(document).ready(function(){
	
	getBatchRunTime();
});


function getBatchRunTime(){
	var name=document.getElementById("cname").value;
	$.ajax({
		url:'findBatchRunTime.action',
		data:{cname:name},
		dataType:"json",
		type:"post",
		success:function(xy,y,p){
			var d = eval("("+xy+")");

            var s="<tr>"+
            " <td><div style='text-align:left;padding-left:130px;padding-top:50px; font-size:15px' id='cname'>"+d.cname+"</div></td>"+
            " <td><div style='text-align:left;padding-left:190px;padding-top:50px; font-size:20px'>"+d.hour+"</div></td>"+
            "<td><div style='text-align:left;padding-left:195px;padding-top:50px; font-size:20px'>"+d.minute+"</div></td>"+
            "<td><div style='text-align:left;padding-left:195px;padding-top:50px; font-size:20px'>"+d.second+"</div></td>"+
            "<td widtd='310' style='padding-left:195px; font-size:20px'>"+
            "</td>"+
          "</tr>"; 
          		
          	$("#show").append(s); 
            
/* 			$.each(arr,function(i,T){
				alert(i+"==="+f);
				alert(T.hour); 
				var s="<tr>"+
        "<td style='text-align:left; padding-left:20px;'><input type='checkbox' name='id[]' value='' />"+T.hour+"</td>"+
        "<td widtd='10%'>"+T.minute+"</td>"+
        "<td>"+T.second+"</td>"+
        "<td widtd='310'>编辑</td>"+
      "</tr>"; 
      		alert(s);
      	$("#show").append(s); 
			}); */
		},
		error:function(e){
			window.haha = e;
			alert(e);
			alert("又错啦");
		} 
		
	});
}


function ddupdateBatchRunTime(){
	$.ajax({
		url:'findBatchRunTime.action',
		data:{cname:"核心"},
		dataType:"json",
		type:"post",
		success:function(xy,y,p){

		},
		error:function(){
			alert("又错啦");
		}
		
	});
}



function updateBatchRunTime(){
	var name=document.getElementById("cname").value;
	var hour=document.getElementById("hour").value;
	var minute=document.getElementById("minute").value;
	var second=document.getElementById("second").value;
	
	/* $.ajax({
		url:'updateBatchRunTime.action',
		data:{cname:name,hour:hour,minute:minute,second:second},
		dataType:"json",
		type:"post",
		success:function(xy,y,p){
			getBatchRunTime();
		},
		error:function(){
			alert("又错啦");
		} 
		
	});*/
	alert(1);
	document.forms[0].submit();
	alert(2);
}









</script> 


<title>Insert title here</title>
</head>
<body onLoad="showClock()">
<!-- 开始 -->
	<form id="main" action="<c:out value='${webapp}'/>/updateBatchRunTime.action" method="post">
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<%-- <img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> --%>
						 <span class="current_status_menu">当前位置：</span> 
						 <span class="current_status_submenu1">参数管理</span> 
						 <span class="current_status_submenu">跑批时间管理</span> 
						 <span class="current_status_submenu">跑批时间设置</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td width="50">跑批单位</td>
								<td width="130"><input type="text" class="tbl_query_text"
									id="cname" name="cname"
									value="核心" /></td>
								
								<td style="text-align: left"><input type="button"
									class="tbl_query_button"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'"
									onclick="submitQueryForm('taxKeyInfoList.action?type=2');"
									name="cmdSelect" value="查询" id="cmdSelect" /></td>
							</tr>
						</table>
					</div>
					<div id="myclock" style="position:absolute;left:900px;top:100px;background-color:none " ></div>
					<table id="tbl_tools" width="100%" border="">
						<tr>
							<td >
								
							</td>
						</tr>
					</table>
					<div id="lessGridList1"
						style="width: 100%; height: 360px; overflow: auto;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0"
							style="border-collapse: collapse; width: 100%; margin-top: 5px;">
							<tr class="lessGrid head gridbr">
								<th width="3%" style="text-align: center"></th>
								<th style="text-align: center">单位</th>
								<th style="text-align: center">小时</th>
								<th style="text-align: center">分钟</th>
								<th style="text-align: center">秒钟</th>
								<th style="text-align: center">操作</th>
							</tr>
							
						</table>
						<table>
							<tr id="show">
						
						</table>
						<table style="margin-top: 30">
							<tr>
								<td>
									<div style="padding-left:131px;font-size: 15px">
										修改
									</div>	
								</td>
								<td>
									<div style="padding-left:175px;">
										<input type="text"
										class="tbl_query_button" style="background-color:;"
										name="hour" value="hour" id="hour" />
									</div>	
								</td>
								<td>
									<div style="padding-left:146px;">
										<input type="text"
										class="tbl_query_button"
										name="minute" value="minute" id="minute" /> 
									</div>	
								</td>
								<td >
									<div style="padding-left:148px;">
										<input type="text"
										class="tbl_query_button"
										name="second" value="second" id="second" />
									</div>	
								</td>
								<td widtd='310' style='padding-left:100px; font-size:20px'>
									<div style="padding-left:50px;">
									    <input type='button' value='提交' style='height:30px;width:50px;' onclick="updateBatchRunTime()" />
									</div>	
								</td>
							</tr>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="width: 100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>