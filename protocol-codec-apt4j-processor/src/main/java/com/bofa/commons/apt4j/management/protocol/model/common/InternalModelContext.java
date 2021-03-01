package com.bofa.commons.apt4j.management.protocol.model.common;

import com.bofa.commons.apt4j.management.internal.model.TypeModel;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import lombok.*;

import java.util.Set;

/**
 * @author bofa1ex
 * @since 2020/3/15
 */
@Getter
@Setter
@Builder
public class InternalModelContext extends JavaModelWritable {

    private TypeModel member_type;
    private String member_name;
    private String member_method_name;

    public Set<String> getStatic_import_stats() {
        return static_import_stats;
    }

    public Set<String> getImport_stats() {
        return import_stats;
    }
}
