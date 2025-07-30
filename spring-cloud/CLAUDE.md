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

## 技术栈

- Spring Boot 3.1.6
- Spring Cloud 2022.0.3
- Spring Cloud Alibaba 2022.0.0.0-RC2
- Java 17
- MyBatis 3.0.3 & MyBatis Plus 3.5.5
- MySQL 8.0.33
- Lombok

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
```

### 运行服务
```bash
# 运行特定模块 (在对应模块目录下)
mvn spring-boot:run

# 或使用 java -jar 运行打包后的 jar
java -jar target/模块名-1.0-SNAPSHOT.jar
```

### 单模块构建和运行
```bash
# 仅构建指定模块及其依赖
mvn clean compile -pl 模块名 -am

# 仅运行指定模块测试
mvn test -pl 模块名

# 从根目录运行指定模块
mvn spring-boot:run -pl 模块名
```

### 依赖管理
```bash
# 查看依赖树
mvn dependency:tree

# 分析依赖冲突
mvn dependency:analyze
```

### 测试相关命令
```bash
# 运行所有模块的测试
mvn test

# 运行指定模块的测试
mvn test -pl 模块名

# 跳过测试执行构建
mvn clean package -DskipTests

# 运行特定测试类
mvn test -Dtest=类名

# 运行特定测试方法
mvn test -Dtest=类名#方法名
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

# 查看 Nacos 注册的服务
# 访问 http://117.72.211.70:8848/nacos 查看服务注册情况
```

## 项目架构模式

这是一个标准的多模块Maven聚合项目，采用微服务架构模式：

- **父项目依赖管理**: 根pom.xml统一管理所有依赖版本，子模块继承父项目配置
- **模块职责划分**:
  - `blog-common`: 公共组件和工具类，包含统一响应格式、异常处理、JWT工具等
  - `blog-info`: 博客相关业务服务，采用 API-Service 分离架构
    - `blog-info-api`: 对外暴露的 Feign 接口定义
    - `blog-info-service`: 具体业务实现，端口 9090
  - `user-info`: 用户管理业务服务，采用 API-Service 分离架构  
    - `user-info-api`: 对外暴露的 Feign 接口定义
    - `user-info-service`: 具体业务实现
  - `gateway-service`: Spring Cloud Gateway 网关，端口 10030，负责路由和统一入口
- **服务发现**: 使用 Nacos 作为注册中心 (117.72.211.70:8848)
- **服务间调用**: 通过 OpenFeign 实现服务间通信
- **网关路由**: 
  - `/user/**` -> user-service
  - `/blog/**` -> blog-service
- **标准目录结构**: 每个模块遵循Maven标准目录结构 (src/main/java, src/main/resources, src/test/java)

## 项目状态

项目已实现完整的微服务架构，包含：
- 完整的 Spring Boot 应用程序和启动类
- Nacos 服务发现与配置中心集成
- Spring Cloud Gateway 网关路由配置
- OpenFeign 服务间调用
- MyBatis Plus 数据访问层
- 完整的 Controller-Service-Mapper 三层架构
- 统一异常处理和响应格式
- JWT 认证和安全工具类

## 开发工作流程

### 添加新模块
1. 在父pom.xml的`<modules>`中添加新模块
2. 创建模块目录和pom.xml，继承父项目
3. 添加模块特定的依赖项

### 模块间依赖
- 业务模块需要公共组件时，在pom.xml中添加对`blog-common`的依赖
- 避免业务模块之间的直接依赖，通过API接口或消息队列通信

## 开发注意事项

- 所有模块继承父项目的依赖管理，避免在子模块中重复声明版本号
- 使用UTF-8编码，目标Java版本为17
- 项目采用中文注释和文档
- 新建Spring Boot应用时需要添加@SpringBootApplication注解和适当的starter依赖

## 环境配置要求

### 数据库配置
- **MySQL 8.0.33**: 用于博客和用户数据存储
- 默认连接: `jdbc:mysql://127.0.0.1:3306/spring_cloud_blog`
- 用户名: `root`, 密码: `212409`
- 需要创建数据库: `spring_cloud_blog`

### 外部依赖服务
- **Nacos 注册中心**: `117.72.211.70:8848`
  - 用于服务注册发现和配置管理
  - 管理界面: http://117.72.211.70:8848/nacos
- **MyBatis Plus**: 自动配置下划线转驼峰命名
- **日志配置**: 控制台输出 + 文件日志 (blog.log, user.log)

### 端口分配
- `blog-info-service`: 9090
- `user-info-service`: 默认端口 (检查 application.yml)
- `gateway-service`: 10030

### 开发环境切换
项目支持多环境配置 (dev/prod)，在 application.yml 中通过 Spring Profile 激活：
```yaml
spring:
  config:
    activate:
      on-profile: dev  # 或 prod
```