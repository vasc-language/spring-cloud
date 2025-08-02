package spring.cloud.common.utils;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 封装转换JSON工具类
 */
@Slf4j
public class JsonUtils<T> {
    /**
     * 字符串转对象
     */
    public static <T> T parseJson(String json, Class<T> clazz) {
        if (!StringUtils.hasLength(json) || clazz == null) {
            log.warn("JsonUtils parseJson parameter is null or empty, json: {}, clazz: {}", json, clazz);
            return null;
        }
        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            log.error("JsonUtils parseJson error, json: {}, clazz: {}, error: {}", json, clazz.getName(), e.getMessage());
            // 根据业务需求决定异常处理策略：
            // 1. 返回null (当前策略，适用于非关键数据转换)
            // 2. 抛出运行时异常 (适用于关键业务数据)
            // throw new RuntimeException("JSON解析失败", e);
            return null;
        }
    }

    /**
     * 对象转字符串
     */
    public static String toJsonString(Object o) {
        if (o == null) {
            log.debug("JsonUtils toJsonString object is null");
            return null;
        }
        try {
            return JSON.toJSONString(o);
        } catch (Exception e) {
            log.error("JsonUtils toJsonString error, object: {}, type: {}, error: {}", 
                     o, o.getClass().getName(), e.getMessage());
            // 根據業務需求決定異常處理策略：
            // 1. 返回null (當前策略，適用於非關鍵數據轉換)  
            // 2. 返回空字符串 return "";
            // 3. 抛出运行时异常 (适用于关键业务数据)
            // throw new RuntimeException("JSON序列化失败", e);
            return null;
        }
    }
}
