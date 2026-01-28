<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="bean.Tournament" %>
<%
  String ctx = request.getContextPath();
  String error = (String) request.getAttribute("error");

  Tournament t = (Tournament) request.getAttribute("tournament");
  Boolean entered = (Boolean) request.getAttribute("entered");
  boolean isEntered = (entered != null && entered.booleanValue());

  // 参加者名リスト（未実装なら null のままでOK）
  @SuppressWarnings("unchecked")
  List<String> participantNames = (List<String>) request.getAttribute("participantNames");
%>

<%!
  private String statusLabel(int status) {
    switch (status) {
      case 0: return "受付前";
      case 1: return "参加受付中";
      case 2: return "開催中";
      case 3: return "終了";
      default: return "不明";
    }
  }

  private String entryReqLabel(int req) {
    switch (req) {
      case 0: return "制限なし";
      case 1: return "条件1";
      case 2: return "条件2";
      case 3: return "条件3";
      default: return "不明";
    }
  }

  private String fmtDateTime(LocalDate d, LocalTime t) {
    if (d == null && t == null) return "";
    if (d != null && t != null) return d.toString() + " " + t.toString();
    if (d != null) return d.toString();
    return t.toString();
  }

  private String fmtDeadline(LocalDateTime dt) {
    if (dt == null) return "";
    DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    return dt.format(f);
  }
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>大会詳細</title>
<style>
body{
    background:#ffffff;
    font-family: Arial, sans-serif;
    margin:0;
    padding:0;
}
.container{
    max-width:900px;
    margin:30px auto;
    padding:20px;
}
h1{
    text-align:center;
}
.section{
    border:1px solid #ccc;
    padding:20px;
    margin-bottom:20px;
}
.row{
    display:flex;
    border-bottom:1px solid #eee;
    padding:8px 0;
}
.row div:first-child{
    width:200px;
    font-weight:bold;
}
.join-btn{
    width:100%;
    padding:15px;
    font-size:18px;
    cursor:pointer;
}
.status{
    text-align:center;
    margin-bottom:20px;
    font-size:16px;
}
</style>
</head>
<body>

<jsp:include page="/base/header.jsp" />

<main>
  <div class="container">

    <% if (error != null) { %>
      <p style="color:red;"><%= error %></p>
    <% } %>

    <% if (t != null) { %>

      <h1><%= t.getName() %></h1>

      <div class="status">
        現在の状態：<strong><%= statusLabel(t.getStatus()) %></strong>
      </div>

      <div class="section">
        <h2>大会情報</h2>
        <div class="row"><div>開催日</div><div><%= fmtDateTime(t.getEventDate(), t.getEventTime()) %></div></div>
        <div class="row"><div>会場</div><div><%= t.getVenue() %></div></div>
        <div class="row"><div>使用ゲーム</div><div><%= t.getGameTitle() %></div></div>
        <div class="row"><div>形式</div><div><%= t.getTournamentFormat() %></div></div>
        <div class="row"><div>定員</div><div><%= t.getParticipantCount() %> / <%= t.getMaxParticipants() %> 人</div></div>
        <div class="row"><div>参加費</div><div><%= t.getEntryFeeYen() %> 円</div></div>
        <div class="row"><div>登録締切</div><div><%= fmtDeadline(t.getRegistrationDeadline()) %></div></div>
        <div class="row"><div>参加条件</div><div><%= entryReqLabel(t.getEntryRequirement()) %></div></div>
      </div>

      <div class="section">
        <h2>試合ルール</h2>
        <div class="row"><div>試合形式</div><div><%= t.getMatchFormat() %></div></div>
        <div class="row"><div>制限時間</div><div><%= t.getTimeLimitMinutes() %> 分</div></div>
        <div class="row"><div>引き分け時</div><div><%= t.getDrawRule() %></div></div>
      </div>

      <div class="section">
        <h2>賞品</h2>
        <div class="row"><div>優勝</div><div><%= t.getPrizeFirst() %></div></div>
        <div class="row"><div>準優勝</div><div><%= t.getPrizeSecond() %></div></div>
        <div class="row"><div>3位</div><div><%= t.getPrizeThird() %></div></div>
      </div>

      <div class="section">
        <h2>現在の参加者</h2>

        <% if (participantNames == null || participantNames.isEmpty()) { %>
          <div class="row"><div>（参加者一覧は未取得）</div></div>
        <% } else { %>
          <% for (String pn : participantNames) { %>
            <div class="row"><div><%= pn %></div></div>
          <% } %>
        <% } %>
      </div>

      <% if (isEntered) { %>
        <button class="join-btn" disabled>エントリー済み</button>
      <% } else { %>
        <form action="<%= ctx %>/tournament/entry" method="post">
          <input type="hidden" name="tournamentId" value="<%= t.getTournamentId() %>">
          <button class="join-btn" type="submit">この大会に参加する</button>
        </form>
      <% } %>

      <p style="text-align:center; margin-top:20px;">
        <a href="<%= ctx %>/tournament/list">一覧へ戻る</a>
      </p>

    <% } %>

  </div>
</main>

<jsp:include page="/base/footer.jsp" />

</body>
</html>
