<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<link rel="stylesheet" type="text/css" href="static/css/main_style.css">
	<title>Вход</title>
</head>
<body>
	<div class="abox">
		<h2>Регистрация:</h2>
		<br>
		<form action="main" method="post">
			<table class="tbl">
				<tr>
					<td class="lft"><strong>Имя: </strong></td>
					<td class="rht"><input autocomplete="off" required class="txt" type="text"name="name"></td>
				</tr>
				<tr>
					<td class="lft"><strong>Логин: </strong></td>
					<td class="rht"><input autocomplete="off" required class="txt" type="text" name="login"></td>
				</tr>
				<tr>
					<td class="lft"><strong>Пароль: </strong></td>
					<td class="rht"><input required class="txt" type="password" name="password"></td>
				</tr>
				<tr>
					<td class="lft"><strong>Повторите пароль: </strong></td>
					<td class="rht"><input required class="txt" type="password" name="rpassword"></td>
				</tr>
				<tr>
					<td class="lft"><input class="bt" type="submit" value="Регистрация" name="breg"></td>
					<td class="rht"><input class="bt" type="reset" value="Сброс"></td>
				</tr>
			</table>
		</form>
		<br><br>
		<h2>Авторизация:</h2>
		<br>
		<form action="main" method="post">
			<table class="tbl">
				<tr>
					<td class="lft"><strong>Логин: </strong></td>
					<td class="rht"><input autocomplete="off" required class="txt" type="text" name="login"></td>
				</tr>
				<tr>
					<td class="lft"><strong>Пароль: </strong></td>
					<td class="rht"><input required class="txt" type="password" name="password"></td>
				</tr>
				<tr>
					<td class="lft"><input class="bt" type="submit" value="Авторизация" name="bauth"></td>
					<td class="rht"><input class="bt" type="reset" value="Сброс"></td>
				</tr>
			</table>
		</form>
	</div>
	
	<form action="main" method="post">
		<input class="tesst abox" type="submit" value="Тестовый запуск (без регистрации и смс)" name="btest">
	</form>
</body>
</html>