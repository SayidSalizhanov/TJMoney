<%@ include file="_header.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Register</title>
    <link rel="stylesheet" href="/css/register.css">
</head>
<body>
<div class="content">
    <h1>Добро пожаловать!</h1>
    <h2>Регистрация</h2>
    <form action="/register" method="post" class="registration-form">
        <label for="username">Введите имя:</label>
        <input type="text" id="username" name="username" required>

        <label for="email">Введите email:</label>
        <input type="email" id="email" name="email" required>

        <label for="password">Введите пароль:</label>
        <input type="password" id="password" name="password" minlength="8" required>

        <label for="confirmPassword">Повторите пароль:</label>
        <input type="password" id="confirmPassword" name="confirmPassword" required>

        <button type="submit">Зарегистрироваться</button>
    </form>
    <c:if test="${not empty errorMessage}">
        <span style="color:red;">
            <c:out value="${errorMessage}" />
        </span><br>
    </c:if>
    <p>Уже зарегистрированы? <a href="/login">Войти</a></p>
</div>
</body>
</html>
