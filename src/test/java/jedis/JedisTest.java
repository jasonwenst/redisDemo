package jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import entity.AcctBalance;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

public class JedisTest {

	private JedisPool pool = null;
	private Jedis jedis = null;
	private RedisSerializer<Object> defaultSerializer = new JdkSerializationRedisSerializer();
	private JedisPoolConfig config = null;
	private JedisSentinelPool sentinelPool = null;

	@Before
	public void setUp() {
		config = new JedisPoolConfig();
		pool = new JedisPool();
		 jedis = pool.getResource();

		HashSet<String> set = new HashSet<String>();
		set.add("10.10.13.172:26379");
		set.add("10.10.13.174:26379");
		sentinelPool = new JedisSentinelPool("mymaster", set);
		jedis = sentinelPool.getResource();
	}

	@After
	public void cleanUp() {
		if (jedis != null) {
			jedis.close();
		}
		if (pool != null) {
			pool.close();
		}
	}

	@Test
	public void jedisTest() {

		AcctBalance acc = new AcctBalance();

		acc.setAcctId(12l);
		acc.setState(2);

		jedis.hset(rawKey("myMap"), rawHashKey("one"), rawHashValue(acc));

		AcctBalance value = deserializeHashValue(jedis.hget(rawKey("myMap"), rawHashKey("one")));

		System.out.println(value.getAcctId());

	}

	@Test
	public void testCluster() {

		AcctBalance acc = new AcctBalance();

		acc.setAcctId(12l);
		acc.setState(2);

		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		jedisClusterNodes.add(new HostAndPort("10.10.13.174", 7001));
		jedisClusterNodes.add(new HostAndPort("10.10.13.172", 7005));
		jedisClusterNodes.add(new HostAndPort("10.10.13.174", 7002));
		@SuppressWarnings("resource")
		JedisCluster jc = new JedisCluster(jedisClusterNodes, config);

		jc.hset(rawKey("myMap"), rawHashKey("one"), rawHashValue(acc));

		AcctBalance value = deserializeHashValue(jc.hget(rawKey("myMap"), rawHashKey("one")));

		System.out.println(value.toString());
	}

	<HV> byte[] rawHashValue(HV value) {
		if (hashValueSerializer() == null & value instanceof byte[]) {
			return (byte[]) value;
		}
		return hashValueSerializer().serialize(value);

	}

	<HK> byte[] rawHashKey(HK hashKey) {
		Assert.notNull(hashKey, "non null hash key required");
		if (hashKeySerializer() == null && hashKey instanceof byte[]) {
			return (byte[]) hashKey;
		}
		return hashKeySerializer().serialize(hashKey);
	}

	byte[] rawKey(Object key) {
		Assert.notNull(key, "non null key required");
		if (keySerializer() == null && key instanceof byte[]) {
			return (byte[]) key;
		}
		return keySerializer().serialize(key);
	}

	@SuppressWarnings("unchecked")
	<HV> HV deserializeHashValue(byte[] value) {
		if (hashValueSerializer() == null) {
			return (HV) value;
		}
		return (HV) hashValueSerializer().deserialize(value);
	}

	public RedisSerializer<Object> hashValueSerializer() {
		return defaultSerializer;
	}

	public RedisSerializer<Object> hashKeySerializer() {
		return defaultSerializer;
	}

	public RedisSerializer<Object> keySerializer() {
		return defaultSerializer;
	}
}
