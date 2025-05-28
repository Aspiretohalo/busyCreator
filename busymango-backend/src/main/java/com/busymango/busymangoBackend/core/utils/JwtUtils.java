package com.busymango.busymangoBackend.core.utils;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWT;

public class JwtUtils {

    private static final String signKey = "yfyfyfyfyfyfyfyfyfyfyfyfyfyfyfyfyfyfyfyfyf";//key要足够长才满足其安全条件
    private static final Long expire = 43200000L;

//    public static String generateJwt(Map<String, Object> claims) {
//        Map<String, Object> map = new HashMap<String, Object>() {
//            private static final long serialVersionUID = 1L;
//
//            {
//                put("uid", Integer.parseInt("123"));
//                put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15);
//            }
//        };
//        JWTUtil.createToken(map, "1234".getBytes());
//
//        return null;
//    }

}
