package com.gb.timer;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.event.Level;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Slf4j
@Aspect
public class TimerAspect {

    @Pointcut("execution(* com.gb..*(..))")
    public void allMethodsInMicroservices() {
    }

    @Around("allMethodsInMicroservices() && @annotation(com.gb.timer.Timer)")
    public Object loggableAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Level level = extractLevel(joinPoint);
        log.debug("Executing loggableAspect for method: {}", joinPoint.getSignature().toShortString());

        Instant start = Instant.now();
        Object result = null;

        try {
            result = joinPoint.proceed();
        } finally {
            Instant end = Instant.now();
            long executionTime = Duration.between(start, end).toMillis();

            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = joinPoint.getSignature().getName();
            System.out.println("Executing loggableAspect for method: " + joinPoint.getSignature().toShortString());


            log.atLevel(level).log("target = {}", joinPoint.getTarget().getClass());
            log.atLevel(level).log("method = {}", joinPoint.getSignature().getName());
            log.atLevel(level).log("args = {}", Arrays.toString(joinPoint.getArgs()));
            log.info("{} - {} #{} seconds", className, methodName, (executionTime / 1000.0));

            if (result != null) {
                log.atLevel(level).log("result = {}", result);
            }
        }

        return result;
    }


    private Level extractLevel(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Timer annotation = signature.getMethod().getAnnotation(Timer.class);
        if (annotation != null) {
            return annotation.level();
        }

        return joinPoint.getTarget().getClass().getAnnotation(Timer.class).level();
    }
}
