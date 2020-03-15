<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.common.InitBuffer" -->
<@compress single_line=true>
    final ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer(${initial_capacity}, ${max_capacity});
</@compress>

