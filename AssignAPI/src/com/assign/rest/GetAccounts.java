package com.assign.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.assign.connection.Connection;
import com.assign.entity.AccountStatement;
import com.assign.handler.ApiError;
import com.assign.handler.DBResponse;
import com.assign.manager.UserManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
	public Response getAccountStatement(final String input) {
		Response response = null;
		try {
			JsonElement obj = new JsonParser().parse(input);
			if (obj.isJsonNull()) {
				ApiError error = new ApiError();
				error.setErrorCode(400);
				error.setErrorMessage(Messages.INVALID_PARAMETERS);
				response = Response.status(400).entity(error.toString()).build();
				return response;
			}
			JsonObject credentialsObj = obj.getAsJsonObject();
			if (!credentialsObj.has("user_id") && !credentialsObj.has("account_id")) {
				ApiError error = new ApiError();
				error.setErrorCode(400);
				error.setErrorMessage(Messages.INVALID_PARAMETERS);
				response = Response.status(400).entity(error.toString()).build();
				return response;

			}
			int userId = credentialsObj.get("user_id").getAsInt();
			String fromDate = null;
			String toDate = null;
			double fromAmount = 0;
			double toAmount = 0;
			if (credentialsObj.has("from_date") && credentialsObj.has("to_date")) {
				UserManager userManager = new UserManager();
				if (!userManager.checkParamPrivilege(userId)) {
					ApiError error = new ApiError();
					error.setErrorCode(401);
					error.setErrorMessage(Messages.UNAUTHORIZED);
					response = Response.status(401).entity(error.toString()).build();
					return response;
				}
				fromDate = credentialsObj.get("from_date").getAsString();
				toDate = credentialsObj.get("to_date").getAsString();
			}

			if (credentialsObj.has("from_amount") && credentialsObj.has("to_amount")) {
				UserManager userManager = new UserManager();
				if (!userManager.checkParamPrivilege(userId)) {
					ApiError error = new ApiError();
					error.setErrorCode(401);
					error.setErrorMessage(Messages.UNAUTHORIZED);
					response = Response.status(401).entity(error.toString()).build();
					return response;
				}
				fromAmount = credentialsObj.get("from_amount").getAsDouble();
				toAmount = credentialsObj.get("to_amount").getAsDouble();
				if(fromAmount < 0 || toAmount < 0){
					ApiError error = new ApiError();
					error.setErrorCode(400);
					error.setErrorMessage(Messages.INVALID_PARAMETERS);
					response = Response.status(400).entity(error.toString()).build();
					return response;
				}
			}

			int accountId = credentialsObj.get("account_id").getAsInt();
			DBResponse responseDb = Connection.getAccountStatements(accountId);
			if (!Global.getEmptyString(fromDate).isEmpty() && !Global.getEmptyString(toDate).isEmpty()) {
				SimpleDateFormat formatterDate = new SimpleDateFormat(Constant.DATE_FORMAT);
				Date date1 = formatterDate.parse(fromDate);
				Date date2 = formatterDate.parse(toDate);
				if(date2.compareTo(date1) <0){
					ApiError error = new ApiError();
					error.setErrorCode(400);
					error.setErrorMessage(Messages.INVALID_PARAMETERS);
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

			response = Response.ok().entity(responseDb.toString()).type(MediaType.APPLICATION_JSON).build();
		} catch (ParseException e) {
			// TODO: handle exception
			ApiError error = new ApiError();
			error.setErrorCode(400);
			error.setErrorMessage(Messages.INVALID_PARAMETERS);
			response = Response.status(400).entity(error.toString()).build();
		} catch (Exception e) {
			// TODO: handle exception
			ApiError error = new ApiError();
			error.setErrorCode(400);
			error.setErrorMessage(Messages.INVALID_PARAMETERS);
			response = Response.status(400).entity(error.toString()).build();
		}

		return response;
	}

}
