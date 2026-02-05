<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="bean.Player" %>
<%
  String ctx = request.getContextPath();
  Player player = (Player) session.getAttribute("loginPlayer");
%>

<hr>

<footer>
  <a href="<%= ctx %>/menu">メニュー</a>

  &nbsp;|&nbsp;
  <a href="<%= ctx %>/tournament/list">大会一覧</a>

  <% if (player != null) { %>
    &nbsp;|&nbsp;
    <a href="<%= ctx %>/logoutPlayer">ログアウト</a>
  <% } else { %>
    &nbsp;|&nbsp;
    <a href="<%= ctx %>/loginPlayer">ログイン</a>
    &nbsp;|&nbsp;
    <a href="<%= ctx %>/registerPlayer">新規登録</a>
  <% } %>

</footer>
