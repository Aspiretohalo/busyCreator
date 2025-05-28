package com.busymango.busymangoBackend.core.aop;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.busymango.busymangoBackend.user.mapper.UserMapper;
import com.busymango.busymangoBackend.user.model.context.UserContext;
import com.busymango.busymangoBackend.user.model.dto.UserDTO;
import com.busymango.busymangoBackend.user.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
@Order(1) // 确保 UserAspect 先执行
public class UserAspect {
    @Resource
    private UserMapper userMapper;

    // 定义切入点，匹配所有不包含 IsLogin 注解的方法
    @Pointcut("execution(* com.busymango.busymangoBackend..*.controller.*.*(..)) && !@annotation(com.busymango.busymangoBackend.core.annotation.IsLogin)")
    public void notIsLogin() {
    }

    @Around("notIsLogin()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
            JWT jwt = JWTUtil.parseToken(token);
            String userAccount = (String) jwt.getPayload("userAccount");
            User user = userMapper.selectUserByAccount(userAccount);

            if (user == null) {
                log.warn("User not found: {}", userAccount);
                throw new RuntimeException("User not found");
            }

            UserDTO userDTO = new UserDTO();
            userDTO.setUserAccount(userAccount);
            userDTO.setUserId(user.getId());
            userDTO.setRole(user.getRole());
            log.info("User authenticated: {}", userDTO);

            // 将 userDTO 存储在 ThreadLocal 中
            UserContext.setUserDTO(userDTO);
        } else {
            log.warn("Authorization token is missing or invalid");
            throw new RuntimeException("Authorization token is missing or invalid");
        }

        try {
            // 继续执行目标方法
            return joinPoint.proceed();
        } finally {
            // 清理 ThreadLocal
            UserContext.clear();
        }
    }
}