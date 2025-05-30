<%@ include file="/templates/_header.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Record</title>
    <link rel="stylesheet" href="/css/records/record.css">
</head>
<body>

<div class="content">
    <form action="/record" method="post" class="record-form">
        <input type="hidden" name="_method" value="PUT">
        <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">
        <input type="hidden" name="recordId" value="<c:out value="${recordId}"/>">

        <div class="form">
            <label for="title">Название:</label><br>
            <input type="text" id="title" name="title" value="<c:out value="${record.title}"/>" required><br><br>

            <label for="createdAt">Создана:</label><br>
            <p id="createdAt"><c:out value="${record.createdAt}"/></p><br>

            <label for="updatedAt">Изменена:</label><br>
            <p id="updatedAt"><c:out value="${record.updatedAt}"/></p><br>

            <label for="content">Содержание:</label><br>
            <textarea id="content" name="content" required><c:out value="${record.content}"/></textarea><br><br>

            <button type="submit" class="save-button">Сохранить изменения</button>
        </div>
    </form>

    <form action="/record" method="post" class="delete-form">
        <input type="hidden" name="_method" value="DELETE">
        <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">
        <input type="hidden" name="recordId" value="<c:out value="${recordId}"/>">

        <button type="submit" class="delete-button">Удалить запись</button>
    </form>
</div>

</body>
</html>
