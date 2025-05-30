<%@ include file="/templates/_header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Участники группы</title>
    <link rel="stylesheet" href="/css/group/members.css">
</head>
<body>

<div class="content-wrapper">
    <div class="content">
        <h2>Участники группы</h2>
        <div class="members-list">
            <c:forEach var="member" items="${members}">
                <div class="member">
                    <p><strong>Пользователь:</strong> <c:out value="${member.username}"/></p>
                    <p><strong>Дата присоединения:</strong> <c:out value="${member.joinedAt}"/></p>
                    <p><strong>Роль:</strong> <c:out value="${member.role}"/></p>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

</body>
</html>
