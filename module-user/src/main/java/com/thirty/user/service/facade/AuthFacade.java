package com.thirty.user.service.facade;

import com.thirty.user.model.dto.LoginDTO;
import com.thirty.user.model.vo.JwtVO;
import org.springframework.stereotype.Service;

@Service
public interface AuthFacade {
    /**
     * 用户登录
     * @param loginDTO 登录DTO
     * @return JwtVO
     */
    JwtVO login(LoginDTO loginDTO);

    /**
     * 刷新令牌
     * @param refreshToken 刷新令牌
     * @return JwtVO
     */
    JwtVO refreshToken(String refreshToken);
}
