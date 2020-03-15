package com.bofa.commons.apt4j.management.protocol.model.common;

import com.bofa.commons.apt4j.management.internal.model.Type;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author bofa1ex
 * @since 2020/3/15
 */
@Getter
@Setter
public class InternalModelContext extends JavaModelWritable {

    private Type member_type;
    private String member_name;
    private String member_method_name;

    public InternalModelContext(Type member_type, String member_name, String member_method_name) {
        this.member_type = member_type;
        this.member_name = member_name;
        this.member_method_name = member_method_name;
    }

    public Set<String> getStatic_import_stats() {
        return static_import_stats;
    }

    public Set<String> getImport_stats() {
        return import_stats;
    }
}
