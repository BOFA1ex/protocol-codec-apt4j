package com.bofa.commons.apt4j.annotate.cache;

import java.lang.annotation.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2020/1/16
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface CacheMapping {
    String value();
}
