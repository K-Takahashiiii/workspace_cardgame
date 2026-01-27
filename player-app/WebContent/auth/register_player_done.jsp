<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String ctx = request.getContextPath();

  // RegisterPlayerServlet から渡される想定
  Integer userNum = (Integer) request.getAttribute("user_num");
  String name = (String) request.getAttribute("name");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>登録完了</title>
</head>
<body>

<jsp:include page="/base/header.jsp" />

<main>
  <h1>登録完了</h1>

  <p>
    <%= (name != null ? name : "ユーザー") %> さん、登録が完了しました。
  </p>

  <% if (userNum != null) { %>
    <p>ユーザー番号：<strong><%= userNum %></strong></p>
  <% } %>

  <hr>

  <p>
    <a href="<%= ctx %>/loginPlayer">ログインへ</a>
  </p>
</main>

<jsp:include page="/base/footer.jsp" />

</body>
</html>
