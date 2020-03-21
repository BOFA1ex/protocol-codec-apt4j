<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.encode.ProtocolEncode$1" -->
<#import "../../../internal/lib/ModelBuilder.ftl" as builder/>
<#--记录当前的基准writerIndex, 如果不为解析元素不为基本类型, 需要变更基准writerIndex -->
<#assign current_standard_writer_index_parameter=standard_writer_index_parameter/>
<#assign current_buffer_name=buffer_parameter/>
<@builder.build_method_head method_head>
    if (${encode_element_name} == null) {
        return;
    }
    <#if is_un_root()>
    <#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.common.ProtocolSpelProcessExpr" -->
        <@includeModel object=part0/>
    </#if>
    <#if convert_model.convert_method_qualifier??>
    <#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.encode.ProtocolEncodePart$0" -->
        <@includeModel object=part1/>
    <#else>
        <#if is_not_primitive()>
            <#if is_un_root()>
                final ByteBuf ${confused_buffer_name} = ${buffer_parameter}.slice(index, length).clear();
                <#assign current_buffer_name=confused_buffer_name/>
            </#if>
            _init("${encode_element_name}", ${encode_element_name}, ${current_buffer_name}, ${channel_parameter});
        </#if>
        <#if is_spel_object()>
            <#if is_un_root()>
                // 这里要更改基准writerIndex
                final int ${confused_standard_writer_index_name} = ${current_buffer_name}.writerIndex();
                <#assign current_standard_writer_index_parameter = confused_standard_writer_index_name/>
            </#if>
            <#if member_mappings??>
                <#list member_mappings as sub>
                    ${sub.member_method_name}(${current_buffer_name}, ${encode_element_name}.get${sub.member_name?cap_first}(), ${channel_parameter}, ${current_standard_writer_index_parameter});
                </#list>
            </#if>
            <#-- issue#1 如果spelMapping不是根对象, 需要回收新缓冲区的writerIndex -->
            <#if is_un_root()>
                ${buffer_parameter}.writerIndex(${buffer_parameter}.writerIndex() + ${current_buffer_name}.writerIndex());
            </#if>
        </#if>
        <#if is_collection()>
            <#assign generic=member_mappings.get(0)/>
            <#assign generic_member_name = generic.member_name?uncap_first/>
            for (final ${generic.member_type.type_name} ${generic_member_name} : ${encode_element_name}) {
                // 这里要更改基准writerIndex
                final int ${confused_standard_writer_index_name} = ${current_buffer_name}.writerIndex();
                ${generic.member_method_name}(${current_buffer_name}, ${generic_member_name}, ${channel_parameter}, ${confused_standard_writer_index_name});
            }
            ${buffer_parameter}.writerIndex(${buffer_parameter}.writerIndex() + ${current_buffer_name}.writerIndex());
        </#if>
    </#if>
</@builder.build_method_head>