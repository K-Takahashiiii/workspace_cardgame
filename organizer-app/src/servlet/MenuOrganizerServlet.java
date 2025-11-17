package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/auth/menu")
public class MenuOrganizerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // 必要ならここでセッションチェックとかしてもOK
        // HttpSession session = request.getSession(false);

        RequestDispatcher rd =
                request.getRequestDispatcher("/auth/menu.jsp");
        rd.forward(request, response);
        return;
    }
}
