<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#import "../../../internal/lib/ModelBuilder.ftl" as builder/>
<#assign current_buffer_name=buffer_parameter/>
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.decode.ProtocolDecodeInternalMethod" -->
<@builder.build_method_head method_head>
    <#if is_un_root()>
        <#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.common.InitConditionIndexLength" -->
        <@includeModel object=part0/>
        <#-- readSlice -->
        final ByteBuf ${confused_buffer_name} = readSlice(${buffer_parameter}, index, length);
        <#assign current_buffer_name=confused_buffer_name/>
    </#if>
    <#if resolveTypeMirror(part0.convert_anon)??>
    <#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.decode.ProtocolDecodeConvertMethod" -->
        <@includeModel object=part1/>
    <#else>
        <#if is_not_primitive()>
            <#if is_cache_obj()>
                ${decode_type.type_simple_name} ${decode_element_name} = new ${decode_type.type_simple_name}();
            <#elseif is_collection()>
                ${decode_type.type_simple_name} ${decode_element_name} = new ${part0.convert_anon.parameters()[0]}<>();
            </#if>
            _init("${decode_element_name}", ${decode_element_name}, ${channel_parameter});
        </#if>
        <#if is_cache_obj()>
            <#if member_mappings??>
                <#list member_mappings as sub>
                    ${sub.member_type.type_simple_name} ${sub.member_name} = ${sub.member_method_name}(${current_buffer_name}, ${channel_parameter});
                    ${decode_element_name}.set${sub.member_name?cap_first}(${sub.member_name});
                </#list>
            </#if>
        </#if>
        <#if is_collection()>
            <#assign generic=member_mappings.get(0)/>
            while (${current_buffer_name}.isReadable()) {
                ${generic.member_type.type_simple_name} ${generic.member_name} = ${generic.member_method_name}(${current_buffer_name}, ${channel_parameter});
                ${decode_element_name}.add(${generic.member_name});
            }
        </#if>
    </#if>
     <#if is_un_root()>
         // 这里主要是考量到一些字段(可变长, 比如mqtt的packetLength)
         ${buffer_parameter}.readerIndex(index + ${current_buffer_name}.readerIndex());
     </#if>
    return ${decode_element_name};
</@builder.build_method_head>
