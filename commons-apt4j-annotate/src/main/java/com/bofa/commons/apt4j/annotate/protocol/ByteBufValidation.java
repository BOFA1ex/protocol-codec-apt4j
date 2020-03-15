package com.bofa.commons.apt4j.annotate.protocol;

import java.lang.annotation.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @package com.bofa.commons.apt4j.annotate.apt.annotate
 * @date 2019/12/31
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface ByteBufValidation {

    /* 缓冲区校验域 */
    Validate validate();

    /* 缓冲区组装域 */
    Mapper mapper() default @Mapper(index = "", length = "");

    /* 校验方法 */
    Class<?> validateMethod();

    /* 优先级 */
    int order() default 0;

    /* 额外的参数 */
    String[] parameters() default {};

    @Retention(RetentionPolicy.SOURCE)
    @Documented
    @interface Validate {
        String index();

        String length();
    }

    @Retention(RetentionPolicy.SOURCE)
    @Documented
    @interface Mapper {
        String index();

        String length();
    }
}
