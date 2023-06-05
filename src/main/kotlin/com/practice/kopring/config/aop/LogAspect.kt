package com.practice.kopring.config.aop

import org.apache.logging.log4j.kotlin.Logging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.*
import org.springframework.stereotype.Component
import java.util.*

@Aspect
@Component
class LogAspect {
    companion object : Logging

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    fun restController() = Unit

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    fun service() = Unit

    @Pointcut("within(@org.springframework.web.bind.annotation.RestControllerAdvice *)")
    fun globalExceptionHandler() = Unit

    @Pointcut("execution(* *.*(..))")
    fun methodPointcut() = Unit

    @Before("restController() && methodPointcut() && service()")
    fun beforeControllerMethod(joinPoint: JoinPoint) {
        logger.info("Before method execution: ${joinPoint.signature}")
    }

    @AfterReturning(pointcut = "restController() && globalExceptionHandler()", returning = "result")
    fun afterControllerMethod(joinPoint: JoinPoint, result: Any?) {
        logger.info("Method returned: ${joinPoint.signature} returned ${result.toString()}")
    }
}
