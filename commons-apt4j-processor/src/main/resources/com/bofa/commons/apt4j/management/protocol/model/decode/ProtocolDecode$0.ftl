<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#import "../../../internal/lib/ModelBuilder.ftl" as builder/>
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.decode.ProtocolDecode$0" -->
<@builder.build_method_head method_head>
    <#if validate_condition??>
        // 校验缓冲区报文
        <@includeModel object=validate_condition/>
    </#if>
    // 基准readerIndex standard_reader_index
    final int standard_reader_index = ${buffer_parameter}.readerIndex();
    ${decode_type.type_name} ${decode_element_name} = null;
    try {
        ${decode_element_name} = ${decode_method_name}(${buffer_parameter}, ${channel_parameter}, standard_reader_index);
    } catch(Exception e) {
        ${decode_element_name} = (${decode_type.type_name})${resolve_exception_name}.INSTANCE.resolveException(${decode_type.type_name}.class, e, ${channel_parameter});
    }
    return ${decode_element_name};
</@builder.build_method_head>
