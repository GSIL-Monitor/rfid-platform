package com.casesoft.dmc.extend.api.config;


import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by pc on 2016-12-27.
 * API文档配置
 */

@EnableSwagger2
public class APIConfig {
    public static String version="1.0.1";
    public static void main(String[] args) {
     }
  @Bean
  public Docket wsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(regex("/api.*"))
                .build().apiInfo(demoApiInfo());//w Docket(DocumentationType.SWAGGER_2);
    }

    private ApiInfo demoApiInfo() {
        Contact contact = new Contact("Casesoft", "http://www.casesoft.com.cn", "");
        ApiInfo apiInfo = new ApiInfo("Casesoft API接口",//大标题
                "REST风格API",//小标题
                version,//版本
                "http://www.casesoft.com.cn",
                contact,//作者
                "主页",//链接显示文字
                ""//网站链接
        );
        return apiInfo;
    }
}
