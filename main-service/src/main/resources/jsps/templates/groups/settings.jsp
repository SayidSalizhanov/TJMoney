<%@ include file="/templates/_header.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Настройки группы</title>
    <link rel="stylesheet" href="/css/group/settings.css">
</head>
<body>

<div class="content-wrapper">
    <div class="content">
        <h2>Настройки группы</h2>
        <form action="/group/settings" method="post" class="settings-form">
            <input type="hidden" name="_method" value="PUT">
            <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">

            <div class="form-group">
                <label for="name">Название группы:</label>
                <input type="text" id="name" name="name" value="<c:out value="${group.name}"/>">
            </div>

            <div class="form-group">
                <label for="description">Описание:</label>
                <textarea id="description" name="description"><c:out value="${group.description}"/></textarea>
            </div>

            <c:if test="${not empty errorMessage}">
                <span class="error-message"><c:out value="${errorMessage}"/></span>
            </c:if>

            <button type="submit" class="save-button">Сохранить изменения</button>
        </form>

        <form action="/group/settings" method="POST" class="delete-form">
            <input type="hidden" name="_method" value="DELETE">
            <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">
            <button type="submit" class="delete-button">Удалить группу</button>
        </form>
    </div>
</div>

</body>
</html>
