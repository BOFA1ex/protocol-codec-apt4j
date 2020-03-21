package com.bofa.commons.apt4j.annotate.spel;

import java.lang.annotation.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2020/1/16
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface SpelMapping {
    String value();
}
