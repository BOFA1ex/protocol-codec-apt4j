package com.bofa.commons.apt4j.management.protocol.model.encode;

import com.bofa.commons.apt4j.management.internal.model.MethodHeadModel;
import com.bofa.commons.apt4j.management.internal.model.TypeModel;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import com.bofa.commons.apt4j.management.protocol.model.common.InitBuffer;
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
public class ProtocolEncode$0 extends JavaModelWritable {

    private MethodHeadModel method_head;

    private TypeModel encode_type;
    private String encode_element_name;
    private String encode_method_name;

    private String channel_parameter;
    private String encode_parameter;

    private InitBuffer init_buffer;
    private List<InitValidation> init_validations;

    public void addInit_validation(InitValidation init_validation) {
        this.init_validations.add(init_validation);
        super.import_stats.addAll(init_validation.getImport_stats());
    }

    public Set<String> getStatic_import_stats() {
        return null;
    }

    public Set<String> getImport_stats() {
        import_stats.addAll(method_head.getImport_stats());
        import_stats.addAll(encode_type.getImport_stats());
        import_stats.addAll(init_buffer.getImport_stats());
        return import_stats;
    }
}
