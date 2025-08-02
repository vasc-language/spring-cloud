package spring.cloud.user.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.cloud.common.constant.Constants;

/**
 * RabbitMQ 配置类
 * 负责定义和配置消息队列相关的 Bean
 *
 * @author SpringCloud Team
 * @version 1.0
 * @since 2024
 */
@Configuration
public class RabbitConfig {

    /**
     * 创建名为 "hello" 的持久化队列
     * <p>
     * 队列特性：
     * - 队列名称：hello
     * - 持久化：true（服务器重启后队列仍然存在）
     * - 排他性：false（多个连接可以访问）
     * - 自动删除：false（没有消费者时不会自动删除）
     *
     * @return Queue 返回配置好的队列实例
     */
    @Bean("hello")
    public Queue queue() {
        return QueueBuilder.durable("hello").build();
    }

//    *
//     * 定义queue Bean
//
//    @Bean("userQueue")
//    public Queue userQueue() {
//        return QueueBuilder.durable(Constants.USER_QUEUE_NAME).build();
//    }
//
//    *
//     * 定义exchange Bean
//
//    @Bean("userExchange")
//    public FanoutExchange userExchange() {
//        return ExchangeBuilder.fanoutExchange(Constants.USER_EXCHANGE_NAME).durable(true).build();
//    }
//
//    *
//     * 定义Binding Bean
//     * 使用广播模式Publish/Subscribe来对交换机和队列进行绑定
//
//    @Bean("userBinding")
//    public Binding userBinding(@Qualifier("userQueue") Queue userQueue, @Qualifier("userExchange") FanoutExchange userExchange) {
//        return BindingBuilder.bind(userQueue).to(userExchange);
//    }

}
