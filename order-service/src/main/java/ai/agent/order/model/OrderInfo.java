package ai.agent.order.model;

import ai.agent.product.model.ProductInfo;
import lombok.Data;

import java.util.Date;

@Data
public class OrderInfo {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer num;
    private Integer price;
    private Integer deleteFlag;
    private Date createTime;
    private Date updateTime;
    private ProductInfo productInfo;
}
