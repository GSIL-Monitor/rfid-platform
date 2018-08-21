package com.casesoft.dmc.extend.api.valid.validation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * Created by pc on 2016-12-27.
 * 接口访问限制标记
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
//最高优先级
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface ApiAuth {
    String name() default "";
}
