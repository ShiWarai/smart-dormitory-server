package ru.mirea.smart_dormitory;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 *ProceedingJoinPoint — это расширение JoinPoint , предоставляющее дополнительный метод continue () .
 * При вызове выполнение кода переходит к следующему совету или к целевому методу.
 * Это дает нам возможность контролировать поток кода и решать, следует ли продолжать дальнейшие вызовы.
 * */
@Slf4j
@Component
@Aspect
public class AOPAspect {
    @Around("allServiceMethods()")
    public Object logParamsAndTime(ProceedingJoinPoint joinPoint) {
        long start = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        long end = System.currentTimeMillis();
        log.info(joinPoint.getSignature().toShortString());
        log.info("Method: " + joinPoint.getSignature().getName() +" from "+joinPoint.getTarget().getClass()+ joinPoint.getArgs()+
                " persist for "+(end-start)+" ms"+" with parametrs{}: "+ Arrays.toString(joinPoint.getArgs()));
        return  result;
    }
    @Pointcut("within(ru.mirea.smart_dormitory.services.*)")
    public void allServiceMethods() {}

}
