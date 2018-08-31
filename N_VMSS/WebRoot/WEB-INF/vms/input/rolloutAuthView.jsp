<!-- 手工转出比例添加页面 -->
<%@page import="com.cjit.vms.input.model.Proportionality"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<link href="<%=webapp%>/page/common.css" type="text/css"
	rel="stylesheet">

<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>进项税管理</title>

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
</style>
<script type="text/javascript">
	function submitForm(actionUrl){
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
	}
	
	function rolloutff(){
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
			$rollout=$("input[name='inputInfo.rollOutAmt']").val();
			//alert($rollout.length)
			if($rollout.length=0){
				alert("请输入比例值")
			}else{
				$.ajax({
					url : 'rollout.action',// 
					type : 'post',
					data : {selectedIds:selectedIds,rollout:$rollout},
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
			}
		}else{
			alert("请选择一条记录");
		}
	}
	
	$(function (){
		$("input[name='save']").click(function(){
			$rup =$("#rup").val();
			//alert($rup);
			$YearMonth=$("#YearMonth").val();
			$KjInstId=$("#KjInstId").val();
			$instid=$("#instid").val();
			$operateDate=$("#operateDate").val();
			if(true==Checkinteger($rup, "请输入数字")&&true==Checknull($rup, "不可为空")){
				$.ajax({
					url : 'rollout.action', 
					type : 'post',
					data : {rup:$rup,KjInstId:$KjInstId,YearMonth:$YearMonth,instid:$instid,operateDate:$operateDate},
					dataType : 'text',
					success : function(rest) {
						if("Y"==rest){
							alert("更新成功");
							submitForm("<c:out value='${webapp}'/>/listInvoiceInSurtax.action");
						}else{
							alert("已过比例计算时间，不可手工调整比例值");
							submitForm("<c:out value='${webapp}'/>/listInvoiceInSurtax.action");
						}
						/* var returnJson = $.parseJSON(ajaxReturn);
							if (returnJson.isNormal) {
								alert("更新成功");
								submitForm("<c:out value='${webapp}'/>/listInvoiceInSurtax.action");
							}else{
								alert(returnJson.message);
							} */
					},
					error : function(ajaxReturn) {
						/* var returnJson = $.parseJSON(ajaxReturn);
						if (returnJson.isNormal) {
							alert("更新失败");
						}else{
							alert(returnJson.message);
						} */
					}
				});
			}
		});
		
		$("input[name='update']").click(function (){
			var rs=confirm("确定更新比例值？");
			if(true==rs){
			$("#ry").hide();
			$("#rup").show();
			$("#rup").focus();
			}
		});
	})
	
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
</script>
</head>
<body>
	<form id="main"
		action="<c:out value='${webapp}'/>/listParamInSurtax.action"
		method="post">
		<input type="hidden" name="o_bill_id"
			value="<s:property value='o_bill_id'/>" /> <input type="hidden"
			name="billDate" value="<s:property value='billDate'/>" /> <input
			type="hidden" name="customerName"
			value="<s:property value='customerName'/>" /> <input type="hidden"
			name="datastatus" value="<s:property value='datastatus'/>" /> <input
			type="hidden" name="instId" value="<s:property value='instId'/>" /> <input
			type="hidden" name="billCode" value="<s:property value='billCode'/>" />
		<input type="hidden" name="billNo"
			value="<s:property value='billNo'/>" /> <input type="hidden"
			name="fapiaoType" value="<s:property value='fapiaoType'/>" /> <input
			type="hidden" name="identifyDate"
			value="<s:property value='identifyDate'/>" /> <input type="hidden"
			name="paginationList.currentPage"
			value="<s:property value='currentPage'/>" />
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td align="left">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">进项税管理</span> <span
							class="current_status_submenu">进项管理</span> <span
							class="current_status_submenu">进项转出</span> <span
							class="current_status_submenu">转出比例</span>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr style="margin-top: 10px;">
							<td align="left"><input type="button"
								class="tbl_query_button"
								onMouseMove="this.className='tbl_query_button_on'"
								onMouseOut="this.className='tbl_query_button'"
								onclick="submitForm('listInvoiceInSurtax.action');"
								name="cmdDelbt" value="返回" id="cmdDelbt" />
							</th>
							</td>
						</tr>
					</table>
					<div id="whitebox">
						<div class="boxline">基本信息</div>
						<table id="tbl_context" cellspacing="0" width="100%"
							align="center" cellpadding="0">
							<tr>
								<td align="left">
									<table id="contenttable" cellpadding="0" cellspacing="0"
										border="0" width="100%">
										<%    Proportionality proportionality=(Proportionality)request.getAttribute("proportionality"); %>
										<tr>
											<td width="15%" class="lessGrid"></td>
											<td>纳税主体编号</td>
											<td><input type="text" style="height: 21px;"
												class="tbl_query_text_readonly" name="billEndNo" id="instid"
												value="<%=proportionality.getInstId() %>" width="100px"
												readonly /></td>
											<td width="10%" class="lessGrid"></td>
										</tr>
										<tr>
											<td width="15%" class="lessGrid"></td>
											<td>纳税主体名称</td>
											<td><input type="text" style="height: 21px;"
												class="tbl_query_text_readonly" name="instName"
												id="instName" value="<%=proportionality.getInstName() %>"
												width="100px" readonly /></td>
											<td width="10%" class="lessGrid"></td>
										</tr>
										<tr>
											<td width="15%" class="lessGrid"></td>
											<td>会计机构编号</td>
											<td><input type="text" style="height: 21px;"
												class="tbl_query_text_readonly" name="kjInstId"
												id="KjInstId" value="<%=proportionality.getKjInstId() %>"
												width="100px" readonly /></td>
											<td width="10%" class="lessGrid"></td>
										<tr>
											<td width="15%" class="lessGrid"></td>
											<td>会计机构名称</td>
											<td><input type="text" style="height: 21px;"
												class="tbl_query_text_readonly" name="kjIstName"
												id="kjIstName" value="<%=proportionality.getKjIstName() %>"
												width="100px" readonly /></td>
											<td width="10%" class="lessGrid"></td>
										</tr>
										</tr>
										<tr>
											<td width="15%" class="lessGrid"></td>
											<td>会计月度</td>
											<td><input type="text" style="height: 21px;"
												class="tbl_query_text_readonly" name="yearMonth"
												id="YearMonth" value="<%=proportionality.getYearMonth() %>"
												width="100px" readonly /></td>
											<td width="10%" class="lessGrid"></td>
										</tr>
										<tr>
											<td width="15%" class="lessGrid"></td>
											<td>数据来源</td>
											<td>
												<%  if("0".equals(proportionality.getDatasource())){
										%> <input type="text" style="height: 21px;"
												class="tbl_query_text_readonly" name="datasource"
												id="datasource" value="比例计算" width="100px" readonly /> <%
									}else{ %> <input type="text" style="height: 21px;"
												class="tbl_query_text_readonly" name="datasource"
												id="datasource" value="手工调整" width="100px" readonly /> <%} %>
												<input type="text" style="height: 21px;"
												class="tbl_query_text_readonly" name="datasource"
												id="datasource"
												value="<%=proportionality.getDatasource() %>" width="100px"
												readonly hidden />
											</td>
											<td width="10%" class="lessGrid"></td>
										</tr>
										<tr>
											<td width="15%" class="lessGrid"></td>
											<td>比例值</td>
											<td><input id="rup" type="text" style="height: 21px;"
												class="tbl_query_text_readonly" name="billEndNo"
												id="BillDistribution.billEndNo" width="100px" hidden /> <input
												id="ry" type="text" style="height: 21px;"
												class="tbl_query_text_readonly"
												id="BillDistribution.billEndNo"
												value="<%=proportionality.getResult() %>" width="100px"
												readonly /></td>
											<td width="10%" class="lessGrid"></td>
										</tr>
										<tr>
											<td width="15%" class="lessGrid"></td>
											<td>操作日期</td>
											<td><input type="text"
												style="height: 21px; margin-right: 100px"
												class="tbl_query_text_readonly" name="operateDate"
												id="operateDate"
												value="<%=proportionality.getOperateDate() %>" width="100px"
												readonly /></td>
											<td width="10%" class="lessGrid"></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<div>
							<%if("Y".equals(request.getAttribute("mass"))){ %>
							<input name="update" type="button" value="修改"
								style="margin-left: 80%" class="tbl_query_button"
								onMouseMove="this.className='tbl_query_button_on'"
								onMouseOut="this.className='tbl_query_button'"> <input
								name="save" type="button" value="保存" style="margin-left: 10px"
								class="tbl_query_button"
								onMouseMove="this.className='tbl_query_button_on'"
								onMouseOut="this.className='tbl_query_button'">
							<%} %>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>