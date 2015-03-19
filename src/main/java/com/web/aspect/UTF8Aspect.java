package com.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by Semyon on 19.03.2015.
 */
@Aspect
public class UTF8Aspect {

    @Pointcut("execution(@com.web.aspect.UTF8 public * *(..))")
    public void publicMethod() {}

    @Around("publicMethod()")
    public Object interceptAndLog(ProceedingJoinPoint invocation) throws Throwable {
        Object[] paramValues = invocation.getArgs();
        MethodSignature ms = (MethodSignature) invocation.getSignature();
        Method m = ms.getMethod();
        Annotation[][] allParameterAnnotations = m.getParameterAnnotations();
        for (int i = 0; i < allParameterAnnotations.length; i++) {
            Annotation[] parameterAnnotations = allParameterAnnotations[i];
            for (int j = 0; j < parameterAnnotations.length; j++) {
                Annotation annotation = parameterAnnotations[j];
                if (annotation.annotationType().equals(RequestParam.class)) {
                    Object param = paramValues[i];
                    if (param instanceof String) {
                        String p = (String) param;
                        try {
                            byte ptext[] = new byte[0];
                            ptext = p.getBytes("ISO-8859-1");
                            String value = new String(ptext, "UTF-8");
                            paramValues[i] = value;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return invocation.proceed(paramValues);
    }

}
