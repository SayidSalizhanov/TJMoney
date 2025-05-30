<%@ include file="/templates/_header.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Xls</title>
    <link rel="stylesheet" href="/css/transactions/uploadXlsTransactions.css">
</head>
<body>

<div class="content">
    <h2>Загрузите Excel файл</h2>
    <form action="/transactions/new/uploadTransactions" method="post" enctype="multipart/form-data">
        <input type="hidden" name="groupId" value="<c:out value="${groupId}"/>">

        <label for="file">Выберите файл:</label>
        <input type="file" name="file" id="file" accept=".xls,.xlsx" required/>
        <br/><br/>
        <button type="submit">Загрузить</button>
        <c:if test="${not empty errorMessage}">
            <span style="color:red;"><c:out value="${errorMessage}"/></span><br>
        </c:if>
    </form>
</div>

</body>
</html>
