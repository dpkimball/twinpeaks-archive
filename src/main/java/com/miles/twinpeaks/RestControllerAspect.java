/*
 * Derived from spring-boot-rest-example by Siamak Khoubyari.
 * Copyright (c) 2014-2018 Siamak Khoubyari <khoubyari@gmail.com>
 * Licensed under the MIT License; see LICENSE-UPSTREAM-MIT.txt.
 * TwinPeaks adapts package names, domain concepts, and application behavior.
 */
package com.miles.twinpeaks;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RestControllerAspect {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Before("execution(public * com.miles.twinpeaks.api.rest.*Controller.*(..))")
    public void logBeforeRestCall(JoinPoint pjp) throws Throwable {
        log.info(":::::AOP Before REST call:::::" + pjp);
    }
}
