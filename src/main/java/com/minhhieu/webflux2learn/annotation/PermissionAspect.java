package com.minhhieu.webflux2learn.annotation;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Log4j2
public class PermissionAspect {

    @Before("@annotation(permission)")
    public void checkPermission(JoinPoint joinPoint, Permission permission) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();

        // Create a SpEL parser
        ExpressionParser parser = new SpelExpressionParser();

        // Create an evaluation context and add method arguments to it
        StandardEvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        // Evaluate the SpEL expression
        String subject = permission.subject();
        if (subject.startsWith("#")) {
            var subjectValue = parser.parseExpression(subject).getValue(context, String.class);
            log.info("subjectValue 0 ===== {}", subjectValue);
        } else {
            log.info("subjectValue 1 ===== {}", subject);
        }

    }

}



