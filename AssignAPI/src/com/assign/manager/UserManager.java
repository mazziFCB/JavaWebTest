package com.assign.manager;

import com.assign.entity.User;
import com.assign.model.UsersModel;

public class UserManager {

	public User loginUser(String username, String userpassword){
		User user = null;
		user = UsersModel.generateUsers().stream().filter(v->{
			return v.getUserName().equalsIgnoreCase(username) && v.getUserPassword().equals(userpassword);
		}).findFirst().orElse(null);
		if(user!=null){
			user.setUserPassword(null);	
		}
	
		return user;
	}
	
	public boolean checkParamPrivilege(int userId){
		User user = UsersModel.generateUsers().stream().filter(v->{
			return v.getUserId() == userId;
		}).findFirst().get();
		 
		return user.getUserType()==1;
	}
}
