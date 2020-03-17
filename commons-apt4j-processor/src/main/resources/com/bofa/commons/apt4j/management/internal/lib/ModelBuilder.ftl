<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- model common structure build-factory -->
<#-- type head -->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.internal.model.TypeHeadModel" -->
<#macro build_type_head args>
${args.modifier} class ${args.class_type.type_name} <#if args.needImplement>implements ${args.interface_type.type_name}</#if><#if args.needExtends>extends ${args.abstract_type.type_name}</#if>{
    <#nested>
}
</#macro>
<#-- method head -->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.internal.model.MethodHeadModel" -->
<#macro build_method_head args>
<#if args.is_override()>@Override</#if>
${args.modifier} ${args.return_type.type_name} ${args.method_name}(<@compress single_line=true><#list args.method_parameters as method_parameter><@build_method_parameter method_parameter/><#if method_parameter_has_next>, </#if></#list></@compress>){
    <#nested>
}
</#macro>
<#-- method parameter -->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.internal.model.MethodParameterModel" -->
<#macro build_method_parameter args>
${args.param_type.type_name} ${args.param_name}<#rt>
</#macro>