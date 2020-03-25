package com.bofa.commons.apt4j.annotate.protocol.internal;

import java.lang.annotation.*;

/**
 * @author bofa1ex
 * @since 2020/3/24
 */
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface ByteBufInternalPoint {

    /** 默认步长为0, 即当前readerIndex/writerIndex */
    String step() default "0";

    /** 当步长值不为常量, 需要引用model以及对应的property */
    ByteBufInternalModel stepExpr() default @ByteBufInternalModel();

    /** 区分步长值的类型 */
    boolean normal() default true;

    /* 步长方向 */
    boolean reverse() default false;
}
