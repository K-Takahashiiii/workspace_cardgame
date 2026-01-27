package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Player;
import bean.Tournament;
import dao.TournamentsDAO;

@WebServlet("/tournament/list")
public class TournamentListPlayerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Player loginPlayer =
                (session == null) ? null : (Player) session.getAttribute("loginPlayer");

        if (loginPlayer == null) {
            request.setAttribute("error", "ログインしてください");
            RequestDispatcher rd = request.getRequestDispatcher("/auth/login_player.jsp");
            rd.forward(request, response);
            return;
        }

        try {
            TournamentsDAO dao = new TournamentsDAO();
            List<Tournament> tournaments = dao.findAll();

            request.setAttribute("tournaments", tournaments);
            RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_list.jsp");
            rd.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "大会一覧の取得中にエラーが発生しました");
            RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_list.jsp");
            rd.forward(request, response);
        }
    }
}
