# ProsePulse 后端项目

## 项目概述
ProsePulse是一个基于Spring Boot的多模块项目，采用了模块化的设计理念，主要包含用户认证、权限管理等功能。项目使用JWT进行身份验证，Redis进行缓存管理，MyBatis-Plus进行数据库操作。

## 项目结构

### 整体结构
```
├── module-common  // 通用组件模块
├── module-core    // 核心启动组件模块
├── module-user    // 用户组件模块
└── pom.xml        // 父项目POM文件
```

### 模块详细结构

#### module-common (通用组件模块)
```
├── config
│   └── RedisConfig.java          // Redis配置类
├── enums
│   ├── IResult.java              // 结果接口
│   └── ResultCode.java           // 通用结果状态码
├── exception
│   ├── BusinessException.java    // 业务异常类
│   └── GlobalExceptionHandler.java // 全局异常处理器
└── utils
    └── Result.java               // 统一响应结果类
```

#### module-user (用户组件模块)
```
├── config
│   ├── DataSourceConfig.java     // 数据源配置
│   └── SecurityConfig.java       // 安全配置
├── controller
│   ├── AuthController.java       // 认证控制器
│   └── UserController.java       // 用户控制器
├── domain
│   ├── Detail.java               // 用户详情实体
│   └── User.java                 // 用户实体
├── dto
│   ├── request                   // 请求DTO
│   │   ├── LoginRequest.java     // 登录请求
│   │   └── RegisterRequest.java  // 注册请求
│   └── response                  // 响应DTO
│       ├── JwtResponse.java      // JWT响应
│       └── UserResponse.java     // 用户信息响应
├── enums
│   ├── AuthResultCode.java       // 认证结果状态码
│   └── UserResultCode.java       // 用户结果状态码
├── filter
│   └── JwtAuthenticationFilter.java // JWT认证过滤器
├── mapper
│   ├── DetailMapper.java         // 用户详情Mapper
│   └── UserMapper.java           // 用户Mapper
├── service
│   ├── DetailService.java        // 用户详情服务接口
│   ├── UserService.java          // 用户服务接口
│   └── impl
│       ├── DetailServiceImpl.java // 用户详情服务实现
│       ├── UserDetailsServiceImpl.java // Spring Security用户详情服务
│       └── UserServiceImpl.java  // 用户服务实现
└── utils
    └── JwtUtil.java              // JWT工具类
```

#### module-core (核心启动模块)
```
└── ModuleCoreApplication.java    // 应用程序入口类
```

## 技术栈
- **Java 17**：使用Java 17作为开发语言
- **Spring Boot 3.5.3**：使用Spring Boot作为基础框架
- **Spring Security**：用于认证和授权
- **MyBatis-Plus**：增强的MyBatis ORM框架
- **Dynamic DataSource**：动态数据源支持
- **JWT**：用于生成和验证JSON Web Token
- **Redis**：用于缓存和黑名单管理
- **MySQL**：数据库

## 数据库表结构

### 用户表(user)
| 字段名 | 类型 | 描述 |
| --- | --- | --- |
| id | Integer | 用户ID，自增主键 |
| username | String | 用户名 |
| password | String | 用户密码 |

### 用户详情表(detail)
| 字段名 | 类型 | 描述 |
| --- | --- | --- |
| id | Integer | 用户ID，主键 |
| name | String | 用户的名字 |
| avatar_url | String | 用户头像链接 |

## API接口文档

### 认证接口

#### 用户注册
- **URL**: `/auth/register`
- **方法**: POST
- **请求体**: 
  ```json
  {
    "username": "用户名",
    "password": "密码"
  }
  ```
- **响应**: 
  ```json
  {
    "code": 200,
    "message": "注册成功",
    "data": null
  }
  ```

#### 用户登录
- **URL**: `/auth/login`
- **方法**: POST
- **请求体**: 
  ```json
  {
    "username": "用户名",
    "password": "密码"
  }
  ```
- **响应**: 
  ```json
  {
    "code": 200,
    "message": "登录成功",
    "data": {
      "accessToken": "访问令牌",
      "refreshToken": "刷新令牌"
    }
  }
  ```

#### 刷新令牌
- **URL**: `/auth/refresh`
- **方法**: POST
- **请求头**: 
  ```
  Authorization: Bearer 刷新令牌
  ```
- **响应**: 
  ```json
  {
    "code": 200,
    "message": "刷新成功",
    "data": {
      "accessToken": "新的访问令牌",
      "refreshToken": "新的刷新令牌"
    }
  }
  ```

#### 用户登出
- **URL**: `/auth/logout`
- **方法**: POST
- **请求头**: 
  ```
  Authorization: Bearer 访问令牌
  ```
- **响应**: 
  ```json
  {
    "code": 200,
    "message": "登出成功",
    "data": null
  }
  ```

### 用户接口

#### 获取用户信息
- **URL**: `/user/info`
- **方法**: GET
- **请求头**: 
  ```
  Authorization: Bearer 访问令牌
  ```
- **响应**: 
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "id": 1,
      "username": "用户名",
      "name": "用户的名字",
      "avatarUrl": "用户头像链接"
    }
  }
  ```

## 错误码说明

### 通用错误码
| 错误码 | 描述 |
| --- | --- |
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

### 认证模块错误码
| 错误码 | 描述 |
| --- | --- |
| 1001 | 用户名已存在 |
| 1002 | 无效的凭证 |
| 1003 | 无效的刷新令牌 |
| 1004 | 刷新令牌已被列入黑名单 |
| 1005 | 刷新令牌已过期 |
| 1006 | 刷新令牌失败 |

### 用户模块错误码
| 错误码 | 描述 |
| --- | --- |
| 2001 | 用户名不存在 |

## 安全配置
项目使用Spring Security进行安全配置，主要特点：
- 禁用CSRF保护
- 配置CORS支持
- 允许`/auth/**`路径的请求无需认证
- 其他请求需要认证
- 使用JWT认证过滤器进行身份验证
- 自定义认证失败和授权失败的响应处理

## 缓存配置
项目使用Redis进行缓存管理，主要用于：
- 存储JWT黑名单，防止已注销的令牌被重用
- 使用StringRedisSerializer序列化key
- 使用GenericJackson2JsonRedisSerializer序列化value

## 启动说明
1. 确保已安装Java 17
2. 确保MySQL服务已启动，并创建了名为`pp_user`的数据库
3. 确保Redis服务已启动
4. 修改`application.yml`中的数据库连接信息
5. 执行以下命令启动项目：
   ```bash
   mvn clean package
   java -jar module-core/target/module-core-0.0.1-SNAPSHOT.jar
   ```
6. 项目默认运行在8080端口