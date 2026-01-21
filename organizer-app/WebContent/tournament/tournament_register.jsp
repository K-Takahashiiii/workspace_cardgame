<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="bean.Organizer" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>大会登録</title>
</head>
<body>

<jsp:include page="/base/header.jsp" />

<h1>大会登録</h1>

<%
    Organizer organizer = (Organizer) session.getAttribute("loginOrganizer");
    if (organizer == null) {
%>
    <p>ログインしてください。</p>
<%
        return;
    }

    // エラーメッセージ
    String error = (String) request.getAttribute("error");

    // 入力保持（ServletがsetAttributeしてきた値があればそれを優先）
    // 無ければ「テスト用初期値」を入れておく
    String name = (String) request.getAttribute("name");
    if (name == null) name = "テスト大会";

    String eventDate = (String) request.getAttribute("eventDate");
    if (eventDate == null) eventDate = "2026-02-01";

    String eventTime = (String) request.getAttribute("eventTime");
    if (eventTime == null) eventTime = "13:00";

    String maxParticipants = (String) request.getAttribute("maxParticipants");
    if (maxParticipants == null) maxParticipants = "32";

    String description = (String) request.getAttribute("description");
    if (description == null) description = "テスト用の大会概要です。";

    String status = (String) request.getAttribute("status");
    if (status == null) status = "1"; // 1=受付中 をデフォに

    // 追加項目
    String venue = (String) request.getAttribute("venue");
    if (venue == null) venue = "〇〇市民ホール";

    String gameTitle = (String) request.getAttribute("gameTitle");
    if (gameTitle == null) gameTitle = "CardGameX";

    String tournamentFormat = (String) request.getAttribute("tournamentFormat");
    if (tournamentFormat == null) tournamentFormat = "スイスドロー";

    String entryFeeYen = (String) request.getAttribute("entryFeeYen");
    if (entryFeeYen == null) entryFeeYen = "500";

    // datetime-local は "yyyy-MM-ddTHH:mm"
    String registrationDeadline = (String) request.getAttribute("registrationDeadline");
    if (registrationDeadline == null) registrationDeadline = "2026-01-31T23:59";

    String matchFormat = (String) request.getAttribute("matchFormat");
    if (matchFormat == null) matchFormat = "BO1";

    String timeLimitMinutes = (String) request.getAttribute("timeLimitMinutes");
    if (timeLimitMinutes == null) timeLimitMinutes = "25";

    String drawRule = (String) request.getAttribute("drawRule");
    if (drawRule == null) drawRule = "時間切れは両者1点";

    String prizeFirst = (String) request.getAttribute("prizeFirst");
    if (prizeFirst == null) prizeFirst = "優勝トロフィー";

    String prizeSecond = (String) request.getAttribute("prizeSecond");
    if (prizeSecond == null) prizeSecond = "準優勝メダル";

    String prizeThird = (String) request.getAttribute("prizeThird");
    if (prizeThird == null) prizeThird = "プロモカード";
%>

<% if (error != null) { %>
    <p style="color:red;"><%= error %></p>
<% } %>

<form action="<%= request.getContextPath() %>/registerTournament" method="post">

    <p>
        大会名：<br>
        <input type="text" name="name" size="40" required value="<%= name %>">
    </p>

    <p>
        開催日：<br>
        <input type="date" name="eventDate" required value="<%= eventDate %>">
    </p>

    <p>
        開催時刻：<br>
        <input type="time" name="eventTime" value="<%= eventTime %>">
    </p>

    <p>
        最大参加人数：<br>
        <input type="number" name="maxParticipants" min="1" required value="<%= maxParticipants %>">
    </p>

    <p>
        大会概要：<br>
        <textarea name="description" rows="5" cols="50" required><%= description %></textarea>
    </p>

    <hr>

    <h2>大会詳細（テスト用初期値入り）</h2>

    <p>
        会場：<br>
        <input type="text" name="venue" size="40" value="<%= venue %>">
    </p>

    <p>
        使用ゲーム：<br>
        <input type="text" name="gameTitle" size="40" value="<%= gameTitle %>">
    </p>

    <p>
        形式：<br>
        <input type="text" name="tournamentFormat" size="40" value="<%= tournamentFormat %>">
    </p>

    <p>
        参加費(円)：<br>
        <input type="number" name="entryFeeYen" min="0" step="1" value="<%= entryFeeYen %>">
    </p>

    <p>
        登録締め切り：<br>
        <input type="datetime-local" name="registrationDeadline" value="<%= registrationDeadline %>">
    </p>

    <p>
        試合形式：<br>
        <input type="text" name="matchFormat" size="40" value="<%= matchFormat %>">
    </p>

    <p>
        制限時間(分)：<br>
        <input type="number" name="timeLimitMinutes" min="0" step="1" value="<%= timeLimitMinutes %>">
    </p>

    <p>
        引き分け時：<br>
        <input type="text" name="drawRule" size="40" value="<%= drawRule %>">
    </p>

    <p>
        優勝賞品：<br>
        <input type="text" name="prizeFirst" size="40" value="<%= prizeFirst %>">
    </p>

    <p>
        準優勝賞品：<br>
        <input type="text" name="prizeSecond" size="40" value="<%= prizeSecond %>">
    </p>

    <p>
        3位賞品：<br>
        <input type="text" name="prizeThird" size="40" value="<%= prizeThird %>">
    </p>

    <hr>

    <p>
        状態：<br>
        <select name="status">
            <option value="0" <%= "0".equals(status) ? "selected" : "" %>>受付前</option>
            <option value="1" <%= "1".equals(status) ? "selected" : "" %>>受付中</option>
        </select>
    </p>

    <p>
        <button type="submit">大会を登録する</button>
    </p>

</form>

<p>
    <a href="<%= request.getContextPath() %>/menu">メニューへ戻る</a>
</p>

<jsp:include page="/base/footer.jsp" />
</body>
</html>
