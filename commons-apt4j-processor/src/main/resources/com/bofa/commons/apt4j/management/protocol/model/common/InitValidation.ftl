<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.common.InitValidation" -->
<@compress single_line=true>
    <#if is_validate()>
        <#if useComponent>
            ${validate_simple_name?uncap_first}.validate(${buffer_parameter}, ${channel_parameter},
        <#else>
            ${validate_simple_name}.INSTANCE.validate(${buffer_parameter}, ${channel_parameter},
        </#if>
    <#else>
        <#if useComponent>
            ${validate_simple_name?uncap_first}.mapper(${buffer_parameter}, ${channel_parameter},
        <#else>
            ${validate_simple_name}.INSTANCE.mapper(${buffer_parameter}, ${channel_parameter},
        </#if>
    </#if>
    "${validate_index}",
    "${validate_length}",
    "${mapper_index}",
    "${mapper_length}"<#rt>
    <@compress single_line=true>
        <#if anon_params?? && anon_params?has_content>, <#list anon_params as anon_param>"${anon_param}"<#if anon_param_has_next>, </#if></#list></#if>
    </@compress>
    <#lt>);
</@compress>

