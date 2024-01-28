package com.example.ticketingprojectrest.aspect;

import com.example.ticketingprojectrest.mapper.UserUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LoggingAspect {

    private final UserUtil userUtil;
    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    public LoggingAspect(UserUtil userUtil) {
        this.userUtil = userUtil;
    }


    @Pointcut("execution(* com.example.ticketingprojectrest.controller.ProjectController.*(..)) || execution(* com.example.ticketingprojectrest.controller.TaskController.*(..))")
    public void anyControllerOperation(){}

    @Before("anyControllerOperation()")
    public void anyBeforeControllerOperationAdvice(JoinPoint joinPoint){
        String username = userUtil.getUserName();
        logger.info("Before () -> User : {} - Method: {} - Parameters: {}",username,joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "anyControllerOperation()",returning = "results")
    public void anyAfterControllerOperationAdvice(JoinPoint joinPoint,Object results){
        String username = userUtil.getUserName();
        logger.info("AfterReturning -> User: {} - Method: {} - Results: {}",username,joinPoint.getSignature().toShortString(),results.toString());
    }

    @AfterThrowing(pointcut = "anyControllerOperation()",throwing = "exception")
    public void anyAfterThrowingControllerOperationAdvice(JoinPoint joinPoint,RuntimeException exception){
        String username = userUtil.getUserName();
        logger.info("AfterReturning -> User: {} - Method: {} - Exception: {}",username,joinPoint.getSignature().toShortString(),exception.getMessage());
    }
}
