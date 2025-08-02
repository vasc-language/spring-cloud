package spring.cloud.user;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * RabbitMQ 消息队列测试类
 * 用于测试 RabbitMQ 消息的发送功能
 * 
 * 测试场景：
 * - 验证消息能够正确发送到指定队列
 * - 测试 RabbitTemplate 的基本功能
 * 
 * @author SpringCloud Team
 * @version 1.0
 * @since 2024
 */
@SpringBootTest
public class RabbitMQTest {
    
    /**
     * RabbitMQ 消息发送模板
     * Spring AMQP 提供的消息操作模板类，用于简化消息的发送和接收
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试消息发送功能
     * 
     * 发送步骤：
     * 1. 使用 RabbitTemplate 的 convertAndSend 方法
     * 2. 参数说明：
     *    - 第一个参数("")：交换机名称，空字符串表示使用默认交换机
     *    - 第二个参数("hello")：路由键/队列名称
     *    - 第三个参数("hello rabbitmq~")：消息内容
     * 
     * 预期结果：
     * - 消息成功发送到 "hello" 队列
     * - HelloQueueListener 监听器会接收并处理该消息
     */
    @Test
    void send(){
        rabbitTemplate.convertAndSend("", "hello", "hello rabbitmq~");
    }
}
