package ai.agent.product.service;

import ai.agent.product.mapper.ProductMapper;
import ai.agent.product.model.ProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 姚东名
 * Date: 2025-07-13
 * Time: 20:57
 */
@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    public ProductInfo selectProductById(Integer id) {
        return productMapper.selectProductById(id);
    }
}
