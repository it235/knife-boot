# knife-boot
基于SpringCloud(Hoxton.SR8) + SpringBoot(2.3.4) 和Cloud Alibaba(2.2.1) 搭建的企业级前后端分离的微服务框架(用户权限管理，统一授权，资源权限管理、JWT、MQ、网关API，Nacos注册配置中心，Seata分布式事务，钉钉微信接入、后台应用管理)，快速开发部署，学习简单，功能强大，提供快速接入核心接口能力，支持多业务系统并行开发，可以作为后端服务的开发脚手架。代码简洁，架构清晰，适合学习和直接在企业项目中使用。

前端采用ElementUI开发，请看前端项目



### 项目结构：

knife-boot
├── knife-api  -- API模块
├──   ├──goods-api  -- 商品API
├──   ├──order-api  -- 订单API
├── knife-auth  -- 鉴权服务
├──   ├──auth-server  -- 鉴权Server
├──   ├──auth-biz  -- 鉴权逻辑
├── knife-business    -- 业务服务
├──   ├──knife-goods  -- 商品服务
├──   ├──knife-order  -- 订单服务
├── knife-base  -- 基础提供
├──   ├──knife-common  -- 鉴权Server
├──   ├──knife-core  -- 鉴权逻辑
├──   ├──knife-spring  -- 鉴权逻辑
├── knife-dashboard  -- 可视化专区
├──   ├──knife-monitor-admin  -- Spring Boot Admin监控[6989]
├──   ├──knife-skywalking-server -- 链路追踪[6979]
├── knife-devops  -- 开发运维模块[6999]
├──   ├──knife-code-gen  -- 代码生成
├──   ├──knife-xxx-server  -- Gateway网关
├── knife-gateway  -- 网关工程[6999]
├──   ├──knife-zuul-server  -- Zuul网关
├──   ├──knife-gateway-server  -- Gateway网关
├── knife-integration



### 架构概览

- 网关
- 注册与配置中心
- 分布式事务
- 性能监控
- 负载均衡、限流与熔断
- 链路追踪
- 任务调度
- 代码生成
- 消息队列
- 搜索引擎（ES）
- 微信集成（公众号、支付、开放平台、小程序）
- 钉钉集成（授权登录、消息推送、组织架构、工作流）
- ELK
- Netty
- 鉴权中心
- RBAC权限管理
- 持续集成Jenkins
- 容器化Docker
- K8S灰度发布
- MySQL
- MongoDB
- Redis
- 私服Nexus
- OpenVPN
- JumpServer堡垒机