<!-- WebContent/index.jsp -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
  <body>

    <jsp:include page="/base/header.jsp" />

    <h2>Organizer App</h2>
    <a href="test.jsp">→ test.jsp を開く</a>


    <p><a href="auth/registerOrganizer">新規登録はこちらから</a></p>
    <p><a href="auth/loginOrganizer">ログインはこちらから</a></p>
  

    <jsp:include page="/base/footer.jsp" />
</body>
</html>
