package com.assignment.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

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
		
		JSONObject objresp =  Global.getQueryResponse("account/get_accounts",null);
		if(objresp.getInt("resultId")!= 1){
			Global.showMessage(objresp.getString("resultMessage"));
			return;
		}
		JSONArray arr = objresp.getJSONArray("results");
		for(int i=0;i<arr.length();i++){
			Account acc = new Account();
			acc.setAccountId(arr.getJSONObject(i).getInt("accountId"));
			acc.setAccountNumber(arr.getJSONObject(i).getString("accountNumber"));
			acc.setAccountType(arr.getJSONObject(i).getString("accountType"));
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
		Global.redirectUserTo("/Pages/Accounts/accountsStatements.xhtml");
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
