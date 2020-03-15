package com.bofa.commons.apt4j.management.internal.utils;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.stream.Collectors;

public class ElementUtils {
    /**
     * @param packageElement
     *
     * @return
     */
    public static String getPackageName(PackageElement packageElement) {
        return packageElement.getQualifiedName().toString();
    }

    public static List<Element> getGenericElements(Types types, Symbol symbol) {
        return symbol.asType().getTypeArguments().stream()
                .map(types::asElement)
                .collect(Collectors.toList());
    }

    public static Element getGenericElement(Types types, Symbol symbol) {
        return getGenericElements(types, symbol).get(0);
    }

    /**
     * 处理泛型, 防止类型擦除
     */
    public static String getSymbolTypeSimpleName(Symbol element) {
        final Type type = element.asType();
        final String outerName = type.tsym.getSimpleName().toString();
        if (type.allparams() == null || type.allparams().size() == 0) {
            return outerName;
        }
        final String innerName = type.allparams().stream()
                .map(t -> getSymbolTypeSimpleName(t.asElement()))
                .collect(Collectors.joining(",", "<", ">"));
        return outerName + innerName;
    }

    public static String getSymbolTypeQualifierName(Symbol element) {
        return element.asType().tsym.getQualifiedName().toString();
    }

}