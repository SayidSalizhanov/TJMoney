<%@ include file="/templates/_header.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Reminder</title>
    <link rel="stylesheet" href="/css/reminders/reminder.css">
</head>
<body>

<div class="content">
    <form action="/reminder" method="POST" class="reminder-form">
        <div class="form">
            <input type="hidden" name="_method" value="PUT">
            <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">
            <input type="hidden" name="reminderId" value="<c:out value="${reminderId}"/>">

            <label for="title">Название:</label><br>
            <input type="text" id="title" name="title" value="<c:out value="${reminder.title}"/>" required><br><br>

            <label for="datetime">Дата и время:</label><br>
            <input type="datetime-local" id="datetime" name="datetime" required value="<c:out value="${reminder.sendAt}"/>"><br><br>

            <label for="status">Статус:</label><br>
            <p id="status"><c:out value="${reminder.status}"/></p><br>

            <label for="message">Содержание:</label><br>
            <textarea id="message" name="message" required><c:out value="${reminder.message}"/></textarea><br><br>

            <button type="submit" class="save-button">Сохранить изменения</button>
        </div>
    </form>

    <form action="/reminder" method="POST" class="delete-form">
        <input type="hidden" name="_method" value="DELETE">
        <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">
        <input type="hidden" name="reminderId" value="<c:out value="${reminderId}"/>">

        <button type="submit" class="delete-button">Удалить напоминание</button>
    </form>
</div>

</body>
</html>
