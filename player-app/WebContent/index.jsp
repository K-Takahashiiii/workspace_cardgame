<!-- WebContent/index.jsp -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
  <body>

    <jsp:include page="/base/header.jsp" />

    <h2>player App</h2>
    <a href="test.jsp">→ test.jsp を開く</a>


    <p><a href="auth/registerPlayer">新規登録はこちらから</a></p>
    <p><a href="auth/loginPlayer">ログインはこちらから</a></p>


    <jsp:include page="/base/footer.jsp" />
</body>
</html>
