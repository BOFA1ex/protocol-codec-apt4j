package com.bofa.commons.apt4j.management.internal.model;

import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import lombok.*;

import java.util.Set;
/**
 * @author bofa1ex
 * @since 2020/3/5
 */
@Getter
@Setter
@Builder
public class TypeHeadModel extends JavaModelWritable {

    public static final String DEFAULT_MODIFIER = "public final";

    private String modifier;
    private TypeModel impl_type;
    private TypeModel interface_type;
    private boolean needImplement;
    private TypeModel super_type;
    private boolean needExtends;

    public Set<String> getStatic_import_stats() {
        return import_stats;
    }

    public Set<String> getImport_stats() {
        return import_stats;
    }
}
