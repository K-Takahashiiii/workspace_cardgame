package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Organizer;

@WebServlet({"/menu", "/auth/menu"})
public class MenuOrganizerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // ① セッション取得（なければ null）
        HttpSession session = request.getSession(false);

        // ② ログイン中の主催者を取り出す
        Organizer loginOrganizer =
                (session == null) ? null : (Organizer) session.getAttribute("loginOrganizer");

        // ③ 未ログインならログイン画面へ（推奨）
        if (loginOrganizer == null) {
            request.setAttribute("error", "ログインしてください");
            RequestDispatcher rd = request.getRequestDispatcher("/auth/login_organizer.jsp");
            rd.forward(request, response);
            return;
        }

        // ④ JSPで表示できるように request に載せる
        request.setAttribute("loginOrganizer", loginOrganizer);

        // ⑤ メニューJSPへ
        RequestDispatcher rd = request.getRequestDispatcher("/auth/menu.jsp");
        rd.forward(request, response);
    }
}
