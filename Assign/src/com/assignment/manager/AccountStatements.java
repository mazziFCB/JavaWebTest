package com.assignment.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import com.assignment.entity.Account;
import com.assignment.entity.AccountStatement;
import com.assignment.entity.User;
import com.assignment.utils.Constant;
import com.assignment.utils.Global;

@ManagedBean
@ViewScoped
public class AccountStatements {

	List<AccountStatement> statementList = new ArrayList<>();
	Account account;
	Date fromDate;
	Date toDate;
	Double fromAmount;
	Double toAmount;

	@PostConstruct
	void init() {

		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		
		if (sessionMap.containsKey("selectedAccount")) {
			account = (Account) sessionMap.get("selectedAccount");
		} else {
			Global.redirectUserTo("/Pages/Accounts/accountsTable.xhtml");
		}

	}
	
	public void goToTable(){
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.remove("selectedAccount");
		Global.redirectUserTo("/Pages/Accounts/accountsTable.xhtml");
	}
	
	public boolean hasCredentials(){
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		User user = (User) sessionMap.get("infos");
		return user.getUserType() ==1;
			
		
	}
	
	public void resetInput(){
		fromDate =null;
		toDate=null;
		fromAmount = 0.0;
		toAmount =0.0;
		searchFilter();
	}
	
	public void searchFilter(){
		SimpleDateFormat formatterDate=new SimpleDateFormat(Constant.DATE_FORMAT); 
		String date1=null;
		String date2=null;
		
		JSONObject objParam = new JSONObject();
		objParam.put("account_id",Integer.toString(account.getAccountId()));
		objParam.put("user_id", Integer.toString(Global.getUser().getUserId()));
		if(fromDate !=null){
			date1 = formatterDate.format(fromDate);
			if(toDate == null){
				Global.showMessage("Enter to date");
				return;
			}
		}
		if(toDate !=null){
			date2 = formatterDate.format(toDate);
			if(toDate == null){
				Global.showMessage("Enter from date");
				return;
			}
		}
		if(date1 != null && date2 != null){
			objParam.put("from_date", date1);
			objParam.put("to_date", date2);
		}
		double amount1=0;
		double amount2=0;
		if(fromAmount!=null){
			amount1 = fromAmount;
			if(toAmount == null){
				Global.showMessage("Enter to amount");
				return;
			}
		}
		if(toAmount!=null){
			amount2 = toAmount;
			if(fromAmount == null){
				Global.showMessage("Enter from amount");
				return;
			}
		}
		if(amount2 >= amount1){
			objParam.put("from_amount", Double.toString(amount1));
			objParam.put("to_amount", Double.toString(amount2));
		}else{
			Global.showMessage("From amount must be less than to amount");
			return;
		}
		
		JSONObject objResp = Global.getQueryResponse("account/get_account_statement", objParam);

		if(objResp.getInt("resultId")!= 1){
			Global.showMessage(objResp.getString("resultMessage"));
			return;
		}
		JSONArray arr = objResp.getJSONArray("results");
		statementList = new ArrayList<>();

		for(int i=0;i<arr.length();i++){
			AccountStatement acc = new AccountStatement();
			acc.setAccountId(arr.getJSONObject(i).getInt("accountId"));
			acc.setAmount(arr.getJSONObject(i).getDouble("amount"));
			acc.setDateFieldString(arr.getJSONObject(i).getString("dateFieldString"));
			statementList.add( acc);
		}
		
		PrimeFaces.current().ajax().update("accounts");

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

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Double getFromAmount() {
		return fromAmount;
	}

	public void setFromAmount(Double fromAmount) {
		this.fromAmount = fromAmount;
	}

	public Double getToAmount() {
		return toAmount;
	}

	public void setToAmount(Double toAmount) {
		this.toAmount = toAmount;
	}
	
	

}
