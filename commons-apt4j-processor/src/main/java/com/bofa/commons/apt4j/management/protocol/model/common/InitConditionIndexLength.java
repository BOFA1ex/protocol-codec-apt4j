package com.bofa.commons.apt4j.management.protocol.model.common;


import com.bofa.commons.apt4j.annotate.protocol.ByteBufConvert;
import com.bofa.commons.apt4j.annotate.protocol.internal.ByteBufInternalPoint;
import com.bofa.commons.apt4j.management.internal.utils.TypeUtils;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import com.bofa.commons.apt4j.management.protocol.model.ProtocolImpl;
import com.google.common.base.Strings;
import lombok.*;

import java.util.Optional;
import java.util.Set;

/**
 * @author bofa1ex
 * @since 2020/3/25
 */
@Getter
@Setter
@Builder
public class InitConditionIndexLength extends JavaModelWritable {

    private boolean is_decode;

    private String channel_parameter;
    private String buffer_parameter;

    private ByteBufConvert convert_anon;

    public boolean is_normal(ByteBufInternalPoint point){
        return point.type() == ByteBufInternalPoint.StepType.NORMAL;
    }

    public boolean is_model(ByteBufInternalPoint point){
        return point.type() == ByteBufInternalPoint.StepType.MODEL;
    }

    public boolean is_reverse(ByteBufInternalPoint point){
        return point.type() == ByteBufInternalPoint.StepType.REVERSE;
    }

    public InitConditionIndexLength processImportAndAutoWire(ProtocolImpl protocolImpl){
        Optional.ofNullable(convert_anon).ifPresent(convert_anon -> {
            final String qualifierName = TypeUtils.resolveClassTypeMirrorException(convert_anon::convertMethod);
            final String simpleName = TypeUtils.qualifierTypeName2SimpleName(qualifierName);
            super.import_stats.add(qualifierName);
            protocolImpl.addAutoWire(simpleName);
        });
        return this;
    }

    public Set<String> getStatic_import_stats() {
        return null;
    }

    public Set<String> getImport_stats() {
        Optional.ofNullable(convert_anon).ifPresent(convert_anon -> super.import_stats.add(TypeUtils.resolveClassTypeMirrorException(convert_anon::convertMethod)));
        return import_stats;
    }
}