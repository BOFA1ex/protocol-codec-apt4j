<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#import "../../../internal/lib/ModelBuilder.ftl" as builder/>
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.common.InitChannelCodecContextMethod" -->
<@builder.build_method_head method_head>
    ChannelCodecContextUtils.setVariable(objName, obj, channel);
    <#list cache_vars as cache_var>
        ChannelCodecContextUtils.setVariable(objName + "${cache_var.key()}", ${cache_var.value()}, channel);
    </#list>
</@builder.build_method_head>