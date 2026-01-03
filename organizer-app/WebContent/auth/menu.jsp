<!-- WebContent/index.jsp -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<body>
	<%@ page contentType="text/html; charset=UTF-8" language="java"%>
	<%@ page import="bean.Organizer"%>

	<%
		Organizer loginOrganizer = (Organizer) session.getAttribute("loginOrganizer");
	%>

	<%
		if (loginOrganizer != null) {
	%>
	<div style="padding: 8px; border: 1px solid #ccc; margin-bottom: 12px;">
		ログイン中： 管理番号：<%=loginOrganizer.getManagementNum()%>
		／ 名前：<%=loginOrganizer.getName()%>
	</div>
	<%
		} else {
	%>
	<div style="padding: 8px; border: 1px solid #f99; margin-bottom: 12px;">
		未ログインです（ログインしてください）</div>
	<%
		}
	%>
	<h2>Organizer App</h2>

	<p>
		<a href="<%=request.getContextPath()%>/registerOrganizer">新規登録はこちらから</a>
	</p>
	<p>
		<a href="<%=request.getContextPath()%>/loginOrganizer">ログインはこちらから</a>
	</p>

	<form action="logoutOrganizer" method="get">
		<button type="submit">ログアウト</button>
	</form>



	<p>
		<a href="<%=request.getContextPath()%>/registerTournament">大会登録（新規作成）</a>
	</p>







</body>
</html>
