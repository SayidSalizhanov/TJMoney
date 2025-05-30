<%@ include file="/templates/_header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>UserGroups</title>
    <link rel="stylesheet" href="/css/user/userGroups.css">
</head>
<body>

<div class="content">
    <div class="button-group">
        <form action="/user/applications" method="GET">
            <button type="submit" class="action-button">Мои заявки</button>
        </form>

        <form action="/groups/new" method="GET">
            <button type="submit" class="action-button">Создать группу</button>
        </form>

        <form action="/groups" method="GET">
            <button type="submit" class="action-button">Найти группу</button>
        </form>
    </div>

    <div class="groups-list">
        <c:forEach var="group" items="${userGroupsDTOs}">
            <form action="/group" method="get" class="group-form">
                <div class="group">
                    <input type="hidden" name="groupId" value="<c:out value="${group.groupId}"/>">
                    <button type="submit" class="group-button">
                        <div class="group-details">
                            <div class="left-column">
                                <p>Название: <c:out value="${group.groupName}"/></p>
                                <p>Роль: <c:out value="${group.role}"/></p>
                            </div>
                            <div class="right-column">
                                <p>Описание: <c:out value="${group.description}"/></p>
                            </div>
                        </div>
                    </button>
                </div>
            </form>
        </c:forEach>
    </div>
    <div class="empty-list">
        <c:if test="${empty userGroupsDTOs}">
            <p>Тут ничего нет...</p>
        </c:if>
    </div>
</div>

</body>
</html>
