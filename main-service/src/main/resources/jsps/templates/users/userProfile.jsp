<%@ include file="/templates/_header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Профиль</title>
    <link rel="stylesheet" href="/css/user/profile.css">
</head>
<body>

<div class="content-wrapper">
    <div class="content">
        <div class="info-avatar">
            <div class="info">
                <h2>Информация о пользователе</h2>
                <p><strong>ID:</strong> <c:out value="${user.id}"/></p>
                <p><strong>Имя пользователя:</strong> <c:out value="${user.username}"/></p>
                <p><strong>Почта:</strong> <c:out value="${user.email}"/></p>
                <c:if test="${user.telegramId == null}">
                    <p><strong>Telegram:</strong> отсутствует</p>
                </c:if>
                <c:if test="${user.telegramId != null}">
                    <p><strong>Telegram:</strong> <c:out value="${user.telegramId}"/></p>
                </c:if>
            </div>
            <div class="avatar">
                <img src="<c:out value="${urlPhoto}"/>" alt="Avatar" class="rounded-circle" width="200" height="200">
            </div>
        </div>

        <div class="button-group">
            <form action="/user/settings" method="GET">
                <button type="submit" class="nav-button">Настройки</button>
            </form>

            <form action="/user/groups" method="GET">
                <button type="submit" class="nav-button">Группы</button>
            </form>

            <form action="/transactions" method="GET">
                <button type="submit" class="nav-button">Транзакции</button>
            </form>
        </div>

        <hr>

        <form action="/user" method="GET" class="filter-form">
            <label for="incomePeriod">Выбрать период:</label>
            <select id="incomePeriod" name="period">
                <option value="all">Все время</option>
                <option value="day">День</option>
                <option value="month">Месяц</option>
                <option value="year">Год</option>
            </select>
            <button type="submit">Применить</button>
        </form>

        <div class="financial-section">
            <div class="financial-info">
                <h2>Доходы</h2>
                <p><strong>Общий доход:</strong> <c:out value="${income}"/></p>
                <p><strong>Заработная плата:</strong> <c:out value="${Заработная_плата}"/></p>
                <p><strong>Прибыль от бизнеса:</strong> <c:out value="${Прибыль_от_бизнеса}"/></p>
                <p><strong>Дивиденты:</strong> <c:out value="${Дивиденты}"/></p>
                <p><strong>Аренда:</strong> <c:out value="${Аренда}"/></p>
                <p><strong>Премии и бонусы:</strong> <c:out value="${Премии_и_бонусы}"/></p>
                <p><strong>Интересы:</strong> <c:out value="${Интересы}"/></p>
                <p><strong>Пенсии и пособия:</strong> <c:out value="${Пенсии_и_пособия}"/></p>
                <p><strong>Другое:</strong> <c:out value="${Другие_доходы}"/></p>
            </div>

            <div class="financial-info">
                <h2>Расходы</h2>
                <p><strong>Общие расходы:</strong> <c:out value="${expense}"/></p>
                <p><strong>Еда и напитки:</strong> <c:out value="${Еда_и_напитки}"/></p>
                <p><strong>Транспорт:</strong> <c:out value="${Транспорт}"/></p>
                <p><strong>Жилье:</strong> <c:out value="${Жилье}"/></p>
                <p><strong>Развлечения:</strong> <c:out value="${Развлечения}"/></p>
                <p><strong>Одежда:</strong> <c:out value="${Одежда}"/></p>
                <p><strong>Здоровье:</strong> <c:out value="${Здоровье}"/></p>
                <p><strong>Образование:</strong> <c:out value="${Образование}"/></p>
                <p><strong>Другое:</strong> <c:out value="${Другие_расходы}"/></p>
            </div>
        </div>

        <hr><br>

        <div class="button-group-wrapper">
            <div class="button-group">
                <form action="/goals" method="GET">
                    <button type="submit" class="goal-button">Цели</button>
                </form>

                <form action="/reminders" method="GET">
                    <button type="submit" class="reminder-button">Напоминания</button>
                </form>

                <form action="/records" method="GET">
                    <button type="submit" class="record-button">Записи</button>
                </form>
            </div>

            <form action="/user" method="post">
                <button type="submit" class="logout-button">Выйти</button>
            </form>
        </div>
    </div>
</div>

</body>
</html>
