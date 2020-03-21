package com.bofa.commons.apt4j.management.protocol.model.decode;

import com.bofa.commons.apt4j.management.internal.model.MethodHeadModel;
import com.bofa.commons.apt4j.management.internal.model.TypeModel;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import com.bofa.commons.apt4j.management.protocol.model.common.InitResolveException;
import com.bofa.commons.apt4j.management.protocol.model.common.InitValidation;
import lombok.*;

import java.util.List;
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

    private MethodHeadModel method_head;

    private TypeModel decode_type;
    private String decode_element_name;
    private String decode_method_name;

    private String buffer_parameter;
    private String channel_parameter;

    private List<InitValidation> init_validations;
    private InitResolveException init_resolve_exception;

    public void addInit_validation(InitValidation init_validation) {
        this.init_validations.add(init_validation);
        super.import_stats.addAll(init_validation.getImport_stats());
    }

    public void setInit_resolve_exception(InitResolveException init_resolve_exception) {
        this.init_resolve_exception = init_resolve_exception;
        super.import_stats.addAll(init_resolve_exception.getImport_stats());
    }

    public Set<String> getStatic_import_stats() {
        return null;
    }

    public Set<String> getImport_stats() {
        super.import_stats.addAll(method_head.getImport_stats());
        super.import_stats.addAll(decode_type.getImport_stats());
        return import_stats;
    }
}
