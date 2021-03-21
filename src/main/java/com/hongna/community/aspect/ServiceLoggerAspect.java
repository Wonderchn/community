package com.hongna.community.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;


@Aspect
@Component
public class ServiceLoggerAspect {
    private static final Logger logger = LoggerFactory.getLogger(ServiceLoggerAspect.class);

    @Pointcut("execution(* com.hongna.community.service.*.*(..))")
    public void pointcut(){

    }


    @Before("pointcut()")
    public void before(JoinPoint joinPoint){
        //用户{1.2.3.4}，在{xxxx},访问了{com.hsw.community.service.xxx}
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String ip = request.getRemoteHost();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String TypeName = joinPoint.getSignature().getDeclaringTypeName(); //类名
        String methodName = joinPoint.getSignature().getName();//方法名
        String target = TypeName+"."+methodName;
        logger.info(String.format("用户[%s],在[%s],访问了[%s]",ip,now,target));
    }
}
