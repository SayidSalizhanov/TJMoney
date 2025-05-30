<%@ include file="/templates/_header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Applications</title>
    <link rel="stylesheet" href="/css/user/userApplications.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.5.0/font/bootstrap-icons.min.css">
    <script src="/js/user/deleteUserApplications.js" defer></script>
</head>
<body>

<div class="content">
    <div class="applications-list">
        <c:forEach var="application" items="${applicationsDTOs}">
            <div class="application">
                <div class="application-info">
                    <p>Название: <c:out value="${application.groupName}"/></p>
                    <p>Дата: <c:out value="${application.sendAt}"/></p>
                    <p>Статус: <c:out value="${application.status}"/></p>
                </div>
                <form action="/user/applications" method="post" class="delete-form">
                    <input type="hidden" name="_method" value="DELETE">
                    <input type="hidden" name="applicationId" value="<c:out value="${application.id}"/>">
                    <button type="submit" class="delete-button" id="delete-button-<c:out value="${application.id}"/>">
                        <i class="bi bi-trash"></i>
                    </button>
                </form>
            </div>
        </c:forEach>
    </div>
    <div class="empty-list">
        <c:if test="${empty applicationsDTOs}">
            <p>Тут ничего нет...</p>
        </c:if>
    </div>
</div>

</body>
</html>
