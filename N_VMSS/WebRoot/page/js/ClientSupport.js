var searchObject = null;
var mSpac = 21;
var iii = 0;
var mValue = "";
var buttonStatu = false;
			
function DigitalOnly()
{
	var nCode = event.keyCode;
    if(nCode < 48 || nCode > 57)
	{
		event.keyCode = 0;
	}
}
function NotQuote()
{
	var nCode = event.keyCode;
    if(nCode==39)
	{
		event.keyCode = 8217;
	}
}

function FloatOnly(obj)
{
	var strValue = obj.value;	
	var nCode = event.keyCode;
	if(nCode == 46)
	{
		if(strValue.indexOf(".") > - 1)
		{
			event.keyCode = 0;
			return;
		}
	}
	else if(nCode==45){
		if(strValue.indexOf("-") > -1){
			event.keyCode = 0;
			return;
		}
	}
	else if((nCode < 48 || nCode > 57) && nCode!=45)
	{
		event.keyCode = 0;
		return ;
	}
}

function FloatOnlyNew(obj)
{
	var strValue = obj.value;	
	reg=/^((-?|\+?)\d+)(\.\d+)?$/;
	if(strValue.match(reg)==null){
		event.keyCode = 0;
	}
}

function CheckIsBlank(obj)
{
	var str = obj.value;
	if(str == '' || str == null)
		return true;
	var length = str.length;
	for(var i = 0;i < length;i ++)
	{ 
		if(str.charAt(i) != ' ')  
			return false;
	}
	return true;
}

function CheckLength(obj,len)
{
	var str = obj.value;
	var vlen = int(len);
	if(str == null || str.length !=vlen)
	{
		alert('�ַ�����ӦΪ'+vlen);
		return false;
	}
		
	return true;
}




//ҳ���������ı�


function findGo(obj,cValue){
	if(event.keyCode == 39 || event.keyCode == 13 || event.keyCode == 40 || event.keyCode == 37 || event.keyCode == 38){
		findText(obj,cValue);
		event.keyCode == 13;
	}
}


function findText(obj,cValue)
{
	var iFlag = 1;
	if(obj ==null)
	{
		obj = document.body;
	}
	if (cValue == null || cValue == '')
	{
		return;
	}
	if(mValue!=cValue)
	{
		iii = 0;
	}
	var rng = obj.createTextRange();
	for (i=0; rng.findText(cValue)!=false; i++) 
	{
		var k = rng.duplicate();
	    rng.collapse(false);
		if (i>=iii)
		{
		    k.select();
			k.scrollIntoView();
			iii+=iFlag;
			mValue = cValue;
			break;

		}

	}
	if(i<iii)
	{
	}
	else
	{
		iii=0;
	}
	
}

function findNext()
{
	if(event.keyCode==13)
		event.keyCode=9;
}

//��ѡ��ȫѡ

function chkall(input1,input2)
{
    var objForm = document.forms[input1];
    var objLen = objForm.length;
    for (var iCount = 0; iCount < objLen; iCount++)
    {
        if (input2.checked == true)
        {
            if (objForm.elements[iCount].type == "checkbox")
            {
                objForm.elements[iCount].checked = true;
            }
        }
        else
        {
            if (objForm.elements[iCount].type == "checkbox")
            {
                objForm.elements[iCount].checked = false;
            }
        }
    }
} 

//ҳ���������ı�

function findTextFillItem(obj,cValue)
{
	var searchObject = null;
	var mSpac = 21;
	var iii = 0;
	var mValue = "";
	var buttonStatu = false;
	
	var iFlag = 1;
	if(obj ==null)
	{
		obj = document.body;
	}
	if (cValue == null || cValue == '')
	{
		alert("Invalid Value!");
		return;
	}
	if(mValue!=cValue)
	{
		iii = 0;
	}
	var rng = obj.createTextRange();
	for (i=0; rng.findText(cValue)!=false; i++) 
	{
		var k = rng.duplicate();
		rng.collapse(false);
		if (i>=iii)
		{
			k.select();
			k.scrollIntoView();
			iii+=iFlag;
			mValue = cValue;
			break;
		}
	}
	if(i<iii)
	{
		//alert(" '" + cValue + "'�� "+ (i + 1) +" �����ҵ�.   ");
	}
	else
	{
		alert("Not Find!");
		iii=0;
	}
}
  
/*****************************************************************************
�������ƣ�CheckAll
������ܣ�ȫѡҳ���е�CheckBox
����	��object ����ȫѡ�Ķ���
����	��2007-07-20 
����    ��wanwanyi
�޸���  ��-----�޸���--------��Ҫ--------------------------
******************************************************************************/
function CheckAll(object)
{
	var aa = document.getElementsByTagName("input");   
	//�õ��Ǹ��ܿصĸ�ѡ���ѡ��״̬  
	var state = object.checked;    
	for (var i=0; i<aa.length; i++)   
	{   
		if ( aa[i].type == "checkbox" )   
		aa[i].checked = state;   
	}   
}   
 
/*****************************************************************************
�������ƣ�CheckAll
������ܣ�ȫѡҳ����ָ��DataGrid��CheckBox
����	��object ����ȫѡ�Ķ���  datagrid ȫѡ�Ķ���
����	��2007-07-20 
����    ��wanwanyi
�޸���  ��-----�޸���--------��Ҫ--------------------------
******************************************************************************/
function CheckAll(object,datagrid)
{
	var aa = datagrid.getElementsByTagName("input");   
	//�õ��Ǹ��ܿصĸ�ѡ���ѡ��״̬  
	var state = object.checked;    
	for (var i=0; i<aa.length; i++)   
	{   
		if ( aa[i].type == "checkbox" && aa[i].disabled == false )   
		aa[i].checked = state;   
	}   
}  

/*****************************************************************************
�������ƣ�Change 
������ܣ�����CheckBox״̬�ж�ɾ����ť�Ƿ����
����	��CheckBox �¼��ı��CheckBox;DataGrid �¼����õ�DataGrid;btnDelete ɾ����ťID
����	��2007-12-07 
����    ��wanwanyi
�޸���  ��-----�޸���--------��Ҫ--------------------------
******************************************************************************/
function Change(CheckBox,DataGrid,btnDelete)
{    
	//�Ƿ���ѡ��
	if(CheckBox.checked && isSelected(DataGrid))
	{   
		btnDelete.disabled = false; 
		//�ж��Ƿ�ȫ��ѡ�У�����ȫѡ��Ϊtrue
		if(isAllSelected(DataGrid,true))
		{
			DataGrid.getElementsByTagName("input")[0].checked = true;
		} 
	}
	else
	{
		//�Ѿ�����һ��û��ѡ�У���ȫѡ��Ϊfalse
		DataGrid.getElementsByTagName("input")[0].checked = false;
		//�ж��Ƿ�Ϊȫ��δѡ�У�����ɾ����ť�û�
		if(isAllSelected(DataGrid,false))
		{
			btnDelete.disabled = true;
		}
	}
}
//�Ƿ�ȫ��ѡ��/δѡ�� true:ȫ��ѡ�� false:ȫ��δѡ��
function isAllSelected(DataGrid,blState)
{ 
	var arrCheck = DataGrid.getElementsByTagName("input");
	//��ȡ��ȫѡ��������Checkbox�ĸ���
	for(var i = 1;i<arrCheck.length;i++)
	{
		if(arrCheck[i].type == "checkbox" && arrCheck[i].checked != blState)
		{ 
			return false; 
		}
	}
	return true;
}
//�жϳ�ȫѡ�����Ƿ���ѡ��
function isSelected(DataGrid)
{
	var arrCheck = DataGrid.getElementsByTagName("input");
	//�ӵڶ�����ʼ����һ��Ϊȫѡ
	for(var i = 1 ;i < arrCheck.length ;i ++)
	{ 
		if(arrCheck[i].type == "checkbox" && arrCheck[i].checked)
		{
			return true; 
		}
	}
	return false;
}
		
/*****************************************************************************
�������ƣ�SelectAll
������ܣ�Ĭ��ȫѡҳ����ָ�������CheckBox
����	��object ���� 
����	��2007-07-20 
����    ��wanwanyi
�޸���  ��-----�޸���--------��Ҫ--------------------------
******************************************************************************/
function SelectAll(object)
{   
	var datagrid = document.getElementById(object);
	if(datagrid == null || datagrid == undefined || (!datagrid))
	{ 
		return false;
	}
	else
	{
		var aa = datagrid.getElementsByTagName("input");    
		for (var i=0; i<aa.length; i++)   
		{   
			if( aa[i].disabled == false )   
				aa[i].checked = true;   
		}   
		return true;
	} 
}  
	
	
/*****************************************************************************
�������ƣ�OpenViewNewWindow
������ܣ��򿪲鿴�ķ�ģ̬����

����	��id  ����id
����	��2007-07-20 16:09
����    ��jz_guo
�޸���  ��-----�޸���--------��Ҫ--------------------------
******************************************************************************/
function OpenViewNewWindow(url)
{
	ContentWindow=window.open(url,"_blank","width=620,height=500,directories=no,status=no,scrollbars=yes,resizable=yes,menubar=yes,top=0,left=100")
}

/*****************************************************************************
�������ƣ�OpenViewTitleNewWindow
������ܣ��򿪱���鿴�ķ�ģ̬����

����	��id  ����id
����	��2007-07-23 14:09
����    ��jz_guo
�޸���  ��-----�޸���--------��Ҫ--------------------------
******************************************************************************/
function OpenViewTitleNewWindow(newURL)
{
	var Width = screen.width * 0.8;
	var height = screen.height * 0.8;
	var leftLen = screen.width * 0.1;
	var topLen = screen.height * 0.05;
	ContentWindow = window.open(newURL,"_blank","toolbar=no,width="+Width+",height="+height+",left="+leftLen+",top="+topLen+",directories=no,status=no,scrollbars=yes,resizable=yes,menubar=yes")
}

/*****************************************************************************
�������ƣ�OpenModifyWindow
������ܣ����޸Ĵ���
����	��id  ����id
����	��2007-07-25 10:18
����    ������

�޸���  ��-----�޸���--------��Ҫ--------------------------
******************************************************************************/
function OpenModifyWindow(newURL)
{
	var Width = screen.width * 0.8;
	var height = screen.height * 0.8;
	var leftLen = screen.width * 0.1;
	var topLen = screen.height * 0.05;
	ContentWindow = window.open(newURL,"_self")
}

/*****************************************************************************
�������ƣ�scrollback

������ܣ�ҳ��ˢ�º��ס������λ�ã�������

����	��object ���� 
����	��2007-07-20 
����    ��wanwanyi
�޸���  ��-----�޸���--------��Ҫ--------------------------
*****************************************************************************/  
function Trim(strValue)     
{
	return strValue.replace(/^\s*|\s*$/g,"");     
}     
function SetCookie(sName,sValue)  
{     
  document.cookie = sName + "=" + escape(sValue);     
}     
function GetCookie(sName)     
{     
	var aCookie = document.cookie.split(";");     
	for (var i=0; i < aCookie.length; i++)     
	{     
		var aCrumb = aCookie[i].split("=");     
		if (sName == Trim(aCrumb[0]))     
		{     
			return unescape(aCrumb[1]);     
		}     
	}
	return   null; 
}     
function scrollback(object)     
{     
	if(GetCookie("scroll")!=null)
	{
		var ss = document.getElementById(object);
		ss.scrollTop=GetCookie("scroll")
	}     
}      

/*****************************************************************************
�������ƣ�goToNextFoce
������ܣ����ڵ�Ԫ����ת
	ע�⣺ ��ҪԤ����rptType���������ͣ��ֱ�ΪDW->������,YW->1104����,Y0ԭ��������ʾ��������
		   ��1104������ҪԤ����startItem, endItem, colCount;1104������ҪԤ����startItem, endItem;ֻ�����ڶ�����
����	��object ���� 
����	��2007-08-23 
����    ��

�޸���  ��-----�޸���--------��Ҫ--------------------------
*****************************************************************************/ 
function goToNextFoce(obj)
{
	if(event.keyCode == 39 || event.keyCode == 13 || event.keyCode == 40 || event.keyCode == 37 || event.keyCode == 38)
	{				
		var strNewName = obj.name;
		/*
		var i = 100;
		do	
		{
			strNewName = getNextItemName(strNewName);
			
			// ������ѭ��

			if (i > 100)
				break;
			i++;
		}
		while(document.kkk.elements[strNewName] == null);
		*/
		var form = document.getElementsByTagName("form")[0];
		var inputs = form.getElementsByTagName("input"); 
		for(var i=0;i<inputs.length;i++){
			strNewName = getNextItemName(strNewName);
			var curInput = document.kkk.elements[strNewName];
			if(null != curInput){
				curInput.focus();
				
				curInput.select();
				
				//alert("��������ĩβ   ");
				break;
			}
		}
		/*
		if( document.kkk.elements[strNewName] != null )
		{
			var curInput = document.kkk.elements[strNewName];
			curInput.focus();
			curInput.select();
		}
		*/
		//event.keyCode = 35;
	}
	return false;
}

function getfocusSelected(obj)
{
		this.select();	
}

function getNextItemName(fullname)
{
	var len = fullname.length;
	var head = "";
	var tail = "";
	if(len == 13)
	{		
		head = fullname.substring(0, len - 9);
		var index = parseInt(fullname.substring(len - 9, len), 10);
		var start = parseInt(startItem.substring(len - 9, len), 10);
		var end = parseInt(endItem.substring(len - 9, len), 10);
		tail = getNextItemTail(index, start, end, 9);
	}
	else if(len == 19)
	{
		// 1104
		head = fullname.substring(0, len - 10);
		
		var lenstart = startItem.length;	
		var lenend = endItem.length;	
		
		var col = parseInt(fullname.substring(len - 5, len), 10);
		var row = parseInt(fullname.substring(len - 10, len - 5), 10);		
		var startCol = parseInt(startItem.substring(lenstart - 5, lenstart), 10);
		var startRow = parseInt(startItem.substring(lenstart - 10, lenstart - 5), 10);		
		var endCol = parseInt(endItem.substring(lenend - 5, lenend), 10);
		var endRow = parseInt(endItem.substring(lenend - 10, lenend - 5), 10);

		tail = getNextItemTailRC(row, col,startRow, endRow, startCol, endCol );
	}
	else if(rptType == "DW")
	{
		head = fullname.substring(0, len - 5);
		var index = parseInt(fullname.substring(len - 5, len), 10);
		var start = parseInt(startItem.substring(len - 5, len), 10);
		var end = parseInt(endItem.substring(len - 5, len), 10);
		tail = getNextItemTail(index, start, end, 5);
		
	}
	if(tail != "")
		fullname = head + tail;

	return fullname;
}

function getNextItemTail(index, start, end, mlen)
{		
	if(event.keyCode == 39)
		index = index + 1;
	else if (event.keyCode == 13 || event.keyCode == 40)
		index = index + colCount;
	else if (event.keyCode == 37 )
		index = index - 1;
	else if (event.keyCode == 38)	
		index = index - colCount;
	
	if(index > end || index < start)
		return ""
	
	var tail = "" + index;
	
	var len1 = tail.length;
	while(len1 < mlen )
	{
		tail = "0" + tail;
		len1++;
	}	
	return tail;
}

// 1104
function getNextItemTailRC(row, col, startRow, endRow, startCol, endCol)
{
	var tail;
	if(event.keyCode == 39)
	{		
		col++;
		if( col > endCol )
		{
			row++;
			col = startCol;
		}
	}
	else if (event.keyCode == 37 )
	{
		col--;
		if(col < startCol)
		{
			row--;
			col = endCol;
		}
	}
	else if (event.keyCode == 13 || event.keyCode == 40)
		row++;
	else if (event.keyCode == 38)
		row--;
	
	if(row < startRow || row > endRow)
		return "";
	
	row = "" + row;
	col = "" + col;
		
	var len1 = row.length;
	while(len1 < 5 )
	{
		row = "0" + row;
		len1++;
	}
	
	len1 = col.length;
	while(len1 < 5 )
	{
		col = "0" + col;
		len1++;
	}
	
	return row + col;
}

 
/*****************************************************************************
�������ƣ�RedirectFirstUrl
������ܣ�����˵�����ҳ����ת����һ�����ܵ�ĵ�һ������

����	�� 
����	��2007-08-25  
����    ��wangwy 
�޸���  ��--
******************************************************************************/
function RedirectFirstUrl()
{
	var firstUrl = document.getElementsByTagName("A")[0].href;
	parent.sysmain.location.href = firstUrl; 
}

/*****************************************************************************
�������ƣ�HideDropDownList
������ܣ�����DropDownList��סHeader���ֵ�BUG
����	��HeaderText Header��ʾ������

����	��2007-08-27  
����    ��wangwy 
�޸���  ��--
******************************************************************************/
function HideDropDownList(HeaderText)
{ 
	var str = '<table cellpadding=0 cellspacing=0 style="MARGIN: 0px; border:0px;width:100%;height=100%; filter:progid:DXImageTransform.Microsoft.Gradient(gradienttype=0, startcolorstr=#d4f0f9, endcolorstr=#b2def1);color:#2d6391;font-size:12px;font-weight:bold"><tr><td>' + HeaderText + '</td></tr></table>' ;
	var doc = window.frames["WebFrame"].document; 
	doc.open();
	doc.write('<html><head></head><body topmargin=-1 leftmargin=0>' + str + '</body></html>');
	doc.close(); 
}

/*****************************************************************************
�������ƣ�chkall
������ܣ��ı�ָ��Table������Checkbox״̬ ���ڲ�¼ָ������ж������ʱ��ȫѡ
����	��tableName Table����
		��inputName ��ѡ�����
����	��2008-12-10  
����    ��wangwy 
�޸���  ��--
******************************************************************************/
function chkall(tableName,inputName)
{ 
    //��ǰ��������
    var index = inputName.parentElement.cellIndex;   
    //��ѡ���״̬
    var state = inputName.checked;
    //����Table
    var objTable = document.getElementById(tableName);
    //ѭ��ÿ��   
    for (var iCount = 0; iCount < objTable.rows.length; iCount++)
    {   
        var arr = objTable.rows[iCount].cells[index].getElementsByTagName("input"); 
        for(var i = 0 ; i< arr.length ; i++)
        {
            if(arr[i].type == "checkbox")     
                arr[i].checked = state; 
        } 
    }  
}  