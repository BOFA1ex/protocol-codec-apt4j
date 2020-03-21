package com.bofa.commons.apt4j.management.protocol.model.common;

import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import lombok.*;

import java.util.List;
import java.util.Set;

/**
 * @author bofa1ex
 * @version 1.0
 * @package com.bofa.commons.apt4j.management.protocol.model
 * @date 2020/3/4
 */
@Getter
@Setter
@Builder
public class InitValidation extends JavaModelWritable {

    private boolean is_validate;

    private String validate_qualifier_name;
    private String validate_simple_name;
    private String validate_index;
    private String validate_length;
    private String mapper_index;
    private String mapper_length;
    private List<String> anon_params;

    private String buffer_parameter;
    private String channel_parameter;

    public Set<String> getStatic_import_stats() {
        return null;
    }

    public Set<String> getImport_stats() {
        import_stats.add(validate_qualifier_name);
        return import_stats;
    }
}
