package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Organizer;
import dao.OrganizersDAO;

@WebServlet({"/registerOrganizer", "/auth/registerOrganizer"})
public class RegisterOrganizerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        // GETで来たときはフォームに飛ばすだけ
        RequestDispatcher rd =
                request.getRequestDispatcher("/auth/register_organizer.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

    	// 1) 文字コード
        request.setCharacterEncoding("UTF-8");

        String storeName = request.getParameter("store_name");
        String repName = request.getParameter("representative_name");
        String password = request.getParameter("password");
        String name = request.getParameter("name");

        String error = null;

     // 3) 入力チェック（null / 空文字）
        if (isEmpty(storeName) || isEmpty(repName) || isEmpty(password) || isEmpty(name)) {
            error = "すべての項目を入力してください";

            request.setAttribute("error", error);
            RequestDispatcher rd = request.getRequestDispatcher("/auth/register_organizer.jsp");
            rd.forward(request, response);
            return;
        }

     // 5) エラーなければ DAO 経由で INSERT

        Organizer organizer = new Organizer();
        organizer.setStoreName(storeName);
        organizer.setRepresentativeName(repName);
        organizer.setPassword(password);
        organizer.setName(name);

        try {
            // ★ DAOを使ってINSERT
            OrganizersDAO dao = new OrganizersDAO();
            int managementNum = dao.insertOrganizer(organizer);

            // ★ ここまで来たら「INSERT成功」
            //完了画面へ遷移
            request.setAttribute("management_num", managementNum);
            request.setAttribute("name", name);
            RequestDispatcher rd = request.getRequestDispatcher("/auth/register_organizer_done.jsp");
            rd.forward(request, response);
            return;

        } catch (SQLException e) {
            // ★ DAOの中でSQLExceptionが起きた場合、ここに飛んでくる
            e.printStackTrace(); // ログに出す（開発中はこれで十分）

            error = "登録中にエラーが発生しました";
            request.setAttribute("error", error);

            RequestDispatcher rd =
                    request.getRequestDispatcher("/auth/register_organizer.jsp");
            rd.forward(request, response);
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

}
