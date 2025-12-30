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

@WebServlet({"/loginOrganizer", "/auth/loginOrganizer"})
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

        String management_num = request.getParameter("management_num");
        String pass = request.getParameter("password");

        String error = null;
        int managementNum = 0;

        // 簡単な入力チェック
        if (isEmpty(management_num) || isEmpty(pass)) {
            error = "管理番号とパスワードを入力してください";
        } else {
            // 空欄じゃないときだけ数値変換
            try {
                managementNum = Integer.parseInt(management_num);
            } catch (NumberFormatException e) {
                error = "管理番号は数字で入力してください";
            }
        }

        if (error != null){
            request.setAttribute("error", error);
            RequestDispatcher rd =
                    request.getRequestDispatcher("/auth/login_organizer.jsp");
            rd.forward(request, response);
            return;
        }

        try {
        	OrganizersDAO dao = new OrganizersDAO();
        	Organizer organizer = dao.loginCheck(managementNum, pass);

        	if(organizer != null){
        		//ログイン成功、セッションに情報をいれる
        		HttpSession session = request.getSession();
        		session.setAttribute("loginOrganizer", organizer);

        		//ログイン成功画面へ遷移
        		RequestDispatcher rd = request.getRequestDispatcher("/auth/login_organizer_success.jsp");
            	rd.forward(request, response);
            	return;

        	} else {
                // 4) ログイン失敗時の戻し
                request.setAttribute("error", "管理番号またはパスワードが違います");
                RequestDispatcher rd =
                        request.getRequestDispatcher("/auth/login_organizer.jsp");
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

    private boolean isEmpty(String s ) {
        return s == null || s.trim().isEmpty();
    }

}
