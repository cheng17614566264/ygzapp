<%@page import="java.io.OutputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileInputStream"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
	<%
String file="E:/workspacechongqing/uploadFile/fileName/12.jpeg";
FileInputStream in =new FileInputStream(new File(file));
OutputStream o=response.getOutputStream();
int i=0;
byte[] buffer=new byte[1024*8];
while((i=in.read(buffer))!=-1){
	o.write(buffer, 0, i);
}

o.flush();
in.close();
o.close();

out.clear(); 
out = pageContext.pushBody();
%>
</body>
</html>