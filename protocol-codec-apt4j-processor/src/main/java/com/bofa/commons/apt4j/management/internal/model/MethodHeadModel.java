package com.bofa.commons.apt4j.management.internal.model;

import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import lombok.*;

import java.util.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @since  2020/3/4
 */

@Getter
@Setter
@Builder
public class MethodHeadModel extends JavaModelWritable {

    public static final String PRIVATE = "private";

    private String modifier;
    private TypeModel return_type;
    private String method_name;
    private List<MethodParameterModel> method_parameters;

    private boolean is_override;

    public Set<String> getStatic_import_stats() {
        return import_stats;
    }

    public Set<String> getImport_stats() {
        for (final MethodParameterModel method_parameter : method_parameters) {
            import_stats.addAll(method_parameter.getImport_stats());
        }
        return import_stats;
    }
}
