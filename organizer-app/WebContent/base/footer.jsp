<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="bean.Organizer" %>
<%
  String ctx = request.getContextPath();
  Organizer organizer = (Organizer) session.getAttribute("loginOrganizer");
%>

<hr>

<footer>
  <a href="<%= ctx %>/menu">メニュー</a>

  &nbsp;|&nbsp;
  <a href="<%= ctx %>/tournament/list">大会一覧</a>

  &nbsp;|&nbsp;
  <a href="<%= ctx %>/registerTournament">大会登録</a>

  <% if (organizer != null) { %>
    &nbsp;|&nbsp;
    <a href="<%= ctx %>/logoutOrganizer">ログアウト</a>
  <% } else { %>
    &nbsp;|&nbsp;
    <a href="<%= ctx %>/loginOrganizer">ログイン</a>
  <% } %>
</footer>
