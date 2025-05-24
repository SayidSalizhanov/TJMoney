package ru.itis.impl.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.itis.impl.annotations.MayBeNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
@ConditionalOnProperty(name = "aspect.enabled", havingValue = "true")
public class NullCheckAspect {

    @Around("execution(* ru.itis.impl.service..*.*(..))")
    public Object checkParametersForNull(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object value = args[i];

            boolean isMayBeNull = parameter.isAnnotationPresent(MayBeNull.class);

            if (!isMayBeNull && value == null) {
                String methodName = method.getName();
                String paramName = parameter.getName();

                throw new IllegalArgumentException(
                        "Параметр \"" + paramName + "\" в методе \"" + methodName + "\" не может быть null"
                );
            }
        }

        return joinPoint.proceed();
    }
}