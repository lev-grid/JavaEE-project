<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<link rel="stylesheet" type="text/css" href="static/css/main_style.css">
	<title>Изучение слов</title>
</head>
<body> <% @SuppressWarnings("unchecked") ArrayList<ArrayList<String>> words = (ArrayList<ArrayList<String>>) request.getAttribute("words"); pageContext.setAttribute("words", words, pageContext.APPLICATION_SCOPE); %>
	<div class="abox menu">
		<form action="menu" method="post"><% String role = (String) request.getSession().getAttribute("role"); %>
			${name} <% if (!role.equals("gast")) { %>(${login})<% } if (role.equals("admin")) { %>
			<input class="bt" type="submit" value="Админ панель" name="admin"><% } if (role.equals("admin") || role.equals("moder")) { %>
			<input class="bt" type="submit" value="Модер панель" name="moder"><% } if (!role.equals("gast")) { %>
			<input class="bt" type="submit" value="Личный кабинет" name="user"><% } %>
			<input class="bt" type="submit" value="Выход" name="exit">
		</form>
	</div>
	<div class="abox">
		<form action="lw" method="post">
			<table cellspacing="0" cellpadding="0" class="tbl"><% for (int i = 0; i < words.size(); ++i) { %>
				<tr class="res_<%= words.get(i).get(4) %>">
					<td class="lft"><%= words.get(i).get(1) %></td>
					<td class="rht"><%= words.get(i).get(2) %> (<%= words.get(i).get(3) %>)</td>
				</tr><% } %>
				<tr>
					<td class="lft niz"><button class="bt" type="button" onclick="location.href='main'" >Закончить</button></td>
					<td class="rht niz"><input class="bt" type="submit" value="Продолжить" name="start"></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>