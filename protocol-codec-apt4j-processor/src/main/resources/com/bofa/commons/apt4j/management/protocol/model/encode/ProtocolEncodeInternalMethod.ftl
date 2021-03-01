<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.encode.ProtocolEncodeInternalMethod" -->
<#import "../../../internal/lib/ModelBuilder.ftl" as builder/>
<#assign current_buffer_name=buffer_parameter/>
<@builder.build_method_head method_head>
    if (${encode_element_name} == null) {
        return;
    }
    <#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.common.InitConditionIndexLength" -->
    <#if is_un_root()>
        <#lt><@includeModel object=part0/>
    </#if>
    <#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.encode.ProtocolEncodeConvertMethod" -->
    <#if resolveTypeMirror(part0.convert_anon)??>
        <@includeModel object=part1/>
    <#else>
        <#if is_not_primitive()>
            <#if is_un_root()>
                final ByteBuf ${confused_buffer_name} = ${buffer_parameter}.slice(index, length).clear();
                <#assign current_buffer_name=confused_buffer_name/>
            </#if>
            _init("${encode_element_name}", ${encode_element_name}, ${channel_parameter});
        </#if>
        <#if is_cache_obj()>
            <#if member_mappings??>
                <#list member_mappings as sub>
                    ${sub.member_method_name}(${current_buffer_name}, ${encode_element_name}.get${sub.member_name?cap_first}(), ${channel_parameter});
                </#list>
            </#if>
        </#if>
        <#if is_collection()>
            <#assign generic=member_mappings.get(0)/>
            <#assign generic_member_name = generic.member_name?uncap_first/>
            for (final ${generic.member_type.type_simple_name} ${generic_member_name} : ${encode_element_name}) {
                ${generic.member_method_name}(${current_buffer_name}, ${generic_member_name}, ${channel_parameter});
            }
        </#if>
        <#-- issue#1 如果spelMapping不是根对象或者是集合对象, 需要回收新缓冲区的writerIndex -->
        <#if is_un_root() && is_not_primitive()>
            ${buffer_parameter}.writerIndex(${buffer_parameter}.writerIndex() + ${current_buffer_name}.writerIndex());
        </#if>
    </#if>

</@builder.build_method_head>