//package ai.agent.product.controller;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequestMapping("/config")
//@RefreshScope // 进行热更新
//@RestController
//public class NacosController {
//    @Value("${nacos.test.num}") // 读取配置信息
//    private Integer num;
//
//    @RequestMapping("/get")
//    public Integer get() {
//        return num;
//    }
//}
