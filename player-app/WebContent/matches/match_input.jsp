<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="bean.Match" %>

<%
  String ctx = request.getContextPath();
  String error = (String) request.getAttribute("error");
  Match m = (Match) request.getAttribute("match");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>勝者入力</title>
<style>
.container{ max-width:640px; margin:20px auto; padding:20px; }
.card{ border:1px solid #ddd; border-radius:12px; padding:16px; background:#fff; }
.row{ margin:12px 0; }
button{ padding:10px 14px; cursor:pointer; }
.small{ color:#666; font-size:12px; }
</style>
</head>
<body>

<jsp:include page="/base/header.jsp" />

<div class="container">
  <h1>勝者入力</h1>

  <% if (error != null) { %>
    <p style="color:red;"><%= error %></p>
  <% } %>

  <% if (m == null) { %>
    <p>試合情報がありません。</p>
    <p><a href="<%= ctx %>/tournament/list">大会一覧へ</a></p>

  <% } else { %>

    <div class="card">
      <div class="small">Round <%= m.getRoundNo() %> / 第<%= (m.getMatchIndex() + 1) %>試合</div>

      <div class="row">P1：<strong><%= (m.getPlayer1Name() != null ? m.getPlayer1Name() : "（未確定）") %></strong></div>
      <div class="row">P2：<strong><%= (m.getPlayer2Name() != null ? m.getPlayer2Name() : "（未確定）") %></strong></div>

      <% if (m.getPlayer1Id() == null || m.getPlayer2Id() == null) { %>
        <p class="small">まだ対戦者が揃っていません。</p>

      <% } else if (m.getWinnerId() != null) { %>
        <p>この試合は確定済み：<strong><%= m.getWinnerName() %></strong></p>
        <% if (m.getDecidedAt() != null) { %>
          <p class="small">確定時刻：<%= m.getDecidedAt() %></p>
        <% } %>

      <% } else { %>
        <form action="<%= ctx %>/match/reportWinner" method="post">
          <input type="hidden" name="tournamentId" value="<%= m.getTournamentId() %>">
          <input type="hidden" name="matchId" value="<%= m.getMatchId() %>">

          <div class="row">
            <label>
              <input type="radio" name="winnerId" value="<%= m.getPlayer1Id() %>" required>
              <%= m.getPlayer1Name() %>
            </label>
          </div>

          <div class="row">
            <label>
              <input type="radio" name="winnerId" value="<%= m.getPlayer2Id() %>" required>
              <%= m.getPlayer2Name() %>
            </label>
          </div>

          <div class="row">
            <button type="submit">確定</button>
          </div>
        </form>

        <p class="small">※ 当事者（P1/P2）以外が確定しようとすると無効になります。</p>
      <% } %>

      <p class="row">
        <a href="<%= ctx %>/match/bracket?tournamentId=<%= m.getTournamentId() %>">ブラケットへ戻る</a>
      </p>
    </div>

  <% } %>
</div>

<jsp:include page="/base/footer.jsp" />

</body>
</html>
