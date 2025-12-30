# Auth Matrix Backend

> 基于 RBAC1.0 的企业级权限管理系统后端

[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen)](https://spring.io/projects/spring-boot)
[![MyBatis Plus](https://img.shields.io/badge/MyBatis%20Plus-3.5.7-blue)](https://baomidou.com/)
[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)

## 核心特性

### 智能化架构设计
- **AOP自动数据源切换**: 基于包路径智能路由，`user` 和 `system` 业务完全解耦
- **模块化Maven架构**: 4大核心模块独立开发，支持灵活组合和扩展
- **双数据库分离设计**: 用户权限与系统配置数据物理隔离，支持独立扩展

### 完整RBAC1权限体系
- **四级权限粒度控制**: 目录→菜单→页面→按钮的细粒度权限管控
- **动态权限树构建**: 基于用户角色实时生成个性化菜单树
- **权限码验证机制**: 支持方法级和接口级的权限拦截
- **角色继承机制**: 支持角色层次体系，上级角色自动继承下级权限c

### 开发体验优化
- **零配置快速启动**: 一键脚本完成从数据库到服务的全流程部署
- **多环境安全配置**: 敏感信息环境隔离，支持开发/生产无缝切换
- **交互式API文档**: SpringDoc自动生成，支持在线调试
- **Maven Wrapper集成**: 无需预装Maven，克隆即可运行

## 技术栈

| 类别 | 技术 | 版本 | 说明 |
|------|------|------|------|
| 核心框架 | Spring Boot | 3.5.3 | 应用框架 |
| 安全框架 | Spring Security | 6.x | 安全认证 |
| ORM框架 | MyBatis Plus | 3.5.7 | 数据持久化 |
| 数据库 | MySQL | 8.0+ | 关系型数据库 |
| 缓存 | Redis | 6.0+ | 分布式缓存 |
| 认证 | JWT | 0.11.5 | 无状态认证 |
| 多数据源 | Dynamic Datasource | 3.6.1 | 动态数据源切换 |
| 对象映射 | MapStruct | 1.5.5 | 对象转换 |
| API文档 | SpringDoc | 2.7.0 | OpenAPI 3.0 |
| 云存储 | Aliyun OSS | 3.16.1 | 文件存储 |

## 模块架构

```
backend/
├── am-core          # 核心启动模块
│   ├── 统一配置管理
│   ├── 服务编排
│   ├── AOP数据源切换
│   └── 全局异常处理
├── am-user          # 用户权限模块
│   ├── 用户管理
│   ├── 角色管理
│   ├── 菜单权限管理
│   └── RBAC核心实现
├── am-system        # 系统管理模块
│   ├── 系统配置
│   ├── 登录日志
│   ├── 操作日志
│   └── 文件上传
├── am-common        # 通用组件模块
│   ├── 工具类
│   ├── 枚举定义
│   ├── 异常定义
│   └── 通用实体
├── sql/             # 数据库脚本
│   ├── init.sql         # 数据库初始化
│   ├── schema/          # 表结构定义
│   ├── data/            # 初始数据
│   └── setup.bat        # 自动化部署脚本
└── document/        # 部署文档
    ├── 1.快速开始.md
    ├── 2.环境配置指南.md
    ├── 3.数据库部署指南.md
    └── 4.依赖安装指南.md
```

## 快速开始

> [!IMPORTANT]
> 请按顺序阅读文档完成部署

### 启动文档

1. **环境准备** - [快速开始](document/启动文档/1.快速开始.md)
2. **环境配置** - [环境配置指南](document/启动文档/2.环境配置指南.md)
3. **数据库部署** - [数据库部署指南](document/启动文档/3.数据库部署指南.md)
4. **依赖安装** - [依赖安装指南](document/启动文档/4.依赖安装指南.md)

### 开发文档

如需在本项目基础上进行二次开发，请参考：

1. **项目架构说明** - [1.项目架构说明.md](document/开发文档/1.项目架构说明.md)
2. **创建新模块** - [2.创建新模块.md](document/开发文档/2.创建新模块.md)
3. **开发业务代码** - [3.开发业务代码.md](document/开发文档/3.开发业务代码.md)
4. **权限控制集成** - [4.权限控制集成.md](document/开发文档/4.权限控制集成.md)
5. **使用现有模块** - [5.使用现有模块.md](document/开发文档/5.使用现有模块.md)

### 启动服务

```bash
# Windows
.\mvnw.cmd spring-boot:run -pl am-core

# macOS/Linux
./mvnw spring-boot:run -pl am-core
```

### 验证

- API 文档: http://localhost:8080/doc.html
- 默认账号: 100001 / Am20250914

## 权限管理体系

### RBAC1.0 模型

本系统实现了完整的 RBAC1.0（Role-Based Access Control）权限模型，支持角色继承机制。

### 角色层次体系

> [!NOTE]
> 以下是系统预置的角色层次结构，可根据业务需求自定义调整

```
超级管理员 (level 0)
├── 系统管理员 (level 1)
├── 用户管理员 (level 1)
│   └── 违规监督员 (level 2)
├── 权限管理员 (level 1)
│   ├── 角色管理员 (level 2)
│   └── 菜单管理员 (level 2)
├── 日志管理员 (level 1)
└── 普通用户 (level -1)
```

**角色继承规则**
- 上级角色自动继承下级角色的所有权限
- level 值越小，权限级别越高
- 超级管理员（level 0）拥有系统最高权限

### 权限控制维度

| 维度 | 说明 | 实现方式 |
|------|------|----------|
| 菜单权限 | 控制用户可见的菜单项 | 动态菜单树生成 |
| 页面权限 | 控制用户可访问的页面 | 路由级别拦截 |
| 按钮权限 | 控制页面内的操作按钮 | 权限码验证 |
| 数据权限 | 控制用户可见的数据范围 | 可扩展实现 |

### 权限验证流程

```
用户请求
    ↓
JWT Token 验证
    ↓
获取用户角色
    ↓
加载角色权限（含继承权限）
    ↓
权限码匹配
    ↓
允许/拒绝访问
```

## 相关项目

- **前端项目**: [Auth Matrix Frontend](https://github.com/thirty30ww/auth-matrix-frontend) - Vue3 + Element Plus 权限管理前端
- **元仓库**: [Auth Matrix](https://github.com/thirty30ww/auth-matrix) - 包含前后端项目及版本发布的元仓库

## 开源协议

本项目基于 [MIT](LICENSE) 协议开源，支持商业使用和二次开发。

---

如果这个项目对你有帮助，欢迎给个 Star 支持一下

