package com.skhu.common;

import com.skhu.oauth.domain.UserLevel;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.skhu.oauth.domain.UserLevel.UNAUTH;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface UserLevelCheck {
    UserLevel level() default UNAUTH;
}