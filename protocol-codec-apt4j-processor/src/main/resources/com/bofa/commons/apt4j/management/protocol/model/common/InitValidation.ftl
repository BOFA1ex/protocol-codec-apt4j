<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.common.InitValidation" -->
<@compress single_line=true>
    <#if is_validate()>
        <#if useComponent>
            ${resolveTypeMirror(validation_anon)?uncap_first}.validate(${buffer_parameter}, ${channel_parameter},
        <#else>
            ${resolveTypeMirror(validation_anon)?uncap_first}.INSTANCE.validate(${buffer_parameter}, ${channel_parameter},
        </#if>
    <#else>
        <#if useComponent>
            ${resolveTypeMirror(validation_anon)?uncap_first}.mapper(${buffer_parameter}, ${channel_parameter},
        <#else>
            ${resolveTypeMirror(validation_anon)?uncap_first}.INSTANCE.mapper(${buffer_parameter}, ${channel_parameter},
        </#if>
    </#if>
    <#if is_normal(validate_index())>
        ${validate_index_step()}
    <#elseif is_reverse(validate_index())>
        ${buffer_parameter}.readableBytes() + ${validate_index_step()}
    <#elseif is_model(validate_index())>
        ((${resolveTypeMirror(validate_index_model())}) ChannelCodecContextUtils.getVariable("${validate_index_model_key()}", ${channel_parameter})).get${validate_index_model_prop()?cap_first}()
    </#if>
    ,
    <#if is_normal(validate_length())>
        ${validate_length_step()}
    <#elseif is_reverse(validate_length())>
        ${buffer_parameter}.readableBytes() + ${validate_length_step()}
    <#elseif is_model(validate_length())>
        ((${resolveTypeMirror(validate_length_model())}) ChannelCodecContextUtils.getVariable("${validate_length_model_key()}", ${channel_parameter})).get${validate_length_model_prop()?cap_first}()
    </#if>
    ,
    <#if is_normal(mapper_index())>
        ${mapper_index_step()}
    <#elseif is_reverse(mapper_index())>
        ${buffer_parameter}.writerIndex() + ${mapper_index_step()}
    <#elseif is_model(mapper_index())>
        ((${resolveTypeMirror(mapper_index_model())}) ChannelCodecContextUtils.getVariable("${mapper_index_model_key()}", ${channel_parameter})).get${mapper_index_model_prop()?cap_first}()
    </#if>
    ,
    <#if is_normal(mapper_length())>
        ${mapper_length_step()}
    <#elseif is_reverse(mapper_length())>
        ${buffer_parameter}.writerIndex() + ${mapper_length_step()}
    <#elseif is_model(mapper_length())>
        ((${resolveTypeMirror(mapper_length_model())}) ChannelCodecContextUtils.getVariable("${mapper_length_model_key()}", ${channel_parameter})).get${mapper_length_model_prop()?cap_first}()
    </#if><#rt>
    <@compress single_line=true>
        <#if validation_parameters()?? && validation_parameters()?has_content>, <#list validation_parameters() as parameter>"${parameter}"<#if parameter_has_next>, </#if></#list></#if>
    </@compress>
    <#lt>);
</@compress>

