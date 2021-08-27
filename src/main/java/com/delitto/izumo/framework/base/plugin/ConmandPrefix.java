package com.delitto.izumo.framework.base.plugin;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ConmandPrefix {
    String bind();
    String[] alias() default {};
}
