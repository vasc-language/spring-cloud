package ai.agent.product.controller;

import ai.agent.product.model.ProductInfo;
import ai.agent.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/product")
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @RequestMapping("/{productId}")
    public ProductInfo getProductById(@PathVariable("productId") Integer productId){
        log.info("接收参数：productId" + productId);
        return productService.selectProductById(productId);
    }

    // 单个参数
    @RequestMapping("/p1")
    public String p1(Integer id) {
        return "product-service 接收的参数 id：" + id;
    }

    // 多个参数
    @RequestMapping("/p2")
    public String p2(Integer id, String name) {
        return "product-service 接收的参数 id：" + id + " name：" + name;
    }

    // 传递JSON
    @RequestMapping("/p3")
    public String p3(ProductInfo productInfo){
        return "product-service 接收到参数: productInfo"+productInfo.toString();
    }

    // 传递对象
    @RequestMapping("/p4")
    public String p4(@RequestBody ProductInfo productInfo){
        return "product-service 接收到参数: productInfo"+productInfo.toString();
    }
}
