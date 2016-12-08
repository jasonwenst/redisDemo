package configure;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Profile("cluster")
@Configuration
@PropertySource(value = "classpath:cluster.properties")
public class ClusterRedisConfigure {
	
	@Autowired
	private Environment environment;
	
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
		
		RedisClusterConfiguration conf = new RedisClusterConfiguration(Arrays.asList(environment.getProperty("spring.redis.cluster.nodes").split(",")));
		conf.setMaxRedirects(1000);
		JedisConnectionFactory factory = new JedisConnectionFactory(conf, poolConfig);
		
		factory.setUsePool(true);

		return factory;
	}
	

}
