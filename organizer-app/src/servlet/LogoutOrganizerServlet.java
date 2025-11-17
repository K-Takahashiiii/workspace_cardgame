package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/auth/logoutOrganizer")
public class LogoutOrganizerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ① セッション取得（○○○○ or ○○○○）
    	HttpSession session = request.getSession(false);
        // ② セッション破棄（×××× を呼ぶ）
    	if (session != null){
    		session.invalidate();
    	}
        // ③ ログアウト完了ページへ forward
    	RequestDispatcher rd = request.getRequestDispatcher("/auth/logout_organizer_done.jsp");
    	rd.forward(request, response);
    	return;

    }
}
