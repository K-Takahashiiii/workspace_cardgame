<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="bean.Organizer" %>
<%@ page import="bean.Tournament" %>

<%!
  private String statusLabel(int status) {
    switch (status) {
      case 0: return "受付前";
      case 1: return "受付中";
      case 2: return "開催中";
      case 3: return "終了";
      default: return "不明";
    }
  }
%>

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>大会開始（確認）</title>
</head>
<body>

<jsp:include page="/base/header.jsp" />

<h1>大会開始（確認）</h1>

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

  if (error != null) {
%>
    <p style="color:red;"><%= error %></p>
<%
  }

  if (t == null) {
%>
  <p style="color:red;">大会データがありません。</p>
  <p><a href="<%= request.getContextPath() %>/tournament/list">大会一覧へ</a></p>
<%
    return;
  }

  DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
  DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");

  String dateStr = (t.getEventDate() == null) ? "-" : t.getEventDate().format(df);
  String timeStr = (t.getEventTime() == null) ? "-" : t.getEventTime().format(tf);
%>

<h2>大会情報</h2>
<table border="1" cellpadding="6" cellspacing="0">
  <tr><th>ID</th><td><%= t.getTournamentId() %></td></tr>
  <tr><th>大会名</th><td><%= t.getName() %></td></tr>
  <tr><th>開催日</th><td><%= dateStr %></td></tr>
  <tr><th>開始時刻</th><td><%= timeStr %></td></tr>
  <tr><th>参加者数</th><td><%= t.getParticipantCount() %> / <%= t.getMaxParticipants() %></td></tr>
  <tr><th>状態</th><td><%= statusLabel(t.getStatus()) %></td></tr>
</table>

<h2>詳細</h2>
<table border="1" cellpadding="6" cellspacing="0">
  <tr><th>会場</th><td><%= (t.getVenue() == null || t.getVenue().isEmpty()) ? "-" : t.getVenue() %></td></tr>
  <tr><th>使用ゲーム</th><td><%= (t.getGameTitle() == null || t.getGameTitle().isEmpty()) ? "-" : t.getGameTitle() %></td></tr>
  <tr><th>形式</th><td><%= (t.getTournamentFormat() == null || t.getTournamentFormat().isEmpty()) ? "-" : t.getTournamentFormat() %></td></tr>
  <tr><th>大会概要</th><td><pre style="margin:0; white-space:pre-wrap;"><%= (t.getDescription() == null) ? "" : t.getDescription() %></pre></td></tr>
</table>

<hr>

<p>
  下の「大会を開始する」を押すと、参加者向けのアクセス用QRコードを<strong>別タブ</strong>で表示します。
</p>

<form method="get" action="<%= request.getContextPath() %>/tournament/qr" target="_blank">
  <input type="hidden" name="tournamentId" value="<%= t.getTournamentId() %>">
  <button type="submit">大会を開始する</button>
</form>

<p>
  <a href="<%= request.getContextPath() %>/tournament/list">大会一覧へ戻る</a>
</p>

<jsp:include page="/base/footer.jsp" />
</body>
</html>
