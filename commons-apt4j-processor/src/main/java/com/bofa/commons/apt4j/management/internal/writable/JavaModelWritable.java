package com.bofa.commons.apt4j.management.internal.writable;

import com.bofa.commons.apt4j.management.internal.constant.JavaModelConstant;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author bofa1ex
 * @version 1.0
 * @package com.bofa.commons.apt4j.management.internal.writable
 * @date 2020/3/3
 */

@Getter
@Setter
public class JavaModelWritable extends FreeMarkerWritable implements JavaModelConstant{

    protected Set<String> import_stats = Sets.newHashSet();

    protected Set<String> static_import_stats = Sets.newHashSet();

    public String javadoc(){
        return DEFAULT_JAVADOC_TEMPLATE;
    }
}
