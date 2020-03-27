package com.bofa.commons.apt4j.management.protocol.model.common;

import com.bofa.commons.apt4j.annotate.cache.CacheVar;
import com.bofa.commons.apt4j.management.internal.model.MethodHeadModel;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import lombok.*;

import java.util.List;
import java.util.Set;

/**
 * @author bofa1ex
 * @since 2020/3/5
 */
@Getter
@Setter
@Builder
public class InitChannelCodecContextMethod extends JavaModelWritable {

    private MethodHeadModel method_head;
    private CacheVar[] cache_vars;

    public Set<String> getStatic_import_stats() {
        return null;
    }

    public Set<String> getImport_stats() {
        import_stats.addAll(method_head.getImport_stats());
        return import_stats;
    }
}
