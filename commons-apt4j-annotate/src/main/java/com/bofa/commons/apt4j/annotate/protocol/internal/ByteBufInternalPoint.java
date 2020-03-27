package com.bofa.commons.apt4j.annotate.protocol.internal;

import java.lang.annotation.*;

/**
 * @author bofa1ex
 * @since 2020/3/24
 */
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface ByteBufInternalPoint {

    /** 步长 */
    String step() default "0";

    /** 当步长值不为常量, 需要引用model以及对应的property */
    ByteBufInternalModel stepExpr() default @ByteBufInternalModel();

    /** 步长正常类型 */
    boolean normalType() default true;
    /** 步长model类型 */
    boolean stepExprType() default false;
    /** 步长反向类型 */
    boolean reverseType() default false;

    /** 步长方向 */
    boolean reverse() default false;
}
