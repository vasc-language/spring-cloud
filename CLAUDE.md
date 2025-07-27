# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Structure

This is a Spring Cloud microservices project using Maven multi-module architecture with the following modules:

- **gateway**: Spring Cloud Gateway - API网关服务，端口10030
- **order-service**: 订单服务，端口8080，使用MyBatis + MySQL
- **product-service**: 产品服务，使用Nacos配置中心
- **product-api**: 产品服务API定义，使用Feign客户端进行服务间调用

## Technology Stack

- Spring Boot 3.1.6 with Java 17 (已统一版本)
- Spring Cloud 2022.0.3
- Spring Cloud Alibaba 2022.0.0.0-RC2 (Nacos服务发现和配置中心)
- MyBatis 3.0.3 + MySQL 8.0.33
- Lombok for code generation
- OpenFeign for service-to-service communication
- JUnit for testing

## Service Discovery & Configuration

- **Nacos Server**: 117.72.211.70:8848
- All services register with Nacos for service discovery
- Product service uses Nacos config center for configuration management
- Order service has Nacos负载均衡策略 enabled for weight-based load balancing

## Build Commands

```bash
# 编译整个项目
mvn compile

# 清理并编译
mvn clean compile

# 打包所有模块
mvn package

# 安装到本地仓库
mvn install

# 先安装product-api模块（解决systemPath依赖问题）
cd product-api && mvn clean install

# 指定环境打包
mvn package -Pdev  # 开发环境
mvn package -Pprod # 生产环境

# 运行特定服务 (在对应模块目录下)
mvn spring-boot:run

# 运行测试
mvn test
```

## Gateway Routes

Gateway配置的路由规则 (gateway/src/main/resources/application.yml):
- `/order/**` 和 `/feign/**` -> order-service
- `/product/**` -> product-service

## Inter-Service Communication

Order service通过ProductApi (Feign客户端) 调用product-service:
- basePackages = "ai.agent.product.api" in @EnableFeignClients
- Product API定义在product-api模块，支持多种参数传递方式

## Profile Configuration

Services支持多环境配置:
- Development: application-dev.yml
- Production: application-prod.yml
- Active profile通过Maven profile控制: @profile.name@

## Database Configuration

Order service使用MyBatis:
- 自动驼峰转换: map-underscore-to-camel-case: true
- SQL日志可通过log-impl配置启用

## Package Structure

所有服务使用ai.agent.*包结构:
- ai.agent.gateway - 网关
- ai.agent.order - 订单服务
- ai.agent.product - 产品服务和API

## Important Notes

- Product-api模块在order-service的pom.xml中使用systemPath依赖，这会产生Maven警告
- **数据库安全**: 数据库密码明文存储在application-dev.yml中，生产环境需要加密处理
- Nacos临时实例配置 (ephemeral) 默认启用，可通过配置关闭
- 需要先启动Nacos服务器才能运行微服务

## Service Startup Order

1. 启动Nacos服务器 (117.72.211.70:8848)
2. 启动MySQL数据库
3. 启动product-service
4. 启动order-service
5. 启动gateway (最后启动)

## Error Handling & Resilience

Order service includes comprehensive error handling:
- **Feign Configuration**: Custom retry logic (3 attempts, 100ms-1s intervals) and error decoder
- **Timeout Settings**: Configurable connection (8s) and read (20s) timeouts for product-service calls
- **Health Check**: `/health/check` endpoint provides service discovery diagnostics
- **Graceful Degradation**: Try-catch blocks in controllers return meaningful error messages instead of 500 errors

## Debugging & Diagnostics

```bash
# Check service health and discovery status
curl http://localhost:8080/health/check

# View Feign call details (logs enabled at FULL level)
# Check order-service logs for detailed request/response information

# Test individual service endpoints
curl http://localhost:9090/product/p1?id=123  # Direct product-service
curl http://localhost:8080/feign/o1?id=123   # Through order-service
curl http://localhost:10030/feign/o1?id=123  # Through gateway
```

## Testing

```bash
# 运行所有测试
mvn test

# 运行特定模块测试
cd gateway && mvn test

# 测试网关路由
curl http://localhost:10030/product/1001
curl http://localhost:10030/order/1
curl http://localhost:10030/feign/o1?id=123

# 测试健康检查
curl http://localhost:8080/health/check
```

## Architecture Notes

- **Service-to-Service Communication**: Order service uses ProductApi (Feign client) defined in product-api module
- **Load Balancing**: Ribbon configuration with retry logic for failed requests
- **Circuit Breaking**: Hystrix configuration available for fault tolerance
- **Configuration Management**: Product service uses Nacos config center, others use local YAML files
- **Cross-Module Dependencies**: product-api uses systemPath dependency in order-service (Maven warning expected)

## Common Issues

- **连接超时**: 检查Nacos服务状态和网络连通性。使用健康检查接口诊断服务发现问题
- **服务发现失败**: 确认服务名称和Nacos配置正确。检查 `/health/check` 输出
- **数据库连接失败**: 检查数据库服务和连接配置
- **SystemPath警告**: product-api依赖使用绝对路径，建议改为标准Maven依赖
- **Feign调用失败**: 查看详细日志 (loggerLevel: FULL)，检查product-service是否运行并注册到Nacos