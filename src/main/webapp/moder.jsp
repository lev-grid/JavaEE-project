<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList, org.bson.Document, java.util.List" %>
<% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<link rel="stylesheet" type="text/css" href="static/css/main_style.css">
	<title>Модер панель</title>
</head>
<body><% @SuppressWarnings("unchecked") ArrayList<ArrayList<String>> cr = (ArrayList<ArrayList<String>>) request.getAttribute("cr"); @SuppressWarnings("unchecked") ArrayList<String> gr = (ArrayList<String>) request.getAttribute("gr"); %>
	<div class="abox menu">
		<form action="menu" method="post"><% String role = (String) request.getSession().getAttribute("role"); %>
			${name} (${login})<% if (role.equals("admin")) { %>
			<input class="bt" type="submit" value="Админ панель" name="admin"><% } %>
			<input class="bt" type="submit" value="Главная" name="main">
			<input class="bt" type="submit" value="Личный кабинет" name="user">
			<input class="bt" type="submit" value="Выход" name="exit">
		</form>
	</div><% String type = (String) request.getAttribute("type"); if (type == null) { %>
	<div class="abox">
		<h2>Работа с данными</h2><br>
		<form action="moder" method="post">
			<table class="tbl">
				<tr>
					<td class="lft"><input class="bt" type="submit" value="Изменить данные" name="schdata"></td>
					<td class="rht"><input class="bt" type="submit" value="Добавить данные" name="saddata"></td>
				</tr>
			</table>
		</form>
	</div><% } else if (type.equals("search")) { %>
	<div class="abox">
		<h2>Изменение данных</h2><br>
		<h4>Поиск данных для обновления</h4><br>
		<form action="moder" method="post">
			<table class="tbl">
				<tr>
					<td class="lft"><strong>На немецком: </strong></td>
					<td class="rht"><input autocomplete="off" class="txt" type="text" name="de"></td>
				</tr>
				<tr>
					<td class="lft"><strong>На русском: </strong></td>
					<td class="rht"><input autocomplete="off" class="txt" type="text" name="ru"></td>
				</tr>
				<tr>
					<td class="lft"><strong>Часть речи: </strong></td>
					<td class="rht"><% for (int i = 0; i < cr.size(); ++i) { %>
						<input type="checkbox" name="cr" value="<%= cr.get(i).get(0) %>"> <%= cr.get(i).get(1) %><br><% } %>
					</td>
				</tr>
				<tr>
					<td class="lft niz"><strong>Категория: </strong></td>
					<td class="rht niz"><% for (int i = 0; i < gr.size(); ++i) { %>
						<input type="checkbox" name="gr" value="<%= gr.get(i) %>"> <%= gr.get(i) %><br><% } %>
					</td>
				</tr>
				<tr>
					<td class="lft"><button class="bt" type="button" onclick="history.back()">Назад</button></td>
					<td class="rht"><input class="bt" type="submit" value="Поиск" name="search"></td>
				</tr>
			</table>
		</form>
	</div><% } else if (type.equals("chdata")) { @SuppressWarnings("unchecked") ArrayList<Document> words = (ArrayList<Document>) request.getAttribute("words"); %>
	<% for (int i = 0; i < words.size(); ++i) { %>
	<div class="abox">
		<h2>Изменение данных</h2><br>
		<form action="moder" method="post">
			<input type="hidden" name="id" value="<%= words.get(i).getObjectId("_id") %>">
			<table class="tbl">
				<tr>
					<td class="lft"><strong>На немецком: </strong></td>
					<td class="rht"><input autocomplete="off" class="txt" type="text" name="de" value="<%= words.get(i).getString("de") %>"></td>
				</tr>
				<tr>
					<td class="lft"><strong>На русском: </strong></td><% @SuppressWarnings("unchecked") String rus = ((List<String>) words.get(i).get("ru")).toString().replace("[", "").replace("]", ""); %>
					<td class="rht"><input autocomplete="off" class="txt" type="text" name="ru" value="<%= rus %>"></td>
				</tr>
				<tr>
					<td class="lft niz"><strong>Категория: </strong></td><% @SuppressWarnings("unchecked") String kat = ((List<String>) words.get(i).get("gr")).toString().replace("[", "").replace("]", ""); %>
					<td class="rht niz"><input autocomplete="off" class="txt" type="text" name="gr" value="<%= kat %>"></td>
				</tr><% String chr = words.get(i).getString("cr"); if (chr.equals("сущ")) { %>
				<tr>
					<td class="lft niz"><strong>Артикль: </strong></td>
					<td class="rht niz"><input autocomplete="off" class="txt" type="text" name="gn" value="<%= words.get(i).getString("gn") %>"></td>
				</tr>
				<tr>
					<td class="lft niz"><strong>Родительный падеж: </strong></td>
					<td class="rht niz"><input autocomplete="off" class="txt" type="text" name="rp" value="<%= words.get(i).getString("rp") %>"></td>
				</tr>
				<tr>
					<td class="lft niz"><strong>Множественное число: </strong></td>
					<td class="rht niz"><input autocomplete="off" class="txt" type="text" name="mn" value="<%= words.get(i).getString("mn") %>"></td>
				</tr><% } %>
				<tr>
					<td class="lft"><button class="bt" type="button" onclick="history.back()">Назад</button></td>
					<td class="rht"><input class="bt" type="submit" value="Изменить" name="chdata"></td>
				</tr>
			</table>
		</form>
	</div><% } } else if (type.equals("addata")) { %>
	<div class="abox">
		<h2>Добавление данных</h2><br>
		<form action="moder" method="post">
			<table class="tbl">
				<tr>
					<td class="lft"><strong>На немецком: </strong></td>
					<td class="rht"><input autocomplete="off" class="txt" type="text" name="de"></td>
				</tr>
				<tr>
					<td class="lft"><strong>На русском: </strong></td>
					<td class="rht"><input autocomplete="off" class="txt" type="text" name="ru"></td>
				</tr>
				<tr>
					<td class="lft niz"><strong>Категория: </strong></td>
					<td class="rht niz"><input autocomplete="off" class="txt" type="text" name="gr"></td>
				</tr>
				<tr>
					<td class="lft niz"><strong>Артикль: </strong></td>
					<td class="rht niz"><input autocomplete="off" class="txt" type="text" name="gn" ></td>
				</tr>
				<tr>
					<td class="lft niz"><strong>Родительный падеж: </strong></td>
					<td class="rht niz"><input autocomplete="off" class="txt" type="text" name="rp"></td>
				</tr>
				<tr>
					<td class="lft niz"><strong>Множественное число: </strong></td>
					<td class="rht niz"><input autocomplete="off" class="txt" type="text" name="mn"></td>
				</tr>
				<tr>
					<td class="lft"><button class="bt" type="button" onclick="history.back()">Назад</button></td>
					<td class="rht"><input class="bt" type="submit" value="Добавить" name="addata"></td>
				</tr>
			</table>
		</form>
	</div><% } %>
</body>
</html>