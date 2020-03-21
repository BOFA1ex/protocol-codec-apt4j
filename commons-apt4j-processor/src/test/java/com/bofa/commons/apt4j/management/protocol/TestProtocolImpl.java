package com.bofa.commons.apt4j.management.protocol;

import com.bofa.commons.apt4j.management.internal.extension.FreeMarkerModelGenerator;
import com.bofa.commons.apt4j.management.internal.model.*;
import com.bofa.commons.apt4j.management.protocol.constant.ProtocolGenerateConstant;
import com.bofa.commons.apt4j.management.protocol.model.ProtocolImpl;
import com.bofa.commons.apt4j.management.protocol.model.common.*;
import com.bofa.commons.apt4j.management.protocol.model.decode.*;
import com.bofa.commons.apt4j.management.protocol.model.encode.*;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * @author bofa1ex
 * @since 2020/3/14
 */
public class TestProtocolImpl {

    FreeMarkerModelGenerator generator = new FreeMarkerModelGenerator();
    ProtocolEncode$0 overrideEncodeMethod;
    ProtocolDecode$0 overrideDecodeMethod;
    ProtocolDecode$1 decodeRootObject;
    ProtocolEncode$1 encodeRootObject;
    InitChannelSpelContextMethod initChannelSpelContextMethod;
    InitMarkAndReadSliceMethod initMarkAndReadSliceMethod;

    @Before
    public void prepare() {
        overrideEncodeMethod = _buildProtocolEncodeEntrance();
        overrideDecodeMethod = _buildProtocolDecodeEntrance();
        decodeRootObject = _buildProtocolDecodeRootObject();
        encodeRootObject = _buildProtocolEncodeRootObject();
        initChannelSpelContextMethod = _buildInitChannelSpelContextMethod();
        initMarkAndReadSliceMethod = _buildInitMarkAndReadSliceMethod();
    }

    @Test
    public void testProtocolImpl() {
        ProtocolImpl _impl = ProtocolImpl.builder()
                .package_name("com.bofa.protocol.flv")
                .type_head(TypeHeadModel.builder()
                        .modifier(TypeHeadModel.DEFAULT_MODIFIER)
                        .impl_type(new TypeModel("FlvParserImpl"))
                        .interface_type(new TypeModel("FlvParser"))
                        .needImplement(true)
                        .build())
                .decode_root_element(_buildProtocolDecodeEntrance())
                .decode_elements( Collections.singletonList(_buildProtocolDecodeRootObject()))
                .encode_root_element(_buildProtocolEncodeEntrance())
                .encode_elements( Collections.singletonList(_buildProtocolEncodeRootObject()))
                .common_methods( Arrays.asList(
                        _buildInitChannelSpelContextMethod(),
                        _buildInitMarkAndReadSliceMethod()
                ))
                .build();
        generator.generateModel(_impl);
    }


    static ProtocolEncode$0 _buildProtocolEncodeEntrance() {
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
                .is_validate(true)
                .build();
        return ProtocolEncode$0.builder()
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
    }

    static ProtocolDecode$0 _buildProtocolDecodeEntrance() {
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
                .anon_params(Arrays.asList("1", "2"))
                .buffer_parameter("buffer")
                .channel_parameter("channel")
                // validate/mapper flag
                .is_validate(true)
                .build();
        ProtocolDecode$0 protocolDecode = ProtocolDecode$0.builder()
                .method_head(methodHead)
                .decode_type(new TypeModel("FlvFile"))
                .decode_element_name("flvFile")
                .decode_method_name("_decodeFlvFile")
                .buffer_parameter("buffer")
                .channel_parameter("channel")
                .init_validations(Lists.newLinkedList())
                .init_resolve_exception(InitResolveException.builder()
                        .channel_parameter("channel")
                        .decode_element_name("flvFile")
                        .decode_type(new TypeModel("FlvFile"))
                        .resolve_exception_simple_name("FlvDecodeResolveExceptionMethod")
                        .build())
                .build();
        protocolDecode.addInit_validation(initValidation);
        return protocolDecode;
    }

    static ProtocolDecode$1 _buildProtocolDecodeRootObject() {
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
        return _decode;
    }

    static ProtocolEncode$1 _buildProtocolEncodeRootObject() {
        final ByteBufConvertAnonModel convertAnonModel = ByteBufConvertAnonModel.builder().build();
        final ProtocolEncode$1 _encode = ProtocolEncode$1.builder()
                .method_head(MethodHeadModel.builder()
                        .modifier(MethodHeadModel.PRIVATE_FINAL)
                        .return_type(new TypeModel("void"))
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
                        .build())
                // index, condition, length 获取缓冲区的解析部分
                .part0(ProtocolSpelProcessExpr.builder()
                        .convert_anon_model(convertAnonModel)
                        .buffer_parameter("buffer")
                        .channel_parameter("channel")
                        .standard_index_parameter(null)
                        .build())
                // convertMethod 不为空的解析部分
                .part1(ProtocolEncodePart$0.builder()
                        .convert_anon_model(convertAnonModel)
                        .channel_parameter("channel")
                        .encode_element_name("flvFile")
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
                new InternalModelContext(new TypeModel("String"), "signature", "_6d186Signature"),
                new InternalModelContext(new TypeModel("Integer"), "version", "_0d94aVersion"),
                new InternalModelContext(new TypeModel("Integer"), "typeFlagsReserved", "_6a8c6TypeFlagsReserved"),
                new InternalModelContext(new TypeModel("Integer"), "typeFlagsAudio", "_61bddTypeFlagsAudio"),
                new InternalModelContext(new TypeModel("Integer"), "typeFlagsReserved2", "_007deTypeFlagsReserved2"),
                new InternalModelContext(new TypeModel("Integer"), "typeFlagsVideo", "_57269TypeFlagsVideo"),
                new InternalModelContext(new TypeModel("Integer"), "dataOffset", "_819a1DataOffset"),
                new InternalModelContext(new TypeModel("List<FlvTag>"), "flvTags", "_1cd12FlvTags")
        ).forEach(_encode::addInternalModelContext);
        return _encode;
    }

    static InitChannelSpelContextMethod _buildInitChannelSpelContextMethod() {
        return InitChannelSpelContextMethod.builder()
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
    }

    static InitMarkAndReadSliceMethod _buildInitMarkAndReadSliceMethod() {
        return InitMarkAndReadSliceMethod.builder()
                .log_message(InitMarkAndReadSliceMethod.SYSTEM_LOG_MESSAGE)
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
                .buffer_parameter("buffer")
                .reader_index_parameter("index")
                .length_parameter("length")
                .build();
    }
}
