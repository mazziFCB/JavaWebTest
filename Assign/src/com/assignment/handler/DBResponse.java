package com.assignment.handler;

import java.util.List;

public class DBResponse {
int resultId;
String resultMessage;
Object resultObject;
List<Object> results;

public DBResponse() {
	// TODO Auto-generated constructor stub
	this.resultId = -1;
	this.resultMessage  ="";
}
public int getResultId() {
	return resultId;
}
public void setResultId(int resultId) {
	this.resultId = resultId;
}
public String getResultMessage() {
	return resultMessage;
}
public void setResultMessage(String resultMessage) {
	this.resultMessage = resultMessage;
}
public Object getResultObject() {
	return resultObject;
}
public void setResultObject(Object resultObject) {
	this.resultObject = resultObject;
}
public List<Object> getResults() {
	return results;
}
public void setResults(List<Object> results) {
	this.results = results;
}


}
