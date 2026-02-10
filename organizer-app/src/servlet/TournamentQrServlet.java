// organizer-app/src/servlet/TournamentQrServlet.java
package servlet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Organizer;
import bean.Tournament;
import dao.TournamentsDAO;

@WebServlet({ "/tournament/qr", "/tournament/showQr" })
public class TournamentQrServlet extends HttpServlet {

    /**
     * player-app のコンテキストパス。
     * もしプロジェクト名（コンテキスト）が違うなら、ここだけ変更してください。
     */
    private static final String PLAYER_CONTEXT_PATH = "/player-app";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Organizer loginOrganizer = (session == null) ? null : (Organizer) session.getAttribute("loginOrganizer");
        if (loginOrganizer == null) {
            request.setAttribute("error", "ログインしてください");
            RequestDispatcher rd = request.getRequestDispatcher("/auth/login_organizer.jsp");
            rd.forward(request, response);
            return;
        }

        int tournamentId;
        try {
            tournamentId = Integer.parseInt(request.getParameter("tournamentId"));
        } catch (Exception e) {
            request.setAttribute("error", "tournamentId が不正です");
            RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_qr.jsp");
            rd.forward(request, response);
            return;
        }

        try {
            TournamentsDAO dao = new TournamentsDAO();
            Tournament t = dao.findByTournamentId(tournamentId);

            if (t == null) {
                request.setAttribute("error", "大会が見つかりません");
                RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_qr.jsp");
                rd.forward(request, response);
                return;
            }

            int organizerId = loginOrganizer.getManagementNum();
            if (t.getOrganizerId() != organizerId) {
                request.setAttribute("error", "この大会はあなたの管理対象ではありません");
                RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_qr.jsp");
                rd.forward(request, response);
                return;
            }

            // ★終了(3)は開始させない（必要なければ消してOK）
            if (t.getStatus() == 3) {
                request.setAttribute("error", "終了した大会は開始できません");
                RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_qr.jsp");
                rd.forward(request, response);
                return;
            }

            // ★ここで「開催中(2)」に更新
            // すでに2でもUPDATEは通る（冪等）。気になるなら条件分岐してもOK。
            int updated = dao.updateStatus(tournamentId, organizerId, 2);
            if (updated == 0) {
                request.setAttribute("error", "大会開始（開催中更新）に失敗しました");
                RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_qr.jsp");
                rd.forward(request, response);
                return;
            }
            t.setStatus(2);

            String joinUrl = buildJoinUrl(request, tournamentId);

            // 外部APIでQR画像を生成（PNGが返る）
            String qrImageUrl = buildQrImageUrl(joinUrl);

            request.setAttribute("tournament", t);
            request.setAttribute("joinUrl", joinUrl);
            request.setAttribute("qrImageUrl", qrImageUrl);

            RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_qr.jsp");
            rd.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "QRコードの生成に失敗しました");
            RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_qr.jsp");
            rd.forward(request, response);
        }
    }

    private String buildJoinUrl(HttpServletRequest request, int tournamentId) {
        // 例: http://localhost:8080/player-app/match/bracket?tournamentId=123
        String scheme = request.getScheme();




//        String host = request.getServerName();
//        String ipv4 = inetAddress.getHostAddress();

        String ipv4 = "";

        InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getLocalHost();
			ipv4 = inetAddress.getHostAddress();
			System.out.println(ipv4);
		} catch (UnknownHostException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}



        int port = request.getServerPort();

        boolean defaultPort = ("http".equalsIgnoreCase(scheme) && port == 80)
                           || ("https".equalsIgnoreCase(scheme) && port == 443);

        String base = scheme + "://" + ipv4 + (defaultPort ? "" : (":" + port));
        return base + PLAYER_CONTEXT_PATH + "/match/bracket?tournamentId=" + tournamentId;
    }

    private String buildQrImageUrl(String joinUrl) {
        // https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=...
        try {
            String encoded = URLEncoder.encode(joinUrl, "UTF-8"); // Java8対応
            return "https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=" + encoded;
        } catch (java.io.UnsupportedEncodingException e) {
            // UTF-8は必ず使えるので通常ここには来ない（コンパイル対策）
            throw new RuntimeException(e);
        }
    }
}
