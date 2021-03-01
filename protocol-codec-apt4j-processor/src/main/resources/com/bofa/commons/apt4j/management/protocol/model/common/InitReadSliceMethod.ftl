<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#import "../../../internal/lib/Log.ftl" as logger/>
<#import "../../../internal/lib/ModelBuilder.ftl" as builder/>
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.common.InitReadSliceMethod" -->
<@builder.build_method_head method_head>
    try {
        return ${buffer_parameter}.slice(${reader_index_parameter}, ${length_parameter});
    } catch(IndexOutOfBoundsException e){
    <@logger.error ext=["e.getMessage()"]>
        <#lt>${log_message}<#rt>
    </@logger.error >
        // 尝试用不完整的数据包解析
        return ${buffer_parameter}.slice(${reader_index_parameter}, ${buffer_parameter}.readableBytes());
    }
</@builder.build_method_head>