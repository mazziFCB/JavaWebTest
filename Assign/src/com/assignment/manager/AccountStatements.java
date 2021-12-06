package com.assignment.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.assignment.connection.Connection;
import com.assignment.entity.Account;
import com.assignment.entity.AccountStatement;
import com.assignment.handler.DBResponse;
import com.assignment.utils.Global;

@ManagedBean
@ViewScoped
public class AccountStatements {

	List<AccountStatement> statementList = new ArrayList<>();
	Account account;

	@PostConstruct
	void init() {

		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

		if (sessionMap.containsKey("selectedAccount")) {
			account = (Account) sessionMap.get("selectedAccount");
			DBResponse response = Connection.getAccountStatements(account.getAccountId());
			for (Object acc : response.getResults()) {
				statementList.add((AccountStatement) acc);
			}
		} else {
			Global.redirectUserTo("accountsTable.xhtml");
		}

	}
	
	public void goToTable(){
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.remove("selectedAccount");
		Global.redirectUserTo("accountsTable.xhtml");
	}

	public List<AccountStatement> getStatementList() {
		return statementList;
	}

	public void setStatementList(List<AccountStatement> statementList) {
		this.statementList = statementList;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
