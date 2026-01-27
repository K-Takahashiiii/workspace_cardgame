<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="bean.Player" %>
<%
  String ctx = request.getContextPath();
  Player player = (Player) session.getAttribute("loginPlayer");

  // 直アクセス対策：未ログインならログインへ
  if (player == null) {
    response.sendRedirect(ctx + "/loginPlayer");
    return;
  }
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>ログイン成功</title>
</head>
<body>

<jsp:include page="/base/header.jsp" />

<main>
  <h1>ログイン成功</h1>

  <p>
    ようこそ、<strong><%= player.getPlayerName() %></strong> さん
  </p>

  <hr>

  <p>
    <a href="<%= ctx %>/menu">メニューへ</a>
  </p>
</main>

<jsp:include page="/base/footer.jsp" />

</body>
</html>
