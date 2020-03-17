package com.bofa.commons.apt4j.management.protocol.model.encode;

import com.bofa.commons.apt4j.management.internal.model.TypeModel;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import com.bofa.commons.apt4j.management.protocol.model.common.ByteBufConvertAnonModel;
import com.google.common.collect.Sets;
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
public class ProtocolEncodePart$0 extends JavaModelWritable {

    private ByteBufConvertAnonModel convertAnonModel;
    private TypeModel encode_type;
    private String encode_type_name;
    private String channel_parameter;
    private String confused_buffer_name;

    public Set<String> getStatic_import_stats() {
        return null;
    }

    public Set<String> getImport_stats() {
        super.import_stats.addAll(
                Sets.newHashSet(
                        "com.bofa.resolve.util.ChannelSpelContextUtils",
                        "io.netty.channel.Channel",
                        "io.netty.buffer.*"
                )
        );
        return import_stats;
    }
}
