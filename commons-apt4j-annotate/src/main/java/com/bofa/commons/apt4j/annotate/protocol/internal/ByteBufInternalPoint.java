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

    /** 步长类型 */
    StepType stepType() default StepType.NORMAL;

    /** 步长方向 */
    boolean reverse() default false;

    enum StepType{
        // readerIndex/writerIndex
        NORMAL(),
        // use stepExpr
        STEP_EXPR(),
        // readableBytes/writeableBytes
        REVERSE();
    }
}
