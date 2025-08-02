package spring.cloud.common.utils;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Optional;

/**
 * 封装发送邮件工具类
 */
public class Mail {
    private JavaMailSender javaMailSender;
    private MailProperties mailProperties;

    public Mail(JavaMailSender javaMailSender, MailProperties mailProperties) {
        this.javaMailSender = javaMailSender;
        this.mailProperties = mailProperties;
    }

    public void send(String to, String subject, String content) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);
        String personal = Optional
                .ofNullable(mailProperties.getProperties().get("personal"))
                .orElse(mailProperties.getUsername());
        helper.setFrom(mailProperties.getUsername(), personal);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        javaMailSender.send(mimeMessage);

    }
}
