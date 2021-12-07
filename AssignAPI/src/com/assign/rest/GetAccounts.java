package com.assign.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.assign.connection.Connection;
import com.assign.entity.AccountStatement;
import com.assign.handler.DBResponse;
import com.assign.manager.UserManager;
import com.healthmarketscience.jackcess.expr.ParseException;

import utils.Constant;
import utils.Global;
import utils.Messages;

@Path("account")
public class GetAccounts {

	@GET
	@Path("/get_accounts/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAccounts() {

		DBResponse responseDb = Connection.getAccounts();

		Response response = Response.ok().entity(responseDb.toString()).type(MediaType.APPLICATION_JSON).build();

		return response;
	}

	@GET
	@Path("/get_account_statement/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAccountStatement(@QueryParam("user_id") int userId,
			@QueryParam("account_id") int accountId,
			@QueryParam("from_date") String fromDate,
			@QueryParam("to_date") String toDate,
			@QueryParam("from_amount") double fromAmount,
			@QueryParam("to_amount") double toAmount) {
		Response response = null;
		try {
			if (userId ==0 || accountId ==0) {
				DBResponse error = new DBResponse();
				error.setResultId(400);
				error.setResultMessage(Messages.INVALID_PARAMETERS);
				response = Response.status(400).entity(error.toString()).build();
				return response;

			}
			boolean threeMonths = true;
			if (fromDate!=null && toDate!=null ) {
				UserManager userManager = new UserManager();
				if (!userManager.checkParamPrivilege(userId)) {
					DBResponse error = new DBResponse();
					error.setResultId(401);
					error.setResultMessage(Messages.UNAUTHORIZED);
					response = Response.status(401).entity(error.toString()).build();
					return response;
				}
				threeMonths = false;
			}

			if(fromAmount < 0 || toAmount < 0){
				DBResponse error = new DBResponse();
				error.setResultId(400);
				error.setResultMessage(Messages.INVALID_PARAMETERS);
				response = Response.status(400).entity(error.toString()).build();
				return response;
			}
			if (fromAmount>=0 && toAmount > 0) {
				UserManager userManager = new UserManager();
				if (!userManager.checkParamPrivilege(userId)) {
					DBResponse error = new DBResponse();
					error.setResultId(401);
					error.setResultMessage(Messages.UNAUTHORIZED);
					response = Response.status(401).entity(error.toString()).build();
					return response;
				}
				
				threeMonths = false;
			}

			DBResponse responseDb = Connection.getAccountStatements(accountId);
			if(threeMonths){
				Calendar cal = Calendar.getInstance(); 
				cal.add(Calendar.MONTH, -3);
				Date date1 = cal.getTime();
				List<Object> filteredList = new ArrayList<>();
				for (Object statement : responseDb.getResults()) {
					AccountStatement objState = ((AccountStatement) statement);
					if (objState.getDateField().compareTo(date1) >= 0) {
						filteredList.add(objState);
					}
				}
				responseDb.setResults(filteredList);
			}else{
				if (!Global.getEmptyString(fromDate).isEmpty() && !Global.getEmptyString(toDate).isEmpty()) {
					SimpleDateFormat formatterDate = new SimpleDateFormat(Constant.DATE_FORMAT);
					Date date1 = formatterDate.parse(fromDate);
					Date date2 = formatterDate.parse(toDate);
					if(date2.compareTo(date1) <0){
						DBResponse error = new DBResponse();
						error.setResultId(400);
						error.setResultMessage(Messages.INVALID_PARAMETERS);
						response = Response.status(400).entity(error.toString()).build();
						return response;
					}
					List<Object> filteredList = new ArrayList<>();
					for (Object statement : responseDb.getResults()) {
						AccountStatement objState = ((AccountStatement) statement);
						if (objState.getDateField().compareTo(date1) >= 0
								&& objState.getDateField().compareTo(date2) <= 0) {
							filteredList.add(objState);
						}
					}
					responseDb.setResults(filteredList);
				}
				
				if ( toAmount > 0) {
					List<Object> filteredList = new ArrayList<>();
					for (Object statement : responseDb.getResults()) {
						AccountStatement objState = ((AccountStatement) statement);
						if (objState.getAmount() >= fromAmount
								&& objState.getAmount() <= toAmount) {
							filteredList.add(objState);
						}
					}
					responseDb.setResults(filteredList);
				}
			}
			
			if(responseDb.getResults().isEmpty()){
				responseDb.setResultMessage(Messages.NO_DATA_FOUND);
				responseDb.setResultId(-1);
			}
			
			

			response = Response.ok().entity(responseDb.toString()).type(MediaType.APPLICATION_JSON).build();
		} catch (ParseException e) {
			// TODO: handle exception
			DBResponse error = new DBResponse();
			error.setResultId(400);
			error.setResultMessage(Messages.INVALID_PARAMETERS);
			response = Response.status(400).entity(error.toString()).build();
		} catch (Exception e) {
			// TODO: handle exception
			DBResponse error = new DBResponse();
			error.setResultId(400);
			error.setResultMessage(Messages.INVALID_PARAMETERS);
			response = Response.status(400).entity(error.toString()).build();
		}

		return response;
	}

}
