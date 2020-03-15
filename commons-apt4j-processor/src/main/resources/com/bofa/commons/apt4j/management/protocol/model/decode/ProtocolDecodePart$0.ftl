<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.decode.ProtocolDecodePart$0" -->
<@compress single_line=true>
    ${decode_type.type_name} ${decode_type_name} = ${convertAnonModel.convert_method}.INSTANCE.decode(${confused_buffer_name}, ${channel_parameter}<#rt>
    <@compress single_line=true>
        <#if convertAnonModel.parameters?has_content>, <#list convertAnonModel.parameters as parameter>"${parameter}"<#if parameter_has_next>, </#if></#list></#if>
    </@compress>
    <#lt>);
</@compress>

