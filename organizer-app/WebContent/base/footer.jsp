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
  <% } %>

  <!-- ここに必要に応じてリンクを追加 -->
  <!-- 例：&nbsp;|&nbsp;<a href="<%= ctx %>/registerTournament">大会登録</a> -->
</footer>
