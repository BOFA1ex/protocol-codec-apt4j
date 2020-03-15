package com.bofa.commons.apt4j.management.protocol.model.common;

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
public class ByteBufConvertAnonModel extends JavaModelWritable {

    private String index;
    private String length;
    private String condition;
    private String convert_method;
    private String[] parameters;

    public Set<String> getStatic_import_stats() {
        return import_stats;
    }

    public Set<String> getImport_stats() {
        return import_stats;
    }
}