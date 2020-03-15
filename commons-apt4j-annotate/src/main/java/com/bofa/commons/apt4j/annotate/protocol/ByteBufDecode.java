package com.bofa.commons.apt4j.annotate.protocol;

import java.lang.annotation.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @package com.bofa.commons.apt4j.annotate.apt
 * @description decode方法标记入口
 * @date 2020/1/16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface ByteBufDecode {

    /** 处理异常 */
    Class<?> resolveException();

    /** 默认密钥 */
    String key() default "";

    String[] parameters() default {};
}
