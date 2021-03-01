package com.bofa.commons.apt4j.management.protocol.model.decode;

import com.bofa.commons.apt4j.annotate.protocol.ByteBufConvert;
import com.bofa.commons.apt4j.management.internal.model.TypeModel;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import lombok.*;

import java.util.Set;

/**
 * @author bofa1ex
 * @version 1.0
 * @package com.bofa.commons.apt4j.management.protocol.model.decode
 * @date 2020/3/6
 */
@Getter
@Setter
@Builder
public class ProtocolDecodeConvertMethod extends JavaModelWritable {

    private ByteBufConvert convert_anon;
    private TypeModel decode_type;
    private String decode_type_name;
    private String channel_parameter;
    private String confused_buffer_name;

    public String[] convert_parameters(){
        return convert_anon.parameters();
    }
    public Set<String> getStatic_import_stats() {
        return null;
    }

    public Set<String> getImport_stats() {
        super.import_stats.addAll(decode_type.getImport_stats());
        return import_stats;
    }
}
