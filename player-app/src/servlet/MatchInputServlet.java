package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Match;
import bean.Player;
import dao.MatchesDAO;

@WebServlet("/match/input")
public class MatchInputServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tidStr = request.getParameter("tournamentId");
        String midStr = request.getParameter("matchId");
        if (tidStr == null || midStr == null) {
            response.sendRedirect(request.getContextPath() + "/tournament/list");
            return;
        }

        int tournamentId, matchId;
        try {
            tournamentId = Integer.parseInt(tidStr);
            matchId = Integer.parseInt(midStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/tournament/list");
            return;
        }

        // ログインチェック
        HttpSession session = request.getSession(false);
        Player loginPlayer = (session == null) ? null : (Player) session.getAttribute("loginPlayer");
        if (loginPlayer == null) {
            HttpSession s = request.getSession();
            s.setAttribute("afterLoginPath", "/match/input?tournamentId=" + tournamentId + "&matchId=" + matchId);
            response.sendRedirect(request.getContextPath() + "/loginPlayer");
            return;
        }

        try {
            MatchesDAO dao = new MatchesDAO();
            Match m = dao.findOne(tournamentId, matchId);

            if (m == null) {
                request.setAttribute("error", "試合が見つかりません");
            } else {
                request.setAttribute("match", m);
            }

            request.getRequestDispatcher("/matches/match_input.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "試合情報の取得中にエラーが発生しました");
            request.getRequestDispatcher("/matches/match_input.jsp").forward(request, response);
        }
    }
}
