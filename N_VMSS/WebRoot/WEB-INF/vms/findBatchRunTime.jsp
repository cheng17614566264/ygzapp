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

/* $(document).ready(function(){
	
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
            
 			$.each(arr,function(i,T){
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
			}); 
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
} */



function updateBatchRunTime(cname,id){
	var name=cname;
	var hour=document.getElementById(id+"hour").value;
	var minute=document.getElementById(id+"minute").value;
	var second=document.getElementById(id+"second").value;
	var intervalHour=document.getElementById(id+"intervalHour").value;
	var intervalMinute=document.getElementById(id+"intervalMinute").value;
	var intervalSecond=document.getElementById(id+"intervalSecond").value;
	if(name == null) {
		alert("请选择跑批单位");
		return;
	}
	if(hour === " " || hour == null || isNaN(hour) || parseInt(hour,10)<0 || parseInt(hour,10)>23) {
		alert("请输入一个24小时制的小时(hour)");
		return;
	}
	if(minute === " " || minute == null || isNaN(minute) || parseInt(minute,10)<0 || parseInt(minute,10)>59) {
		alert("请输入一个0-59分钟(minute)");
		return;
	}
	if(second === " " || second == null || isNaN(second) || parseInt(second,10)<0 || parseInt(second,10)>59) {
		alert("请输入一个0-59数字(second)");
		return;
	}
	
	$.ajax({
		url:'updateBatchRunTime.action',
		data:{cname:name,hour:hour,minute:minute,second:second,intervalHour:intervalHour,intervalMinute:intervalMinute,intervalSecond:intervalSecond},
		dataType:"json",
		type:"post",
		success:function(xy,y,p){
			alert("设置成功");
			$(location).attr('href', window.location.href);
		},
		error:function(){
			alert("ERROR");
		} 
		
	});
	/* alert(1);
	document.forms[0].submit();
	alert(2); */
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
					<!-- <div class="widthauto1">
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
					</div> -->
					<div id="myclock" style="position:absolute;left:900px;top:45px;background-color:none " ></div>
					<table id="tbl_tools" width="100%" border="">
						<tr>
							<td >
								
							</td>
						</tr>
					</table>
					<%-- <div id="lessGridList1"
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
								<!-- <th style="text-align: center">间隔小时</th>
								<th style="text-align: center">间隔分钟</th>
								<th style="text-align: center">间隔秒钟</th> -->
								<th style="text-align: center">操作</th>
							</tr>
							
						</table>
						<!-- <table>
							<tr id="show"></tr>
							
						</table> -->
						<table style="margin-top: 30">
							<s:iterator value="batchRunTimelist" id="blist" status="struts">
							<tr>
								<td>
									<div style="padding-left:115px;font-size: 15px;padding-top:10px;padding-bottom:20px;">
										 <s:property value="#blist.cname" />
									</div>	
								</td>
								<td>
									<div style="padding-left:175px;">
										<input type="text"
										class="tbl_query_button" style="color:#666;"
										name="hour" value='<s:property value="#blist.hour" />' id='<s:property value="#blist.id"/>hour' />
									</div>	
								</td>
								<td>
									<div style="padding-left:146px;">
										<input type="text"
										class="tbl_query_button" style="color:#666;"
										name="minute" value='<s:property value="#blist.minute" />' id='<s:property value="#blist.id"/>minute' /> 
									</div>	
								</td>
								<td >
									<div style="padding-left:148px;">
										<input type="text"
										class="tbl_query_button" style="color:#666;"
										name="second" value='<s:property value="#blist.second" />' id='<s:property value="#blist.id"/>second' />
									</div>	
								</td>
								<td >
									<div style="padding-left:148px;">
										<input type="text"
										class="tbl_query_button" style="color:#666;"
										name="second" value='<s:property value="#blist.second" />' id='<s:property value="#blist.id"/>second' />
									</div>	
								</td>
								<td >
									<div style="padding-left:148px;">
										<input type="text"
										class="tbl_query_button" style="color:#666;"
										name="second" value='<s:property value="#blist.second" />' id='<s:property value="#blist.id"/>second' />
									</div>	
								</td>
								<td >
									<div style="padding-left:148px;">
										<input type="text"
										class="tbl_query_button" style="color:#666;"
										name="second" value='<s:property value="#blist.second" />' id='<s:property value="#blist.id"/>second' />
									</div>	
								</td>
								<td style='padding-left:100px; font-size:20px'>
									<div style="padding-left:50px;">
									    <input type='button' value='提交' style='height:30px;width:50px;' 
									    onclick="updateBatchRunTime('<s:property value="#blist.cname" />','<s:property value="#blist.id" />')" />
									</div>	
								</td>
							</tr>
							</s:iterator>
						</table>
					</div> --%>
					<div id="lessGridList4"
						style="width: 100%; height: 360px; overflow: auto;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align: center">单位</th>
								<th style="text-align: center">小时</th>
								<th style="text-align: center">分钟</th>
								<th style="text-align: center">秒钟</th>
								<th style="text-align: center">间隔小时</th>
								<th style="text-align: center">间隔分钟</th>
								<th style="text-align: center">间隔秒钟</th>
								<th style="text-align: center">操作</th>
							</tr>
							<s:iterator value="batchRunTimelist" id="blist"
								status="stuts">
								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>" >
									<td align="center"><s:property value="#blist.cname" /></td>
									<td align="center">
										<input type="text" class="tbl_query_button" style="color:#666;" 
										 name="hour" value='<s:property value="#blist.hour" />' id='<s:property value="#blist.id"/>hour' />
									</td>
									<td align="center">
										<input type="text" class="tbl_query_button" style="color:#666;"
										name="minute" value='<s:property value="#blist.minute" />' id='<s:property value="#blist.id"/>minute' />
									</td>
									<td align="center">
										<input type="text" class="tbl_query_button" style="color:#666;"
										name="minute" value='<s:property value="#blist.second" />' id='<s:property value="#blist.id"/>second' />
									</td>
									<td align="center">
										<input type="text" class="tbl_query_button" style="color:#666;" 
										 name="hour" value='<s:property value="#blist.intervalHour" />' id='<s:property value="#blist.id"/>intervalHour' />
									</td>
									<td align="center">
										<input type="text" class="tbl_query_button" style="color:#666;"
										name="minute" value='<s:property value="#blist.intervalMinute" />' id='<s:property value="#blist.id"/>intervalMinute' />
									</td>
									<td align="center">
										<input type="text" class="tbl_query_button" style="color:#666;"
										name="minute" value='<s:property value="#blist.intervalSecond" />' id='<s:property value="#blist.id"/>intervalSecond' />
									</td>
									<td align="center">
										<input type='button' value='提交' style='height:30px;width:50px;' 
									    onclick="updateBatchRunTime('<s:property value="#blist.cname" />','<s:property value="#blist.id" />')" />
									</td>
									
								</tr>
							</s:iterator>
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