//package com.busymango.busymangoBackend.config;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Contact;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.info.License;
//import org.springdoc.core.models.GroupedOpenApi;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * <p>
// * Knife4j整合Swagger3 Api接口文档
// * </p>
// *
// * @author 奶茶要加菲
// * @since 2024-07-21
// */
//@Configuration
//public class Knife4jConfig {
//
//    @Bean
//    public GroupedOpenApi adminApi() { // 创建了一个api接口的分组
//        return GroupedOpenApi.builder()
//                .group("admin-api") // 分组名称
//                .pathsToMatch("/**") // 接口请求路径规则
//                .build();
//    }
//
//    @Bean
//    public OpenAPI openAPI(){
//        return new OpenAPI()
//                .info(new Info() // 基本信息配置
//                        .title("Knife4j整合Swagger3 Api接口文档") // 标题
//                        .description("Knife4j后端接口服务...") // 描述Api接口文档的基本信息
//                        .version("v1.0.0") // 版本
//                        // 设置OpenAPI文档的联系信息，包括联系人姓名为"patrick"，邮箱为"patrick@gmail.com"。
//                        .contact(new Contact().name("奶茶要加菲").email("957886410@qq.com"))
//                        // 设置OpenAPI文档的许可证信息，包括许可证名称为"Apache 2.0"，许可证URL为"http://springdoc.org"。
//                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
//                );
//    }
//}
//
//
