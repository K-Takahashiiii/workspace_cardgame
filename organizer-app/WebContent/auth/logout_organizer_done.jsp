<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主催者 ログアウト完了</title>
</head>
<body>

    <jsp:include page="/base/header.jsp" />

    <h1>主催者 ログアウト</h1>

    <p>ログアウトしました。</p>

    <p>
        <a href="<%= request.getContextPath() %>/menu">トップへ戻る</a>
    </p>


    <jsp:include page="/base/footer.jsp" />
</body>
</html>
