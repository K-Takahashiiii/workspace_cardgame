<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>主催者 新規登録</title>
</head>
<body>
<h1>主催者 新規登録</h1>

<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
    <p style="color:red"><%= error %></p>
<%
    }
%>

<form action="registerOrganizer" method="post">
    <div>
        <label>名前(name)：</label>
        <input type="text" name="name">
    </div>
    <div>
        <label>パスワード(pass)：</label>
        <input type="password" name="pass">
    </div>
    <div>
        <label>電話番号(tell_num)：</label>
        <input type="text" name="tell_num">
    </div>
    <div>
        <button type="submit">登録</button>
    </div>
</form>

</body>
</html>
