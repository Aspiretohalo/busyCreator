package com.busymango.busymangoBackend.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否为登录接口
 *
 * @author caoyanghalo@qq.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsLogin {
    /**
     * 登录接口不需要登录验证，因此
     *
     * @return true 表示不需要登录验证，false 表示需要验证
     */
    boolean value() default true;
}