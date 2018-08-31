<!-- 进项转出比例计算页面 -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@page import="cjit.crms.util.StringUtil"%>
<%@page import="com.cjit.vms.input.util.Util"%>
<%@page import="com.cjit.vms.input.model.Proportionality"%>
<%@page import="com.cjit.vms.input.model.SubjectEntity"%>
<%@page import="com.cjit.gjsz.datadeal.model.CodeDictionary"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.cjit.vms.input.model.InputInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.cjit.vms.input.model.InputInfoUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/editview.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>进项税转出比例计算</title>
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

.ellipsis_div {
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}
</style>
<script type="text/javascript">
//转出比例计算
$(function (){
	$("#cmdbljs").click(function (){
		$.ajax({
			url : 'reckonAction.action',// 跳转到 action    
			type : 'post',
			dataType : 'text',
			success : function(ajaxReturn) {
				var returnJson = $.parseJSON(ajaxReturn);
					if (returnJson.isNormal) {
						alert("转出比例计算已完成");
					}else{
						alert(returnJson.message);
					}
			},
			error : function(ajaxReturn) {
				var returnJson = $.parseJSON(ajaxReturn);
				if (returnJson.isNormal) {
					alert("转出比例计算已完成");
				}else{
					alert(returnJson.message);
				}
			}
			
		});
		submitForm("listInvoiceInSurtaxBLJS.action");
	});
	
	 var $FZ=$("td[name='FZ']");
	 var val=0;
	 $FZ.each(function(){
		 if(val==0){
			 val=$(this).val()*1;
		 }else{
			 val=val+$(this).val()*1;
		 }
	 });
	 
	 
});

	function submitForm(actionUrl) {
		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
		form.action = "listInvoiceInSurtaxBLSelect.action";
	}


	function showDetailInfoDIV(logID) {
		var currRow = window.event.srcElement.parentElement.parentElement;// 获取当前行

		document.getElementById("_td2").innerHTML = logID;

		for (var i = 3; i < 16; i++) {
			document.getElementById("_td" + i).innerHTML = currRow.cells[i - 1].title;
		}

		document.getElementById("detailInfoDIV").style.display = 'block';
	}
	/* 
	 *进项转出
	 */
	function transInput() {
		var ids = document.getElementsByName("billId");
		var selectedIds = null;
		if (ids.length > 0) {
			for (var i = 0; i < ids.length; i++) {
				var id = ids[i];
				var j = 0;
				if (id.checked) {
					if (selectedIds != null) {
						selectedIds = selectedIds + "," + id.value;
					} else {
						selectedIds = id.value;
					}
					j++;
				}
			}
		}
		if (selectedIds == null) {
			alert("请选择要转出的记录");
		}
		submitAction(document.forms[0], "transInputInfo.action?selectedIds="
				+ selectedIds);
		document.forms[0].action = "listInvoiceInSurtax.action";
	}
	function BtnSave(){
		$U_FZ = $("#U_FZ").val();
		$U_FM = $("#U_FM").val();
		/* if(true==checkfile()){ */
		if($U_FZ != "" && $U_FM != ""){	
			if(true==Checkinteger($U_FZ, "请输入数字")&&true==Checkinteger($U_FM, "请输入数字")){
				$("#U_rolloutResult").attr("value",Number($U_FZ*1/($U_FM*1)).toFixed(2));
				if(window.confirm("确定调整比例值！")){
					document.forms[0].action="<%=webapp%>/rolloutSGAudit.action?U_FZ="+$U_FZ+"&U_FM="+$U_FM ;
					document.forms[0].submit();
				}
			}
		}else{
			alert("请输入数值！");
		}
	}
	
	 function cmdRoll(){
		 $("#rolloutval").toggle();
		 $("#rolloutDiv").toggle();
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
				alert("请上传附件 ！");
				return false ;
			}else{
				if(count>0){
					alert("文件格式不对，请上传png/jpeg/pdf文件 ！");
					return false ;
				}
			}
			return true ;
	 }
	 
	 function  Checkinteger(obj,massage){
			if(!isNaN(obj)){
				return true;
			}else{
				alert(massage);
				return false;
			}
	}
	 
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
	//科目字典录入
	function OpenModalWindowSubmit(newURL,width,height,needReload) {
	   
		try{
			retData = window.showModalDialog(newURL, 
					  window, 
					  "dialogWidth:" + width
					+ "px;dialogHeight:" + height
					+ "px;center=1;scroll=1;help=0;status=0;");
		}catch (e) {
			retData=window.open(newURL, window, "dialogWidth:" + width
					+ "px;dialogHeight:" + height
					+ "px;center=1;scroll=1;help=0;status=0;");;
		}
		if(needReload && retData){
			window.document.forms[0].submit();
		}
	}
	//新增分子(免税)
	<% List<CodeDictionary> codeDictionary = (List<CodeDictionary>) request.getAttribute("codeDictionaryList"); %>
	var i=<%=codeDictionary.size()-1%>;
	function createDiv(){
		var str = "<tr id='createDiv"+(i+1)+"'><td style='text-align: center'><input type='checkbox' style='width: 13px; height: 13px;' name='billId' value='"+(i+1)+"' /></td> <td style='text-align: center'>分子(免税)</td> <td style='text-align: center'><input id='subjectName"+(i+1)+"' type='text' size='30' style='height:24px;margin:5px;text-align: center;' onblur='getSubjectLedgerMoney("+(i+1)+")' /></td> <td style='text-align: center'><span id='createDivspan"+(i+1)+"'>0</span></td> <td name='FZ' style='text-align: center' hidden><input id='subjectId"+(i+1)+"' type='text' value='0' /></td> </tr>";
		$("#createDiv"+i).after(str);
		i = i+1;
	}
	//删除分子(免税)
	function deleteDiv(){
		var checkboxId = document.getElementsByName("billId");
		var j=0;
		for(j = 0; j < checkboxId.length; j++){
			if(checkboxId[j].checked){
				var id=checkboxId[j].value;
				var subjectId = $("#subjectId"+id).val();
				$.ajax({
					type : "post",
					url : "deleteSubjectLedgerMoney.action",
					data:{subjectId:subjectId},
					dataType : "json",
					success : function(data) {
						if(id == 0){
							$("<input id='subjectId"+id+"' type='text' value='0' />").replaceAll("#subjectId"+id);
							$("<span id='createDivspan"+id+"'>0</span>").replaceAll("#createDivspan"+id);
							$("<input id='subjectName"+id+"' type='text' size='30' style='height:24px;margin:5px;text-align: center;' onblur='getSubjectLedgerMoney("+id+")' />").replaceAll("#subjectName"+id);
						}else{
							$("#createDiv"+id).remove();
						}
						var d = eval("("+data+")");
						alert(d.message);
					}
				});
			}
		}
	}
	//清空分子(免税)
	function clearDiv(){
		$.ajax({
			type : "post",
			url : "deleteSubjectLedgerMoney.action",
			data:{subjectId:"-1"},
			dataType : "json",
			success : function(data) {
				var d = eval("("+data+")");
				alert(d.message);
			}
		});
		$(location).attr('href', window.location.href);
	}
	//根据模糊查询获取此科目在总账中的余额
	function getSubjectLedgerMoney(id){
		var subjectName = $("#subjectName"+id).val();
		$.ajax({
			type : "post",
			url : "getSubjectLedgerMoney.action",
			data:{subjectName:subjectName},
			dataType : "json",
			success : function(data) {
				var d = eval("("+data+")");
				if(d.money == null){
					$("<span id='createDivspan"+id+"'>未查询到结果</span>").replaceAll("#createDivspan"+id);
					$("<input id='subjectId"+id+"' type='text' value='0' />").replaceAll("#subjectId"+id);
				}else{
					$("<span id='createDivspan"+id+"'>"+d.money+"</span>").replaceAll("#createDivspan"+id);
					$("<input id='subjectId"+id+"' type='text' value='"+d.id+"' />").replaceAll("#subjectId"+id);
				}
			}
		});
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
   $(function(){
	 	$("#cmdbljsTZ").click(function(){
	 		$("#rolloutDiv").toggle();
	 	})
   })
</script>
</head>
<body>
	<form id="main"
		action="<c:out value='${webapp}'/>/listInvoiceInSurtax.action"
		method="post" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">进项税管理</span> <span
							class="current_status_submenu">进项管理</span> <span
							class="current_status_submenu">进项转出比例计算</span>
					</div> <!-- 查询条件功能框 -->
					<div class="widthauto1"></div> <!-- 功能框 -->
					<table id="tbl_tools" width="100%" border="0">
						<tr align="left">
							<td style="text-align: left"><a href="javascript:void();"
								onclick="submitForm('exportExeclInvoiceBLJS.action')"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
							</a>
							<a href="javascript:void();"
								onclick="submitForm('generalUpdate.action')"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									总账数据更新
							</a>
							<a href="javascript:void();"
								onclick="OpenModalWindowSubmit('<c:out value="${webapp}"/>/insertSubjectDic.action',600,360,false)"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									科目字典录入
							</a>
							<a href="javascript:void();"
								onclick="OpenModalWindowSubmit('<c:out value="${webapp}"/>/findSubjectDic.action',600,360,false)"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									科目字典查看
							</a>
							<a href="javascript:void();"
								onclick="createDiv()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									新增分子(免税)
							</a>
							<a href="javascript:void();"
								onclick="deleteDiv()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									删除分子(免税)
							</a></td>
						</tr>
					</table> <!-- 展示区 -->
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align: center"><input
									id="CheckAll" style="width: 13px; height: 13px;"
									type="checkbox" onClick="checkAll(this,'billId')" /></th>
								<th style="text-align: center">数据类型</th>
								<th style="text-align: center">科目名称</th>
								<th style="text-align: center">贷方本位币发生总额</th>
								<!-- <th style="text-align: center">操作</th> -->
							</tr>
							<%-- <%   List<SubjectEntity> FZlist =(List<SubjectEntity>) request.getAttribute("FZ");
									for(int i=0;i<FZlist.size();i++){
										SubjectEntity subject = FZlist.get(i);
										
										//将中文转换成字母，防止URL报错
										/* String name = subject.getDirectionName();
										if("保费收入".equals(name)){
											name = "BFSR";
										}else{
											name = "TZSY";
										} */
										%> --%>
							
							<%
							List<Map> subjectEntityList = (List<Map>) request.getAttribute("resultList");
								if(subjectEntityList.size() > 0){
									for(int j=0;j<subjectEntityList.size();j++){
										Map code = subjectEntityList.get(j);
							%>
										<tr id="createDiv<%= j %>">
											<td style="text-align: center"><input type="checkbox"
												style="width: 13px; height: 13px;" name="billId"
												value="<%= j %>" /></td>
											<td style="text-align: center">分子(免税)</td>
											<td style="text-align: center"><input id="subjectName<%= j %>" type="text" size="30" style="height:24px;margin:5px;text-align: center;" value='<%=code.get("subjectName") %>' onblur="getSubjectLedgerMoney(<%= j %>)" /></td>
											<td style="text-align: center"><span id="createDivspan<%= j %>"><%=code.get("result") %></span></td>
											<td name="FZ" style="text-align: center" hidden><input id="subjectId<%= j %>" type="text" value='<%=code.get("subjectId") %>' /></td>
										</tr>
							<%
									}
								}else{
									
							%>
							
										<tr id="createDiv0">
											<td style="text-align: center"><input type="checkbox"
												style="width: 13px; height: 13px;" name="billId"
												value="0" /></td>
											<td style="text-align: center">分子(免税)</td>
											<%-- <td style="text-align: center"><%= subject.getDirectionName()%></td> --%>
											<td style="text-align: center"><input id="subjectName0" type="text" size="30" style="height:24px;margin:5px;text-align: center;" onblur="getSubjectLedgerMoney(0)" /></td>
											<td style="text-align: center"><span id="createDivspan0">0</span></td>
											<td name="FZ" style="text-align: center" hidden><input id="subjectId0" type="text" value="0" /></td>
										</tr>		
							
							<%
					
								}
							
							%>			
							
							<%-- <tr id="createDiv0">
								<td style="text-align: center"><input type="checkbox"
									style="width: 13px; height: 13px;" name="billId"
									value="0" /></td>
								<td style="text-align: center">分子(免税)</td>
								<td style="text-align: center"><%= subject.getDirectionName()%></td>
								<td style="text-align: center"><input id="subjectName0" type="text" size="30" style="height:24px;margin:5px;" onblur="getSubjectLedgerMoney(0)" /></td>
								<td style="text-align: center"><span id="createDivspan0">0</span></td>
								<td name="FZ" style="text-align: center" hidden><input id="subjectId0" type="text" value="0" /></td>
								<td style="text-align: center"><a
									href="invoiceSelectMassage.action?massages=1-<%= subject.getDirectionName()%>-FZ">
								<td style="text-align: center"><a
									href="invoiceSelectMassage.action?massages=1-<%= name%>-FZ">
										<img
										src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
										title="查看" style="border-width: 0px;" />
								</a></td>
							</tr> --%>
							<%-- <%
									
									List<SubjectEntity> FMlist =(List<SubjectEntity>) request.getAttribute("FM");
									for(int i=0;i<FMlist.size();i++){
										SubjectEntity subject = FMlist.get(i);
										
										//将中文转换成字母，防止URL报错
										String name = subject.getDirectionName();
										if("其他收入".equals(name)){
											name = "QTSR";
										}else if("利息收入".equals(name)){
											name = "LXSR";
										}else if("营业外支出".equals(name)){
											name = "YYWZC";
										}else if("保费收入".equals(name)){
											name = "BFSR";
										}else{
											name = "TZSY";
										}
							%>
							<tr>
								<td style="text-align: center"><input type="checkbox"
									style="width: 13px; height: 13px;" name="billId"
									value="<%= subject.getDirectionName()%>" /></td>
								<td style="text-align: center">分母(免税+应税)</td>
								<td style="text-align: center"><%= subject.getDirectionName()%></td>
								<td style="text-align: center"><%= subject.getCreditDescSum()==null?"0":subject.getCreditDescSum()%></td>
								<td name="FM" style="text-align: center" hidden><%= subject.getCreditDescSum()==null?"0":subject.getCreditDescSum()%></td>
								<td style="text-align: center"><a
									href="invoiceSelectMassage.action?massages=1-<%= subject.getDirectionName()%>-FM"> --%>
								<%-- <td style="text-align: center"><a
									href="invoiceSelectMassage.action?massages=1-<%= name%>-FM">
										<img
										src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
										title="查看" style="border-width: 0px;" />
								</a></td> --%>
							</tr>
							<tr>
								<td style="text-align: center"><input type="checkbox"
									style="width: 13px; height: 13px;" name="billId"
									value="0" /></td>
								<td style="text-align: center">分母(免税+应税)</td>
								<td style="text-align: center">总收入</td>
								<td style="text-align: center"><s:property value="generalLedgerResult.generalLedgerMoneyAll" /></td>
								<%-- <td style="text-align: center"><a
									href="invoiceSelectMassage.action?massages=1-<%= name%>-FM">
										<img
										src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
										title="查看" style="border-width: 0px;" />
								</a></td> --%>
							</tr>
							<%-- <%
									}
							%> --%>
						</table>
						<div></div>
						<div align="left" style="margin-top: 2%" id="rolloutval">
							<%-- <%   Proportionality prop = (Proportionality)request.getAttribute("proportionality"); %> --%>
							分子和:<input id="FZ"
								value="<s:property value="generalLedgerResult.generalLedgerMoneyOfTaxfree" />"
								readonly="readonly" /> 分母和:<input id="FM"
								value="<s:property value="generalLedgerResult.generalLedgerMoney" />"
								readonly="readonly" /> 数据类型：<input id="datasource" value="比例计算"
								readonly="readonly" /> 比例值：<input
								value="<s:property value="generalLedgerResult.generalLedgerResult" />"
								readonly="readonly" name="rolloutResult" /> <input type="button"
								id="cmdbljs" class="tbl_query_button" value="比例计算"
								onMouseMove="this.className='tbl_query_button_on'"
								onMouseOut="this.className='tbl_query_button'" />
						</div>
						<%-- <% if(StringUtil.isNotEmpty(prop.getResult())){
							%> --%>
						<div align="left" style="margin-top: 2%" id="rolloutvalSG">
							分子和:<input id="U_FZ" value="<s:property value="generalLedgerResultOfHand.generalLedgerMoneyOfTaxfree" />" /> 
							分母和:<input id="U_FM" value="<s:property value="generalLedgerResultOfHand.generalLedgerMoneyAll" />" />
							数据类型：<input id="U_datasource" value="手工调整" readonly="readonly" />
							比例值：<input value="<s:property value="generalLedgerResultOfHand.generalLedgerResult" />" id="U_rolloutResult" readonly="readonly" />
							<!-- <a id="cmdbljsTZ">
								* 上传附件
							</a> -->
							<input type="button" id="cmdblSg" class="tbl_query_button"
								value="手工调整" onMouseMove="this.className='tbl_query_button_on'"
								onMouseOut="this.className='tbl_query_button'"
								onclick="BtnSave()" /> <!-- <a id="cmdbljsTZ"> * 上传附件 </a> -->
						</div>
						<%-- <%
						} %> --%>
						<div align="left" style="margin-top: 1%" id="rolloutDiv" hidden>
							<div>
								<%-- <s:file id="theFile" name="theFile" size="30"
									style="height:26px; line-height:30px; "></s:file> --%>
								<!-- <input type="button" id="cmdRollBackBtnAdd"
									class="tbl_query_button"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" value="新增"
									onclick="buttonAdd()" /> <input type="button"
									id="cmdRollBackBtndown" class="tbl_query_button"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" value="删除"
									onclick="buttondown()" /> -->
								<!--	<input type="button" id="cmdRollBackBtnSave" class="tbl_query_button" value="保存" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="BtnSave()" style="margin-top: 3%"/>
						    	 	<input type="button" id="cmdRollBackBtnBackout" class="tbl_query_button" value="撤销" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="cmdRoll()" /> -->
							</div>
						</div>
						<input type="hidden" name="currentPage"
							value="<s:property value="paginationList.currentPage"/>" /> <input
							type="hidden" name="o_bill_id" id="o_bill_id" value="" />
					</div>
					<div id="anpBoud" align="Right" style="width: 100%;">
						<table width="100%" cellspacing="0" border="0">
						</table>
					</div>
				</td>
			</tr>
		</table>
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
</body>
<script type="text/javascript">
	$(".cmdViewBt").click(function() {
		$o_bill_id = $(this).attr("rel");
		$("#o_bill_id").val($o_bill_id);
		submitForm("<c:out value='${webapp}'/>/viewInvoiceScanAuth.action");
	});

	$(function() {
		$(".edit_insurtax").click(
				function() {
					bill_id = $(this).attr("rel");
					OpenModalWindow(
							"<c:out value='${webapp}'/>/editInvoiceInSurtax.action?bill_id="
									+ bill_id, 800, 600, true);
				});
		//转出提交
		$("#cmdRollOutSubmitBtn").click(function() {
			$billIds = $("input[name='billId']:checked");
			if ($billIds.size() == 0) {
				alert("请选择您要转出的数据");
				return false;
			}
			submitForm('rollOutSubmitInvoiceInSurtax.action');
		});
	});
	/* function sgrollout(){
		submitForm("<c:out value='${webapp}'/>/sgrollout.action");
	} */
	
</script>
</html>
