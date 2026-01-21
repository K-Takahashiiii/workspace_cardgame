<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="bean.Organizer" %>
<%
  String ctx = request.getContextPath();
  Organizer organizer = (Organizer) session.getAttribute("loginOrganizer");
%>

<hr>

<footer>
  <a href="<%= ctx %>/menu">メニュー</a>

  <% if (organizer != null) { %>
    &nbsp;|&nbsp;
    <a href="<%= ctx %>/logoutOrganizer">ログアウト</a>
  <% } else { %>
    &nbsp;|&nbsp;
    <a href="<%= ctx %>/loginOrganizer">ログイン</a>
  <% } %>

  <!-- ここに必要に応じてリンクを追加 -->
  <a href="<%= request.getContextPath() %>/tournament/list">大会一覧</a>

</footer>
