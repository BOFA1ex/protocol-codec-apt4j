<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#import "../../../internal/lib/ModelBuilder.ftl" as builder/>
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.decode.ProtocolDecode$0" -->
<@builder.build_method_head method_head>
    <#if init_validation??>
        // 校验缓冲区报文
        <@includeModel object=init_validation/>
    </#if>
    // 基准readerIndex standard_reader_index
    final int standard_reader_index = ${buffer_parameter}.readerIndex();
    ${decode_type.type_simple_name} ${decode_element_name} = null;
    try {
        ${decode_element_name} = ${decode_method_name}(${buffer_parameter}, ${channel_parameter}, standard_reader_index);
    } catch(Exception e) {
        <@includeModel object=init_resolve_exception/>
    }
    return ${decode_element_name};
</@builder.build_method_head>
