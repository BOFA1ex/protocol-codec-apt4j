<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#import "../../../internal/lib/ModelBuilder.ftl" as builder/>
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.decode.ProtocolOverrideDecodeMethod" -->
<@builder.build_method_head method_head>
    <#if init_validations?has_content>
        // 校验缓冲区报文
        <#list init_validations as init_validation>
            <@includeModel object=init_validation/>
        </#list>
    </#if>
    ${decode_type.type_simple_name} ${decode_element_name} = null;
    try {
        ${decode_element_name} = ${decode_method_name}(${buffer_parameter}, ${channel_parameter});
    } catch(Exception e) {
        <#if resolveTypeMirror(decode_anon)??>
            <@compress single_line=true>
                <#if useComponent>
                    ${decode_element_name} = (${decode_type.type_simple_name})${resolveTypeMirror(decode_anon)?uncap_first}.resolveException(${decode_type.type_simple_name}.class, e, ${channel_parameter});
                <#else>
                    ${decode_element_name} = (${decode_type.type_simple_name})${resolveTypeMirror(decode_anon)}.INSTANCE.resolveException(${decode_type.type_simple_name}.class, e, ${channel_parameter});
                </#if>
            </@compress>
        <#else>
            e.printStackTrace();
        </#if>
    }
    return ${decode_element_name};
</@builder.build_method_head>
