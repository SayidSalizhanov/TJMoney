<%@ include file="/templates/_header.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Transaction</title>
    <link rel="stylesheet" href="/css/transactions/transaction.css">
</head>
<body>

<div class="content">
    <form action="/transaction" method="post" class="transaction-form">
        <div class="form">
            <input type="hidden" name="_method" value="PUT">
            <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">
            <input type="hidden" name="transactionId" value="<c:out value="${transactionId}"/>">

            <label for="amount">Стоимость:</label><br>
            <input type="number" id="amount" name="amount" min="0" max="1000000" value="<c:out value="${transaction.amount}"/>" required><br><br>

            <label for="type">Тип:</label><br>
            <p id="type"><c:out value="${transaction.type}"/></p><br>
            <input type="hidden" name="type" value="<c:out value="${transaction.type}"/>">

            <label for="category">Категория:</label><br>
            <p id="category"><c:out value="${transaction.category}"/></p><br>
            <input type="hidden" name="category" value="<c:out value="${transaction.category}"/>">

            <label for="dateTime">Дата и время:</label><br>
            <p id="dateTime"><c:out value="${transaction.dateTime}"/></p><br>

            <c:if test="${not empty ownerName}">
                <label for="ownerName">Участник:</label><br>
                <p id="ownerName"><c:out value="${ownerName}"/></p><br>
            </c:if>

            <label for="description">Описание:</label><br>
            <textarea id="description" name="description" required><c:out value="${transaction.description}"/></textarea><br><br>

            <button type="submit" class="save-button">Сохранить изменения</button>
        </div>
    </form>

    <form action="/transaction" method="POST" class="delete-form">
        <input type="hidden" name="_method" value="DELETE">
        <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">
        <input type="hidden" name="transactionId" value="<c:out value="${transactionId}"/>">

        <button type="submit" class="delete-button">Удалить транзакцию</button>
    </form>
</div>

</body>
</html>
