package com.project.matchingsystem.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.util.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtProvider.resolveAccessToken(request);

        if(accessToken!=null){
            if(!jwtProvider.validateToken(accessToken)){
                jwtExceptionHandler(response, ErrorCode.INVALID_TOKEN.getMessage(), HttpStatus.UNAUTHORIZED);
                return;
            }
            // 로그아웃으로 accessToken이 블랙리스트에 있는지 확인
            String isLogout = redisUtil.getAccessToken(accessToken);
            if(ObjectUtils.isEmpty(isLogout)) {
                Claims info = jwtProvider.getUserInfoFromToken(accessToken);
                setAuthentication(info.getSubject());
            }
        }
        filterChain.doFilter(request,response);
    }

    public void setAuthentication(String username){
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtProvider.createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus httpStatus) {
        response.setStatus(httpStatus.value());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new ResponseStatusDto(httpStatus.toString(), msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
