<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="bean.Organizer" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>主催者 ログイン成功</title>
</head>
<body>
<h1>主催者 ログイン成功</h1>

<%
    Organizer organizer = (Organizer) session.getAttribute("loginOrganizer");
    String name = null;
    if (organizer != null) {
        name = organizer.getName();
    }
%>

<p><strong><%= name != null ? name : "（不明）" %></strong> さん、ログインしました。</p>

<p>
    新規登録画面に戻る → <a href="auth_templates/registerOrganizer">主催者 新規登録</a><br>
    ログイン画面に戻る → <a href="auth_templates/loginOrganizer">主催者 ログイン</a><br>
    <form action="logoutOrganizer" method="get">
		<button type="submit">ログアウト</button>
	</form>
	<br><a href="menu">トップへ戻る</a>
</p>

</body>
</html>
