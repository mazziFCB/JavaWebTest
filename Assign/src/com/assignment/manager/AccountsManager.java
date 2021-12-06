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
import com.assignment.handler.DBResponse;
import com.assignment.utils.Global;


@ManagedBean
@ViewScoped
public class AccountsManager {

	
	List<Account> accountList = new ArrayList<>();
	Account selectedAccount;
	@PostConstruct
	void init(){
		DBResponse response = Connection.getAccounts();
		for(Object acc : response.getResults()){
			accountList.add((Account) acc);
		}
		
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.remove("selectedAccount");

	}
	
	public void viewAccountDetails(){
		if(selectedAccount == null){
			Global.showMessage("Select Account!");
			return;
		}
		
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("selectedAccount", selectedAccount);
		Global.redirectUserTo("accountsStatements.xhtml");
	}
	
	public List<Account> getAccountList() {
		return accountList;
	}
	public void setAccountList(List<Account> accountList) {
		this.accountList = accountList;
	}
	
	public Account getSelectedAccount() {
		return selectedAccount;
	}
	public void setSelectedAccount(Account selectedAccount) {
		this.selectedAccount = selectedAccount;
	}
}
