<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="bean.Match" %>

<%
  String ctx = request.getContextPath();
  String error = (String) request.getAttribute("error");
  Integer tournamentId = (Integer) request.getAttribute("tournamentId");

  @SuppressWarnings("unchecked")
  List<List<Match>> rounds = (List<List<Match>>) request.getAttribute("rounds");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>トーナメント表</title>
<style>
.container{ max-width:1100px; margin:20px auto; padding:20px; }
.cols{ display:flex; gap:16px; align-items:flex-start; overflow-x:auto; }
.col{ min-width:260px; flex:1; border:1px solid #ddd; padding:10px; border-radius:10px; background:#fafafa; }
.cardLink{ display:block; text-decoration:none; color:inherit; }
.matchBox{ border:1px solid #eee; background:#fff; padding:10px; border-radius:10px; margin-bottom:10px; }
.matchBox:hover{ border-color:#bbb; }
.name{ padding:6px 8px; border-radius:8px; margin:4px 0; }
.win{ background:#e7fff0; font-weight:bold; }
.small{ color:#666; font-size:12px; }
.badge{ display:inline-block; font-size:12px; padding:2px 8px; border-radius:999px; background:#f0f0f0; margin-left:6px;}
</style>
</head>
<body>

<jsp:include page="/base/header.jsp" />

<div class="container">

  <% if (error != null) { %>
    <p style="color:red;"><%= error %></p>
  <% } %>

  <h1>トーナメント表</h1>
  <% if (tournamentId != null) { %>
    <p class="small">tournamentId = <%= tournamentId %></p>
  <% } %>

  <div class="cols">
    <% if (rounds != null) { %>
      <% for (int r = 0; r < rounds.size(); r++) { %>
        <div class="col">
          <h2>Round <%= (r+1) %></h2>

          <% for (Match m : rounds.get(r)) { %>

            <%
              String p1n = (m.getPlayer1Name() != null) ? m.getPlayer1Name() : "（未確定）";
              String p2n = (m.getPlayer2Name() != null) ? m.getPlayer2Name() : "（未確定）";

              Integer w = m.getWinnerId();
              boolean p1win = (w != null && m.getPlayer1Id() != null && w.intValue() == m.getPlayer1Id().intValue());
              boolean p2win = (w != null && m.getPlayer2Id() != null && w.intValue() == m.getPlayer2Id().intValue());

              String href = ctx + "/match/input?tournamentId=" + m.getTournamentId() + "&matchId=" + m.getMatchId();
              boolean ready = (m.getPlayer1Id() != null && m.getPlayer2Id() != null);
            %>

            <a class="cardLink" href="<%= href %>">
              <div class="matchBox">
                <div class="small">
                  第<%= (m.getMatchIndex() + 1) %>試合
                  <% if (w != null) { %><span class="badge">確定</span><% } %>
                  <% if (w == null && ready) { %><span class="badge">入力</span><% } %>
                </div>

                <div class="name <%= p1win ? "win" : "" %>"><%= p1n %></div>
                <div class="name <%= p2win ? "win" : "" %>"><%= p2n %></div>

                <div class="small">
                  <% if (!ready) { %>
                    対戦者待ち
                  <% } else if (w == null) { %>
                    タップして勝者入力
                  <% } else { %>
                    タップして詳細確認
                  <% } %>
                </div>
              </div>
            </a>

          <% } %>
        </div>
      <% } %>
    <% } else { %>
      <p>rounds が空です（Servletから rounds を渡してください）</p>
    <% } %>
  </div>

  <% if (tournamentId != null) { %>
    <p style="margin-top:16px;">
      <a href="<%= ctx %>/match/bracket?tournamentId=<%= tournamentId %>">再読み込み</a>
    </p>
  <% } %>

</div>

<jsp:include page="/base/footer.jsp" />

</body>
</html>
