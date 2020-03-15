<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.encode.ProtocolEncodePart$0" -->
<@compress single_line=true>
    final ByteBuf ${confused_buffer_name} = ${convertAnonModel.convert_method}.INSTANCE.encode(${encode_type_name}, length, ${channel_parameter}<#rt>
    <@compress single_line=true>
        <#if convertAnonModel.parameters?has_content>, <#list convertAnonModel.parameters as parameter>"${parameter}"<#if parameter_has_next>, </#if></#list></#if>
    </@compress>
    <#lt>);
</@compress>

buffer.writerIndex(index);
buffer.forEachByte(index, length, value -> {
    buffer.writeByte(${confused_buffer_name}.readByte() + value);
    return true;
});
