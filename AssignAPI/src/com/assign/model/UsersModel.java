package com.assign.model;

import java.util.ArrayList;
import java.util.List;

import com.assign.entity.User;

public class UsersModel {

	
	public static List<User> generateUsers(){
		List<User> users;
		users = new ArrayList<>();
		users.add(new User(1, "testAdmin", "iGqIyv/USJyUCeTmiBJdlw==", 1));		
		users.add(new User(2, "testUser", "FZDXAtJKTJuriQvADj92FQ==", 2));
		
		return users;
		
	}
} 
