package spring.cloud.common.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class JWTUtilsTest {

    @Test
    void genJwt() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("name", "zhangsan");
        System.out.println(JWTUtils.genJwt(claims));
        // eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiemhhbmdzYW4iLCJpZCI6MSwiaWF0IjoxNzUzODQ4NzQ1LCJleHAiOjE3NTM4NTA1NDV9.ww_zCL-2LMzqBPTixXv8kwQ4XEo0buPrkdJds6s6ZWQ
    }
}