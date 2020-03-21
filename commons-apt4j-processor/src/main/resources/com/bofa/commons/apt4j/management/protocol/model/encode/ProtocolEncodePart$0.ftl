<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.encode.ProtocolEncodePart$0" -->
<@compress single_line=true>
    <#if useComponent>
        final ByteBuf ${confused_buffer_name} = ${convert_anon_model.convert_method_simple?uncap_first}.encode(${encode_type_name}, length, ${channel_parameter}<#rt>
    <#else>
        final ByteBuf ${confused_buffer_name} = ${convert_anon_model.convert_method_simple}.INSTANCE.encode(${encode_type_name}, length, ${channel_parameter}<#rt>
    </#if>
    <@compress single_line=true>
        <#if convert_anon_model.parameters?has_content>, <#list convert_anon_model.parameters as parameter>"${parameter}"<#if parameter_has_next>, </#if></#list></#if>
    </@compress>
    <#lt>);
</@compress>

buffer.writerIndex(index);
buffer.forEachByte(index, length, value -> {
    buffer.writeByte(${confused_buffer_name}.readByte() + value);
    return true;
});
