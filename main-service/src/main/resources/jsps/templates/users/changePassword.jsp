<%@ include file="/templates/_header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Смена пароля</title>
    <link rel="stylesheet" href="/css/user/changePassword.css">
</head>
<body>

<div class="content-wrapper">
    <div class="content">
        <h2>Смена пароля</h2>
        <form method="post" action="/user/changePassword" class="password-form">
            <div class="form-group">
                <label for="oldPassword">Старый пароль:</label>
                <input type="password" id="oldPassword" name="oldPassword" required>
            </div>

            <div class="form-group">
                <label for="newPassword">Новый пароль:</label>
                <input type="password" id="newPassword" name="newPassword" required>
            </div>

            <div class="form-group">
                <label for="repeatPassword">Повторите пароль:</label>
                <input type="password" id="repeatPassword" name="repeatPassword" required>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="error-message"><c:out value="${errorMessage}" /></div>
            </c:if>

            <button type="submit" class="submit-button">Сменить пароль</button>
        </form>
    </div>
</div>

</body>
</html>
