package com.bofa.commons.apt4j.annotate.protocol;

import com.bofa.commons.apt4j.annotate.cache.CacheVar;

import java.lang.annotation.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2020/1/7
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Protocol {

    String implName();

    CacheVar[] variables() default {};
}
