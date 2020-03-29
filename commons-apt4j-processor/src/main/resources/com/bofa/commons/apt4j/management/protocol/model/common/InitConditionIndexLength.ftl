<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.common.InitConditionIndexLength" -->
<#if condition_model_key()?? && !condition_model_key().equals("anon")>
    if(!(((${resolveTypeMirror(condition_model())})ChannelCodecContextUtils.getVariable("${condition_model_key()}", ${channel_parameter})).get${condition_model_prop()?cap_first}() ${condition_operator()} ${condition_compare_value()})) {
        return<#if is_decode()> null</#if>;
    }
</#if>
<#if is_decode()>
    <@compress single_line=true>
    <#if is_normal(index())>
        int index = ${buffer_parameter}.readerIndex() + (${index_step()});
    <#elseif is_reverse(index())>
        int index = ${buffer_parameter}.readableBytes() + (${index_step()});
    <#elseif is_model(index())>
        int index = ((${resolveTypeMirror(index_model())}) ChannelCodecContextUtils.getVariable("${index_model_key()}", ${channel_parameter})).get${index_model_prop()?cap_first}();
    </#if>
    </@compress>

    <@compress single_line=true>
    <#if is_normal(length())>
        int length = (${length_step()});
    <#elseif is_reverse(length())>
        int length = ${buffer_parameter}.readableBytes() + (${length_step()});
    <#elseif is_model(length())>
        int length = ((${resolveTypeMirror(length_model())}) ChannelCodecContextUtils.getVariable("${length_model_key()}", ${channel_parameter})).get${length_model_prop()?cap_first}();
    </#if>
    </@compress>
<#else>
    <@compress single_line=true>
    <#if is_normal(index())>
        int index = ${buffer_parameter}.writerIndex() + (${index_step()});
    <#elseif is_reverse(index())>
        int index = ${buffer_parameter}.writeableBytes() + (${index_step()});
    <#elseif is_model(index())>
        int index = ((${resolveTypeMirror(index_model())}) ChannelCodecContextUtils.getVariable("${index_model_key()}", ${channel_parameter})).get${index_model_prop()?cap_first}();
    </#if>
    </@compress>

    <@compress single_line=true>
    <#if is_normal(length())>
        int length = (${length_step()});
    <#elseif is_reverse(length())>
        int length = ${buffer_parameter}.writableBytes() + (${length_step()});
    <#elseif is_model(length())>
        int length = ((${resolveTypeMirror(length_model())}) ChannelCodecContextUtils.getVariable("${length_model_key()}", ${channel_parameter})).get${length_model_prop()?cap_first}();
    </#if>
    </@compress>
</#if>

