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

@WebServlet("/optioningTournament")
public class OptioningTournamentServlet extends HttpServlet {

    private static final String JSP_PATH = "/tournament/tournament_optioning.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ログインチェック
        HttpSession session = request.getSession(false);
        Organizer loginOrganizer = (session == null) ? null : (Organizer) session.getAttribute("loginOrganizer");
        if (loginOrganizer == null) {
            request.setAttribute("error", "ログインしてください");
            request.getRequestDispatcher("/auth/login_organizer.jsp").forward(request, response);
            return;
        }

        // tournamentId を取得（互換で id も許容）
        String idStr = request.getParameter("tournamentId");
        if (isEmpty(idStr)) idStr = request.getParameter("id");

        if (isEmpty(idStr)) {
            response.sendRedirect(request.getContextPath() + "/tournament/list");
            return;
        }

        try {
            int tournamentId = Integer.parseInt(idStr);

            TournamentsDAO dao = new TournamentsDAO();
            Tournament t = dao.findByTournamentId(tournamentId);

            if (t == null) {
                response.sendRedirect(request.getContextPath() + "/tournament/list");
                return;
            }

            // 自分の大会かチェック
            if (t.getOrganizerId() != loginOrganizer.getManagementNum()) {
                response.sendRedirect(request.getContextPath() + "/tournament/list");
                return;
            }

            request.setAttribute("tournament", t);
            RequestDispatcher rd = request.getRequestDispatcher(JSP_PATH);
            rd.forward(request, response);

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "設定画面の表示に失敗しました");
            response.sendRedirect(request.getContextPath() + "/tournament/list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // ログインチェック
        HttpSession session = request.getSession(false);
        Organizer loginOrganizer = (session == null) ? null : (Organizer) session.getAttribute("loginOrganizer");
        if (loginOrganizer == null) {
            request.setAttribute("error", "ログインしてください");
            request.getRequestDispatcher("/auth/login_organizer.jsp").forward(request, response);
            return;
        }

        String error = null;

        // 必須：対象大会ID
        String tournamentIdStr = request.getParameter("tournamentId");

        // 基本
        String name = request.getParameter("name");
        String eventDateStr = request.getParameter("eventDate");
        String eventTimeStr = request.getParameter("eventTime");
        String maxStr = request.getParameter("maxParticipants");
        String description = request.getParameter("description");
        String statusStr = request.getParameter("status");

        // 詳細
        String venue = request.getParameter("venue");
        String gameTitle = request.getParameter("gameTitle");
        String tournamentFormat = request.getParameter("tournamentFormat");
        String entryFeeYenStr = request.getParameter("entryFeeYen");
        String registrationDeadlineStr = request.getParameter("registrationDeadline"); // datetime-local: yyyy-MM-ddTHH:mm
        String matchFormat = request.getParameter("matchFormat");
        String timeLimitMinutesStr = request.getParameter("timeLimitMinutes");
        String drawRule = request.getParameter("drawRule");
        String prizeFirst = request.getParameter("prizeFirst");
        String prizeSecond = request.getParameter("prizeSecond");
        String prizeThird = request.getParameter("prizeThird");

        // 参加条件（単数）
        String entryRequirementStr = request.getParameter("entryRequirement");

        // 最低限の必須チェック
        if (isEmpty(tournamentIdStr) || isEmpty(name) || isEmpty(eventDateStr) || isEmpty(maxStr) || isEmpty(statusStr)) {
            error = "必須項目を入力してください";
        }

        int tournamentId = 0;
        LocalDate eventDate = null;
        LocalTime eventTime = null;
        int maxParticipants = 0;
        int status = 0;

        int entryFeeYen = 0;
        LocalDateTime registrationDeadline = null;
        int timeLimitMinutes = 0;

        int entryRequirement = 0; // 0=条件なし運用

        if (error == null) {
            try { tournamentId = Integer.parseInt(tournamentIdStr); }
            catch (Exception e) { error = "大会IDが不正です"; }
        }

        if (error == null) {
            try { eventDate = LocalDate.parse(eventDateStr); }
            catch (Exception e) { error = "開催日が不正です"; }
        }

        if (error == null && !isEmpty(eventTimeStr)) {
            try { eventTime = LocalTime.parse(eventTimeStr); }
            catch (Exception e) { error = "開催時刻が不正です"; }
        }

        if (error == null) {
            try {
                maxParticipants = Integer.parseInt(maxStr);
                if (maxParticipants <= 0) error = "最大参加人数は1以上にしてください";
            } catch (Exception e) {
                error = "最大参加人数は数字で入力してください";
            }
        }

        if (error == null) {
            try { status = Integer.parseInt(statusStr); }
            catch (Exception e) { error = "状態が不正です"; }
        }

        if (error == null && !isEmpty(entryFeeYenStr)) {
            try {
                entryFeeYen = Integer.parseInt(entryFeeYenStr);
                if (entryFeeYen < 0) error = "参加費は0以上にしてください";
            } catch (Exception e) {
                error = "参加費は数字で入力してください";
            }
        }

        if (error == null && !isEmpty(timeLimitMinutesStr)) {
            try {
                timeLimitMinutes = Integer.parseInt(timeLimitMinutesStr);
                if (timeLimitMinutes < 0) error = "制限時間は0以上にしてください";
            } catch (Exception e) {
                error = "制限時間は数字で入力してください";
            }
        }

        if (error == null && !isEmpty(registrationDeadlineStr)) {
            try {
                registrationDeadline = LocalDateTime.parse(registrationDeadlineStr);
            } catch (Exception e) {
                error = "登録締め切りが不正です";
            }
        }

        if (error == null && !isEmpty(entryRequirementStr)) {
            try {
                entryRequirement = Integer.parseInt(entryRequirementStr);
                if (entryRequirement < 0 || entryRequirement > 3) {
                    error = "参加条件コードは0〜3で入力してください";
                }
            } catch (Exception e) {
                error = "参加条件コードは数字で入力してください";
            }
        }

        // 更新用 Tournament（エラー時もJSPに戻すためここで作る）
        Tournament t = new Tournament();
        t.setTournamentId(tournamentId);
        t.setOrganizerId(loginOrganizer.getManagementNum());

        t.setName(name);
        t.setEventDate(eventDate);
        t.setEventTime(eventTime);
        t.setMaxParticipants(maxParticipants);
        t.setDescription(description);
        t.setStatus(status);

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

        t.setEntryRequirement(entryRequirement);

        // ★追加：エラー時に戻す表示のため、参加者数だけ再取得して詰める
        // （DBにある「本当の人数」を表示できる）
        if (tournamentId != 0) {
            try {
                TournamentsDAO dao = new TournamentsDAO();
                Tournament current = dao.findByTournamentId(tournamentId);
                if (current != null) {
                    t.setParticipantCount(current.getParticipantCount());
                }
            } catch (SQLException ignore) {
                // 表示用なので失敗しても更新処理自体は止めない
            }
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("tournament", t);
            RequestDispatcher rd = request.getRequestDispatcher(JSP_PATH);
            rd.forward(request, response);
            return;
        }

        // DAO 更新
        try {
            TournamentsDAO dao = new TournamentsDAO();
            int updated = dao.updateTournament(t);

            if (updated == 0) {
                request.setAttribute("error", "更新対象が見つからないか、権限がありません");
                request.setAttribute("tournament", t);
                request.getRequestDispatcher(JSP_PATH).forward(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/tournament/list");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "更新中にエラーが発生しました");
            request.setAttribute("tournament", t);
            request.getRequestDispatcher(JSP_PATH).forward(request, response);
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
