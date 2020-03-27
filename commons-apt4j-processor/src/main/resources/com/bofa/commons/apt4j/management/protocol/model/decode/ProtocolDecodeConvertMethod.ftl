<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.decode.ProtocolDecodeConvertMethod" -->
<@compress single_line=true>
    <#if useComponent>
        ${decode_type.type_simple_name} ${decode_type_name} = ${resolveTypeMirror(convert_anon)?uncap_first}.decode(${confused_buffer_name}, ${channel_parameter}<#rt>
    <#else>
        ${decode_type.type_simple_name} ${decode_type_name} = ${resolveTypeMirror(convert_anon)}.INSTANCE.decode(${confused_buffer_name}, ${channel_parameter}<#rt>
    </#if>
    <@compress single_line=true>
        <#if convert_anon.parameters()?has_content>, <#list convert_anon.parameters() as parameter>"${parameter}"<#if parameter_has_next>, </#if></#list></#if>
    </@compress>
    <#lt>);
</@compress>

