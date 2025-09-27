@echo off
chcp 65001 >nul
echo ========================================
echo       Auth Matrix 数据库部署脚本
echo ========================================
echo.

:: 检查是否有管理员权限
net session >nul 2>&1
if %errorLevel% == 0 (
    echo [信息] 检测到管理员权限
) else (
    echo [警告] 建议以管理员身份运行此脚本
)

:: 检查 MySQL 是否可用
echo.
echo [预检查] 检测 MySQL 环境...
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未找到 MySQL 命令！
    echo 请确保：
    echo 1. MySQL 已正确安装
    echo 2. MySQL 的 bin 目录已添加到系统 PATH 环境变量
    echo 3. 或者使用完整路径，如：C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe
    pause
    exit /b 1
)
echo [成功] MySQL 环境检测通过

:: 检查必要文件是否存在
echo.
echo [预检查] 检查 SQL 文件...
if not exist "init.sql" (
    echo [错误] 找不到 init.sql 文件！
    echo 请确保在 sql 目录下运行此脚本。
    pause
    exit /b 1
)
if not exist "schema\pp_user.sql" (
    echo [错误] 找不到 schema\pp_user.sql 文件！
    pause
    exit /b 1
)
if not exist "schema\pp_system.sql" (
    echo [错误] 找不到 schema\pp_system.sql 文件！
    pause
    exit /b 1
)
if not exist "data\pp_user.sql" (
    echo [错误] 找不到 data\pp_user.sql 文件！
    pause
    exit /b 1
)
if not exist "data\pp_system.sql" (
    echo [错误] 找不到 data\pp_system.sql 文件！
    pause
    exit /b 1
)
echo [成功] SQL 文件检查通过

:: 获取 MySQL 密码（只需输入一次）
echo.
set /p MYSQL_PASSWORD=请输入 MySQL root 用户密码: 

echo.
echo [步骤 1/4] 正在创建数据库...
mysql -u root -p%MYSQL_PASSWORD% < init.sql
if %errorlevel% neq 0 (
    echo [错误] 数据库创建失败！错误代码：%errorlevel%
    echo 可能的原因：
    echo 1. MySQL 密码错误
    echo 2. MySQL 服务未启动
    echo 3. 权限不足
    pause
    exit /b 1
)
echo [成功] 数据库创建完成

echo.
echo [步骤 2/4] 正在创建用户模块表结构...
mysql -u root -p%MYSQL_PASSWORD% < schema\pp_user.sql
if %errorlevel% neq 0 (
    echo [错误] 用户模块表结构创建失败！错误代码：%errorlevel%
    pause
    exit /b 1
)
echo [成功] 用户模块表结构创建完成

echo.
echo [步骤 3/4] 正在创建系统模块表结构...
mysql -u root -p%MYSQL_PASSWORD% < schema\pp_system.sql
if %errorlevel% neq 0 (
    echo [错误] 系统模块表结构创建失败！错误代码：%errorlevel%
    pause
    exit /b 1
)
echo [成功] 系统模块表结构创建完成

echo.
echo [步骤 4/4] 正在导入初始数据...
echo 导入用户模块数据...
mysql -u root -p%MYSQL_PASSWORD% < data\pp_user.sql
if %errorlevel% neq 0 (
    echo [错误] 用户模块数据导入失败！错误代码：%errorlevel%
    pause
    exit /b 1
)

echo 导入系统模块数据...
mysql -u root -p%MYSQL_PASSWORD% < data\pp_system.sql
if %errorlevel% neq 0 (
    echo [错误] 系统模块数据导入失败！错误代码：%errorlevel%
    pause
    exit /b 1
)

echo.
echo ========================================
echo           部署完成！
echo ========================================
echo.
echo 默认管理员账号信息：
echo 用户名: 100001
echo 密码: Am20250914
echo 角色: 超级管理员
echo.
echo 数据库连接信息：
echo 用户数据库: pp_user
echo 系统数据库: pp_system
echo.
echo 请确保在项目中配置正确的数据库连接参数。
echo 参考文档: document/3.数据库部署指南.md
echo.
pause