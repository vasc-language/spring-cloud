package spring.cloud.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.cloud.common.utils.Redis;

/**
 * 测试在 blog-common 包中的 Redis Bean 在 user-info model 中是否能使用
 */
@SpringBootTest
public class RedisClientTest {
    @Autowired
    private Redis redis;

    @Test
    public void Test() {
        redis.set("key5", "value5");
    }
}
