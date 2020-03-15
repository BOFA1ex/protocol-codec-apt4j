<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.common.ProtocolSpelProcessExpr" -->
if (!ChannelSpelContextUtils.processExprAndGet("${convert_anon_model.condition}", ${channel_parameter}, boolean.class)) {
    return<#if standard_index_parameter.contentEquals("standardReaderIndex")> null</#if>;
}
int index = ChannelSpelContextUtils.processExprAndGet("${convert_anon_model.index} + " + ${standard_index_parameter}, ${channel_parameter}, int.class);
int length = ChannelSpelContextUtils.processExprAndGet("${convert_anon_model.length}", ${channel_parameter}, int.class);
