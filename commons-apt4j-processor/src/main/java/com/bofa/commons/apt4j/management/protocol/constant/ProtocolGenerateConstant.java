package com.bofa.commons.apt4j.management.protocol.constant;


import com.bofa.commons.apt4j.management.internal.model.Type;

/**
 * @author bofa1ex
 * @since 2020/2/16
 */
public interface ProtocolGenerateConstant {

    String DEFAULT_STANDARD_READER_INDEX = "standardReaderIndex";
    String DEFAULT_STANDARD_WRITER_INDEX = "standardWriterIndex";

    /* markAndReadSlice方法名及参数名 */
    String _MARK_METHOD_NAME = "markAndReadSlice";
    String _MARK_METHOD_PARAMETER_BUFFER = "buffer";
    String _MARK_METHOD_PARAMETER_INDEX = "index";
    String _MARK_METHOD_PARAMETER_LENGTH = "length";

    /* init方法名及参数名 */
    String _INIT_METHOD_NAME = "_init";
    String _INIT_METHOD_PARAMETER_OBJECT_NAME = "objName";
    String _INIT_METHOD_PARAMETER_OBJECT = "obj";
    String _INIT_METHOD_PARAMETER_BUFFER = "buffer";
    String _INIT_METHOD_PARAMETER_CHANNEL = "channel";

    /* 外部依赖引入的typeName */
    String CHANNEL_UTILS_QUALIFIER_NAME = "com.bofa.resolve.util.ChannelSpelContextUtils";
    String CHANNEL_UTILS_TYPE_NAME = "ChannelSpelContextUtils";
    String CHANNEL_QUALIFIER_NAME = "io.netty.channel.Channel";
    String CHANNEL_TYPE_NAME = "Channel";
    String BYTEBUF_QUALIFIER_NAME = "io.netty.buffer.ByteBuf";
    String BYTEBUF_TYPE_NAME = "ByteBuf";
    String VOID_QUALIFIER_NAME = "void";
    String OBJECT_TYPE_NAME = "Object";
    String OBJECT_QUALIFIER_NAME = "java.lang.Object";

    Type CHANNEL_UTILS_TYPE = new Type(CHANNEL_UTILS_TYPE_NAME, CHANNEL_UTILS_QUALIFIER_NAME);
    Type BYTEBUF_TYPE = new Type(BYTEBUF_TYPE_NAME, BYTEBUF_QUALIFIER_NAME);
    Type CHANNEL_TYPE = new Type(CHANNEL_TYPE_NAME, CHANNEL_QUALIFIER_NAME);
    Type INTEGER_TYPE = new Type("Integer", "java.lang.Integer");
    Type STRING_TYPE = new Type("String", "java.lang.String");
    Type OBJECT_TYPE = new Type(OBJECT_TYPE_NAME, OBJECT_QUALIFIER_NAME);
    Type VOID_TYPE = new Type("void", "void");

//    Type INTEGER_TYPE = new Type("Integer","java.lang.Integer");
//    Type INTEGER_TYPE = new Type("Integer","java.lang.Integer");

    String PRIMITIVE = "0";
    String SPEL_OBJ = "1";
    String COLLECTION = "2";
}
