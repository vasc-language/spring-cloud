package ai.agent.order.controller;

import ai.agent.product.api.ProductApi;
import ai.agent.product.model.ProductInfo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 依赖 product-api jar进行远程调用
 */
@Slf4j
@RequestMapping("/feign")
@RestController
public class FeignController {
    @Autowired
    private ProductApi productApi;

    @RequestMapping("/o1")
    public String o1(Integer id, String userName, HttpServletResponse response) {
        log.info("接收过滤器添加参数userName：{}", userName);
        
        try {
            log.info("开始调用product-service的p1方法，参数id: {}", id);
            String result = productApi.p1(id);
            log.info("成功调用product-service，返回结果: {}", result);
            response.setStatus(200);
            return result;
        } catch (Exception e) {
            log.error("调用product-service失败，错误信息: {}", e.getMessage(), e);
            response.setStatus(503); // Service Unavailable
            return "product-service服务调用失败: " + e.getMessage() + "。请确保product-service已启动并注册到Nacos。参数id: " + id;
        }
    }

    @RequestMapping("/o2")
    public String o2(Integer id, String name) {
        try {
            log.info("开始调用product-service的p2方法，参数id: {}, name: {}", id, name);
            String result = productApi.p2(id, name);
            log.info("成功调用product-service，返回结果: {}", result);
            return result;
        } catch (Exception e) {
            log.error("调用product-service失败，错误信息: {}", e.getMessage(), e);
            return "product-service服务调用失败: " + e.getMessage() + "。参数id: " + id + ", name: " + name;
        }
    }

    @RequestMapping("/o3")
    public String o3() {
        try {
            ProductInfo productInfo = new ProductInfo();
            productInfo.setId(45);
            productInfo.setProductName("T恤");
            log.info("开始调用product-service的p3方法，参数: {}", productInfo);
            String result = productApi.p3(productInfo);
            log.info("成功调用product-service，返回结果: {}", result);
            return result;
        } catch (Exception e) {
            log.error("调用product-service失败，错误信息: {}", e.getMessage(), e);
            return "product-service服务调用失败: " + e.getMessage();
        }
    }

    @RequestMapping("/o4")
    public String o4() {
        try {
            ProductInfo productInfo = new ProductInfo();
            productInfo.setId(46);
            productInfo.setProductName("T恤46");
            log.info("开始调用product-service的p4方法，参数: {}", productInfo);
            String result = productApi.p4(productInfo);
            log.info("成功调用product-service，返回结果: {}", result);
            return result;
        } catch (Exception e) {
            log.error("调用product-service失败，错误信息: {}", e.getMessage(), e);
            return "product-service服务调用失败: " + e.getMessage();
        }
    }
}
