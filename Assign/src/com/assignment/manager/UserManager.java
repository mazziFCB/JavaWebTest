package com.assignment.manager;

import com.assignment.entity.User;
import com.assignment.models.UsersModel;

public class UserManager {

	public User loginUser(String username, String userpassword){
		User user = null;
		user = UsersModel.generateUsers().stream().filter(v->{
			return v.getUserName().equalsIgnoreCase(username) && v.getUserPassword().equals(userpassword);
		}).findFirst().orElse(null);
		return user;
	}
}
