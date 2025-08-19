# 环境配置说明

本项目使用环境变量来管理敏感配置信息。在运行项目之前，您需要设置以下环境变量：

## 必需的环境变量

### 数据库配置
```bash
DB_URL=jdbc:mysql://localhost:3306/pp_user?allowPublicKeyRetrieval=true&useSSL=false
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password
```

### Redis 配置
```bash
REDIS_HOST=localhost
REDIS_PORT=6379
```

### JWT 配置
```bash
JWT_SECRET=your_256_bit_secret_key_here
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000
```

### 阿里云 OSS 配置
```bash
ALIYUN_OSS_ENDPOINT=oss-cn-hangzhou.aliyuncs.com
ALIYUN_OSS_ACCESS_KEY_ID=your_access_key_id
ALIYUN_OSS_ACCESS_KEY_SECRET=your_access_key_secret
ALIYUN_OSS_BUCKET_NAME=your_bucket_name
ALIYUN_OSS_DOMAIN=https://your_bucket_name.oss-cn-hangzhou.aliyuncs.com
```

## 配置方法

### 方法一：使用 .env 文件（推荐）

1. 复制 `application-template.yml` 为 `application.yml`
2. 在项目根目录创建 `.env` 文件：

```bash
# 数据库配置
DB_URL=jdbc:mysql://localhost:3306/pp_user?allowPublicKeyRetrieval=true&useSSL=false
DB_USERNAME=root
DB_PASSWORD=your_password

# Redis 配置
REDIS_HOST=localhost
REDIS_PORT=6379

# JWT 配置
JWT_SECRET=v1Baq3mNoF6pXjGsGkgNx8TuHn5rYzE0lPwDcViW7b9CtAeSdQkFf2jR4IyM6ZOU
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# 阿里云 OSS 配置
ALIYUN_OSS_ENDPOINT=oss-cn-hangzhou.aliyuncs.com
ALIYUN_OSS_ACCESS_KEY_ID=your_access_key_id
ALIYUN_OSS_ACCESS_KEY_SECRET=your_access_key_secret
ALIYUN_OSS_BUCKET_NAME=your_bucket_name
ALIYUN_OSS_DOMAIN=https://your_bucket_name.oss-cn-hangzhou.aliyuncs.com
```

### 方法二：系统环境变量

在操作系统中设置环境变量：

#### Windows
```cmd
set DB_URL=jdbc:mysql://localhost:3306/pp_user?allowPublicKeyRetrieval=true&useSSL=false
set DB_USERNAME=your_username
set DB_PASSWORD=your_password
# ... 其他变量
```

#### Linux/macOS
```bash
export DB_URL="jdbc:mysql://localhost:3306/pp_user?allowPublicKeyRetrieval=true&useSSL=false"
export DB_USERNAME="your_username"
export DB_PASSWORD="your_password"
# ... 其他变量
```

### 方法三：IDE 配置

在您的 IDE（如 IntelliJ IDEA）中配置运行时环境变量。

## 安全注意事项

1. **永远不要** 将包含真实敏感信息的配置文件提交到版本控制系统
2. `.env` 文件已被添加到 `.gitignore` 中，确保不会被意外提交
3. JWT 密钥应该是安全的 256 位随机字符串
4. 定期更换数据库密码和 API 密钥
5. 在生产环境中使用更安全的密钥管理服务

## 开发团队协作

1. 每个开发者需要自己创建 `.env` 文件
2. 可以创建 `.env.example` 文件作为模板（不包含真实密码）
3. 在项目文档中说明必需的环境变量

## 验证配置

启动应用程序后，检查日志确认所有配置都已正确加载。如果有配置错误，应用程序会在启动时报错。