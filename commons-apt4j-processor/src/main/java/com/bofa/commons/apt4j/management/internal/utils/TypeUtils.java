package com.bofa.commons.apt4j.management.internal.utils;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;

import javax.lang.model.element.Element;
import javax.lang.model.type.*;
import javax.lang.model.util.Types;
import java.util.Optional;
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

    public static Symbol.TypeSymbol resolvePrimitiveType(Types typeUtils, TypeMirror typeMirror){
        // typeUtils.asElement 返回的类型只有Symbol.TypeSymbol, 因此这里不担心会出CastException
        // 考虑到typeUtils.asElement(primitiveType)会返回null, 这里需要对typeMirror进行包装, 返回包装类型
        return (Symbol.TypeSymbol) Optional.ofNullable(typeUtils.asElement(typeMirror))
                .orElseGet(() -> typeUtils.boxedClass((PrimitiveType) typeMirror));
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
