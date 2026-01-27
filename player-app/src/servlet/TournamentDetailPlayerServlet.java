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

import bean.Player;
import bean.Tournament;
import dao.ParticipantsDAO;
import dao.TournamentsDAO;

@WebServlet("/tournament/detail")
public class TournamentDetailPlayerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String ctx = request.getContextPath();

        // ログインチェック
        HttpSession session = request.getSession(false);
        Player loginPlayer = (session == null) ? null : (Player) session.getAttribute("loginPlayer");
        if (loginPlayer == null) {
            request.setAttribute("error", "ログインしてください");
            request.getRequestDispatcher("/auth/login_player.jsp").forward(request, response);
            return;
        }

        String tid = request.getParameter("tournamentId");
        if (tid == null) {
            request.setAttribute("error", "tournamentId がありません");
            request.getRequestDispatcher("/tournament/tournament_detail.jsp").forward(request, response);
            return;
        }

        int tournamentId;
        try {
            tournamentId = Integer.parseInt(tid);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "tournamentId が不正です");
            request.getRequestDispatcher("/tournament/tournament_detail.jsp").forward(request, response);
            return;
        }

        try {
            TournamentsDAO tdao = new TournamentsDAO();
            Tournament t = tdao.findById(tournamentId);

            if (t == null) {
                request.setAttribute("error", "大会が見つかりません");
                request.getRequestDispatcher("/tournament/tournament_detail.jsp").forward(request, response);
                return;
            }

            ParticipantsDAO pdao = new ParticipantsDAO();
            boolean entered = pdao.exists(tournamentId, loginPlayer.getUserNum());

            request.setAttribute("tournament", t);
            request.setAttribute("entered", entered);

            RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_detail.jsp");
            rd.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "大会詳細の取得中にエラーが発生しました");
            request.getRequestDispatcher("/tournament/tournament_detail.jsp").forward(request, response);
        }
    }
}
