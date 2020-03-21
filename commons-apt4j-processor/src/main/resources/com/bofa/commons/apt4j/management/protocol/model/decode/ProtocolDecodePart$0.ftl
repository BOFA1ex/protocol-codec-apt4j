<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.decode.ProtocolDecodePart$0" -->
<@compress single_line=true>
    <#if useComponent>
        ${decode_type.type_name} ${decode_type_name} = ${convert_anon_model.convert_method_simple?uncap_first}.decode(${confused_buffer_name}, ${channel_parameter}<#rt>
    <#else>
        ${decode_type.type_name} ${decode_type_name} = ${convert_anon_model.convert_method_simple}.INSTANCE.decode(${confused_buffer_name}, ${channel_parameter}<#rt>
    </#if>
    <@compress single_line=true>
        <#if convert_anon_model.parameters?has_content>, <#list convert_anon_model.parameters as parameter>"${parameter}"<#if parameter_has_next>, </#if></#list></#if>
    </@compress>
    <#lt>);
</@compress>

