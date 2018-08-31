<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.BillInfo"
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
<!-- <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script> -->
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/lhb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/vms.js"></script>

<script type="text/javascript">
        // [查询]按钮  [导出]按钮
        function submitForm(actionUrl) {
            submitAction(document.forms[0], actionUrl);
            document.forms[0].action = "listBillAudit.action?paginationList.showCount="
                    + "false";
        }
        // [删除]按钮
        function revoke() {
            if (checkChkBoxesSelected("selectBillIds")) {
                if (!confirm("确定是否删除？")) {
                    return false;
                }
                submitAction(document.forms[0], "revokeBillYS.action");
                document.forms[0].action = "listBillModify.action?paginationList.showCount="
                        + "false";
            } else {
                alert("请选择票据记录！");
            }
        }
        // [提交]按钮
        function submitBill() {
            if (checkChkBoxesSelected("selectBillIds")) {
                submitAction(document.forms[0], "submitBills.action");
                document.forms[0].action = "listBillModify.action?paginationList.showCount="
                        + "false";
            } else {
                alert("请选择票据记录！");
            }
        }
        // [添加]按钮
        function createBill() {
            submitAction(document.forms[0],
                    "createBill.action?fromFlag=menu&flag=create");
            document.forms[0].action = "listBillModify.action?paginationList.showCount="
                    + "false";
        }

        function OpenModalWindowSubmit(newURL, width, height, needReload) {
            var retData = false;
            if (typeof (width) == 'undefined') {
                width = screen.width * 0.9;
            }
            if (typeof (height) == 'undefined') {
                height = screen.height * 0.9;
            }
            if (typeof (needReload) == 'undefined') {
                needReload = false;
            }
            retData = showModalDialog(newURL, window, "dialogWidth:" + width
                    + "px;dialogHeight:" + height
                    + "px;center=1;scroll=0;help=0;status=0;");
            if (needReload && retData) {
                window.document.forms[0].submit();
            }
        }
        // 票据审核画面                    [审核通过/拒绝]按钮
        function audit(type) {
            if (checkChkBoxesSelected("selectBillIds")) {
                var billId = "";
                var billIds = document.Form1.selectBillIds;
                var count = 0;
                if (!isNaN(billIds.length)) {
                    for (var i = 0; i < billIds.length; i++) {
                        if (billIds[i].checked) {
                            billId = billId == '' ? billIds[i].value : billId + ","
                            + billIds[i].value;
                        }
                    }
                } else {
                    billId = billIds.value;
                }
                if (type == 'P') {
                    if (!confirm("确定要通过审核吗？")) {
                        return false;
                    }
                    //document.getElementById('BtnView').disabled=true;
                    submitAction(document.forms[0], "auditBill.action?billId="
                            + billId + "&submitFlag=" + type
                            + "&paginationList.showCount=" + "false");
                    //审核通过弹窗信息
                    alertMessage();
                    document.forms[0].action = "listBillAudit.action?paginationList.showCount="
                            + "false";
                } else if (type == 'R') {
                    /*if(!confirm("确定要拒绝审核吗？")){
                     return false;
                     }*/
                    //openNewDiv('newDiv');
                    OpenModalWindowSubmit(
                            "billModifyAuditingToCancelReason.action?billId="
                            + billId + "&submitFlag=" + type, 500, 250,
                            true,'add');
                    document.forms[0].action = "listBillAudit.action?paginationList.showCount="
                            + "false";
                    //submitAction(document.forms[0], "auditBill.action?submitFlag="+type);
                }
            } else {
                alert("请选择票据记录！");
            }
        }
        
      //销项管理附件查看
    	function viewImgFromInvoiceJF(){
    	  	var selects = document.getElementsByName("selectBillIds");
    		var count = 0 ;
    		var index = 0 ;
    		for(var i=0 ;i<selects.length ;i++){
    			if(selects[i].checked==true){
    				count++;
    				index = i ;
    			}
    		}
    		if(count>1){
    			alert("请选择单条挑记录")
    		}else if(count== 0){
    			alert("请选择记录")
    		}else{
    			// bill.getIsHandiwork()+"-"+bill.getInsureId()+"-"+bill.getTtmpRcno()+"-"+bill.getDataStatus()+"-"+bill.getBillId()
    			 var val =document.getElementsByName("selects")[index].value ;
    			 if("3"==val.split("-")[0]){
    				 val="Y-"+val.split("-")[4];
    			 }else{
    				 val="N-"+val.split("-")[1]+"-"+val.split("-")[2]+"-"+val.split("-")[3];
    			 }
    			 OpenModalWindowSubmit('viewImgFromBillOfFuJian.action?fileName='+val,1000,650,true);
    		}
    	}
    </script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listBillAudit.action"
		id="Form1">
		<%
        String currMonth = (String) request.getAttribute("currMonth");
    %>
		<input type="hidden" name="currMonth" id="currMonth"
			value="<%=currMonth%>" /> <input type="hidden" name="reasionInfo"
			id="reasionInfo" value="" />
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition"><s:component
						template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">开票管理</span> <span
							class="current_status_submenu">票据审核</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%"
							border="0">
							<tr>
								<td style="text-align: right; width: 6%;">投保单号</td>
								<td style="text-align: left; width: 14%;"><input
									class="tbl_query_text" type="text" name="ttmpRcno"
									value="<s:property value='billCondition.ttmpRcno'/>"
									maxlength="20" /></td>
								<td style="text-align: right; width: 6%;">保单号</td>
								<td style="text-align: left; width: 14%;"><input
									class="tbl_query_text" type="text" name="cherNum"
									value="<s:property value='billCondition.cherNum'/>"
									maxlength="20" /></td>
								<td style="text-align: right; width: 6%;">发票类型</td>
								<td style="text-align: left; width: 14%;"><select
									name="billCondition.fapiaoType" style="width: 133px"
									cssClass="tbl_query_text">
										<option value=""
											<s:if test='billCondition.fapiaoType==""'>selected</s:if>
											<s:else></s:else>>全部</option>
										<option value="0"
											<s:if test='billCondition.fapiaoType=="0"'>selected</s:if>
											<s:else></s:else>>增值税专用发票</option>
										<option value="1"
											<s:if test='billCondition.fapiaoType=="1"'>selected</s:if>
											<s:else></s:else>>增值税普通发票</option>
								</select></td>
							</tr>
							<tr>
								<td style="text-align: right; width: 6%;">客户名称</td>
								<td style="text-align: left; width: 14%;"><input
									type="text" class="tbl_query_text"
									name="billCondition.customerName"
									value="<s:property value='billCondition.customerName'/>" /></td>
								<td style="text-align: right; width: 6%;">开具类型</td>
								<td style="text-align: left; width: 14%;"><select
									name="billCondition.issueType" style="width: 133px"
									cssClass="tbl_query_text">
										<option value=""
											<s:if test='billCondition.issueType==""'>selected</s:if>
											<s:else></s:else>>全部</option>
										<option value="1"
											<s:if test='billCondition.issueType=="1"'>selected</s:if>
											<s:else></s:else>>单笔</option>
										<option value="2"
											<s:if test='billCondition.issueType=="2"'>selected</s:if>
											<s:else></s:else>>合并</option>
										<option value="3"
											<s:if test='billCondition.issueType=="3"'>selected</s:if>
											<s:else></s:else>>拆分</option>
								</select></td>
								<td style="text-align: right; width: 6%;">承保日期</td>
								<td style="text-align: left; width: 20%;"><input
									class="tbl_query_time" type="text"
									name="billCondition.hissBeginDte"
									value="<s:property value='billCondition.hissBeginDte'/>"
									onfocus="WdatePicker()" size='11' /> -- <input
									class="tbl_query_time" id="" type="text"
									name="billCondition.hissEndDte"
									value="<s:property value='billCondition.hissEndDte'/>"
									onfocus="WdatePicker()" size='11' /></td>
								<td style="text-align: right; width: 6%;"></td>
							</tr>
							<tr>
								<td></td>
								<td style="width: 80px;" align="right"><input type="button"
									class="tbl_query_button" value="查询"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="BtnView"
									id="BtnView" onclick="submitForm('listBillAudit.action')" />
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left"><a href="#" onclick="return audit('P');"
								name="cmdP" id="cmdP"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1017.png" />
									审核通过
							</a> <a href="#" onclick="return audit('R');" name="cmdR" id="cmdR">
									<img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1018.png" />
									审核拒绝
							</a> <a href="#" onclick="viewImgFromInvoiceJF()" name="cmdR"
								id="cmdR"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1027.png" />
									附件查看
							</a></td>
							</td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<div id="rDiv" style="width: 0px; height: auto;">
							<table class="lessGrid" cellspacing="0" rules="all" border="0"
								cellpadding="0" style="border-collapse: collapse; width: 100%;">
								<tr class="lessGrid head">
									<th style="text-align: center"><input
										style="width: 13px; height: 13px;" id="CheckAll"
										type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
									</th>
									<th style="text-align: center">序号</th>
									<th style="text-align: center">投保单号</th>
									<th style="text-align: center">保单号</th>
									<th style="text-align: center">申请开票日期</th>
									<th style="text-align: center">客户名称</th>
									<th style="text-align: center">客户纳税人识别号</th>
									<th style="text-align: center">合计金额</th>
									<th style="text-align: center">合计税额</th>
									<th style="text-align: center">价税合计</th>
									<th style="text-align: center">是否手工录入</th>
									<th style="text-align: center">开具类型</th>
									<th style="text-align: center">发票类型</th>
									<th style="text-align: center">状态</th>
									<th style="text-align: center">承保日期</th>
									<th style="text-align: center">缴费频率</th>
									<th style="text-align: center"
										<s:if test="flag != 'audit'">colspan="2"</s:if>>操作</th>
								</tr>
								<%
                                PaginationList paginationList = (PaginationList) request
                                        .getAttribute("paginationList");
                                if (paginationList != null) {
                                    List billInfoList = paginationList.getRecordList();
                                    if (billInfoList != null && billInfoList.size() > 0) {
                                        for (int i = 0; i < billInfoList.size(); i++) {
                                            BillInfo bill = (BillInfo) billInfoList.get(i);
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
									<td align="center"><input
										style="width: 13px; height: 13px;" type="checkbox"
										name="selectBillIds"
										value="<%=BeanUtils.getValue(bill, "billId")%>" /> 
										<input name="selects" type="hidden"
										value="<%= bill.getIsHandiwork()+"-"+bill.getInsureId()+"-"+bill.getTtmpRcno()+"-"+bill.getDataStatus()+"-"+bill.getBillId()%>" /> 
										<input type="hidden" name="selectBillDates" value="<%=bill.getBillDate()%>" /></td>
									<td align="center"><%=i + 1%></td>
									<td align="center"><%=bill.getTtmpRcno() == null ? "" : bill
                                        .getTtmpRcno()%></td>
									<td align="center"><%=bill.getInsureId() == null ? "" : bill
                                        .getInsureId()%></td>
									<td align="center"><%=bill.getApplyDate() == null ? "" : bill
                                        .getApplyDate()%></td>
									<td align="left"><%=bill.getCustomerName()%></td>
									<td align="left"><%=bill.getCustomerTaxno()%></td>
									<td align="right"><%=NumberUtils.format(bill.getAmtSum(), "", 2)%>
									</td>
									<td align="right"><%=NumberUtils.format(bill.getTaxAmtSum(), "", 2)%>
									</td>
									<td align="right"><%=NumberUtils.format(bill.getSumAmt(), "", 2)%>
									</td>
									<td align="left"><%=DataUtil.getIsHandiworkCH(bill
                                        .getIsHandiwork())%></td>
									<td align="left"><%=DataUtil.getIssueTypeCH(bill.getIssueType())%>
									</td>
									<td align="left"><%=DataUtil.getFapiaoTypeCH(bill.getFapiaoType())%>
									</td>
									<td align="left"><%=DataUtil.getDataStatusCH(
                                        bill.getDataStatus(), "BILL")%>
									</td>
									<!-- <td><%=bill.getChanNelCh() == null ? "" : bill
                                        .getChanNelCh()%>
                                </td>
                                <td><%=bill.getFeeTypCh() == null ? "" : bill
                                        .getFeeTypCh()%>
                                </td> -->
									<td><%=bill.getHissDte() == null ? "" : bill
                                        .getHissDte()%></td>
									<td><%=bill.getBillFreqCh() == null ? "" : bill
                                        .getBillFreqCh()%></td>
									<!-- <td><%=bill.getDsouRceCh() == null ? "" : bill
                                        .getDsouRceCh()%>
                                </td> -->

									<td align="center">
										<%--  <a href="JFexport.action?cherNum=<%=bill.getInsureId()%>">
                                        <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png"
                                             title="附件下载" style="border-width: 0px;"/>
                                    </a> --%> <a
										href="billAuditToTrans.action?curPage=<s:property value='paginationList.currentPage'/>&billId=<%=BeanUtils.getValue(bill, "billId")%>&dsource=<%=bill.getDsouRce()%>">
											<img
											src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
											title="查看交易" style="border-width: 0px;" />
									</a> <a href="javascript:void(0);"
										onClick="OpenModalWindowSubmit('viewImgFromBillEdit.action?billId=<%=bill.getBillId()%>',1000,650,true)">
											<img
											src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png"
											title="查看票样" style="border-width: 0px;" />
									</a>
									</td>
								</tr>

								<%
                                        }
                                    }
                                }
                            %>
								</tr>
							</table>
						</div>
					</div>
					<div id="anpBoud" align="Right"
						style="overflow: auto; width: 100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<input type="hidden" name="paginationList.showCount"
									value="false" />
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
					</div></td>
			</tr>
		</table>
	</form>
</body>
</html>