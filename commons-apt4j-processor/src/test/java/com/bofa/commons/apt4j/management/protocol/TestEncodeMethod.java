package com.bofa.commons.apt4j.management.protocol;

import com.bofa.commons.apt4j.management.internal.extension.FreeMarkerModelGenerator;
import com.bofa.commons.apt4j.management.internal.model.*;
import com.bofa.commons.apt4j.management.protocol.constant.ProtocolGenerateConstant;
import com.bofa.commons.apt4j.management.protocol.model.common.*;
import com.bofa.commons.apt4j.management.protocol.model.encode.*;
import org.junit.Test;

import java.util.*;

/**
 * @author bofa1ex
 * @since 2020/3/14
 */
public class TestEncodeMethod {

    FreeMarkerModelGenerator generator = new FreeMarkerModelGenerator();

    @Test
    public void testEncodeOverrideMethod(){
        MethodParameterModel m1 = new MethodParameterModel(new TypeModel("FlvFile"), "flvFile");
        MethodParameterModel m2 = new MethodParameterModel(new TypeModel("Channel"), "channel");
        MethodHeadModel methodHead = MethodHeadModel.builder()
                .modifier(MethodHeadModel.PRIVATE_FINAL)
                .return_type(new TypeModel("ByteBuf"))
                .method_name("encode")
                .method_parameters(Arrays.asList(m1, m2))
                .is_override(true)
                .build();
        final InitValidation initValidation = InitValidation.builder()
                .validate_name("CheckSumValidateMethod")
                .validate_index("0")
                .validate_length("9")
                .mapper_index("9")
                .mapper_length("1")
                // ByteBufValidation#parameters
                .anon_params(Arrays.asList("1", "2"))
                .buffer_parameter("buffer")
                .channel_parameter("channel")
                // validate/mapper flag
                .is_validate(false)
                .build();
        final ProtocolEncode$0 protocolEncode = ProtocolEncode$0.builder()
                .method_head(methodHead)
                .validate_condition(initValidation)
                .encode_type(new TypeModel("FlvFile"))
                .encode_element_name("flvFile")
                .encode_method_name("_d123FlvFile")
                .encode_parameter("flvFile")
                .channel_parameter("channel")
                .init_buffer(InitBuffer.builder()
                        .initial_capacity("2 << 11")
                        .max_capacity("Integer.MAX_VALUE")
                        .build())
                .build();
        generator.generateModel(protocolEncode);
    }

    @Test
    public void testEncodeRootObjectElement(){
        final ByteBufConvertAnonModel convertAnonModel = ByteBufConvertAnonModel.builder().build();
        final ProtocolEncode$1 _encode = ProtocolEncode$1.builder()
                .method_head(MethodHeadModel.builder()
                        .modifier(MethodHeadModel.PRIVATE_FINAL)
                        .return_type(new TypeModel("FlvFile"))
                        .method_name("_d123FlvFile")
                        .method_parameters(Arrays.asList(
                                MethodParameterModel.builder()
                                        .param_name("buffer")
                                        .param_type(ProtocolGenerateConstant.BYTEBUF_TYPE)
                                        .build(),
                                MethodParameterModel.builder()
                                        .param_name("channel")
                                        .param_type(ProtocolGenerateConstant.CHANNEL_TYPE)
                                        .build(),
                                MethodParameterModel.builder()
                                        .param_name("standardWriterIndex")
                                        .param_type(ProtocolGenerateConstant.INTEGER_TYPE)
                                        .build()
                        ))
                        .is_override(false)
                        .build())
                // index, condition, length 获取缓冲区的解析部分
                .part0(ProtocolSpelProcessExpr.builder()
                        .convert_anon_model(convertAnonModel)
                        .buffer_parameter("buffer")
                        .channel_parameter("channel")
                        .standard_index_parameter("standardWriterIndex")
                        .build())
                // convertMethod 不为空的解析部分
                .part1(ProtocolEncodePart$0.builder()
                        .convertAnonModel(convertAnonModel)
                        .channel_parameter("channel")
                        .encode_type(new TypeModel("FlvFile"))
                        .encode_type_name("flvFile")
                        .confused_buffer_name(null)
                        .build())
                .buffer_parameter("buffer")
                .channel_parameter("channel")
                .standard_writer_index_parameter("standardWriterIndex")
                .member_mappings(new LinkedList<>())
                .convert_model(convertAnonModel)
                .encode_type(new TypeModel("FlvFile"))
                .encode_element_name("flvFile")
                .is_not_primitive(true)
                .is_spel_object(true)
                .is_collection(false)
                .is_un_root(false)
                .confused_buffer_name(null)
                .confused_standard_writer_index_name(null)
                .build();
        Arrays.asList(
                new InternalModelContext(new TypeModel("String"), "signature", "_6c186Signature"),
                new InternalModelContext(new TypeModel("Integer"), "version", "_0d93aVersion"),
                new InternalModelContext(new TypeModel("Integer"), "typeFlagsReserved", "_6a8c5TypeFlagsReserved"),
                new InternalModelContext(new TypeModel("Integer"), "typeFlagsAudio", "_61bddTypeFlagsAudio"),
                new InternalModelContext(new TypeModel("Integer"), "typeFlagsReserved2", "_007deTypeFlagsReserved2"),
                new InternalModelContext(new TypeModel("Integer"), "typeFlagsVideo", "_57269TypeFlagsVideo"),
                new InternalModelContext(new TypeModel("Integer"), "dataOffset", "_819a1DataOffset"),
                new InternalModelContext(new TypeModel("List<FlvTag>"), "flvTags", "_1cd12FlvTags")
        ).forEach(_encode::addInternalModelContext);
        generator.generateModel(_encode);
    }

    @Test
    public void testEncodeInternalPrimitiveElement(){
        final ByteBufConvertAnonModel convertAnonModel = ByteBufConvertAnonModel.builder()
                .index("0")
                .length("3")
                .condition("true")
                .convert_method("AsciiConvertMethod")
                .build();
        final ProtocolEncode$1 _encode = ProtocolEncode$1.builder()
                .method_head(MethodHeadModel.builder()
                        .modifier(MethodHeadModel.PRIVATE_FINAL)
                        .return_type(new TypeModel("String"))
                        .method_name("_6d186Signature")
                        .method_parameters(Arrays.asList(
                                MethodParameterModel.builder()
                                        .param_name("buffer")
                                        .param_type(ProtocolGenerateConstant.BYTEBUF_TYPE)
                                        .build(),
                                MethodParameterModel.builder()
                                        .param_name("channel")
                                        .param_type(ProtocolGenerateConstant.CHANNEL_TYPE)
                                        .build(),
                                MethodParameterModel.builder()
                                        .param_name("standardWriterIndex")
                                        .param_type(ProtocolGenerateConstant.INTEGER_TYPE)
                                        .build()
                        ))
                        .build())
                // index, condition, length 获取缓冲区的解析部分
                .part0(ProtocolSpelProcessExpr.builder()
                        .convert_anon_model(convertAnonModel)
                        .buffer_parameter("buffer")
                        .channel_parameter("channel")
                        .standard_index_parameter("standardWriterIndex")
                        .build())
                // convertMethod 不为空的解析部分
                .part1(ProtocolEncodePart$0.builder()
                        .convertAnonModel(convertAnonModel)
                        .encode_type(new TypeModel("String"))
                        .encode_type_name("signature")
                        .channel_parameter("channel")
                        .confused_buffer_name("_7ddd3Buffer")
                        .build())
                .buffer_parameter("buffer")
                .channel_parameter("channel")
                .standard_writer_index_parameter("standardWriterIndex")
                .member_mappings(new LinkedList<>())
                .convert_model(convertAnonModel)
                .encode_type(new TypeModel("String"))
                .encode_element_name("signature")
                .is_not_primitive(false)
                .is_spel_object(false)
                .is_collection(false)
                .is_un_root(true)
                .confused_buffer_name("_7ddd3Buffer")
                .confused_standard_writer_index_name(null)
                .build();
        generator.generateModel(_encode);
    }

    @Test
    public void testEncodeInternalCollectionElement(){
        final ByteBufConvertAnonModel convertAnonModel = ByteBufConvertAnonModel.builder()
                .index("9").length("#flvFile_buffer.readableBytes()").condition("true")
                .convert_method(null)
                .parameters(new String[]{"java.util.ArrayList"})
                .build();
        final ProtocolEncode$1 _encode = ProtocolEncode$1.builder()
                .method_head(MethodHeadModel.builder()
                        .modifier(MethodHeadModel.PRIVATE_FINAL)
                        .return_type(new TypeModel("List<FlvTag>"))
                        .method_name("_7f295FlvTags")
                        .method_parameters(Arrays.asList(
                                MethodParameterModel.builder()
                                        .param_name("buffer")
                                        .param_type(ProtocolGenerateConstant.BYTEBUF_TYPE)
                                        .build(),
                                MethodParameterModel.builder()
                                        .param_name("channel")
                                        .param_type(ProtocolGenerateConstant.CHANNEL_TYPE)
                                        .build(),
                                MethodParameterModel.builder()
                                        .param_name("standardWriterIndex")
                                        .param_type(ProtocolGenerateConstant.INTEGER_TYPE)
                                        .build()
                        ))
                        .build())
                .buffer_parameter("buffer")
                .channel_parameter("channel")
                .standard_writer_index_parameter("standardWriterIndex")
                // index, condition, length 获取缓冲区的解析部分
                .part0(ProtocolSpelProcessExpr.builder()
                        .convert_anon_model(convertAnonModel)
                        .buffer_parameter("buffer")
                        .channel_parameter("channel")
                        .standard_index_parameter("standardWriterIndex")
                        .build())
                // convertMethod 不为空的解析部分
                .part1(ProtocolEncodePart$0.builder()
                        .convertAnonModel(convertAnonModel)
                        .encode_type(new TypeModel("List<FlvTag>"))
                        .encode_type_name("flvTags")
                        .channel_parameter("channel")
                        .confused_buffer_name("_ef449Buffer")
                        .build())
                .member_mappings(new LinkedList<>())
                .convert_model(convertAnonModel)
                .encode_type(new TypeModel("List<FlvTag>"))
                .encode_element_name("flvTags")
                .is_not_primitive(true)
                .is_spel_object(false)
                .is_collection(true)
                .is_un_root(true)
                .confused_buffer_name("_ef449Buffer")
                .confused_standard_writer_index_name("_13f07StandardWriterIndex")
                .build();
        _encode.addInternalModelContext(
                new InternalModelContext(new TypeModel("FlvTag"), "flvTag", "_fg9baFlvTag")
        );
        generator.generateModel(_encode);
    }

    @Test
    public void testEncodeInternalSpelMappingObjectElement(){
        final ByteBufConvertAnonModel convertAnonModel = ByteBufConvertAnonModel.builder()
                .index("15")
                .length("#flvTag.dataLength")
                .condition("#flvTag.tagType == 8")
                .convert_method(null)
                .build();
        final ProtocolEncode$1 _encode = ProtocolEncode$1.builder()
                .method_head(MethodHeadModel.builder()
                        .modifier(MethodHeadModel.PRIVATE_FINAL)
                        .return_type(ProtocolGenerateConstant.VOID_TYPE)
                        .method_name("_94d67FlvAudioTagBody")
                        .method_parameters(Arrays.asList(
                                MethodParameterModel.builder()
                                        .param_name("buffer")
                                        .param_type(ProtocolGenerateConstant.BYTEBUF_TYPE)
                                        .build(),
                                MethodParameterModel.builder()
                                        .param_name("flvAudioTagBody")
                                        .param_type(new TypeModel("FlvAudioTagBody"))
                                        .build(),
                                MethodParameterModel.builder()
                                        .param_name("channel")
                                        .param_type(ProtocolGenerateConstant.CHANNEL_TYPE)
                                        .build(),
                                MethodParameterModel.builder()
                                        .param_name("standardWriterIndex")
                                        .param_type(ProtocolGenerateConstant.INTEGER_TYPE)
                                        .build()
                        ))
                        .build())
                // index, condition, length 获取缓冲区的解析部分
                .part0(ProtocolSpelProcessExpr.builder()
                        .convert_anon_model(convertAnonModel)
                        .buffer_parameter("buffer")
                        .channel_parameter("channel")
                        .standard_index_parameter("standardWriterIndex")
                        .build())
                // convertMethod 不为空的解析部分
                .part1(ProtocolEncodePart$0.builder()
                        .convertAnonModel(convertAnonModel)
                        .encode_type(new TypeModel("FlvAudioTagBody"))
                        .encode_type_name("flvAudioTagBody")
                        .channel_parameter("channel")
                        .confused_buffer_name("_8e7f9Buffer")
                        .build())
                .buffer_parameter("buffer")
                .channel_parameter("channel")
                .standard_writer_index_parameter("standardWriterIndex")
                .member_mappings(new LinkedList<>())
                .convert_model(convertAnonModel)
                .encode_type(new TypeModel("FlvAudioTagBody"))
                .encode_element_name("flvAudioTagBody")
                .is_not_primitive(true)
                .is_spel_object(true)
                .is_collection(false)
                .is_un_root(true)
                .confused_buffer_name("_8e7f9Buffer")
                .confused_standard_writer_index_name("_cfca7StandardWriterIndex")
                .build();
        _encode.addInternalModelContext(
                new InternalModelContext(new TypeModel("Integer"), "soundFormat", "_8acfbSoundFormat")
        );
        generator.generateModel(_encode);
    }
}
