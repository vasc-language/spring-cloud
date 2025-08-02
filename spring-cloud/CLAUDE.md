# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目架构

这是一个基于 Spring Cloud 的微服务项目，采用多模块 Maven 结构：

- **父项目**: spring-cloud (聚合项目)
- **子模块**:
  - `blog-info`: 博客信息服务
  - `user-info`: 用户信息服务  
  - `blog-common`: 公共模块
  - `gateway-service`: 网关服务

## 核心架构设计

### 缓存架构
- **Redis-MySQL 双存储**: 用户服务采用 Redis + MySQL 的缓存架构
- **缓存策略**: 先查 Redis，不存在则查 MySQL 并回写 Redis
- **缓存失效**: 2周过期时间 (`EXPIRE_TIME = 2 * 7 * 24 * 60 * 60`)
- **降级机制**: Redis 解析失败时自动降级到数据库查询
- **关键实现**: `UserServiceImpl.queryUserInfo()` 方法展示了标准的缓存穿透防护

### OpenFeign 服务间调用
- **API 分离**: 每个服务都有独立的 `-api` 模块定义 Feign 接口
- **服务发现**: 通过 `@FeignClient(value = "user-service")` 自动服务发现
- **统一响应**: 所有 API 返回 `Result<T>` 统一响应格式
- **跨服务调用**: 如 `BlogServiceImpl` 调用 `UserServiceApi` 获取作者信息

### 网关认证架构
- **全局过滤器**: `AuthFilter` 实现 JWT 认证
- **白名单机制**: `AuthWhiteNameProperties` 配置免认证路径
- **统一鉴权**: 所有请求经过网关统一鉴权处理

### 消息队列架构
- **RabbitMQ 集成**: 异步消息处理和服务解耦
- **队列配置**: `RabbitConfig` 定义持久化队列和交换机
- **消息监听**: `@RabbitListener` 注解实现消息消费
- **消息发送**: `RabbitTemplate` 提供消息发送能力
- **手动确认模式**: 确保消息可靠处理

### 公共组件设计
- **工具类封装**: `Redis`, `JsonUtils`, `JWTUtils`, `SecurityUtil` 等
- **自动配置**: `RedisConfig` 提供条件化的 Redis Bean 配置
- **统一异常**: `BlogException` 和全局异常处理机制
- **参数校验**: `RegexUtil` 提供邮箱、URL 等格式校验
- **常量定义**: `Constants` 类统一管理队列和交换机名称

## 技术栈

- **Spring Boot** 3.1.6
- **Spring Cloud** 2022.0.3 (Kilburn)
- **Spring Cloud Alibaba** 2022.0.0.0-RC2
- **Java** 17 (LTS)
- **MyBatis** 3.0.3 & **MyBatis Plus** 3.5.5
- **MySQL** 8.0.33
- **Redis** (本地部署 127.0.0.1:6379)
- **RabbitMQ** (远程部署 117.72.211.70:5672/blog)
- **Nacos** 服务注册与配置中心
- **Spring Cloud Gateway** 微服务网关
- **OpenFeign** 服务间调用
- **Lombok** 代码简化

## 构建和运行命令

### Maven 构建
```bash
# 在根目录下构建整个项目
mvn clean compile

# 打包所有模块
mvn clean package

# 安装到本地仓库
mvn clean install

# 跳过测试构建
mvn clean package -DskipTests

# 并行构建提升速度 (推荐)
mvn clean package -T 4

# 仅构建修改过的模块
mvn clean compile -pl blog-common,user-info/user-info-service -am
```

### 运行服务
```bash
# 运行特定模块 (在对应模块目录下)
mvn spring-boot:run

# 使用指定配置文件运行
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 后台运行服务
nohup mvn spring-boot:run > service.log 2>&1 &

# 或使用 java -jar 运行打包后的 jar
java -jar target/模块名-1.0-SNAPSHOT.jar

# 指定配置运行 jar
java -jar -Dspring.profiles.active=prod target/模块名-1.0-SNAPSHOT.jar
```

### 测试相关命令
```bash
# 运行所有模块的测试
mvn test

# 运行指定模块的测试
mvn test -pl 模块名

# 运行特定测试类
mvn test -Dtest=类名

# 运行特定测试方法  
mvn test -Dtest=类名#方法名

# 仅运行 blog-common 模块测试
mvn test -pl blog-common
```

### 开发调试命令
```bash
# 启动时开启远程调试 (端口 5005)
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# 查看应用日志 (日志文件在项目根目录)
tail -f blog.log        # 博客服务日志
tail -f user.log        # 用户服务日志

# 检查服务健康状态
curl http://localhost:9090/actuator/health  # 博客服务
curl http://localhost:10030/actuator/health # 网关服务
curl http://localhost:8080/actuator/health  # 用户服务

# 查看 Nacos 注册的服务
# 访问 http://117.72.211.70:8848/nacos 查看服务注册情况
```

## 数据流和调用链路

### 用户认证流程
1. 客户端请求 -> Gateway (AuthFilter)
2. JWT Token 验证 -> 解析用户信息
3. 白名单检查 -> 放行或拒绝
4. 转发到目标服务

### 用户查询缓存流程
1. `UserServiceImpl.queryUserInfo(userName)`
2. 构建 Redis key: `user:{userName}`
3. 检查 Redis 是否存在 -> `redis.hasKey(key)`
4. 存在: 获取JSON -> 解析为对象 -> 返回
5. 不存在: 查询 MySQL -> 写入 Redis -> 返回
6. 解析失败: 删除无效缓存 -> 降级查询 MySQL

### 跨服务调用链路
1. Blog服务获取作者信息: `BlogServiceImpl.getAuthorInfo()`
2. 通过 Feign 调用: `UserServiceApi.getAuthorInfo(blogId)`
3. 服务发现定位: `user-service`
4. 执行远程调用获取用户信息

### 消息队列处理流程
1. 消息发送: `RabbitTemplate.convertAndSend(exchange, routingKey, message)`
2. 队列路由: 消息通过交换机路由到目标队列
3. 监听器接收: `@RabbitListener(queues = "queueName")` 自动监听
4. 消息处理: 业务逻辑处理接收到的消息
5. 手动确认: 处理完成后手动确认消息

## 关键代码模式

### Redis 缓存模式
```java
// 标准缓存查询模式 (UserServiceImpl.queryUserInfo)
String key = buildKey(userName);
if (redis.hasKey(key)) {
    String userJson = redis.get(key);
    UserInfo userInfo = JsonUtils.parseJson(userJson, UserInfo.class);
    return userInfo == null ? selectUserInfoByName(userName) : userInfo;
}
// 降级到数据库查询并回写缓存
```

### OpenFeign 接口定义模式
```java
@FeignClient(value = "user-service", path = "/user")
public interface UserServiceApi {
    @RequestMapping("/getUserInfo")
    Result<UserInfoResponse> getUserInfo(@RequestParam("userId") Integer userId);
}
```

### 条件化配置模式
```java
@Bean
@ConditionalOnProperty(prefix = "spring.data.redis", name = "host")
public Redis redis(StringRedisTemplate redisTemplate) {
    return new Redis(redisTemplate);
}
```

### RabbitMQ 队列配置模式
```java
// 队列配置 (RabbitConfig)
@Bean("hello")
public Queue queue() {
    return QueueBuilder.durable("hello").build();
}

// 消息监听器 (HelloQueueListener)
@RabbitListener(queues = "hello")
public void handler(Message message){
    System.out.println("收到消息:" + message);
}

// 消息发送 (RabbitMQTest)
@Autowired
private RabbitTemplate rabbitTemplate;

public void sendMessage() {
    rabbitTemplate.convertAndSend("", "hello", "hello rabbitmq~");
}
```

## 环境配置要求

### 数据库配置
- **MySQL 8.0.33**: 用于博客和用户数据存储
- 用户服务数据库: `jdbc:mysql://127.0.0.1:3306/spring_cloud_user`
- 博客服务数据库: `jdbc:mysql://127.0.0.1:3306/spring_cloud_blog`
- 用户名: `root`, 密码: `212409`

### Redis 缓存配置
- **Redis**: 本地部署 127.0.0.1:6379
- 连接池配置: 最大连接数8，最大空闲连接8
- 超时设置: 60秒空闲自动关闭
- 用于用户会话缓存和热点数据存储

### RabbitMQ 消息队列配置
- **RabbitMQ 服务器**: `117.72.211.70:5672`
- **虚拟主机**: `/blog`
- **认证信息**: 用户名/密码 `bite:bite`
- **连接地址**: `amqp://bite:bite@117.72.211.70:5672/blog`
- **确认模式**: 手动确认 (`acknowledge-mode: manual`)
- **队列类型**: 持久化队列，服务器重启后保持存在

### 外部依赖服务
- **Nacos 注册中心**: `117.72.211.70:8848`
- 管理界面: http://117.72.211.70:8848/nacos

### 端口分配
- `blog-info-service`: 9090
- `user-info-service`: 8080  
- `gateway-service`: 10030

## 常见开发任务

### 添加新的缓存逻辑
1. 在 `blog-common` 中扩展 `Redis` 工具类方法
2. 在业务服务中注入 `Redis` Bean
3. 实现查询-缓存-降级的三层逻辑
4. 添加对应的单元测试

### 新增服务间调用
1. 在目标服务的 `-api` 模块定义 Feign 接口
2. 使用 `@FeignClient` 注解指定服务名
3. 在调用方添加对 `-api` 模块的依赖
4. 注入 Feign 接口进行远程调用

### 扩展网关过滤逻辑
1. 修改 `AuthFilter` 实现新的过滤规则
2. 在 `AuthWhiteNameProperties` 中配置白名单路径
3. 确保过滤器实现 `Ordered` 接口控制执行顺序

### 新增消息队列功能
1. 在 `RabbitConfig` 中定义新的队列和交换机 Bean
2. 在 `Constants` 类中添加队列和交换机名称常量
3. 创建消息监听器类并使用 `@RabbitListener` 注解
4. 使用 `RabbitTemplate` 发送消息到指定队列
5. 配置适当的确认模式和错误处理机制

## 性能监控与可观测性

### Spring Boot Actuator
已集成健康检查端点：
- Blog服务健康检查: `http://localhost:9090/actuator/health`
- 网关服务健康检查: `http://localhost:10030/actuator/health`
- 用户服务健康检查: `http://localhost:8080/actuator/health`

### 推荐监控方案
- **Micrometer + Prometheus**: 指标收集
- **Zipkin/Jaeger**: 分布式链路追踪  
- **ELK Stack**: 日志聚合分析
- **Spring Boot Admin**: 服务监控面板