<!-- file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../page/include.jsp"%>
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
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/vms.js"></script>
<script>
        function revoke() {
            submitAction(document.forms[0], "listBillTrack.action?fromFlag=menu&flag=track");
        }
    </script>

<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="" id="Form1">
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">综合查询</span> <span
							class="current_status_submenu">票据查询</span> <span
							class="current_status_submenu">查看交易</span>
					</div> </br> <!--	<div style="overflow:auto;width:100%;" id="list1">--> <!--	<table id="lessGridList" width="100%" class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" display="none" style="border-collapse: collapse;">-->

					<div id="lessGridList1" style="width: 100%;">
						<div id="rDiv" style="width: 0px; height: auto;">
							<table class="lessGrid" cellspacing="0" rules="all" border="0"
								cellpadding="0" style="border-collapse: collapse; width: 100%;">
								<tr class="lessGrid head">
									<th style="text-align: center">序号</th>
									<th style="text-align: center">交易时间</th>
									<th style="text-align: center">客户名称</th>
									<th style="text-align: center">保单号</th>
									<th style="text-align: center">投保单号</th>
									<th style="text-align: center">纳税人类型</th>
									<th style="text-align: center">交易类型</th>
									<th style="text-align: center">交易标志</th>
									<th style="text-align: center">是否打票</th>
									<th style="text-align: center">是否冲账</th>
									<th style="text-align: center">发票类型</th>
									<th style="text-align: center">交易金额</th>
									<th style="text-align: center">税率</th>
									<th style="text-align: center">税额</th>
									<th style="text-align: center">价税合计</th>
									<th style="text-align: center">未开票金额</th>
									<th style="text-align: center">状态</th>
								</tr>
								<%
								//2018-03-23新增，区分是手工开票还是核心推送
	                            String status = (String)request.getAttribute("status");
                                PaginationList paginationList = (PaginationList) request
                                        .getAttribute("paginationList");
                                if (paginationList != null) {
                                    List transInfoList = paginationList.getRecordList();
                                    if (transInfoList != null && transInfoList.size() > 0) {
                                        for (int i = 0; i < transInfoList.size(); i++) {
                                            TransInfo trans = (TransInfo) transInfoList.get(i);
                                            if (i % 2 == 0) {
                            %>
								<tr class="lessGrid rowA">
									<%
									} else {
								%>
								
								<tr class="lessGrid rowB">
									<%
                                    }
                                %>
									<td align="center"><%=i + 1%></td>
									<td align="center"><%=trans.getTransDate()%></td>
									<td align="center"><%=trans.getCustomerName()%></td>
									<td align="center"><%=trans.getCherNum()%></td>
									<!-- 2018-03-16更改 避免出现null -->
									<td align="center"><%-- <%=trans.getTtmpRcno()%> --%>
									<%=trans.getTtmpRcno()==null?"":trans.getTtmpRcno()%>
									</td>
									<td align="center"><%=trans.getCustomerTaxPayerType()==null?"":DataUtil.getTaxpayerTypeCH(trans.getCustomerTaxPayerType())%>
									</td>
									<td align="center"><%=trans.getTransTypeName()%></td>
									<td align="center"><%=DataUtil.getTransFlag(trans.getTransFlag())%>
									</td>
									<td align="center"><%=DataUtil.getFapiaoFlagCH(trans
                                        .getTransFapiaoFlag())%></td>
									<td align="center"><%=DataUtil.getYOrNCH(trans.getIsReverse())%>
									</td>
									<td align="center"><%=DataUtil.getFapiaoTypeCH(trans
                                        .getFapiaoType())%></td>
									<td align="right"><%=NumberUtils.format(trans.getAmt(), "", 2)%>
									</td>
									<%--<td align="right"><%=DataUtil.getTaxFlagCH(trans.getTaxFlag())%>--%>
									<td align="right"><%=NumberUtils.format(trans.getTaxRate(), "", 4)%>
									</td>
									<td align="right"><%=NumberUtils.format(trans.getTaxAmt(), "", 2)%>
									</td>
									<!-- 税额 -->
									<td align="right"><%-- <%=NumberUtils.format(trans.getAmt(), "", 2)%> --%>
									<%=NumberUtils.format(trans.getReverseAmt(), "", 2)%>
									</td>
									<!-- 价税合计 -->
									<td align="right"><%=NumberUtils.format(trans.getBalance(), "", 2)%>
									</td>
									<td align="center"><%-- <%=DataUtil.getDataStatusCH(trans.getDataStatus(), "TRANS")%> --%>
                                        <%=DataUtil.getDataStatusCH(trans.getDataStatus(), status)%>
									</td>
								</tr>
								<%
                                        }
                                    }
                                }
                            %>
							</table>
						</div>
					</div>
					<div id="anpBoud" align="Right" style="width: 100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left"><input type="button"
								class="tbl_query_button" value="关闭"
								onMouseMove="this.className='tbl_query_button_on'"
								onMouseOut="this.className='tbl_query_button'" name="btCancel"
								id="btCancel" onclick="window.history.back(-1);" /></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
