// organizer-app/src/servlet/TournamentListServlet.java
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

import bean.Organizer;
import bean.Tournament;
import dao.TournamentsDAO;

@WebServlet({ "/tournament/list", "/tournamentList" })
public class TournamentListServlet extends HttpServlet {

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

        try {
            int organizerId = loginOrganizer.getManagementNum(); // tournaments.organizer_id に入れてる値
            TournamentsDAO dao = new TournamentsDAO();
            List<Tournament> tournaments = dao.findByOrganizerId(organizerId);

            request.setAttribute("tournaments", tournaments);

            RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_list.jsp");
            rd.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "大会一覧の取得に失敗しました");
            RequestDispatcher rd = request.getRequestDispatcher("/auth/menu.jsp");
            rd.forward(request, response);
        }
    }
}
