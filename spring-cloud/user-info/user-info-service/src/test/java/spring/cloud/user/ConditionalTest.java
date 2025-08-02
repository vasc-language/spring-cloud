package spring.cloud.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.system.JavaVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 测试 @Conditional
 */
@SpringBootTest
public class ConditionalTest {
    @Test
    void Test() {
        System.out.println("执行 Test() 方法");
    }
}

@Configuration
class AppConfig {
    // 如果 JDK17Conditional.matches 返回 true，则注册该Bean
    @Bean
    @Conditional(JDK17Conditional.class)
    public JDK17 JDK17Conditional() {
        System.out.println("JDK17 注册了");
        return new JDK17();
    }

    @Bean
    @Conditional(JDK21Conditional.class)
    public JDK21 JDK21Conditional() {
        System.out.println("JDK21 注册了");
        return new JDK21();
    }
}

class JDK17{};
class JDK21{};

class JDK17Conditional implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return JavaVersion.getJavaVersion().equals(JavaVersion.SEVENTEEN); // Java 17
    }
}

class JDK21Conditional implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return JavaVersion.getJavaVersion().equals(JavaVersion.TWENTY_ONE); // Java 21
    }
}
