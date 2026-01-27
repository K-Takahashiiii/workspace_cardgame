package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Player;
import dao.PlayersDAO;

@WebServlet({"/registerPlayer", "/auth/registerPlayer"})
public class RegisterPlayerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rd =
                request.getRequestDispatcher("/auth/register_player.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String telephoneNum = request.getParameter("telephone_num");
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String playerName = request.getParameter("player_name");

        String error = null;

        if (isEmpty(telephoneNum) || isEmpty(name) || isEmpty(password) || isEmpty(playerName)) {
            error = "すべての項目を入力してください";
            request.setAttribute("error", error);
            request.getRequestDispatcher("/auth/register_player.jsp").forward(request, response);
            return;
        }

        Player player = new Player();
        player.setTelephoneNum(telephoneNum);
        player.setName(name);
        player.setPassword(password);
        player.setPlayerName(playerName);

        try {
            PlayersDAO dao = new PlayersDAO();
            int userNum = dao.insertPlayer(player);

            request.setAttribute("user_num", userNum);
            request.setAttribute("name", name);
            request.getRequestDispatcher("/auth/register_player_done.jsp").forward(request, response);
            return;

        } catch (SQLException e) {
            e.printStackTrace();
            error = "登録中にエラーが発生しました";
            request.setAttribute("error", error);
            request.getRequestDispatcher("/auth/register_player.jsp").forward(request, response);
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
