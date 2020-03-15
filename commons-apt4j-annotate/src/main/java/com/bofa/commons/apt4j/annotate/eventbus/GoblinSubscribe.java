package com.bofa.commons.apt4j.annotate.eventbus;

import java.lang.annotation.*;

/**
 * @author bofa1ex
 * @since 2020/2/21
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface GoblinSubscribe {
    /** 标识符 */
    String identifier();

    /** 主题 */
    String[] topic() default {"anon"};

    /** 订阅方法执行优先级 */
    int order() default 0;

    /** 处理异常 */
    Class<?> errorHandle() default Void.class;

    /** 初始化阶段是否需要注册到注册表 */
    boolean autoRegister() default true;
}
