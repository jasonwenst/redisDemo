package configure;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Profile("ha")
@Configuration
@PropertySource("classpath:redis-ha.properties")
public class HaRedisConfigure {
	
	@SuppressWarnings("unused")
	@Autowired
	private Environment environment;
	
	@Bean
	RedisTemplate<String, Object> redisTemplate() throws IOException {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
//		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setEnableTransactionSupport(true);
		return redisTemplate;
	}
	
	@Bean
	public RedisConnectionFactory jedisConnectionFactory() throws IOException {
		
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(128);
		poolConfig.setJmxEnabled(true);
		poolConfig.setMaxTotal(8);
		poolConfig.setMinIdle(2);
		
		RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration(new ResourcePropertySource("resource", "classpath:ha.properties"));
		JedisConnectionFactory factory = new JedisConnectionFactory(sentinelConfig, poolConfig);

		return factory;
	}

}
