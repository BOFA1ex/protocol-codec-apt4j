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
                .validate_qualifier_name("CheckSumValidateMethod")
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
                .init_validations(Collections.singletonList(initValidation))
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
                        .convert_anon_model(convertAnonModel)
                        .channel_parameter("channel")
                        .encode_type(new TypeModel("FlvFile"))
                        .encode_element_name("flvFile")
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
                InternalModelContext.builder()
                        .member_name("signature")
                        .member_method_name("_6c186Signature")
                        .member_type(new TypeModel("String"))
                        .build(),
                InternalModelContext.builder()
                        .member_name("version")
                        .member_method_name("_0d93aVersion")
                        .member_type(new TypeModel("Integer"))
                        .build(),
                InternalModelContext.builder()
                        .member_name("typeFlagsReserved")
                        .member_method_name("_6a8c5TypeFlagsReserved")
                        .member_type(new TypeModel("Integer"))
                        .build(),
                InternalModelContext.builder()
                        .member_name("typeFlagsAudio")
                        .member_method_name("_61bddTypeFlagsAudio")
                        .member_type(new TypeModel("Integer"))
                        .build(),
                InternalModelContext.builder()
                        .member_name("typeFlagsReserved2")
                        .member_method_name("_007deTypeFlagsReserved2")
                        .member_type(new TypeModel("Integer"))
                        .build(),
                InternalModelContext.builder()
                        .member_name("typeFlagsVideo")
                        .member_method_name("_57269TypeFlagsVideo")
                        .member_type(new TypeModel("Integer"))
                        .build(),
                InternalModelContext.builder()
                        .member_name("dataOffset")
                        .member_method_name("_819a1DataOffset")
                        .member_type(new TypeModel("Integer"))
                        .build(),
                InternalModelContext.builder()
                        .member_name("flvTags")
                        .member_method_name("_1cd12FlvTags")
                        .member_type(new TypeModel("List<FlvTag>"))
                        .build()
        ).forEach(_encode::addInternalModelContext);
        generator.generateModel(_encode);
    }

    @Test
    public void testEncodeInternalPrimitiveElement(){
        final ByteBufConvertAnonModel convertAnonModel = ByteBufConvertAnonModel.builder()
                .index("0")
                .length("3")
                .condition("true")
                .convert_method_simple("AsciiConvertMethod")
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
                        .convert_anon_model(convertAnonModel)
                        .encode_type(new TypeModel("String"))
                        .encode_element_name("signature")
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
                        .convert_anon_model(convertAnonModel)
                        .encode_type(new TypeModel("List<FlvTag>"))
                        .encode_element_name("flvTags")
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
                InternalModelContext.builder()
                        .member_name("flvTag")
                        .member_method_name("_fg9baFlvTag")
                        .member_type(new TypeModel("FlvTag"))
                        .build()
        );
        generator.generateModel(_encode);
    }

    @Test
    public void testEncodeInternalSpelMappingObjectElement(){
        final ByteBufConvertAnonModel convertAnonModel = ByteBufConvertAnonModel.builder()
                .index("15")
                .length("#flvTag.dataLength")
                .condition("#flvTag.tagType == 8")
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
                        .convert_anon_model(convertAnonModel)
                        .encode_type(new TypeModel("FlvAudioTagBody"))
                        .encode_element_name("flvAudioTagBody")
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
                InternalModelContext.builder()
                        .member_name("soundFormat")
                        .member_method_name("_8acfbSoundFormat")
                        .member_type(new TypeModel("Integer"))
                        .build()
        );
        generator.generateModel(_encode);
    }
}
