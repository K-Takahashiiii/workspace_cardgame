package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.DBUtil;

@WebServlet("/registerOrganizer")
public class RegisterOrganizerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        // GETで来たときはフォームに飛ばすだけ
        RequestDispatcher rd =
                request.getRequestDispatcher("/register_organizer.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String pass = request.getParameter("pass");
        String tellNumStr = request.getParameter("tell_num");

        String error = null;
        int tellNum = 0;

        // 簡単な入力チェック
        if (name == null || name.isEmpty()
                || pass == null || pass.isEmpty()
                || tellNumStr == null || tellNumStr.isEmpty()) {

            error = "すべての項目を入力してください";

        } else {
            try {
                tellNum = Integer.parseInt(tellNumStr);
            } catch (NumberFormatException e) {
                error = "電話番号は数字で入力してください";
            }
        }

        // エラーがあればフォームに戻す
        if (error != null) {
            request.setAttribute("error", error);
            RequestDispatcher rd =
                    request.getRequestDispatcher("/register_organizer.jsp");
            rd.forward(request, response);
            return;
        }

        // DBへINSERT
        String sql = "INSERT INTO organizers (name, pass, tell_num)"
                   + " VALUES (?, ?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, pass);
            ps.setInt(3, tellNum);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            error = "登録中にエラーが発生しました";
            request.setAttribute("error", error);
            RequestDispatcher rd =
                    request.getRequestDispatcher("/register_organizer.jsp");
            rd.forward(request, response);
            return;
        }

        // 成功時：完了画面に飛ばす
        request.setAttribute("name", name);
        RequestDispatcher rd =
                request.getRequestDispatcher("/register_organizer_done.jsp");
        rd.forward(request, response);
    }
}
