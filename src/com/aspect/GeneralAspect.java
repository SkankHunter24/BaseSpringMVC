package com.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import base.RollBackException;
/**
 * 基本切面
 */
@Component
@Aspect
public class GeneralAspect {
	public Logger logger = null;
	
	// 定义三层切点
	@Pointcut("execution(* com.controller..*(..))") 
	public void actionPointCut() {
	}

	@Pointcut("execution(* com.model..*(..))")
	public void servicePointCut() {
	}

	@Pointcut("execution(* com.dao..*(..))")
	public void daoPointCut() {
	}
	

	// action执行过程////////////////////////////////////////////
	@Before("actionPointCut()")
	public void actionBeforeExecution(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getName();
	}

	@After("actionPointCut()")
	public void actionAfterExecution(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getName();
	}
 
	@AfterReturning(pointcut = "actionPointCut()", returning = "returnVal")
	public void actionAfterReturning(JoinPoint joinPoint, Object returnVal) {
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getName();
	}
	
	
	@AfterThrowing(pointcut = "actionPointCut()",throwing="exception")
	public void actionAfterThrowing(JoinPoint joinPoint,Throwable exception){  
		//对action层未catch异常进行处理 
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getName();
		Logger logger = LogManager.getLogger(className);
		logger.error(className+" " +methodName+"() method has untreated exception:"+exception.getMessage());
	}
	
	
	// action结束///////////////////////////////////////////////////////////

	
	// service执行过程////////////////////////////////////////////
	@Before("servicePointCut()")
	public void serviceBeforeExecution(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getName();
	}

	@After("servicePointCut()")
	public void serviceAfterExecution(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getName();
	}

	@AfterReturning(pointcut = "servicePointCut()", returning = "returnVal")
	public void serviceAfterReturning(JoinPoint joinPoint, Object returnVal) {
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getName();
	}
	
	
	@AfterThrowing(pointcut = "servicePointCut()",throwing="exception")
	public void serviceAfterThrowing(JoinPoint joinPoint,Throwable exception){  
		//对service层未catch异常进行处理 
		//如果该异常是运行时异常 则抛出RollBackException通知spring进行回滚
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getName();
		Logger logger = LogManager.getLogger(className);
		logger.error(className+" " +methodName+"() method has untreated exception:"+exception.getMessage());
		boolean b = exception instanceof RuntimeException;
		if(b) throw new RollBackException(exception.getMessage());
		
	}
	// service结束///////////////////////////////////////////////////////////

	
	// dao执行过程////////////////////////////////////////////
	@Before("daoPointCut()")
	public void daoBeforeExecution(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getName();
	}

	@After("daoPointCut()")
	public void daoAfterExecution(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getName();
	}

	@AfterReturning(pointcut = "daoPointCut()", returning = "returnVal")
	public void daoAfterReturning(JoinPoint joinPoint, Object returnVal) {
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getName();
	}
	
	
	@AfterThrowing(pointcut = "daoPointCut()",throwing="exception")
	public void daoAfterThrowing(JoinPoint joinPoint,Throwable exception){  
		//对dao层未catch异常进行处理 
		//如果该异常是运行时异常 则抛出RollBackException通知spring进行回滚
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getName();
		Logger logger = LogManager.getLogger(className);
		logger.error(className+" " +methodName+"() method has untreated exception:"+exception.getMessage());
		boolean b = exception instanceof RuntimeException;
		if(b) throw new RollBackException(exception.getMessage());
	}
	// dao结束///////////////////////////////////////////////////////////

}
