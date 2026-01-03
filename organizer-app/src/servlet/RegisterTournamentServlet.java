package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

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

@WebServlet({ "/registerTournament", "/tournament/registerTournament" })
public class RegisterTournamentServlet extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		Organizer loginOrganizer = (session == null) ? null : (Organizer) session.getAttribute("loginOrganizer");
		if (loginOrganizer == null) {
		    request.setAttribute("error", "ログインしてください");
		    request.getRequestDispatcher("/auth/login_organizer.jsp").forward(request, response);
		    return;
		}


        RequestDispatcher rd =
                request.getRequestDispatcher("/tournament/tournament_register.jsp");
        rd.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		// ① セッションからログイン主催者を取得
		HttpSession session = request.getSession(false);
		Organizer loginOrganizer = (session == null) ? null : (Organizer) session.getAttribute("loginOrganizer");

		if (loginOrganizer == null) {
			request.setAttribute("error", "ログインしてください");
			RequestDispatcher rd = request.getRequestDispatcher("/auth/login_organizer.jsp");
			rd.forward(request, response);
			return;
		}

		// ② フォーム入力を受け取る（JSPのname属性と一致させる）
		String name = request.getParameter("name");
		String eventDateStr = request.getParameter("event_date");
		String eventTimeStr = request.getParameter("event_time"); // 空でもOK
		String maxStr = request.getParameter("max_participants");
		String description = request.getParameter("description");
		String statusStr = request.getParameter("status");

		String error = null;

		// ③ 空チェック（必要な項目だけ）
		if (isEmpty(name) || isEmpty(eventDateStr) || isEmpty(maxStr) || isEmpty(description) || isEmpty(statusStr)) {
			error = "必須項目を入力してください";
		}

		// ④ 型変換（date/time/int）
		LocalDate eventDate = null;
		LocalTime eventTime = null;
		int maxParticipants = 0;
		int status = 0;

		if (error == null) {
			try {
				eventDate = LocalDate.parse(eventDateStr);
			} catch (Exception e) {
				error = "開催日が不正です";
			}
		}

		if (error == null) {
			// 時刻は任意（空ならnullのまま）
			if (!isEmpty(eventTimeStr)) {
				try {
					eventTime = LocalTime.parse(eventTimeStr);
				} catch (Exception e) {
					error = "開催時刻が不正です";
				}
			}
		}

		if (error == null) {
			try {
				maxParticipants = Integer.parseInt(maxStr);
				if (maxParticipants <= 0) {
					error = "最大参加人数は1以上にしてください";
				}
			} catch (NumberFormatException e) {
				error = "最大参加人数は数字で入力してください";
			}
		}

		if (error == null) {
			try {
				status = Integer.parseInt(statusStr);
			} catch (NumberFormatException e) {
				error = "状態が不正です";
			}
		}

		// ⑤ エラーがあればフォームに戻す
		if (error != null) {
			request.setAttribute("error", error);
			RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_register.jsp");
			rd.forward(request, response);
			return;
		}

		// ⑥ Tournament を組み立てる（organizer_id はセッションから）
		Tournament t = new Tournament();
		t.setName(name);

		// loginOrganizer から管理番号を取り出して organizerId にセット
		// ここは Organizer の getter 名に合わせる
		t.setOrganizerId(loginOrganizer.getManagementNum());

		t.setEventDate(eventDate);
		t.setEventTime(eventTime);
		t.setMaxParticipants(maxParticipants);
		t.setCurrentParticipants(0);
		t.setDescription(description);
		t.setStatus(status);

		// ⑦ DAOでINSERT
		try {
			TournamentsDAO dao = new TournamentsDAO();
			dao.insertTournament(t);

			// 登録完了画面へ
			request.setAttribute("name", name);
			RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_register_done.jsp");
			rd.forward(request, response);
			return;

		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("error", "登録中にエラーが発生しました");
			RequestDispatcher rd = request.getRequestDispatcher("/tournament/tournament_register.jsp");
			rd.forward(request, response);
			return;
		}
	}

	private boolean isEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}
}