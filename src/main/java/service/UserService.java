package service;

import java.util.List;

import redistest.model.UserModel;

public interface UserService {
	
	
	UserModel getUser(String username);

	
	List<UserModel> getAllUsers();
}
