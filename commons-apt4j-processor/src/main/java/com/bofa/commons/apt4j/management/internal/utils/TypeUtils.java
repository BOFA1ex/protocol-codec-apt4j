package com.bofa.commons.apt4j.management.internal.utils;

import com.sun.tools.javac.code.Type;

import javax.lang.model.type.MirroredTypeException;
import java.util.function.Supplier;

/**
 * @author bofa1ex
 * @since 2020/3/12
 */
public class TypeUtils {

    public static String getTypeSimpleName(Type type) {
        return type.tsym.getSimpleName().toString();
    }

    public static String getTypeQualifierName(Type type) {
        return type.tsym.getQualifiedName().toString();
    }

    /**
     * 由于编译期间,class对象未初始实例, 这里会抛出MirroredTypeException的异常
     */
    public static String resolveClassTypeMirrorException(Supplier<Class<?>> supplier) {
        try {
            supplier.get();
        } catch (MirroredTypeException e) {
            final String typeName = e.getTypeMirror().toString();
            // 由于注解给定的class对象default值不能为null, 只能默认指定Void.class.
            // c++对void的定义是无类型参数, 所以默认指定Void.class.
            if (typeName.endsWith("Void")) {
                return null;
            }
            return typeName;
        }
        return null;
    }
}
