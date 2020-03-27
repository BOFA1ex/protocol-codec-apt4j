package com.bofa.commons.apt4j.management.protocol.model.common;

import com.bofa.commons.apt4j.annotate.protocol.ByteBufValidation;
import com.bofa.commons.apt4j.annotate.protocol.internal.ByteBufInternalPoint;
import com.bofa.commons.apt4j.management.internal.utils.TypeUtils;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import com.bofa.commons.apt4j.management.protocol.model.ProtocolImpl;
import lombok.*;

import java.util.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2020/3/4
 */
@Getter
@Setter
@Builder
public class InitValidation extends JavaModelWritable {

    private boolean is_validate;
    private ByteBufValidation validation_anon;

    private String buffer_parameter;
    private String channel_parameter;

    public Set<String> getStatic_import_stats() {
        return null;
    }

    public boolean is_normal(ByteBufInternalPoint point){
        return point.type() == ByteBufInternalPoint.StepType.NORMAL;
    }

    public boolean is_model(ByteBufInternalPoint point){
        return point.type() == ByteBufInternalPoint.StepType.MODEL;
    }

    public boolean is_reverse(ByteBufInternalPoint point){
        return point.type() == ByteBufInternalPoint.StepType.REVERSE;
    }

    public InitValidation processImportAndAutoWire(ProtocolImpl protocolImpl){
        Optional.ofNullable(validation_anon).ifPresent(validationAnon -> {
            final String qualifierName = TypeUtils.resolveClassTypeMirrorException(validationAnon::validateMethod);
            final String simpleName = TypeUtils.qualifierTypeName2SimpleName(qualifierName);
            super.import_stats.add(qualifierName);
            protocolImpl.addAutoWire(simpleName);
        });
        return this;
    }

    public Set<String> getImport_stats() {
        return import_stats;
    }
}
