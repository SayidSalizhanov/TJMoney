<%@ include file="/templates/_header.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>New Reminder</title>
    <link rel="stylesheet" href="/css/reminders/newReminder.css">
</head>
<body>

<div class="content">
    <form action="/reminders/new" method="post" class="reminder-form">
        <div class="form">
            <label for="title">Название:</label><br>
            <input type="text" id="title" name="title" required><br><br>

            <label for="datetime">Дата и время отправки:</label><br>
            <input type="datetime-local" id="datetime" name="datetime" required><br><br>

            <label for="message">Содержание:</label><br>
            <textarea id="message" name="message" required></textarea><br><br>

            <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">

            <c:if test="${not empty errorMessage}">
                <span style="color:red;"><c:out value="${errorMessage}"/></span><br>
            </c:if>

            <button type="submit" class="save-button">Сохранить</button>
        </div>
    </form>
</div>

</body>
</html>
