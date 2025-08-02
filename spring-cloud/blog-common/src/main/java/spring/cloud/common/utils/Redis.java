package spring.cloud.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类，对 RedisTemplate 进行二次封装
 */
@Slf4j
public class Redis {
    private StringRedisTemplate redisTemplate;

    private static final String REDIS_SPLIT = ":";
    private static final String REDIS_DEFAULT_PREFIX = "default";

    public Redis(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * redis get()
     */
    public String get(String key) {
        try {
            return key == null ? null : redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("redis get() has error: {}", e);
            return null;
        }
    }

    /**
     * redis hasKey()
     */
    public boolean hasKey(String key) {
        try {
            return key == null ? false : redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("redis hasKey() has error: {}", e);
            return false;
        }
    }

    /**
     * redis set()
     */
    public boolean set(String key, String value) {
        try {
            if (key == null || key.trim().isEmpty()) {
                log.warn("redis set() key is null or empty");
                return false;
            }
            if (value == null) {
                log.warn("redis set() value is null, key:{}", key);
                return false;
            }
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis set() error, key:{}, value:{}, e:{}", key, value, e);
            return false;
        }
    }

    /**
     * redis set()
     * 设置过期时间，单位为 second
     */
    public boolean set(String key, String value, long timeout) {
        try {
            if (key == null || key.trim().isEmpty()) {
                log.warn("redis set() key is null or empty");
                return false;
            }
            if (value == null) {
                log.warn("redis set() value is null, key:{}", key);
                return false;
            }
            if (timeout <= 0) {
                log.warn("redis set() timeout must be positive, key:{}, timeout:{}", key, timeout);
                return false;
            }
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("redis set() error, key:{}, value:{}, timeout:{}, e:{}", key, value, timeout, e);
            return false;
        }
    }

    /**
     * 删除单个key
     */
    public boolean delete(String key) {
        try {
            if (key == null || key.trim().isEmpty()) {
                log.warn("redis delete() key is null or empty");
                return false;
            }
            return Boolean.TRUE.equals(redisTemplate.delete(key));
        } catch (Exception e) {
            log.error("redis delete() error, key:{}, e:{}", key, e);
            return false;
        }
    }

    /**
     * 删除多个key
     */
    public long delete(Collection<String> keys) {
        try {
            if (keys == null || keys.isEmpty()) {
                log.warn("redis delete() keys is null or empty");
                return 0;
            }
            Long deleted = redisTemplate.delete(keys);
            return deleted != null ? deleted : 0;
        } catch (Exception e) {
            log.error("redis delete() error, keys:{}, e:{}", keys, e);
            return 0;
        }
    }

    /**
     * 设置key的过期时间
     */
    public boolean expire(String key, long timeout) {
        try {
            if (key == null || key.trim().isEmpty()) {
                log.warn("redis expire() key is null or empty");
                return false;
            }
            if (timeout <= 0) {
                log.warn("redis expire() timeout must be positive, key:{}, timeout:{}", key, timeout);
                return false;
            }
            return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, TimeUnit.SECONDS));
        } catch (Exception e) {
            log.error("redis expire() error, key:{}, timeout:{}, e:{}", key, timeout, e);
            return false;
        }
    }

    /**
     * 获取key的过期时间(秒)
     */
    public long getExpire(String key) {
        try {
            if (key == null || key.trim().isEmpty()) {
                log.warn("redis getExpire() key is null or empty");
                return -2;
            }
            Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            return expire != null ? expire : -2;
        } catch (Exception e) {
            log.error("redis getExpire() error, key:{}, e:{}", key, e);
            return -2;
        }
    }

    /**
     * 递增操作
     */
    public long increment(String key) {
        try {
            if (key == null || key.trim().isEmpty()) {
                log.warn("redis increment() key is null or empty");
                return 0;
            }
            Long result = redisTemplate.opsForValue().increment(key);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("redis increment() error, key:{}, e:{}", key, e);
            return 0;
        }
    }

    /**
     * 递增指定步长
     */
    public long increment(String key, long delta) {
        try {
            if (key == null || key.trim().isEmpty()) {
                log.warn("redis increment() key is null or empty");
                return 0;
            }
            Long result = redisTemplate.opsForValue().increment(key, delta);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("redis increment() error, key:{}, delta:{}, e:{}", key, delta, e);
            return 0;
        }
    }

    /**
     * 递减操作
     */
    public long decrement(String key) {
        try {
            if (key == null || key.trim().isEmpty()) {
                log.warn("redis decrement() key is null or empty");
                return 0;
            }
            Long result = redisTemplate.opsForValue().decrement(key);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("redis decrement() error, key:{}, e:{}", key, e);
            return 0;
        }
    }

    /**
     * 获取匹配的keys
     */
    public Set<String> keys(String pattern) {
        try {
            if (pattern == null || pattern.trim().isEmpty()) {
                log.warn("redis keys() pattern is null or empty");
                return Set.of();
            }
            return redisTemplate.keys(pattern);
        } catch (Exception e) {
            log.error("redis keys() error, pattern:{}, e:{}", pattern, e);
            return Set.of();
        }
    }

    /**
     * List操作 - 左侧推入
     */
    public long leftPush(String key, String value) {
        try {
            if (key == null || key.trim().isEmpty()) {
                log.warn("redis leftPush() key is null or empty");
                return 0;
            }
            if (value == null) {
                log.warn("redis leftPush() value is null, key:{}", key);
                return 0;
            }
            Long result = redisTemplate.opsForList().leftPush(key, value);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("redis leftPush() error, key:{}, value:{}, e:{}", key, value, e);
            return 0;
        }
    }

    /**
     * List操作 - 右侧推入
     */
    public long rightPush(String key, String value) {
        try {
            if (key == null || key.trim().isEmpty()) {
                log.warn("redis rightPush() key is null or empty");
                return 0;
            }
            if (value == null) {
                log.warn("redis rightPush() value is null, key:{}", key);
                return 0;
            }
            Long result = redisTemplate.opsForList().rightPush(key, value);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("redis rightPush() error, key:{}, value:{}, e:{}", key, value, e);
            return 0;
        }
    }

    /**
     * List操作 - 左侧弹出
     */
    public String leftPop(String key) {
        try {
            if (key == null || key.trim().isEmpty()) {
                log.warn("redis leftPop() key is null or empty");
                return null;
            }
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            log.error("redis leftPop() error, key:{}, e:{}", key, e);
            return null;
        }
    }

    /**
     * List操作 - 获取列表长度
     */
    public long listSize(String key) {
        try {
            if (key == null || key.trim().isEmpty()) {
                log.warn("redis listSize() key is null or empty");
                return 0;
            }
            Long size = redisTemplate.opsForList().size(key);
            return size != null ? size : 0;
        } catch (Exception e) {
            log.error("redis listSize() error, key:{}, e:{}", key, e);
            return 0;
        }
    }

    /**
     * List操作 - 获取指定范围的元素
     */
    public List<String> listRange(String key, long start, long end) {
        try {
            if (key == null || key.trim().isEmpty()) {
                log.warn("redis listRange() key is null or empty");
                return List.of();
            }
            List<String> result = redisTemplate.opsForList().range(key, start, end);
            return result != null ? result : List.of();
        } catch (Exception e) {
            log.error("redis listRange() error, key:{}, start:{}, end:{}, e:{}", key, start, end, e);
            return List.of();
        }
    }

    /**
     * 设置 Key 的形式
     * 1 zhangsan    user:1:zhangsan
     * prefix: user
     * 1 zhangsan: args
     */
    public String buildKey(String prefix, String... args) {
        if (prefix == null) {
            prefix = REDIS_DEFAULT_PREFIX;
        }
        StringBuffer key = new StringBuffer();
        key.append(prefix);
        for (String arg : args) {
            key.append(REDIS_SPLIT).append(arg);
        }
        return key.toString();
    }

}
