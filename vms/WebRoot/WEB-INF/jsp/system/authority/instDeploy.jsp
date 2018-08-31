<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="../../common/include.jsp"%>
    <link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
    <script language="javascript" type="text/javascript">
	$(function() {
		$("#BtnSave").click(function() {
			
				//$("#leftIds option").attr("selected", "selected");
				/* /* $("#selectedTransTypeRight option").attr("selected", "selected");
				$("#selectedTransTypeLeft option").attr("selected", "selected"); */ 
				var btnInst =  document.getElementById("btnInst");
				var act = "instDeploy.action?"+btnInst;
				submitForm(act);
			

		});
		//删除
		$("#delBtn").click(function() {
			/* $("#selectedTransTypeRight option:selected");
			$("#selectedTransTypeLeft option:selected"); */
			if(checkChkBoxesSelected("userList")){
				submitForm("deleteInstDeploy.action");
			}else{
				alert("请选择数据");
			}
			
		});
		
		//刷新
		$("#flushBtn").click(function() {
			var instId = document.getElementById("instAdd").value;
			var st = "instDeploy.action?instId="+instId;
			submitForm(st);
		});
		
		function checkChkBoxesSelected(chkBoxName){
			var chkBoexes= document.getElementsByName(chkBoxName);
			for(i=0;i<chkBoexes.length;i++){
				if(chkBoexes[i].checked){
					return true;
				}
			}
			return false;
		}
		


	})

	function submitForm(actionUrl) {
		var form = $("#main");
		var oldAction = form.attr("action");
		form.attr("action", actionUrl);
		form.submit();
		form.attr("action", oldAction);
	}
	</script>
</head>
<body scroll="no" style="overflow:hidden;">
<%-- <div class="showBoxDiv">
		<form id="main" method="post" action="updateDatastatus.action">
			<div id="editsubpanel" class="editsubpanel">
				<div style="overflow: auto; width: 100%;">
					<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
						<tr class="header">
							<th colspan="4">机构关联配置</th>
						</tr>
						<tr>
							<td align="right" class="listbar" style="width:120px">机构名称:</td>
							<td>
								<s:textfield name="vei.instName" cssClass="tbl_query_text_readonly" readonly="true" style="width:300px"></s:textfield>
							</td>
							<td>
								<!-- <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="addBtn" value="新增" id="addBtn" onclick="submitForm(addInst)"/>  -->
								<a href="#" onClick="OpenModalWindowSubmit('<c:out value="${webapp}"/>/addInst.action,600,500,true)"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon16.png"/>新增</a>
							</td>
							<td>
								<!-- <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="delBtn" value="删除" id="delBtn" /> -->
							</td>
						</tr>
						<tr>
							<td align="right" class="listbar">机构关联:</td>
							<td class="listbar">
								<s:optiontransferselect label="机构关联" name="selectedTransTypeLeft" leftTitle="未关联机构" list="leftSel" listKey="costCenter" listValue="costCenter" multiple="true" headerKey="headerKey"
									emptyOption="false" allowUpDownOnLeft="false" rightTitle="已关联机构" doubleList="rightSel" doubleListKey="costCenter" doubleListValue="costCenter" doubleName="selectedTransTypeRight"
									doubleHeaderKey="doubleHeaderKey" doubleEmptyOption="false" doubleMultiple="true" allowUpDownOnRight="false" cssStyle="width:150px;height:300px;padding:5px"
									doubleCssStyle="width:150px;height:300px;padding:5px"
								/> 
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
				<s:if test="!showOnly">
					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnSave" value="保存" id="BtnSave" />
				</s:if>
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="window.close()" value="关闭" id="BtnReturn" />
			</div>
		</form>
	</div> --%>
<div style="width:100%; height:100%; overflow:auto; border:1px solid #000000;">
    <form id="main" name="frm" action="<c:out value='${webapp}'/>/saveUserByRole.action" method="post">
    <table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
    <tr><td class="centercondition">
    <s:if test="role.roleId != null && role.roleId != '' ">
    	<input type="hidden" name="role.roleId" value="<s:property value="role.roleId"/>"/>
    </s:if>
   <table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
   	<tr>
		<th colspan="4">机构关联配置</th>
	</tr>
	<tr align="left">
		<td align="right" class="listbar" style="width:120px">业务机构名称:</td>
		<td>
			<s:textfield name="vei.instName" cssClass="tbl_query_text_readonly" readonly="true" style="width:300px"></s:textfield>
			<input type="hidden" id="instAdd" value="<s:property value="vei.instId"/>"/>
			<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="addBtn" value="新增" id="addBtn" onclick="OpenModalWindow('<c:out value="${webapp}"/>/addInst.action?instId=<s:property value="vei.instId"/>',700,400,true)"/>
			<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="delBtn" value="删除" id="delBtn" />
			<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="flushBtn" value="刷新" id="flushBtn" />
		</td>
		<td>
			
		</td>	
	</tr>
	</table>
   
  <div style="overflow: auto; width: 100%; height: 100%;" id="lessGridListTemp">
       <table  class="lessGridTemp" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse;">
            <tr class="lessGridTemp headTemp">
                <th width="3%" style="text-align: center;">
                	<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="cbxselectall(this,'userList')" />
                </th>
                <th style="text-align: center;">财务或费控机构代码</th>
                <th style="text-align: center;">财务或费控机构名称</th>
                <th style="text-align: center;">业务机构代码</th>
                <!-- <th style="text-align: center;">业务机构名称</th> -->
                <th style="text-align: center;">财务或费控机构级别</th>
                <th style="text-align: center;">来源</th>
            </tr>

 			<s:iterator value="leftSel" status="stuts" id="row">
				<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
				<td align=center style="width:20px">
						<input type="checkbox" style="width:13px;height:13px;" name="userList" value="<s:property value="costCenter"/>"/>
				</td>
				<td><s:property value="costCenter"/></td>
				<td><s:property value="costCenterName"/></td>
				<td><s:property value="instId"/></td>
				<td><s:property value="instLevel"/></td>
				<td><s:property value="dsource"/></td>
				</tr>
			</s:iterator>
        </table>
        </div>
    <div id="anpBoud" align="Right" style="width:100%;">
        <table width="100%" cellspacing="0" border="0">
            <tr>
                <td align="left"></td>
                <td align="right"><ww:component template="pagediv"/></td>
            </tr>
        </table>
    </div>
    <div id="ctrlbutton" class="ctrlbutton" align="right" style="border:0px">
    	<!-- <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"  onclick="saveCfg()" name="BtnSave" value="保存" id="BtnSave" class="input_button"/> -->
	    <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"  onclick="window.close()" name="BtnClose" value="关闭" id="BtnClose" class="input_button"/>
	 </div>
	 
	 </td></tr></table>
    </form>
    </div>
</body>
</html>
