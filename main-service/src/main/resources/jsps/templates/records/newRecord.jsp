<%@ include file="/templates/_header.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>New Record</title>
    <link rel="stylesheet" href="/css/records/newRecord.css">
</head>
<body>

<div class="content">
    <form action="/records/new" method="post" class="record-form">
        <div class="form">
            <label for="title">Название:</label><br>
            <input type="text" id="title" name="title" required><br><br>

            <label for="content">Содержание:</label><br>
            <textarea id="content" name="content" required></textarea><br><br>

            <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">

            <button type="submit" class="save-button">Сохранить запись</button>
        </div>
    </form>
</div>

</body>
</html>
