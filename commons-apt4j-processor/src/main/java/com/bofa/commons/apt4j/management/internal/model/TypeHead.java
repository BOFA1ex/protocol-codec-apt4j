package com.bofa.commons.apt4j.management.internal.model;

import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;

import java.util.List;
import java.util.Set;
/**
 * @author bofa1ex
 * @since 2020/3/5
 */
public class TypeHead extends JavaModelWritable {

    private String modifier;
    private Type class_type;
    private Type interface_type;
    private List<Type> interface_type_parameters;
    private boolean needImplement;
    private Type abstract_type;
    private List<Type> abstract_type_parameters;
    private boolean needExtends;

    public TypeHead(String modifier, Type class_type, Type interface_type, List<Type> interface_type_parameters, boolean needImplement, Type abstract_type, List<Type> abstract_type_parameters, boolean needExtends) {
        this.modifier = modifier;
        this.class_type = class_type;
        this.interface_type = interface_type;
        this.interface_type_parameters = interface_type_parameters;
        this.needImplement = needImplement;
        this.abstract_type = abstract_type;
        this.abstract_type_parameters = abstract_type_parameters;
        this.needExtends = needExtends;
    }

    public static TypeHead buildImplTypeHead(Type class_type,  Type interface_type, List<Type> interface_type_parameters) {
        return new TypeHead("public", class_type, interface_type, interface_type_parameters, true, null, null, false);
    }

    public String getModifier() {
        return modifier;
    }

    public Type getClass_type() {
        return class_type;
    }

    public Type getInterface_type() {
        return interface_type;
    }

    public List<Type> getInterface_type_parameters() {
        return interface_type_parameters;
    }

    public boolean isNeedImplement() {
        return needImplement;
    }

    public Type getAbstract_type() {
        return abstract_type;
    }

    public List<Type> getAbstract_type_parameters() {
        return abstract_type_parameters;
    }

    public boolean isNeedExtends() {
        return needExtends;
    }

    public Set<String> getStatic_import_stats() {
        return import_stats;
    }

    public Set<String> getImport_stats() {
        return import_stats;
    }
}
