package com.bofa.commons.apt4j.management.protocol;

import com.bofa.commons.apt4j.management.internal.extension.FreeMarkerModelGenerator;
import com.bofa.commons.apt4j.management.internal.model.*;
import com.bofa.commons.apt4j.management.protocol.constant.ProtocolGenerateConstant;
import com.bofa.commons.apt4j.management.protocol.model.common.*;
import com.bofa.commons.apt4j.management.protocol.model.decode.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author bofa1ex
 * @since 2020/3/14
 */
public class TestDecodeMethod {

    FreeMarkerModelGenerator generator = new FreeMarkerModelGenerator();

    @Test
    public void testDecodeInternalCollectionElement() {
        final ByteBufConvertAnonModel convertAnonModel = ByteBufConvertAnonModel.builder()
                .index("9").length("#flvFile_buffer.readableBytes()").condition("true")
                .parameters(new String[]{"java.util.ArrayList"})
                .build();
        final ProtocolDecode$1 _decode = ProtocolDecode$1.builder()
                .method_head(MethodHeadModel.builder()
                        .modifier(MethodHeadModel.PRIVATE_FINAL)
                        .return_type(new TypeModel("List<FlvTag>"))
                        .method_name("_1cd12FlvTags")
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
                                        .param_name("standardReaderIndex")
                                        .param_type(ProtocolGenerateConstant.INTEGER_TYPE)
                                        .build()
                        ))
                        .build())
                .buffer_parameter("buffer")
                .channel_parameter("channel")
                .standard_reader_index_parameter("standardReaderIndex")
                // index, condition, length 获取缓冲区的解析部分
                .part0(ProtocolSpelProcessExpr.builder()
                        .convert_anon_model(convertAnonModel)
                        .buffer_parameter("buffer")
                        .channel_parameter("channel")
                        .standard_index_parameter("standardReaderIndex")
                        .build())
                // convertMethod 不为空的解析部分
                .part1(ProtocolDecodePart$0.builder()
                        .convert_anon_model(convertAnonModel)
                        .decode_type(new TypeModel("List<FlvTag>"))
                        .channel_parameter("channel")
                        .decode_type_name("flvTags")
                        .confused_buffer_name("_ef449Buffer")
                        .build())
                .member_mappings(new LinkedList<>())
                .convert_model(convertAnonModel)
                .decode_type(new TypeModel("List<FlvTag>"))
                .decode_element_name("flvTags")
                .is_not_primitive(true)
                .is_spel_object(false)
                .is_collection(true)
                .is_un_root(true)
                .confused_buffer_name("_ef449Buffer")
                .confused_standard_reader_index_name("_13f07StandardReaderIndex")
                .build();
        _decode.addInternalModelContext(
                new InternalModelContext(new TypeModel("FlvTag"), "flvTag", "_fd9baFlvTag")
        );
        generator.generateModel(_decode);
    }


    @Test
    public void testDecodeOverrideMethod() {
        MethodParameterModel m1 = new MethodParameterModel(new TypeModel("ByteBuf"), "buffer");
        MethodParameterModel m2 = new MethodParameterModel(new TypeModel("Channel"), "channel");
        MethodHeadModel methodHead = MethodHeadModel.builder()
                .modifier(MethodHeadModel.PRIVATE_FINAL)
                .return_type(new TypeModel("FlvFile"))
                .method_name("decode")
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
                .anon_params(Arrays.asList("1","2"))
                .buffer_parameter("buffer")
                .channel_parameter("channel")
                // validate/mapper flag
                .is_validate(true)
                .build();
        ProtocolDecode$0 protocolDecode = ProtocolDecode$0.builder()
                .method_head(methodHead)
                .init_validation(initValidation)
                .decode_type(new TypeModel("FlvFile"))
                .decode_element_name("flvFile")
                .decode_method_name("_decodeFlvFile")
                .buffer_parameter("buffer")
                .channel_parameter("channel")
                .init_resolve_exception(InitResolveException.builder()
                        .channel_parameter("channel")
                        .decode_element_name("flvFile")
                        .decode_type(new TypeModel("FlvFile"))
                        .resolve_exception_qualifier_name("FlvDecodeResolveExceptionMethod")
                        .build())
                .build();
        generator.generateModel(protocolDecode);
    }

    @Test
    public void testDecodeRootObjectMethod() {
        final ByteBufConvertAnonModel convertAnonModel = ByteBufConvertAnonModel.builder().build();
        final ProtocolDecode$1 _decode = ProtocolDecode$1.builder()
                .method_head(MethodHeadModel.builder()
                        .modifier(MethodHeadModel.PRIVATE_FINAL)
                        .return_type(new TypeModel("FlvFile"))
                        .method_name("_e12fdFlvFile")
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
                                        .param_name("standardReaderIndex")
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
                        .standard_index_parameter(null)
                        .build())
                // convertMethod 不为空的解析部分
                .part1(ProtocolDecodePart$0.builder()
                        .convert_anon_model(convertAnonModel)
                        .decode_type(new TypeModel("FlvFile"))
                        .channel_parameter("channel")
                        .decode_type_name("flvFile")
                        .confused_buffer_name(null)
                        .build())
                .buffer_parameter("buffer")
                .channel_parameter("channel")
                .standard_reader_index_parameter("standardReaderIndex")
                .member_mappings(new LinkedList<>())
                .convert_model(convertAnonModel)
                .decode_type(new TypeModel("FlvFile"))
                .decode_element_name("flvFile")
                .is_not_primitive(true)
                .is_spel_object(true)
                .is_collection(false)
                .is_un_root(false)
                .confused_buffer_name(null)
                .confused_standard_reader_index_name(null)
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
        ).forEach(_decode::addInternalModelContext);
        generator.generateModel(_decode);
    }

    @Test
    public void testDecodePrimitive() {
        final ByteBufConvertAnonModel convertAnonModel = ByteBufConvertAnonModel.builder()
                .index("0")
                .length("3")
                .condition("true")
                .convert_method_simple("AsciiConvertMethod")
                .build();
        final ProtocolDecode$1 _decode = ProtocolDecode$1.builder()
                .method_head(MethodHeadModel.builder()
                        .modifier(MethodHeadModel.PRIVATE_FINAL)
                        .return_type(new TypeModel("String"))
                        .method_name("_6c186Signature")
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
                                        .param_name("standardReaderIndex")
                                        .param_type(ProtocolGenerateConstant.INTEGER_TYPE)
                                        .build()
                        ))
                        .build())
                // index, condition, length 获取缓冲区的解析部分
                .part0(ProtocolSpelProcessExpr.builder()
                        .convert_anon_model(convertAnonModel)
                        .buffer_parameter("buffer")
                        .channel_parameter("channel")
                        .standard_index_parameter("standardReaderIndex")
                        .build())
                // convertMethod 不为空的解析部分
                .part1(ProtocolDecodePart$0.builder()
                        .convert_anon_model(convertAnonModel)
                        .decode_type(new TypeModel("String"))
                        .channel_parameter("channel")
                        .decode_type_name("signature")
                        .confused_buffer_name("_7ddd3Buffer")
                        .build())
                .buffer_parameter("buffer")
                .channel_parameter("channel")
                .standard_reader_index_parameter("standardReaderIndex")
                .member_mappings(new LinkedList<>())
                .convert_model(convertAnonModel)
                .decode_type(new TypeModel("String"))
                .decode_element_name("signature")
                .is_not_primitive(false)
                .is_spel_object(false)
                .is_collection(false)
                .is_un_root(true)
                .confused_buffer_name("_7ddd3Buffer")
                .confused_standard_reader_index_name(null)
                .build();
        generator.generateModel(_decode);
    }

    @Test
    public void testDecodeInternalSpelMappingObjectElement() {
        final ByteBufConvertAnonModel convertAnonModel = ByteBufConvertAnonModel.builder()
                .index("15")
                .length("#flvTag.dataLength")
                .condition("#flvTag.tagType == 8")
                .build();
        final ProtocolDecode$1 _decode = ProtocolDecode$1.builder()
                .method_head(MethodHeadModel.builder()
                        .modifier(MethodHeadModel.PRIVATE_FINAL)
                        .return_type(new TypeModel("FlvAudioTagBody"))
                        .method_name("_94d66FlvAudioTagBody")
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
                                        .param_name("standardReaderIndex")
                                        .param_type(ProtocolGenerateConstant.INTEGER_TYPE)
                                        .build()
                        ))
                        .build())
                // index, condition, length 获取缓冲区的解析部分
                .part0(ProtocolSpelProcessExpr.builder()
                        .convert_anon_model(convertAnonModel)
                        .buffer_parameter("buffer")
                        .channel_parameter("channel")
                        .standard_index_parameter("standardReaderIndex")
                        .build())
                // convertMethod 不为空的解析部分
                .part1(ProtocolDecodePart$0.builder()
                        .convert_anon_model(convertAnonModel)
                        .decode_type(new TypeModel("FlvAudioTagBody"))
                        .channel_parameter("channel")
                        .decode_type_name("flvAudioTagBody")
                        .confused_buffer_name("_8e7f9Buffer")
                        .build())
                .buffer_parameter("buffer")
                .channel_parameter("channel")
                .standard_reader_index_parameter("standardReaderIndex")
                .member_mappings(new LinkedList<>())
                .convert_model(convertAnonModel)
                .decode_type(new TypeModel("FlvAudioTagBody"))
                .decode_element_name("flvAudioTagBody")
                .is_not_primitive(true)
                .is_spel_object(true)
                .is_collection(false)
                .is_un_root(true)
                .confused_buffer_name("_8e7f9Buffer")
                .confused_standard_reader_index_name("_cfca7StandardReaderIndex")
                .build();
        _decode.addInternalModelContext(
                new InternalModelContext(new TypeModel("Integer"), "soundFormat", "_8acfbSoundFormat")
        );
        generator.generateModel(_decode);
    }
}
