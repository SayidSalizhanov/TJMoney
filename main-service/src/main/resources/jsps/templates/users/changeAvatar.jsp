<%@ include file="/templates/_header.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Смена аватара</title>
    <link rel="stylesheet" href="/css/user/changeAvatar.css">
</head>
<body class="content-body">

<div class="content-wrapper">
    <div class="content">
        <h2>Смена аватара</h2>
        <div class="avatar-preview mb-3">
            <img src="<c:out value="${urlPhoto}" />" alt="Avatar" class="rounded-circle" width="250" height="250">
        </div>

        <form action="/user/changeAvatar" method="post" enctype="multipart/form-data" class="avatar-form mb-4">
            <div class="form-group">
                <label for="avatarFile">Сменить аватар</label>
                <input type="file" name="file" id="avatarFile" class="form-control-file" accept="image/*" required>
            </div>
            <button type="submit" class="btn-success">Загрузить аватар</button>
        </form>

        <form action="/user/changeAvatar" method="post">
            <input type="hidden" name="_method" value="DELETE">
            <button type="submit" class="btn-success delete-button">Удалить аватар</button>
        </form>
    </div>
</div>

</body>
</html>
