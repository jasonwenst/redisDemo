package operationtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class PopThread extends Thread {
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(PopThread.class);
	
	private RedisTemplate<String, Object> template;
	private String key;
	public PopThread(RedisTemplate<String, Object> redisTemplate, String key) {
		super();
		this.template = redisTemplate;
		this.key = key;
	}

	public void run() {
		while(template.opsForList().size(key) > 0) {
			String value = (String) template.opsForList().leftPop(key);
			System.out.println(Thread.currentThread().getName()+"-pop key :" + value);
//			if(Long.valueOf(value.substring(3))%100 == 0) {
//				System.out.println("value =" + value + ", do rigth push");
//				template.opsForList().rightPush(key, value);
//			}
		}
	}
}
