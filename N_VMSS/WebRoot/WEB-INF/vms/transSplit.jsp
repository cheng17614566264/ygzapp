<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@include file="../../page/include.jsp"%>
<base target="_self">
<meta http-equiv='pragma' content='no-cache'>
<meta http-equiv='cache-control' content='no-cache'>
<META HTTP-EQUIV="Expires" CONTENT="0">
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>��ֿ�Ʊ</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script type="text/javascript"
	src="<c:out value="${webapp}"/>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript">
	function save(){
		//��ȡtr��
		var trNum = $("#tbody tr").length;
		//alert(trNum);
		var myAllMoney = "";
		var myMoney = "";
		var myMoneyNum = 0;
		for(var i=0;i<trNum;i++){
			myMoney = document.getElementById("money"+(i+1));
			
			//alert(myMoney.value);
			if($.trim(myMoney.value)==""){
				alert("���" + "����Ϊ��");
				//url.focus();
				return false;
			}
			
			if(isNaN($.trim(myMoney.value))){
				alert("��������ֵ�ͣ�");
				//url.focus();
				return false;
			}
			
			if($.trim(myMoney.value)<=0){
				alert("���" + "�������0");
				//url.focus();
				return false;
			}
            var reg = new RegExp("^[0-9]+(.[0-9]{0,2})?$", "g");
            if (!reg.test($.trim(myMoney.value))) {
				alert("���" + "���ֻ������λС��");
				//url.focus();
				return false;
			}
			myMoneyNum += parseFloat(myMoney.value);
			
			//alert("myMoneyNum"+myMoneyNum);
			
			myAllMoney += myMoney.value+"_";
			//alert(myAllMoney);
		}
		document.getElementById("allMoney").value = myAllMoney;
		var moneyTotal = document.getElementById("moneyTotal").value;
		moneyTotal = parseFloat(moneyTotal);
		
		//alert("moneyTotal"+moneyTotal);
		
		
		if(myMoneyNum.toFixed(2) - moneyTotal.toFixed(2) > 0){
			alert("������ܳ����ܽ�");
			return false;
		}else if(myMoneyNum.toFixed(2) < moneyTotal.toFixed(2)){
			alert("����������ܽ��,����������");
			//return false;
		}
		var transid= document.getElementById("transId").value;
		
		/* alert(transid+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh"); */
		
		window.form1.submit();
	}
	function addLine(){
	    var trNumPre = $("#tbody tr").length;
	    var preMoney = $("#tbody .tbl_query_text2:last");
	    //��ǰ�н�Ϊ�ղſɼ���
	    if($.trim(preMoney.val())!=""){
			var trNum =  $("#tbody tr").length+1;
			var newTr = "<tr id='tr" + trNum + "'><td align='center' style='background-color:#ffffff;'>" 
			    + trNum + "</td><td align='center' style='background-color:#ffffff;'> <input type='text' class='tbl_query_text2' id='money" + trNum
			    + "' name='money' value='' onkeypress='checkkey(this);' onkeyup='changeMoney(this);' > </td><td style='background-color:#ffffff;'>&nbsp;</td></tr>";
			$("#tbody").append(newTr);
		}
	}
	
	function subtractLine(){
		// �õ����һ��tr �����һ����������ɾ�������¼�����
		var trNum = $("#tbody tr").length;
		if(trNum==1){
			alert("�Ѿ�Ϊ���һ��," + "����ɾ��");
			//url.focus();
			return false;
		}
	    $("#tbody").find("tr:last").remove();
	    changeMoney();
	}
	
	
	function checkkey(v){
		var kc=event.keyCode;
		//ֻ�������롰+����-����.�����س�����0-9�����ַ���
		if (kc==43 || kc ==45 || kc ==46 || (kc >47 && kc <58)){
			if(kc==46 && v.indexOf('.') >-1){
				//������������С����
				event.returnValue = false; 
			}
			if(kc==43){
				//���������������ţ��������������м��������� 
				if (v.length>0 && v.indexOf('+')>-1){
				//event.returnValue = false;
				}
			}else if (kc==45){
				//���������������ţ��������������м����븺�� 
				if (v.length>0 && v.indexOf('-')>-1){
					//event.returnValue = false;
				}
			}
		}else{
			event.returnValue = false;
			alert("��������ֵ����");
		}
	}
	function changeMoney(obj){
		var moneyTotal = document.getElementById("moneyTotal").value;
		//��ȡtr�� �Ӷ���ȡ���и��ӵĽ��ֵ
		var trNum = $("#tbody tr").length;
		var myMoney = "";
		var myMoneyNum = 0;
		for(var i=0;i<trNum;i++){
			myMoney = document.getElementById("money"+(i+1));
			if($.trim(myMoney.value)!=""){
				// ��Ӹ������
				myMoneyNum += parseFloat(myMoney.value);
			}
		}
		moneyTotal = parseFloat(moneyTotal);
		var moneySy = moneyTotal - myMoneyNum;
		document.getElementById("moneySy").innerHTML = moneySy.toFixed(2);
	}
	<%
		String message = (String)request.getAttribute("message");
		if (message != null && !"".equals(message)){
		    message = message.replaceAll("MoneyError", "��ֽ���ʽ���ԣ�������С��λ���2λ���������֡�");
			message = message.replaceAll("AmtOverFull", "���ڽ��׽������ŷ�Ʊ��Ʊ����޶�Ľ��ף�������ֿ�Ʊ��");
			message = message.replaceAll("NotExistsTaxRate", "����˰Ŀ��Ϣ�����޶�Ӧ˰����Ϣ�Ľ��ף����ܽ��п�Ʊ��");
			message = message.replaceAll("NotExistsGoods", "�����޶�Ӧ��Ʊ��Ʒ�Ľ�����Ϣ�����ܽ��п�Ʊ��");
			message = message.replaceAll("NotExistsTrans", "�����ڶ�Ӧ���׽�����Ϣ�����ܽ��п�Ʊ��");
			message = message.replaceAll("TransStatusError", "������Ϣ����δ��Ʊ״̬�����ܽ��п�Ʊ��");
	%>
			alert('<%=message%>');
	<%
		}
	%>
	</script>
</head>
<body scroll="no" style="overflow: hidden;">
	<%
		String transId = (String)request.getAttribute("transId");
		System.out.print(transId);
		String userId = (String)request.getAttribute("userId");
		String amt = (String)request.getAttribute("amt");
		String saveFlag = (String)request.getAttribute("saveFlag");
		if ("1".equals(saveFlag)){
	%>
	<script type="text/javascript">
	</script>
	<%
		}
	%>
	<div class="showBoxDiv">
		<form name="form1" id="form1" action="transToManyBill.action"
			method="post">
			<div id="editsubpanel" class="editsubpanel">
				<div style="overflow: auto; width: 100%;">
					<input type="hidden" name="transId" id="transId" value="<%=transId%>" /> 
					
					<input type="hidden" name="userId" id="userId" value="<%=userId%>" /> 
					<input type="hidden" name="moneyTotal" id="moneyTotal" value="<%=amt%>" /> 
					<input type="hidden" name="allMoney" id="allMoney" value="" />
					<div class="windowtitle"
						style="background: #004C7E; height: 45px; line-height: 45px; text-align: center; color: #FFF; font-size: 14px; font-weight: bold;">��ֿ�Ʊ</div>
					<div style="width: 100%; align: center;">
						<table cellpadding="0" cellspacing="5" width="100%" border="0"
							id="mTable">
							<tr>
								<td align="right" style="width: 15%;">�ܽ��:&nbsp;&nbsp;</td>
								<td style="width: 45%"><%=amt%></td>
								<td align="left"><input name="add" type="button" id="add"
									value="+" onclick="addLine()" class="tbl_query_button" /></td>
								<td align="left"><input name="subtract" type="button"
									id="subtract" value="-" onclick="subtractLine()"
									class="tbl_query_button" /></td>
							</tr>
							<tr>
								<td align="right">ʣ����:&nbsp;&nbsp;</td>
								<td align="left" id="moneySy"><%=amt%></td>
							</tr>
						</table>
					</div>
					<div style="width: 100%; align: center;" id="lessGridList">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th width="15%"
									style="text-align: center; background-color: #00A9DA;">���</th>
								<th width="65%"
									style="text-align: center; background-color: #00A9DA;">���</th>
								<th width="20%"
									style="text-align: center; background-color: #00A9DA;">��ע</th>
							</tr>
							<tbody id="tbody">
								<tr id="tr1">
									<td align="center"><s:property value="1" /></td>
									<td align="center"><input type="text"
										class="tbl_query_text2" id="money1" name="money" value=""
										onkeypress="checkkey(this);" onkeyup="changeMoney(this);">
									</td>
									<td>������ֻ������λС����</td>
								</tr>
							</tbody>
						</table>
						<table cellpadding="0" cellspacing="5" width="100%" border="0"
							id="mTable">
							<tr>
								<td colspan="2" align="left"><input type="button"
									class="tbl_query_button" onclick="save()" name="BtnView"
									value="����" id="BtnView" /></td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</form>
	</div>
</body>
</html>