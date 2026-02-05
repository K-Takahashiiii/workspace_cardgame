<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="bean.Organizer" %>
<%@ page import="bean.Tournament" %>

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>大会QRコード</title>
</head>
<body>

<jsp:include page="/base/header.jsp" />

<h1>大会QRコード</h1>

<%
  Organizer organizer = (Organizer) session.getAttribute("loginOrganizer");
  if (organizer == null) {
%>
  <p>ログインしてください。</p>
  <p><a href="<%= request.getContextPath() %>/loginOrganizer">ログイン</a></p>
<%
    return;
  }

  String error = (String) request.getAttribute("error");
  Tournament t = (Tournament) request.getAttribute("tournament");
  String joinUrl = (String) request.getAttribute("joinUrl");
  String qrImageUrl = (String) request.getAttribute("qrImageUrl");

  if (error != null) {
%>
    <p style="color:red;"><%= error %></p>
<%
  }

  if (t == null || joinUrl == null || qrImageUrl == null) {
%>
  <p style="color:red;">QRコードの生成に失敗しました。</p>
  <p><a href="<%= request.getContextPath() %>/tournament/list">大会一覧へ</a></p>
<%
    return;
  }
%>

<h2><%= t.getName() %>（ID: <%= t.getTournamentId() %>）</h2>

<p>
  このQRコードを参加者に配布してください。スキャンすると、参加者側のブラケット表示ページへ移動します。
</p>

<p>
  <img src="<%= qrImageUrl %>" alt="QRコード" />
</p>

<p>
  直接URL：
  <a href="<%= joinUrl %>" target="_blank" rel="noopener noreferrer"><%= joinUrl %></a>
</p>

<p>
  <a href="<%= request.getContextPath() %>/tournament/list">大会一覧へ戻る</a>
</p>

<jsp:include page="/base/footer.jsp" />

</body>
</html>
