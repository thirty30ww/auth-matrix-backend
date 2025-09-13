# 数据库部署指南

## 快速部署

### 方式一：自动化脚本（推荐）

#### Windows 用户

1. 确保 MySQL 服务正在运行
2. 进入项目的 `sql/` 目录
3. 右键点击 `setup.bat`，选择"以管理员身份运行"
4. 按照提示输入 MySQL root 密码
5. 等待脚本执行完成

```cmd
cd sql
setup.bat
```

### 方式二：手动部署

如果自动化脚本执行失败，可以按以下步骤手动部署：

```bash
# 1. 创建数据库
mysql -u root -p < sql/init.sql

# 2. 创建表结构
mysql -u root -p < sql/schema/pp_user.sql
mysql -u root -p < sql/schema/pp_system.sql

# 3. 导入初始数据
mysql -u root -p < sql/data/pp_user.sql
mysql -u root -p < sql/data/pp_system.sql
```

## 默认数据

### 管理员账号

部署完成后，系统将包含以下默认管理员账号：

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| 100001 | 123456 | 超级管理员 | 拥有所有权限 |
| 100002 | 123456 | 组织管理员 | 组织管理权限 |

### 角色层次结构

```
超级管理员 (level 0)
├── 系统管理员 (level 1)
├── 组织管理员 (level 1)
│   └── 部门管理员 (level 2)
└── 游客 (level -1)
```

## 数据源切换机制

项目使用 AOP 自动数据源切换，无需手动指定：

- `com.thirty.user.service.*` → 自动使用 `user` 数据源
- `com.thirty.system.service.*` → 自动使用 `system` 数据源

切换逻辑参见：`module-core/src/main/java/com/thirty/core/aspect/AutoDataSourceAspect.java`