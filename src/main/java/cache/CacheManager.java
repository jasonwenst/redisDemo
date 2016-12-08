package cache;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import redistest.model.UserModel;
import service.UserService;

//@Component("myCacheManager")
//@EnableCaching
public class CacheManager {
	
	private static Logger logger = LoggerFactory.getLogger(CacheManager.class);
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private UserService service;
	
	private static final Long WAIT_TIME = 1000l;
	private static final String SYN_SIGNAL = "syncUsers";
	byte[] lock = new byte[0];
	
	@SuppressWarnings("unchecked")
	public List<Object>  getAllUsers() throws InterruptedException {
		Long currentTime = System.currentTimeMillis();
		while((System.currentTimeMillis() - currentTime) < WAIT_TIME) {
			if(redisTemplate.hasKey("allUsers")) {
				logger.info("{} - constans key allUsers and return allUsers list!", Thread.currentThread().getName());
				
				return (List<Object>) redisTemplate.opsForValue().get("allUsers");
			}else {
				synchronized(lock) {
					if(redisTemplate.hasKey("allUsers")) {
						return (List<Object>) redisTemplate.opsForValue().get("allUsers");
					}
					else if(redisTemplate.hasKey(SYN_SIGNAL)) {
						logger.info("{} - waiting sync allUsers!",  Thread.currentThread().getName());
						Thread.sleep(200);
					} else {
						logger.info("{} - allUsers and syncUsers not exists, get allUsers!", Thread.currentThread().getName());
						redisTemplate.opsForValue().set(SYN_SIGNAL, "just an signal for fetch data!");
						redisTemplate.opsForList().leftPushAll("allUsers", (Collection<UserModel> )service.getAllUsers());
						boolean ret = redisTemplate.opsForValue().setIfAbsent("allUsers", service.getAllUsers());
						logger.info("{}- set allUsers {}", Thread.currentThread().getName(), ret);
						redisTemplate.delete(SYN_SIGNAL);
						break;
					}
				}
			}
		}
		
		logger.info("{} - time out and get allUsers!", Thread.currentThread().getName());
		
		redisTemplate.delete(SYN_SIGNAL);
		return (List<Object>) redisTemplate.opsForValue().get("allUsers");
		
	}
	
	
	

}
