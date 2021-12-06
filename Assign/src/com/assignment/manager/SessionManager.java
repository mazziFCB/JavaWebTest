package com.assignment.manager;

import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpSession;

import com.assignment.entity.User;

@ManagedBean
@ApplicationScoped
public class SessionManager {

	private final Map<Integer/* this will be customer ID */, HttpSession> sessionsHashMap;

	public SessionManager() {
		sessionsHashMap = new HashMap<>();
	}

	public boolean addLoginSession(HttpSession session) {
		User info = (User) session.getAttribute("infos");
		int userId = info.getUserId();
		try {

			
			// System.out.println("In add loginSession");
			// System.out.println("sessionsHashMap size is: " +
			// sessionsHashMap.size());
			// loop throw sessions if userId is found invalidate that session
			if (sessionsHashMap.containsKey(userId)) {
				HttpSession sess = sessionsHashMap.get(userId);
				System.out.println("lastAccessTime" + sess.getLastAccessedTime());

				// System.out.println("Try to remove a session since a user with
				// same id logged in");
				// try {
				// sessionsHashMap.get(userId).invalidate();
				// } catch (Exception ex) {
				//
				// }
				return false;

			}

			
			// System.out.println("Added new session, size is: " +
			// sessionsHashMap.size());
		} catch (IllegalStateException ex) {
			sessionsHashMap.remove(userId);
			session.setMaxInactiveInterval(300);
			sessionsHashMap.put(userId, session);
			return true;
		}
		session.setMaxInactiveInterval(300);
		sessionsHashMap.put(userId, session);
		return true;
	}

	public void removeSession(int userId) {
		// sessions.remove(session);
		sessionsHashMap.remove(userId);
		// System.out.println("in remove new session size " +
		// sessionsHashMap.size());
		// System.out.println("a session is removed, new session size is: " +
		// sessions.size());

	}
}
