package com.bofa.commons.apt4j.annotate.protocol;

import com.bofa.commons.apt4j.annotate.protocol.internal.ByteBufInternalPoint;

import java.lang.annotation.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019/12/31
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
@Repeatable(ByteBufValidationGroup.class)
@Documented
public @interface ByteBufValidation {

    /* 缓冲区校验域 */
    Validate validate();

    /* 缓冲区组装域 */
    Mapper mapper();

    /* 校验方法 */
    Class<?> validateMethod();

    /* 优先级 */
    int order() default 0;

    /* 额外的参数 */
    String[] parameters() default {};


    @interface Validate {
        ByteBufInternalPoint index();

        ByteBufInternalPoint length();
    }

    @Retention(RetentionPolicy.SOURCE)
    @Documented
    @interface Mapper {
        ByteBufInternalPoint index();

        ByteBufInternalPoint length();
    }
}
