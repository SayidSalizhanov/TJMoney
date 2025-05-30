<%@ include file="/templates/_header.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>newTransaction</title>
    <link rel="stylesheet" href="/css/transactions/newTransaction.css">
    <script src="/js/transaction/transactionCategory.js" defer></script>
</head>
<body>

<div class="content">
    <form action="/transactions/new" method="post" class="transaction-form">
        <div class="form">
            <label for="amount">Стоимость:</label><br>
            <input type="number" id="amount" name="amount" min="0" max="1000000" required><br><br>

            <label for="type">Тип:</label><br>
            <select id="type" name="type" required>
                <option value="Доход">Доход</option>
                <option value="Расход">Расход</option>
            </select><br><br>

            <label for="category">Категория:</label><br>
            <select id="category" name="category" required></select><br><br>

            <label for="datetime">Дата и время:</label><br>
            <input type="datetime-local" id="datetime" name="datetime" required><br><br>

            <label for="description">Описание:</label><br>
            <textarea id="description" name="description" required></textarea><br><br>

            <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">

            <c:if test="${not empty errorMessage}">
                <span style="color:red;"><c:out value="${errorMessage}"/></span><br>
            </c:if>

            <button type="submit" class="save-button">Сохранить</button>
        </div>
    </form>

    <div class="upload-xls">
        <form action="/transactions/new/uploadTransactions" method="get">
            <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">
            <button type="submit" class="upload-button">Загрузить excel таблицу</button>
        </form>
    </div>
</div>

</body>
</html>
