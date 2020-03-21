<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.common.InitResolveException" -->
<@compress single_line=true>
    <#if useComponent>
        ${decode_element_name} = (${decode_type.type_simple_name})${resolve_exception_simple_name?uncap_first}.resolveException(${decode_type.type_simple_name}.class, e, ${channel_parameter});
    <#else>
        ${decode_element_name} = (${decode_type.type_simple_name})${resolve_exception_simple_name}.INSTANCE.resolveException(${decode_type.type_simple_name}.class, e, ${channel_parameter});
    </#if>
</@compress>

