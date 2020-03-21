package com.bofa.commons.apt4j.management.protocol.model.common;

import com.bofa.commons.apt4j.management.internal.model.TypeModel;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author bofa1ex
 * @version 1.0
 * @package com.bofa.commons.apt4j.management.protocol.model.common
 * @date 2020/3/18
 */
@Getter
@Setter
@Builder
public class InitResolveException extends JavaModelWritable {

    private String channel_parameter;

    private TypeModel decode_type;
    private String decode_element_name;

    private String resolve_exception_qualifier_name;
    private String resolve_exception_simple_name;

    public Set<String> getStatic_import_stats() {
        return null;
    }

    public Set<String> getImport_stats() {
        import_stats.add(resolve_exception_qualifier_name);
        return import_stats;
    }
}
