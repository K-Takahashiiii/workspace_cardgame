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

import bean.Organizer;
import dao.OrganizersDAO;

@WebServlet("/auth/loginOrganizer")
public class LoginOrganizerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // GETで来たらログイン画面に飛ばすだけ
        RequestDispatcher rd =
                request.getRequestDispatcher("/auth/login_organizer.jsp");
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
                    request.getRequestDispatcher("/auth/login_organizer.jsp");
            rd.forward(request, response);
            return;
        }

        try {
        	OrganizersDAO dao = new OrganizersDAO();
        	Organizer organizer = dao.loginCheck(name, pass);

        	if(organizer != null){
        		//ログイン成功、セッションに情報をいれる
        		HttpSession session = request.getSession();
        		session.setAttribute("loginOrganizer", organizer);

        		//ログイン成功画面へ遷移
        		RequestDispatcher rd = request.getRequestDispatcher("/auth/login_organizer_success.jsp");
            	rd.forward(request, response);
            	return;

        	}

        } catch (SQLException e) {

        	e.printStackTrace();
        	request.setAttribute("error", "ログイン処理中にエラーが発生しました");
        	RequestDispatcher rd = request.getRequestDispatcher("/auth/login_organizer.jsp");
        	rd.forward(request, response);
        	return;

        }



    }
}
