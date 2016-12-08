package configure;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Profile("local")
@Configuration
public class LocalRedisConfigure {
	
	
	@Bean
	RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
//		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setEnableTransactionSupport(true);
		return redisTemplate;
	}
	

	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {
		
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(128);
		poolConfig.setJmxEnabled(true);
		poolConfig.setMaxTotal(8);
		poolConfig.setMinIdle(2);
		
		
		JedisConnectionFactory factory = new JedisConnectionFactory(poolConfig);
		factory.setHostName("localhost");
		factory.setPort(6379);
		factory.setUsePool(true);

		return factory;
	}
	

	@Bean
	CacheManager cacheManager() {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
		cacheManager.setDefaultExpiration(86400);
		return cacheManager;
	}
}
