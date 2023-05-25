package com.lord.local.gptjavaapi.target.aspect;

import com.lord.local.gptjavaapi.target.ApiAuth;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApiAuthAspect {
    @Before("@annotation(apiAuth) || @within(apiAuth)")
    public void checkApiAuth(ApiAuth apiAuth) {
        // 在这里编写处理逻辑，例如验证认证信息
    }
}
