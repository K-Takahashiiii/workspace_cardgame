package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Organizer;
import bean.Tournament;
import dao.TournamentsDAO;

@WebServlet({ "/registerTournament", "/tournament/registerTournament" })
public class RegisterTournamentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Organizer loginOrganizer = (session == null) ? null : (Organizer) session.getAttribute("loginOrganizer");
        if (loginOrganizer == null) {
            request.setAttribute("error", "ログインしてください");
            request.getRequestDispatcher("/auth/login_organizer.jsp").forward(request, response);
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_register.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // ① セッションからログイン主催者を取得
        HttpSession session = request.getSession(false);
        Organizer loginOrganizer = (session == null) ? null : (Organizer) session.getAttribute("loginOrganizer");

        if (loginOrganizer == null) {
            request.setAttribute("error", "ログインしてください");
            RequestDispatcher rd = request.getRequestDispatcher("/auth/login_organizer.jsp");
            rd.forward(request, response);
            return;
        }

        // ② フォーム入力を受け取る
        // 互換対応：camelCase / snake_case どっちでも取れるようにする
        String name = param(request, "name");

        String eventDateStr = param(request, "eventDate", "event_date");
        String eventTimeStr = param(request, "eventTime", "event_time"); // 任意

        String maxStr = param(request, "maxParticipants", "max_participants");
        String description = param(request, "description");
        String statusStr = param(request, "status");

        // 追加フィールド（任意が多い想定）
        String venue = param(request, "venue");
        String gameTitle = param(request, "gameTitle", "game_title");
        String tournamentFormat = param(request, "tournamentFormat", "tournament_format");
        String entryFeeYenStr = param(request, "entryFeeYen", "entry_fee_yen");
        String registrationDeadlineStr = param(request, "registrationDeadline", "registration_deadline"); // datetime-local想定(yyyy-MM-ddTHH:mm)
        String matchFormat = param(request, "matchFormat", "match_format");
        String timeLimitMinutesStr = param(request, "timeLimitMinutes", "time_limit_minutes");
        String drawRule = param(request, "drawRule", "draw_rule");
        String prizeFirst = param(request, "prizeFirst", "prize_first");
        String prizeSecond = param(request, "prizeSecond", "prize_second");
        String prizeThird = param(request, "prizeThird", "prize_third");

        String error = null;

        // ③ 必須チェック（必要最低限だけ必須にする）
        if (isEmpty(name) || isEmpty(eventDateStr) || isEmpty(maxStr) || isEmpty(statusStr)) {
            error = "必須項目を入力してください";
        }

        // ④ 型変換
        LocalDate eventDate = null;
        LocalTime eventTime = null;
        int maxParticipants = 0;
        int status = 0;

        int entryFeeYen = 0;         // 任意：未入力なら0扱い
        LocalDateTime registrationDeadline = null; // 任意
        int timeLimitMinutes = 0;     // 任意：未入力なら0扱い

        if (error == null) {
            try {
                eventDate = LocalDate.parse(eventDateStr); // yyyy-MM-dd
            } catch (Exception e) {
                error = "開催日が不正です";
            }
        }

        if (error == null) {
            if (!isEmpty(eventTimeStr)) {
                try {
                    eventTime = LocalTime.parse(eventTimeStr); // HH:mm
                } catch (Exception e) {
                    error = "開催時刻が不正です";
                }
            }
        }

        if (error == null) {
            try {
                maxParticipants = Integer.parseInt(maxStr);
                if (maxParticipants <= 0) {
                    error = "最大参加人数は1以上にしてください";
                }
            } catch (NumberFormatException e) {
                error = "最大参加人数は数字で入力してください";
            }
        }

        if (error == null) {
            try {
                status = Integer.parseInt(statusStr);
            } catch (NumberFormatException e) {
                error = "状態が不正です";
            }
        }

        // 任意項目：参加費(円)
        if (error == null && !isEmpty(entryFeeYenStr)) {
            try {
                entryFeeYen = Integer.parseInt(entryFeeYenStr);
                if (entryFeeYen < 0) {
                    error = "参加費は0以上にしてください";
                }
            } catch (NumberFormatException e) {
                error = "参加費は数字で入力してください";
            }
        }

        // 任意項目：制限時間(分)
        if (error == null && !isEmpty(timeLimitMinutesStr)) {
            try {
                timeLimitMinutes = Integer.parseInt(timeLimitMinutesStr);
                if (timeLimitMinutes < 0) {
                    error = "制限時間は0以上にしてください";
                }
            } catch (NumberFormatException e) {
                error = "制限時間は数字で入力してください";
            }
        }

        // 任意項目：登録締め切り（datetime-localなら "yyyy-MM-ddTHH:mm" で入る）
        if (error == null && !isEmpty(registrationDeadlineStr)) {
            try {
                registrationDeadline = LocalDateTime.parse(registrationDeadlineStr);
            } catch (Exception e) {
                error = "登録締め切りが不正です";
            }
        }

        // ⑤ エラーがあればフォームに戻す（入力保持用に request.setAttribute も載せる）
        if (error != null) {
            request.setAttribute("error", error);

            // 入力保持（必要なものだけ）
            request.setAttribute("name", name);
            request.setAttribute("eventDate", eventDateStr);
            request.setAttribute("eventTime", eventTimeStr);
            request.setAttribute("maxParticipants", maxStr);
            request.setAttribute("description", description);
            request.setAttribute("status", statusStr);

            request.setAttribute("venue", venue);
            request.setAttribute("gameTitle", gameTitle);
            request.setAttribute("tournamentFormat", tournamentFormat);
            request.setAttribute("entryFeeYen", entryFeeYenStr);
            request.setAttribute("registrationDeadline", registrationDeadlineStr);
            request.setAttribute("matchFormat", matchFormat);
            request.setAttribute("timeLimitMinutes", timeLimitMinutesStr);
            request.setAttribute("drawRule", drawRule);
            request.setAttribute("prizeFirst", prizeFirst);
            request.setAttribute("prizeSecond", prizeSecond);
            request.setAttribute("prizeThird", prizeThird);

            RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_register.jsp");
            rd.forward(request, response);
            return;
        }

        // ⑥ Tournament を組み立てる（organizer_id はセッションから）
        Tournament t = new Tournament();
        t.setName(name);

        // Organizer の getter 名はプロジェクトに合わせる（現状のあなたのコードを尊重）
        t.setOrganizerId(loginOrganizer.getManagementNum());

        t.setEventDate(eventDate);
        t.setEventTime(eventTime);
        t.setMaxParticipants(maxParticipants);
        t.setCurrentParticipants(0);
        t.setDescription(description);
        t.setStatus(status);

        // 追加（大会詳細）
        t.setVenue(venue);
        t.setGameTitle(gameTitle);
        t.setTournamentFormat(tournamentFormat);
        t.setEntryFeeYen(entryFeeYen);
        t.setRegistrationDeadline(registrationDeadline);
        t.setMatchFormat(matchFormat);
        t.setTimeLimitMinutes(timeLimitMinutes);
        t.setDrawRule(drawRule);
        t.setPrizeFirst(prizeFirst);
        t.setPrizeSecond(prizeSecond);
        t.setPrizeThird(prizeThird);

        // ⑦ DAOでINSERT
        try {
            TournamentsDAO dao = new TournamentsDAO();
            dao.insertTournament(t);

            // 登録完了画面へ
            request.setAttribute("tournament", t); // まとめて渡す（done側で自由に表示できる）
            RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_register_done.jsp");
            rd.forward(request, response);
            return;

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "登録中にエラーが発生しました");

            // 入力保持（最低限）
            request.setAttribute("tournament", t);

            RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_register.jsp");
            rd.forward(request, response);
            return;
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * 1つ目のキーが空なら2つ目…の順でパラメータ取得（互換用）
     */
    private String param(HttpServletRequest request, String... keys) {
        for (String k : keys) {
            String v = request.getParameter(k);
            if (!isEmpty(v)) return v;
        }
        // どれも無い/空なら、最初のキーで取得した結果（null or 空）を返しておく
        return (keys.length >= 1) ? request.getParameter(keys[0]) : null;
    }
}
