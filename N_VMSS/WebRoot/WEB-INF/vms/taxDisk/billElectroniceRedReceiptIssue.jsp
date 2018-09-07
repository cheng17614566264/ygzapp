<!--file: <%=request.getRequestURI() %> -->
<%@page import="com.cjit.vms.trans.model.RedReceiptApplyInfo"%>
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<OBJECT ID=sk CLASSID="clsid:003BD8F2-A6C3-48EF-9B72-ECFD8FC4D49F"
	codebase="NISEC_SKSCX.ocx#version=1,0,0,1" style="display: none"></OBJECT>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/lhb.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/window.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/taxSelver/currentbill.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/taxSelver/validatorTax.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/taxSelver/compareCurrentBill.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/taxSelver/billCancel.js"></script>
<script type="text/javascript">
		// [查询]按钮
		function search(){
			//document.forms[0].submit();
			submitAction(document.forms[0], "listElectronicsRedReceiptIssue.action");
			document.forms[0].action="listElectronicsRedReceiptIssue.action";
		}
		//导出按钮
		function exportExcel(){
			submitAction(document.forms[0], "redElectronicsReceiptBillIssueToExcel.action?type=redReceiptExcel");
			document.forms[0].action="listElectronicsRedReceiptIssue.action";
		}
		// 开具按钮
		function issueRedBillDisk(){
			if(checkChkBoxesSelected("selectBillIds")){
			//税控盘号查询
				var taxDiskNo ="";
				try {
					taxDiskNo = DocCenterCltObj.FunGetPara('','taxDiskNo');
				} catch (e) {
					return alert("请安装税控插件!!");					
				}
				if (taxDiskNo == null) {
					return alert("请连接税控盘!!");
				} 
				
				var chkBoexes= document.getElementsByName("selectBillIds");
				var j = 0;
				var billId="";
				for(i=0;i<chkBoexes.length;i++){
					if(chkBoexes[i].checked){
						j++;
						billId = chkBoexes[i].value;
					}
					if(j>1){
						alert("请选择单条记录进行操作！");
						return false;
					}
				}
				
				alert("税控盘号：" + taxDiskNo);
				//获取选中的billId、数量、发票类型
				var billIds = document.getElementsByName("selectBillIds");
				var fapiaoTypes = document.getElementsByName("fapiaoTypes");
				var invalidInvoiceNum = document.Form1.invalidInvoiceNum.value;
				var fapiaoType = fapiaoTypes[0].value;
				var billId = billIds[0].value;
				
				//转action查询信息
				$.ajax({url: 'getRegisteredInfo.action',
						type: 'POST',
						async:false,
						data:{taxDiskNo:"1060",fapiaoType:fapiaoType},
						dataType: 'text',
						timeout: 1000,
						error: function(){
							return false;},
						success: function(result){
							alert("查询发票库存发送数据：" + result);
							if (result == ''){
								alert("查询信息失败。");
								return false;
							} else if (result == 'lock') {
								alert("不能同时操作数据。");
								return false;
							} else if (result == 'registeredInfoError') {
								alert("获取税控盘注册码失败。");
								return false;
							} else if (result == 'taxPwdError') {
								alert("获取税控盘口令和证书口令失败。");
								return false;
							} else  {
								//数控盘查询发票库存量surplusInvoice
								var invoiceStockNum = DocCenterCltObj.FunGetPara(result,'surplusInvoice');
						/*		if (eval(invoiceStockNum) < eval(selectNum + parseInt(invalidInvoiceNum))) {
									alert("库存不足，无法开具。");
									return false;
								}
						*/		
								alert("发票库存数量:" + invoiceStockNum);
								getIssueRedBillInfo(result, billId);
							}
						}
					});
					document.forms[0].action="billRedIssueList.action";
			}else{
				alert("请选择票据记录！");
			}
		}
		
		
		
		
		
		
		function getIssueRedBillInfo(preStr, billId) {
			//获取选中的billId、数量、发票类型
			if(!confirm("是否确认开具？")){
				return false;
			}
			
			//转action查询信息
			$.ajax({url: 'issueRedBill.action',
					type: 'POST',
					async:false,
					data:{preStr:preStr,billId:billId},
					dataType: 'text',
					timeout: 1000,
					error: function(){
						return false;},
					success: function(result){
						if (result == ''){
							alert("查询信息失败。");
							return false;
						} else if (result == 'billItemError') {
							alert("无法查询发票对应的商品。");
							return false;
						} else if (result == 'billItemNum') {
							alert("发票对应的商品数量不能超过9个。");
							return false;
						} else  {
							alert("红票开具发送数据：" +  result);
							var issueRes = DocCenterCltObj.FunGetPara(result,'issueInvoice');
							alert("红票开具结果：" + issueRes);
							updateIssueRedBillResult(issueRes);
						}
					}
				});
				document.forms[0].action="billRedIssueList.action";
		}
		
		
		
		function issueRedBill(){
			if (!checkChkBoxesSelected("selectBillIds")) {
				alert("请选择票据记录");
				return false;
			}
			checkDiskAndSelver("",BillRedIssueSelvlet());
			
		}
		
		/*
发票开具 服务
*/

		/* function BillRedIssueSelvlet(){
			var billIds = document.getElementsByName("selectBillIds");
			var fapiaoTypes = document.getElementsByName("fapiaoTypes");
			var fapiaoType = fapiaoTypes[0].value;
			// alert(fapiaoType);
				var ids = "";
				var selectNum = 0;
				var j=0;
				var a=0;
				for(var i=0;i<billIds.length;i++){
					if (billIds[i].checked){
						a++;
					}
				}
				a=a-1;
				for (var i = 0; i < billIds.length; i++){
					//alert("11");
					if (billIds[i].checked){
						var aa=busineessBillCancel();
					//	alert(aa=="end");
						if(aa=="end"){
						j= CreatebillIssueSelverXml(billIds[i].value,fapiaoType,j,i,a);
							
						}else{
							break;
						}
					}
				}
		}
		
		// 发票空白作废业务
		function busineessBillCancel(){
			var aa="success";
			for(var i=0;;i++){
				var bill=CompareCurrentBill();
				//alert("比较当前票号结果"+bill);
				if(bill=="error"){
					aa=bill;
					//alert(aa);
					break;
				}else if(bill="end"){
					aa=bill;
					break;
				}else{
				BillCancel();
				}
			}
			return aa;
		}
		//创建发票开具的报文
		function CreatebillIssueSelverXml(ids,fapiaoType,j,i,a){
		//	alert(ids);
		var IssueType=1;
			$.ajax({url: 'CreatebillIssueSelverXml.action',
					type: 'POST',
					async:false,
					data:{billIds:ids,fapiaoType:fapiaoType,IssueType:IssueType},
					dataType: 'text',
				//	timeout: 1000,
					error: function(){
						return false;},
					success: function(result){
					try
						    {
						//alert(result);
						ret = sk.Operate(result);
						 j=updateIssueSelverResult(ret,ids,fapiaoType,j,i,a);
						//alert(ret);
						    }
						catch(e)
						    {
						alert(e.message + ",errno:" + e.number);
						    }	
						
									
							
					}
				});
			return j;
		}
		function updateIssueSelverResult(issueRes,billId,fapiaoType,j,i,a) {
			$.ajax({url: 'praseRedBillIssueReturnXml.action',
					type: 'POST',
					async:false,
					data:{issueRes:issueRes,billId:billId,fapiaoType:fapiaoType},
					dataType: 'text',
					timeout: 1000,
					error: function(){return false;},
					success: function(result){
						if(result=="success"){
							j++;
						}
						if(a==i){
							
							if(a==0&result!="success"){
								alert(result);
							}else{
								a=a+1;
						alert("开具 "+a+"条 !开具成功"+j+"条");
							}
							
						}
					}
				});
						submitAction(document.forms[0], "listIssueBill.action?paginationList.showCount="+"false");
						document.forms[0].action="listIssueBill.action?paginationList.showCount="+"false";
			return j;
		} */
	
	
		
		
		
		var msg = '<s:property value="message" escape="false"/>';
		if (msg != null && msg != ''){
			alert(msg);
		}
	</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" id="Form1">
		<table id="tbl_main" cellpadding=0 cellspacing=0>
			<tr>
				<td align="left"><s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">电票管理</span> <span
							class="current_status_submenu">红票开具</span>
					</div> <%--//序号		开票日期	 billDate	客户名称	customerName		客户纳税人识别号 customerTaxno	
	//合计金额	amtSum	合计税额	taxAmtSum	价税合计 sumAmt		是否手工录入	isHandiwork	开具类型	issueType	发票类型	fapiaoType	状态 datastatus	操作			
	
		--%>
					<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%"
						border="0">
						<tr>
							<td>投保单号</td>
							<td><input type="text" class="tbl_query_text" name="vtiTtmprcon" value="" /></td>
							<td align="left">保单号</td>
							<td><input type="text" class="tbl_query_text" name="vtiCherNum" value="" /></td>
							<td align="left">开票日期</td>
							<td>
								<input class="tbl_query_time" id="billBeginDate" type="text" name="billBeginDate" value=""
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billEndDate\')}'})" size='11' "/> -- 
								<input class="tbl_query_time" id="billEndDate" type="text" name="billEndDate" value=""
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billBeginDate\')}'})" size='11' "/></td>
						</tr>
						<tr>
							<td align="left">原发票代码</td>
							<td><input type="text" class="tbl_query_text" name="billCode" maxlength="10" value="" /></td>
							<td align="left">原发票号码</td>
							<td><input type="text" class="tbl_query_text" name="billNo" maxlength="8" value="" /></td>
							<td align="left">客户名称</td>
							<td><input type="text" class="tbl_query_text" name="customerName" value="" /></td>
						</tr>
						<tr>
							<td align="right">
								<input type="button" class="tbl_query_button" value="查询"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" 
									name="BtnView" id="BtnView" onclick="search()" />
							</td>
						</tr>
					</table>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left">
								<a href="#" name="BtnView" id="BtnView" onclick="exportExcel()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />导出</a>
								<a href="#" name="BtnView" id="BtnView" onclick="issueRedBill()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1010.png" />开具</a>
							</td>
						</tr>
					</table>

					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align: center">
									<input style="width: 13px; height: 13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
								</th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">投保单号</th>
								<th style="text-align: center">保单号</th>
								<th style="text-align: center">保全受理号</th>
								<th style="text-align: center">开票日期</th>
								<th style="text-align: center">客户名称</th>
								<th style="text-align: center">客户纳税人识别号</th>
								<th style="text-align: center">合计金额</th>
								<th style="text-align: center">合计税额</th>
								<th style="text-align: center">价税合计</th>
								<th style="text-align: center">是否手工录入</th>
								<th style="text-align: center">开具类型</th>
								<th style="text-align: center">状态</th>
								<th style="text-align: center">渠道</th>
								<th style="text-align: center">费用类型</th>
								<th style="text-align: center">承保日期</th>
								<th style="text-align: center">缴费频率</th>
								<th style="text-align: center">数据来源</th>
								<th style="text-align: center">操作</th>
							</tr>
							<%
							PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
							if (paginationList != null){
								List billInfoList = paginationList.getRecordList();
								if (billInfoList != null && billInfoList.size() > 0){
									for(int i=0; i<billInfoList.size(); i++){
										RedReceiptApplyInfo bill = (RedReceiptApplyInfo)billInfoList.get(i);
										if(i%2==0){
							%>
							<tr class="lessGrid rowA">
							<%
										}else{
							%>
							<tr class="lessGrid rowB">
							<%
										}
							%>
								<td align="center">
									<input style="width: 13px; height: 13px;" type="checkbox" name="selectBillIds" value="<%=BeanUtils.getValue(bill,"billId")%>" />
									<input type="hidden" name="fapiaoTypes" value="<%=bill.getFapiaoType() %>" />
								</td>
								<td align="center"><%=(i+1)%></td>
								<td align="center"><%=bill.getTtmprcno()==null?"":bill.getTtmprcno()%></td><!-- 投保单号 -->
								<td align="center"><%=bill.getInsureId()==null?"":bill.getInsureId()%></td><!-- 保单号 -->
								<td align="center"><%=bill.getRepnum()==null?"":bill.getRepnum()%></td><!-- 保全受理号 -->
								<td align="center"><%=bill.getBillDate()==null?"":bill.getBillDate()%></td><!-- 开票日期 -->
								<td align="center"><%=bill.getCustomerName()==null?"":bill.getCustomerName()%></td><!-- 客户名称 -->
								<td align="center"><%=bill.getCustomerTaxno()==null?"":bill.getCustomerTaxno()%></td><!-- 客户纳税人识别号 -->
								<td align="right"><%=NumberUtils.format(bill.getAmtSum(),"",2)%></td><!-- 合计金额 -->
								<td align="right"><%=NumberUtils.format(bill.getTaxAmtSum(),"",2)%></td><!-- 合计税额 -->
								<td align="right"><%=NumberUtils.format(bill.getSumAmt(),"",2)%></td><!-- 价税合计 -->
								<td align="center"><%=DataUtil.getIsHandiworkCH(bill.getIsHandiwork())%></td><!-- 是否手工录入 -->
								<td align="center"><%=DataUtil.getIssueTypeCH(bill.getIssueType())%></td><!-- 开具类型 -->
								<td align="center"><%=bill.getDatastatus()%></td><!-- 状态 -->
								<td align="center"><%=bill.getChannelch()==null?"":bill.getChannelch()%></td><!-- 渠道-->
								<td align="center"><%=bill.getFeeTypCh()==null?"":bill.getFeeTypCh()%></td><!-- 费用类型 -->
								<td align="center"><%=bill.getHissDte()==null?"":bill.getHissDte()%></td><!-- 承保日期 -->
								<td align="center"><%=bill.getBillFreq()==null?"":bill.getBillFreq()%></td><!-- 缴费频率 -->
								<td align="center"><%=bill.getdSource()==null?"":bill.getdSource()%></td><!-- 数据来源 -->
								<td>
									<a href="viewIssueTrans.action?curPage=<s:property value='paginationList.currentPage'/>&billId=<%=BeanUtils.getValue(bill,"billId")%>">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看交易" style="border-width: 0px;" />
									</a>
									<a href="javascript:void(0);" onClick="OpenModalWindow('viewImgFromBillEdit.action?fromFlag=red&billId=<%=bill.getBillId()%>',1000,650,false)">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png" title="查看票样" style="border-width: 0px;" />
									</a>
								</td>
							</tr>
							<%
										}
									}
								}
							%>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="overflow: auto; width: 100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
					</div></td>
			</tr>
		</table>
	</form>
</body>
</html>