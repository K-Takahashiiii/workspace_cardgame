<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>主催者 新規登録</title>
</head>
<body>

    <jsp:include page="/base/header.jsp" />

<h1>主催者 新規登録</h1>

<%
  String error = (String) request.getAttribute("error");
  if (error != null) {
%>
  <p style="color:red"><%= error %></p>
<%
  }
%>

<form action="<%= request.getContextPath() %>/registerOrganizer" method="post">
  <div>
    <label>店舗名(store_name)：</label>
    <input type="text" name="store_name">
  </div>
  <div>
    <label>代表者名(representative_name)：</label>
    <input type="text" name="representative_name">
  </div>
  <div>
    <label>パスワード(password)：</label>
    <input type="password" name="password">
  </div>
  <div>
    <label>名前(name)：</label>
    <input type="text" name="name">
  </div>
  <div>
    <button type="submit">登録</button>
  </div>
</form>



    <jsp:include page="/base/footer.jsp" />
</body>
</html>
