package com.bofa.commons.apt4j.management.protocol.model.encode;

import com.bofa.commons.apt4j.annotate.protocol.ByteBufConvert;
import com.bofa.commons.apt4j.management.internal.model.TypeModel;
import com.bofa.commons.apt4j.management.internal.utils.TypeUtils;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import lombok.*;

import java.util.Optional;
import java.util.Set;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2020/3/6
 */
@Getter
@Setter
@Builder
public class ProtocolEncodeConvertMethod extends JavaModelWritable {

    private ByteBufConvert convert_anon;
    private TypeModel encode_type;
    private String encode_element_name;
    private String channel_parameter;
    private String confused_buffer_name;

    public String[] convert_parameters(){
        return convert_anon.parameters();
    }

    public Set<String> getStatic_import_stats() {
        return null;
    }

    public Set<String> getImport_stats() {
        super.import_stats.addAll(encode_type.getImport_stats());
        return import_stats;
    }
}
