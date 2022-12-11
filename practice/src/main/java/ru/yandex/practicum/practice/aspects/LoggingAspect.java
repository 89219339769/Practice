package ru.yandex.practicum.practice.aspects;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import ru.yandex.practicum.practice.model.Client;

import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @AfterReturning(pointcut = "addAllClients() ",returning = "val")

    public void afterOperationAspect(JoinPoint jp, Object val) {

        log.info("получилось");
    }

    @Pointcut("execution(public * ru.yandex.practicum.practice.controller.*.findAllClients*(..))")
    private void addAllClients() {
    }




//  @Around("findAllClients()")
//  public void before(JoinPoint joinPoint) {
//      log.info("получилось, только оснваная логинка не работает"+joinPoint.getSignature());
//  }
}
