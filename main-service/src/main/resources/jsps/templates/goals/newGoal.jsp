<%@ include file="/templates/_header.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>NewGoal</title>
    <link rel="stylesheet" href="/css/goals/newGoal.css">
</head>
<body>

<div class="content">
    <form action="/goals/new" method="post" class="goal-form">
        <div class="form">
            <label for="title">Название:</label><br>
            <input type="text" id="title" name="title" required><br><br>

            <label for="progress">Прогресс:</label><br>
            <input type="number" id="progress" name="progress" min="0" max="100" required><br><br>

            <label for="description">Содержание:</label><br>
            <textarea id="description" name="description" required></textarea><br><br>

            <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">

            <button type="submit" class="save-button">Сохранить цель</button>
        </div>
    </form>
</div>

</body>
</html>
