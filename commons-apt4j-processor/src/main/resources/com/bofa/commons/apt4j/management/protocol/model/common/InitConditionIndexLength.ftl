<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.common.InitConditionIndexLength" -->
<#assign condition=convert_anon.condition()/>
<#assign index=convert_anon.index()/>
<#assign length=convert_anon.length()/>

<#assign condition_model=condition.model()/>
<#assign index_model=index.model()/>
<#assign length_model=length.model()/>
<#if condition.model().key()?? && !condition.model().key().equals("anon")>
    if(!(((${resolveTypeMirror(condition_model)})ChannelCodecContextUtils.getVariable("${condition.model().key()}", ${channel_parameter})).get${condition.model().prop()?cap_first}() ${condition.operator()} ${condition.compareValue()})) {
        return<#if is_decode()> null</#if>;
    }
</#if>
<#if is_decode()>
    <@compress single_line=true>
    <#if is_normal(index)>
        int index = ${buffer_parameter}.readerIndex() + (${index.step()});
    <#elseif is_reverse(index)>
        int index = ${buffer_parameter}.readableBytes() + (${index.step()});
    <#elseif is_model(index)>
        int index = ((${resolveTypeMirror(index_model)}) ChannelCodecContextUtils.getVariable("${index_model.key()}", ${channel_parameter})).get${index_model.prop()?cap_first}();
    </#if>
    </@compress>

    <@compress single_line=true>
    <#if is_normal(length)>
        int length = (${length.step()});
    <#elseif is_reverse(length)>
        int length = ${buffer_parameter}.readableBytes() + (${length.step()});
    <#elseif is_model(length)>
        int length = ((${resolveTypeMirror(length_model)}) ChannelCodecContextUtils.getVariable("${length_model.key()}", ${channel_parameter})).get${length_model.prop()?cap_first}();
    </#if>
    </@compress>
<#else>
    <@compress single_line=true>
    <#if is_normal(index)>
        int index = ${buffer_parameter}.writerIndex() + (${index.step()});
    <#elseif is_reverse(index)>
        int index = ${buffer_parameter}.writeableBytes() + (${index.step()});
    <#elseif is_model(index)>
        int index = ((${resolveTypeMirror(index_model)}) ChannelCodecContextUtils.getVariable("${index_model.key()}", ${channel_parameter})).get${index_model.prop()?cap_first}();
    </#if>
    </@compress>

    <@compress single_line=true>
    <#if is_normal(length)>
        int length = (${length.step()});
    <#elseif is_reverse(length)>
        int length = ${buffer_parameter}.writableBytes() + (${length.step()});
    <#elseif is_model(length)>
        int length = ((${resolveTypeMirror(length_model)}) ChannelCodecContextUtils.getVariable("${length_model.key()}", ${channel_parameter})).get${length_model.prop()?cap_first}();
    </#if>
    </@compress>
</#if>

