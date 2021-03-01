package com.bofa.commons.apt4j.management.protocol.model.common;

import com.bofa.commons.apt4j.annotate.protocol.ByteBufValidation;
import com.bofa.commons.apt4j.annotate.protocol.internal.ByteBufInternalModel;
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

    public ByteBufInternalPoint validate_index(){
        return validation_anon.validate().index();
    }

    public ByteBufInternalModel validate_index_model(){
        return validate_index().model();
    }

    public String validate_index_step(){
        return validate_index().step();
    }

    public String validate_index_model_key(){
        return validate_index_model().key();
    }

    public String validate_index_model_prop(){
        return validate_index_model().prop();
    }


    public ByteBufInternalPoint validate_length(){
        return validation_anon.validate().length();
    }

    public ByteBufInternalModel validate_length_model(){
        return validate_length().model();
    }

    public String validate_length_step(){
        return validate_length().step();
    }

    public String validate_length_model_key(){
        return validate_length_model().key();
    }

    public String validate_length_model_prop(){
        return validate_length_model().prop();
    }

    public ByteBufInternalPoint mapper_index(){
        return validation_anon.mapper().index();
    }

    public ByteBufInternalModel mapper_index_model(){
        return mapper_index().model();
    }

    public String mapper_index_step(){
        return mapper_index().step();
    }

    public String mapper_index_model_key(){
        return mapper_index_model().key();
    }

    public String mapper_index_model_prop(){
        return mapper_index_model().prop();
    }

    public ByteBufInternalPoint mapper_length(){
        return validation_anon.mapper().length();
    }

    public ByteBufInternalModel mapper_length_model(){
        return mapper_length().model();
    }
    public String mapper_length_step(){
        return mapper_length().step();
    }

    public String mapper_length_model_key(){
        return mapper_length_model().key();
    }

    public String mapper_length_model_prop(){
        return mapper_length_model().prop();
    }

    public String[] validation_parameters(){
        return validation_anon.parameters();
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
