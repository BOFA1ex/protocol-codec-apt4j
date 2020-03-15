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
public class ProtocolSpelProcessExpr extends JavaModelWritable {

    private ByteBufConvertAnonModel convert_anon_model;
    private String buffer_parameter;
    private String channel_parameter;
    private String standard_index_parameter;

    public ProtocolSpelProcessExpr(ByteBufConvertAnonModel convert_anon_model, String buffer_parameter, String channel_parameter, String standard_index_parameter) {
        this.convert_anon_model = convert_anon_model;
        this.buffer_parameter = buffer_parameter;
        this.channel_parameter = channel_parameter;
        this.standard_index_parameter = standard_index_parameter;
    }

    public Set<String> getStatic_import_stats() {
        return null;
    }

    public Set<String> getImport_stats() {
        import_stats.add("com.bofa.resolve.util.ChannelSpelContextUtils");
        import_stats.add( "io.netty.channel.Channel");
        import_stats.add("io.netty.buffer.*");
        return import_stats;
    }
}
