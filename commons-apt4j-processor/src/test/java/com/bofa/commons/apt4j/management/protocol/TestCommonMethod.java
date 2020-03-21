package com.bofa.commons.apt4j.management.protocol;

import com.bofa.commons.apt4j.management.internal.extension.FreeMarkerModelGenerator;
import com.bofa.commons.apt4j.management.internal.model.*;
import com.bofa.commons.apt4j.management.protocol.model.common.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author bofa1ex
 * @since 2020/3/14
 */
public class TestCommonMethod {

    FreeMarkerModelGenerator generator = new FreeMarkerModelGenerator();

    @Test
    public void testInitBuffer() {
        InitBuffer initBuffer =  InitBuffer.builder()
                .initial_capacity("2 << 11")
                .max_capacity("Integer.MAX_VALUE")
                .build();
        generator.generateModel(initBuffer);
    }


    @Test
    public void testInitChannelSpelContextMethod() {
        InitChannelSpelContextMethod initBuffer = InitChannelSpelContextMethod.builder()
                .method_head(MethodHeadModel.builder()
                        .modifier(MethodHeadModel.PRIVATE_FINAL)
                        .return_type(new TypeModel("void"))
                        .method_name("_init")
                        .method_parameters(Arrays.asList(
                                new MethodParameterModel(new TypeModel("String"), "objName"),
                                new MethodParameterModel(new TypeModel("Object"), "obj"),
                                new MethodParameterModel(new TypeModel("ByteBuf"), "buffer"),
                                new MethodParameterModel(new TypeModel("Channel"), "channel")
                        ))
                        .is_override(false)
                        .build()
                )
                .spel_vars(Collections.singletonList(
                        new InitChannelSpelContextMethod.SpelVarModel("_buffer", "buffer")
                ))
                .build();
        generator.generateModel(initBuffer);
    }

    @Test
    public void testInitMarkAndReadSliceMethod() {
        InitMarkAndReadSliceMethod initBuffer = InitMarkAndReadSliceMethod.builder()
                .method_head(MethodHeadModel.builder()
                        .modifier(MethodHeadModel.PRIVATE_FINAL)
                        .return_type(new TypeModel("ByteBuf"))
                        .method_name("markAndReadSlice")
                        .method_parameters(Arrays.asList(
                                new MethodParameterModel(new TypeModel("ByteBuf"), "buffer"),
                                new MethodParameterModel(new TypeModel("int"), "index"),
                                new MethodParameterModel(new TypeModel("int"), "length")
                        ))
                        .is_override(false)
                        .build())
                .log_message(InitMarkAndReadSliceMethod.SYSTEM_LOG_MESSAGE)
                .buffer_parameter("buffer")
                .reader_index_parameter("index")
                .length_parameter("length")
                .build();
        generator.generateModel(initBuffer);
    }

    @Test
    public void testInitValidation() {
        FreeMarkerModelGenerator generator = new FreeMarkerModelGenerator();
        final InitValidation initValidation = InitValidation.builder()
                .validate_qualifier_name("TestValidation")
                .validate_index("0")
                .validate_length("-1")
                .mapper_index("8")
                .mapper_length("9")
                // ByteBufValidation#parameters
                .anon_params(null)
                .buffer_parameter("buffer")
                .channel_parameter("channel")
                // validate/mapper flag
                .is_validate(true)
                .build();
        generator.generateModel(initValidation);
    }
}
