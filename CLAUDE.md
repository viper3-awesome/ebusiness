# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

电商后端项目，Spring Boot 3.2.4 + MyBatis-Plus 3.5.5 + Redis + JWT + MySQL 8，前端为 Vue 3 + Vite + Element Plus。Java 17。

## 构建与运行

```bash
# 后端（需在 ebusiness-server 目录下执行）
cd ebusiness-server
mvn clean package -DskipTests        # 打包
mvn spring-boot:run                  # 启动（端口 8443，context-path /eBusiness）

# 前端（需在 ebusiness-vite 目录下执行）
cd ebusiness-vite
npm install                          # 安装依赖
npm run dev                          # 启动 Vite 开发服务器
npm run build                        # 生产构建
```

## 关键路径异常

**重要：** 源码目录名包含 `(1)` 后缀（如 `com/ch/ch16(1)/`、`common(1)/`、`config(1)/`），这是非标准目录结构。Maven 编译已通过说明 IDE 或 Maven 可能做了特殊处理。`@MapperScan` 扫描的 `basePackages` 为 `com.ch.ch16.mapper`，实体别名为 `com.ch.ch16.entity`，这意味着运行时 classpath 下存在不带 `(1)` 的同名包。**编辑时务必注意实际文件路径使用 `ch16(1)` 目录。**

## 代码架构

### 分层结构（controller/admin 和 controller/before 两个上下文）

`controller/admin/*` — 管理端接口（`/api/admin/**`），处理后台管理员操作
`controller/before/*` — 前台用户接口（`/api/before/**`），处理用户端操作

两者的 entity、mapper、service 也各自分离到 `admin` 和 `before` 子包中，互不交叉。

### 鉴权机制

- `WebConfig` 注册 `AuthInterceptor` 拦截所有 `/api/**` 请求
- `@AuthIgrone` 注解标记不需要鉴权的接口（如登录、注册、获取验证码）
- 鉴权流程：从请求头 `Authorization` 取 token → 验证 JWT 签名 → 检查 Redis 中 `login_<username>` 是否存在且未过期
- JWT 工具：`JwtTokenUtil`（基于 hutool JWTUtil），token 有效期 10 分钟（`conf/conf.properties` 中的 `token.expiration`）
- Redis 过期时间 1800 秒（30 分钟），每次校验通过后自动续期

### API 响应规范

所有接口返回值统一使用 `ResponseResult<T>` 包装，包含四个字段：
- `result` — 业务数据
- `statusCode` — 状态码枚举（C200/C401/C404/C405/C500）
- `resultType` — 结果类型（SUCCESS/MESSAGE/ERROR/NOT_FOUND）
- `msgId` — 消息标识，对应前端国际化文案（如 "A001" 表示用户不存在，"A002" 表示密码错误）

### 数据层

- MyBatis-Plus 配置了 MySQL 分页插件（`MybatisPlusConfig`）
- Mapper XML 文件位于 `src/main/resources/mappers/`
- `RedisUtil` 封装了 RedisTemplate，提供 String/Map/Set/List 操作及序列化存取（`setObject`/`getObject` 通过 hutool JSON 序列化）
- `MD5Util` 用于密码加密
