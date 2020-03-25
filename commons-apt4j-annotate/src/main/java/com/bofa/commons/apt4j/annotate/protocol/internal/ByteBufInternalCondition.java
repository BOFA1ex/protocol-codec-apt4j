package com.bofa.commons.apt4j.annotate.protocol.internal;

import java.lang.annotation.*;

/**
 * @author bofa1ex
 * @since 2020/3/24
 */
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface ByteBufInternalCondition {

    /** condition 引用的model和对应的property */
    ByteBufInternalModel model() default @ByteBufInternalModel();

    /** 默认condition为true */
    boolean skip() default true;

    /** 操作运算符 */
    String operator() default "==";

    /** condition需要比较的值 */
    String compareValue() default "";

}
