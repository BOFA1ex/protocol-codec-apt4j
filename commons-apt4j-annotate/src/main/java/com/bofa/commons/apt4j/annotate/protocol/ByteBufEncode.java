package com.bofa.commons.apt4j.annotate.protocol;

import java.lang.annotation.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @package com.bofa.commons.apt4j.annotate.apt
 * @date 2020/1/16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface ByteBufEncode {
    /* 缓冲区初始容量默认256 */
    int initialCapacity() default 2 << 8;
    /* 缓冲区最大容量默认Integer.MAX_VALUE */
    int maxCapacity() default Integer.MAX_VALUE;
}
