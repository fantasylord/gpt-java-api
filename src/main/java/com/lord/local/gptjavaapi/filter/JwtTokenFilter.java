package com.lord.local.gptjavaapi.filter;

import com.alibaba.fastjson2.JSON;
import com.lord.local.gptjavaapi.model.resultful.ChatBaseResponse;
import com.lord.local.gptjavaapi.service.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.UTF8;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        //过滤swagger
        String path = request.getRequestURI();
        if (path.toLowerCase().startsWith("/swagger")
                || path.equalsIgnoreCase("/v2/api-docs")
                || path.equalsIgnoreCase("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }
        //过滤 注册 登录
        if (path.equalsIgnoreCase("/user/create")
                || path.equalsIgnoreCase("/user/getToken")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtTokenProvider.validateToken(token)) {
                String username = jwtTokenProvider.getUsernameFromToken(token);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                ChatBaseResponse chatBaseResponse = ChatBaseResponse.errorResponse("token过期", -1004);
                response.getWriter().write(JSON.toJSONString(chatBaseResponse));
                return;
            }
        } else {

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ChatBaseResponse chatBaseResponse = ChatBaseResponse.errorResponse("请先登录", -1004);
            response.getWriter().write(JSON.toJSONString(chatBaseResponse));
            return;
        }
        filterChain.doFilter(request, response);
    }
}
