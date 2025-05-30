<%@ include file="/templates/_header.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Настройки</title>
    <link rel="stylesheet" href="/css/user/settings.css">
</head>
<body>

<div class="content-wrapper">
    <div class="content">
        <form action="/user/settings" method="post" class="settings-form">
            <input type="hidden" name="_method" value="PUT">

            <div class="form-group">
                <label for="username">Имя пользователя:</label>
                <input type="text" id="username" name="username" value="<c:out value="${user.username}"/>">
            </div>

            <div class="form-group">
                <label for="telegramId">Telegram:</label>
                <input type="text" id="telegramId" name="telegramId" value="<c:out value="${user.telegramId}"/>">
            </div>

            <div class="form-group">
                <label for="sendingToTelegram">Хотите получать напоминания и другую информацию через телеграмм бота?</label>
                <input type="checkbox" id="sendingToTelegram" name="sendingToTelegram">
            </div>

            <div class="form-group">
                <label for="sendingToEmail">Хотите получать напоминания и другую информацию на почту?</label>
                <input type="checkbox" id="sendingToEmail" name="sendingToEmail">
            </div>

            <c:if test="${not empty errorMessage}">
                <span style="color:red;"><c:out value="${errorMessage}"/></span><br>
            </c:if>

            <button type="submit" class="save-button">Сохранить изменения</button>
        </form>

        <div class="button-group">
            <form action="/user/changePassword" method="GET" class="settings-form-inline">
                <button type="submit" class="small-button">Сменить пароль</button>
            </form>

            <form action="/user/changeAvatar" method="GET" class="settings-form-inline">
                <button type="submit" class="small-button photo-button">Сменить аватар</button>
            </form>

            <form action="/user/settings" method="POST" class="settings-form-inline">
                <input type="hidden" name="_method" value="DELETE">
                <button type="submit" class="small-button delete-button">Удалить пользователя</button>
            </form>
        </div>
    </div>
</div>

</body>
</html>
