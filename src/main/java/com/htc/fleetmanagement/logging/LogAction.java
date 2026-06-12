package com.htc.fleetmanagement.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAction {
	
	public static final Logger log = LoggerFactory.getLogger(LogAction.class);
	
	@Before("execution(* com.htc.fleetmanagement.controller.*.*(..))")
	public void logBeforeControllerMethods(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		log.info("Entering method: " + methodName);
	}
	
	
	@After("execution(* com.htc.fleetmanagement.controller.*.*(..))")
	public void logAfterControllerMethods(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		log.info("Exiting method: " + methodName);
	}
	
	@AfterThrowing(pointcut = "execution(* com.htc.fleetmanagement.controller.*.*(..))", throwing = "ex")
	public void logExceptions(JoinPoint joinPoint, Throwable ex) {
		String methodName = joinPoint.getSignature().getName();
		log.error("Exception in method: " + methodName + " - " + ex.getMessage());
	}
	

	@AfterReturning(pointcut = "execution(* com.htc.fleetmanagement.controller.*.*(..))", returning = "result")
	public void logReturnValues(JoinPoint joinPoint, Object result) {
		String methodName = joinPoint.getSignature().getName();
		log.info("Method " + methodName + " returned: " + result);
	}
	
	
	@Around("execution(* com.htc.fleetmanagement.controller.*.*(..))")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		String methodName = joinPoint.getSignature().getName();
		long startTime = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		long endTime = System.currentTimeMillis();
		log.info("Execution time of " + methodName + ": " + (endTime - startTime) + " ms");
		return result;
	}

}
