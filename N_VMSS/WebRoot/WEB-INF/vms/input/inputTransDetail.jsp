<%@page import="com.cjit.vms.input.model.BillDetailEntity"%>
<%@page import="java.util.List"%>
<%@page import="com.cjit.vms.input.model.InputInfoUtil"%>
<%@page import="com.cjit.vms.input.model.InputInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<%@ include file="../../../page/modalPage.jsp"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<title>进项税信息明细</title>
<link href="<%=bopTheme2%>/css/subWindow.css" rel="stylesheet">
<link href="<%=bopTheme2%>/css/inputInvoice.css" rel="stylesheet">

</head>
<body scroll="no" style="overflow: hidden;">
	<div class="showBoxDiv">
		<form name="detailForm" id="detailForm" method="post">
			<div id="editsubpanel" class="editsubpanel">
				<div style="overflow: auto; width: 100%;">
					<table id="contenttable" class="lessGrid" cellspacing="0"
						width="100%" align="center" cellpadding="0">
						<caption style="background-color: #02A2AA; font-size: 18px">进项票据信息</caption>
						<tr>
							<td width="20%" align="right" class="listbar">发票代码:&nbsp;&nbsp;&nbsp;</td>
							<td width="35%"><s:property value='inputInvoiceNew.billId' /></td>
							<td width="20%" align="right" class="listbar">发票号码:&nbsp;&nbsp;&nbsp;</td>
							<td width="35%"><s:property value='inputInvoiceNew.billCode' /></td>
						</tr>
						<tr>
							<td width="20%" align="right" class="listbar">币种:&nbsp;&nbsp;&nbsp;</td>
							<td width="35%"><s:property value='inputInvoiceNew.curreny' /></td>
							<td width="20%" align="right" class="listbar">金额:&nbsp;&nbsp;&nbsp;</td>
							<td width="35%"><s:property value='inputInvoiceNew.zAmt' /></td>
						</tr>
						<tr>
							<td width="20%" align="right" class="listbar">税额:&nbsp;&nbsp;&nbsp;</td>
							<td width="35%"><s:property value='inputInvoiceNew.zTax' /></td>
							<td width="20%" align="right" class="listbar">价税合计金额:&nbsp;&nbsp;&nbsp;</td>
							<td width="35%"><s:property value='inputInvoiceNew.zSumTax' /></td>
						</tr>

						<tr>
							<td width="20%" align="right" class="listbar">开票日期:&nbsp;&nbsp;&nbsp;</td>
							<td width="35%"><s:property value='inputInvoiceNew.billDate' /></td>
							<td width="20%" align="right" class="listbar">供应商名称:&nbsp;&nbsp;&nbsp;</td>
							<td width="35%"><s:property value='inputInvoiceNew.name' /></td>
						</tr>
						<tr>
							<td width="20%" align="right" class="listbar">发票类型:&nbsp;&nbsp;&nbsp;</td>
							<td width="35%"><s:set id='billType'
									value='inputInvoiceNew.billType' /> <%=InputInfoUtil.billTypeMap.get(request.getAttribute("billType"))%>
							</td>
							<td width="20%" align="right" class="listbar">发票状态:&nbsp;&nbsp;&nbsp;</td>
							<td width="35%"><s:set id='billStatu'
									value='inputInvoiceNew.billStatu' /> <%=InputInfoUtil.billStatuMap.get(request.getAttribute("billStatu"))%>
							</td>
						</tr>
						<tr>
							<td width="20%" align="right" class="listbar">报销机构编码:&nbsp;&nbsp;&nbsp;</td>
							<td width="35%"><s:property value='inputInvoiceNew.billInst' /></td>
							<td width="20%" align="right" class="listbar">供应商纳税人识别号:&nbsp;&nbsp;&nbsp;</td>
							<td width="35%"><s:property value='inputInvoiceNew.taxNo' /></td>
						</tr>

						<tr>
							<td width="20%" align="right" class="listbar">供应商开户帐号:&nbsp;&nbsp;&nbsp;</td>
							<td width="35%"><s:property value='inputInvoiceNew.bankNo' /></td>
							<td width="20%" align="right" class="listbar">供应商开户银行:&nbsp;&nbsp;&nbsp;</td>
							<td width="35%"><s:property value='inputInvoiceNew.bankName' /></td>
						</tr>
						<tr>
							<td width="20%" align="right" class="listbar">供应商电话:&nbsp;&nbsp;&nbsp;</td>
							<td width="35%"><s:property
									value='inputInvoiceNew.customerTel' /></td>
							<td width="20%" align="right" class="listbar">供应商地址:&nbsp;&nbsp;&nbsp;</td>
							<td width="35%"><s:property
									value='inputInvoiceNew.customerAdd' /></td>
						</tr>
						<tr>
							<td width="20%" align="right" class="listbar">用途:&nbsp;&nbsp;&nbsp;</td>
							<td width="35%"><s:set id="purpose"
									value="inputInvoiceNew.purpose" /> <%=InputInfoUtil.purposeMap.get(request.getAttribute("purpose"))%>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div id="ctrlbutton" class="ctrlbutton" style="border: 0px">
				<input type="button" class="tbl_query_button"
					onMouseMove="this.className='tbl_query_button_on'"
					onMouseOut="this.className='tbl_query_button'"
					onclick="window.history.back()" name="BtnReturn" value="返回"
					id="BtnReturn" />
			</div>
			<div id="lessGridList4" style="overflow: auto;" 100%" align="center">
				<table class="lessGrid" cellspacing="0" rules="all" border="0"
					cellpadding="0" style="border-collapse: collapse; width: 100%;">
					<tr class="lessGrid head">
						<th style="text-align: center; background-color: #E8E8E8">分摊机构</th>
						<th style="text-align: center; background-color: #E8E8E8">金额</th>
						<th style="text-align: center; background-color: #E8E8E8">税率</th>
						<th style="text-align: center; background-color: #E8E8E8">税额</th>
						<th style="text-align: center; background-color: #E8E8E8">价税合计金额</th>
						<th style="text-align: center; background-color: #E8E8E8">是否抵免</th>
						<th style="text-align: center; background-color: #E8E8E8">转出比例</th>
						<th style="text-align: center; background-color: #E8E8E8">转出金额</th>
						<th style="text-align: center; background-color: #E8E8E8">转出原因</th>
					</tr>
					<%
					InputInfo inputInfo=(InputInfo)request.getAttribute("inputInvoiceNew");
					List<BillDetailEntity> list=inputInfo.getBillDetailList();
					System.out.print("---------------------"+inputInfo!=null?inputInfo:null);
					if(list.size()<1){
						return;
					}
					for(int i=0;i<list.size();i++){
						BillDetailEntity billDetailEntity=list.get(i);
						if(billDetailEntity.getShareInst()==null){
							billDetailEntity.setShareInst(inputInfo.getBillInst());
						}
					%>
					<tr class="lessGrid rowB">
						<td style="text-align: center;"><%=billDetailEntity.getShareInst() %></td>
						<td style="text-align: center;"><%=billDetailEntity.getAmt() %></td>
						<td style="text-align: center;"><%=billDetailEntity.getTaxRate() %></td>
						<td style="text-align: center;"><%=billDetailEntity.getTax() %></td>
						<td style="text-align: center;"><%=billDetailEntity.getSumAmt() %></td>
						<td style="text-align: center;"><%=InputInfoUtil.isCreditMap.get(billDetailEntity.getIsCredit()) %></td>
						<td style="text-align: center;"><%=billDetailEntity.getRollOutRatio()==null?"":billDetailEntity.getRollOutRatio() %></td>
						<td style="text-align: center;"><%=billDetailEntity.getRollOutAmt() %></td>
						<td style="text-align: center;"><%=InputInfoUtil.remarkMap.get(billDetailEntity.getRemark()) %></td>
					</tr>
					<%	
					}
					%>

				</table>
			</div>

		</form>
	</div>
</body>
</html>