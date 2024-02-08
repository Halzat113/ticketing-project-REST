package com.example.ticketingprojectrest.aspect;

import static com.example.ticketingprojectrest.util.UserUtil.*;

import com.example.ticketingprojectrest.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class LoggingAspect {

    private final UserUtil userUtil;

    public LoggingAspect(UserUtil userUtil) {
        this.userUtil = userUtil;
    }


 //   Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.ticketingprojectrest.controller.ProjectController.*(..)) || execution(* com.example.ticketingprojectrest.controller.TaskController.*(..))")
    private void anyControllerOperation(){}

    @Before("anyControllerOperation()")
    public void anyBeforeControllerAdvice(JoinPoint joinPoint){
        log.info("================Execution will start================");
        log.info("Before () -> User : {} - Method: {} - Parameters: {}",getUserName(),joinPoint.getSignature().toShortString(),joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "anyControllerOperation()",returning = "results")
    public void anyAfterControllerOperationAdvice(JoinPoint joinPoint,Object results){
        log.info("AfterReturning -> User: {} - Method: {} - Results: {}",getUserName(),joinPoint.getSignature().toShortString(),results.toString());
    }

    @AfterThrowing(pointcut = "anyControllerOperation()",throwing = "exception")
    public void anyAfterControllerOperationAdvice(JoinPoint joinPoint,RuntimeException exception) {
        log.info("AfterThrowing -> User: {} - Method: {} - Results: {}", getUserName(), joinPoint.getSignature().toShortString(), exception.getMessage());
    }

    }
