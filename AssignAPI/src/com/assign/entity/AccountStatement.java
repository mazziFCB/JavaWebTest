package com.assign.entity;

import java.util.Date;

public class AccountStatement {

	int id;
	int accountId;
	Date dateField;
	String dateFieldString;
	double amount;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public Date getDateField() {
		return dateField;
	}
	public void setDateField(Date dateField) {
		this.dateField = dateField;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public String getDateFieldString() {
		return dateFieldString;
	}
	
	public void setDateFieldString(String dateFieldString) {
		this.dateFieldString = dateFieldString;
	}
	
	
}
