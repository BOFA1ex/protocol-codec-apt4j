package com.bofa.commons.apt4j.management.protocol.model.common;

import com.bofa.commons.apt4j.management.internal.model.MethodHead;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
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
public class InitMarkAndReadSliceMethod extends JavaModelWritable {

    public static final String DEFAULT_LOG_MESSAGE = "\"数据包不完整 %s\"";

    private MethodHead method_head;
    private String buffer_parameter;
    private String reader_index_parameter;
    private String length_parameter;
    private String log_message;


    public Set<String> getStatic_import_stats() {
        return null;
    }

    public Set<String> getImport_stats() {
        import_stats.addAll(method_head.getImport_stats());
        return import_stats;
    }
}
