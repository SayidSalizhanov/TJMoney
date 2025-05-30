<%@ include file="/templates/_header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Transactions</title>
    <link rel="stylesheet" href="/css/transactions/transactions.css">
</head>
<body>

<div class="content">
    <form action="/transactions/new" method="GET" class="create-transaction-form">
        <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">
        <button type="submit" class="create-button">Создать транзакцию</button>
    </form>

    <div class="transactions-list">
        <c:forEach var="transaction" items="${transactions}">
            <form action="/transaction" method="get" class="transaction-form">
                <div class="transaction">
                    <input type="hidden" name="transactionId" value="<c:out value="${transaction.transactionId}"/>">
                    <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">
                    <button type="submit" class="transaction-button">
                        <div class="transaction-details">
                            <div class="left-column">
                                <c:if test="${groupId != 0}">
                                    <p>Участник: <c:out value="${transaction.username}"/></p>
                                </c:if>
                                <c:if test="${groupId == 0}">
                                    <p>Финансы: <c:out value="${transaction.amount}"/></p>
                                </c:if>
                                <p>Дата: <c:out value="${transaction.dateTime}"/></p>
                            </div>
                            <div class="right-column">
                                <p>Категория: <c:out value="${transaction.category}"/></p>
                                <p>Тип: <c:out value="${transaction.type}"/></p>
                            </div>
                        </div>
                    </button>
                </div>
            </form>
        </c:forEach>
    </div>
    <div class="empty-list">
        <c:if test="${empty transactions}">
            <p>Тут ничего нет...</p>
        </c:if>
    </div>
</div>

</body>
</html>
