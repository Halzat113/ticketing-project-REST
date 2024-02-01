package com.example.ticketingprojectrest.aspect;

import static com.example.ticketingprojectrest.util.UserUtil.*;

import com.example.ticketingprojectrest.util.UserUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LoggingAspect {

    private final UserUtil userUtil;

    public LoggingAspect(UserUtil userUtil) {
        this.userUtil = userUtil;
    }

    Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.ticketingprojectrest.controller.ProjectController.*(..)) || execution(* com.example.ticketingprojectrest.controller.TaskController.*(..))")
    private void anyControllerOperation(){}

    @Before("anyControllerOperation()")
    public void anyBeforeControllerAdvice(JoinPoint joinPoint){
        logger.info("Before () -> User : {} - Method: {} - Parameters: {}",getUserName(),joinPoint.getSignature().toShortString(),joinPoint.getArgs());
    }

    @AfterReturning(value = "anyControllerOperation()",returning = "results")
    public void anyAfterControllerOperationAdvice(JoinPoint joinPoint,Object results){
        logger.info("AfterReturning -> User: {} - Method: {} - Results: {}",getUserName(),joinPoint.getSignature().toShortString(),results.toString());
    }

    @AfterThrowing(value = "anyControllerOperation()",throwing = "exception")
    public void anyAfterControllerOperationAdvice(JoinPoint joinPoint,RuntimeException exception) {
        logger.info("AfterThrowing -> User: {} - Method: {} - Results: {}", getUserName(), joinPoint.getSignature().toShortString(), exception.getMessage());
    }

    }
