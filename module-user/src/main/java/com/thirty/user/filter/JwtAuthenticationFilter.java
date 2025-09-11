package com.thirty.user.filter;

import com.thirty.user.constant.AuthConstant;
import com.thirty.user.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private JwtUtil jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 检查Authorization头是否存在且以"Bearer "开头
        if (authorizationHeader != null && authorizationHeader.startsWith(AuthConstant.BEARER_PREFIX)) {
            jwt = authorizationHeader.substring(AuthConstant.BEARER_PREFIX_LENGTH);
            try {
                username = jwtUtils.extractUsername(jwt);
                
                // 刷新令牌不应该用于访问API，只有访问令牌才能用于认证
                if (jwtUtils.isRefreshToken(jwt) && !request.getRequestURI().endsWith("/auth/refresh")) {
                    filterChain.doFilter(request, response);
                    return;
                }
            } catch (Exception e) {
                // 这不是真正的异常，只是token无效或已过期
                // 继续过滤链，让SecurityContext保持为空，后续的安全配置会处理未认证的请求
                filterChain.doFilter(request, response);
                return;
            }
        }

        // 如果成功提取用户名且SecurityContext中没有认证信息，则进行认证
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 检查访问令牌是否在黑名单中
            if (!jwtUtils.isAccessTokenInBlacklist(jwt)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (!userDetails.isEnabled()) {
                    // 用户被禁用，拒绝访问
                    filterChain.doFilter(request, response);
                    return;
                }
    
                // 验证token是否有效
                if (jwtUtils.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}