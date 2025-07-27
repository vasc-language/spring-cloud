package ai.agent.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局路由过滤器工厂
 */
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    /**
     * ServerWebExchange: HTTP 请求-响应交互契约，提供了对HTTP请求和响应的访问
     * GatewayFilterChain：过滤器链
     * Mono：Reactor 的核心类，数据流发布者，Mono 最多只能触发一个事件，可以把 Mono 用于异步完成任务时，发出通知
     * chain.filter(exchange)：执行请求
     * Mono.fromRunnable()：创建一个包含 Runnable 元素的数据流
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Pre Global Filter");
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            log.info("Post Global Filter~");
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
