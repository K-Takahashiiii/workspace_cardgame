package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.DBUtil;

@WebServlet("/loginOrganizer")
public class LoginOrganizerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // GETで来たらログイン画面に飛ばすだけ
        RequestDispatcher rd =
                request.getRequestDispatcher("/login_organizer.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String pass = request.getParameter("pass");

        String error = null;

        // 簡単な入力チェック
        if (name == null || name.isEmpty()
                || pass == null || pass.isEmpty()) {

            error = "名前とパスワードを入力してください";

            request.setAttribute("error", error);
            RequestDispatcher rd =
                    request.getRequestDispatcher("/login_organizer.jsp");
            rd.forward(request, response);
            return;
        }

        // DBで照合
        String sql = "SELECT id, name, pass, tell_num "
                   + "FROM organizers "
                   + "WHERE name = ? AND pass = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, pass);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // ログイン成功
                    int id = rs.getInt("id");
                    int tellNum = rs.getInt("tell_num");

                    // セッションにログイン情報を保存しておく
                    HttpSession session = request.getSession();
                    session.setAttribute("loginOrganizerId", id);
                    session.setAttribute("loginOrganizerName", name);
                    session.setAttribute("loginOrganizerTellNum", tellNum);

                    // 成功画面へ
                    RequestDispatcher rd =
                            request.getRequestDispatcher("/login_organizer_success.jsp");
                    rd.forward(request, response);
                    return;

                } else {
                    // ログイン失敗
                    error = "名前またはパスワードが違います";
                    request.setAttribute("error", error);
                    RequestDispatcher rd =
                            request.getRequestDispatcher("/login_organizer.jsp");
                    rd.forward(request, response);
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            error = "ログイン処理中にエラーが発生しました";
            request.setAttribute("error", error);
            RequestDispatcher rd =
                    request.getRequestDispatcher("/login_organizer.jsp");
            rd.forward(request, response);
        }
    }
}
