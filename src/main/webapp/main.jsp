<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<link rel="stylesheet" type="text/css" href="static/css/main_style.css">
	<title>Главная страница</title>
</head>
<body>
	<div class="abox menu">
		<form action="menu" method="post"><% String role = (String) request.getSession().getAttribute("role"); %>
			${name} <% if (!role.equals("gast")) { %>(${login})<% } if (role.equals("admin")) { %>
			<input class="bt" type="submit" value="Админ панель" name="admin"><% } if (role.equals("admin") || role.equals("moder")) { %>
			<input class="bt" type="submit" value="Модер панель" name="moder"><% } if (!role.equals("gast")) { %>
			<input class="bt" type="submit" value="Личный кабинет" name="user"><% } %>
			<input class="bt" type="submit" value="Выход" name="exit">
		</form>
	</div>
	<a class="tesst abox" href="slw?type=gl">Изучение слов</a>
	<a class="tesst abox" href="slw?type=ddd">Родовые артикли</a>
	<a class="tesst abox" href="slw?type=mn">Множественное число</a>
	<a class="tesst abox" href="chisl">Числительные</a>
</body>
</html>