<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru"> <!-- Установите корректную кодировку -->
<head>
    <meta charset="UTF-8">
    <title><c:out value="${pageTitle}" default="My Web App"/></title>
    <link rel="stylesheet" type="text/css" href="/css/_header.css">
</head>
<body>
<header>
    <div class="logo">
        <img src="/static/logo.png" alt="Logo">
    </div>
    <nav>
        <ul class="centered-menu">
            <li><a href="/mainPage">Главная</a></li>
            <li><a href="https://kaktotak.by/kalkuliatory" target="_blank">Вычисления</a></li>
            <li><a href="/aboutUs">О нас</a></li>
        </ul>
        <ul>
            <li><a href="/user">Профиль</a></li>
        </ul>
    </nav>
</header>
</body>
</html>
