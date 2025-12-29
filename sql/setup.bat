@echo off
setlocal enabledelayedexpansion
chcp 65001 >nul 2>nul

echo.
echo ========================================
echo    Auth Matrix 数据库部署脚本
echo ========================================
echo.
echo 请选择 MySQL 部署方式:
echo   1. 本地 MySQL
echo   2. Docker MySQL
echo.
set /p "MYSQL_TYPE=请输入选项 (1 或 2): "

if "%MYSQL_TYPE%"=="1" (
    set "USE_DOCKER=false"
) else if "%MYSQL_TYPE%"=="2" (
    set "USE_DOCKER=true"
) else (
    echo.
    echo ERROR: 无效的选项，请输入 1 或 2
    goto end
)

if "%USE_DOCKER%"=="true" (
    echo.
    echo INFO: 已选择 Docker MySQL
) else (
    echo.
    echo INFO: 已选择本地 MySQL
)

echo.
echo --- 检测环境 ---

if "%USE_DOCKER%"=="true" (
    docker --version >nul 2>&1
    if !errorlevel! neq 0 (
        echo ERROR: 未找到 Docker，请先安装 Docker
        goto end
    )
    echo OK: Docker 环境正常
) else (
    mysql --version >nul 2>&1
    if !errorlevel! neq 0 (
        echo ERROR: 未找到 MySQL，请先安装 MySQL
        goto end
    )
    echo OK: MySQL 环境正常
)

echo --- 验证文件 ---

if not exist "init.sql" (
    echo ERROR: 找不到 init.sql 文件
    echo 请确保在 sql 目录下运行此脚本
    goto end
)
if not exist "schema\am_user.sql" (
    echo ERROR: 找不到 schema\am_user.sql 文件
    goto end
)
if not exist "schema\am_system.sql" (
    echo ERROR: 找不到 schema\am_system.sql 文件
    goto end
)
if not exist "data\am_user.sql" (
    echo ERROR: 找不到 data\am_user.sql 文件
    goto end
)
if not exist "data\am_system.sql" (
    echo ERROR: 找不到 data\am_system.sql 文件
    goto end
)
echo OK: 文件完整

echo.
echo ----------------------------------------
echo 请输入 MySQL 连接信息
echo ----------------------------------------
if "%USE_DOCKER%"=="true" (
    set /p "DOCKER_CONTAINER=容器名称 (默认: mysql): "
    if "!DOCKER_CONTAINER!"=="" set "DOCKER_CONTAINER=mysql"
    
    set /p "MYSQL_HOST=主机地址 (默认: 127.0.0.1): "
    if "!MYSQL_HOST!"=="" set "MYSQL_HOST=127.0.0.1"
    
    set /p "MYSQL_PORT=端口 (默认: 3306): "
    if "!MYSQL_PORT!"=="" set "MYSQL_PORT=3306"
)

:input_password
set /p "MYSQL_PASSWORD=root 密码: "

echo.
echo --- 验证密码 ---
if "%USE_DOCKER%"=="true" (
    echo SELECT 1; | docker exec -i !DOCKER_CONTAINER! mysql -h!MYSQL_HOST! -P!MYSQL_PORT! -u root -p!MYSQL_PASSWORD! >nul 2>&1
) else (
    echo SELECT 1; | mysql -u root -p!MYSQL_PASSWORD! >nul 2>&1
)
if !errorlevel! neq 0 (
    echo ERROR: 密码错误或无法连接数据库
    echo.
    goto input_password
)
echo OK: 密码验证通过

echo.
echo ========================================
echo 开始部署数据库
echo ========================================
echo.

echo Step 1/4: 正在创建数据库...
if "%USE_DOCKER%"=="true" (
    type init.sql | docker exec -i !DOCKER_CONTAINER! mysql -h!MYSQL_HOST! -P!MYSQL_PORT! -u root -p!MYSQL_PASSWORD! 2>nul
) else (
    mysql -u root -p!MYSQL_PASSWORD! < init.sql 2>nul
)
if !errorlevel! neq 0 (
    echo ERROR: 数据库创建失败
    goto end
)
echo OK: 完成

echo Step 2/4: 正在创建用户模块表结构...
if "%USE_DOCKER%"=="true" (
    type schema\am_user.sql | docker exec -i !DOCKER_CONTAINER! mysql -h!MYSQL_HOST! -P!MYSQL_PORT! -u root -p!MYSQL_PASSWORD! 2>nul
) else (
    mysql -u root -p!MYSQL_PASSWORD! < schema\am_user.sql 2>nul
)
if !errorlevel! neq 0 (
    echo ERROR: 用户模块表结构创建失败
    goto end
)
echo OK: 完成

echo Step 3/4: 正在创建系统模块表结构...
if "%USE_DOCKER%"=="true" (
    type schema\am_system.sql | docker exec -i !DOCKER_CONTAINER! mysql -h!MYSQL_HOST! -P!MYSQL_PORT! -u root -p!MYSQL_PASSWORD! 2>nul
) else (
    mysql -u root -p!MYSQL_PASSWORD! < schema\am_system.sql 2>nul
)
if !errorlevel! neq 0 (
    echo ERROR: 系统模块表结构创建失败
    goto end
)
echo OK: 完成

echo Step 4/4: 正在导入初始数据...
if "%USE_DOCKER%"=="true" (
    type data\am_user.sql | docker exec -i !DOCKER_CONTAINER! mysql -h!MYSQL_HOST! -P!MYSQL_PORT! -u root -p!MYSQL_PASSWORD! 2>nul
) else (
    mysql -u root -p!MYSQL_PASSWORD! < data\am_user.sql 2>nul
)
if !errorlevel! neq 0 (
    echo ERROR: 用户模块数据导入失败
    goto end
)
echo OK: 用户模块数据完成

if "%USE_DOCKER%"=="true" (
    type data\am_system.sql | docker exec -i !DOCKER_CONTAINER! mysql -h!MYSQL_HOST! -P!MYSQL_PORT! -u root -p!MYSQL_PASSWORD! 2>nul
) else (
    mysql -u root -p!MYSQL_PASSWORD! < data\am_system.sql 2>nul
)
if !errorlevel! neq 0 (
    echo ERROR: 系统模块数据导入失败
    goto end
)
echo OK: 系统模块数据完成

echo.
echo ========================================
echo           部署完成！
echo ========================================
echo.
echo 默认管理员账号:
echo   用户名: 100001
echo   密码: Am20250914
echo   角色: 超级管理员
echo.
echo 数据库信息:
echo   用户数据库: am_user
echo   系统数据库: am_system
echo.
echo 请在项目中配置正确的数据库连接参数
echo 参考文档: document/3.数据库部署指南.md
echo.

:end
pause
