package ru.itis.impl.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
@Slf4j
@ConditionalOnProperty(name = "aspect.enabled", havingValue = "true")
public class MethodCallCounterAspect {

    private final Map<String, AtomicInteger> methodCallCount = new ConcurrentHashMap<>();

    @Around("execution(* ru.itis.impl.service..*.*(..))")
    public Object countMethodCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Class<?> declaringType = joinPoint.getSignature().getDeclaringType();

        String key = declaringType.getName() + "." + methodName;

        methodCallCount.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet();

        return joinPoint.proceed();
    }

    public Map<String, Integer> getCallCountSnapshot() {
        Map<String, Integer> snapshot = new java.util.HashMap<>();
        methodCallCount.forEach((key, value) -> snapshot.put(key, value.get()));
        return snapshot;
    }
}