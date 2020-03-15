package com.bofa.commons.apt4j.management.protocol;

import com.bofa.commons.apt4j.management.internal.extension.FreeMarkerModelGenerator;
import com.bofa.commons.apt4j.management.internal.model.*;
import com.bofa.commons.apt4j.management.protocol.constant.ProtocolGenerateConstant;
import com.bofa.commons.apt4j.management.protocol.model.ProtocolImpl;
import com.bofa.commons.apt4j.management.protocol.model.common.*;
import com.bofa.commons.apt4j.management.protocol.model.decode.*;
import com.bofa.commons.apt4j.management.protocol.model.encode.*;
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
        ProtocolImpl _impl = new ProtocolImpl(
                "com.bofa.protocol.flv",
                new TypeHead("public final", new Type("FlvParserImpl"), new Type("FlvParser"), null, true, null, null, false),
                _buildProtocolDecodeEntrance(),
                Collections.singletonList(_buildProtocolDecodeRootObject()),
                _buildProtocolEncodeEntrance(),
                Collections.singletonList(_buildProtocolEncodeRootObject()),
                Arrays.asList(
                        _buildInitChannelSpelContextMethod(),
                        _buildInitMarkAndReadSliceMethod()
                )
        );
        generator.generateModel(_impl);
    }


    static ProtocolEncode$0 _buildProtocolEncodeEntrance() {
        MethodParameter m1 = new MethodParameter(new Type("FlvFile"), "flvFile");
        MethodParameter m2 = new MethodParameter(new Type("Channel"), "channel");
        MethodHead methodHead = MethodHead.builder()
                .modifier(MethodHead.PRIVATE_FINAL)
                .return_type(new Type("ByteBuf"))
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
                .is_validate(true)
                .build();
        return ProtocolEncode$0.builder()
                .method_head(methodHead)
                .validate_condition(initValidation)
                .encode_type(new Type("FlvFile"))
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
        MethodParameter m1 = new MethodParameter(new Type("ByteBuf"), "buffer");
        MethodParameter m2 = new MethodParameter(new Type("Channel"), "channel");
        MethodHead methodHead = MethodHead.builder()
                .modifier(MethodHead.PRIVATE_FINAL)
                .return_type(new Type("FlvFile"))
                .method_name("decode")
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
                .is_validate(true)
                .build();
        ProtocolDecode$0 protocolDecode = ProtocolDecode$0.builder()
                .method_head(methodHead)
                .decode_type(new Type("FlvFile"))
                .decode_element_name("flvFile")
                .decode_method_name("_decodeFlvFile")
                .buffer_parameter("buffer")
                .channel_parameter("channel")
                .resolve_exception_name("FlvDecodeResolveExceptionMethod")
                .build();
        protocolDecode.setValidate_condition(initValidation);
        return protocolDecode;
    }

    static ProtocolDecode$1 _buildProtocolDecodeRootObject() {
        final ByteBufConvertAnonModel convertAnonModel = ByteBufConvertAnonModel.builder().build();
        final ProtocolDecode$1 _decode = ProtocolDecode$1.builder()
                .method_head(MethodHead.builder()
                        .modifier(MethodHead.PRIVATE_FINAL)
                        .return_type(new Type("FlvFile"))
                        .method_name("_e12fdFlvFile")
                        .method_parameters(Arrays.asList(
                                MethodParameter.builder()
                                        .param_name("buffer")
                                        .param_type(ProtocolGenerateConstant.BYTEBUF_TYPE)
                                        .build(),
                                MethodParameter.builder()
                                        .param_name("channel")
                                        .param_type(ProtocolGenerateConstant.CHANNEL_TYPE)
                                        .build(),
                                MethodParameter.builder()
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
                        .convertAnonModel(convertAnonModel)
                        .decode_type(new Type("FlvFile"))
                        .channel_parameter("channel")
                        .decode_type_name("flvFile")
                        .confused_buffer_name(null)
                        .build())
                .buffer_parameter("buffer")
                .channel_parameter("channel")
                .standard_reader_index_parameter("standardReaderIndex")
                .member_mappings(new LinkedList<>())
                .convert_model(convertAnonModel)
                .decode_type(new Type("FlvFile"))
                .decode_element_name("flvFile")
                .is_not_primitive(true)
                .is_spel_object(true)
                .is_collection(false)
                .is_un_root(false)
                .confused_buffer_name(null)
                .confused_standard_reader_index_name(null)
                .build();
        Arrays.asList(
                new InternalModelContext(new Type("String"), "signature", "_6c186Signature"),
                new InternalModelContext(new Type("Integer"), "version", "_0d93aVersion"),
                new InternalModelContext(new Type("Integer"), "typeFlagsReserved", "_6a8c5TypeFlagsReserved"),
                new InternalModelContext(new Type("Integer"), "typeFlagsAudio", "_61bddTypeFlagsAudio"),
                new InternalModelContext(new Type("Integer"), "typeFlagsReserved2", "_007deTypeFlagsReserved2"),
                new InternalModelContext(new Type("Integer"), "typeFlagsVideo", "_57269TypeFlagsVideo"),
                new InternalModelContext(new Type("Integer"), "dataOffset", "_819a1DataOffset"),
                new InternalModelContext(new Type("List<FlvTag>"), "flvTags", "_1cd12FlvTags")
        ).forEach(_decode::addInternalModelContext);
        return _decode;
    }

    static ProtocolEncode$1 _buildProtocolEncodeRootObject() {
        final ByteBufConvertAnonModel convertAnonModel = ByteBufConvertAnonModel.builder().build();
        final ProtocolEncode$1 _encode = ProtocolEncode$1.builder()
                .method_head(MethodHead.builder()
                        .modifier(MethodHead.PRIVATE_FINAL)
                        .return_type(new Type("void"))
                        .method_name("_d123FlvFile")
                        .method_parameters(Arrays.asList(
                                MethodParameter.builder()
                                        .param_name("buffer")
                                        .param_type(ProtocolGenerateConstant.BYTEBUF_TYPE)
                                        .build(),
                                MethodParameter.builder()
                                        .param_name("channel")
                                        .param_type(ProtocolGenerateConstant.CHANNEL_TYPE)
                                        .build(),
                                MethodParameter.builder()
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
                        .convertAnonModel(convertAnonModel)
                        .channel_parameter("channel")
                        .encode_type_name("flvFile")
                        .build())
                .buffer_parameter("buffer")
                .channel_parameter("channel")
                .standard_writer_index_parameter("standardWriterIndex")
                .member_mappings(new LinkedList<>())
                .convert_model(convertAnonModel)
                .encode_type(new Type("FlvFile"))
                .encode_element_name("flvFile")
                .is_not_primitive(true)
                .is_spel_object(true)
                .is_collection(false)
                .is_un_root(false)
                .confused_buffer_name(null)
                .confused_standard_writer_index_name(null)
                .build();
        Arrays.asList(
                new InternalModelContext(new Type("String"), "signature", "_6d186Signature"),
                new InternalModelContext(new Type("Integer"), "version", "_0d94aVersion"),
                new InternalModelContext(new Type("Integer"), "typeFlagsReserved", "_6a8c6TypeFlagsReserved"),
                new InternalModelContext(new Type("Integer"), "typeFlagsAudio", "_61bddTypeFlagsAudio"),
                new InternalModelContext(new Type("Integer"), "typeFlagsReserved2", "_007deTypeFlagsReserved2"),
                new InternalModelContext(new Type("Integer"), "typeFlagsVideo", "_57269TypeFlagsVideo"),
                new InternalModelContext(new Type("Integer"), "dataOffset", "_819a1DataOffset"),
                new InternalModelContext(new Type("List<FlvTag>"), "flvTags", "_1cd12FlvTags")
        ).forEach(_encode::addInternalModelContext);
        return _encode;
    }

    static InitChannelSpelContextMethod _buildInitChannelSpelContextMethod() {
        return InitChannelSpelContextMethod.builder()
                .method_head(MethodHead.builder()
                        .modifier(MethodHead.PRIVATE_FINAL)
                        .return_type(new Type("void"))
                        .method_name("_init")
                        .method_parameters(Arrays.asList(
                                new MethodParameter(new Type("String"), "objName"),
                                new MethodParameter(new Type("Object"), "obj"),
                                new MethodParameter(new Type("ByteBuf"), "buffer"),
                                new MethodParameter(new Type("Channel"), "channel")
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
                .log_message(InitMarkAndReadSliceMethod.DEFAULT_LOG_MESSAGE)
                .method_head(MethodHead.builder()
                        .modifier(MethodHead.PRIVATE_FINAL)
                        .return_type(new Type("ByteBuf"))
                        .method_name("markAndReadSlice")
                        .method_parameters(Arrays.asList(
                                new MethodParameter(new Type("ByteBuf"), "buffer"),
                                new MethodParameter(new Type("int"), "index"),
                                new MethodParameter(new Type("int"), "length")
                        ))
                        .is_override(false)
                        .build())
                .buffer_parameter("buffer")
                .reader_index_parameter("index")
                .length_parameter("length")
                .build();
    }
}
