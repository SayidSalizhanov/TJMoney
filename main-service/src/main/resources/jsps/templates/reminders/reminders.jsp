<%@ include file="/templates/_header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Reminders</title>
    <link rel="stylesheet" href="/css/reminders/reminders.css">
</head>
<body>

<div class="content">
    <form action="/reminders/new" method="GET" class="create-reminder-form">
        <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">
        <button type="submit" class="create-button">Создать напоминание</button>
    </form>

    <div class="reminders-list">
        <c:forEach var="reminder" items="${reminders}">
            <form action="/reminder" method="get" class="reminder-form">
                <div class="reminder">
                    <input type="hidden" name="reminderId" value="<c:out value="${reminder.id}"/>">
                    <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">
                    <button type="submit" class="reminder-button">
                        <div class="reminder-details">
                            <div class="left-column">
                                <p>Название: <c:out value="${reminder.title}"/></p>
                            </div>
                            <div class="right-column">
                                <p>Статус: <c:out value="${reminder.status}"/></p>
                            </div>
                        </div>
                    </button>
                </div>
            </form>
        </c:forEach>
    </div>
    <div class="empty-list">
        <c:if test="${empty reminders}">
            <p>Тут ничего нет...</p>
        </c:if>
    </div>
</div>

</body>
</html>
