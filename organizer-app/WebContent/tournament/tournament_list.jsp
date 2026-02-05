<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
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
  <title>大会一覧</title>
</head>
<body>

  <jsp:include page="/base/header.jsp" />

  <h1>大会一覧</h1>

  <%
    String error = (String) request.getAttribute("error");
    if (error != null) {
  %>
      <p style="color:red"><%= error %></p>
  <%
    }

    @SuppressWarnings("unchecked")
    List<Tournament> tournaments = (List<Tournament>) request.getAttribute("tournaments");

    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");

    String ctx = request.getContextPath();
  %>

  <p>
    <a href="<%= ctx %>/registerTournament">+ 大会登録</a>
    &nbsp;|&nbsp;
    <a href="<%= ctx %>/menu">メニューへ戻る</a>
  </p>

  <%
    if (tournaments == null || tournaments.isEmpty()) {
  %>
      <p>大会がありません。</p>
  <%
    } else {
  %>
      <table border="1" cellpadding="6" cellspacing="0">
        <tr>
          <th>ID</th>
          <th>大会名</th>
          <th>開催日</th>
          <th>開始時刻</th>
          <th>参加</th>
          <th>状態</th>
          <th>設定</th>
        </tr>

        <%
          for (Tournament t : tournaments) {
            String dateStr = (t.getEventDate() == null) ? "-" : t.getEventDate().format(df);
            String timeStr = (t.getEventTime() == null) ? "-" : t.getEventTime().format(tf);
        %>
            <tr>
              <td><%= t.getTournamentId() %></td>
              <td><%= t.getName() %></td>
              <td><%= dateStr %></td>
              <td><%= timeStr %></td>
              <td><%= t.getParticipantCount() %> / <%= t.getMaxParticipants() %></td>
              <td><%= statusLabel(t.getStatus()) %></td>
              <td>
                <a href="<%= ctx %>/optioningTournament?tournamentId=<%= t.getTournamentId() %>">詳細設定</a>
                &nbsp;|&nbsp;
                <a href="<%= ctx %>/tournament/start?tournamentId=<%= t.getTournamentId() %>">大会開始</a>
              </td>
            </tr>
        <%
          }
        %>
      </table>
  <%
    }
  %>

  <jsp:include page="/base/footer.jsp" />

</body>
</html>
