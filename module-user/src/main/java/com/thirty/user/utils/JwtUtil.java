package com.thirty.user.utils;

import com.thirty.user.constant.AuthConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtUtil {

    // JWT密钥，从配置文件中读取
    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    /**
     * 1. 在构造方法执行完后
     * 2. 在依赖注入完成后
     * 3. 在对象被实际使用前
     * 自动调用这个方法
     */
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
    
    // 访问令牌有效期（毫秒）
    @Value("${jwt.expiration}")
    private long expiration;

    // 刷新令牌有效期（毫秒）
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;
    
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 从token中提取用户名
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 从Authorization头中提取token
     */
    public String extractToken(String authHeader) {
        return authHeader.substring(AuthConstant.BEARER_PREFIX_LENGTH);
    }

    /**
     * 从Authorization头中提取用户名
     */
    public String getUsernameFromAuthHeader(String authHeader) {
        String token = extractToken(authHeader);
        return extractUsername(token);
    }



    /**
     * 从token中提取过期时间
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 从token中提取指定的claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从token中提取所有的claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 检查token是否过期
     */
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 为用户生成访问令牌
     */
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(AuthConstant.TOKEN_TYPE_CLAIM, AuthConstant.ACCESS_TOKEN_TYPE);
        return createToken(claims, userDetails.getUsername(), expiration);
    }
    
    /**
     * 为用户生成刷新令牌
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(AuthConstant.TOKEN_TYPE_CLAIM, AuthConstant.REFRESH_TOKEN_TYPE);
        return createToken(claims, userDetails.getUsername(), refreshExpiration);
    }

    /**
     * 创建token
     */
    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 验证token是否有效
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // 检查token是否在黑名单中
        if (isAccessToken(token) && isAccessTokenInBlacklist(token)) {
            return false;
        }
        if (isRefreshToken(token) && isRefreshTokenInBlacklist(token)) {
            return false;
        }
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    /**
     * 检查是否是刷新令牌
     */
    public boolean isRefreshToken(String token) {
        try {
            final Claims claims = extractAllClaims(token);
            return AuthConstant.REFRESH_TOKEN_TYPE.equals(claims.get(AuthConstant.TOKEN_TYPE_CLAIM));
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 检查是否是访问令牌
     */
    public boolean isAccessToken(String token) {
        try {
            final Claims claims = extractAllClaims(token);
            return AuthConstant.ACCESS_TOKEN_TYPE.equals(claims.get(AuthConstant.TOKEN_TYPE_CLAIM)) || claims.get(AuthConstant.TOKEN_TYPE_CLAIM) == null; // 兼容旧令牌
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 将访问令牌加入黑名单
     * @param token JWT访问令牌
     */
    public void addAccessTokenToBlacklist(String token) {
        String key = AuthConstant.TOKEN_BLACKLIST_PREFIX + AuthConstant.ACCESS_TOKEN_BLACKLIST_SUFFIX + token;
        long remainingTime = extractExpiration(token).getTime() - System.currentTimeMillis();
        if (remainingTime > 0) {
            redisTemplate.opsForValue().set(key, AuthConstant.BLACKLIST_VALUE, remainingTime, TimeUnit.MILLISECONDS);
        }
    }
    
    /**
     * 将刷新令牌加入黑名单
     * @param token JWT刷新令牌
     */
    public void addRefreshTokenToBlacklist(String token) {
        String key = AuthConstant.TOKEN_BLACKLIST_PREFIX + AuthConstant.REFRESH_TOKEN_BLACKLIST_SUFFIX + token;
        long remainingTime = extractExpiration(token).getTime() - System.currentTimeMillis();
        if (remainingTime > 0) {
            redisTemplate.opsForValue().set(key, AuthConstant.BLACKLIST_VALUE, remainingTime, TimeUnit.MILLISECONDS);
        }
    }
    
    /**
     * 检查访问令牌是否在黑名单中
     * @param token JWT访问令牌
     * @return 是否在黑名单中
     */
    public boolean isAccessTokenInBlacklist(String token) {
        String key = AuthConstant.TOKEN_BLACKLIST_PREFIX + AuthConstant.ACCESS_TOKEN_BLACKLIST_SUFFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    
    /**
     * 检查刷新令牌是否在黑名单中
     * @param token JWT刷新令牌
     * @return 是否在黑名单中
     */
    public boolean isRefreshTokenInBlacklist(String token) {
        String key = AuthConstant.TOKEN_BLACKLIST_PREFIX + AuthConstant.REFRESH_TOKEN_BLACKLIST_SUFFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}