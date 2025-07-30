package spring.cloud.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import spring.cloud.common.pojo.Result;
import spring.cloud.common.utils.JWTUtils;

import java.util.List;

/**
 * 过滤器
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private ObjectMapper objectMapper;

//    @Autowired
//    private AuthWhiteName authWhiteName;

    private List<String> authWhiteName = List.of("/user/login", "/user/register");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //配置白名单, 如果请求url为白名单, 则放行
        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();
        if (match(path, authWhiteName)) {
            //放行
            return chain.filter(exchange);
        }

        //从header中获取token
        String userToken = request.getHeaders().getFirst("user_token");
        log.info("从header中获取token:{}", userToken);
        if (!StringUtils.hasLength(userToken)) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }

        //验证用户token
        Claims claims = JWTUtils.parseJWT(userToken);
        if (claims == null) {
            log.info("令牌验证失败");
            return unauthorizedResponse(exchange, "令牌过期或者不合法!");
        }
        return chain.filter(exchange);
    }

    private boolean match(String path, List<String> url) {
        if (url == null || url.size() == 0) {
            return false;
        }
        return url.contains(path);
    }

    @SneakyThrows
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String errMsg) {
        log.error("[用户认证失败], url: {}", exchange.getRequest().getURI().getPath());

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        Result result = Result.fail(errMsg);
        DataBuffer dataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(result));
        return response.writeWith(Mono.just(dataBuffer));
    }

    @Override
    public int getOrder() {
        return -200;   //order 值越小, 优先级越高
    }
}

