package operationtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import service.UserService;

public class CallUserService extends Thread {
	
	private static Logger logger = LoggerFactory.getLogger(CallUserService.class);
	
	private UserService userService;
	private String username;
	
	public CallUserService(UserService service, String username) {
		this.userService = service;
		this.username = username;
	}
	
	public void run() {
		while(true){
			logger.info("{} - call userService!", Thread.currentThread().getName());
			synchronized (username) {
				userService.getUser(username);
			}
		}
	}

	
}
