// organizer-app/src/servlet/StartTournamentServlet.java
package servlet;

import java.io.IOException;
import java.sql.SQLException;

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

@WebServlet({ "/tournament/start", "/startTournament" })
public class StartTournamentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Organizer loginOrganizer = (session == null) ? null : (Organizer) session.getAttribute("loginOrganizer");
        if (loginOrganizer == null) {
            request.setAttribute("error", "ログインしてください");
            RequestDispatcher rd = request.getRequestDispatcher("/auth/login_organizer.jsp");
            rd.forward(request, response);
            return;
        }

        int tournamentId;
        try {
            tournamentId = Integer.parseInt(request.getParameter("tournamentId"));
        } catch (Exception e) {
            request.setAttribute("error", "tournamentId が不正です");
            RequestDispatcher rd = request.getRequestDispatcher("/tournament/list");
            rd.forward(request, response);
            return;
        }

        try {
            TournamentsDAO dao = new TournamentsDAO();
            Tournament t = dao.findByTournamentId(tournamentId);

            if (t == null) {
                request.setAttribute("error", "大会が見つかりません");
                RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_start.jsp");
                rd.forward(request, response);
                return;
            }

            int organizerId = loginOrganizer.getManagementNum();
            if (t.getOrganizerId() != organizerId) {
                request.setAttribute("error", "この大会はあなたの管理対象ではありません");
                RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_start.jsp");
                rd.forward(request, response);
                return;
            }

            request.setAttribute("tournament", t);
            RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_start.jsp");
            rd.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "大会情報の取得に失敗しました");
            RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_start.jsp");
            rd.forward(request, response);
        }
    }
}
