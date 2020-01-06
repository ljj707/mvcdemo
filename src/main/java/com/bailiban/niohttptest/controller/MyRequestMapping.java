package com.bailiban.niohttptest.controller;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 运行时可以访问该注解
@Target(ElementType.METHOD)   // 该注解使用在类上
public @interface MyRequestMapping {

    String value() default "";
}
