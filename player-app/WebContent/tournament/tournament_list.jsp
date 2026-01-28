<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="bean.Tournament" %>

<%!
  private String labelFor(int req) {
    switch (req) {
      case 0: return "制限なし";
      case 1: return "条件1";
      case 2: return "条件2";
      case 3: return "条件3";
      default: return "不明";
    }
  }
%>

<%
  String ctx = request.getContextPath();
  String error = (String) request.getAttribute("error");
  @SuppressWarnings("unchecked")
  List<Tournament> tournaments = (List<Tournament>) request.getAttribute("tournaments");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>大会一覧</title>
</head>
<body>

<jsp:include page="/base/header.jsp" />

<main>
  <h1>大会一覧</h1>

  <% if (error != null) { %>
    <p style="color:red;"><%= error %></p>
  <% } %>

  <% if (tournaments == null || tournaments.isEmpty()) { %>
    <p>大会がありません。</p>
  <% } else { %>
    <table border="1" cellpadding="6" cellspacing="0">
      <tr>
        <th>ID</th>
        <th>大会名</th>
        <th>会場</th>
        <th>開催日</th>
        <th>締切</th>
        <th>参加</th>
        <th>参加条件</th>
        <th>詳細</th>
      </tr>

      <% for (Tournament t : tournaments) { %>
        <tr>
          <td><%= t.getTournamentId() %></td>
          <td><%= t.getName() %></td>
          <td><%= t.getVenue() %></td>
          <td><%= (t.getEventDate() != null ? t.getEventDate().toString() : "") %></td>
          <td><%= (t.getRegistrationDeadline() != null ? t.getRegistrationDeadline().toString() : "") %></td>
          <td><%= t.getParticipantCount() %> / <%= t.getMaxParticipants() %></td>
          <td><%= labelFor(t.getEntryRequirement()) %></td>
          <td>
            <a href="<%= ctx %>/tournament/detail?tournamentId=<%= t.getTournamentId() %>">詳細</a>
          </td>
        </tr>
      <% } %>
    </table>
  <% } %>

  <hr>
  <p><a href="<%= ctx %>/menu">メニューへ</a></p>
</main>

<jsp:include page="/base/footer.jsp" />

</body>
</html>
