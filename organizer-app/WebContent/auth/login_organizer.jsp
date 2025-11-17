<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>主催者 ログイン</title>
</head>
<body>
<h1>主催者 ログイン</h1>

<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
    <p style="color:red"><%= error %></p>
<%
    }
%>

<form action="loginOrganizer" method="post">
    <div>
        <label>名前(name)：</label>
        <input type="text" name="name">
    </div>
    <div>
        <label>パスワード(pass)：</label>
        <input type="password" name="pass">
    </div>
    <div>
        <button type="submit">ログイン</button>
    </div>
</form>

<p>
    新規登録は <a href="registerOrganizer">こちら</a>
    <a href="index">トップへ戻る</a>
</p>

</body>
</html>
