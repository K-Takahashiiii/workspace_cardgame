<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="bean.Organizer" %>
<%
  Organizer organizer = (Organizer) session.getAttribute("loginOrganizer");
%>

<header>
  <h1>カードゲーム大会運営（主催者）</h1>

  <% if (organizer != null) { %>
    <p>ログイン中：<%= organizer.getName() %> さん</p>
  <% } %>

  <hr>
</header>
