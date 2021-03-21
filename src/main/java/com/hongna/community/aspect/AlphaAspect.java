package com.hongna.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Component
//@Aspect
public class AlphaAspect {
    @Pointcut("execution(* com.hongna.community.service.*.*(..))")
    public void pointcut(){

    }

    /**
     * 在方法执行前
     */
    @Before("pointcut()")
    public void before(){
        System.out.println("before");
    }

    /**
     * 在方法执行后
     */
    @After("pointcut()")
    public void after(){
        System.out.println("after");
    }


    /**
     * 当方法拥有返回值之后
     */
    @AfterReturning("pointcut()")
    public void afterReturning(){
        System.out.println("afterReturning");
    }

    /**
     * 方法抛出异常后
     */
    @AfterThrowing("pointcut()")
    public void afterThrowing(){
        System.out.println("方法抛出异常后");
    }

    /**
     *
     */
    @Around("pointcut()")
    public Object  around(ProceedingJoinPoint joinPoint ) throws Throwable {
        System.out.println("aroundBefore");
        Object obj = joinPoint.proceed();
        System.out.println("aroundAfter");
        return obj;
    }
}
