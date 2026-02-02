package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Match;
import bean.Player;
import dao.MatchesDAO;

@WebServlet("/match/bracket")
public class BracketServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tidStr = request.getParameter("tournamentId");
        if (tidStr == null) {
            response.sendRedirect(request.getContextPath() + "/tournament/list");
            return;
        }

        int tournamentId;
        try {
            tournamentId = Integer.parseInt(tidStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/tournament/list");
            return;
        }

        // ログインチェック（未ログインならログインへ）
        HttpSession session = request.getSession(false);
        Player loginPlayer = (session == null) ? null : (Player) session.getAttribute("loginPlayer");
        if (loginPlayer == null) {
            HttpSession s = request.getSession();
            s.setAttribute("afterLoginPath", "/matches/bracket?tournamentId=" + tournamentId);
            response.sendRedirect(request.getContextPath() + "/loginPlayer");
            return;
        }

        try {
            MatchesDAO dao = new MatchesDAO();

            // 初回だけブラケット生成
            dao.createBracketIfAbsent(tournamentId);

            // 取得
            List<Match> matches = dao.findByTournamentId(tournamentId);

            // roundごとに分解
            int maxRound = 0;
            for (Match m : matches) maxRound = Math.max(maxRound, m.getRoundNo());

            List<List<Match>> rounds = new ArrayList<>();
            for (int r = 1; r <= maxRound; r++) rounds.add(new ArrayList<>());

            for (Match m : matches) {
                rounds.get(m.getRoundNo() - 1).add(m);
            }

            request.setAttribute("tournamentId", tournamentId);
            request.setAttribute("rounds", rounds);

            request.getRequestDispatcher("/matches/bracket.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "トーナメント表の取得中にエラーが発生しました");
            request.getRequestDispatcher("/matches/bracket.jsp").forward(request, response);
        }
    }
}
