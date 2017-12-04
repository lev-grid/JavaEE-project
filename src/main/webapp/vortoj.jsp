<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<title>Изучение слов</title>
</head>
<body>
	<% String[] r1 = (String[]) request.getAttribute("r1"); %>
	<% String[] d1 = (String[]) request.getAttribute("d1"); %>
	<div id="cont">
		<div id="zg">
			<div id="knp"></div>
			<div id="nzg">monate</div>
		</div>
		<div id="tge"></div>
		<div id="tru"></div>
		<%-- <table id="tabla">
			<% for (int i = 0; i < d1.length; ++i) { %>
			<tr>
				<td id="de"><%= d1[i] %></td>
				<td id="ru"><%= r1[i] %></td>
			</tr>
			<% } %>
		</table> --%>
	</div>
</body>
</html>