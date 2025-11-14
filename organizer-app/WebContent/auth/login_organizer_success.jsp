<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>主催者 ログイン成功</title>
</head>
<body>
<h1>主催者 ログイン成功</h1>

<%
    String name = (String) session.getAttribute("loginOrganizerName");
%>

<p><strong><%= name != null ? name : "（不明）" %></strong> さん、ログインしました。</p>

<p>
    ログアウトは（まだ作ってないけど）後で追加するとして、<br>
    とりあえずこの画面が出れば「ログイン成功」です。
</p>

<p>
    新規登録画面に戻る → <a href="registerOrganizer">主催者 新規登録</a><br>
    ログイン画面に戻る → <a href="loginOrganizer">主催者 ログイン</a>
</p>

</body>
</html>
