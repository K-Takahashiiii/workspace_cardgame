<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>主催者登録 完了</title>
</head>
<body>
<h1>主催者登録 完了</h1>

<p><strong><%= request.getAttribute("name") %></strong> さんの登録が完了しました。</p>

<p>
	<a href="registerOrganizer">もう一度登録する</a>
	<a href="index">トップへ戻る</a>

</p>


</body>
</html>
