<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>メニュー</title>
</head>
<body>

    <jsp:include page="/base/header.jsp" />

    <h2>メニュー</h2>

    <p>
        <a href="<%= request.getContextPath() %>/registerTournament">大会登録（新規作成）</a>
        &nbsp;|&nbsp;
        <a href="<%= request.getContextPath() %>/tournament/list">大会一覧</a>
    </p>

    <!-- 必要に応じてリンクを追加 -->

    <jsp:include page="/base/footer.jsp" />

</body>
</html>
