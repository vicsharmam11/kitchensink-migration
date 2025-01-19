package com.demo.kitchensink.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Define pointcut to target all methods in specific packages
    @Pointcut("execution(* com.demo.kitchensink.service..*(..)) || execution(* com.demo.kitchensink.controller..*(..))")
    public void applicationMethods() {}

    // Before advice to log method details
    @Before("applicationMethods()")
    public void logMethodEntry(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();
        logger.info("Entering method: {} with arguments: {}", methodName, args);
    }

    // After advice to log method exit
    @AfterReturning(value = "applicationMethods()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Exiting method: {} with result: {}", methodName, result);
    }

    // Around advice to measure execution time
    @Around("applicationMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long timeTaken = System.currentTimeMillis() - startTime;
        logger.info("Method {} executed in {} ms", joinPoint.getSignature(), timeTaken);
        return result;
    }
}

