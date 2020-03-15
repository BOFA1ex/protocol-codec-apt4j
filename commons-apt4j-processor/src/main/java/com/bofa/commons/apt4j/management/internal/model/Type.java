package com.bofa.commons.apt4j.management.internal.model;

import com.bofa.commons.apt4j.management.internal.utils.ElementUtils;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import com.sun.tools.javac.code.Symbol;
import lombok.Data;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Set;

/**
 * @author bofa1ex
 * @version 1.0
 * @package com.bofa.commons.apt4j.management.internal.model
 * @date 2020/3/3
 */
@Data
public class Type extends JavaModelWritable {

    private boolean isInterface;
    private boolean isCollection;

    private Elements elementUtils;
    private Types typeUtils;
    private TypeMirror typeMirror;
    private TypeElement typeElement;
    private String type_name;
    private String type_qualifier_name;

    /** 只是为了测试 */
//    @Deprecated
    public Type(String typeSimpleName) {
        this.type_name = typeSimpleName;
    }

    public Type(String type_name, String type_qualifier_name) {
        this(type_name, type_qualifier_name, false, false);
    }

    public Type(Symbol element){
        this(ElementUtils.getSymbolTypeSimpleName(element), ElementUtils.getSymbolTypeQualifierName(element));
    }

    /** Exist Type */
    public Type(Elements elementUtils, Types typeUtils, TypeElement typeElement) {
        this.elementUtils = elementUtils;
        this.typeUtils = typeUtils;
        this.typeElement = typeElement;
        this.typeMirror = typeElement.asType();
        this.isInterface = typeElement.getKind() == ElementKind.INTERFACE;
        this.isCollection = false;
        this.type_name = typeElement.getSimpleName().toString();
        this.type_qualifier_name = typeElement.getQualifiedName().toString();
        super.import_stats.add(type_qualifier_name);
    }

    /* Virtual Type */
    public Type(String type_name, String type_qualifier_name, boolean isCollection, boolean isInterface) {
        this.type_name = type_name;
        this.type_qualifier_name = type_qualifier_name;
        this.isCollection = isCollection;
        this.isInterface = isInterface;
        super.import_stats.add(type_qualifier_name);
    }

    public Set<String> getStatic_import_stats() {
        return import_stats;
    }

    public Set<String> getImport_stats() {
        return import_stats;
    }
}
