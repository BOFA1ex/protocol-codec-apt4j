package com.bofa.commons.apt4j.management.internal.model;

import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import lombok.*;

import java.util.Set;

/**
 * @author bofa1ex
 * @version 1.0
 * @package com.bofa.commons.apt4j.management.internal.model
 * @date 2020/3/3
 */
@Getter
@Setter
@Builder
public class MethodParameterModel extends JavaModelWritable {

    private TypeModel param_type;
    private String param_name;

    public MethodParameterModel(TypeModel param_type, String param_name) {
        this.param_type = param_type;
        this.param_name = param_name;
    }

    public Set<String> getStatic_import_stats() {
        return import_stats;
    }

    public Set<String> getImport_stats() {
        import_stats.addAll(param_type.getImport_stats());
        return import_stats;
    }
}
