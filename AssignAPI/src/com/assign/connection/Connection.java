package com.assign.connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.assign.entity.Account;
import com.assign.entity.AccountStatement;
import com.assign.handler.DBResponse;
import com.assign.security.Encryptor;

import utils.Constant;
import utils.Messages;

public class Connection {

	
	
	
	public static DBResponse getAccounts(){
		DBResponse response = new DBResponse();
		
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try (java.sql.Connection connection = DriverManager.getConnection(Constant.DATABASE_URL)) {
            
			String sql = "SELECT id,account_type,account_number FROM Account";
             
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            response.setResultMessage(Messages.NO_DATA_FOUND);
            response.setResultId(-1);
            List<Object> accList = new ArrayList<>();
            while (result.next()) {
                response.setResultMessage(Messages.SUCCESS);
                response.setResultId(1);
            	
            	Account acc = new Account();
            	acc.setAccountId(result.getInt("id"));
            	acc.setAccountType(result.getString("account_type"));
            	acc.setAccountNumber(Encryptor.getStringHashed(result.getString("account_number")));
            	accList.add(acc);
            }
            
            response.setResults(accList);
             
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
		
		return response;
	}
	
	public static DBResponse getAccountStatements(int accountId){
		DBResponse response = new DBResponse();
		
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try (java.sql.Connection connection = DriverManager.getConnection(Constant.DATABASE_URL)) {
            
			String sql = "SELECT id,account_id,datefield,amount FROM Statement"
					+ " where account_id = " + accountId;
             
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
             
            List<Object> accList = new ArrayList<>();
            response.setResultMessage(Messages.NO_DATA_FOUND);
            response.setResultId(-1);
            while (result.next()) {
            	response.setResultMessage(Messages.SUCCESS);
                response.setResultId(1);
            	AccountStatement accStatement = new AccountStatement();
            	accStatement.setId(result.getInt("id"));
            	accStatement.setAccountId(result.getInt("account_id"));
            	
            	String dateField = result.getString("datefield");
            	 SimpleDateFormat formatterDate=new SimpleDateFormat(Constant.DATE_FORMAT);  
            	 Date date2=formatterDate.parse(dateField);  
            	 accStatement.setDateFieldString(dateField);
            	 accStatement.setDateField(date2);
            	 accStatement.setAmount(Double.parseDouble(result.getString("amount")));
            	accList.add(accStatement);
            }
            
            response.setResults(accList);
             
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}
}
