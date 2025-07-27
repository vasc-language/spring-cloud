package ai.agent.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义网关过滤器工厂
 * 
 * 这个类实现了Spring Cloud Gateway的自定义过滤器功能，用于在请求处理的前后执行特定的逻辑。
 * 继承AbstractGatewayFilterFactory可以创建可配置的网关过滤器。
 * 
 * 主要功能：
 * 1. 在请求到达目标服务之前执行预处理逻辑（Pre Filter）
 * 2. 在响应返回客户端之前执行后处理逻辑（Post Filter）
 * 3. 支持通过配置文件进行参数配置
 * 
 * @author AI Agent
 */
@Slf4j
@Component
public class CustomGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomConfig> implements Ordered {
    
    /**
     * 构造函数
     * 调用父类构造函数并传入配置类，用于绑定YAML配置文件中的参数
     */
    public CustomGatewayFilterFactory() {
        super(CustomConfig.class);
    }

    /**
     * 应用过滤器的核心方法
     * 
     * 这个方法根据传入的配置创建并返回一个GatewayFilter实例。
     * 该过滤器会在每个匹配的请求上执行。
     * 
     * @param config 从YAML配置文件中读取的配置参数对象
     * @return 配置好的GatewayFilter实例
     */
    @Override
    public GatewayFilter apply(CustomConfig config) {
        // 返回一个匿名GatewayFilter实现
        return new GatewayFilter() {
            /**
             * 过滤器的执行逻辑
             * 
             * 这个方法实现了请求处理的核心逻辑，分为三个阶段：
             * 1. Pre阶段：在请求转发到目标服务之前执行
             * 2. 请求转发：调用chain.filter()将请求传递给下游服务
             * 3. Post阶段：在收到响应后、返回给客户端之前执行
             * 
             * @param exchange 包含HTTP请求和响应信息的对象
             * @param chain 过滤器链，用于将请求传递给下一个过滤器或目标服务
             * @return Mono<Void> 响应式编程的返回类型，表示异步操作的完成
             */
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                // === Pre Filter阶段 ===
                // 在请求转发到目标服务之前执行的逻辑
                // 可以在这里进行：请求参数修改、请求头添加、身份验证、请求日志记录等
                log.info("Pre Filter, config:{} ", config);  // 记录预处理日志和配置信息
                
                // === 请求转发 + Post Filter阶段 ===
                // chain.filter(exchange): 将请求传递给过滤器链的下一个环节或目标服务
                // .then(): 在前面的操作完成后执行后续操作
                // Mono.fromRunnable(): 将同步代码块转换为响应式操作
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    // === Post Filter阶段 ===
                    // 在收到目标服务响应后、返回给客户端之前执行的逻辑
                    // 可以在这里进行：响应头修改、响应内容处理、响应日志记录、性能统计等
                    log.info("Post Filter....");  // 记录后处理日志
                }));
            }
        };
    }

    /**
     * 获取过滤器的执行顺序
     * 
     * 当存在多个过滤器时，Spring会根据Order值来确定执行顺序。
     * 数值越小，优先级越高，越早执行。
     * 
     * @return 过滤器的执行顺序值
     */
    @Override
    public int getOrder() {
        // LOWEST_PRECEDENCE表示最低优先级，即最后执行
        // 这意味着这个过滤器会在其他过滤器之后执行
        return Ordered.LOWEST_PRECEDENCE;
    }
}
