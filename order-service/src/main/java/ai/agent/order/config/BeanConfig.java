package ai.agent.order.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {
    @Bean
    @LoadBalanced // spring-cloud 自带的负载均衡器
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
