@echo off
chcp 65001 >nul
echo ========================================
echo       Auth Matrix 数据库部署脚本
echo ========================================
echo.

:: 选择 MySQL 类型
echo.
echo 请选择 MySQL 部署方式
echo 1 本地 MySQL
echo 2 Docker MySQL
echo.
set /p MYSQL_TYPE=请输入选项 1 或 2: 

if "%MYSQL_TYPE%"=="1" (
    set USE_DOCKER=false
    echo.
    echo [信息] 已选择本地 MySQL
) else if "%MYSQL_TYPE%"=="2" (
    set USE_DOCKER=true
    echo.
    echo [信息] 已选择 Docker MySQL
) else (
    echo [错误] 无效的选项
    pause
    exit /b 1
)

:: 根据选择检查环境
echo.
if "%USE_DOCKER%"=="true" (
    echo [预检查] 检测 Docker 环境
    docker --version >nul 2>&1
    if %errorlevel% neq 0 (
        echo [错误] 未找到 Docker 命令
        pause
        exit /b 1
    )
    echo [成功] Docker 环境检测通过
) else (
    echo [预检查] 检测本地 MySQL 环境
    mysql --version >nul 2>&1
    if %errorlevel% neq 0 (
        echo [错误] 未找到 MySQL 命令
        pause
        exit /b 1
    )
    echo [成功] MySQL 环境检测通过
)

:: 检查必要文件是否存在
echo.
echo [预检查] 检查 SQL 文件
if not exist "init.sql" (
    echo [错误] 找不到 init.sql 文件
    echo 请确保在 sql 目录下运行此脚本
    pause
    exit /b 1
)
if not exist "schema\am_user.sql" (
    echo [错误] 找不到 schema\am_user.sql 文件
    pause
    exit /b 1
)
if not exist "schema\am_system.sql" (
    echo [错误] 找不到 schema\am_system.sql 文件
    pause
    exit /b 1
)
if not exist "data\am_user.sql" (
    echo [错误] 找不到 data\am_user.sql 文件
    pause
    exit /b 1
)
if not exist "data\am_system.sql" (
    echo [错误] 找不到 data\am_system.sql 文件
    pause
    exit /b 1
)
echo [成功] SQL 文件检查通过

:: 获取 MySQL 连接信息
echo.
if "%USE_DOCKER%"=="true" (
    set /p DOCKER_CONTAINER=请输入 MySQL 容器名称 默认 mysql: 
    if "%DOCKER_CONTAINER%"=="" set DOCKER_CONTAINER=mysql
    
    set /p MYSQL_HOST=请输入 MySQL 主机地址 默认 127.0.0.1: 
    if "%MYSQL_HOST%"=="" set MYSQL_HOST=127.0.0.1
    
    set /p MYSQL_PORT=请输入 MySQL 端口 默认 3306: 
    if "%MYSQL_PORT%"=="" set MYSQL_PORT=3306
)

set /p MYSQL_PASSWORD=请输入 MySQL root 用户密码: 

echo.
echo [步骤 1/4] 正在创建数据库
if "%USE_DOCKER%"=="true" (
    type init.sql | docker exec -i %DOCKER_CONTAINER% mysql -h%MYSQL_HOST% -P%MYSQL_PORT% -u root -p%MYSQL_PASSWORD%
) else (
    mysql -u root -p%MYSQL_PASSWORD% < init.sql
)
if %errorlevel% neq 0 (
    echo [错误] 数据库创建失败 错误代码 %errorlevel%
    pause
    exit /b 1
)
echo [成功] 数据库创建完成

echo.
echo [步骤 2/4] 正在创建用户模块表结构
if "%USE_DOCKER%"=="true" (
    type schema\am_user.sql | docker exec -i %DOCKER_CONTAINER% mysql -h%MYSQL_HOST% -P%MYSQL_PORT% -u root -p%MYSQL_PASSWORD%
) else (
    mysql -u root -p%MYSQL_PASSWORD% < schema\am_user.sql
)
if %errorlevel% neq 0 (
    echo [错误] 用户模块表结构创建失败 错误代码 %errorlevel%
    pause
    exit /b 1
)
echo [成功] 用户模块表结构创建完成

echo.
echo [步骤 3/4] 正在创建系统模块表结构
if "%USE_DOCKER%"=="true" (
    type schema\am_system.sql | docker exec -i %DOCKER_CONTAINER% mysql -h%MYSQL_HOST% -P%MYSQL_PORT% -u root -p%MYSQL_PASSWORD%
) else (
    mysql -u root -p%MYSQL_PASSWORD% < schema\am_system.sql
)
if %errorlevel% neq 0 (
    echo [错误] 系统模块表结构创建失败 错误代码 %errorlevel%
    pause
    exit /b 1
)
echo [成功] 系统模块表结构创建完成

echo.
echo [步骤 4/4] 正在导入初始数据
echo 导入用户模块数据
if "%USE_DOCKER%"=="true" (
    type data\am_user.sql | docker exec -i %DOCKER_CONTAINER% mysql -h%MYSQL_HOST% -P%MYSQL_PORT% -u root -p%MYSQL_PASSWORD%
) else (
    mysql -u root -p%MYSQL_PASSWORD% < data\am_user.sql
)
if %errorlevel% neq 0 (
    echo [错误] 用户模块数据导入失败 错误代码 %errorlevel%
    pause
    exit /b 1
)

echo 导入系统模块数据
if "%USE_DOCKER%"=="true" (
    type data\am_system.sql | docker exec -i %DOCKER_CONTAINER% mysql -h%MYSQL_HOST% -P%MYSQL_PORT% -u root -p%MYSQL_PASSWORD%
) else (
    mysql -u root -p%MYSQL_PASSWORD% < data\am_system.sql
)
if %errorlevel% neq 0 (
    echo [错误] 系统模块数据导入失败 错误代码 %errorlevel%
    pause
    exit /b 1
)

echo.
echo ========================================
echo           部署完成
echo ========================================
echo.
echo 默认管理员账号信息
echo 用户名 100001
echo 密码 Am20250914
echo 角色 超级管理员
echo.
echo 数据库连接信息
echo 用户数据库 am_user
echo 系统数据库 am_system
echo.
echo 请确保在项目中配置正确的数据库连接参数
echo 参考文档 document/3.数据库部署指南.md
echo.
pause
