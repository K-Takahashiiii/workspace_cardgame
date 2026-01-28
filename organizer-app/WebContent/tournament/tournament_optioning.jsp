<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="bean.Organizer" %>
<%@ page import="bean.Tournament" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>大会設定</title>
</head>
<body>

<jsp:include page="/base/header.jsp" />

<h1>大会設定</h1>

<%
  // ログインチェック（念のため。基本はServletで弾いてる想定）
  Organizer organizer = (Organizer) session.getAttribute("loginOrganizer");
  if (organizer == null) {
%>
  <p>ログインしてください。</p>
<%
    return;
  }

  String error = (String) request.getAttribute("error");
  Tournament t = (Tournament) request.getAttribute("tournament");
  if (t == null) {
%>
  <p style="color:red;">大会データがありません。</p>
  <p><a href="<%= request.getContextPath() %>/tournament/list">大会一覧へ</a></p>
<%
    return;
  }

  // datetime-local 用：LocalDateTime -> "yyyy-MM-ddTHH:mm"
  String registrationDeadline = "";
  if (t.getRegistrationDeadline() != null) {
    String s = t.getRegistrationDeadline().toString(); // 例: 2026-01-31T23:59:00
    registrationDeadline = (s.length() >= 16) ? s.substring(0, 16) : s;
  }

  // 参加条件コード（0〜3）
  int entryRequirement = t.getEntryRequirement();
%>

<% if (error != null) { %>
  <p style="color:red;"><%= error %></p>
<% } %>

<form method="post" action="<%= request.getContextPath() %>/optioningTournament">
  <input type="hidden" name="tournamentId" value="<%= t.getTournamentId() %>">

  <h2>基本</h2>

  <p>
    大会名：<br>
    <input type="text" name="name" size="40" required value="<%= t.getName() == null ? "" : t.getName() %>">
  </p>

  <p>
    開催日：<br>
    <input type="date" name="eventDate" required value="<%= t.getEventDate() == null ? "" : t.getEventDate().toString() %>">
  </p>

  <p>
    開催時刻：<br>
    <input type="time" name="eventTime" value="<%= t.getEventTime() == null ? "" : t.getEventTime().toString() %>">
  </p>

  <p>
    最大参加人数：<br>
    <input type="number" name="maxParticipants" min="1" required value="<%= t.getMaxParticipants() %>">
  </p>

  <p>
    現在参加者数：<br>
    <strong><%= t.getParticipantCount() %></strong> / <%= t.getMaxParticipants() %>
  </p>

  <p>
    大会概要：<br>
    <textarea name="description" rows="5" cols="60"><%= t.getDescription() == null ? "" : t.getDescription() %></textarea>
  </p>

  <p>
    状態：<br>
    <select name="status">
      <option value="0" <%= (t.getStatus()==0) ? "selected" : "" %>>受付前</option>
      <option value="1" <%= (t.getStatus()==1) ? "selected" : "" %>>受付中</option>
      <option value="2" <%= (t.getStatus()==2) ? "selected" : "" %>>開催中</option>
      <option value="3" <%= (t.getStatus()==3) ? "selected" : "" %>>終了</option>
    </select>
  </p>

  <hr>

  <h2>大会詳細</h2>

  <p>
    会場：<br>
    <input type="text" name="venue" size="40" value="<%= t.getVenue() == null ? "" : t.getVenue() %>">
  </p>

  <p>
    使用ゲーム：<br>
    <input type="text" name="gameTitle" size="40" value="<%= t.getGameTitle() == null ? "" : t.getGameTitle() %>">
  </p>

  <p>
    形式：<br>
    <input type="text" name="tournamentFormat" size="40" value="<%= t.getTournamentFormat() == null ? "" : t.getTournamentFormat() %>">
  </p>

  <p>
    参加費(円)：<br>
    <input type="number" name="entryFeeYen" min="0" step="1" value="<%= t.getEntryFeeYen() %>">
  </p>

  <p>
    登録締め切り：<br>
    <input type="datetime-local" name="registrationDeadline" value="<%= registrationDeadline %>">
  </p>

  <p>
    試合形式：<br>
    <input type="text" name="matchFormat" size="40" value="<%= t.getMatchFormat() == null ? "" : t.getMatchFormat() %>">
  </p>

  <p>
    制限時間(分)：<br>
    <input type="number" name="timeLimitMinutes" min="0" step="1" value="<%= t.getTimeLimitMinutes() %>">
  </p>

  <p>
    引き分け時：<br>
    <input type="text" name="drawRule" size="60" value="<%= t.getDrawRule() == null ? "" : t.getDrawRule() %>">
  </p>

  <p>
    優勝賞品：<br>
    <input type="text" name="prizeFirst" size="60" value="<%= t.getPrizeFirst() == null ? "" : t.getPrizeFirst() %>">
  </p>

  <p>
    準優勝賞品：<br>
    <input type="text" name="prizeSecond" size="60" value="<%= t.getPrizeSecond() == null ? "" : t.getPrizeSecond() %>">
  </p>

  <p>
    3位賞品：<br>
    <input type="text" name="prizeThird" size="60" value="<%= t.getPrizeThird() == null ? "" : t.getPrizeThird() %>">
  </p>

  <hr>

  <h2>参加条件</h2>
  <p>
    参加条件コード：<br>
    <input type="number" name="entryRequirement" min="0" max="3" step="1" value="<%= entryRequirement %>">
    <br>
    <small>
      0：参加条件なし<br>
      1：いままで３位以上になったことがある<br>
      2：対戦での総勝利数１０回以上<br>
      3：対戦での総勝利数３回以上
    </small>
  </p>

  <p>
    <button type="submit">更新する</button>
  </p>
</form>

<p>
  <a href="<%= request.getContextPath() %>/tournament/list">大会一覧へ戻る</a>
</p>

<jsp:include page="/base/footer.jsp" />
</body>
</html>
