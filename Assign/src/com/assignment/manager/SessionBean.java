package com.assignment.manager;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.assignment.utils.Global;

@ManagedBean
@SessionScoped
public class SessionBean {

	@ManagedProperty(value = "#{sessionManager}")
	private SessionManager sessionManager;
	
	
	public void logout(){
		int userId = Global.getUser().getUserId();
		 FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		 sessionManager.removeSession(userId);
	        Global.redirectUserTo("/Pages/index.xhtml");
	}
	
	
	
	public SessionManager getSessionManager() {
		return sessionManager;
	}
	
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
}
