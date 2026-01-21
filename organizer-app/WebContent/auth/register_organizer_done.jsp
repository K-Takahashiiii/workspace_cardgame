<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>主催者 登録完了</title>
</head>
<body>

    <jsp:include page="/base/header.jsp" />

<h1>主催者 登録完了</h1>

<%
  Integer managementNum = (Integer) request.getAttribute("management_num");
  String name = (String) request.getAttribute("name");
%>

<p><strong><%= name %></strong> さんの登録が完了しました。</p>
<p>あなたの管理番号（ログインID）は：<strong><%= managementNum %></strong></p>

<p>
  <a href="<%= request.getContextPath() %>/registerOrganizer">もう一度登録する</a><br>
  <a href="<%= request.getContextPath() %>/loginOrganizer">ログインへ</a>
</p>



    <jsp:include page="/base/footer.jsp" />
</body>
</html>
