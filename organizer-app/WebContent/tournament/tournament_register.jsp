<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="bean.Organizer" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>大会登録</title>
</head>
<body>

<h1>大会登録</h1>

<%
    Organizer organizer = (Organizer) session.getAttribute("loginOrganizer");
    if (organizer == null) {
%>
    <p>ログインしてください。</p>
<%
        return;
    }
%>

<!-- エラーメッセージ表示 -->
<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
    <p style="color:red;"><%= error %></p>
<%
    }
%>

<form action="<%= request.getContextPath() %>/registerTournament" method="post">

    <p>
        大会名：<br>
        <input type="text" name="name" size="40" required>
    </p>

    <p>
        開催日：<br>
        <input type="date" name="event_date" required>
    </p>

    <p>
        開催時刻：<br>
        <input type="time" name="event_time">
    </p>

    <p>
        最大参加人数：<br>
        <input type="number" name="max_participants" min="1" required>
    </p>

    <p>
        大会概要：<br>
        <textarea name="description" rows="5" cols="50" required></textarea>
    </p>

    <p>
        状態：<br>
        <select name="status">
            <option value="0">受付前</option>
            <option value="1">受付中</option>
        </select>
    </p>

    <p>
        <button type="submit">大会を登録する</button>
    </p>

</form>

<p>
    <a href="<%= request.getContextPath() %>/menu">メニューへ戻る</a>
</p>

</body>
</html>
