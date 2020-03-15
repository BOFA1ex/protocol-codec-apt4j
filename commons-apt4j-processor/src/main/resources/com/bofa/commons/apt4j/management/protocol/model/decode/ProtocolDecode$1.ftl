<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#import "../../../internal/lib/ModelBuilder.ftl" as builder/>
<#--记录当前的基准readerIndex, 如果不为解析元素不为基本类型, 需要变更基准readerIndex -->
<#assign current_standard_reader_index_parameter=standard_reader_index_parameter/>
<#assign current_buffer_name=buffer_parameter/>
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.decode.ProtocolDecode$1" -->
<@builder.build_method_head method_head>
    <#if is_un_root()>
    <#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.common.ProtocolSpelProcessExpr" -->
        <@includeModel object=part0/>
        final ByteBuf ${confused_buffer_name} = markAndReadSlice(${buffer_parameter}, index, length);
        <#assign current_buffer_name=confused_buffer_name/>
    </#if>
    <#if convert_model.convert_method??>
    <#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.decode.ProtocolDecodePart$0" -->
        <@includeModel object=part1/>
    <#else>
        <#if is_not_primitive()>
            <#if is_spel_object()>
                ${decode_type.type_name} ${decode_element_name} = new ${decode_type.type_name}();
            <#elseif is_collection()>
                ${decode_type.type_name} ${decode_element_name} = new ${convert_model.parameters[0]}();
            </#if>
            _init("${decode_element_name}", ${decode_element_name}, ${current_buffer_name}, ${channel_parameter});
        </#if>
        <#if is_spel_object()>
            <#if is_un_root()>
                // 这里要更改基准readerIndex
                final int ${confused_standard_reader_index_name} = ${confused_buffer_name}.readerIndex();
                <#assign current_standard_reader_index_parameter = confused_standard_reader_index_name/>
            </#if>
            <#if member_mappings??>
                <#list member_mappings as sub>
                    ${sub.member_type.type_name} ${sub.member_name} = ${sub.member_method_name}(${current_buffer_name}, ${channel_parameter}, ${current_standard_reader_index_parameter});
                    ${decode_element_name}.set${sub.member_name?cap_first}(${sub.member_name});
                </#list>
            </#if>
        </#if>
        <#if is_collection()>
            <#assign generic=member_mappings.get(0)/>
            while (${confused_buffer_name}.isReadable()) {
                // 这里要更改基准readerIndex
                final int ${confused_standard_reader_index_name} = ${confused_buffer_name}.readerIndex();
                ${generic.member_type.type_name} ${generic.member_name} = ${generic.member_method_name}(${confused_buffer_name}, ${channel_parameter}, ${confused_standard_reader_index_name});
                ${decode_element_name}.add(${generic.member_name});
            }
        </#if>
    </#if>
    return ${decode_element_name};
</@builder.build_method_head>
