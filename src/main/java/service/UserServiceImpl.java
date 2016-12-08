package service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import redistest.model.UserModel;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Cacheable(value="userCache", key="#username")
	public UserModel getUser(String username) {
			logger.info("{} - execute userService, userName = {}",Thread.currentThread().getName(), username);
			return new UserModel(username);
	}

//	@Cacheable(value="allUsers")
	public List<UserModel> getAllUsers() {
		logger.info("{} - execute getAllUsers!", Thread.currentThread().getName());
		return Arrays.asList(new UserModel("jason"), new UserModel("david"));
	}
	
	
	

}
