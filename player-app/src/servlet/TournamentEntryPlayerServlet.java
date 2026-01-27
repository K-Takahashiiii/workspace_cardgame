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
import dao.ParticipantsDAO;

@WebServlet("/tournament/entry")
public class TournamentEntryPlayerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // ログインチェック
        HttpSession session = request.getSession(false);
        Player loginPlayer = (session == null) ? null : (Player) session.getAttribute("loginPlayer");
        if (loginPlayer == null) {
            request.setAttribute("error", "ログインしてください");
            RequestDispatcher rd = request.getRequestDispatcher("/auth/login_player.jsp");
            rd.forward(request, response);
            return;
        }

        // tournamentId取得
        String tid = request.getParameter("tournamentId");
        if (tid == null || tid.trim().isEmpty()) {
            request.setAttribute("error", "tournamentId がありません");
            RequestDispatcher rd = request.getRequestDispatcher("/tournament/entry_done.jsp");
            rd.forward(request, response);
            return;
        }

        int tournamentId;
        try {
            tournamentId = Integer.parseInt(tid);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "tournamentId が不正です");
            RequestDispatcher rd = request.getRequestDispatcher("/tournament/entry_done.jsp");
            rd.forward(request, response);
            return;
        }

        try {
            ParticipantsDAO dao = new ParticipantsDAO();

            int result = dao.enterWithCountUp(
                tournamentId,
                loginPlayer.getUserNum(),     // players.USER_NUM
                loginPlayer.getPlayerName()   // スナップショット保存
            );

            // entry_done.jsp 側で表示に使う
            request.setAttribute("result", result); // 1 / 0 / -1
            request.setAttribute("tournamentId", tournamentId);

            RequestDispatcher rd = request.getRequestDispatcher("/tournament/entry_done.jsp");
            rd.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "エントリー処理中にエラーが発生しました");
            request.setAttribute("tournamentId", tournamentId);

            RequestDispatcher rd = request.getRequestDispatcher("/tournament/entry_done.jsp");
            rd.forward(request, response);
        }
    }
}
