package spring.cloud.common.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Redis工具类单元测试
 */
@ExtendWith(MockitoExtension.class)
class RedisTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private ListOperations<String, String> listOperations;

    private Redis redis;

    @BeforeEach
    void setUp() {
        redis = new Redis(redisTemplate);
    }

    @Test
    void testGet_Success() {
        String key = "test:key";
        String expectedValue = "test:value";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(expectedValue);

        String result = redis.get(key);

        assertEquals(expectedValue, result);
        verify(valueOperations).get(key);
    }

    @Test
    void testGet_NullKey() {
        String result = redis.get(null);
        assertNull(result);
    }

    @Test
    void testGet_Exception() {
        String key = "test:key";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenThrow(new RuntimeException("Redis连接异常"));

        String result = redis.get(key);

        assertNull(result);
    }

    @Test
    void testHasKey_Success() {
        String key = "test:key";
        when(redisTemplate.hasKey(key)).thenReturn(true);

        boolean result = redis.hasKey(key);

        assertTrue(result);
        verify(redisTemplate).hasKey(key);
    }

    @Test
    void testHasKey_NullKey() {
        boolean result = redis.hasKey(null);
        assertFalse(result);
    }

    @Test
    void testSet_Success() {
        String key = "test:key";
        String value = "test:value";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        boolean result = redis.set(key, value);

        assertTrue(result);
        verify(valueOperations).set(key, value);
    }

    @Test
    void testSet_NullKey() {
        boolean result = redis.set(null, "value");
        assertFalse(result);
    }

    @Test
    void testSet_EmptyKey() {
        boolean result = redis.set("", "value");
        assertFalse(result);
    }

    @Test
    void testSet_NullValue() {
        boolean result = redis.set("key", null);
        assertFalse(result);
    }

    @Test
    void testSetWithTimeout_Success() {
        String key = "test:key";
        String value = "test:value";
        long timeout = 60;
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        boolean result = redis.set(key, value, timeout);

        assertTrue(result);
        verify(valueOperations).set(key, value, timeout, TimeUnit.SECONDS);
    }

    @Test
    void testSetWithTimeout_NegativeTimeout() {
        boolean result = redis.set("key", "value", -1);
        assertFalse(result);
    }

    @Test
    void testSetWithTimeout_ZeroTimeout() {
        boolean result = redis.set("key", "value", 0);
        assertFalse(result);
    }

    @Test
    void testDelete_Success() {
        String key = "test:key";
        when(redisTemplate.delete(key)).thenReturn(true);

        boolean result = redis.delete(key);

        assertTrue(result);
        verify(redisTemplate).delete(key);
    }

    @Test
    void testBuildKey_WithPrefix() {
        String result = redis.buildKey("user", "123", "profile");
        assertEquals("user:123:profile", result);
    }

    @Test
    void testBuildKey_NullPrefix() {
        String result = redis.buildKey(null, "123", "profile");
        assertEquals("default:123:profile", result);
    }

    @Test
    void testBuildKey_NoArgs() {
        String result = redis.buildKey("user");
        assertEquals("user", result);
    }
}