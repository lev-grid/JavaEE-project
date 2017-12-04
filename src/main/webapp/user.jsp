<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<link rel="stylesheet" type="text/css" href="static/css/main_style.css">
	<title>Страница пользователя</title>
</head>
<body> <% @SuppressWarnings("unchecked") ArrayList<ArrayList<String>> words = (ArrayList<ArrayList<String>>) request.getAttribute("words"); %>
	<div class="abox menu">
		<form action="menu" method="post"><% String role = (String) request.getSession().getAttribute("role"); %>
			${name} <% if (!role.equals("gast")) { %>(${login})<% } if (role.equals("admin")) { %>
			<input class="bt" type="submit" value="Админ панель" name="admin"><% } if (role.equals("admin") || role.equals("moder")) { %>
			<input class="bt" type="submit" value="Модер панель" name="moder"><% } %>
			<input class="bt" type="submit" value="Главная" name="main">
			<input class="bt" type="submit" value="Выход" name="exit">
		</form>
	</div> <% if (request.getAttribute("del") == null) { %>
	<div class="abox">
		<h2>Основная информация:</h2>
		<br>
		<table class="tbl">
			<tr>
				<td class="lft"><strong>Имя: </strong></td>
				<td class="rht">${name}</td>
			</tr>
			<tr>
				<td class="lft"><strong>Логин: </strong></td>
				<td class="rht">${login}</td>
			</tr>
			<tr>
				<td class="lft"><strong>Роль: </strong></td>
				<td class="rht">${role}</td>
			</tr>
		</table>
	</div>
	<div class="abox">
		<h2>Изменение данных:</h2>
		<br>
		<table class="tbl">
			<tr>
				<td class="lft"><strong>Изменить имя: </strong></td>
				<td class="rht">
					<form action="user" method="post">
						<input autocomplete="off" required class="txt" type="text" name="nname" value="${name}">
						<input class="bt" type="submit" value="Изменить" name="chname">
					</form>
				</td>
			</tr>
			<tr>
				<td class="lft"><strong>Изменить пароль: </strong></td>
				<td class="rht">
					<form action="user" method="post">
						<input required class="txt" type="password" name="npassword">
						<input required class="txt" type="password" name="nrpassword">
						<input class="bt" type="submit" value="Изменить" name="chpass">
					</form>
				</td>
			</tr>
		</table>
	</div>
	<div class="abox">
		<h2>Статистика:</h2>
		<br>
		<table class="tbl"><% for (int i = 0; i < words.size(); ++i) { %>
			<tr>
				<td class="lft"><strong><%= words.get(i).get(0) %></strong></td>
				<td class="rht"><%= words.get(i).get(1) %></td>
			</tr><% } %>
		</table>
	</div>
	<div class="abox">
		<h2>Удалить аккаунт:</h2>
		<br>
		<form action="user" method="post" style="text-align: center;">
			<input class="bt" type="submit" value="Удалить" name="del1" style="background: #faa;">
		</form>
	</div><% } else { %>
	<div class="abox err">
		<h2>Удаление аккаунта</h2>
		<br>
		<h4>Вы уверены что хотите удалить аккаунт?</h4>
		<br>
		<form action="user" method="post" style="text-align: center;">
			<input class="bt" type="submit" value="Да" name="del2" style="background: #faa;">
			<input class="bt" type="submit" value="Нет" name="del3" style="background: #afa;">
		</form>
	</div><% } %>
</body>
</html>