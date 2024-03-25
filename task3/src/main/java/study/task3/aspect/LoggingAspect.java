package study.task3.aspect;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Around("execution(* study.task3.service.*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Method {}.{} is called with args {}",
            joinPoint.getSignature().getDeclaringType(),
            joinPoint.getSignature().getName(), joinPoint.getArgs());
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("Method {} throws an exception: {}",
                joinPoint.getSignature().getName(), e);
            throw e;
        }
        log.info("Method {} returns {}", joinPoint.getSignature().getName(), proceed);
        return proceed;
    }
}
