<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<link rel="stylesheet" type="text/css" href="static/css/main_style.css">
	<title>Админ панель</title>
</head>
<body><% @SuppressWarnings("unchecked") List<String> roles = (List<String>) request.getAttribute("roles"); %>
	<div class="abox menu">
		<form action="menu" method="post">
			${name} (${login})
			<input class="bt" type="submit" value="Главная" name="main">
			<input class="bt" type="submit" value="Модер панель" name="moder">
			<input class="bt" type="submit" value="Личный кабинет" name="user">
			<input class="bt" type="submit" value="Выход" name="exit">
		</form>
	</div>
	<div class="abox">
		<h4>Изменение уровня доступа</h4><br>
		<form action="admin" method="post">
			<table class="tbl">
				<tr>
					<td class="lft"><strong>Логин: </strong></td>
					<td class="rht"><input autocomplete="off" required class="txt" type="text" name="login"></td>
				</tr>
				<tr>
					<td class="lft"><strong>Роль: </strong></td>
					<td class="rht"><select required class="txt" name="role"><% for (int i = 0; i < roles.size(); ++i) { %>
					    <option><%= roles.get(i) %></option><% } %>
					   </select>
					</td>
				</tr>
				<tr>
					<td class="lft"><input class="bt" type="submit" value="Изменить" name="chrole"></td>
					<td class="rht"><input class="bt" type="reset" value="Сброс"></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>