# 电商后台管理系统 — 配置与运行说明

## 1. 环境要求

| 组件 | 版本 |
|------|------|
| JDK | 17 |
| Maven | 3.8+ |
| MySQL | 8.0+ |
| Redis | 6.0+ |
| Node.js | 18+ |
| npm | 9+ |

## 2. 数据库初始化

### 2.1 创建数据库

```sql
CREATE DATABASE IF NOT EXISTS ch16 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2.2 导入初始化数据

```bash
mysql -u root -p123456 ch16 < ebusiness-server/ch16.sql
mysql -u root -p123456 ch16 < ebusiness-server/init_data.sql
```

### 2.3 验证导入

```sql
-- 管理员表应有 1 条记录（admin/admin）
SELECT * FROM ausertable;

-- 商品表应有若干测试数据
SELECT COUNT(*) FROM goodstable;
```

## 3. 项目结构

```
ebusiness/
├── ebusiness-server/          # Spring Boot 后端
│   ├── pom.xml                # Maven 依赖（JPA + Redis + MySQL + Hutool）
│   ├── ch16.sql               # 数据库建表 + 初始数据
│   ├── init_data.sql           # 商品/类型测试数据
│   └── src/main/
│       ├── java/com/ch/ch16(1)/     # 注意: 源码目录含 (1) 后缀
│       │   ├── Ch16Application.java       # 启动类 @EnableJpaRepositories
│       │   ├── entity/                    # JPA 实体（@Entity）
│       │   ├── repository/admin/          # 管理端 Repository
│       │   ├── repository/before/         # 前端 Repository
│       │   ├── service/admin/             # 管理端 Service
│       │   ├── service/before/            # 前端 Service
│       │   ├── controller/admin/          # 管理端 Controller (/api/admin/*)
│       │   ├── controller/before/         # 前端 Controller (/api/before/*)
│       │   └── common/                    # 工具类（JWT/Redis/拦截器/响应封装）
│       └── resources/
│           ├── application.properties     # 主配置（数据源/JPA）
│           └── conf/conf.properties       # JWT/Redis 参数
│
└── ebusiness-vite/            # Vue 3 前端
    ├── package.json            # Vue 3 + Element Plus + Axios + ECharts
    ├── vite.config.js          # Vite 配置（代理 + 端口）
    └── src/
        ├── App.vue
        ├── router/             # 路由配置
        ├── axios/              # Axios 请求封装
        ├── views/before/       # 用户端页面
        ├── views/admin/        # 管理端页面
        └── components/         # 公共组件（HeaderView/HeaderCom/SidebarCom）
```

## 4. 后端配置说明

### 4.1 关键配置文件

**`application.properties`** — 主配置：

| 配置项 | 值 | 说明 |
|--------|-----|------|
| `server.port` | 8443 | 服务端口 |
| `server.servlet.context-path` | /eBusiness | 上下文路径 |
| `spring.datasource.url` | jdbc:mysql://localhost:3306/ch16 | 数据库连接 |
| `spring.datasource.username` | root | 数据库用户 |
| `spring.datasource.password` | 123456 | 数据库密码 |
| `spring.jpa.hibernate.ddl-auto` | none | 不自动建表（使用 SQL 初始化） |
| `spring.jpa.show-sql` | true | 控制台输出 SQL |
| `spring.servlet.multipart.location` | D:/data/apps/temp | 图片上传临时目录 |

**`conf/conf.properties`** — JWT/Redis 参数：

| 配置项 | 值 | 说明 |
|--------|-----|------|
| `token.header` | Authorization | JWT 请求头 |
| `token.expiration` | 10 | Token 有效期（分钟） |
| `redis.expiration` | 1800 | Redis 过期时间（秒） |

### 4.2 技术栈

- **Spring Boot 3.2.4** + **Spring Data JPA** (Hibernate 6.4.4)
- **Redis** (Spring Data Redis)
- **JWT** (Hutool JWTUtil)
- **MySQL 8** (HikariCP 连接池)
- **Lombok**

### 4.3 启动命令

```bash
cd ebusiness-server

# 首次或代码变更后先编译
mvn clean compile -DskipTests

# 启动（后端地址: http://localhost:8443/eBusiness）
mvn spring-boot:run
```

## 5. 前端配置说明

### 5.1 技术栈

- **Vue 3.4** (Composition API + `<script setup>`)
- **Element Plus 2.7** (UI 组件库)
- **Vue Router 4.3** (路由)
- **Axios 1.7** (HTTP 请求)
- **ECharts 5.5** (管理端统计图表)
- **Vite 5.2** (构建工具)

### 5.2 启动命令

```bash
cd ebusiness-vite

# 首次运行安装依赖
npm install

# 启动开发服务器（前端地址: http://localhost:3000）
npm run dev
```

## 6. 服务地址汇总

| 服务 | 地址 |
|------|------|
| 前端页面 | `http://localhost:3000` |
| 后端 API | `http://localhost:8443/eBusiness` |
| 管理端登录 | `http://localhost:3000` → 点击"管理员"链接 |
| 用户端 | `http://localhost:3000` → 首页 |

## 7. 默认账号

| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 管理员 | `admin` | `admin` | 管理后台登录，明文存储 |
| 普通用户 | 自行注册 | — | 密码经 MD5 加盐后存储 |

## 8. API 接口概览

### 8.1 鉴权说明

- `AuthInterceptor` 拦截所有 `/api/**` 请求
- `@AuthIgrone` 注解标记免鉴权接口（登录/注册/首页查询）
- 鉴权流程：请求头 `Authorization` → JWT 验签 → Redis 校验登录态

### 8.2 管理端接口 (`/api/admin/*`)

| 接口 | 方法 | 鉴权 | 说明 |
|------|------|------|------|
| `/api/admin/login` | POST | 免 | 管理员登录 |
| `/api/admin/goods/getGoods` | POST | 需 | 分页查询商品 |
| `/api/admin/goods/add` | POST | 需 | 新增/修改商品 |
| `/api/admin/goods/delete` | POST | 需 | 删除商品 |
| `/api/admin/goods/getGoodsIndex` | POST | 免 | 首页商品列表 |
| `/api/admin/goods/getAdvGoods` | POST | 免 | 轮播广告商品 |
| `/api/admin/goods/getGoodsById` | POST | 免 | 商品详情 |
| `/api/admin/type/getAllGoodsType` | POST | 免 | 所有商品类型 |
| `/api/admin/type/getGoodsType` | POST | 需 | 分页查询类型 |
| `/api/admin/type/add` | POST | 需 | 新增类型 |
| `/api/admin/type/update` | POST | 需 | 修改类型 |
| `/api/admin/type/delete` | POST | 需 | 删除类型 |
| `/api/admin/orders/getAllOrders` | POST | 需 | 所有订单 |
| `/api/admin/orders/getOrdersDetail` | POST | 需 | 订单详情 |
| `/api/admin/orders/goPay` | POST | 需 | 支付订单 |
| `/api/admin/orders/submitOrder` | POST | 需 | 提交订单（购物车） |
| `/api/admin/orders/goSubmitOrder` | POST | 需 | 直接下单 |
| `/api/admin/orders/selectOrderByMonth` | POST | 需 | 月度销售统计 |
| `/api/admin/orders/selectOrderByType` | POST | 需 | 按类型统计 |

### 8.3 用户端接口 (`/api/before/*`)

| 接口 | 方法 | 鉴权 | 说明 |
|------|------|------|------|
| `/api/before/register` | POST | 免 | 用户注册 |
| `/api/before/login` | POST | 免 | 用户登录 |
| `/api/before/getcode` | GET | 免 | 获取图形验证码 |
| `/api/before/cart/add` | POST | 需 | 加入购物车 |
| `/api/before/cart/myCart` | POST | 需 | 查询购物车 |
| `/api/before/cart/bupDateCart` | POST | 需 | 批量更新购物车 |
| `/api/before/cart/clearCart` | POST | 需 | 清空购物车 |
| `/api/before/cart/removeCart` | POST | 需 | 删除购物车项 |
| `/api/before/focus/add` | POST | 需 | 收藏商品 |
| `/api/before/focus/getMyFocusGoods` | POST | 需 | 我的收藏 |

## 9. 数据分页

所有分页接口支持 `currentPage` + `pageSize` 参数，前端可在 5/10/20 条/页之间切换。

## 10. 技术架构变更记录（迁移自 MyBatis-Plus）

| 层面 | 变更前 | 变更后 |
|------|--------|--------|
| ORM | MyBatis-Plus 3.5.5 | Spring Data JPA (Hibernate 6.4.4) |
| 实体注解 | `@TableName/@TableId/@TableField` | `@Entity/@Table/@Id/@GeneratedValue/@Transient` |
| 数据访问 | `BaseMapper<T>` + XML | `JpaRepository<T, ID>` + `JpaSpecificationExecutor<T>` |
| 分页 | `IPage/Page` + 每页2条 | `Pageable` + 5/10/20可切换 |
| 链式查询 | `lambdaQuery().eq().count()` | `Specification<T>` + `repository.findAll(spec)` |
| 自定义SQL | Mapper XML | `@Query` (JPQL/Native SQL) |
| 分页插件 | MybatisPlusInterceptor | JPA 内置 |

## 11. 常见问题

### Q: 启动报 `Unknown column 'xxx' in 'field list'`
确认已导入 `ch16.sql` 和 `init_data.sql`，数据库表结构与实体类字段名一致。

### Q: 图片上传失败
确认 `D:/data/apps/temp` 目录存在且有写入权限。

### Q: Redis 连接失败
确认 Redis 服务已启动（默认 `localhost:6379`），无密码。

### Q: 前端页面报 CORS 错误
确认后端已启动在 8443 端口，`Ch16Application` 中已配置全局 CORS 过滤器。

### Q: Token 过期需要重新登录
JWT 有效期 10 分钟，Redis 缓存 30 分钟，每次鉴权通过自动续期。
