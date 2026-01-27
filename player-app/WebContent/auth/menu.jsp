<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String ctx = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>メニュー（プレイヤー）</title>
</head>
<body>

<jsp:include page="/base/header.jsp" />

<main>
  <h1>メニュー</h1>

  <ul>
    <li><a href="<%= ctx %>/tournament/list">大会一覧</a></li>
    <li><a href="<%= ctx %>/logoutPlayer">ログアウト</a></li>
  </ul>
</main>

<jsp:include page="/base/footer.jsp" />

</body>
</html>
