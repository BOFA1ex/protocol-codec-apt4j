package com.bofa.commons.apt4j.annotate.protocol.internal;

import java.lang.annotation.*;

/**
 * @author bofa1ex
 * @since 2020/3/24
 */
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface ByteBufInternalModel {

    String key() default "anon";

    String prop() default "";

    Class<?> keyClazz() default Void.class;
}
