<%@ include file="/templates/_header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Records</title>
    <link rel="stylesheet" href="/css/records/records.css">
</head>
<body>

<div class="content">
    <form action="/records/new" method="GET" class="create-record-form">
        <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">
        <button type="submit" class="create-button">Создать запись</button>
    </form>

    <div class="records-list">
        <c:forEach var="record" items="${records}">
            <form action="/record" method="get" class="record-form">
                <div class="record">
                    <input type="hidden" name="recordId" value="<c:out value="${record.id}"/>">
                    <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">
                    <button type="submit" class="record-button">
                        <div class="record-details">
                            <p>Название: <c:out value="${record.title}"/></p>
                        </div>
                    </button>
                </div>
            </form>
        </c:forEach>
    </div>
    <div class="empty-list">
        <c:if test="${empty records}">
            <p>Тут ничего нет...</p>
        </c:if>
    </div>
</div>

</body>
</html>
