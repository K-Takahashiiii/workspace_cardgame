<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String ctx = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>ログアウト完了</title>
</head>
<body>

<jsp:include page="/base/header.jsp" />

<main>
  <h1>ログアウトしました</h1>

  <p>ご利用ありがとうございました。</p>

  <hr>

  <p>
    <a href="<%= ctx %>/loginPlayer">ログイン画面へ</a>
  </p>
</main>

<jsp:include page="/base/footer.jsp" />

</body>
</html>
