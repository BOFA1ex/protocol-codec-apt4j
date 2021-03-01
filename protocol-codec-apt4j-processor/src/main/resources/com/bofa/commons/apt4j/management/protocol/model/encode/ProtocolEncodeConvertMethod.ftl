<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.encode.ProtocolEncodeConvertMethod" -->
<@compress single_line=true>
    <#if useComponent>
        final ByteBuf ${confused_buffer_name} = ${resolveTypeMirror(convert_anon)?uncap_first}.encode(${encode_element_name}, length, ${channel_parameter}<#rt>
    <#else>
        final ByteBuf ${confused_buffer_name} = ${resolveTypeMirror(convert_anon)}.INSTANCE.encode(${encode_element_name}, length, ${channel_parameter}<#rt>
    </#if>
    <@compress single_line=true>
        <#if convert_parameters()?has_content>, <#list convert_parameters() as parameter>"${parameter}"<#if parameter_has_next>, </#if></#list></#if>
    </@compress>
    <#lt>);
</@compress>

buffer.writerIndex(index);
buffer.forEachByte(index, ${confused_buffer_name}.writerIndex(), value -> {
    buffer.writeByte(${confused_buffer_name}.readByte() + value);
    return true;
});
