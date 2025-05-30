<%@ include file="/templates/_header.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Форма</title>
    <link rel="stylesheet" href="/css/group/newGroup.css">
</head>
<body>
<div class="content">
    <form action="/groups/new" method="post">
        <div class="form-container">
            <label for="groupName">Название группы:</label><br>
            <input type="text" id="groupName" name="name" required>

            <label for="description">Описание:</label><br>
            <textarea id="description" name="description" required></textarea>

            <input type="submit" value="Создать группу" class="submit-button">
        </div>
    </form>
</div>
</body>
</html>
