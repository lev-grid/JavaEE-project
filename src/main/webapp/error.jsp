<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<link rel="stylesheet" type="text/css" href="static/css/main_style.css">
	<title>Ошибка</title>
</head>
<body>
	<div class="abox err">
		<h2>${head}:</h2>
		<br>${disc}
	</div>
	
	<a class="tesst abox err" href="auth">Главная</a>
</body>
</html>