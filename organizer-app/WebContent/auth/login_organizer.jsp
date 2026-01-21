<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>主催者 ログイン</title>
</head>
<body>

    <jsp:include page="/base/header.jsp" />

<h1>主催者 ログイン</h1>

<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
    <p style="color:red"><%= error %></p>
<%
    }
%>

<form action="<%= request.getContextPath() %>/loginOrganizer" method="post">
    <div>
        <label>管理番号(management_num)：</label>
        <input type="text" name="management_num">
    </div>

    <div>
        <label>パスワード(password)：</label>
        <input type="password" name="password">
    </div>

    <div>
        <button type="submit">ログイン</button>
    </div>
</form>

<p>
    <a href="<%= request.getContextPath() %>/registerOrganizer">新規登録はこちら</a>
</p>



    <jsp:include page="/base/footer.jsp" />
</body>
</html>
