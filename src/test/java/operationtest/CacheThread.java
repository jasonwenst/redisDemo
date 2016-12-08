package operationtest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cache.CacheManager;
import redistest.model.UserModel;

public class CacheThread extends Thread {
	
	private static Logger logger = LoggerFactory.getLogger(CacheThread.class);

	private CacheManager manager;
	
	public CacheThread(CacheManager manager) {
		super();
		this.manager = manager;
	}

	public void run() {
		try {
			while(true) {
				List<Object> os = manager.getAllUsers();
				for(Object o : os) {
					logger.info("{}-username = {}", Thread.currentThread().getName(), ((UserModel)o).getUsername());
				}
				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
