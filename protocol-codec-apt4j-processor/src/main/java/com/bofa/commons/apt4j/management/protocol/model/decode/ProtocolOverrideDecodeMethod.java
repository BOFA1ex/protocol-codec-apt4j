package com.bofa.commons.apt4j.management.protocol.model.decode;

import com.bofa.commons.apt4j.annotate.protocol.ByteBufDecode;
import com.bofa.commons.apt4j.management.internal.model.MethodHeadModel;
import com.bofa.commons.apt4j.management.internal.model.TypeModel;
import com.bofa.commons.apt4j.management.internal.utils.TypeUtils;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import com.bofa.commons.apt4j.management.protocol.model.ProtocolImpl;
import com.bofa.commons.apt4j.management.protocol.model.common.InitConditionIndexLength;
import com.bofa.commons.apt4j.management.protocol.model.common.InitValidation;
import lombok.*;

import java.util.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @package com.bofa.commons.apt4j.management.protocol.model
 * @date 2020/3/3
 */
@Getter
@Setter
@Builder
public class ProtocolOverrideDecodeMethod extends JavaModelWritable {

    private MethodHeadModel method_head;

    private ByteBufDecode decode_anon;

    private TypeModel decode_type;
    private String decode_element_name;
    private String decode_method_name;

    private String buffer_parameter;
    private String channel_parameter;

    private List<InitValidation> init_validations;

    public ProtocolOverrideDecodeMethod processImportAndAutoWire(ProtocolImpl protocolImpl){
        Optional.ofNullable(decode_anon).ifPresent(decode_anon -> {
            final String qualifierName = TypeUtils.resolveClassTypeMirrorException(decode_anon::resolveException);
            final String simpleName = TypeUtils.qualifierTypeName2SimpleName(qualifierName);
            super.import_stats.add(qualifierName);
            protocolImpl.addAutoWire(simpleName);
        });
        return this;
    }

    public void addInit_validation(InitValidation init_validation) {
        this.init_validations.add(init_validation);
        super.import_stats.addAll(init_validation.getImport_stats());
    }

    public Set<String> getStatic_import_stats() {
        return null;
    }

    public Set<String> getImport_stats() {
        Optional.ofNullable(decode_anon).ifPresent(decode_anon -> super.import_stats.add(TypeUtils.resolveClassTypeMirrorException(decode_anon::resolveException)));
        super.import_stats.addAll(method_head.getImport_stats());
        super.import_stats.addAll(decode_type.getImport_stats());
        return import_stats;
    }
}
