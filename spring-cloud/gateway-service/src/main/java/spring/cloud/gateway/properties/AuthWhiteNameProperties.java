package spring.cloud.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 白名单配置，，网关不校验此处白名单的 url
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties("auth.white")
public class AuthWhiteNameProperties {
    private List<String> url;
}
