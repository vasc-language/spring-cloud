package spring.cloud.user.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.cloud.common.constant.Constants;
import spring.cloud.common.utils.JsonUtils;
import spring.cloud.common.utils.Mail;
import spring.cloud.user.dataobject.UserInfo;

import java.io.IOException;

/**
 * 监听user rabbitmq 发送到消费者中
 */
@Slf4j
@Component
public class UserQueueListener {
    @Autowired
    private Mail mail;
    /*@RabbitListener(queues = Constants.USER_QUEUE_NAME)
    public void handler(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String body = new String(message.getBody());
            log.info("接收消息：{}", body);
            // TODO 发送邮件
            // 手动确定消息
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            // 手动不确定消息
            channel.basicNack(deliveryTag, true, true);
            log.error("发送邮件失败：", e);
        }
    }*/

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = Constants.USER_QUEUE_NAME, durable = "true"),
            exchange = @Exchange(value = Constants.USER_EXCHANGE_NAME, type = ExchangeTypes.FANOUT)
    ))
    public void handler(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String body = new String(message.getBody());
            log.info("接收消息：{}", body);
            // TODO 发送邮件
            UserInfo userInfo = JsonUtils.parseJson(body, UserInfo.class);
            mail.send(userInfo.getEmail(), "恭喜加入比特博客社区", buildContent(userInfo.getUserName()));
            // 手动确定消息
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            // 手动不确定消息
            channel.basicNack(deliveryTag, true, true);
            log.error("发送邮件失败：", e);
        }
    }

    private String buildContent(String userName){
        StringBuilder builder = new StringBuilder();
        builder.append("尊敬的").append(userName).append(", 您好!").append("<br/>");
        builder.append("感谢您注册成为我们博客社区的一员! 我们很高新您加入我们的大家庭!<br/>");
        builder.append("您的注册信息如下: 用户名: ").append(userName).append("<br/>");
        builder.append("为了确保您的账⼾安全，请妥善保管您的登录信息. 如果使⽤过程中, 遇到任何问题, 欢迎联系我们的⽀持团队. XXXX@bite.com <br/>");
        builder.append("再次感谢您的加⼊，我们期待看到您的精彩内容！<br/>")
                .append("最好的祝愿<br/>")
                .append("⽐特博客团队").toString();
        return builder.toString();
    }
}
