package operationtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import cache.CacheManager;
import configure.BaseJUnit4Test;
import redistest.model.UserModel;
import service.UserService;

@ActiveProfiles("ha")
public class OpserTest extends BaseJUnit4Test {

	private Logger logger = LoggerFactory.getLogger(OpserTest.class);

	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired(required=false)
	private List<RedisTemplate<String, Object>> templateFactory;
	@Resource
	private RedisConnectionFactory jedisConnectionFactory;

	@Autowired(required=false)
	private UserService userService;
	@Autowired(required=false)
	private CacheManager myCacheManager;
	

	/**
	 * set操作
	 */
	@Test
	public void testOpsSet() {
		
		redisTemplate.delete("mySet");
		
		/**
		 * 添加值
		 */
		redisTemplate.opsForSet().add("mySet", "value1", "value2", "value3");
		redisTemplate.opsForSet().add("otherSet", "value1", "value4", "value5");

		/**
		 * 获取otherSet中不存在的值
		 */
		// Set<Object> mySet = redisTemplate.opsForSet().difference("mySet",
		// "otherSet"); // return { value2 value3}

		/**
		 * 取差集，并保存到指定key
		 */
		// redisTemplate.opsForSet().differenceAndStore("mySet", "otherSet",
		// "descSet"); // descSet = { value2 value3}

		/**
		 * 获取key中所有值， 用iterator遍历有顺序
		 */
		// Set<Object> mySet = redisTemplate.opsForSet().members("descSet");

		/**
		 * 随机取指定个数值
		 */
		// Set<Object> mySet =
		// redisTemplate.opsForSet().distinctRandomMembers("mySet", 2);
		/**
		 * 获取交集
		 */
		// Set<Object> mySet = redisTemplate.opsForSet().intersect("mySet",
		// "otherSet");
		/**
		 * 获取交集并保存
		 */
		// redisTemplate.opsForSet().intersectAndStore("mySet", "otherSet",
		// "descSet");
		/**
		 * 判断是否存在某个值
		 */
//		logger.info(redisTemplate.opsForSet().isMember("mySet", "value1").toString());

		/**
		 * 移动值到指定key
		 */
		// redisTemplate.opsForSet().move("mySet", "value2", "descSet");

		/**
		 * 随机获取一个值，并且remove掉
		 */
		// logger.info(redisTemplate.opsForSet().pop("mySet").toString());

		/**
		 * 随机获取一个值
		 */
//		logger.info(redisTemplate.opsForSet().randomMember("mySet").toString());
//		/**
//		 * 删除value
//		 */
//		redisTemplate.opsForSet().remove("mySet", "value1");
//		redisTemplate.opsForSet().size("mySet"); // 计算长度
//		redisTemplate.opsForSet().union("mySet", "otherSet"); // 获取并集

		// for(Iterator<Object> it = mySet.iterator(); it.hasNext(); ) {
		// logger.info(it.next().toString());
		// }

		// logger.info(o.toString());
	}

	/**
	 * Map操作
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testOpsHash() {

		redisTemplate.opsForValue().getOperations().multi();

		redisTemplate.delete("myMap");

		Map<String, UserModel> myMap = new HashMap<String, UserModel>();
		myMap.put("second", new UserModel("second"));
		myMap.put("third", new UserModel("third"));

		/* 在map中添加数据 */
		redisTemplate.opsForHash().put("myMap", "first", new UserModel("first")); // 单个添加
		redisTemplate.opsForHash().putAll("myMap", myMap); // 添加map对象
		// redisTemplate.opsForHash().putIfAbsent("myKey", "fisrt", new
		// UserModel("new")); //添加单个数据， 如果key已经存在，则不保存，返回false，实现了并发控制

		redisTemplate.opsForHash().get("myMap",
				"third");
		redisTemplate.opsForHash().entries("myMap");
		redisTemplate.opsForHash().values("myMap");
		redisTemplate.opsForHash().multiGet("myMap",
				(Collection) (Arrays.asList("first", "second")));

		/* 获得map中数据个数 */
		redisTemplate.opsForHash().size("myMap");

		redisTemplate.opsForHash().hasKey("myMap", "first");

		redisTemplate.opsForHash().keys("myMap");

		/* 删除指定key的value， 可同时删除多个key */
		Long ret = redisTemplate.opsForHash().delete("myMap", "first");
		logger.info(ret.toString());

		// redisTemplate.opsForHash().increment("myMap", "count", 3) //
		// 操作value的值，+3

	}

	/**
	 * list操作
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testOpsList() {

		List<UserModel> myList = new ArrayList<UserModel>();
		myList.add(new UserModel("first"));
		myList.add(new UserModel("second"));

		/* 添加单个对象 */
		redisTemplate.opsForList().leftPush("myList", new UserModel("one"));

		/* 一次添加多个对象 */
		redisTemplate.opsForList().leftPushAll("myList", (Collection) myList); // [second,
																				// first,
																				// one]
																				// //
																				// 添加一个list
																				// ，
																				// 需要转成collection
		redisTemplate.opsForList().leftPushAll("myList", new UserModel("third"), new UserModel("fourth")); // 一次添加多个

		/* 在指定对象前插入 */
		redisTemplate.opsForList().leftPush("myList", new UserModel("one"), new UserModel("middle")); // 在one前插入
																										// middle

		/* 非空插入 */
		redisTemplate.opsForList().leftPushIfPresent("myList", new UserModel("new")); // 只有存在mylist这个key，并且list中有值才能操作成功

		/* list间操作 */
		redisTemplate.opsForList().rightPopAndLeftPush("myList", "otherList");

		/* 获得指定位置的对象 */
		redisTemplate.opsForList().index("myList", 0);// list下标

		/**
		 * 获得指定范围内的list param key param start param end -1 表示最后一个，-2表示倒数第二个
		 */
		redisTemplate.opsForList().range("mylist", 0, -1).toString(); // -1表示最后一个

		/**
		 * 更新指定位置的值 param key param index 位置 param value
		 */
		redisTemplate.opsForList().set("myList", 0, new UserModel("update"));

		/* 从左边出栈 返回object对象 */
		redisTemplate.opsForList().leftPop("myList"); // 从左边出栈
		// Object o = redisTemplate.opsForList().leftPop("myList", 10000,
		// TimeUnit.MILLISECONDS); // 设置超时

	}
	
	/**
	 * value操作
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testOpsValue() {

		Map<String, String> myMap = new HashMap<String, String>();

		myMap.put("1", "1");
		myMap.put("2", "2");
		myMap.put("3", "3");
		myMap.put("4", "4");

		// set值
		redisTemplate.opsForValue().set("myValue", "value");
		redisTemplate.opsForValue().setIfAbsent("myValue", "value"); // 只有当
																		// myValue
																		// 不存在的时候才能set成功
		// 一次设置多个键值对
		redisTemplate.opsForValue().multiSet(myMap); //

		// 一次获取多个值
		@SuppressWarnings("rawtypes")
		List<Object> list = redisTemplate.opsForValue().multiGet((Collection) Arrays.asList("1", "2"));

		for (Object o : list) {
			logger.info(o.toString());
		}

	}
	
	
	

	@Test
	public void testTransInThread() throws InterruptedException {

		redisTemplate.delete("myList");
		for (int i = 0; i < 1000; i++) {
			redisTemplate.opsForList().leftPush("myList", "list" + i);
		}

		new Thread() {
			private RedisTemplate<String, Object> redisTemplate = templateFactory.get(0);

			public void run() {
				while (redisTemplate.opsForList().size("myList") > 0) {
					redisTemplate.multi();
					int num = Integer.valueOf(redisTemplate.opsForList().leftPop("myList").toString().substring(4)) % 4;
					if (1 == num) {
						logger.info(Thread.currentThread().getName() + " - get " + num);
						redisTemplate.exec();
					} else {
						// new ClassNotSupportException("num is not support!");
						logger.info("{} is not support!", num);
					}
				}
			}
		}.start();

		Thread.sleep(1000000);

		List<Object> values = templateFactory.get(0).opsForList().range("myList", 0, -1);

		for (Object o : values) {
			logger.info(o.toString());
		}
	}

	@Test
	public void testTrans() {

		redisTemplate.multi();
		for (int i = 0; i < 10; i++) {
			BoundHashOperations<String, String, String> hs = redisTemplate.boundHashOps("zhang");
			hs.put("zhang" + i, "hao" + i);
		}
		redisTemplate.exec();

		// redisTemplate.delete("myValue");
		// redisTemplate.multi();
		// redisTemplate.opsForValue().set("myValue", "vvvvv");
		//// logger.info(redisTemplate.opsForValue().get("myValue").toString());
		//// redisTemplate.exec();
		// redisTemplate.discard();
		// logger.info(redisTemplate.opsForValue().get("myValue").toString());

		//
		// redisTemplate.delete("myList");
		// redisTemplate.opsForList().leftPushAll("myList",
		// (Collection)Arrays.asList(new UserModel("jason"), new
		// UserModel("wenst")));
		// List<Object> values = redisTemplate.opsForList().range("myList", 0,
		// -1);
		//
		// for(Object o : values) {
		// logger.info(o.toString());
		// }
		// try{
		// redisTemplate.multi();
		//
		// redisTemplate.opsForList().leftPop("myList");
		//
		// throw new NullPointerException();
		//
		//
		// } catch(NullPointerException e) {
		// logger.info("catch NullPointerException!");
		// redisTemplate.opsForList().getOperations().exec();
		// }
		//
		// List<Object> values1 = redisTemplate.opsForList().range("myList", 0,
		// -1);
		//
		// for(Object o : values1) {
		// logger.info(o.toString());
		// }
	}

	/**
	 * 测试setNX并发， 多客户端并发set同一个key， 只能set一次
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testSetNX() throws InterruptedException {
		redisTemplate.delete("testNX");

		Thread t1 = new Thread() {
			public void run() {
				while (true)
					logger.info("{} -- {}", Thread.currentThread().getName(), templateFactory.get(0).opsForValue()
							.setIfAbsent("testNX", userService.getAllUsers()).toString());
			}
		};
		Thread t2 = new Thread() {
			public void run() {
				while (true)
					logger.info("{} -- {}", Thread.currentThread().getName(), templateFactory.get(1).opsForValue()
							.setIfAbsent("testNX", userService.getAllUsers()).toString());
			}
		};
		Thread t3 = new Thread() {
			public void run() {
				while (true)
					logger.info("{} -- {}", Thread.currentThread().getName(), templateFactory.get(2).opsForValue()
							.setIfAbsent("testNX", userService.getAllUsers()).toString());
			}
		};
		Thread t4 = new Thread() {
			public void run() {
				while (true)
					logger.info("{} -- {}", Thread.currentThread().getName(), templateFactory.get(3).opsForValue()
							.setIfAbsent("testNX", userService.getAllUsers()).toString());
			}
		};
		Thread t5 = new Thread() {
			public void run() {
				while (true)
					logger.info("{} -- {}", Thread.currentThread().getName(), templateFactory.get(4).opsForValue()
							.setIfAbsent("testNX", userService.getAllUsers()).toString());
			}
		};
		Thread t6 = new Thread() {
			public void run() {
				while (true)
					logger.info("{} -- {}", Thread.currentThread().getName(), templateFactory.get(5).opsForValue()
							.setIfAbsent("testNX", userService.getAllUsers()).toString());
			}
		};

		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();

		Thread.sleep(2000);

		logger.info("delete key testNX");
		redisTemplate.delete("testNX");

		Thread.sleep(100000);
	}

	@Test
	@Ignore
	public void testSyncCall() throws InterruptedException {

		redisTemplate.delete("allUsers");

		CacheThread t1 = new CacheThread(myCacheManager);
		CacheThread t2 = new CacheThread(myCacheManager);
		CacheThread t3 = new CacheThread(myCacheManager);
		CacheThread t4 = new CacheThread(myCacheManager);

		t1.start();
		t2.start();
		t3.start();
		t4.start();

		Thread.sleep(2000);

		logger.info("delete allUsers!");
		redisTemplate.delete("allUsers");

		Thread.sleep(1000000);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testCache() {

		// userRedisTemplate.delete("allUsers");
		// List<Object> it = redisTemplate.opsForList().range("allUsers~keys",
		// 0, -1);
		//
		// for(Object user : it) {
		// logger.info(((UserModel)it).getUsername());
		// }

		// for(UserModel user : userService.getAllUsers()) {
		// redisTemplate.opsForList().leftPush("allUsers", user);
		// }
		redisTemplate.opsForList().leftPushAll("allUsers", (Collection) userService.getAllUsers());
		List<Object> users = redisTemplate.opsForList().range("allUsers", 0, -1); // get
																					// allUsers
																					// from
																					// redis

		logger.info("get from template");
		for (Object user : users) {
			logger.info(((UserModel) user).getUsername());
		}

		// logger.info("get from service");
		// for(UserModel user : userService.getAllUsers()) {
		// logger.info(user.getUsername());
		// }
	}

	@Test
	public void testUserService() throws InterruptedException {

		String username = "wenst";

		redisTemplate.delete(username);

		CallUserService t1 = new CallUserService(userService, username);
		CallUserService t2 = new CallUserService(userService, username);
		CallUserService t3 = new CallUserService(userService, username);
		CallUserService t4 = new CallUserService(userService, username);
		CallUserService t5 = new CallUserService(userService, username);
		CallUserService t6 = new CallUserService(userService, username);

		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();

		logger.info("delete key {}", username);
		redisTemplate.delete(username);

		Thread.sleep(222000);
	}

	@Test
	public void testThread() throws InterruptedException {

		logger.info("gogogogooggo");

		redisTemplate.delete("keyList");

		for (int i = 0; i < 10; i++) {
			redisTemplate.opsForList().leftPush("keyList", "key" + i);
			logger.info("push value {}", "key" + i);
		}
		System.out.println(redisTemplate.opsForList().index("keyList", 0));
		// pop测试
		PopThread t = new PopThread(redisTemplate, "keyList");
		PopThread t1 = new PopThread(redisTemplate, "keyList");
		// PopThread t2 = new PopThread(templateFactory.get(2), "keyList");
		// PopThread t3 = new PopThread(templateFactory.get(3), "keyList");
		t.start();
		t1.start();
		// t2.start();
		// t3.start();
		while (redisTemplate.opsForList().size("keyList") > 0) {
			JedisConnectionFactory factory = (JedisConnectionFactory) redisTemplate.getConnectionFactory();
			System.out.println("config pool = " + ":" + factory.getPoolConfig().getMaxIdle());
			Thread.sleep(1000);
			logger.info("clients = {}", redisTemplate.getConnectionFactory().getConnection().getClientList());
		}

	}

}
