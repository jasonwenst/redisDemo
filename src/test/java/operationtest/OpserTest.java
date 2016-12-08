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
	 * set����
	 */
	@Test
	public void testOpsSet() {
		
		redisTemplate.delete("mySet");
		
		/**
		 * ���ֵ
		 */
		redisTemplate.opsForSet().add("mySet", "value1", "value2", "value3");
		redisTemplate.opsForSet().add("otherSet", "value1", "value4", "value5");

		/**
		 * ��ȡotherSet�в����ڵ�ֵ
		 */
		// Set<Object> mySet = redisTemplate.opsForSet().difference("mySet",
		// "otherSet"); // return { value2 value3}

		/**
		 * ȡ��������浽ָ��key
		 */
		// redisTemplate.opsForSet().differenceAndStore("mySet", "otherSet",
		// "descSet"); // descSet = { value2 value3}

		/**
		 * ��ȡkey������ֵ�� ��iterator������˳��
		 */
		// Set<Object> mySet = redisTemplate.opsForSet().members("descSet");

		/**
		 * ���ȡָ������ֵ
		 */
		// Set<Object> mySet =
		// redisTemplate.opsForSet().distinctRandomMembers("mySet", 2);
		/**
		 * ��ȡ����
		 */
		// Set<Object> mySet = redisTemplate.opsForSet().intersect("mySet",
		// "otherSet");
		/**
		 * ��ȡ����������
		 */
		// redisTemplate.opsForSet().intersectAndStore("mySet", "otherSet",
		// "descSet");
		/**
		 * �ж��Ƿ����ĳ��ֵ
		 */
//		logger.info(redisTemplate.opsForSet().isMember("mySet", "value1").toString());

		/**
		 * �ƶ�ֵ��ָ��key
		 */
		// redisTemplate.opsForSet().move("mySet", "value2", "descSet");

		/**
		 * �����ȡһ��ֵ������remove��
		 */
		// logger.info(redisTemplate.opsForSet().pop("mySet").toString());

		/**
		 * �����ȡһ��ֵ
		 */
//		logger.info(redisTemplate.opsForSet().randomMember("mySet").toString());
//		/**
//		 * ɾ��value
//		 */
//		redisTemplate.opsForSet().remove("mySet", "value1");
//		redisTemplate.opsForSet().size("mySet"); // ���㳤��
//		redisTemplate.opsForSet().union("mySet", "otherSet"); // ��ȡ����

		// for(Iterator<Object> it = mySet.iterator(); it.hasNext(); ) {
		// logger.info(it.next().toString());
		// }

		// logger.info(o.toString());
	}

	/**
	 * Map����
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testOpsHash() {

		redisTemplate.opsForValue().getOperations().multi();

		redisTemplate.delete("myMap");

		Map<String, UserModel> myMap = new HashMap<String, UserModel>();
		myMap.put("second", new UserModel("second"));
		myMap.put("third", new UserModel("third"));

		/* ��map��������� */
		redisTemplate.opsForHash().put("myMap", "first", new UserModel("first")); // �������
		redisTemplate.opsForHash().putAll("myMap", myMap); // ���map����
		// redisTemplate.opsForHash().putIfAbsent("myKey", "fisrt", new
		// UserModel("new")); //��ӵ������ݣ� ���key�Ѿ����ڣ��򲻱��棬����false��ʵ���˲�������

		redisTemplate.opsForHash().get("myMap",
				"third");
		redisTemplate.opsForHash().entries("myMap");
		redisTemplate.opsForHash().values("myMap");
		redisTemplate.opsForHash().multiGet("myMap",
				(Collection) (Arrays.asList("first", "second")));

		/* ���map�����ݸ��� */
		redisTemplate.opsForHash().size("myMap");

		redisTemplate.opsForHash().hasKey("myMap", "first");

		redisTemplate.opsForHash().keys("myMap");

		/* ɾ��ָ��key��value�� ��ͬʱɾ�����key */
		Long ret = redisTemplate.opsForHash().delete("myMap", "first");
		logger.info(ret.toString());

		// redisTemplate.opsForHash().increment("myMap", "count", 3) //
		// ����value��ֵ��+3

	}

	/**
	 * list����
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testOpsList() {

		List<UserModel> myList = new ArrayList<UserModel>();
		myList.add(new UserModel("first"));
		myList.add(new UserModel("second"));

		/* ��ӵ������� */
		redisTemplate.opsForList().leftPush("myList", new UserModel("one"));

		/* һ����Ӷ������ */
		redisTemplate.opsForList().leftPushAll("myList", (Collection) myList); // [second,
																				// first,
																				// one]
																				// //
																				// ���һ��list
																				// ��
																				// ��Ҫת��collection
		redisTemplate.opsForList().leftPushAll("myList", new UserModel("third"), new UserModel("fourth")); // һ����Ӷ��

		/* ��ָ������ǰ���� */
		redisTemplate.opsForList().leftPush("myList", new UserModel("one"), new UserModel("middle")); // ��oneǰ����
																										// middle

		/* �ǿղ��� */
		redisTemplate.opsForList().leftPushIfPresent("myList", new UserModel("new")); // ֻ�д���mylist���key������list����ֵ���ܲ����ɹ�

		/* list����� */
		redisTemplate.opsForList().rightPopAndLeftPush("myList", "otherList");

		/* ���ָ��λ�õĶ��� */
		redisTemplate.opsForList().index("myList", 0);// list�±�

		/**
		 * ���ָ����Χ�ڵ�list param key param start param end -1 ��ʾ���һ����-2��ʾ�����ڶ���
		 */
		redisTemplate.opsForList().range("mylist", 0, -1).toString(); // -1��ʾ���һ��

		/**
		 * ����ָ��λ�õ�ֵ param key param index λ�� param value
		 */
		redisTemplate.opsForList().set("myList", 0, new UserModel("update"));

		/* ����߳�ջ ����object���� */
		redisTemplate.opsForList().leftPop("myList"); // ����߳�ջ
		// Object o = redisTemplate.opsForList().leftPop("myList", 10000,
		// TimeUnit.MILLISECONDS); // ���ó�ʱ

	}
	
	/**
	 * value����
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testOpsValue() {

		Map<String, String> myMap = new HashMap<String, String>();

		myMap.put("1", "1");
		myMap.put("2", "2");
		myMap.put("3", "3");
		myMap.put("4", "4");

		// setֵ
		redisTemplate.opsForValue().set("myValue", "value");
		redisTemplate.opsForValue().setIfAbsent("myValue", "value"); // ֻ�е�
																		// myValue
																		// �����ڵ�ʱ�����set�ɹ�
		// һ�����ö����ֵ��
		redisTemplate.opsForValue().multiSet(myMap); //

		// һ�λ�ȡ���ֵ
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
	 * ����setNX������ ��ͻ��˲���setͬһ��key�� ֻ��setһ��
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
		// pop����
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
