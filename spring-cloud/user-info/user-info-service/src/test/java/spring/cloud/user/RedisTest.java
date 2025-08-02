package spring.cloud.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

/**
 * Created with IntelliJ IDEA.
 * Description: Redis测试类，仅在Redis服务可用时运行
 * User: 姚东名
 * Date: 2025-08-01
 * Time: 7:42
 */
@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void test(){
        redisTemplate.opsForValue().set("hello", "spring");
        String val = redisTemplate.opsForValue().get("hello");
        System.out.println(val);
    }
}
