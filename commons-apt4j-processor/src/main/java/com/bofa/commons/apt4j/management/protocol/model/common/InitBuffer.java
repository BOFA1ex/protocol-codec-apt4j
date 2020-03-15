package com.bofa.commons.apt4j.management.protocol.model.common;

import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import lombok.*;

import java.util.Set;

/**
 * @author bofa1ex
 * @since 2020/3/7
 */
@Getter
@Setter
@Builder
public class InitBuffer extends JavaModelWritable {

    private String initial_capacity;
    private String max_capacity;

    public Set<String> getStatic_import_stats() {
        return null;
    }

    public Set<String> getImport_stats() {
        return null;
    }
}
