package spring.cloud.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import spring.cloud.common.utils.Redis;

@Configuration
public class RedisConfig {
    @Bean
    @ConditionalOnProperty(prefix = "spring.data.redis", name = "host")
    public Redis redis(StringRedisTemplate redisTemplate){
        return new Redis(redisTemplate);
    }
}
