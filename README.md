# 🔐 Auth Matrix Backend

> 基于 RBAC1.0 的企业级权限管理系统后端 - 模块化架构，智能路由，开箱即用

[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-green)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)

## ✨ 核心特色

### 🏗️ 智能化架构设计
- **AOP自动数据源切换**：基于包路径智能路由，`user` 和 `system` 业务完全解耦
- **模块化Maven架构**：4大核心模块独立开发，支持灵活组合和扩展
- **双数据库分离设计**：用户权限与系统配置数据物理隔离，支持独立扩展

### 🎯 完整RBAC1权限体系
- **4维权限粒度控制**：目录→菜单→页面→按钮的细粒度权限管控
- **动态权限树构建**：基于用户角色实时生成个性化菜单树
- **权限码验证机制**：支持方法级和接口级的权限拦截

### 🚀 开发体验优化
- **零配置快速启动**：一键脚本完成从数据库到服务的全流程部署
- **多环境安全配置**：敏感信息环境隔离，支持开发/生产无缝切换
- **交互式API文档**：SpringDoc自动生成，支持在线调试
- **Maven Wrapper集成**：无需预装Maven，克隆即可运行

## 🛠️ 技术栈

**核心框架**：Spring Boot 3.5.3 + Spring Security 6.x + MyBatis Plus 3.5.7  
**数据存储**：MySQL 8.0 + Redis 6.0  
**权限认证**：JWT 0.11.5 + 自定义RBAC实现  
**多数据源**：Dynamic Datasource 3.6.1 + AOP智能切换  

## 📁 模块架构

```
auth-matrix-backend/
├── 🚀 module-core          # 核心启动模块 - 统一配置与服务编排
├── 👥 module-user          # 用户权限模块 - RBAC核心实现
├── ⚙️ module-system        # 系统管理模块 - 配置与日志管理  
├── 🔧 module-common        # 通用组件模块 - 共享工具与枚举
├── 📊 sql/                 # 数据库脚本 - 自动化部署脚本
└── 📖 document/           # 详细文档 - 分步部署指南
```

## ⚡快速启动

> 💡 详细部署步骤请按顺序阅读：[快速开始](document/1.快速开始.md) → [环境配置](document/2.环境配置指南.md) → [数据库部署](document/3.数据库部署指南.md)

## 🎯 权限管理能力

### 角色层次体系
RBAC1的关键特色，角色可以继承其下级角色的权限，从而实现权限的层级管理。
```
超级管理员 (level 0) - 系统最高权限
├── 系统管理员 (level 1) - 系统配置管理
├── 用户管理员 (level 1) - 用户生命周期管理
│   └── 违规监督员 (level 2) - 用户行为监管
├── 权限管理员 (level 1) - 权限分配管理
│   ├── 角色管理员 (level 2) - 角色CRUD操作
│   └── 菜单管理员 (level 2) - 菜单权限配置
├── 日志管理员 (level 1) - 系统日志查看
└── 游客 (level -1) - 基础访问权限
```

### 权限控制维度
- **菜单权限**：动态菜单树生成，按角色显示可访问菜单
- **页面权限**：路由级别访问控制，未授权页面自动拦截
- **按钮权限**：操作按钮显隐控制，精确到每个功能点
- **数据权限**：基于角色的数据范围限制（可扩展）

这是一套示例的角色层次体系，使用者可以根据业务需求进行删除及调整

## 🔗 生态项目

- **前端项目**：[Auth Matrix Frontend](https://github.com/thirty30ww/auth-matrix-frontend) - Vue3 + Element Plus权限管理前端
- **Docker部署**：支持容器化部署

## 📄 开源协议

本项目基于 [MIT](LICENSE) 协议开源，支持商业使用和二次开发。

---

**🌟 企业级权限管理解决方案，如果对你有帮助请给个Star！**

**🔗 配套前端项目**：[Auth Matrix Frontend →](https://github.com/thirty30ww/auth-matrix-frontend)

