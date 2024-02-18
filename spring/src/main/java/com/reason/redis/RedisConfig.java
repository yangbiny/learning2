package com.reason.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author impassive
 */
@Configuration
@ComponentScan
public class RedisConfig {

  @Bean
  public RedisTemplate<String, String> redisTemplate(
      LettuceConnectionFactory connectionFactory
  ) {
    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(connectionFactory);
    return redisTemplate;
  }

  @Bean
  public LettuceConnectionFactory connectionFactory() {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
    configuration.setPort(6379);
    configuration.setHostName("127.0.0.1");
    return new LettuceConnectionFactory(configuration);
  }

}
