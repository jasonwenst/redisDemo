package operationtest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;

import configure.BaseJUnit4Test;
import entity.AcctBalance;
import repository.AcctBalanceRepository;

public class PerformTest extends BaseJUnit4Test{
	
	private Logger log = LoggerFactory.getLogger(PerformTest.class);
	
	@Autowired
	AcctBalanceRepository acctBalanceRepository;
	
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	
	private static final int PAGE_SIZE = 10000;
	
	
	@Test
	public void testQuerySingle() {
		
		/**
		 *	2016-09-13 11:01:44,074  INFO operationtest.PerformTest.testQuerySingle[46] - query db finish in 364 milliseconds!
		 *	2016-09-13 11:01:44,225  INFO operationtest.PerformTest.testQuerySingle[53] - query redis finished in 18 milliseconds!
		 */
		
		long dbStart = System.currentTimeMillis();
		acctBalanceRepository.findOne(377061322882l);
		long dbEnd = System.currentTimeMillis();
		log.info("query db finish in {} milliseconds!", dbEnd - dbStart);
		
		log.info(((AcctBalance)redisTemplate.opsForHash().get("acctBalanceMap", 377061322882l)).toString());// 确保连接池中存在连接
		
		long redisStart = System.currentTimeMillis();
		redisTemplate.opsForHash().get("acctBalanceMap", 377061663618l);
		long redisEnd = System.currentTimeMillis();
		log.info("query redis finished in {} milliseconds!", redisEnd - redisStart);
		
	}
	
	
	@Test
	public void testQueryMap() {
		redisTemplate.getConnectionFactory().getConnection();
		log.info("start!!");
//		redisTemplate.opsForHash().get("acctBalanceMap", 377061322882l);
		long start = System.currentTimeMillis();
		redisTemplate.opsForHash().entries("acctBalanceMap");
//		acctBalanceRepository.findAll(new PageRequest(0, 10000));
		long end = System.currentTimeMillis();
		
		log.info("finish in {} milliseconds!", end - start);
	}
	
	
	@Test
	public void storeMapTest() {
		
		/**
		 * finish in 315 milliseconds!     ----    redis setup in local
		 * finish in 2483 milliseconds!    -----	redis setup in server
		 */
		redisTemplate.opsForHash().get("acctBalanceMap", 377061322882l);
		
		redisTemplate.delete("acctBalanceMap");
		Page<AcctBalance> res = acctBalanceRepository.findAll(new PageRequest(0, 10000));
		Map<Long, AcctBalance> myMap = new HashMap<Long, AcctBalance>();
		for(int i = 0; i < res.getSize(); i++) {
			myMap.put(res.getContent().get(i).getAcctId(), res.getContent().get(i));
		}
		
		log.info("start to store in redis!");
		
		long start = System.currentTimeMillis();
		
		redisTemplate.opsForHash().putAll("acctBalanceMap", myMap);
		
		long end = System.currentTimeMillis();
		
		log.info("finish in {} milliseconds!", end - start);
	}
	
	
	@Test 
	public void queryListTest() {
		
		/**
		 * 2016-09-13 10:42:56,054  INFO operationtest.PerformTest.queryListTest[75] - query from db cost 5037 millisenond!
		 * 2016-09-13 10:42:56,136  INFO operationtest.PerformTest.queryListTest[81] - query from redis cost  3219 milliseconds!
		 */
		
		long dbStart = System.currentTimeMillis();
		acctBalanceRepository.findAll(new PageRequest(0, 10000));
		
		long dbEnd = System.currentTimeMillis();
		
		redisTemplate.opsForList().size("allAcctBalance");
		log.info("query from db cost {} millisenond!", dbEnd - dbStart);
		
		long redisStart = System.currentTimeMillis();
		@SuppressWarnings("unused")
		Object d = redisTemplate.opsForList().range("allAcctBalance", 0, 10000);
		long redisEnd = System.currentTimeMillis();
		
		log.info("query from redis cost  {} milliseconds!", redisEnd - redisStart);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
//	@Ignore
	public void test() {
		
		
		log.info("start quert from db!");
		
		
		Pageable page = new PageRequest(0, PAGE_SIZE);
		Page<AcctBalance> res = acctBalanceRepository.findAll(page);
		long start = System.currentTimeMillis();
		
		
		log.info("all resutl size is {}", res.getSize());
		
//		PushThread t1 = new PushThread(0, PAGE_SIZE, 100, redisTemplate, acctBalanceRepository);
//		PushThread t2 = new PushThread(100, PAGE_SIZE, 200, redisTemplate, acctBalanceRepository);
//		PushThread t3 = new PushThread(200, PAGE_SIZE, 300, redisTemplate, acctBalanceRepository);
//		PushThread t4 = new PushThread(300, PAGE_SIZE, 400, redisTemplate, acctBalanceRepository);
//		PushThread t5 = new PushThread(400, PAGE_SIZE, 500, redisTemplate, acctBalanceRepository);
//		PushThread t6 = new PushThread(500, PAGE_SIZE, 600, redisTemplate, acctBalanceRepository);
//		PushThread t7 = new PushThread(600, PAGE_SIZE, 700, redisTemplate, acctBalanceRepository);
//		PushThread t8 = new PushThread(700, PAGE_SIZE, 800, redisTemplate, acctBalanceRepository);
//		PushThread t9 = new PushThread(800, PAGE_SIZE, res.getSize()/10000, redisTemplate, acctBalanceRepository);
		
//		t1.start();
//		t2.start();
//		t3.start();
//		t4.start();
//		t5.start();
//		t6.start();
//		t7.start();
//		t8.start();
//		t9.start();
		redisTemplate.opsForList().leftPushAll("allAcctBalance", (Collection)res.getContent());
		
//		redisTemplate.delete("allAcctBalance");
//		for(int i = 0; i < res.getSize(); i++) {
//			log.info("get {} page from db", i);
//			Page<AcctBalance> pageRes = acctBalanceRepository.findAll(new PageRequest(i, PAGE_SIZE));
//			redisTemplate.opsForList().leftPushAll("allAcctBalance", (Collection)pageRes.getContent());
//			log.info("log {} page to redis complete!", i);
//		}
		
		log.info("finish store in redis and cost {} millisecond!", (System.currentTimeMillis() - start));
	}
	
	

}
