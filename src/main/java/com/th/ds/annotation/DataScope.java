package com.th.ds.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author xiaokaixin
 * @Date 2022/5/31 07:46
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataScope {

    String deptAlias() default "";

    String userAlias() default "";
}
