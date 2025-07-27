package ai.agent.order.config;

import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FeignConfig {

    @Bean
    public Retryer feignRetryer() {
        // 重试配置：初始间隔100ms，最大间隔1秒，最大重试次数3次
        return new Retryer.Default(100, 1000, 3);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    public static class CustomErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultErrorDecoder = new Default();

        @Override
        public Exception decode(String methodKey, feign.Response response) {
            log.error("Feign调用失败 - 方法: {}, 状态码: {}, 原因: {}", 
                     methodKey, response.status(), response.reason());
            
            switch (response.status()) {
                case 500:
                    return new RuntimeException("product-service 内部服务器错误");
                case 404:
                    return new RuntimeException("product-service 接口未找到");
                case 503:
                    return new RuntimeException("product-service 服务不可用");
                default:
                    return defaultErrorDecoder.decode(methodKey, response);
            }
        }
    }
}