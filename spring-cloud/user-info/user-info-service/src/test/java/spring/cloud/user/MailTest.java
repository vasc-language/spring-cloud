package spring.cloud.user;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@SpringBootTest
public class MailTest {
    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    void send() throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);
        helper.setFrom("2072855200@qq.com", "比特博客社区");
        helper.setTo("zrt3ljnygz@163.com");
        helper.setSubject("测试邮件发送");
        helper.setText("<h1>用户注册成功</h1>", true);

        javaMailSender.send(mimeMessage);

    }
}