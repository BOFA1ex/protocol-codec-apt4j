<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.encode.ProtocolEncode$0" -->
<#import "../../../internal/lib/ModelBuilder.ftl" as builder/>
<@builder.build_method_head method_head>
    <@includeModel object=init_buffer/>
    // 基准writeIndex standardWriteIndex
    final int standardWriterIndex = buffer.writerIndex();
    ${encode_method_name}(buffer, ${encode_parameter}, ${channel_parameter}, standardWriterIndex);
    <#if init_validations?has_content>
        // 组装校验码/混淆数据域
        <#list init_validations as init_validation>
            <@includeModel object=init_validation/>
        </#list>
    </#if>
    return buffer;
</@builder.build_method_head>