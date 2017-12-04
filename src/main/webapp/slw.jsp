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
<body><% @SuppressWarnings("unchecked") ArrayList<ArrayList<String>> cr = (ArrayList<ArrayList<String>>) request.getAttribute("cr"); @SuppressWarnings("unchecked") ArrayList<String> gr = (ArrayList<String>) request.getAttribute("gr"); %>
	<div class="abox menu"><% String type = (String) request.getAttribute("type"); pageContext.setAttribute("type", type, pageContext.APPLICATION_SCOPE); %>
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
			<table class="tbl"><% if (type.equals("gl")) { %>
				<tr>
					<td class="lft"><strong>Часть речи: </strong></td>
					<td class="rht"><% for (int i = 0; i < cr.size(); ++i) { %>
						<input type="checkbox" name="cr" value="<%= cr.get(i).get(0) %>"> <%= cr.get(i).get(1) %><br><% } %>
					</td>
				</tr><% } %>
				<tr>
					<td class="lft niz"><strong>Категория: </strong></td>
					<td class="rht niz"><% for (int i = 0; i < gr.size(); ++i) { %>
						<input type="checkbox" name="gr" value="<%= gr.get(i) %>"> <%= gr.get(i) %><br><% } %>
					</td>
				</tr>
				<tr>
					<td class="lft niz"><strong>Количество слов: </strong></td>
					<td class="rht niz"><input required class="txt" type="number" name="size" value="8"></td>
				</tr>
				<tr><% if (type.equals("gl")) { %>
					<td class="lft"><strong>С артиклем: </strong></td><% } else { %>
					<td class="lft"><strong>С переводом: </strong></td><% } %>
					<td class="rht">
						<input checked type="radio" name="flag" value="true"> да
						<input type="radio" name="flag" value="false"> нет
					</td>
				</tr><% if (!role.equals("gast")) { %>
				<tr>
					<td class="lft"><strong>Выбор режима: </strong></td>
					<td class="rht">
						<input checked type="radio" name="new" value="false"> Режим изучения<br>
						<input type="radio" name="new" value="true"> Режим повторения
					</td>
				</tr><% } %>
				<tr>
					<td class="lft niz"><button class="bt" type="button" onclick="history.back()">Назад</button></td>
					<td class="rht niz"><input class="bt" type="submit" value="Создать" name="create"></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>