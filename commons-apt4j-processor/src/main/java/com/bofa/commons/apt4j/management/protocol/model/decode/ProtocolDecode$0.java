package com.bofa.commons.apt4j.management.protocol.model.decode;

import com.bofa.commons.apt4j.management.internal.model.MethodHead;
import com.bofa.commons.apt4j.management.internal.model.Type;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import com.bofa.commons.apt4j.management.protocol.model.common.InitValidation;
import lombok.*;

import java.util.Set;

/**
 * @author bofa1ex
 * @version 1.0
 * @package com.bofa.commons.apt4j.management.protocol.model
 * @date 2020/3/3
 */
@Getter
@Setter
@Builder
public class ProtocolDecode$0 extends JavaModelWritable {

    private MethodHead method_head;

    private Type decode_type;
    private String decode_element_name;
    private String decode_method_name;

    private String buffer_parameter;
    private String channel_parameter;

    private String resolve_exception_name;
    private InitValidation validate_condition;

    public void setValidate_condition(InitValidation validate_condition) {
        this.validate_condition = validate_condition;
        // Nullable?lazy-load
        super.import_stats.addAll(validate_condition.getImport_stats());
    }

    public Set<String> getStatic_import_stats() {
        return null;
    }

    public Set<String> getImport_stats() {
        super.import_stats.addAll(method_head.getImport_stats());
        super.import_stats.addAll(decode_type.getImport_stats());
        super.import_stats.add(resolve_exception_name);
        return import_stats;
    }
}
