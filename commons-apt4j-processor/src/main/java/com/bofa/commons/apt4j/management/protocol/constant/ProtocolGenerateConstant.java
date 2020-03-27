package com.bofa.commons.apt4j.management.protocol.constant;


import com.bofa.commons.apt4j.management.internal.model.TypeModel;

/**
 * @author bofa1ex
 * @since 2020/2/16
 */
public interface ProtocolGenerateConstant {

    /* markAndReadSlice方法名及参数名 */
    String _READ_SLICE_METHOD_NAME = "readSlice";
    String _READ_SLICE_METHOD_PARAMETER_BUFFER = "buffer";
    String _READ_SLICE_METHOD_PARAMETER_INDEX = "index";
    String _READ_SLICE_METHOD_PARAMETER_LENGTH = "length";

    /* init方法名及参数名 */
    String _INIT_METHOD_NAME = "_init";
    String _INIT_METHOD_PARAMETER_OBJECT_NAME = "objName";
    String _INIT_METHOD_PARAMETER_OBJECT = "obj";
    String _INIT_METHOD_PARAMETER_CHANNEL = "channel";

    /* 外部依赖引入的typeName */
    String CHANNEL_UTILS_QUALIFIER_NAME = "com.bofa.protocol.codec.util.ChannelCodecContextUtils";
    String CHANNEL_UTILS_TYPE_NAME = "ChannelSpelContextUtils";
    String CHANNEL_QUALIFIER_NAME = "io.netty.channel.Channel";
    String CHANNEL_TYPE_NAME = "Channel";
    String BYTEBUF_QUALIFIER_NAME = "io.netty.buffer.ByteBuf";
    String BYTEBUF_TYPE_NAME = "ByteBuf";
    String VOID_QUALIFIER_NAME = "void";
    String OBJECT_TYPE_NAME = "Object";
    String OBJECT_QUALIFIER_NAME = "java.lang.Object";

    TypeModel CHANNEL_UTILS_TYPE = new TypeModel(CHANNEL_UTILS_TYPE_NAME, CHANNEL_UTILS_QUALIFIER_NAME);
    TypeModel BYTEBUF_TYPE = new TypeModel(BYTEBUF_TYPE_NAME, BYTEBUF_QUALIFIER_NAME);
    TypeModel CHANNEL_TYPE = new TypeModel(CHANNEL_TYPE_NAME, CHANNEL_QUALIFIER_NAME);
    TypeModel INTEGER_TYPE = new TypeModel("Integer", "java.lang.Integer");
    TypeModel STRING_TYPE = new TypeModel("String", "java.lang.String");
    TypeModel OBJECT_TYPE = new TypeModel(OBJECT_TYPE_NAME, OBJECT_QUALIFIER_NAME);
    TypeModel VOID_TYPE = new TypeModel("void", "void");

    String PRIMITIVE = "0";
    String SPEL_OBJ = "1";
    String COLLECTION = "2";
}
