package org.spring.createa.demoproject;

import java.util.logging.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

  private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

  @Around("execution(* org.spring.createa.demoproject.controller.*.*(..))")
  public Object controllerLog(ProceedingJoinPoint joinPoint) throws Throwable {
    logger.info(joinPoint.getSignature().getName() + " invoked");
    return joinPoint.proceed();
  }
}
