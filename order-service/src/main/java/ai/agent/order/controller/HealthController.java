package ai.agent.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/health")
public class HealthController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/check")
    public Map<String, Object> healthCheck() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 检查当前服务注册状态
            result.put("service", "order-service");
            result.put("status", "UP");
            
            // 检查 Nacos 连接和服务发现
            var services = discoveryClient.getServices();
            result.put("discovered_services", services);
            
            // 检查 product-service 是否可发现
            var productInstances = discoveryClient.getInstances("product-service");
            result.put("product_service_instances", productInstances.size());
            result.put("product_service_details", productInstances);
            
            if (productInstances.isEmpty()) {
                result.put("warning", "product-service 未发现任何实例！请确保 product-service 已启动并注册到 Nacos");
            }
            
            log.info("健康检查完成，发现服务: {}, product-service实例数: {}", services, productInstances.size());
            
        } catch (Exception e) {
            log.error("健康检查失败: {}", e.getMessage(), e);
            result.put("status", "DOWN");
            result.put("error", e.getMessage());
        }
        
        return result;
    }
}