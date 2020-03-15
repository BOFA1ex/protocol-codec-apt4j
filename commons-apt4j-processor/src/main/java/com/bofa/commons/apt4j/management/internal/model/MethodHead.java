package com.bofa.commons.apt4j.management.internal.model;

import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import lombok.*;

import java.util.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @package com.bofa.commons.apt4j.management.internal.model
 * @date 2020/3/4
 */

@Getter
@Setter
@Builder
public class MethodHead extends JavaModelWritable {

    public static final String PRIVATE_FINAL = "private final";

    private String modifier;
    private Type return_type;
    private String method_name;
    private List<MethodParameter> method_parameters;

    private boolean is_override;

    public Set<String> getStatic_import_stats() {
        return import_stats;
    }

    public Set<String> getImport_stats() {
        for (final MethodParameter method_parameter : method_parameters) {
            import_stats.addAll(method_parameter.getImport_stats());
        }
        return import_stats;
    }
}
