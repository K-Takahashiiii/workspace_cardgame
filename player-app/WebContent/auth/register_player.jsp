<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String ctx = request.getContextPath();
  String error = (String) request.getAttribute("error");

  String telephoneNum = request.getParameter("telephone_num");
  String name = request.getParameter("name");
  String playerName = request.getParameter("player_name");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>プレイヤー新規登録</title>
</head>
<body>

<jsp:include page="/base/header.jsp" />

<main>
  <h1>プレイヤー新規登録</h1>

  <% if (error != null) { %>
    <p style="color:red;"><%= error %></p>
  <% } %>

  <form action="<%= ctx %>/registerPlayer" method="post">
    <div>
      <label>電話番号（ハイフンなし）</label><br>
      <input type="text" name="telephone_num" maxlength="15"
             value="<%= telephoneNum != null ? telephoneNum : "" %>">
    </div>

    <div>
      <label>名前</label><br>
      <input type="text" name="name" maxlength="50"
             value="<%= name != null ? name : "" %>">
    </div>

    <div>
      <label>パスワード</label><br>
      <input type="password" name="password" maxlength="255" value="">
    </div>

    <div>
      <label>プレイヤーネーム</label><br>
      <input type="text" name="player_name" maxlength="50"
             value="<%= playerName != null ? playerName : "" %>">
    </div>

    <div style="margin-top: 12px;">
      <button type="submit">登録</button>
    </div>
  </form>

  <hr>

  <p>
    すでにアカウントをお持ちですか？
    <a href="<%= ctx %>/loginPlayer">ログイン</a>
  </p>
</main>

<jsp:include page="/base/footer.jsp" />

</body>
</html>
