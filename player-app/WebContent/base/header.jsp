<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="bean.Player" %>
<%
  Player player = (Player) session.getAttribute("loginPlayer");
%>

<header>
  <h1>カードゲーム大会運営（プレイヤー）</h1>

  <% if (player != null) { %>
    <p>ログイン中：<%= player.getPlayerName() %> さん</p>
  <% } %>

  <hr>
</header>
