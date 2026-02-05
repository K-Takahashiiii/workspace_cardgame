package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Player;
import dao.MatchesDAO;
import dao.ResultsDAO;
import dao.TournamentsDAO;

@WebServlet("/match/reportWinner")
public class ReportWinnerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);
        Player loginPlayer = (session == null) ? null : (Player) session.getAttribute("loginPlayer");
        if (loginPlayer == null) {
            response.sendRedirect(request.getContextPath() + "/loginPlayer");
            return;
        }

        int tournamentId = Integer.parseInt(request.getParameter("tournamentId"));
        int matchId = Integer.parseInt(request.getParameter("matchId"));
        int winnerId = Integer.parseInt(request.getParameter("winnerId"));

        try {
            MatchesDAO dao = new MatchesDAO();

            // 1=成功, 0=既に確定, -1=不正
            int r = dao.reportWinner(tournamentId, matchId, winnerId, loginPlayer.getUserNum());

            if (r == 1) {
                ResultsDAO rdao = new ResultsDAO();

                // 決勝確定して results 保存までできたら true が返る想定
                boolean finalized = rdao.upsertIfFinalized(tournamentId);

                if (finalized) {
                    new TournamentsDAO().updateStatus(tournamentId, 3); // 3=終了
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/match/bracket?tournamentId=" + tournamentId);
    }
}
