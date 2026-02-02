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
import dao.PlayersDAO;

@WebServlet({"/loginPlayer", "/auth/loginPlayer"})
public class LoginPlayerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // ログインフォームへ
        RequestDispatcher rd =
                request.getRequestDispatcher("/auth/login_player.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String telephoneNum = request.getParameter("telephone_num");
        String password = request.getParameter("password");

        String error = null;

        if (isEmpty(telephoneNum) || isEmpty(password)) {
            error = "電話番号とパスワードを入力してください";
            request.setAttribute("error", error);
            request.getRequestDispatcher("/auth/login_player.jsp").forward(request, response);
            return;
        }

        try {
            PlayersDAO dao = new PlayersDAO();
            Player player = dao.findByTelephoneAndPassword(telephoneNum, password);

            if (player == null) {
                // 認証失敗
                error = "電話番号またはパスワードが違います";
                request.setAttribute("error", error);
                request.getRequestDispatcher("/auth/login_player.jsp").forward(request, response);
                return;
            }

            // 認証成功：セッションに保存
            HttpSession session = request.getSession();
            session.setAttribute("loginPlayer", player);

            // ★追加：ログイン前の「戻り先」があれば、そこへ戻す
            // 例：/match/bracket?tournamentId=123
            //     /match/input?tournamentId=123&matchId=45
            String afterLoginPath = (String) session.getAttribute("afterLoginPath");
            if (afterLoginPath != null && !afterLoginPath.isEmpty()) {
                session.removeAttribute("afterLoginPath"); // 使い捨て
                response.sendRedirect(request.getContextPath() + afterLoginPath);
                return;
            }

            // 戻り先が無いなら、従来通り成功画面へ
            RequestDispatcher rd =
                    request.getRequestDispatcher("/auth/login_player_success.jsp");
            rd.forward(request, response);
            return;

        } catch (SQLException e) {
            e.printStackTrace();
            error = "ログイン処理中にエラーが発生しました";
            request.setAttribute("error", error);
            request.getRequestDispatcher("/auth/login_player.jsp").forward(request, response);
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
