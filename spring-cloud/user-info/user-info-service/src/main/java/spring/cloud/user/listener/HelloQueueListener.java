package spring.cloud.user.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Hello 队列消息监听器
 * 负责监听和处理来自 "hello" 队列的消息
 * 
 * 功能说明：
 * - 自动监听指定队列的消息
 * - 接收到消息后进行相应的业务处理
 * - 支持消息的自动确认机制
 * 
 * @author SpringCloud Team
 * @version 1.0
 * @since 2024
 */
@Component
public class HelloQueueListener {
    
    /**
     * 处理来自 "hello" 队列的消息
     * 
     * 监听配置：
     * - 队列名称：hello（与 RabbitConfig 中配置的队列名称对应）
     * - 监听模式：自动监听，有消息时自动触发
     * - 确认模式：默认自动确认（方法执行完成后自动确认消息已处理）
     * 
     * 处理流程：
     * 1. 接收到队列中的消息
     * 2. 将消息内容打印到控制台
     * 3. 方法执行完成后自动确认消息处理完毕
     * 
     * @param message 接收到的消息对象，包含消息体、属性、头信息等
     */
    @RabbitListener(queues = "hello")
    public void handler(Message message){
        System.out.println("收到消息:" + message);
    }
}
