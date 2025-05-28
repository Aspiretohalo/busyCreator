package com.busymango.busymangoBackend.core.aop;

import java.util.UUID;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.busymango.busymangoBackend.core.model.dto.interfaceAccess.InterfaceAccessDTO;
import com.busymango.busymangoBackend.core.service.InterfaceAccessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 请求响应日志 AOP
 *
 * @author caoyanghalo@qq.com
 **/
@Aspect
@Component
@Slf4j
@Order(2) // 确保 UserAspect 先执行
public class LogInterceptor {
    @Resource
    private InterfaceAccessService interfaceAccessService;

    /**
     * 执行拦截
     */
    @Around("execution(* com.busymango.busymangoBackend..*.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 生成请求唯一 id
        String requestId = UUID.randomUUID().toString();
        String url = httpServletRequest.getRequestURI();
        // 获取请求参数
        Object[] args = point.getArgs();
        String reqParam = "[" + StringUtils.join(args, ", ") + "]";
        // 输出请求日志
        log.info("request start, id: {}, path: {}, ip: {}, params: {}", requestId, url,
                httpServletRequest.getRemoteHost(), reqParam);
        // 执行原方法
        Object result = point.proceed();
        // 输出响应日志
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("request end, id: {}, cost: {}ms", requestId, totalTimeMillis);

        InterfaceAccessDTO interfaceAccessDTO = new InterfaceAccessDTO();
        interfaceAccessDTO.setInterfacePath(httpServletRequest.getRequestURI());
        interfaceAccessDTO.setRequestIp(httpServletRequest.getRemoteHost());
        interfaceAccessDTO.setRequestParams(reqParam);
        interfaceAccessDTO.setResponseTime(totalTimeMillis);

        interfaceAccessService.saveRequest(interfaceAccessDTO);
        return result;
    }
}

