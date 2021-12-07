package com.assignment.manager;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.shaded.json.JSONObject;

import com.assignment.entity.User;
import com.assignment.utils.Constant;
import com.assignment.utils.Global;
import com.assignment.utils.Messages;

@ManagedBean
@ViewScoped
public class LoginManager {

	@ManagedProperty(value = "#{sessionManager}")
	private SessionManager sessionManager;

	String txtUsername;
	String txtPassword;

	
	@PostConstruct
	void init(){
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		if(sessionMap != null && sessionMap.containsKey("infos")){
			Global.redirectUserTo("/Pages/Accounts/accountsTable.xhtml");
		}

	}
	public void login() {

		String username = txtUsername;
		String userpassword = txtPassword;
		if (username == null || username.trim().isEmpty()) {
			Global.showMessage(Messages.EMAIL_REQUIRED, FacesMessage.SEVERITY_WARN);
			return;
		}
		if (userpassword == null || userpassword.trim().isEmpty()) {
			Global.showMessage(Messages.PASSWORD_REQUIRED, FacesMessage.SEVERITY_WARN);
			return;
		}

		
        JSONObject obj = new JSONObject();
        obj.put("username", username);
        obj.put("password", userpassword);

        JSONObject response = Global.postAndRec(Constant.END_POINT, "login", obj);
        if(response.getInt("resultId")!=1){
        	Global.showMessage(response.getString("resultMessage"));
        	return;
        }
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		User  user = new User();
		user.setUserId(response.getJSONObject("resultObject").getInt("userId"));
		user.setUserType(response.getJSONObject("resultObject").getInt("userType"));
		user.setUserName(username);
		sessionMap.put("infos", user);
		if (sessionManager.addLoginSession(
				(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true))) {
			// login customer
			Global.redirectUserTo("/Pages/Accounts/accountsTable.xhtml");
		} else {
			Global.showMessage(Messages.ALREADY_LOGGED_IN, FacesMessage.SEVERITY_WARN);
		}

		return;
	}

	public void loginPage() {
		Global.redirectUserTo("/Pages/index.xhtml");
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public SessionManager getSessionManager() {
		return sessionManager;
	}

	public String getTxtUsername() {
		return txtUsername;
	}

	public void setTxtUsername(String txtUsername) {
		this.txtUsername = txtUsername;
	}

	public String getTxtPassword() {
		return txtPassword;
	}

	public void setTxtPassword(String txtPassword) {
		this.txtPassword = txtPassword;
	}

}
