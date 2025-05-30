<%@ include file="/templates/_header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Goals</title>
    <link rel="stylesheet" href="/css/goals/goals.css">
</head>
<body>

<div class="content">
    <form action="/goals/new" method="GET" class="create-goal-form">
        <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">
        <button type="submit" class="create-button">Создать цель</button>
    </form>

    <div class="goals-list">
        <c:forEach var="goal" items="${goals}">
            <form action="/goal" method="get" class="goal-form">
                <div class="goal">
                    <input type="hidden" name="goalId" value="<c:out value="${goal.id}"/>">
                    <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">
                    <button type="submit" class="goal-button">
                        <div class="goal-details">
                            <div class="left-column">
                                <p>Название: <c:out value="${goal.title}"/></p>
                            </div>
                            <div class="right-column">
                                <p>Прогресс: <c:out value="${goal.progress}"/></p>
                            </div>
                        </div>
                    </button>
                </div>
            </form>
        </c:forEach>
    </div>
    <div class="empty-list">
        <c:if test="${empty goals}">
            <p>Тут ничего нет...</p>
        </c:if>
    </div>
</div>

</body>
</html>
