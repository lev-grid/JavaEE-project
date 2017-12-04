<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<title>der-das-die</title>
</head>
<body>
<%-- 	<% String[] de = (String[]) request.getAttribute("de"); %>
	<% String[] ru = (String[]) request.getAttribute("ru"); %>
	<% String[] gn = (String[]) request.getAttribute("gn"); %> --%>
	<div id="cont">
		<div id="zg">
			<div id="knp"></div>
			<div id="nzg">monate</div>
		</div>
		<table id="tabla">
			<% for (int i = 0; i < (int) request.getAttribute("count"); ++i) { %>
			<tr>
				<td id="de"><%= ((String) request.getAttribute("gn"+i)).concat(" ").concat((String) request.getAttribute("de"+i)) %></td>
				<td id="ru"><%= request.getAttribute("ru"+i) %></td>
			</tr>
			<% } %>
		</table>
	</div>
</body>
</html>