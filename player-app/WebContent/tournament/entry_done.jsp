<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String ctx = request.getContextPath();

  String error = (String) request.getAttribute("error");
  Integer result = (Integer) request.getAttribute("result");       // 1 / 0 / -1
  Integer tournamentId = (Integer) request.getAttribute("tournamentId");

  String message;
  String detail;

  if (error != null) {
    message = "エントリーに失敗しました";
    detail = error;
  } else if (result != null && result.intValue() == 1) {
    message = "エントリー完了！";
    detail = "大会への参加登録が完了しました。";
  } else if (result != null && result.intValue() == 0) {
    message = "すでにエントリー済みです";
    detail = "同じ大会に二重エントリーはできません。";
  } else if (result != null && result.intValue() == -1) {
    message = "定員に達しています";
    detail = "満員のためエントリーできませんでした。";
  } else {
    message = "結果が取得できませんでした";
    detail = "処理結果が不明です。もう一度お試しください。";
  }
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>エントリー結果</title>
  <style>
    body{ background:#fff; font-family: Arial, sans-serif; margin:0; padding:0; }
    .container{ max-width:900px; margin:30px auto; padding:20px; }
    .section{ border:1px solid #ccc; padding:20px; margin-bottom:20px; }
    .status{ text-align:center; margin-bottom:10px; font-size:18px; }
    .msg{ text-align:center; font-size:22px; font-weight:bold; margin:10px 0; }
    .detail{ text-align:center; font-size:14px; color:#333; margin:0; }
    .btn{
      display:block; width:100%; padding:14px; font-size:16px;
      text-align:center; border:1px solid #333; text-decoration:none; margin-top:14px;
    }
    .btn:hover{ opacity:0.9; }
    .btn2{ margin-top:10px; }
    .err{ color:#d00; }
  </style>
</head>
<body>

<jsp:include page="/base/header.jsp" />

<main>
  <div class="container">
    <div class="section">

      <div class="status">エントリー結果</div>

      <div class="msg <%= (error != null ? "err" : "") %>"><%= message %></div>
      <p class="detail <%= (error != null ? "err" : "") %>"><%= detail %></p>

      <% if (tournamentId != null) { %>
        <a class="btn" href="<%= ctx %>/tournament/detail?tournamentId=<%= tournamentId %>">大会詳細へ戻る</a>
      <% } %>

      <a class="btn btn2" href="<%= ctx %>/tournament/list">大会一覧へ</a>
      <a class="btn btn2" href="<%= ctx %>/menu">メニューへ</a>

    </div>
  </div>
</main>

<jsp:include page="/base/footer.jsp" />

</body>
</html>
