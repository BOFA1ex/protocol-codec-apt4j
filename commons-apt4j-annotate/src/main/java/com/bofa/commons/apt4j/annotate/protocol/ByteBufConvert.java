package com.bofa.commons.apt4j.annotate.protocol;

import java.lang.annotation.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @since  2019/12/31
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface ByteBufConvert {

    /* 缓冲区slice起始索引 */
    String index();

    /* 缓冲区slice长度 */
    String length();

    /* 转换方法 */
    Class<?> convertMethod() default Void.class;

    /* 转换所需的条件，比如某个type特定值 */
    String condition() default "true";

    /* 额外的参数 */
    String[] parameters() default {};

}
