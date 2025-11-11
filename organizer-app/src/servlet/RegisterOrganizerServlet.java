package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.OrganizersDAO;

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

    	// 1) 文字コード
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String pass = request.getParameter("pass");
        String tellNumStr = request.getParameter("tell_num");

        String error = null;
        int tellNum = 0;

     // 3) 入力チェック（null / 空文字 / 数値チェック）
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

     // 4) エラーあったらフォームに戻す（エラーメッセージ付き）
        if (error != null) {
            request.setAttribute("error", error);
            RequestDispatcher rd =
                    request.getRequestDispatcher("/register_organizer.jsp");
            rd.forward(request, response);
            return;
        }

     // 5) エラーなければ DAO 経由で INSERT
        try {
            // ★ DAOを使ってINSERT
            OrganizersDAO dao = new OrganizersDAO();
            dao.insertOrganizers(name, pass, tellNum);

            // ★ ここまで来たら「INSERT成功」
            //完了画面へ遷移
            request.setAttribute("name", name);
            RequestDispatcher rd =
                    request.getRequestDispatcher("/register_organizer_done.jsp");
            rd.forward(request, response);
            return;

        } catch (SQLException e) {
            // ★ DAOの中でSQLExceptionが起きた場合、ここに飛んでくる
            e.printStackTrace(); // ログに出す（開発中はこれで十分）

            error = "登録中にエラーが発生しました";
            request.setAttribute("error", error);

            RequestDispatcher rd =
                    request.getRequestDispatcher("/register_organizer.jsp");
            rd.forward(request, response);
            return;
        }
    }
}
