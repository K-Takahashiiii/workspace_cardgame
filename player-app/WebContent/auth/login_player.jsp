<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String ctx = request.getContextPath();
  String error = (String) request.getAttribute("error");

  // エラー時に入力を残す（パスワードは残さない）
  String telephoneNum = request.getParameter("telephone_num");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>ログイン（プレイヤー）</title>
</head>
<body>

<jsp:include page="/base/header.jsp" />

<main>
  <h1>ログイン</h1>

  <% if (error != null) { %>
    <p style="color:red;"><%= error %></p>
  <% } %>

  <form action="<%= ctx %>/loginPlayer" method="post">
    <div>
      <label>電話番号</label><br>
      <input type="text" name="telephone_num" maxlength="15"
             value="<%= telephoneNum != null ? telephoneNum : "" %>">
    </div>

    <div>
      <label>パスワード</label><br>
      <input type="password" name="password" maxlength="255" value="">
    </div>

    <div style="margin-top: 12px;">
      <button type="submit">ログイン</button>
    </div>
  </form>

  <hr>

  <p>
    はじめての方は
    <a href="<%= ctx %>/registerPlayer">新規登録</a>
  </p>
</main>

<jsp:include page="/base/footer.jsp" />

</body>
</html>
