package com.bofa.commons.apt4j.management.protocol.model.common;


import com.bofa.commons.apt4j.annotate.protocol.ByteBufConvert;
import com.bofa.commons.apt4j.annotate.protocol.internal.*;
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

    public ByteBufInternalCondition condition(){
        return convert_anon.condition();
    }

    public ByteBufInternalPoint index(){
        return convert_anon.index();
    }

    public ByteBufInternalPoint length(){
        return convert_anon.length();
    }

    public String[] convert_parameters(){
        return convert_anon.parameters();
    }

    public String index_step(){
        return index().step();
    }

    public String length_step(){
        return length().step();
    }

    public ByteBufInternalModel index_model(){
        return index().model();
    }

    public ByteBufInternalModel length_model(){
        return length().model();
    }

    public ByteBufInternalModel condition_model(){
        return condition().model();
    }

    public String condition_operator(){
        return condition().operator();
    }

    public String condition_compare_value(){
        return condition().compareValue();
    }

    public String condition_model_key(){
        return condition_model().key();
    }

    public String condition_model_prop(){
        return condition_model().prop();
    }

    public String index_model_key(){
        return index_model().key();
    }

    public String index_model_prop(){
        return index_model().prop();
    }

    public String length_model_key(){
        return length_model().key();
    }

    public String length_model_prop(){
        return length_model().prop();
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