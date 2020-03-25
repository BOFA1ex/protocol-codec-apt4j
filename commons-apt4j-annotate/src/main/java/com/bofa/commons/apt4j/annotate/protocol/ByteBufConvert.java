package com.bofa.commons.apt4j.annotate.protocol;

import com.bofa.commons.apt4j.annotate.protocol.internal.ByteBufInternalCondition;
import com.bofa.commons.apt4j.annotate.protocol.internal.ByteBufInternalPoint;

import java.lang.annotation.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019/12/31
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface ByteBufConvert {

    /* 缓冲区slice起始索引 */
    ByteBufInternalPoint index();

    /* 缓冲区slice长度 */
    ByteBufInternalPoint length();

    /* 转换方法 */
    Class<?> convertMethod() default Void.class;

    /* 转换所需的条件，比如某个type特定值 */
    ByteBufInternalCondition condition() default @ByteBufInternalCondition();

    /* 额外的参数 */
    String[] parameters() default {};

}
