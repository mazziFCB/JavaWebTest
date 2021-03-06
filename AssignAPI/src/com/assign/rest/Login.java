package com.assign.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.assign.entity.User;
import com.assign.handler.DBResponse;
import com.assign.manager.UserManager;
import com.assign.security.Encryptor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import utils.Messages;

@Path("login")
public class Login {

	@POST 	
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(final String input) {

		JsonElement obj = new JsonParser().parse(input);
		if(obj.isJsonNull()){
			DBResponse error = new DBResponse();
			error.setResultId(400);
			error.setResultMessage(Messages.INVALID_PARAMETERS);
			Response response=Response.status(400).entity(error.toString()).build();
			return response;
		}
		JsonObject credentialsObj  =obj.getAsJsonObject();
		if(!credentialsObj.has("username") && !credentialsObj.has("password")){
			DBResponse error = new DBResponse();
			error.setResultId(400);
			error.setResultMessage(Messages.INVALID_PARAMETERS);
			Response response=Response.status(400).entity(error.toString()).build();
			return response;
			
		}
		UserManager userManager = new UserManager();
		String username = credentialsObj.get("username").getAsString();
		String password = Encryptor.doEncrypt(credentialsObj.get("password").getAsString());
		User user = userManager.loginUser(username, password);
		if(user == null){
			DBResponse error = new DBResponse();
			error.setResultId(400);
			error.setResultMessage(Messages.INVALID_CREDENTIALS);
			Response response=Response.status(400).entity(error.toString()).build();
			return response;
		}
		DBResponse dbResponse = new DBResponse();
		dbResponse.setResultId(1);
		dbResponse.setResultMessage(Messages.SUCCESS);
		dbResponse.setResultObject(user);
		Response response=Response.ok().entity(dbResponse.toString()).type(MediaType.APPLICATION_JSON).build();

		return response;
	}


}
