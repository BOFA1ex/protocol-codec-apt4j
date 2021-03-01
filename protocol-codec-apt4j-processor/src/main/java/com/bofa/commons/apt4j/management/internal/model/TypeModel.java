package com.bofa.commons.apt4j.management.internal.model;

import com.bofa.commons.apt4j.management.internal.utils.ElementUtils;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import com.sun.tools.javac.code.Symbol;
import lombok.*;

import java.util.Set;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2020/3/3
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class TypeModel extends JavaModelWritable {

    private String type_simple_name;
    private String type_qualifier_name;

    /** 只是为了测试 */
    @Deprecated
    public TypeModel(String typeSimpleName) {
        this.type_simple_name = typeSimpleName;
    }

    /** Exist Element */
    public TypeModel(Symbol element){
        this(ElementUtils.getSymbolTypeSimpleName(element), ElementUtils.getSymbolTypeQualifierName(element));
    }

    /** Exist Element */
    public TypeModel(String type_simple_name, String type_qualifier_name) {
        this.type_simple_name = type_simple_name;
        this.type_qualifier_name = type_qualifier_name;
    }

    public Set<String> getStatic_import_stats() {
        return static_import_stats;
    }

    public Set<String> getImport_stats() {
        super.import_stats.add(type_qualifier_name);
        return import_stats;
    }
}
